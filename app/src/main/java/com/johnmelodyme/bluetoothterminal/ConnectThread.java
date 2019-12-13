package com.johnmelodyme.bluetoothterminal;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.sql.Connection;
import java.util.UUID;

public class ConnectThread extends Thread{
    private BluetoothSocket socket;
    private BluetoothDevice Device;
    private BluetoothAdapter bluetoothAdapter;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public ConnectThread(BluetoothDevice bluetoothDevice){
        BluetoothSocket tmp;
        tmp = null;
        Device = bluetoothDevice;
        try {
            tmp = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
            socket = null;
        }
    }

    public void run(){
        bluetoothAdapter.cancelDiscovery();
        try {
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void cancel(){
        try{
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
