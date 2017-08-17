package repository;

import java.util.List;

public class BankRepository {
    /*
    This class keeps track of all banks.
    Since it's a singleton class, it can be accessed from anywhere (not a very nice design pattern, I know, but for now, it works :)
     */

    private static BankRepository instance = null;

    protected BankRepository() {
        // Exists only to defeat instantiation.
    }

    private void addBank(String name, String bic, String ipAddress, int port) {
        Bank newBank = new Bank(name, bic, ipAddress, port);
        newBank.save();
    }

    public List<Bank> getBanks() {
        return Bank.find.all();
    }

    public Bank getBank(String bic) {
        List<Bank> banks = this.getBanks();
        for(Bank bank : banks) {
            if(bank.getIdentificationCode().equals(bic)) {
                return bank;
            }
        }
        return null;
    }

    public static BankRepository getInstance() {
        if(instance == null) {
            instance = new BankRepository();

            // create some banks
            if(Bank.find.all().size() == 0) {
                instance.addBank("Test bank 1", "BNKA", "127.0.0.1", 9000);
                instance.addBank("Test bank 2", "BNKB", "127.0.0.1", 9001);
            }
        }
        return instance;
    }
}