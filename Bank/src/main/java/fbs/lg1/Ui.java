package fbs.lg1;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

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
            String input = null;
            if (choice.getPhraseType() != null) {
                System.out.print("Enter input: ");
                input = scanner.nextLine();
            }
            List<Object> results = choice.executeMethod(input);
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