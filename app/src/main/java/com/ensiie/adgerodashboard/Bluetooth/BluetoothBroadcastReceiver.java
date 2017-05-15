package com.ensiie.adgerodashboard.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.ensiie.adgerodashboard.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 04/05/2017.
 * Broadcast receiver for bluetooth events.
 */
public class BluetoothBroadcastReceiver extends BroadcastReceiver {

    /**
     * The pointer to the list containing all the newly found devices.
     */
    private List<BluetoothDevice> m_Devices;

    /**
     * Adapter for the recycler view that will contain the newly discovered devices.
     * We keep it in this class because we need to be able to call
     * notifyDatasetChanged on the adapter in the onReceive method of this class.
     */
    private BluetoothDeviceAdapter m_Adapter;

    /**
     * The progress bar to dismiss when discovery has ended. Optional.
     */
    private ProgressBar m_ProgressBar;

    /**
     * The button used to start the discovery. Optional.
     */
    private ImageButton m_StartButton;

    private BluetoothBroadcastReceiver()
    {
        // Empty private default constructor to force use of the other constructor.
    }

    public BluetoothBroadcastReceiver(List<BluetoothDevice> newDevices)
    {
        m_Devices = newDevices;

        // Initialize a default list if given was null
        if (m_Devices == null)
        {
            m_Devices = new ArrayList<>();
        }

        m_Adapter = new BluetoothDeviceAdapter(m_Devices);
    }

    /**
     * Get the adapter for the list of the newly discovered devices.
     * @return the pointer to the Bluetooth Device Adapter.
     */
    public RecyclerView.Adapter getAdapter()
    {
        return m_Adapter;
    }

    public void setProgressBar(ProgressBar progressBar)
    {
        m_ProgressBar = progressBar;
    }

    public void setButton(ImageButton button)
    {
        m_StartButton = button;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // First get the intent action and make sure it is the action we want (here when a bluetooth device is found)
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {

            // Discovery has found a device. Get the BluetoothDevice
            // object and its info from the Intent.
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            // We add the device to the list and notify that the dataset has changed
            // to refresh  the recycler view.
            m_Devices.add(device);
            m_Adapter.notifyDataSetChanged();
        }
        else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
        {
            Utils.showToast(context, "Discovery started");
            if (m_StartButton != null)
            {
                m_StartButton.setVisibility(View.GONE);
            }

            if (m_ProgressBar != null)
            {
                m_ProgressBar.setVisibility(View.VISIBLE);
            }
        }
        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
        {
            Utils.showToast(context, "Discovery ended !");
            if (m_ProgressBar != null)
            {
                m_ProgressBar.setVisibility(View.INVISIBLE);
            }

            if (m_StartButton != null)
            {
                m_StartButton.setVisibility(View.VISIBLE);
            }
        }
    }
}
