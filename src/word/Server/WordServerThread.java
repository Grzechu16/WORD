package word.Server;

import word.Answer;
import word.Question;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WordServerThread extends Thread {
    Socket mySocket;
    int id, answer;
    String text, photo;

    public WordServerThread(Socket socket) {
        super();
        mySocket = socket;
    }
/**
    public void run() {
        Connection connection;
        List<Question> questions = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/WORD", "postgres", "postgres");
            Statement statement = connection.createStatement();

            ResultSet rset = statement.executeQuery("SELECT * FROM pytania ORDER BY RAND() LIMIT 5");

            while (rset.next()) {

                id = rset.getInt("id");
                answer = rset.getInt("answer");
                text = rset.getString("text");
                photo = rset.getString("photo");

                Question question = new Question(id, text, photo, answer);
                questions.add(question);
            }
            ObjectOutputStream output = new ObjectOutputStream(mySocket.getOutputStream());
            for (int i = 0; i < 5; i++) {
                output.writeObject(questions.get(i));
            }


        } catch (SQLException ex) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
 */
}
