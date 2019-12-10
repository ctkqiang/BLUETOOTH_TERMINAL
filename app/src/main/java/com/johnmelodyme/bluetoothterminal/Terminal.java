package com.johnmelodyme.bluetoothterminal;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.util.Set;

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
    BluetoothDevice BD;
    TextView bluetooth_textView, Connected_device;
    ImageButton BLUETOOTH_SWITCH, SETTING;
    {
        REQUEST_CODE = 1;
    }

    @Override
    public void onStart(){
        super.onStart();
        System.out.println("APPLICATION STARTING...");
    }

    //ON START DECLARATION ::
    private void init_DECLARATION() {
        BA = BluetoothAdapter.getDefaultAdapter();
        BLUETOOTH_SWITCH = findViewById(R.id.BLUETOOTH_SWITCH);
        bluetooth_textView = findViewById(R.id.BT);
        SETTING = findViewById(R.id.Setting);
        Connected_device = findViewById(R.id.connectedDevice);
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
                // ON CLICK DISABLE BLUETOOTH:
                BA.disable();
                // SHOW MESSAGE OF EACH ACTION TAKEN || SET_TEXT_ BLUETOOTH_SWITCH ::
                if (!BA.isEnabled()){
                    BA.startDiscovery();
                    String bluetooth_is_ENABLED;
                    bluetooth_is_ENABLED = "BT: ON";
                    bluetooth_textView.setText(bluetooth_is_ENABLED);
                    Toast.makeText(Terminal.this, "BLUETOOTH ON",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    BA.cancelDiscovery();
                    String bluetooth_is_DISABLED;
                    bluetooth_is_DISABLED = "BT: OFF";
                    bluetooth_textView.setText(bluetooth_is_DISABLED);
                    Toast.makeText(Terminal.this, "BLUETOOTH OFF",
                            Toast.LENGTH_SHORT)
                            .show();
                }
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

        // OPEN APPLICATION SETTINGS:
        SETTING.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TO_SETTING;
                TO_SETTING = new Intent(Terminal.this,
                        pereference.class);
                startActivity(TO_SETTING);
            }
        });

        // SHOW THE TOAST_TEXT ON LONG PRESSED:
        SETTING.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Terminal.this, "SETTING",
                        Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        // QUERY PAIRED DEVICES:
        Set<BluetoothDevice> PAIRED_DEVICES = BA.getBondedDevices();
        if (PAIRED_DEVICES.size() > 0){
            // GET NAMES AND ADDRESSES OF EACH PAIRED DEVICES:
            for (BluetoothDevice DEVICE : PAIRED_DEVICES){
                String THREAD_NAME = Thread.currentThread().getName();
                String DEVICE_NAME;
                String DEVICE_ADDRESS;
                DEVICE_NAME = DEVICE.getName();
                DEVICE_ADDRESS = DEVICE.getAddress();
                // SET TEXT FOR DISPLAY::
                Connected_device.setText(DEVICE_NAME + DEVICE_ADDRESS + THREAD_NAME);
            }
        }
    }

    private void OPEN_DEVICE_BLUETOOTH_SETTINGS() {
        Intent intentOpenBluetoothSettings;
        intentOpenBluetoothSettings = new Intent();
        intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        Toast.makeText(Terminal.this, "OPENING BLUETOOTH SETTING",
                Toast.LENGTH_SHORT)
                .show();
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
