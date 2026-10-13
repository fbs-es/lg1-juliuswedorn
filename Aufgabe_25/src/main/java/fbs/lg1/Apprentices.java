package fbs.lg1;

public class Apprentices extends Staff{
    private String SkilledOccupation;
    private int YearOfTraining;

    Apprentices(String FirstName, String LastName, float Salary,String SkilledOccupation, int YearOfTraining) {
        super(FirstName, LastName, Salary);
        this.SkilledOccupation=SkilledOccupation;
        this.YearOfTraining = YearOfTraining;
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
