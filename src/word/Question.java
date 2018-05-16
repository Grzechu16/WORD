package word;

import java.io.Serializable;

public class Question implements Serializable {
    String text, answer;
    int id;

    public Question(){}

    public Question(String text, String answer, int id) {
        this.text = text;
        this.answer = answer;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

