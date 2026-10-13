package fbs.lg1;

import fbs.lg1.UI.Ui;
import fbs.lg1.UI.UiElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

import static fbs.lg1.UI.UiElement.Step.step;

public class StaffManagementPortal {

    private final Ui ui = new Ui();

    private final List<Apprentices> apprentices = new ArrayList<>();
    private final List<Employee> employees = new ArrayList<>();
    private final List<TemporaryWorkers> temporaryWorkers = new ArrayList<>();

    public void addApprentices(Apprentices apprentice) {
        apprentices.add(apprentice);
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void addTemporaryWorkers(TemporaryWorkers temporaryWorker) {
        temporaryWorkers.add(temporaryWorker);
    }

    public void genApp() {
        List<UiElement<?>> mainMenuItems = new ArrayList<>();

        mainMenuItems.add(buildStaffMenu(
                "Apprentices", "1", () -> apprentices,
                Apprentices::getFullName, Apprentices::changeSalary));

        mainMenuItems.add(buildStaffMenu(
                "Employees", "2", () -> employees,
                Employee::getFullName, Employee::changeSalary));

        mainMenuItems.add(buildStaffMenu(
                "Temporary Workers", "3", () -> temporaryWorkers,
                TemporaryWorkers::getFullName, TemporaryWorkers::changeSalary));

        ui.selectValue(mainMenuItems);
    }

    private <T> UiElement<?> buildStaffMenu(
            String label,
            String inputKey,
            Supplier<List<T>> supplier,
            Function<T, String> nameOf,
            BiPredicate<T, Float> changeSalary
    ) {
        return Ui.buildDynamicMenu(
                label,
                inputKey,
                supplier,
                (member, index) -> createStaffActionSubmenu(
                        nameOf.apply(member),
                        String.valueOf(index + 1),
                        member,
                        nameOf,
                        changeSalary
                )
        );
    }

    private <T> UiElement<?> createStaffActionSubmenu(
            String label,
            String inputKey,
            T staff,
            Function<T, String> nameOf,
            BiPredicate<T, Float> changeSalary
    ) {
        List<UiElement<?>> actions = new ArrayList<>();

        actions.add(new UiElement<>(
                "Name anzeigen",
                "1",
                List.of(new UiElement<>("Details:", null, step(() -> nameOf.apply(staff))))
        ));

        actions.add(new UiElement<>(
                "Gehalt anpassen",
                "2",
                List.of(new UiElement<>(
                        "Gehalt ändern:",
                        null,
                        step(
                                List.of((String s) -> Float.parseFloat(s)),
                                inputs -> {
                                    float amount = (Float) inputs.get(0);
                                    return changeSalary.test(staff, amount) ? "Successfully changed Salary":"The Salary cant be negative";
                                }
                        )
                ))
        ));

        return new UiElement<>(label, inputKey, actions);
    }
}