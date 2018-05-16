package word;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gregory on 2017-12-20.
 */
public class ServerQuestionThread extends Thread {
    Socket mySocket;

    public ServerQuestionThread(Socket socket) {
        super();
        mySocket = socket;
    }

    public void run() {
        try {
            Connection connection;

            List<Question> questionList = new ArrayList<>();
                 // List<PackageInput> packageInputs = new ArrayList<>();

            try {

                connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/WORD","postgres", "1234");
                Statement statement = connection.createStatement();

                ResultSet resultset = statement.executeQuery("SELECT * FROM questions ORDER BY RAND() LIMIT 10");

                while (resultset.next()) {
                    Question question = new Question();
                    question.id = resultset.getInt("id");
                    question.text = resultset.getString("question");
                    question.answer = resultset.getString("answer");
                    questionList.add(question);
                    System.out.println(question);
                }

            } catch (SQLException ex) {
            }





            mySocket.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}

