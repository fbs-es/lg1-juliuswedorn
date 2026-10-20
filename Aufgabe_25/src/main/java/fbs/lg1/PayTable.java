package fbs.lg1;

/**
 * Defines payment-related operations for staff members,
 * specifically providing a method to modify salaries.
 */
public interface PayTable {

    /**
     * Changes the salary by a specified amount.
     *
     * @param salary the amount by which to change the salary (can be positive or negative)
     * @return {@code true} if the salary was successfully changed; {@code false} otherwise
     */
    boolean changeSalary(float salary);
}