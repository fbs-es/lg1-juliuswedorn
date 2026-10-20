package fbs.lg1;

/**
 * Represents an apprentice, extending the abstract {@link Staff} class.
 * Contains specific details about their skilled occupation and year of training.
 */
public class Apprentices extends Staff {

    private String SkilledOccupation;
    private int YearOfTraining;

    /**
     * Constructs a new Apprentice with the specified personal details, salary, skilled occupation, and year of training.
     *
     * @param FirstName         the c name of the apprentice
     * @param LastName          the last name of the apprentice
     * @param Salary            the initial salary of the apprentice
     * @param SkilledOccupation the skilled occupation or trade being learned
     * @param YearOfTraining    the current year of training
     */
    Apprentices(String FirstName, String LastName, float Salary, String SkilledOccupation, int YearOfTraining) {
        super(FirstName, LastName, Salary);
        this.SkilledOccupation = SkilledOccupation;
        this.YearOfTraining = YearOfTraining;
    }

    /**
     * Changes the salary for the apprentice by adding the specified amount.
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