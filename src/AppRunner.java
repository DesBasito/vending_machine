import enums.ActionLetter;
import model.*;
import PaymentMethod.Acceptor;
import PaymentMethod.BankCard;
import PaymentMethod.CoinAcceptor;
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
                new Snickers(ActionLetter.E, 90),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 120)
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
        String str = choiceOfPayMeth();
        allowProducts.addAll(getAllowedProducts(str).toArray());
        chooseAction(allowProducts, str);

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

    private Long checkCard(String str) {
        print(str);
        try {
            String num = fromConsole();
            if ( num.toCharArray().length > 11 && num.toCharArray().length < 16) {
                throw new RuntimeException(String.format("%s: ", "Enter a bank card correctly!"));
            }
            return Long.parseLong(num);
        } catch (RuntimeException e) {
            return checkCard(e.getMessage());
        }
    }

    private Integer checkPassword(String str) {
        print(str);
        try {
            String num = fromConsole();
            if (num.toCharArray().length != 4 ) {
                throw new RuntimeException(String.format("%s: ", "Enter a password correctly!(only 4 numbers)"));
            }
            return Integer.parseInt(num);
        } catch (RuntimeException e) {
            return checkPassword(e.getMessage());
        }
    }

    private String choiceOfPayMeth() {
        System.out.print("\nВыберите способ оплаты: \na - наличными\np - банковской картой\nспособ оплаты: ");
        String action = fromConsole().substring(0, 1);
        if ("a".equalsIgnoreCase(action)) {
            return " Вы выбрали наличную оплату\n a - Пополнить баланс";
        } else if ("p".equalsIgnoreCase(action)) {
            checkCard("Номер банковской карточки: ");
            checkPassword("Пароль: ");
            new Scanner(System.in).nextLine();
            return " Вы выбрали оплату банковской картой";
        } else {
            print("Выбери только один из двух!");
            return choiceOfPayMeth();
        }
    }

    private void chooseAction(UniversalArray<Product> products, String str) {
        System.out.println(str);
        showActions(products);
        print(" h - Выйти");
        System.out.print("купить: ");
        String action = fromConsole().substring(0, 1);
        if ("a".equalsIgnoreCase(action)) {
            paymentMethod[0].setAmount(paymentMethod[0].getAmount() + 5);
            print("Вы пополнили баланс на 5");
            if (paymentMethod[0].getAmount() < 25) {
                print("Ничего не купишь, кидай еще");
                chooseAction(products, str);
            }
            return;
        }

        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    if (str.equals(" Вы выбрали оплату банковской картой")) {
                        paymentMethod[1].setAmount(paymentMethod[1].getAmount() - products.get(i).getPrice());
                    } else {
                        paymentMethod[0].setAmount(paymentMethod[0].getAmount() - products.get(i).getPrice());
                    }
                    print("Вы купили " + products.get(i).getName());
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            if ("h".equalsIgnoreCase(action)) {
                isExit = true;
            } else {
                print("Недопустимая буква. Попробуйте еще раз.");
                chooseAction(products, str);
            }
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
