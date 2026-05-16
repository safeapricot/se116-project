package com.objectville.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CityMap {

    // mapi tutmak icin gereken karakter arrayi
    private char[][] grid;


    // haritayı yüklemek için şimdilik test amaçlı olan metod
    public void loadMap(String filePath) {
        // satırları tutmak için gereken arraylist, çünkü mapin uzunluğunu önceden bilemeyiz.
        ArrayList<String> lines = new ArrayList<String>();

        File file = new File(filePath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            System.out.println("Loading Map...");
            System.out.println("-----------------");

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                lines.add(line);
            }
            System.out.println("-----------------");
            System.out.println("Map successfully loaded!");

            // sütün ve satır sayılarını alıyoruz
            int rowCount = lines.size();
            int colCount = lines.get(0).length(); // <- ilk satırın uzunluğunu sütün sayısı olarak aldık

            // bize asıl gereken arrayı oluşturma
            grid = new char[rowCount][colCount];
            for (int i = 0; i < rowCount; i++) {
                grid[i] = lines.get(i).toCharArray();
            }


        } catch (IOException e) {
            System.out.println("Error loading Map!" + e.getMessage());
        }
    }
    public char[][] getGrid() {
        return grid;
    }
}
