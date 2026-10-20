package fbs.lg1;

/**
 * Represents a regular employee, extending the abstract {@link Staff} class.
 * Contains additional information regarding the hiring year and holiday entitlement.
 */
public class Employee extends Staff {

    private int HiringYear;
    private int HolidayEntitlement;

    /**
     * Constructs a new Employee with the specified personal details, salary, hiring year, and holiday entitlement.
     *
     * @param FirstName          the first name of the employee
     * @param LastName           the last name of the employee
     * @param salary             the initial salary of the employee
     * @param HiringYear         the year the employee was hired
     * @param HolidayEntitlement the holiday entitlement days for the employee
     */
    Employee(String FirstName, String LastName, float salary, int HiringYear, int HolidayEntitlement) {
        super(FirstName, LastName, salary);
        this.HiringYear = HiringYear;
        this.HolidayEntitlement = HolidayEntitlement;
    }

    /**
     * Changes the salary for the employee by adding the specified amount.
     * Ensures that the resulting salary does not drop below or equal to zero.
     *
     * @param salary the amount to adjust the current salary by
     * @return {@code true} if the salary change was successful;
     *         {@code false} if the resulting salary would be less than or equal to zero
     */
    @Override
    public boolean changeSalary(float salary) {
        // implement different logic here if needed
        float newSalary = super.salary + salary;
        if (newSalary <= 0) return false;
        super.salary = newSalary;
        return true;
    }
}