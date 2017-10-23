package com.example.androidthings.gattserver;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.google.android.things.pio.Gpio;

import java.util.HashMap;
import java.util.UUID;

import static com.example.androidthings.gattserver.SimplePicoPro.GPIO_128;
import static com.example.androidthings.gattserver.SimplePicoPro.GPIO_39;

/**
 * A custom profile to hold Characteristics of your choice
 * Created by bjoern on 10/11/17.
 */

public class CustomProfile {
    private static final String TAG = CustomProfile.class.getSimpleName();
    private static int mCounter = 0;

    /* Generate your own with www.uuidgenerator.net */
    public static UUID CUSTOM_SERVICE = UUID.fromString("a9f34d86-2b08-4fc3-9f79-ff176bf6c8b8");
    public static UUID WRITE_RESET = UUID.fromString("64cd5877-b525-415b-bd04-573c18613f04");
    public static UUID READ_MSG = UUID.fromString("38c753cc-2078-4c18-9151-7fa7e5150eaa");

    public static HashMap<Gpio, String> gpioMessageMap = new HashMap<Gpio, String>();

    private static String message = "default";
    private static String deviceName = "kindred";

    public static HashMap<Gpio, String> getGpioMessageMap() {
        gpioMessageMap.put(GPIO_128, "128 msg");
        return gpioMessageMap;
    }

    /**
     * Return a configured {@link BluetoothGattService} instance for the
     * a custom Service.
     */
    public static BluetoothGattService createCustomService() {
        BluetoothGattService service = new BluetoothGattService(CUSTOM_SERVICE,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);

        // Input Characteristic
        BluetoothGattCharacteristic inputCharacteristic = new BluetoothGattCharacteristic(READ_MSG,
                //Read-only characteristic, supports notifications
                BluetoothGattCharacteristic.PROPERTY_READ,
                BluetoothGattCharacteristic.PERMISSION_READ);

        // Output Characteristic
        BluetoothGattCharacteristic outputCharacteristic = new BluetoothGattCharacteristic(WRITE_RESET,
                //write characteristic,
                BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_WRITE);

        service.addCharacteristic(inputCharacteristic);
        service.addCharacteristic(outputCharacteristic);

        return service;
    }

    public static String getDeviceName() {
        return deviceName;
    }

    // this is the only value that the device will be broadcasting
    // TODO: only broadcast on button push
    public static byte[] getInputValue() {
        return message.getBytes();
    }

    // sent upon receipt of initial message
    public static void acknowledgeAndRest () {
        // TODO: do something like turn on led
        // TODO: we should only broadcast when there is a message to broadcast
        SimplePicoPro.pinMode(GPIO_39, 1);
        message = "default";
    }

    // connected to the gpio pins
    public static void setCurrentMsg(Gpio button) {
        HashMap<Gpio, String> map = getGpioMessageMap();
        message = map.get(button);
    }

}
