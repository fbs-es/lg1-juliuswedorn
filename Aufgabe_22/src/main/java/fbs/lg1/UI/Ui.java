package fbs.lg1.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static fbs.lg1.UI.UiElement.Step.step;

public class Ui {

    private final Scanner scanner = new Scanner(System.in);

    public Ui() {}

    public static <T> UiElement<?> buildDynamicMenu(
            String label,
            String inputKey,
            Supplier<List<T>> listSupplier,
            Function<T, String> labelExtractor
    ) {
        return new UiElement<>(
                label,
                inputKey,
                List.of(new UiElement<>(label + ":", null, step(() -> {
                        List<T> items = listSupplier.get();
                        if (items == null || items.isEmpty()) {
                            return "No items found.";
                        }
                        return items.stream()
                                .map(labelExtractor)
                                .collect(Collectors.joining("\n"));
                })))
        );
    }

    public static <T> List<UiElement<?>> buildListMenu(
            List<T> items,
            Function<T, String> labelExtractor,
            String detailHeader
    ) {
        return buildListMenu(
                items,
                labelExtractor,
                detailHeader,
                item -> step(() -> labelExtractor.apply(item))
        );
    }

    public static <T> List<UiElement<?>> buildListMenu(
            List<T> items,
            Function<T, String> labelExtractor,
            String detailHeader,
            Function<T, UiElement.Step> stepExtractor
    ) {
        return buildMenu(items, (item, index) -> {
            String label = labelExtractor.apply(item);
            return new UiElement<>(
                    label,
                    String.valueOf(index + 1),
                    List.of(new UiElement<>(detailHeader, null, stepExtractor.apply(item)))
            );
        });
    }

    public static <T> List<UiElement<?>> buildMenu(
            List<T> items,
            BiFunction<T, Integer, UiElement<?>> itemMapper
    ) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }

        return IntStream.range(0, items.size())
                .mapToObj(i -> {
                    T item = items.get(i);
                    return item != null ? itemMapper.apply(item, i) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

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

            try {
                List<?> results = choice.executeMethod(inputs);
                for (Object result : results) {
                    System.out.println(result);
                }
            } catch (Exception e) {
                System.out.println("Error executing operation: " + e.getMessage());
            }
        }

        List<UiElement<?>> children = choice.getChild();
        if (children != null && !children.isEmpty()) {
            selectValue(children);
        }
    }
}