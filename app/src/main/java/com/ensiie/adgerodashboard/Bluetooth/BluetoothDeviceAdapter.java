package com.ensiie.adgerodashboard.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ensiie.adgerodashboard.R;

import java.util.List;

/**
 * Created by lucas on 04/05/2017.
 * Adapter to display bluetooth devices in a recycler view.
 */
public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.BluetoothDeviceViewHolder> {

    /**
     * A pointer to the dataset of this adapter. Here, a list of bluetooth devices to display.
     * A pointer is used so that every time the dataset is changed, we can call notifyDatasetChanged()
     * to refresh the recycler view.
     */
    private List<BluetoothDevice> m_Dataset;

    private BluetoothDeviceAdapter()
    {
        // Empty private default constructor to force use of second constructor
    }

    public BluetoothDeviceAdapter(List<BluetoothDevice> dataset)
    {
        m_Dataset = dataset;
    }

    @Override
    public BluetoothDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bluetooth_device, parent, false);
        BluetoothDeviceViewHolder holder = new BluetoothDeviceViewHolder(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO establish connection with the selected device here
                // (retrieve the tag of the view to have device's mac address)
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(BluetoothDeviceViewHolder holder, int position) {
        BluetoothDevice device = m_Dataset.get(position);
        holder.m_DeviceName.setText(device.getName());
        holder.itemView.setTag(device.getAddress());
    }

    @Override
    public int getItemCount() {
        return m_Dataset.size();
    }

    static class BluetoothDeviceViewHolder extends RecyclerView.ViewHolder
    {
        private TextView m_DeviceName;

        BluetoothDeviceViewHolder(View itemView) {
            super(itemView);
            m_DeviceName = (TextView)itemView.findViewById(R.id.deviceName);
        }
    }


}
