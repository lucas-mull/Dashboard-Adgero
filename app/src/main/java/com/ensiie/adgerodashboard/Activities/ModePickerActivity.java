package com.ensiie.adgerodashboard.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ensiie.adgerodashboard.Structures.DataReceptionMode;
import com.ensiie.adgerodashboard.DataSingleton;
import com.ensiie.adgerodashboard.R;
import com.ensiie.adgerodashboard.Utils;

public class ModePickerActivity extends AppCompatActivity {

    /**
     * Button to start bluetooth mode
     */
    private Button m_BluetoothButton;

    /**
     * Button to start direct wifi mode
     */
    private Button m_DWifiButton;

    /**
     * Button to start simulation mode
     */
    private Button m_SimulationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_picker);

        // Display Adgero logo in action bar
        Utils.setupLogoInActionBar(this);

        // Get the buttons
        m_BluetoothButton = (Button)findViewById(R.id.bluetoothButton);
        m_DWifiButton = (Button)findViewById(R.id.directWifiButton);
        m_SimulationButton = (Button)findViewById(R.id.simulationButton);

        m_BluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BluetoothPickerActivity.class);
                startActivity(intent);
            }
        });

        m_DWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WifiP2pPickerActivity.class);
                startActivity(intent);
            }
        });

        m_SimulationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSingleton.Instance.setDataReceptionMode(DataReceptionMode.SIMULATION);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
