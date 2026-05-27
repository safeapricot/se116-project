package com.objectville.engine;

import com.objectville.model.Cell;
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
            Cell cell = queue.poll(323);
        }

    }
}
