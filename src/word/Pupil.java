package word;

public class Pupil {
    long pesel;
    String name, surname;

    public Pupil(long pesel, String name, String surname) {
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
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
