package com.ensiie.adgerodashboard.Activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ensiie.adgerodashboard.Structures.AppStatus;
import com.ensiie.adgerodashboard.Structures.DataReceptionMode;
import com.ensiie.adgerodashboard.DataSingleton;
import com.ensiie.adgerodashboard.DrivingSimulation;
import com.ensiie.adgerodashboard.R;
import com.ensiie.adgerodashboard.Utils;
import com.triggertrap.seekarc.SeekArc;

public class MainActivity extends AppCompatActivity {

    /**
     * The refresh rate for the update thread in milli seconds.
     */
    private final static int REFRESH_RATE_MILLIS = 100;

    /**
     * The battery low level treshold. Whenever the battery percentage is below that level, the battery icon
     * will be displayed in red instead of green.
     */
    private final static int BATTERY_LOW_TRESHOLD = 20;

    /**
     * The half arc used to display the delivery ratio
     */
    private SeekArc m_DeliveryArc;

    /**
     * The half arc used to display the storage ratio
     */
    private SeekArc m_StorageArc;

    /**
     * The image (red | orange | green circle) used to display the status of the card.
     */
    private ImageView m_AppStatusImage;

    /**
     * The TextView containing the associated text displayed for the status.
     */
    private TextView m_AppStatusText;

    /**
     * The TextView containing the current energy status (delivery | storage)
     */
    private TextView m_EnergyStatus;

    /**
     * The TextView containing the value in percentage associated to the energy status
     */
    private TextView m_EnergyValue;

    /**
     * The TextView containing the battery level in percentage
     */
    private TextView m_BatteryLevel;

    /**
     * The battery icon. This image uses levels to display different images according to the battery level.
     */
    private ImageView m_BatteryImage;

    /**
     * The color to use when the battery level is ABOVE the low treshold
     */
    private int m_BatteryNormalColor;

    /**
     * The color to use when the battery level is BELOW the low treshold
     */
    private int m_BatteryLowColor;

    /**
     * Whether or not to update the UI. When true, a background thread refreshes the UI every REFRESH_RATE_MILLIS milliseconds.
     * Set this to false to stop the Thread.
     */
    private boolean m_Update = true;

    /**
     * Called when the activity is started.
     * Also called upon rotation of the app.
     * @param savedInstanceState the bundle containing retained information we want to restore. we don't use it here
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show the logo in the action bar
        Utils.setupLogoInActionBar(this);

        // Get the two different colors for the battery icon
        m_BatteryNormalColor = ContextCompat.getColor(this, R.color.batteryGreen);
        m_BatteryLowColor = ContextCompat.getColor(this, R.color.batteryRed);

        // Get all the components we need from the layout
        m_DeliveryArc = (SeekArc)findViewById(R.id.seekArcDelivery);
        m_StorageArc = (SeekArc)findViewById(R.id.seekArcStorage);
        m_EnergyStatus = (TextView)findViewById(R.id.energyStatus);
        m_EnergyValue = (TextView)findViewById(R.id.energyValue);
        m_BatteryLevel = (TextView)findViewById(R.id.batteryValue);
        m_BatteryImage = (ImageView)findViewById(R.id.batteryOutline);
        m_AppStatusImage = (ImageView)findViewById(R.id.statusImage);
        m_AppStatusText = (TextView)findViewById(R.id.appStatus);

        // Get the simulation Floating Action Buttons (FABs)
        final FloatingActionButton fabAccelerate = (FloatingActionButton)findViewById(R.id.fabAccelerate);
        final FloatingActionButton fabBrake = (FloatingActionButton)findViewById(R.id.fabBrake);

        // Launch simulation if needed
        if (DataSingleton.Instance.getDataReceptionMode().equals(DataReceptionMode.SIMULATION))
        {
            final Handler simulationHandler = new Handler(Looper.getMainLooper())
            {
                @Override
                public void handleMessage(Message inputMessage)
                {
                    if (inputMessage.what > 0)
                    {
                        fabAccelerate.setEnabled(true);
                        fabBrake.setEnabled(true);
                    }
                }
            };

            fabAccelerate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fabAccelerate.setEnabled(false);
                    fabBrake.setEnabled(false);
                    DrivingSimulation.AccelerateRandom(simulationHandler);

                }
            });

            fabBrake.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fabAccelerate.setEnabled(false);
                    fabBrake.setEnabled(false);
                    DrivingSimulation.BrakeRandom(simulationHandler);
                }
            });
        }
        else
        {
            fabAccelerate.setVisibility(View.GONE);
            fabBrake.setVisibility(View.GONE);
        }

        // launch the thread that will periodically update the UI
        new Thread(m_RefreshAction).start();
    }

    /**
     * Called when the activity is destroyed
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        // Stop the update thread
        m_Update = false;
    }

    /**
     * The runnable that will perform the refresh of the UI
     */
    private Runnable m_RefreshAction = new Runnable(){

        @Override
        public void run() {
            while (m_Update)
            {
                try {
                    // Perform the action periodically
                    Thread.sleep(REFRESH_RATE_MILLIS);

                    // We calculate all the values we need to update the energy in the background thread.
                    final int energyValue, energyTextColor;
                    final String energyStatus;
                    int tempValue = 0,  tempColor = android.R.color.primary_text_dark;
                    String tempStatus = "";

                    final int storageRatio = (int)DataSingleton.Instance.getStorageRatio();
                    final int deliveryRatio = (int)DataSingleton.Instance.getDeliveryRatio();

                    if (storageRatio > 0)
                    {
                        tempValue = storageRatio;
                        tempColor = m_StorageArc.getProgressColor();
                        tempStatus = getString(R.string.storage);
                    }
                    else if (deliveryRatio > 0)
                    {
                        tempValue = deliveryRatio;
                        tempColor = m_DeliveryArc.getProgressColor();
                        tempStatus = getString(R.string.delivery);
                    }

                    energyValue = tempValue;
                    energyTextColor = tempColor;
                    energyStatus = tempStatus;

                    AppStatus currentStatus = DataSingleton.Instance.getAppStatus();
                    final Drawable appStatusDrawable = currentStatus.getDrawable(getApplicationContext());
                    final String appStatusText = currentStatus.getString(getApplicationContext());

                    // We call runOnUiThread to update the UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_StorageArc.setProgress(storageRatio);
                            m_DeliveryArc.setProgress(deliveryRatio);
                            m_EnergyStatus.setText(energyStatus);
                            m_EnergyStatus.setTextColor(energyTextColor);
                            m_EnergyValue.setText(getResources().getString(R.string.percentage, energyValue));
                            m_EnergyValue.setTextColor(energyTextColor);
                            m_AppStatusImage.setImageDrawable(appStatusDrawable);
                            m_AppStatusText.setText(appStatusText);
                        }
                    });

                    // We get the values we need to update the battery level.
                    final int batteryLevel = (int)DataSingleton.Instance.getBatteryPercentage();
                    final int batteryColor = (batteryLevel > BATTERY_LOW_TRESHOLD) ? m_BatteryNormalColor : m_BatteryLowColor;

                    // We call runOnUiThread to update the UI once again
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_BatteryImage.setColorFilter(batteryColor);
                            m_BatteryImage.setImageLevel(batteryLevel);
                            m_BatteryLevel.setText(getResources().getString(R.string.percentage, batteryLevel));
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

}
