package task_1;

import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> shop = new LinkedList<>();

        Thread producer = new Thread(
                () -> {
                    synchronized (shop) {
                        for (int i = 0; i < 5; i++) {
                            shop.add("Toyota " + i);
                            System.out.println("Производитель Toyota выпустил 1 автомобиль");
                            shop.notify();
                        }
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Thread consumer = new Thread(
                () -> {
                    for (int i = 1; i < 6; i++) {
                        System.out.println("Покупатель " + i + " зашел в автосалон");
                        synchronized (shop) {
                            if (shop.isEmpty()) {
                                System.out.println("Машин нет");
                                try {
                                    shop.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            System.out.println("Покупатель " + i + " уехал на новенькой " + shop.remove(0));
                        }
                    }
                });
        producer.start();
        consumer.start();
    }
}