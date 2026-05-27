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

        queue.add(providerCell);
        visited.add(providerCell);

        int remainingAmount = amount;

        while (!queue.isEmpty() && remainingAmount > 0) {
            Cell currentCell = queue.poll();

            if (currentCell instanceof Zone) {
                Zone zone = (Zone) currentCell;
                remainingAmount = zone.consumeUtility(utilityType, remainingAmount);
            }
            if (remainingAmount <= 0) {
                break;
            }
            List<Cell> neighbors = map.getNeighbors(currentCell.getX(), currentCell.getY());
            for (Cell neighbor : neighbors) {
                if (!visited.contains(neighbor) && neighbor.isConnectable()) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }

    }
}
