package com.ensiie.adgerodashboard;

import android.util.Log;

import com.ensiie.adgerodashboard.Structures.AppStatus;
import com.ensiie.adgerodashboard.Structures.DataReceptionMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucas on 08/05/2017.
 * This singleton class is used to store all the important values at runtime.
 * This is susceptible to change as the app may need to display more and more information.
 */
public class DataSingleton {

    public final static DataSingleton Instance = new DataSingleton();

    // Int values for each sensor type
    private final static int TYPE_BATTERY_LEVEL = 1;
    private final static int TYPE_KERS_STATUS = 2;
    private final static int TYPE_STORAGE_RATIO = 3;
    private final static int TYPE_DELIVERY_RATIO = 4;

    /**
     * The json key for the board sub object
     */
    private final static String KEY_BOARD = "board";

    /**
     * The json key for the sensor array that contains all the info.
     */
    private final static String KEY_SENSORS = "sensor";

    /**
     * The json key for the sensor type
     */
    private final static String KEY_SENSOR_TYPE = "-type";

    /**
     * The json key for the sensor value
     */
    private final static String KEY_SENSOR_VALUE = "-v";

    /**
     * The energy delivery ratio.
     * The value is updated every time the connection receives an input from the card.
     */
    private double m_DeliveryRatio = 0;

    /**
     * The energy storage ratio.
     * The value is updated every time the connection receives an input from the card.
     */
    private double m_StorageRatio = 0;

    /**
     * Indicates the filling percentage of the battery.
     * The value is updated every time the connection receives an input from the card.
     */
    private double m_BatteryPercentage = 0;

    /**
     * The status of the system given by the card (Okay, standby or error)
     */
    private AppStatus m_AppStatus = AppStatus.INACTIVE;

    /**
     * The way we receive data in the app
     * Can be bluetooth, direct wifi or just a simulation that generates random data.
     */
    private DataReceptionMode m_DataReceptionMode = DataReceptionMode.SIMULATION;

    /**
     * Empty private constructor to avoid instantiating the singleton from outside this class.
     */
    private DataSingleton(){}

    /**
     * Sets all the necessary data from a given JSON object
     * @param data the json object to retrieve the data from
     */
    public void setData(JSONObject data)
    {
        try {
            JSONObject board = data.getJSONObject(KEY_BOARD);
            JSONArray sensors = board.getJSONArray(KEY_SENSORS);
            for (int i = 0; i < sensors.length(); i++)
            {
                JSONObject currentSensorObject = sensors.getJSONObject(i);
                setSensorType(currentSensorObject.getInt(KEY_SENSOR_TYPE), currentSensorObject.getDouble(KEY_SENSOR_VALUE));
            }
        } catch (JSONException e) {
            Log.e("JSON Error: ", "the necessary keys could not be found. Please verify the json object or the key values.");
            e.printStackTrace();
        }
    }

    /**
     * Sets the correct value depending on the type
     * @param type type of the sensor
     * @param value value to assign
     */
    private void setSensorType(int type, double value)
    {
        switch(type)
        {
            case TYPE_BATTERY_LEVEL:
                setBatteryPercentage(value);
                break;
            case TYPE_KERS_STATUS:
                setAppStatus(AppStatus.getInstance((int)value));
                break;
            case TYPE_STORAGE_RATIO:
                setStorageRatio(value);
                break;
            case TYPE_DELIVERY_RATIO:
                setDeliveryRatio(value);
                break;
        }
    }

    /*
     * Getters and setters for all the fields.
     * The setters are synchronized since we don't want two background threads to access and modify a value at the same time
     */

    public synchronized void setDeliveryRatio(double deliveryRatio)
    {
        m_DeliveryRatio = clamp(deliveryRatio);
    }

    public double getDeliveryRatio()
    {
        return m_DeliveryRatio;
    }

    public synchronized void setStorageRatio(double storageRatio)
    {
        m_StorageRatio = clamp(storageRatio);
    }

    public double getStorageRatio()
    {
        return m_StorageRatio;
    }

    public synchronized void setBatteryPercentage(double batteryPercentage)
    {
        m_BatteryPercentage = clamp(batteryPercentage);
    }

    public double getBatteryPercentage()
    {
        return m_BatteryPercentage;
    }

    public synchronized void setAppStatus(AppStatus appStatus)
    {
        m_AppStatus = appStatus;
    }

    public AppStatus getAppStatus()
    {
        return m_AppStatus;
    }

    public void setDataReceptionMode(DataReceptionMode dataReceptionMode)
    {
        m_DataReceptionMode = dataReceptionMode;
    }

    public DataReceptionMode getDataReceptionMode()
    {
        return m_DataReceptionMode;
    }

    /**
     * Helper method to clamp a ratio to make sure it stays between 0 and 100
     * @param ratio value to clamp
     * @return the clamped value (0 if ratio < 0, 100 if ratio > 100, unchanged ratio otherwise
     */
    private double clamp(double ratio)
    {
        if (ratio < 0)
            ratio = 0;
        else if (ratio > 100)
            ratio = 100;

        return ratio;
    }
}
