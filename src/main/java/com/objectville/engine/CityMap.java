package com.objectville.engine;

import com.objectville.model.Cell;
import com.objectville.model.Empty;
import com.objectville.model.Road;
import com.objectville.model.zones.*;
import com.objectville.model.services.*;
import com.objectville.model.providers.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CityMap {

    // mapi tutmak icin gereken karakter arrayi
    private char[][] charGrid;
    private Cell[][] cellGrid;

    // haritayı yüklemek için şimdilik test amaçlı olan metod
    public void loadMap(String filePath) {
        // satırları tutmak için gereken arraylist, çünkü mapin uzunluğunu önceden bilemeyiz.
        ArrayList<String> lines = new ArrayList<String>();

        File file = new File(filePath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            System.out.println("Loading Map...");

            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            System.out.println("Map successfully loaded!");

            // sütün ve satır sayılarını alıyoruz
            int rowCount = lines.size();
            int colCount = lines.get(0).length(); // <- ilk satırın uzunluğunu sütün sayısı olarak aldık

            // bize asıl gereken arrayı oluşturma
            charGrid = new char[rowCount][colCount];
            for (int i = 0; i < rowCount; i++) {
                charGrid[i] = lines.get(i).toCharArray();
            }


        } catch (IOException e) {
            System.out.println("Error loading Map!" + e.getMessage());
        }
    }

    // Harflere classları atama methodu switch case ile
    public void generateCells() {
        if (charGrid == null) {
            System.out.println("Error: map must be loaded first!");
            return;
        }

        int rowCount = charGrid.length;
        int colCount = charGrid[0].length;
        cellGrid = new Cell[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                char letter = charGrid[i][j];

                switch (letter) {
                    case 'E':
                        cellGrid[i][j] = new Empty(i, j);
                        break;
                    case 'R':
                        cellGrid[i][j] = new Road(i, j);
                        break;
                    case 'T':
                        cellGrid[i][j] = new InternetHub(i, j);
                        break;
                    case 'W':
                        cellGrid[i][j] = new WaterPumpingStation(i, j);
                        break;
                    case 'I':
                        cellGrid[i][j] = new Industrial(i, j);
                        break;
                    case 'S':
                        cellGrid[i][j] = new School(i, j);
                        break;
                    case 'D':
                        cellGrid[i][j] = new Hospital(i, j);
                        break;
                    case 'F':
                        cellGrid[i][j] = new PoliceStation(i, j);
                        break;
                    case 'H':
                        cellGrid[i][j] = new Housing(i, j);
                        break;
                    case 'C':
                        cellGrid[i][j] = new Commercial(i, j);
                        break;
                    case 'P':
                        cellGrid[i][j] = new PowerPlant(i, j);
                        break;
                    //sanırım bu kadar
                    default:
                        System.out.println("Error: Unknown letter!");
                        break;
                }
            }
        }
        System.out.println("Cells successfully loaded!");
    }

    public char[][] getGrid() {
        return charGrid;
    }

    public Cell[][] getCellGrid() {
        return cellGrid;
    }

    // alttaki method utilityprovider'in BFS algoritması için gerekli (komsularda isConnectable var mi diye bakacagiz)
    public List<Cell> getNeighbors(int x, int y) {
        List<Cell> neighbors = new ArrayList<>();
        int rowCount = charGrid.length;
        int colCount = charGrid[0].length;

        // (y - 1)
        if (y > 0) neighbors.add(cellGrid[y - 1][x]);
        // (y + 1)
        if (y < rowCount - 1) neighbors.add(cellGrid[y + 1][x]);
        // (x - 1)
        if (x > 0) neighbors.add(cellGrid[y][x - 1]);
        // (x + 1)
        if (x < colCount - 1) neighbors.add(cellGrid[y][x + 1]);

        return neighbors;


    }

    //TOPLAM TALEP
    public int getTotalUtilityDemand() {
        int totalDemand = 0;

        for (int i = 0; i < cellGrid.length; i++) {
            for (int j = 0; j < cellGrid[0].length; j++) {

                char letter = charGrid[i][j];

                if (letter == 'H' || letter == 'I' || letter == 'C') {

                    totalDemand += cellGrid[i][j].getDemand();
                    //Cell dosyasının içine bir getDemand methodu ekledim
                }
            }
        }
        return totalDemand;
    }

    public void moveUtility() {
        for (int i = 0; i < cellGrid.length; i++) {
            for (int j = 0; j < cellGrid[0].length; j++) {
                char letter = charGrid[i][j];
                if (letter == 'W' || letter == 'P' || letter == 'I') {

                    runBFS(cellGrid[i][j]);
                }
            }
        }
        System.out.println("Distribute is completed");
    }

    //Burayı kesin KONTROL edin

    public void runBFS(Cell providerCell){
        ArrayList<Cell> queue = new ArrayList<>();
        ArrayList<Cell> visited = new ArrayList<>();

        queue.add(providerCell);
        visited.add(providerCell);

        String utilityType =providerCell.getUtilityType();
        int availableUtility = providerCell.getOutput();

        while(queue.size()>0 && availableUtility > 0){
            Cell nextCell = queue.remove(0);
            List<Cell> neighbors = getNeighbors(nextCell.getX(), nextCell.getY());

            for (Cell neighbor : neighbors) {
                if (visited.contains(neighbor)) {
                }
                else {
                    visited.add(neighbor);
                }

                char neighborSymbol = charGrid[neighbor.getY()][neighbor.getX()];
                if (neighborSymbol == 'R' ){
                    queue.add(neighbor);
                }
                else if (neighborSymbol == 'H' || neighborSymbol == 'I' || neighborSymbol == 'C') {
                    availableUtility = neighbor.consumeUtility(utilityType, availableUtility);
                }
            }
        }
    }

}





