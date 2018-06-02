package word;

import java.io.Serializable;

public class Question implements Serializable {
    String text, photo;
    int id, answer;

    public Question(){

    }

    public Question(int id, String text, String photo, int answer){
        this.id = id;
        this.text = text;
        this.photo = photo;
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "text='" + text + '\'' +
                ", photo='" + photo + '\'' +
                ", id=" + id +
                ", answer=" + answer +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

