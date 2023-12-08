package PaymentMethod;

public class BankCard implements Acceptor {
    private int amount;

    public BankCard(int balance){
        this.amount = balance;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
