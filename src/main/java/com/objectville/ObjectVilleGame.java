package com.objectville;

import com.objectville.engine.CityMap;
import com.objectville.engine.SimulationManager;
import com.objectville.exception.ConfigurationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ObjectVilleGame {
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                throw new ConfigurationException(
                        "Programı harita dosyası adı ve tick sayısı ile çalıştırın.");
            }
            String mapFile = args[0];
            int tourNumber = parseTicks(args[1]);

            setupLog();

            CityMap map = new CityMap();
            map.loadMap(mapFile);
            map.generateCells();

            new SimulationManager(map).run(tourNumber);

        } catch (ConfigurationException e) {
            System.out.println("Configuration error: " + e.getMessage());
        }
    }

    private static int parseTicks(String s) {
        int ticks;
        try {
            ticks = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Tick sayısı bir tam sayı olmalı: " + s);
        }
        if (ticks < 0) {
            throw new ConfigurationException("Tick sayısı negatif olamaz: " + ticks);
        }
        return ticks;
    }

    public static void setupLog() {
        try {
            File logDir = new File("logs");
            if (!logDir.exists()) logDir.mkdirs();

            DateTimeFormatter zamanLog = DateTimeFormatter.ofPattern("ddMMyyyy-HHmmss");
            String saat = LocalDateTime.now().format(zamanLog);
            String dosyaIsmi = "logs" + File.separator + "log-" + saat + ".txt";

            PrintStream file = new PrintStream(new File(dosyaIsmi));
            PrintStream console = System.out;

            PrintStream copyConsole = new PrintStream(file) {
                @Override
                public void write(byte[] buf, int off, int len) {
                    super.write(buf, off, len);
                    console.write(buf, off, len);
                }
                @Override
                public void flush() {
                    super.flush();
                    console.flush();
                }
            };
            System.setOut(copyConsole);
        } catch (FileNotFoundException e) {
            System.out.println("Error. There was an error with the log file. " + e.getMessage());
        }
    }
}