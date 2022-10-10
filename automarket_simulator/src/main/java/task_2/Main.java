package task_2;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static final int BUYERS = 10;
    public final static int SELLING_CAR = 10;
    public final static int TIME_BETWEEN_PROD = 1000;
    public final static int TIME_BETWEEN_BUYERS = 1000;

    public static void main(String[] args) {
        List<String> shop = new LinkedList<>();
        ReentrantLock locker = new ReentrantLock(true);
        Condition condition = locker.newCondition();

        Thread producer = new Thread(
                () -> {
                    for (int i = 0; i < SELLING_CAR; i++) {
                        locker.lock();
                        shop.add(String.format("Toyota %d", i + 1));
                        System.out.println("Производитель Toyota выпустил 1 автомобиль");
                        condition.signal();
                        try {
                            Thread.sleep(TIME_BETWEEN_PROD);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            locker.unlock();
                        }
                    }
                    Thread.currentThread().interrupt();
                });

        Thread consumer = new Thread(
                () -> {
                    try {
                        while (!producer.isInterrupted()) {
                            locker.lock();
                            for (int i = 0; i < BUYERS; i++) {
                                System.out.printf("Покупатель %d зашел в автосалон %n", i + 1);
                                if (shop.isEmpty()) {
                                    System.out.printf("Покупатель %d встал в очередь на машину %n", i + 1);
                                    condition.await();
                                }
                                System.out.printf("Покупатель %d уехал на новенькой %s%n", i + 1, shop.remove(0));
                                Thread.sleep(TIME_BETWEEN_BUYERS);
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        locker.unlock();
                    }
                });

        producer.start();
        consumer.start();
    }
}