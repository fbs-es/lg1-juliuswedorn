package fbs.lg1;

public class Main {
    private static CityGlideApp cityGlideApp;

    public static void main(String[] args) {
        startApp();
    }

    private static void startApp() {
        newCityGlideApp();
        cityGlideApp.addAccount(new User("Helmut","helmut@proton.me",12.53f));
        cityGlideApp.addAccount(new User("Flora","flora@outlock.com",11.30f));
        cityGlideApp.addScooter(new Scooter());
        cityGlideApp.addScooter(new Scooter(2,false));
        cityGlideApp.addScooter(new Scooter(16,true));
        cityGlideApp.genCityGlideApp();
    }

    public static CityGlideApp newCityGlideApp() {
        return cityGlideApp = new CityGlideApp();
    }

    public static CityGlideApp getCityGlideApp() {
        return cityGlideApp;
    }
}
