package com.example.androidthings.gattserver;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Debug;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    public static UUID WRITE_BUTTON_MSG = UUID.fromString("d06e59b9-2ca8-490c-acb0-da8ccd3ad80d");
    public static UUID WRITE_DEVICE_NAME = UUID.fromString("a033590f-11e7-4eec-9674-8c57c3a1fa24");
    public static UUID READ_MSG = UUID.fromString("38c753cc-2078-4c18-9151-7fa7e5150eaa");

    private static String message = "default";
    HashMap<Gpio, String> gpioMessageMap = new HashMap<Gpio, String>();
    HashMap<Integer, Gpio> buttonNumGpioMap = new HashMap<Integer, Gpio>();

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

        BluetoothGattCharacteristic outputCharacteristic2 = new BluetoothGattCharacteristic(WRITE_BUTTON_MSG,
                //write characteristic,
                BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_WRITE);


        BluetoothGattCharacteristic outputCharacteristic3 = new BluetoothGattCharacteristic(WRITE_DEVICE_NAME,
                //write characteristic,
                BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_WRITE);

        service.addCharacteristic(inputCharacteristic);
        service.addCharacteristic(outputCharacteristic);
        service.addCharacteristic(outputCharacteristic2);
        service.addCharacteristic(outputCharacteristic3);

        return service;
    }

    // this is the only value that the device will be broadcasting
    // TODO: how to send multiple tiers of info. ie name + request
    public static byte[] getInputValue() {
        return message.getBytes();
    }

    // needs to be done from app
    public void setDeviceName(String name) {
        // TODO
    }

    // needs to be done from app
    public void setButtonVal(Integer buttonNum, String msg) {
        Gpio pin = buttonNumGpioMap.get(buttonNum);
        gpioMessageMap.put(pin, msg);
    }

    // sent upon receipt of initial message
    public static void resetOutputValue () {
        message = "default";
    }

    // connected to the gpio pins
    public void setCurrentMsg(Gpio button) {
        try {
            message = gpioMessageMap.get(button);
        } catch (Exception e) {
            Log.e(TAG, "button message not set up", e);
            message = "default";
        }
    }

}
