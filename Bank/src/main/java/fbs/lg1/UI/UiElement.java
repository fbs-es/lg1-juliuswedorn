package fbs.lg1.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class UiElement<R> {

    public record Step<R>(
            List<Function<String, ?>> parsers,
            Function<List<Object>, R> method
    ) {
        public R run(List<String> rawInputs) {
            if (method == null) {
                return null;
            }

            List<Object> parsedArgs = new ArrayList<>();
            if (parsers != null && !parsers.isEmpty()) {
                for (int i = 0; i < parsers.size(); i++) {
                    String raw = (rawInputs != null && i < rawInputs.size()) ? rawInputs.get(i) : null;
                    Function<String, ?> parser = parsers.get(i);
                    parsedArgs.add((parser != null && raw != null) ? parser.apply(raw) : raw);
                }
            }

            return method.apply(parsedArgs);
        }

        public R run(String... rawInputs) {
            return run(rawInputs != null ? List.of(rawInputs) : List.of());
        }

        public static <R> Step<R> step(Supplier<R> supplier) {
            return new Step<>(
                    List.of(),
                    args -> supplier != null ? supplier.get() : null
            );
        }

        public static <R> Step<R> step(Function<String, R> method) {
            return step(Function.identity(), method);
        }

        public static <T, R> Step<R> step(Function<String, T> parser, Function<T, R> method) {
            return new Step<>(
                    List.of(parser),
                    args -> {
                        T arg = (!args.isEmpty()) ? (T) args.get(0) : null;
                        return method != null ? method.apply(arg) : null;
                    }
            );
        }

        public static <T1, T2, R> Step<R> step(
                Function<String, T1> p1,
                Function<String, T2> p2,
                BiFunction<T1, T2, R> method) {
            return new Step<>(
                    List.of(p1, p2),
                    args -> {
                        T1 arg1 = args.size() > 0 ? (T1) args.get(0) : null;
                        T2 arg2 = args.size() > 1 ? (T2) args.get(1) : null;
                        return method != null ? method.apply(arg1, arg2) : null;
                    }
            );
        }

        public static <R> Step<R> step(List<Function<String, ?>> parsers, Function<List<Object>, R> method) {
            return new Step<>(parsers != null ? parsers : List.of(), method);
        }

        public static <R> Step<R> step(Function<List<Object>, R> method, Function<String, ?>... parsers) {
            return new Step<>(List.of(parsers), method);
        }
    }

    private final String label;
    private final String input;
    private final List<UiElement<?>> child;
    private final List<Step<R>> steps;

    public UiElement(String label, String input, List<UiElement<?>> child, List<Step<R>> steps) {
        this.label = label;
        this.input = input;
        this.child = (child != null) ? List.copyOf(child) : List.of();
        this.steps = (steps != null) ? List.copyOf(steps) : List.of();
    }

    public UiElement(String label, String input, Step<R>... steps) {
        this(label, input, List.of(), List.of(steps));
    }

    public UiElement(String label, String input, List<UiElement<?>> child) {
        this(label, input, child, List.of());
    }

    public List<Function<String, ?>> getParsers() {
        if (steps == null || steps.isEmpty()) {
            return List.of();
        }
        return steps.get(0).parsers();
    }

    public String getLabel() {
        return label;
    }

    public String getInput() {
        return input;
    }

    public List<UiElement<?>> getChild() {
        return child;
    }

    public boolean hasMethod() {
        return !steps.isEmpty();
    }

    public List<R> executeMethod(List<String> rawInputs) {
        List<R> results = new ArrayList<>();
        for (Step<R> step : steps) {
            R res = step.run(rawInputs);
            if (res != null) {
                results.add(res);
            }
        }
        return results;
    }

    public List<R> executeMethod(String... rawInputs) {
        return executeMethod(rawInputs != null ? List.of(rawInputs) : List.of());
    }
}
//ICH BIN EIN KOCH