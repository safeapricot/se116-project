package com.objectville;

import com.objectville.engine.CityMap;
import com.objectville.engine.SimulationManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ObjectVilleGame {
    public static void main(String[] args){
        if(args.length!=2){ //nolur nolmaz yanlış girilirse diye.
           System.out.println("Error: Run the program with map file name and tick number. Invalid input.");
           return;
       }
       String mapFile = args[0];
       int tourNumber = Integer.parseInt(args[1]);

        System.out.println(mapFile);
//harita oluştur ve cell leri kontrol et
        CityMap map = new CityMap();
        map.loadMap(mapFile);
        map.generateCells();

        // simülasyon başlamadan log kaydı almayı başlatıyorum
        setupLog();

        SimulationManager sManager = new SimulationManager(map);//simülasyonu başlat

    }

    // konsol çıktılarını dosyaya kopyalayan sistem
    public static void setupLog() {
        // ilk olarak log klasörü
        try {
            File logDir = new File("logs");
            if (!logDir.exists()) {
                boolean isCreated = logDir.mkdirs(); // Sonucu bir değişkene atadık
                if (!isCreated) {
                    System.out.println("Error: There was an error creating the logs folder.");
                }
            }
            //output dosya adı
            DateTimeFormatter zamanLog = DateTimeFormatter.ofPattern("ddMMyyyy-HHmmss");
            String saat = LocalDateTime.now().format(zamanLog);
            String dosyaIsmi = "logs" + File.separator + "log-" + saat + ".txt";

            //dosya oluşturma ve konsola esitleme
            PrintStream file = new PrintStream(new File(dosyaIsmi));
            PrintStream console = System.out;

            // konsoldaki yazıları dosyaya kopyalayan method
            PrintStream copyConsole = new PrintStream(file) {
             @Override
             public void write(byte[] buf, int off, int len) {
                 super.write(buf, off, len);   // önce txt dosyasına
                 console.write(buf, off, len); // sonra aynı yazıyı ekrana yansıt
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
