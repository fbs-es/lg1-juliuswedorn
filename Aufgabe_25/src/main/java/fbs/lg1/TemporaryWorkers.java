package fbs.lg1;

/**
 * Represents a temporary worker, extending the abstract {@link Staff} class.
 * Contains information about their working time arrangements.
 */
public class TemporaryWorkers extends Staff {

    private WorkingTime workingTime;

    /**
     * Constructs a new TemporaryWorker with the specified personal details, salary, and working time.
     *
     * @param FirstName   the first name of the temporary worker
     * @param LastName    the last name of the temporary worker
     * @param salary      the initial salary of the temporary worker
     * @param workingTime the working time model or configuration assigned
     */
    TemporaryWorkers(String FirstName, String LastName, float salary, WorkingTime workingTime) {
        super(FirstName, LastName, salary);
        this.workingTime = workingTime;
    }

    /**
     * Changes the salary for the temporary worker by adding the specified amount.
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