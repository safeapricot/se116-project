package com.objectville.engine;

import com.objectville.exception.InvalidMapException;
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

    private char[][] charGrid;
    private Cell[][] cellGrid;

    public void loadMap(String filePath) {
        ArrayList<String> lines = new ArrayList<String>();

        File file = new File(filePath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new InvalidMapException("Map file could not be read: " + filePath);
        }

        if (lines.isEmpty()) {
            throw new InvalidMapException("Map file is empty: " + filePath);
        }

        int rowCount = lines.size();
        int colCount = lines.get(0).length();

        for (int i = 0; i < rowCount; i++) {
            if (lines.get(i).length() != colCount) {
                throw new InvalidMapException("Map is not rectangular at row " + i);
            }
        }

        charGrid = new char[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            charGrid[i] = lines.get(i).toCharArray();
        }
    }

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
                        cellGrid[i][j] = new Empty(j, i);
                        break;
                    case 'R':
                        cellGrid[i][j] = new Road(j, i);
                        break;
                    case 'T':
                        cellGrid[i][j] = new InternetHub(j, i);
                        break;
                    case 'W':
                        cellGrid[i][j] = new WaterPumpingStation(j, i);
                        break;
                    case 'I':
                        cellGrid[i][j] = new Industrial(j, i);
                        break;
                    case 'S':
                        cellGrid[i][j] = new School(j, i);
                        break;
                    case 'D':
                        cellGrid[i][j] = new Hospital(j, i);
                        break;
                    case 'F':
                        cellGrid[i][j] = new PoliceStation(j, i);
                        break;
                    case 'H':
                        cellGrid[i][j] = new Housing(j, i);
                        break;
                    case 'C':
                        cellGrid[i][j] = new Commercial(j, i);
                        break;
                    case 'P':
                        cellGrid[i][j] = new PowerPlant(j, i);
                        break;
                    default:
                        throw new InvalidMapException("Unknown letter in map: " + letter);
                }
            }
        }
    }

    public char[][] getGrid() {
        return charGrid;
    }

    public Cell[][] getCellGrid() {
        return cellGrid;
    }

    public List<Cell> getNeighbors(int x, int y) {
        List<Cell> neighbors = new ArrayList<>();
        int rowCount = charGrid.length;
        int colCount = charGrid[0].length;

        if (y > 0 && x > 0) neighbors.add(cellGrid[y - 1][x - 1]);
        if (y > 0) neighbors.add(cellGrid[y - 1][x]);
        if (y > 0 && x < colCount - 1) neighbors.add(cellGrid[y - 1][x + 1]);
        if (x > 0) neighbors.add(cellGrid[y][x - 1]);
        if (x < colCount - 1) neighbors.add(cellGrid[y][x + 1]);
        if (y < rowCount - 1 && x > 0) neighbors.add(cellGrid[y + 1][x - 1]);
        if (y < rowCount - 1) neighbors.add(cellGrid[y + 1][x]);
        if (y < rowCount - 1 && x < colCount - 1) neighbors.add(cellGrid[y + 1][x + 1]);

        return neighbors;
    }

    public int getTotalUtilityDemand() {
        int totalDemand = 0;

        for (int i = 0; i < cellGrid.length; i++) {
            for (int j = 0; j < cellGrid[0].length; j++) {

                char letter = charGrid[i][j];

                if (letter == 'H' || letter == 'I' || letter == 'C') {
                    totalDemand += cellGrid[i][j].getDemand();
                }
            }
        }
        return totalDemand;
    }
}