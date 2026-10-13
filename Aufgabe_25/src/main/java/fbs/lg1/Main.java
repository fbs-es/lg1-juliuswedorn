package fbs.lg1;

public class Main {
    private static StaffManagementPortal portal;

    public static void main(String[] args) {
        startApp();
    }

    public static void startApp() {
        newApp();

        portal.addApprentices(new Apprentices("Manuel","Berger",2100,"Wood Tester",2));
        portal.addEmployee(new Employee("Karin","Hauser",1000,1984,10));
        portal.addTemporaryWorkers(new TemporaryWorkers("Francisca","Schuldorf",2700,new WorkingTime(2025,20000000)));
        portal.genApp();

    }

    public static void newApp() {
        portal = new StaffManagementPortal();
    }

    public static StaffManagementPortal getApp() {
        return portal;
    }
}