package word;

import com.sun.deploy.util.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    /**
     * Main window layout
     */
    @FXML
    public Pane paneMain;
    @FXML
    public Pane paneQuestion;
    @FXML
    public Pane paneSummary;
    @FXML
    public TextField textfieldPesel;
    @FXML
    public TextField textfieldName;
    @FXML
    public TextField textfieldSurname;
    @FXML
    public Button buttonStart;
    @FXML
    public Button buttonMainExit;


    /**
     * Question window layout
     */
    @FXML
    public TextArea textAreaQuestion;
    @FXML
    public Button buttonA;
    @FXML
    public Button buttonB;
    @FXML
    public Button buttonNext;
    @FXML
    public Button buttonQuestionExit;
    @FXML
    public RadioButton radioA;
    @FXML
    public RadioButton radioB;
    @FXML
    public ImageView imageViewPicture;

    /**
     * Summary window layout
     */
    @FXML
    public Text textScore;
    @FXML
    public Text textPassed;
    @FXML
    public Button buttonSummaryExit;

    Parent root;
    private Stage primaryStage;
    Socket socket;
    List<Question> questions = new ArrayList<>();
    List<Pupil> pupils = new ArrayList<>();
    public int number = 0;
    UserResult userResult = new UserResult();


    public Controller() {
    }

    @FXML
    void initialize() throws IOException, ClassNotFoundException {
        userResult.setScore(0);
        paneMain.setVisible(true);
        paneQuestion.setVisible(false);
        getQuestions();
        buttonStart.setOnAction(new ButtonEventHandler());
        buttonMainExit.setOnAction(new ButtonEventHandler());
        buttonNext.setOnAction(new ButtonEventHandler());
        buttonQuestionExit.setOnAction(new ButtonEventHandler());
        buttonSummaryExit.setOnAction(new ButtonEventHandler());
        buttonA.setOnAction(new ButtonEventHandler());
        buttonB.setOnAction(new ButtonEventHandler());
        radioA.setOnAction(new ButtonEventHandler());
        radioB.setOnAction(new ButtonEventHandler());
    }


    public void showAlert(String text) {
        assert text != null;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Uwaga!");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public void start() throws IOException, ClassNotFoundException {


    }

    class ButtonEventHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Object source = event.getSource();
            if (source == buttonStart) {
                if (textfieldPesel.getText().trim().isEmpty() || textfieldName.getText().trim().isEmpty() || textfieldSurname.getText().trim().isEmpty()) {
                    showAlert("Proszę wypełnić wymagane pola");
                } else {
                    long pesel = Long.parseLong(textfieldPesel.getText());
                    String name = textfieldName.getText();
                    String surname = textfieldSurname.getText();
                    userResult.setPesel(pesel);
                    try {
                        addPupil(pesel, name, surname);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    loadFirstQuestions();
                }
            }
            if (source == buttonNext) {
                checkAnswer(number);
                number++;
                if (number >= 10) {
                    paneQuestion.setVisible(false);
                    paneSummary.setVisible(true);
                    try {
                        addScore(userResult.getScore(),userResult.getPesel());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    printSummary(userResult.getScore());
                } else {
                    loadQuestions(number);
                }
            }
            if (source == buttonA) {
                radioB.setSelected(false);
                radioA.setSelected(true);
            }
            if (source == buttonB) {
                radioA.setSelected(false);
                radioB.setSelected(true);
            }
            if (source == buttonMainExit || source == buttonQuestionExit || source == buttonSummaryExit) {
                closeScene(buttonMainExit);
            }
        }
    }

    public void getQuestions() {

        int id, answer;
        String text, photo;

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection;
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/WORD", "postgres", "postgres");
            Statement statement = connection.createStatement();

            ResultSet rset = statement.executeQuery("SELECT * FROM questions ORDER BY RANDOM() LIMIT 10");

            while (rset.next()) {

                id = rset.getInt("id");
                answer = rset.getInt("answer");
                text = rset.getString("text");
                photo = rset.getString("photo");

                questions.add(new Question(id, text, photo, answer));


            }
            System.out.println(questions);


        } catch (SQLException ex) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void addScore(int score, long pesel) throws ClassNotFoundException, SQLException {
        assert score != 0;
        assert pesel != 0;
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection;
        connection = DriverManager.getConnection("jdbc:mysql://localhost/" + "word" + "?user=root");
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE pupil set score = " + score + " where pesel = " + pesel);
        preparedStatement.executeUpdate();
    }

    public void addPupil(long pesel, String name, String surname) throws SQLException, ClassNotFoundException {
        assert pesel != 0;
        Pupil pupil = new Pupil(pesel, name, surname);
        pupils.add(pupil);
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection;
        connection = DriverManager.getConnection("jdbc:mysql://localhost/" + "word" + "?user=root");
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO pupil (name, pesel, surname) VALUES (?,?,?)");
        preparedStatement.setString(1, pupil.getName());
        preparedStatement.setLong(2, pupil.getPesel());
        preparedStatement.setString(3, pupil.getSurname());
        preparedStatement.executeUpdate();
    }

    public void loadFirstQuestions() {
        paneMain.setVisible(false);
        paneQuestion.setVisible(true);
        String questionText = questions.get(0).getText();
        textAreaQuestion.setText(questionText);

        imageViewPicture = new ImageView(new Image(getClass().getResourceAsStream("Images/" + questions.get(0).getId() + ".png")));
        imageViewPicture.setFitWidth(241);
        imageViewPicture.setFitHeight(205);
        imageViewPicture.setX(326);
        imageViewPicture.setY(26);
        paneQuestion.getChildren().add(imageViewPicture);
    }

    public void loadQuestions(int number) {
        assert number != 0;
        String questionText = questions.get(number).getText();
        textAreaQuestion.setText(questionText);
        imageViewPicture.setImage(new Image(getClass().getResourceAsStream("Images/" + questions.get(number).getId() + ".png")));
    }

    public void checkAnswer(int number) {
        int answer = 0;

        if (radioA.isSelected()) {
            answer = 1;
        } else if (radioB.isSelected()) {
            answer = 2;
        }
        if (questions.get(number).getAnswer() == answer) {
            userResult.score++;
        }
    }

    public void printSummary(int score) {
        int percent = (100 * score) / 10;
        textScore.setText(score + " / 10");
        if (percent > 60) {
            textPassed.setText("Test zaliczony!");
            textPassed.setFill(Color.GREEN);
        } else {
            textPassed.setText("Test nie został zaliczony");
            textPassed.setFill(Color.RED);
        }
    }

    public void closeScene(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

}






