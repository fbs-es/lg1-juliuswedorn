package fbs.lg1;

public class Main {

    private static BankTerminal bankTerminal;

    public static void main(String[] args) {
        startBank();
    }

    private static void startBank() {
        newBankTerminal();
        bankTerminal.addAccount(new Account("Me", 300.0f, "At"), 0);
        bankTerminal.addAccount(new Account("John Doe", -180.0f, "de"), 1);
        bankTerminal.genBankTerminal();
    }

    public static BankTerminal newBankTerminal() {
        return bankTerminal = new BankTerminal();
    }

    public static BankTerminal getBankTerminal() {
        return bankTerminal;
    }
}