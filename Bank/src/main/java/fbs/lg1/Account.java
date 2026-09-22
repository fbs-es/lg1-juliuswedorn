package fbs.lg1;

import java.util.Random;

public class Account {
    private String Name;
    private float Balance;
    private long AccountNumber;
    private String IBAN;
    public Account(String Name, float Balance, String  Country) {
        initialize(Name, Balance,Country);
    }

    public Account(String Name, String  Country) {
        this(Name, 0.0f, Country);
    }

    private void initialize(String name, float balance,String Country) {
        this.Name = name;
        this.Balance = balance < 0 ? 0 : balance;
        this.AccountNumber = genRandomNumber(10000000L, 99999999L);
        long bankCode = genRandomNumber(10000L, 99999L);
        this.IBAN = genIBAN(Country.toUpperCase(), bankCode, this.AccountNumber);
    }

    public float getBalance() {
        return this.Balance;
    }

    public String getName() {
        return this.Name;
    }

    public String getIBAN() {
        return this.IBAN;
    }

    public boolean depositBalance(float ammount) throws Exception {
        if (ammount < 0) throw new Exception("Amount must be positive");
        this.Balance = this.Balance + ammount;
        return true;
    }

    public boolean withdrawMoney(float ammount) throws Exception {
        if (ammount < 0) throw new Exception("Amount must be positive");
        float BalanceAfter = this.Balance - ammount;
        if (BalanceAfter < 0) throw new Exception("Not enoug Money");
        this.Balance = BalanceAfter;
        return true;
    }

    //public void  makePayment (String IBAN, float amount) throws Exception {
    //    if (amount < 0) throw new Exception("Amount must be positive");
    //    Account targetAccount =
    //}

    public boolean changeName(String newName)  throws Exception {
        if (newName == null)  throw new Exception("Name cant be null");
        if (newName.replace(" ", "").isEmpty()) throw new Exception("Name cant be empty");;
        this.Name = newName;
        return true;
    }

    private long genRandomNumber(long min,long max) {
        Random rand = new Random();
        return rand.nextLong(min, max);
    }

    //private Account findAccount(String IBAN) {

    //}

    //the Iban is only valid for AT (i think the docu on it is very bad)
    private String genIBAN(String country, long  blz, long  accountNumber) {
        country = country.toUpperCase();
        String BLZ = String.format("%08d", blz);
        String AccountNumber = String.format("%010d", accountNumber);

        String bban = BLZ + AccountNumber;
        String rearranged = bban + country + "00";
        int rem = 0;
        for (int i = 0; i < rearranged.length(); i++) {
            char c = rearranged.charAt(i);
            int val = (c >= 'A' && c <= 'Z') ? c - 'A' + 10 : c - '0';
            rem = (rem * 10 + val) % 97;
        }
        String CheckSum = String.format("%02d", 98 - rem);

        return country + " "+ CheckSum + " "+ BLZ + AccountNumber;
    }

}
