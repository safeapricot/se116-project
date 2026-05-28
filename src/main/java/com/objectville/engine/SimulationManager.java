package com.objectville.engine;

import com.objectville.model.Cell;
import com.objectville.model.providers.UtilityProvider;
import com.objectville.model.services.Service;
import com.objectville.model.zones.Zone;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.HashSet;
import java.util.Set;

public class SimulationManager {
    private CityMap map;




     // BFS methodu

    public void distributeBFS(Cell providerCell, String utilityType, int amount) {
        Queue<Cell> queue = new LinkedList<>();
        Set<Cell> visited = new HashSet<>(); // sonsuz donguye girilmemesi icin hashset

        queue.add(providerCell); // siraya soktuk
        visited.add(providerCell); // hashsete soktuk

        int remainingAmount = amount;

        while (!queue.isEmpty() && remainingAmount > 0) {
            Cell currentCell = queue.poll(); // siradan aliyoruz

            if (currentCell instanceof Zone) {
                Zone zone = (Zone) currentCell; // consumeUtility kullanmak icin atadik
                remainingAmount = zone.consumeUtility(utilityType, remainingAmount);
            }
            if (remainingAmount <= 0) {
                break;
            }
            List<Cell> neighbors = map.getNeighbors(currentCell.getX(), currentCell.getY());
            for (Cell neighbor : neighbors) {
                if (!visited.contains(neighbor) && neighbor.isConnectable()) { // eger hala kaynak varsa yoldan devam ediyoruz.
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }
    }

    // servicelari paylastirmak icin gereken method
    public void provideServices() {
        Cell[][] grid = map.getCellGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] instanceof Service) {
                    Service service = (Service) grid[i][j];
                    setRadius(service, grid); // teker teker service'lari arayip radiuslari ciziyoruz
                }
            }
        }
    }
    // yaricaplari kullanarak alanlari olusturma methodu. daha kolay oldugu icin euclid kullanmaya karar verdim.
    private void setRadius(Service service, Cell[][] grid) {

    }
}
