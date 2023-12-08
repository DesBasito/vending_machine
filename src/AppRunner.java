import PaymentMethod.Acceptor;
import PaymentMethod.BankCard;
import PaymentMethod.CoinAcceptor;
import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final Acceptor[] paymentMethod;

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        paymentMethod = new Acceptor[]{new CoinAcceptor(80), new BankCard(300)};
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);

        print("Монет на сумму: " + paymentMethod[0].getAmount());
        print("Баланс на карте: " + paymentMethod[1].getAmount());
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
//        String str = choiceOfPayMeth();
//        allowProducts.addAll(getAllowedProducts(str).toArray());
//        chooseAction(allowProducts, str);

    }

    private UniversalArray<Product> getAllowedProducts(String str) {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        if (str.equals(" Вы выбрали оплату банковской картой")) {
            for (int i = 0; i < products.size(); i++) {
                if (paymentMethod[1].getAmount() >= products.get(i).getPrice()) {
                    allowProducts.add(products.get(i));
                }
            }
        } else {
            for (int i = 0; i < products.size(); i++) {
                if (paymentMethod[0].getAmount() >= products.get(i).getPrice()) {
                    allowProducts.add(products.get(i));
                }
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products,String str) {
        showActions(products);
        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    paymentMethod[0].setAmount(paymentMethod[0].getAmount() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                } else if ("h".equalsIgnoreCase(action)) {
                    isExit = true;
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            print("Недопустимая буква. Попрбуйте еще раз.");
//            chooseAction(products);
        }


    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
