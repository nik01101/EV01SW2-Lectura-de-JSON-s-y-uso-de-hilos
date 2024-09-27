package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
    public static void clasificacionGlucosa() throws InterruptedException {
        
        int size = 10;
        CountDownLatch latch = new CountDownLatch(3);
        Random random = new Random();

        // Crear 3 arrays con niveles de glucosa aleatorios
        int[] glucosaNiveles1 = random.ints(size, 70, 180).toArray();
        int[] glucosaNiveles2 = random.ints(size, 70, 180).toArray();
        int[] glucosaNiveles3 = random.ints(size, 70, 180).toArray();

        // Mostrar niveles de glucosa generados
        System.out.println("Niveles de glucosa hilo 1: " + Arrays.toString(glucosaNiveles1));
        System.out.println("Niveles de glucosa hilo 2: " + Arrays.toString(glucosaNiveles2));
        System.out.println("Niveles de glucosa hilo 3: " + Arrays.toString(glucosaNiveles3));

        // Crear hilos para clasificar los arrays
        clasificarGlucosa  clasificar1 = new clasificarGlucosa (glucosaNiveles1, latch);
        clasificarGlucosa  clasificar2 = new clasificarGlucosa (glucosaNiveles2, latch);
        clasificarGlucosa  clasificar3 = new clasificarGlucosa (glucosaNiveles3, latch);

        Thread thread1 = new Thread(clasificar1);
        Thread thread2 = new Thread(clasificar2);
        Thread thread3 = new Thread(clasificar3);

        // Iniciar los hilos
        thread1.start();
        thread2.start();
        thread3.start();

        // Esperar a que todos los hilos terminen
        latch.await();

        // Juntar resultados
        int[] resultadosCombinados = new int[size * 3];
        System.arraycopy(clasificar1.getClasificaciones(), 0, resultadosCombinados, 0, size);
        System.arraycopy(clasificar2.getClasificaciones(), 0, resultadosCombinados, size, size);
        System.arraycopy(clasificar3.getClasificaciones(), 0, resultadosCombinados, size * 2, size);

        System.out.println("Resultados en total: " + Arrays.toString(resultadosCombinados));

        // Contar clasificaciones
        int normalCantidad = 0, prediabetesCantidad = 0, diabetesCantidad = 0;
        for (int clasificacion : resultadosCombinados) {
            if (clasificacion == 0) normalCantidad++;
            else if (clasificacion == 1) prediabetesCantidad++;
            else if (clasificacion == 2) diabetesCantidad++;
        }

        // Calcular porcentajes
        double total = resultadosCombinados.length;
        double normalPorcentaje = (normalCantidad / total) * 100;
        double prediabetesPorcentaje = (prediabetesCantidad / total) * 100;
        double diabetesPorcentaje = (diabetesCantidad / total) * 100;

        // Mostrar resultados
        System.out.println("Clasificación de resultados:");
        System.out.println("Normal: " + String.format("%.1f", normalPorcentaje) + "%");
        System.out.println("Prediabetes: " + String.format("%.1f", prediabetesPorcentaje) + "%");
        System.out.println("Diabetes: " + String.format("%.1f", diabetesPorcentaje) + "%");
    }

    public static void leerArchivo() {
        String jsonFilePath = "E:/Servicios Web 2/demo/src/main/java/com/example/json/cuentas.json";
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            List<Cuenta> cuentas = objectMapper.readValue(new File(jsonFilePath), new TypeReference<List<Cuenta>>(){});
            
            for (Cuenta cuenta : cuentas) {
                if (cuenta.isEstado()) {
                    generarArchivo(cuenta);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generarArchivo(Cuenta cuenta) {
        String fileName = cuenta.getNro_cuenta() + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            if (cuenta.getSaldo() > 5000.00) {
                writer.write("Banco de origen: " + cuenta.getBanco() + "\n");
                writer.write("La cuenta con el nro de cuenta: " + cuenta.getNro_cuenta() +
                        " tiene un saldo de " + String.format("%.4f", cuenta.getSaldo()) + "\n");
                writer.write("Usted es apto a participar en el concurso de la SBS por 10000.00 soles.\n");
                writer.write("¡Suerte!\n");
            } else {
                writer.write("Banco de origen: " + cuenta.getBanco() + "\n");
                writer.write("La cuenta con el nro de cuenta: " + cuenta.getNro_cuenta() +
                        " no tiene un saldo superior a 5000.00.\n");
                writer.write("Lamentablemente no podrá acceder al concurso de la SBS por 10000.00 soles.\n");
                writer.write("Gracias\n");
            }
            System.out.println("Archivo generado: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Clasificar niveles de glucosa");
            System.out.println("2. Leer JSON y crear archivos para cuentas");
            System.out.println("3. Salir");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    clasificacionGlucosa();
                    exit = true;
                    break;
                case 2:
                    leerArchivo();
                    break;
                case 3:
                    System.out.println("Saliendo...");
                    exit = true;
                    break;
                default:
                    System.out.println("intente nuevamente.");
                    break;
            }
        }

        scanner.close();
    }
}
