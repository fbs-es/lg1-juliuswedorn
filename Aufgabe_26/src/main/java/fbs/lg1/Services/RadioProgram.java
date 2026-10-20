package fbs.lg1.Services;

public class RadioProgram {
    String Name;
    RadioBroadcast RadioBroadcast;
    public RadioProgram(String name, RadioBroadcast radioBroadcast) {
        initialize(name, radioBroadcast);
    }
    private void initialize(String name,RadioBroadcast radioBroadcast) {
        this.Name = name;
        this.RadioBroadcast = radioBroadcast;
    }

    public String getName() {
        return Name;
    }
    public RadioBroadcast getRadioBroadcast() {
        return RadioBroadcast;
    }
}
