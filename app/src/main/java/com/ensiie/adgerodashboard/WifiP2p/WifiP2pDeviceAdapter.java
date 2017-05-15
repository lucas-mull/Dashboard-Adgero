package com.ensiie.adgerodashboard.WifiP2p;

import android.net.wifi.p2p.WifiP2pDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ensiie.adgerodashboard.R;

import java.util.List;

/**
 * Created by lucas on 04/05/2017.
 */
public class WifiP2pDeviceAdapter extends RecyclerView.Adapter<WifiP2pDeviceAdapter.WifiP2pDeviceViewHolder> {

    /**
     * A pointer to the dataset of this adapter. Here, a list of bluetooth devices to display.
     * A pointer is used so that every time the dataset is changed, we can call notifyDatasetChanged()
     * to refresh the recycler view.
     */
    private List<WifiP2pDevice> m_Dataset;

    private WifiP2pDeviceAdapter()
    {
        // Empty private default constructor to force use of second constructor
    }

    public WifiP2pDeviceAdapter(List<WifiP2pDevice> dataset)
    {
        m_Dataset = dataset;
    }

    @Override
    public WifiP2pDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifip2p_device, parent, false);
        WifiP2pDeviceViewHolder holder = new WifiP2pDeviceViewHolder(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO handle connexion process here
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(WifiP2pDeviceViewHolder holder, int position) {
        WifiP2pDevice device = m_Dataset.get(position);
        holder.m_DeviceName.setText(device.deviceName);
        holder.itemView.setTag(device.deviceAddress);
    }

    @Override
    public int getItemCount() {
        return m_Dataset.size();
    }

    static class WifiP2pDeviceViewHolder extends RecyclerView.ViewHolder
    {
        private TextView m_DeviceName;

        WifiP2pDeviceViewHolder(View itemView) {
            super(itemView);
            m_DeviceName = (TextView)itemView.findViewById(R.id.deviceName);
        }
    }


}
