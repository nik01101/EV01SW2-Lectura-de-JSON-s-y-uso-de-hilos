package com.example;
import java.util.concurrent.CountDownLatch;

public class clasificarGlucosa  implements Runnable {

    private int[] glucosaNiveles;
    private int[] clasificaciones; // 0: Normal, 1: Prediabetes, 2: Diabetes
    private CountDownLatch latch;

    public clasificarGlucosa (int[] glucosaNiveles, CountDownLatch latch) {
        this.glucosaNiveles = glucosaNiveles;
        this.clasificaciones = new int[glucosaNiveles.length];
        this.latch = latch;
    }
    @Override
    public void run() {
        clasificarGlucosaNiveles();
        latch.countDown(); 
    }
    private void clasificarGlucosaNiveles() {
        for (int i = 0; i < glucosaNiveles.length; i++) {
            if (glucosaNiveles[i] < 99) {
                clasificaciones[i] = 0; // Normal
            } else if (glucosaNiveles[i] <= 125) {
                clasificaciones[i] = 1; // Prediabetes
            } else {
                clasificaciones[i] = 2; // Diabetes
            }
        }
    }

    public int[] getClasificaciones() {
        return clasificaciones;
    }

}
