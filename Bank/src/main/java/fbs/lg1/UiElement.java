package fbs.lg1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class UiElement<R> {

    public record Step<T, R>(Function<String, T> parser, Function<T, R> method) {
        public Object run(String rawInput) {
            T parsed = (parser != null && rawInput != null) ? parser.apply(rawInput) : null;
            return method != null ? method.apply(parsed) : null;
        }
    }

    public static <T, R> Step<T, R> step(Function<String, T> parser, Function<T, R> method) {
        return new Step<>(parser, method);
    }

    public static <R> Step<String, R> step(Function<String, R> method) {
        return new Step<>(null, method);
    }

    private final String label;
    private final String input;
    private final List<UiElement<?>> child;
    private final List<Step<?, ?>> steps;

    public UiElement(String label, String input, List<UiElement<?>> child, List<Step<?, ?>> steps) {
        this.label = label;
        this.input = input;
        this.child = (child != null) ? List.copyOf(child) : List.of();
        this.steps = (steps != null) ? List.copyOf(steps) : List.of();
    }

    public UiElement(String label, String input, Step<?, ?>... steps) {
        this(label, input, List.of(), List.of(steps));
    }

    public UiElement(String label, String input, List<UiElement<?>> child) {
        this(label, input, child, List.of());
    }

    public Function<String, ?> getPhraseType() {
        if (steps == null || steps.isEmpty()) {
            return null;
        }
        return steps.get(0).parser();
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

    public List<Object> executeMethod(String rawInput) {
        List<Object> results = new ArrayList<>();
        for (Step<?, ?> step : steps) {
            Object res = step.run(rawInput);
            if (res != null) {
                results.add(res);
            }
        }
        return results;
    }
}
//ICH BIN EIN KOCH