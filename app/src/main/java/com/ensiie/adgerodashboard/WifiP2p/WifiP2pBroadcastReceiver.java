package com.ensiie.adgerodashboard.WifiP2p;

/**
 * Created by lucas on 15/05/2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.provider.Settings;

import com.ensiie.adgerodashboard.Activities.WifiP2pPickerActivity;
import com.ensiie.adgerodashboard.Utils;

/**
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
public class WifiP2pBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager m_Manager;
    private WifiP2pManager.Channel m_Channel;
    private WifiP2pPickerActivity m_Activity;

    public WifiP2pBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                    WifiP2pPickerActivity activity) {
        super();
        this.m_Manager = manager;
        this.m_Channel = channel;
        this.m_Activity = activity;
    }

    private WifiP2pManager.PeerListListener m_PeerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peers) {
            Utils.showToast(m_Activity, "Peers found: " + peers.getDeviceList().size());
            m_Activity.displayList(peers);
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
            }
            else
            {
                // Wi-Fi P2P is not enabled
                Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                m_Activity.startActivity(i);
            }
        }
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            m_Manager.requestPeers(m_Channel, m_PeerListListener);
        }
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        }
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }
}

