package com.ensiie.adgerodashboard;

import android.os.Handler;

import java.util.Random;

/**
 * Created by lucas on 08/05/2017.
 * This class is used to create Acceleration and Brake simulations to visualize the UI in action.
 * Only used for demo purposes. Can be removed safely afterwards.
 */

public class DrivingSimulation {

    /**
     * The default duration after which a accelerating or a braking thread times out.
     */
    private final static int DEFAULT_DURATION = 5;

    /**
     * The min duration time out
     */
    private final static int MIN_DURATION = 2;

    /**
     * The max duration time out
     */
    private final static int MAX_DURATION = 4;

    /**
     * Random Number Generator.
     */
    private static Random m_Rng = new Random();

    /**
     * Simulate an acceleration for a random amount of seconds
     * @param handler the handler to send a message to once the simulation is done.
     */
    public static void AccelerateRandom(Handler handler)
    {
        int duration = m_Rng.nextInt(MAX_DURATION - MIN_DURATION) + MIN_DURATION;
        new Thread(new AccelerateTask(duration, handler)).start();
    }

    /**
     * Simulate an deceleration for a random amount of seconds
     * @param handler the handler to send a message to once the simulation is done.
     */
    public static void BrakeRandom(Handler handler)
    {
        int duration = m_Rng.nextInt(MAX_DURATION - MIN_DURATION) + MIN_DURATION;
        new Thread(new BrakeTask(duration, handler)).start();
    }

    /**
     * The task that simulates an acceleration
     */
    private static class AccelerateTask implements Runnable {

        private int m_Length = DEFAULT_DURATION;
        private Handler m_Handler;

        AccelerateTask(int length, Handler handler)
        {
            m_Length = length;
            m_Handler = handler;
        }

        @Override
        public void run() {

            // Calculate startTime and endTime.
            long start = System.currentTimeMillis();
            long end = start + m_Length * 1000;

            // Get the current battery percentage and delivery ratio
            double batteryPercentage = DataSingleton.Instance.getBatteryPercentage();
            double storageRatio = DataSingleton.Instance.getStorageRatio();

            // This loop updates the Singleton values
            while(System.currentTimeMillis() < end)
            {
                storageRatio += 0.2f;
                batteryPercentage -= 0.05f;
                DataSingleton.Instance.setStorageRatio(storageRatio);
                DataSingleton.Instance.setBatteryPercentage(batteryPercentage);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // This loops puts the delivery ratio value back to 0.
            while (storageRatio > 0)
            {
                storageRatio -= 0.5f;
                DataSingleton.Instance.setStorageRatio(storageRatio);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Send a message to the handler so it knows job is done.
            m_Handler.sendEmptyMessage(1);
        }
    }

    /**
     * The task that simulates braking
     */
    private static class BrakeTask implements Runnable {

        private int m_Duration = DEFAULT_DURATION;
        private Handler m_Handler;

        BrakeTask(int length, Handler handler)
        {
            m_Duration = length;
            m_Handler = handler;
        }

        @Override
        public void run() {

            // Calculate startTime and endTime.
            long start = System.currentTimeMillis();
            long end = start + m_Duration * 1000;

            // Get the current battery percentage and delivery ratio
            double batteryPercentage = DataSingleton.Instance.getBatteryPercentage();
            double deliveryRatio = DataSingleton.Instance.getDeliveryRatio();

            // This loop updates the Singleton values
            while(System.currentTimeMillis() < end)
            {
                deliveryRatio += 0.2f;
                batteryPercentage += 0.1f;
                DataSingleton.Instance.setDeliveryRatio(deliveryRatio);
                DataSingleton.Instance.setBatteryPercentage(batteryPercentage);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // This loops puts the delivery ratio value back to 0.
            while (deliveryRatio > 0)
            {
                deliveryRatio -= 0.5f;
                DataSingleton.Instance.setDeliveryRatio(deliveryRatio);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Send a message to the handler so it knows job is done.
            m_Handler.sendEmptyMessage(1);
        }
    }
}
