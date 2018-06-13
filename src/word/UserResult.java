package word;

public class UserResult {
    int score;
    long pesel;

    public UserResult() {
    }

    public UserResult(int score, long pesel) {
        this.score = score;
        this.pesel = pesel;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getPesel() {
        return pesel;
    }

    public void setPesel(long pesel) {
        this.pesel = pesel;
    }
}
