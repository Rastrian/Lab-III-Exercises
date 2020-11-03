import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import chronometer.Chronometer;

public class App {
    private static ArrayList<Chronometer> tempos;
    private static Chronometer chronometer;
    private static Chronometer generalTime;

    private static long convertNanoSecToSeconds(long l) {
        return (TimeUnit.SECONDS.convert(l, TimeUnit.NANOSECONDS));
    }

    private static int recFibonacci(int n) {
        return (n < 2) ? 1 : recFibonacci(n - 1) + recFibonacci(n - 2);
    }

    private static void fibonacci(int number) {
        generalTime = new Chronometer();
        tempos = new ArrayList<Chronometer>();
        chronometer = null;
        boolean limit = false;
        generalTime.start();
        System.out.println("Starting...");
        while (!limit) {
            chronometer = new Chronometer();
            chronometer.start();
            chronometer.setValue(recFibonacci(number));
            chronometer.stop();

            if (convertNanoSecToSeconds(chronometer.getTime()) > 30) {
                limit =  true;
            }

            tempos.add(chronometer);
            chronometer = null;
            number++;
        }
        generalTime.stop();
        System.out.println("General Time: "+convertNanoSecToSeconds(generalTime.getTime()));

        parseTimes("fibonacci.txt");
    }

    private static void parseTimes(String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))){
            tempos.forEach(t -> {
                try {
                    bw.write("Value: " + t.getValue() + " | Time: " + t.getTime() + " (Start: " + convertNanoSecToSeconds(t.getTimeStart())
                            + " -> End: " + convertNanoSecToSeconds(t.getTimeEnd()) + ")");
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        fibonacci(3);
    }
}
