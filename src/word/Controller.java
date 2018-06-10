package word;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

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
    public Button aButton;
    @FXML
    public Button bButton;
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
    public Text textScore, textPassed;
    @FXML
    public Button buttonSummaryExit;

    Parent root;
    private Stage primaryStage;
    Socket socket;
    List<Question> questions = new ArrayList<>();
    public int number = 0;

    public Controller() {
    }

    @FXML
    void initialize() throws IOException, ClassNotFoundException {
        paneMain.setVisible(true);
        paneQuestion.setVisible(false);
        getQuestions();
        buttonStart.setOnAction(new ButtonEventHandler());
        buttonMainExit.setOnAction(new ButtonEventHandler());
        buttonNext.setOnAction(new ButtonEventHandler());
        buttonQuestionExit.setOnAction(new ButtonEventHandler());
    }


    public void showAlert(String text) {
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
                    int pesel = Integer.parseInt(textfieldPesel.getText());
                    String name = textfieldName.getText();
                    String surname = textfieldSurname.getText();
                    try {
                        addPupil(pesel, name, surname);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    loadQuestions(number);
                    number++;
                }
            }
            if (source == buttonNext) {
                if (number >= 10) {
                    paneQuestion.setVisible(false);
                } else {
                    loadQuestions(number);
                    number++;
                }
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

    public void addPupil(long pesel, String name, String surname) throws SQLException, ClassNotFoundException {
        Pupil pupil = new Pupil(pesel, name, surname);
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection;
        connection = DriverManager.getConnection("jdbc:mysql://localhost/" + "word" + "?user=root");
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO pupil (name, pesel, surname) VALUES (?,?,?)");
        preparedStatement.setString(1, pupil.getName());
        preparedStatement.setLong(2, pupil.getPesel());
        preparedStatement.setString(3, pupil.getSurname());
        preparedStatement.executeUpdate();
    }

    public void loadQuestions(int number) {
        paneMain.setVisible(false);
        paneQuestion.setVisible(true);
        String questionText = questions.get(number).getText();
        textAreaQuestion.setText(questionText);

        imageViewPicture = new ImageView(new Image(getClass().getResourceAsStream("Images/" + questions.get(number).getId() + ".png")));
        imageViewPicture.setFitWidth(241);
        imageViewPicture.setFitHeight(205);
        imageViewPicture.setX(326);
        imageViewPicture.setY(26);
        paneQuestion.getChildren().add(imageViewPicture);
    }

    public void checkAnswer(){

    }

    public void closeScene(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

}






