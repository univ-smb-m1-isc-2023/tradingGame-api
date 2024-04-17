package tyg.tradinggame.tradinggame.application.game.logic;

import java.util.Random;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;

public class Utils {

    public static void deduceSumFromWallet(StockOrder stockOrder, double price) {
        double balance = stockOrder.getWallet().getBalance() - price * stockOrder.getQuantity();
        stockOrder.getWallet().setBalance(balance);
    }

    public static void addSumToWallet(StockOrder stockOrder, double price) {
        double balance = stockOrder.getWallet().getBalance() + price * stockOrder.getQuantity();
        stockOrder.getWallet().setBalance(balance);
    }

    public static double randomInRange(double min, double max) {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }
}
