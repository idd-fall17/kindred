package com.example.androidthings.gattserver;
import com.google.android.things.pio.Gpio;
import com.example.androidthings.gattserver.CustomProfile;


/**
 * Created by kate on 10/18/17.
 */

public class TestApp extends SimplePicoPro {

    @Override
    public void setup() {
        //GPIO 128 for input
        pinMode(GPIO_128,Gpio.DIRECTION_IN);
        setEdgeTrigger(GPIO_128,Gpio.EDGE_BOTH);

        // GPIO 39 for Green LED
        pinMode(GPIO_39,Gpio.DIRECTION_OUT_INITIALLY_LOW);

        // GPIO 37 for Orange LED
        pinMode(GPIO_37,Gpio.DIRECTION_OUT_INITIALLY_LOW);

        // GPIO 35 for Red LED
        pinMode(GPIO_35,Gpio.DIRECTION_OUT_INITIALLY_LOW);
    }

    @Override
    public void loop() {
        //nothing to do here
    }

    @Override
    void digitalEdgeEvent(Gpio pin, boolean value) {
        println("digitalEdgeEvent"+pin+", "+value);

        //When GPIO goes form low to high to low, button is pressed
        if(pin==GPIO_128 && value==HIGH) {
            CustomProfile.setCurrentMsg(pin);
            turnOffAllLED();
            turnOnRedLED();
        }
    }

    void turnOffAllLED () {
        SimplePicoPro.digitalWrite(GPIO_35, false);
        SimplePicoPro.digitalWrite(GPIO_37, false);
        SimplePicoPro.digitalWrite(GPIO_39, false);
    }

    void turnOnGreenLED () {
        SimplePicoPro.pinMode(GPIO_39, 1);
    }

    void turnOnOrangeLED () {
        SimplePicoPro.pinMode(GPIO_37, 1);
    }

    void turnOnRedLED () {
        SimplePicoPro.digitalWrite(GPIO_35, true);
    }





}
