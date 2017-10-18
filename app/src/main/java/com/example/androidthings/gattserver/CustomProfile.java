package com.example.androidthings.gattserver;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Debug;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.util.UUID;

/**
 * A custom profile to hold Characteristics of your choice
 * Created by bjoern on 10/11/17.
 */

public class CustomProfile extends SimplePicoPro {
    private static final String TAG = CustomProfile.class.getSimpleName();
    private static int mCounter = 0;

    /* Generate your own with www.uuidgenerator.net */
    public static UUID CUSTOM_SERVICE = UUID.fromString("a9f34d86-2b08-4fc3-9f79-ff176bf6c8b8");
    public static UUID WRITE_COUNTER = UUID.fromString("64cd5877-b525-415b-bd04-573c18613f04");
    public static UUID READ_COUNTER = UUID.fromString("38c753cc-2078-4c18-9151-7fa7e5150eaa");

    private static String message = "test";
    private static String message_1 = "break";

    @Override
    public void setup() {
        println("Setting up");
        //set two GPIOs to input
        pinMode(GPIO_128,Gpio.DIRECTION_IN);
        setEdgeTrigger(GPIO_128,Gpio.EDGE_BOTH);
    }

    @Override
    public void loop() {
        //nothing to do here
    }

    @Override
    void digitalEdgeEvent(Gpio pin, boolean value) {
        println("digitalEdgeEvent"+pin+", "+value);
        // when 128 goes from LOW to HIGH
        // this is on button button release for pull-up resistors
        if(pin==GPIO_128 && value==HIGH) {
            println("BUTTON HITTING!!!!");
            message = message_1;
        }
    }

    /**
     * Return a configured {@link BluetoothGattService} instance for the
     * a custom Service.
     */
    public static BluetoothGattService createCustomService() {
        BluetoothGattService service = new BluetoothGattService(CUSTOM_SERVICE,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);

        // Input Characteristic
        BluetoothGattCharacteristic inputCharacteristic = new BluetoothGattCharacteristic(READ_COUNTER,
                //Read-only characteristic, supports notifications
                BluetoothGattCharacteristic.PROPERTY_READ,
                BluetoothGattCharacteristic.PERMISSION_READ);

        // Output Characteristic
        BluetoothGattCharacteristic outputCharacteristic = new BluetoothGattCharacteristic(WRITE_COUNTER,
                //write characteristic,
                BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_WRITE);

        service.addCharacteristic(inputCharacteristic);
        service.addCharacteristic(outputCharacteristic);

        return service;
    }

    public static byte[] getInputValue() {
        //byte[] field = new byte[1];
        //field[0] = (byte) mCounter;
        //mCounter = (mCounter+1) %128;
        return message.getBytes();
    }

    public static void setOutputValue (byte[] value) {
        message_1 = value.toString();
        //handle output here
        //mCounter = value[0];
    }
}
