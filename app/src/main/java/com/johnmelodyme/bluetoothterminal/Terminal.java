package com.johnmelodyme.bluetoothterminal;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @CREATOR: JOHN MELODY MELISSA ESKHOLAZHT .C.T.K.;
 * @DATETIME: 16/12/2019;
 * @COPYRIGHT: 2019 - 2023;
 * @PROJECTNAME: BLUETOOTH LOW ENERGY TERMINAL;
 * @ACTIVITY: TERMINAL;
 */

public class Terminal extends AppCompatActivity {
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
    ConnectThread connectThread;
    TextView bluetooth_textView, Connected_device, device_TV;
    ImageButton BLUETOOTH_SWITCH, SETTING;
    ListView List_of_device;
    Button READ;
    UUID uuid;

    {
        READ_BUFFER = new byte[1024];
        //Standard SerialPortService ID:
        uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        REQUEST_CODE = 1;
        SYSTEM_EXIT_STATUS = 0;
    }

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
                arrayList.add(DEVICE_NAME);


            }
            AA = new ArrayAdapter(Terminal.this, R.layout.support_simple_spinner_dropdown_item, arrayList);
            List_of_device.setAdapter(AA);
        } else  {
            System.out.println("$NULL");
        }

        List_of_device.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice bluetooth_devices;
                bluetooth_devices = (BluetoothDevice) List_of_device.getItemAtPosition(position);
                try {
                    String CREATE_BOND = "CREATE_BOND";
                    Method method = bluetooth_devices.getClass().getMethod(CREATE_BOND,(Class[])null);
                    method.invoke(bluetooth_devices, (Object[])null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*
                String selected = (String) parent.getItemAtPosition(position);
                String deviceSELECTED = BD.getName();
                if (selected.equals("HC-05")){
                    if (BD.getName().equals("HC-05")){
                        connectThread = new ConnectThread(BD);
                        connectThread.start();
                    }
                }

                if(BD.getName().equals("HC-05")){
                    BM.createBond();
                } else {
                    Toast.makeText(Terminal.this, "Please Connect a \"HC-05\"",
                            Toast.LENGTH_SHORT)
                            .show();
                }
                 */
            }
        });

        READ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceList.add(device);
                // Add the name and address to an array adapter to show in a ListView::
                String DD = device.getName();
                deviceARRAY.add(DD);

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
}
