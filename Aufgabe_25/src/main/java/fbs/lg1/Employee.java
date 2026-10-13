package fbs.lg1;

public class Employee extends Staff{
    private int HiringYear;
    private int HolidayEntitlement;

    Employee(String FirstName, String LastName, float salary, int HiringYear, int HolidayEntitlement) {
        super(FirstName, LastName, salary);
        this.HiringYear=HiringYear;
        this.HolidayEntitlement=HolidayEntitlement;
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
