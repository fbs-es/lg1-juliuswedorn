package fbs.lg1;

import java.util.List;
import static fbs.lg1.UiElement.step;

public class Main {
    public static void main(String[] args) {
        Ui ui = new Ui();
        Account account = new Account("Me", 300.0f, "At");

        ui.selectValue( List.of(
                new UiElement<>("Change name", "1",
                        List.of(new UiElement<>("Set Name", null, step(name -> name, s -> {
                                    try {
                                        return account.changeName(s) ? "Susses": "Fail";
                                    } catch (Exception e) {
                                        return "Error: " + e.getMessage();
                                    }
                                }),
                                step(unused -> {
                                    try {
                                        return "Your new Name: " + account.getName();
                                    } catch (Exception e) {
                                        return "Error: " + e.getMessage();
                                    }
                                })
                                )
                        )
                ),

                new UiElement<>("Deposit Balance", "2",
                        List.of(new UiElement<>("Deposit Balance", null, step(Float::parseFloat, amount -> {
                                    try {
                                        return account.depositBalance(amount) ? "Susses": "Fail";
                                    } catch (Exception e) {
                                        return "Error: " + e.getMessage();
                                    }
                                }),
                                step(unused -> {
                                    try {
                                        return "Your money: " + account.getBalance();
                                    } catch (Exception e) {
                                        return "Error: " + e.getMessage();
                                    }
                                })
                                )
                        )
                ),

                new UiElement<>("Withdraw Money", "3",
                        List.of(new UiElement<>("Withdraw Amount", null,
                                step(Float::parseFloat, amount -> {
                                    try {
                                        return account.withdrawMoney(amount) ? "Susses": "Fail";
                                    } catch (Exception e) {
                                        return "Error: " + e.getMessage();
                                    }
                                }),
                                step(unused -> {
                                    try {
                                        return "Your money: " + account.getBalance();
                                    } catch (Exception e) {
                                        return "Error: " + e.getMessage();
                                    }
                                })

                        ))
                ),

                new UiElement<>("Print Balance", "4",
                        step(unused -> {
                            try {
                                return account.getBalance();
                            } catch (Exception e) {
                                return "Error: " + e.getMessage();
                            }
                        })
                ),

                new UiElement<>("Print Name", "5",
                        step(unused -> {
                            try {
                                return "Current Name: " + account.getName();
                            } catch (Exception e) {
                                return "Error: " + e.getMessage();
                            }
                        })
                ),

                new UiElement<>("Print IBAN", "6",
                        step(unused -> {
                            try {
                                return "Your IBAN: " + account.getIBAN();
                            } catch (Exception e) {
                                return "Error: " + e.getMessage();
                            }
                        })
                )
        ));
    }
}