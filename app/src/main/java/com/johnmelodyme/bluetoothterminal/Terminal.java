package com.johnmelodyme.bluetoothterminal;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
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
    {
        REQUEST_CODE = 1;
    }

    public void onStart(){
        super.onStart();
    }

    private void init_DECLARATION() {
        BA = BluetoothAdapter.getDefaultAdapter();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_DECLARATION();
        CHECK_BLUETOOTH();
        ON_BLUETOOTH();
    }

    private void ON_BLUETOOTH() {
        if (!(BA.isEnabled())){
            BM.getAdapter()
                    .enable();
        }
        else {
            String message = "BLUETOOTH ERROR";
            System.out.println(message);
        }
    }

    private void CHECK_BLUETOOTH() {
        BLUETOOTH_SUPPORTABIILITY();
        if (!(BA.isEnabled())){
            Intent REQUEST_BLUETOOTH;
            REQUEST_BLUETOOTH = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(REQUEST_BLUETOOTH, REQUEST_CODE);
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
