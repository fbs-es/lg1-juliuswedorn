package fbs.lg1;

public class Main {

    public static void main(String[] args) {
        Kinosaal meinSaal = new Kinosaal();
        boolean check = true;
        while (check) {
            check = meinSaal.menu();
        }

    }
}
