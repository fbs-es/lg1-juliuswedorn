package fbs.lg1;

/**
 * Represents an abstract staff member within the system.
 * Implements the {@link PayTable} interface to handle payment-related functionality.
 */
public abstract class Staff implements PayTable {

    /** The first name of the staff member. */
    protected String firstName;

    /** The last name of the staff member. */
    protected String lastName;

    /** The current salary of the staff member. */
    protected float salary;

    /**
     * Constructs a new Staff member with the specified first name, last name, and initial salary.
     *
     * @param firstName the first name of the staff member
     * @param lastName  the last name of the staff member
     * @param salary    the initial salary amount
     */
    Staff(String firstName, String lastName, float salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
    }

    /**
     * Changes the salary for the staff member by adding the specified amount.
     * The resulting salary must be strictly greater than zero.
     *
     * @param salary the amount to adjust the current salary by (can be positive or negative)
     * @return {@code true} if the salary change was successful;
     *         {@code false} if the resulting salary would be less than or equal to zero
     */
    public boolean changeSalary(float salary) {
        float newSalary = this.salary + salary;
        if (newSalary <= 0) return false;
        this.salary = newSalary;
        return true;
    }

    /**
     * Retrieves the full name of the staff member by combining the first and last name.
     *
     * @return a string containing the first name and last name separated by a space
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}