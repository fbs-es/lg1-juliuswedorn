package fbs.lg1;

public class TemporaryWorkers extends Staff {
    private WorkingTime workingTime;


    TemporaryWorkers(String FirstName, String LastName, float salary,WorkingTime workingTime) {
        super(FirstName, LastName, salary);
        this.workingTime = workingTime;
    }

    @Override
    public boolean changeSalary(float salary) {
        //implement different logic here
        float newSalary = super.salary + salary;
        if (newSalary <= 0) return false;
        super.salary = newSalary;
        return true;
    }
}
