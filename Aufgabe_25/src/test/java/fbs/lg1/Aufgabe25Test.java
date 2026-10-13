package fbs.lg1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Aufgabe25Test {

    private StaffManagementPortal SMP;
    private Apprentices apprentice;
    private Employee employee;
    private TemporaryWorkers tempWorker;

    @BeforeEach
    void setUp() {
        Main.newApp();
        SMP = Main.getApp();

        tempWorker = new TemporaryWorkers("Huber", "Meier", 894, new WorkingTime(2025, 20000000));
        employee = new Employee("Karin", "Hauser", 1000, 1984, 10);
        apprentice = new Apprentices("Ines", "Riser", 299, "Trying to Teach", 800);

        SMP.addTemporaryWorkers(tempWorker);
        SMP.addEmployee(employee);
        SMP.addApprentices(apprentice);
    }

    @Test
    @DisplayName("Check if Salary can be negative")
    void checkIfSalaryCanBeNegative() {
        assertThat(apprentice.changeSalary(-300)).isFalse();
        assertThat(employee.changeSalary(-1001)).isFalse();
        assertThat(tempWorker.changeSalary(-895)).isFalse();
    }

    @Test
    @DisplayName("Salary of exactly 0 is rejected")
    void salaryCannotBecomeZero() {
        assertThat(apprentice.changeSalary(-299)).isFalse();
        assertThat(employee.changeSalary(-1000)).isFalse();
        assertThat(tempWorker.changeSalary(-894)).isFalse();
    }

    @Test
    @DisplayName("Valid salary changes are accepted")
    void validChangesAreAccepted() {
        assertThat(apprentice.changeSalary(-20)).isTrue();
        assertThat(employee.changeSalary(100)).isTrue();
        assertThat(tempWorker.changeSalary(-50)).isTrue();
    }

    @Test
    @DisplayName("Rejected change leaves salary untouched")
    void rejectedChangeKeepsSalary() {
        apprentice.changeSalary(-300);
        assertThat(apprentice.changeSalary(-298)).isTrue();
    }
}