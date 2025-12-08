package ch.qos.logback.crac;

import java.util.stream.IntStream;

/**
 * A simple application to demonstrate CRaC.
 */
public class CracApp {

    public static void main(String args[]) throws InterruptedException {
        // This is a part of the saved state
        long startTime = System.currentTimeMillis();
        for(int counter: IntStream.range(1, 100).toArray()) {
            Thread.sleep(1000);
            long currentTime = System.currentTimeMillis();
            System.out.println("Counter: " + counter + "(passed " + (currentTime-startTime) + " ms)");
            startTime = currentTime;
        }
    }
}


