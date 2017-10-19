package com.example.androidthings.gattserver;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Debug;
import android.util.Log;

import com.google.android.things.pio.Gpio;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

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
    public static UUID WRITE_BUTTON_MSG = UUID.fromString("d06e59b9-2ca8-490c-acb0-da8ccd3ad80d");
    public static UUID WRITE_DEVICE_NAME = UUID.fromString("a033590f-11e7-4eec-9674-8c57c3a1fa24");
    public static UUID WRITE_STUDENT_NAME = UUID.fromString("1c396d9b-a89b-4047-97b2-59bbda0ab88b");
    public static UUID READ_MSG = UUID.fromString("38c753cc-2078-4c18-9151-7fa7e5150eaa");

    private static String message = "default";
    private static String deviceName = "kindred";
    private static String studentName = "student";
    public static HashMap<Gpio, String> gpioMessageMap = new HashMap<Gpio, String>();
    public static HashMap<Integer, Gpio> buttonNumGpioMap = new HashMap<Integer, Gpio>();

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

        BluetoothGattCharacteristic outputCharacteristic4 = new BluetoothGattCharacteristic(WRITE_STUDENT_NAME,
                //write characteristic,
                BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_WRITE);

        service.addCharacteristic(inputCharacteristic);
        service.addCharacteristic(outputCharacteristic);
        service.addCharacteristic(outputCharacteristic2);
        service.addCharacteristic(outputCharacteristic3);
        service.addCharacteristic(outputCharacteristic4);

        return service;
    }

    // this is the only value that the device will be broadcasting
    // TODO: only broadcast on button push
    public static byte[] getInputValue() {
        // build json data
        JSONObject obj = new JSONObject();

        try {
            obj.put("name", studentName);
            obj.put("message", message);
        }
        catch (JSONException e) {
            Log.e(TAG, "error building json response", e);
        }

        // cast json payload to bytes
        return obj.toString().getBytes();
    }

    public static void setDeviceName(String name) {
        deviceName = name;
    }

    public static String getDeviceName() {
        return deviceName;
    }

    public static void setStudentName(String name) {
        studentName = name;
    }

    public static void setButtonVal(Integer buttonNum, String msg) {
        Gpio pin = buttonNumGpioMap.get(buttonNum);
        Log.d(TAG, "pin: " + pin);
        Log.d(TAG, "setButtonVal: msg" + msg);
        gpioMessageMap.put(pin, msg);
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
        try {
            Log.d(TAG, "setCurrentMsg: button" + button);
            Log.d(TAG, "current message: " + message);
            message = gpioMessageMap.get(button);
        } catch (Exception e) {
            Log.e(TAG, "button message not set up", e);
            message = "default";
        }
    }

}
