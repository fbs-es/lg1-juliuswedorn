package fbs.lg1;

import fbs.lg1.UI.Ui;
import fbs.lg1.UI.UiElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static fbs.lg1.UI.UiElement.Step.step;

public class CityGlideApp {

    private final List<User> users = new ArrayList<>();
    private final List<Scooter> scooters = new ArrayList<>();
    private final Ui ui = new Ui();

    @FunctionalInterface
    private interface ScooterAction {
        boolean execute(Scooter scooter, User user) throws Exception;
    }

    public void genCityGlideApp() {
        List<UiElement<?>> accountMenuItems = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user != null) {
                accountMenuItems.add(createAppMenu(user, i));
            }
        }

        if (accountMenuItems.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        ui.selectValue(accountMenuItems);
    }

    private UiElement<?> createAppMenu(User user, int index) {
        return new UiElement<>(
                user.getName(),
                String.valueOf(index + 1),
                List.of(
                        new UiElement<>("Print Name", "1",
                                List.of(new UiElement<>("Your Name:", null,
                                        step(() -> {
                                            try {
                                                return user.getName();
                                            } catch (Exception e) {
                                                return "Error: " + e.getMessage();
                                            }
                                        })
                                ))
                        ),
                        new UiElement<>("Print Credit Amount", "2",
                                List.of(new UiElement<>("Credit Amount:", null,
                                        step(() -> {
                                            try {
                                                return user.getCredit();
                                            } catch (Exception e) {
                                                return "Error: " + e.getMessage();
                                            }
                                        })
                                ))
                        ),
                        new UiElement<>("Rent Scooter", "3", createScooterMenu(user, Scooter::startScooter)),
                        new UiElement<>("Stop Renting Scooter", "4", createScooterMenu(user, Scooter::stopScooter)),
                        new UiElement<>("Print Ride History", "5",
                                List.of(new UiElement<>("Ride History:", null,
                                        step(() -> {
                                            try {
                                                return user.getRideHistoryString();
                                            } catch (Exception e) {
                                                return "Error: " + e.getMessage();
                                            }
                                        })
                                ))
                        ),
                        new UiElement<>("Pay in Credit", "6",
                                List.of(new UiElement<>("How much you want to pay:", null,
                                        step(Float::parseFloat, amount -> {
                                            try {
                                                return user.payInCredit(amount) ? "Success" : "Fail";
                                            } catch (Exception e) {
                                                return "Error: " + e.getMessage();
                                            }
                                        })
                                ))
                        )
                )
        );
    }

    private List<UiElement<?>> createScooterMenu(User user, ScooterAction action) {
        return createScooterMenu(user, action, scooter -> true);
    }

    private List<UiElement<?>> createScooterMenu(User user, ScooterAction action, Predicate<Scooter> filter) {
        List<UiElement<?>> scooterItems = new ArrayList<>();

        for (Scooter scooter : scooters) {
            if (scooter != null && filter.test(scooter)) {
                scooterItems.add(new UiElement<>(
                        "Scooter " + scooter.getScooterId(),
                        String.valueOf(scooter.getScooterId()),
                        List.of(new UiElement<>(null, null,
                                step(() -> {
                                    try {
                                        return action.execute(scooter, user)  ? "Your Action was Successful":"something didn't work";
                                    } catch (Exception e) {
                                        return "Error: " + e.getMessage();
                                    }
                                })
                        ))
                ));
            }
        }

        if (scooterItems.isEmpty()) {
            System.out.println("No scooters available.");
        }

        return scooterItems;
    }

    public void addAccount(User user) {
        if (user != null) {
            users.add(user);
        }
    }

    public void addScooter(Scooter scooter) {
        if (scooter != null) {
            scooters.add(scooter);
        }
    }
    public int getUserCount() {
        return users.size();
    }

    public int getScooterCount() {
        return scooters.size();
    }

}