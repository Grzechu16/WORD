package word;

public class Pupil {
    long pesel;
    String name, surname;
    int score;

    public Pupil(long pesel, String name, String surname) {
        if(name == null){
            throw new IllegalArgumentException("You cannot create Pupil without name");
        }

        if(surname == null){
            throw new IllegalArgumentException("You cannot create Pupil without surname");
        }

        if(pesel == 0){
            throw new IllegalArgumentException("You cannot create Pupil without pesel");
        }

        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
