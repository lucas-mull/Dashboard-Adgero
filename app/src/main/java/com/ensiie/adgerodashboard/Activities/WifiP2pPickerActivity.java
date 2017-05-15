package com.ensiie.adgerodashboard.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.ensiie.adgerodashboard.R;
import com.ensiie.adgerodashboard.Utils;
import com.ensiie.adgerodashboard.WifiP2p.WifiP2pBroadcastReceiver;
import com.ensiie.adgerodashboard.WifiP2p.WifiP2pDeviceAdapter;

import java.util.ArrayList;

/**
 * Created by lucas on 15/05/2017.
 */
public class WifiP2pPickerActivity extends AppCompatActivity {

    private WifiP2pManager m_Manager;
    private WifiP2pManager.Channel m_Channel;
    private BroadcastReceiver m_Receiver;
    private IntentFilter m_IntentFilter;
    private ArrayList<WifiP2pDevice> m_Devices = new ArrayList<>();

    private RecyclerView m_DeviceList;
    private ProgressBar m_ProgressBar;
    private ImageButton m_RefreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directwifi_picker);

        m_ProgressBar = (ProgressBar)findViewById(R.id.discoveryProgress);
        m_ProgressBar.setVisibility(View.INVISIBLE);

        m_RefreshButton = (ImageButton)findViewById(R.id.discoveryRefresh);
        m_RefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDiscovery();
            }
        });

        m_DeviceList = (RecyclerView)findViewById(R.id.listDevices);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        m_DeviceList.setLayoutManager(linearLayoutManager);
        m_DeviceList.setAdapter(new WifiP2pDeviceAdapter(m_Devices));

        m_Manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        m_Channel = m_Manager.initialize(this, getMainLooper(), null);
        m_Receiver = new WifiP2pBroadcastReceiver(m_Manager, m_Channel, this);

        m_IntentFilter = new IntentFilter();
        m_IntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        m_IntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        m_IntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        m_IntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        startDiscovery();
    }

    private void startDiscovery()
    {
        m_RefreshButton.setVisibility(View.GONE);
        Utils.showToast(this, "Discovery started");
        m_Manager.discoverPeers(m_Channel, new OnDiscoveryListener());
        m_ProgressBar.setVisibility(View.VISIBLE);
    }

    public void displayList(WifiP2pDeviceList list)
    {
        m_Devices.clear();
        for (WifiP2pDevice device : list.getDeviceList())
        {
            m_Devices.add(device);
        }

        m_DeviceList.getAdapter().notifyDataSetChanged();
    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(m_Receiver, m_IntentFilter);
    }

    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(m_Receiver);
    }

    class OnDiscoveryListener implements WifiP2pManager.ActionListener {

        @Override
        public void onSuccess() {
            Utils.showToast(getApplicationContext(), "Discovery was successful");
            m_ProgressBar.setVisibility(View.INVISIBLE);
            m_RefreshButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFailure(int reason) {

        }
    }
}
