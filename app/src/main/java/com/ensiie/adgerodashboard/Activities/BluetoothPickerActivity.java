package com.ensiie.adgerodashboard.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ensiie.adgerodashboard.Bluetooth.BluetoothBroadcastReceiver;
import com.ensiie.adgerodashboard.Bluetooth.BluetoothDeviceAdapter;
import com.ensiie.adgerodashboard.R;
import com.ensiie.adgerodashboard.Utils;

import java.util.ArrayList;
import java.util.List;

public class BluetoothPickerActivity extends AppCompatActivity {

    static final private int REQUEST_ENABLE_BT = 1;

    /**
     * Default Bluetooth Adapter of the device
     */
    private BluetoothAdapter m_BluetoothAdapter;

    /**
     * Broadcast receiver for when a bluetooth device is found.
     * Every time a new bluetooth device is discovered, the OnReceive Method of this
     * receiver will be called.
     */
    private BluetoothBroadcastReceiver m_Receiver;

    /**
     * List of the newly discovered devices. Initialized immediately to empty.
     */
    private List<BluetoothDevice> m_Devices = new ArrayList<>();

    /**
     * List of the already bonded devices. Initialized immediately to empty.
     */
    private List<BluetoothDevice> m_BondedDevices = new ArrayList<>();

    /**
     * Both recycler views in the activity.
     */
    private RecyclerView m_NewDevicesRV, m_PairedDevicesRV;

    /**
     * The textview that is displayed if there is no paired device registered.
     */
    private TextView m_NoPairedDevice;

    /**
     * The progress bar that shows while the discovery is in progress.
     */
    private ProgressBar m_DiscoveryProgress;

    /**
     * The button that allows to refresh the discovery
     */
    private ImageButton m_DiscoveryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_picker);

        // Show adgero logo in action bar
        Utils.setupLogoInActionBar(this);

        // Get the recycler views
        m_NewDevicesRV = (RecyclerView)findViewById(R.id.discoverDevicesRecyclerView);
        m_PairedDevicesRV = (RecyclerView)findViewById(R.id.pairedDevicesRecyclerView);

        // Get the text view
        m_NoPairedDevice = (TextView)findViewById(R.id.noPairedDeviceText);

        // Get the progress bar
        m_DiscoveryProgress = (ProgressBar)findViewById(R.id.discoveryProgress);
        m_DiscoveryProgress.setVisibility(View.INVISIBLE);

        // Get the discovery refresh button
        m_DiscoveryButton = (ImageButton)findViewById(R.id.discoveryRefresh);
        m_DiscoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_BluetoothAdapter.startDiscovery();
            }
        });

        checkIsBluetoothEnabled();
    }

    private void checkIsBluetoothEnabled()
    {
        // Get the default bluetooth adapter
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (m_BluetoothAdapter == null)
        {
            // Device does not support bluetooth
            try {
                throw new Exception("This device does not support Bluetooth");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // If bluetooth is not enabled, display a dialogue to the user to ask them to enable it.
        // Otherwise we start the discovery right away
        if (!m_BluetoothAdapter.isEnabled())
        {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
        }
        else
        {
            initBluetoothDiscovery();
        }
    }

    /**
     * Initializes the bluetooth devices discovery.
     */
    private void initBluetoothDiscovery()
    {
        // Initialize the broadcast receiver with the list of already paired devices.
        m_BondedDevices.addAll(m_BluetoothAdapter.getBondedDevices());

        // Display text view instead of the list if the list is empty.
        if (m_BondedDevices.isEmpty())
        {
            m_NoPairedDevice.setVisibility(View.VISIBLE);
            m_PairedDevicesRV.setVisibility(View.INVISIBLE);
        }

        // Initialize the broadcast receiver
        m_Receiver = new BluetoothBroadcastReceiver(m_Devices);
        m_Receiver.setProgressBar(m_DiscoveryProgress);
        m_Receiver.setButton(m_DiscoveryButton);

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(m_Receiver, filter);

        m_PairedDevicesRV.setAdapter(new BluetoothDeviceAdapter(m_BondedDevices));
        m_PairedDevicesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        m_NewDevicesRV.setAdapter(m_Receiver.getAdapter());
        m_NewDevicesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Start the discovery
        m_BluetoothAdapter.startDiscovery();
    }

    /**
     * Called after the user has answered to the "Do you wish to enable bluetooth" dialogue.
     * @param requestCode what activity has ended
     * @param resultCode what was the result
     * @param data extra data (unused here)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // If the user has accepted to use bluetooth, we initialize the discovery.
        if (resultCode == RESULT_OK) {
            initBluetoothDiscovery();
        }
    }
}
