package com.johnmelodyme.bluetoothterminal;

/*
 * Copyright 2020 John Melody Me
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @CREATOR: JOHN MELODY MELISSA ESKHOLAZHT .C.T.K.;
 * @DATETIME: 12/12/2019;
 * @COPYRIGHT: 2019 - 2023;
 * @PROJECTNAME: BLUETOOTH LOW ENERGY TERMINAL;
 * @ACTIVITY: TERMINAL;
 */

public class Terminal extends AppCompatActivity {
    String ConnectedDeviceName = null;
    public static String MAC_ADDRESS;
    int REQUEST_CODE;
    int SYSTEM_EXIT_STATUS;
    int BYTE_COUNT;
    byte [] READ_BUFFER;
    InputStream inputStream;
    OutputStream outputStream;
    BluetoothAdapter BA;
    BluetoothSocket BS;
    BluetoothDevice BM;
    BluetoothDevice BD;
    List<String> deviceARRAY = new ArrayList<>();
    List<BluetoothDevice> deviceList = new ArrayList<>();
    ArrayAdapter<String> AA;
    ArrayAdapter arrayAdapter_term;
    ConnectThread connectThread;
    TextView bluetooth_textView, Connected_device, device_TV, found_device;
    ImageButton BLUETOOTH_SWITCH, SETTING;
    ListView List_of_device, READ_BUFFER_LV;
    Button READ, SAVE_DATA;
    UUID uuid;
    {
        READ_BUFFER = new byte[1024];
        //Standard SerialPortService ID:
        uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        REQUEST_CODE = 1;
        SYSTEM_EXIT_STATUS = 0;
    }

    // ON START
    @Override
    public void onStart(){
        super.onStart();
        System.out.println("APPLICATION STARTING...");
        BLUETOOTH_SUPPORTABIILITY();
    }

    // ON DESTROY:
    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * @METHOD: init_DECLARACTION;
     * @NAME: ON START DECLARATION ::
     */
    private void init_DECLARATION(){
        BA = BluetoothAdapter.getDefaultAdapter();

        BLUETOOTH_SWITCH = findViewById(R.id.BLUETOOTH_SWITCH);
        bluetooth_textView = findViewById(R.id.BT);
        SETTING = findViewById(R.id.Setting);
        Connected_device = findViewById(R.id.connectedDevice);
        READ = findViewById(R.id.read);
        List_of_device = findViewById(R.id.listofdevice);
        List_of_device.setBackgroundColor(Color.TRANSPARENT);
        device_TV = findViewById(R.id.dev);
        SAVE_DATA = findViewById(R.id.Save_data);
        READ_BUFFER_LV = findViewById(R.id.read_buffer);
        // HC-05 :
        MAC_ADDRESS = "00:18:E4:40:00:06";
    }

    private void TERMINAL_SCREEN() {
        String BUFFERED_DATA [] = new String[]{"0A", "78" , "0X0A"};
        arrayAdapter_term = new ArrayAdapter<String>(this, R.layout.termview, BUFFERED_DATA);
        READ_BUFFER_LV.setAdapter(arrayAdapter_term);
    }

