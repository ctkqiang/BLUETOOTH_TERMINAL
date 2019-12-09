package com.johnmelodyme.bluetoothterminal;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @CREATOR: JOHN MELODY MELISSA ESKHOLAZHT .C.T.K.
 * @DATETIME: 12/12/2019
 * @COPYRIGHT: 2019 - 2023
 * @PROJECTNAME: BLUETOOTH LOW ENERGY TERMINAL
 */
public class Terminal extends AppCompatActivity {
    // DECLARATION :
    int REQUEST_CODE;
    BluetoothAdapter BA;
    BluetoothManager BM;
    ImageButton BLUETOOTH_SWITCH, SETTING;
    {
        REQUEST_CODE = 1;
    }

    public void onStart(){
        super.onStart();
        System.out.println("APPLICATION STARTING...");
    }

    private void init_DECLARATION() {
        BA = BluetoothAdapter.getDefaultAdapter();
        BLUETOOTH_SWITCH = findViewById(R.id.BLUETOOTH_SWITCH);
        SETTING = findViewById(R.id.Setting);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // REFER TO THE METHODS::
        init_DECLARATION();
        BLUETOOTH_SUPPORTABIILITY();

        // ON CLICKED CONNECTIVITY:
        BLUETOOTH_SWITCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ENABLE BLUETOOTH _ REQUEST::
                //CHECK_BLUETOOTH()
                BA.enable();
                Toast.makeText(Terminal.this, "BLUETOOTH ON",
                        Toast.LENGTH_SHORT)
                        .show();
                // ON CLICK DISABLE BLUETOOTH:
                BA.disable();
                Toast.makeText(Terminal.this, "BLUETOOTH OFF",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        // OPEN DEVICE BLUETOOTH SETTING ON LONG PRESS:
        BLUETOOTH_SWITCH.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // WHEN LONG PRESS ON BUTTON DETECTED, RUN "OPEN_DEVICE_BLUETOOTH_SETTINGS();" METHOD::
                OPEN_DEVICE_BLUETOOTH_SETTINGS(); // REFER METHOD
                return false;
            }
        });
    }

    private void OPEN_DEVICE_BLUETOOTH_SETTINGS() {
        Intent intentOpenBluetoothSettings;
        intentOpenBluetoothSettings = new Intent();
        intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intentOpenBluetoothSettings);
    }

    private void CHECK_BLUETOOTH() {
        if (!(BA.isEnabled())){
            String msg = "BLUETOOTH_REQUEST";
            System.out.println(msg);
            Intent REQUEST_BLUETOOTH;
            REQUEST_BLUETOOTH = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(REQUEST_BLUETOOTH, REQUEST_CODE);
        } else {
            String msg = "SOMETHING IS WRONG";
            System.out.println(msg);
        }
    }

    private void BLUETOOTH_SUPPORTABIILITY() {
        if (BA == null){
            String BNS;
            BNS = "BLUETOOTH IS NOT SUPPORTED IN THIS DEVICE.";
            Toast.makeText(Terminal.this, BNS,
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
