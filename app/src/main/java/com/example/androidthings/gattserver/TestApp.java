package com.example.androidthings.gattserver;
import com.google.android.things.pio.Gpio;
import com.example.androidthings.gattserver.CustomProfile;


/**
 * Created by kate on 10/18/17.
 */

public class TestApp extends SimplePicoPro {

    @Override
    public void setup() {
        //set two GPIOs to input
        pinMode(GPIO_128,Gpio.DIRECTION_IN);
        setEdgeTrigger(GPIO_128,Gpio.EDGE_BOTH);
        CustomProfile.buttonNumGpioMap.put(0, GPIO_128);

        // this is for led
        pinMode(GPIO_39,Gpio.DIRECTION_IN);
        setEdgeTrigger(GPIO_39,Gpio.EDGE_BOTH);
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
            CustomProfile.setCurrentMsg(pin);
        }
    }

}
