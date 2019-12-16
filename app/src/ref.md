
** https://stackoverflow.com/questions/4715865/how-to-programmatically-tell-if-a-bluetooth-device-is-connected#

public static boolean isBluetoothHeadsetConnected() {
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
            && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED;
}

https://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
https://stackoverflow.com/questions/40590908/how-do-i-receive-bluetooth-data-from-arduino-to-android
https://solidgeargroup.com/discovery-bluetooth-devices-android/
http://www.londatiga.net/it/programming/android/how-to-programmatically-pair-or-unpair-android-bluetooth-device/
http://mcuhq.com/27/simple-android-bluetooth-application-with-arduino-example
