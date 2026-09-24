package fbs.lg1.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

public class Ui {

    private final Scanner scanner = new Scanner(System.in);

    public Ui() {}

    public void selectValue(List<UiElement<?>> elements) {
        if (elements == null || elements.isEmpty()) return;

        if (elements.stream().noneMatch(e -> e.getInput() != null)) {
            for (UiElement<?> option : elements) {
                if (option.getLabel() != null) {
                    System.out.println(option.getLabel());
                }
                traverse(option);
            }
            return;
        }

        boolean running = true;

        while (running) {
            System.out.println();
            for (UiElement<?> option : elements) {
                if (option.getInput() != null) {
                    System.out.println("[" + option.getInput() + "] " + option.getLabel());
                } else {
                    System.out.println(option.getLabel());
                }
            }
            System.out.println("[0] Go Back");

            System.out.print("Select an option: ");
            String selectionKey = scanner.nextLine().trim();
            System.out.println();

            if ("0".equals(selectionKey)) {
                running = false;
            } else {
                Optional<UiElement<?>> selected = elements.stream()
                        .filter(opt -> selectionKey.equalsIgnoreCase(opt.getInput()))
                        .findFirst();

                if (selected.isPresent()) {
                    traverse(selected.get());
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }

    private void traverse(UiElement<?> choice) {
        if (choice.hasMethod()) {
            List<Function<String, ?>> parsers = choice.getParsers();
            List<String> inputs = new ArrayList<>();

            if (parsers != null && !parsers.isEmpty()) {
                for (int i = 0; i < parsers.size(); i++) {
                    if (parsers.size() == 1) {
                        System.out.print("Enter input: ");
                    } else {
                        System.out.print("Enter input " + (i + 1) + ": ");
                    }
                    inputs.add(scanner.nextLine());
                }
            }

            List<?> results = choice.executeMethod(inputs);
            for (Object result : results) {
                System.out.println(result);
            }
        }

        List<UiElement<?>> children = choice.getChild();
        if (children != null && !children.isEmpty()) {
            selectValue(children);
        }
    }
}