    /*
    private void OPEN_BLUETOOTH() throws IOException {
        BS = BD.createRfcommSocketToServiceRecord(uuid);
        BS.connect();
        outputStream = BS.getOutputStream();
        inputStream = BS.getInputStream();
    }
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // REFER TO THE METHODS::
        init_DECLARATION();
        BLUETOOTH_SUPPORTABIILITY();

        if (BA.isDiscovering()){
            BA.cancelDiscovery();
        }
        BA.startDiscovery();

        IntentFilter BROAD;
        BROAD = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(receiver, BROAD);

        // ON CLICKED CONNECTIVITY:
        BLUETOOTH_SWITCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ENABLE BLUETOOTH _ REQUEST::
                // CHECK_BLUETOOTH()
                // SHOW MESSAGE OF EACH ACTION TAKEN || SET_TEXT_ BLUETOOTH_SWITCH ::
                if (!BA.isEnabled()){
                    BA.enable();
                    BA.startDiscovery();
                    String bluetooth_is_ENABLED;
                    bluetooth_is_ENABLED = "BT: ON";
                    bluetooth_textView.setText(bluetooth_is_ENABLED);
                    Toast.makeText(Terminal.this, "BLUETOOTH ON",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    BA.disable();
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

        CONNECT_BLUETOOTH();

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
        ArrayList arrayList = new ArrayList();
        if (PAIRED_DEVICES.size() > 0){
            // GET NAMES AND ADDRESSES OF EACH PAIRED DEVICES:
            for (BluetoothDevice DEVICE : PAIRED_DEVICES){
                String THREAD_NAME = Thread.currentThread().getName();
                String DEVICE_NAME;
                String DEVICE_ADDRESS;
                DEVICE_NAME = DEVICE.getName();
                DEVICE_ADDRESS = DEVICE.getAddress();
                // SET TEXT FOR DISPLAY::
                //Connected_device.setText(DEVICE_NAME + DEVICE_ADDRESS + THREAD_NAME);
                Connected_device.setText(DEVICE_ADDRESS);
                arrayList.add(DEVICE_NAME + " [" + DEVICE_ADDRESS + "]");
            }
            AA = new ArrayAdapter(Terminal.this, R.layout.support_simple_spinner_dropdown_item, arrayList);
            List_of_device.setAdapter(AA);
        } else  {
            System.out.println("$NULL");
        }

        List_of_device.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView::
                String CLICKED_DEVICE_NAME;
                CLICKED_DEVICE_NAME = (String) parent.getItemAtPosition(position);
                if (CLICKED_DEVICE_NAME.equals("HC-05")){
                    try {
                        Method method = BD.getClass().getMethod("createBond", (Class[]) null);
                        method.invoke(BD, (Object[]) null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(Terminal.this, "Clicked \"HC-05\" ", Toast.LENGTH_SHORT).show();
                } else if (CLICKED_DEVICE_NAME.equals("HC-06")){
                    try {
                        Method method = BD.getClass().getMethod("createBond", (Class[]) null);
                        method.invoke(BD, (Object[]) null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("OPPS");
                }
            }
        });

        READ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TERMINAL_SCREEN();
            }
        });

        SAVE_DATA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EXTERNAL_DRIVE_AVAILABLE() || EXTERNAL_DRIVE_READ_ONLY()) {
                    SAVE_DATA.setEnabled(false);
                }
            }
        });
    }

    private void CONNECT_BLUETOOTH() {
        String action = getIntent().getAction();
        // When discovery finds a device_TV
        if (BluetoothDevice.ACTION_FOUND.equals(action)){
            // Get the BluetoothDevice object from the Intent
            BluetoothDevice device = getIntent().getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            // If it's already paired, skip it, because it's been listed already
            if (device.getBondState() != BluetoothDevice.BOND_BONDED)
            {
                device_TV.setText(device.getName());
            }
        }

        IntentFilter BROAD;
        BROAD = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(receiver, BROAD);

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

    /**
     * @METHOD: onBackPressed;
     * @NAME: DIALOG ON BACK PRESS;
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Terminal.this);
        builder.setMessage("Do you want to exit ?");
        builder.setTitle("ARE YOU SURE?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Broadcast ::
    // Create a BroadcastReceiver for ACTION_FOUND::
    private final BroadcastReceiver receiver = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device_TV
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                int state;
                int prev_state;
                state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                prev_state = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
                if (state == BluetoothDevice.BOND_BONDED && prev_state == BluetoothDevice.BOND_BONDING) {
                    String PAIREDE;
                    PAIREDE = "Paired";
                    Toast.makeText(getApplicationContext(), PAIREDE,
                            Toast.LENGTH_SHORT)
                            .show();
                } else if (state == BluetoothDevice.BOND_NONE && prev_state == BluetoothDevice.BOND_BONDED){
                    String UNPAIREDE;
                    UNPAIREDE = "Unpaired";
                    Toast.makeText(getApplicationContext(), UNPAIREDE,
                            Toast.LENGTH_SHORT)
                            .show();
                }
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                found_device.setText("Device Found: " + device.getName().toUpperCase());
                /*
                // Add the name and address to an array adapter to show in a ListView::
                String DD = device.getName();
                deviceARRAY.add(DD);
                 *
                 */
            }
        }
    };

    /*
    private void showDeviceSelecterDialog(ArrayList deviceStrs
            , final ArrayList devices) {
        // show list
        final AlertDialog.Builder alertDialog =
                new AlertDialog.Builder(this);

        ArrayAdapter adapter =
                new ArrayAdapter(this,
                        android.R.layout.select_dialog_singlechoice,
                        deviceStrs.toArray(new String[deviceStrs.size()]));

        alertDialog.setSingleChoiceItems(adapter, -1,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        int position = ((AlertDialog) dialog)
                                .getListView()
                                .getCheckedItemPosition();
                        String deviceAddress = (String) devices.get(position);
                    }
                });

        alertDialog.setTitle("Choose Bluetooth device");
        alertDialog.show();
    }
     */

    private static boolean EXTERNAL_DRIVE_AVAILABLE(){
        String EXTERNAL_STORAGE_STATE;
        EXTERNAL_STORAGE_STATE = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(EXTERNAL_STORAGE_STATE)){
            return true;
        }
        return false;
    }

    private static boolean EXTERNAL_DRIVE_READ_ONLY(){
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }


}