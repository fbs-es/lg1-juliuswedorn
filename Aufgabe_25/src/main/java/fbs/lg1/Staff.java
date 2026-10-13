package fbs.lg1;

import java.util.ArrayList;

public abstract class Staff implements PayTable {
    protected String firstName;
    protected String lastName;
    protected float salary;

    Staff(String firstName, String lastName, float salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
    }

    public boolean changeSalary(float salary) {
        float newSalary = this.salary + salary;
        if (newSalary <= 0) return false;
        this.salary = newSalary;
        return true;
    }

    public String getFullName(){
        return this.firstName +" "+ this.lastName;
    }
}
