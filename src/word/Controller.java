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
import javafx.scene.image.ImageView ;

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
    public TextField textfieldPesel;
    @FXML
    public TextField textfieldName;
    @FXML
    public TextField textfieldSurname;
    @FXML
    public Button buttonStart;
    @FXML
    public Button buttonMainExit;
    @FXML
    public Pane mainPane;

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
    public Text textScore, textPassed;
    @FXML
    public Button buttonSummaryExit;

    Parent root;
    private Stage primaryStage;
    Socket socket;
    List<Question> questions = new ArrayList<>();

    public Controller() {
    }

    @FXML
    void initialize() throws IOException, ClassNotFoundException {
        buttonStart.setOnAction(new ButtonEventHandler());
        buttonMainExit.setOnAction(new ButtonEventHandler());
    }


    public void showAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Uwaga!");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public void start() throws IOException, ClassNotFoundException {
    getQuestions();

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
                    try {
                        changeScene(buttonStart, "question");
                        loadQuestions();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (source == buttonMainExit) {
                closeScene(buttonMainExit);
            }
        }
    }

    public void getQuestions() {

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = null;
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/WORD", "postgres", "postgres");
            Statement statement = connection.createStatement();

            ResultSet rset = statement.executeQuery("SELECT * FROM questions ORDER BY RANDOM() LIMIT 5");

            while (rset.next()) {
                Question question = new Question();
                question.id = rset.getInt("id");
                question.answer = rset.getInt("answer");
                question.text = rset.getString("text");
                question.photo = rset.getString("photo");

                questions.add(question);
                System.out.println(question);
            }


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

    public void loadQuestions() {
        String questionText = questions.get(0).getText();
        textAreaQuestion.setText(questionText);
        File file = new File("https://drive.google.com/open?id=18G61plcGfjEPMV81DxvsSMSRsR7b3708");
        Image image = new Image(file.toURI().toString());
        imageViewPicture.setImage(image);
    }

    public void changeScene(Button from, String to) throws IOException {
        Stage stage1 = (Stage) from.getScene().getWindow();
        stage1.close();
        FXMLLoader fxmlLoader = new FXMLLoader();

        if (to == "main") {
            fxmlLoader = new FXMLLoader(getClass().getResource("styles/questionWindow.fxml"));
        } else if (to == "question") {
            fxmlLoader = new FXMLLoader(getClass().getResource("styles/questionWindow.fxml"));
        } else if (to == "summary") {
            fxmlLoader = new FXMLLoader(getClass().getResource("styles/summaryWindow.fxml"));
        }

        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("WORD Kraków - Egzamin na prawo jazdy 2018");
        stage.setScene(new Scene(root1, 600, 400));
        stage.show();

    }

    public void closeScene(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

}






