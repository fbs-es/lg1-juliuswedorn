package fbs.lg1.Services;

import java.util.ArrayList;
import java.util.List;

public class RadioStation {
    String Name;
    int Freq;
    List<RadioProgram> Programs = new ArrayList<>();

    public RadioStation(String name, int freq, RadioProgram program) {
        initialize(name, freq, program);
    }

    private void initialize(String name, int freq, RadioProgram program) {
        this.Name = name;
        this.Freq = freq;
        if (program != null) {
            this.Programs.add(program);
        }
    }

    public void addProgram(RadioProgram program) {
        this.Programs.add(program);
    }

    public String getName() {
        return Name;
    }

    public int getFreq() {
        return Freq;
    }

    public RadioProgram getProgram() {
        return Programs.isEmpty() ? null : Programs.get(Programs.size() - 1);
    }

    public List<RadioProgram> getPrograms() {
        return Programs;
    }
}