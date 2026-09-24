package fbs.lg1;

import fbs.lg1.UI.Ui;
import fbs.lg1.UI.UiElement;

import java.util.ArrayList;
import java.util.List;

import static fbs.lg1.UI.UiElement.Step.step;

public class BankTerminal {

    private final Account[] accounts = new Account[10];
    private final Ui ui = new Ui();

    public void genBankTerminal() {
        List<UiElement<?>> accountMenuItems = new ArrayList<>();

        for (int i = 0; i < accounts.length; i++) {
            Account account = accounts[i];
            if (account != null) {
                accountMenuItems.add(createAccountMenu(account, i));
            }
        }

        if (accountMenuItems.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        ui.selectValue(accountMenuItems);
    }

    private UiElement<?> createAccountMenu(Account account, int index) {
        return new UiElement<>(
                account.getName() + " [" + account.getIBAN() + "]",
                String.valueOf(index + 1),
                List.of(
                        new UiElement<>("Change name", "1",
                                List.of(new UiElement<>("Set Name", null,
                                        step(name -> name, s -> {
                                            try {
                                                return account.changeName(s) ? "Success" : "Fail";
                                            } catch (Exception e) {
                                                return "Error: " + e.getMessage();
                                            }
                                        }),
                                        step(() -> {
                                            try {
                                                return "Your new Name: " + account.getName();
                                            } catch (Exception e) {
                                                return "Error: " + e.getMessage();
                                            }
                                        })
                                ))
                        ),

                        new UiElement<>("Deposit Balance", "2",
                                List.of(new UiElement<>("Deposit Balance", null,
                                        step(Float::parseFloat, amount -> {
                                            try {
                                                return account.depositBalance(amount) ? "Success" : "Fail";
                                            } catch (Exception e) {
                                                return "Error: " + e.getMessage();
                                            }
                                        }),
                                        step(() -> {
                                            try {
                                                return "Your money: " + account.getBalance();
                                            } catch (Exception e) {
                                                return "Error: " + e.getMessage();
                                            }
                                        })
                                ))
                        ),

                        new UiElement<>("Withdraw Money", "3",
                                List.of(new UiElement<>("Withdraw Amount", null,
                                        step(Float::parseFloat, amount -> {
                                            try {
                                                return account.withdrawMoney(amount) ? "Success" : "Fail";
                                            } catch (Exception e) {
                                                return "Error: " + e.getMessage();
                                            }
                                        }),
                                        step(() -> {
                                            try {
                                                return "Your money: " + account.getBalance();
                                            } catch (Exception e) {
                                                return "Error: " + e.getMessage();
                                            }
                                        })
                                ))
                        ),

                        new UiElement<>("Print Balance", "4",
                                step(() -> {
                                    try {
                                        return account.getBalance();
                                    } catch (Exception e) {
                                        return "Error: " + e.getMessage();
                                    }
                                })
                        ),

                        new UiElement<>("Print Name", "5",
                                step(() -> {
                                    try {
                                        return "Current Name: " + account.getName();
                                    } catch (Exception e) {
                                        return "Error: " + e.getMessage();
                                    }
                                })
                        ),

                        new UiElement<>("Print IBAN", "6",
                                step(() -> {
                                    try {
                                        return "Your IBAN: " + account.getIBAN();
                                    } catch (Exception e) {
                                        return "Error: " + e.getMessage();
                                    }
                                })
                        ),

                        new UiElement<>("Transfer Money", "7",
                                step(
                                        iban -> iban,
                                        Float::parseFloat,
                                        (iban, amount) -> {
                                            try {
                                                return account.makePayment(iban, amount) ? "Success" : "Fail";
                                            } catch (Exception e) {
                                                return "Error: " + e.getMessage();
                                            }
                                        }
                                ),
                                step(() -> {
                                    try {
                                        return "Your money: " + account.getBalance();
                                    } catch (Exception e) {
                                        return "Error: " + e.getMessage();
                                    }
                                })
                        )
                )
        );
    }

    public Account[] getAccounts() {
        return accounts;
    }

    public void addAccount(Account account, int i) {
        accounts[i] = account;
    }
}