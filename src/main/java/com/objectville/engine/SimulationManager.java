package com.objectville.engine;

import com.objectville.model.Cell;
import com.objectville.model.providers.UtilityProvider;
import com.objectville.model.services.Service;
import com.objectville.model.zones.Commercial;
import com.objectville.model.zones.Housing;
import com.objectville.model.zones.Industrial;
import com.objectville.model.zones.Zone;

import java.util.*;

public class SimulationManager {
    private CityMap map;

    public SimulationManager(CityMap map) {
        this.map = map;
    }

    public void run(int ticks) {
        Cell[][] grid = map.getCellGrid();
        ResourcePool pool = new ResourcePool();

        for (int tick = 1; tick <= ticks; tick++) {
            System.out.println("Tick " + tick );

            resetZones(grid);
            provideServices();
            distributeUtilities();
            distributeResources(pool, grid);
            updateZonesAndAccumulate(pool, grid);
        }
    }

    private void resetZones(Cell[][] grid) {
        for (Cell[] row : grid)
            for (Cell cell : row)
                if (cell instanceof Zone z) z.resetTurnData();
    }

    // ===== BFS =====
    public void distributeBFS(Cell providerCell, String utilityType, int amount) {
        Queue<Cell> queue = new LinkedList<>();
        Set<Cell> visited = new HashSet<>();

        queue.add(providerCell);
        visited.add(providerCell);

        int remainingAmount = amount;

        while (!queue.isEmpty() && remainingAmount > 0) {
            Cell currentCell = queue.poll();

            if (currentCell instanceof Zone) {
                Zone zone = (Zone) currentCell;
                remainingAmount = zone.consumeUtility(utilityType, remainingAmount);
            }
            if (remainingAmount <= 0) break;

            List<Cell> neighbors = map.getNeighbors(currentCell.getX(), currentCell.getY());
            for (Cell neighbor : neighbors) {
                if (!visited.contains(neighbor) && neighbor.isConnectable()) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }
    }

    public void distributeUtilities() {
        Cell[][] grid = map.getCellGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] instanceof UtilityProvider) {
                    UtilityProvider up = (UtilityProvider) grid[i][j];
                    distributeBFS(up, up.getUtilityType(), up.getCapacity());
                }
            }
        }
    }

    private void distributeResources(ResourcePool pool, Cell[][] grid) {
        List<Housing> houses = new ArrayList<>();
        List<Industrial> industrials = new ArrayList<>();
        List<Commercial> commercials = new ArrayList<>();

        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell instanceof Housing) houses.add((Housing) cell);
                else if (cell instanceof Industrial) industrials.add((Industrial) cell);
                else if (cell instanceof Commercial) commercials.add((Commercial) cell);
            }
        }

        int totalReceivers = industrials.size() + commercials.size();
        if (totalReceivers > 0) {
            int perZone = pool.getPopulation() / totalReceivers;
            for (Industrial i : industrials) i.receivedPopulation = perZone;
            for (Commercial c : commercials) c.receivedPopulation = perZone;
        }
        pool.setPopulation(0);

        if (!commercials.isEmpty()) {
            int perC = pool.getGoods() / commercials.size();
            for (Commercial c : commercials) c.receivedGoods = perC;
        }
        pool.setGoods(0);

        if (!houses.isEmpty()) {
            int perH = pool.getLifestyle() / houses.size();
            for (Housing h : houses) h.receivedLifestyle = perH;
        }
        pool.setLifestyle(0);
    }

    private void updateZonesAndAccumulate(ResourcePool pool, Cell[][] grid) {
        for (Cell[] row : grid)
            for (Cell cell : row) {
                if (cell instanceof Housing h) {
                    h.updateLevelAndOutput();
                    pool.addPopulation(h.getOutput());
                } else if (cell instanceof Industrial i) {
                    i.updateLevelAndOutput();
                    pool.addGoods(i.getOutput());
                } else if (cell instanceof Commercial c) {
                    c.updateLevelAndOutput();
                    pool.addLifestyle(c.getOutput());
                }
            }
    }

    public void provideServices() {
        Cell[][] grid = map.getCellGrid();
        for (Cell[] cells : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                if (cells[j] instanceof Service) {
                    setRadius((Service) cells[j], grid);
                }
            }
        }
    }

    private void setRadius(Service service, Cell[][] grid) {
        int x = service.getX();
        int y = service.getY();
        int r = service.getRadius();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] instanceof Zone) {
                    Zone z = (Zone) grid[i][j];
                    double distance = Math.sqrt(Math.pow(z.getX() - x, 2) + Math.pow(z.getY() - y, 2));
                    if (distance <= r) {
                        z.currentServices.put(service.getServiceType(), true);


                        String label = (z instanceof Housing) ? "House" : z.getClass().getSimpleName();
                        System.out.println(label + " at (" + z.getY() + "," + z.getX() + ") received "
                                + service.getServiceType().toLowerCase() + " service");
                    }
                }
            }
        }
    }
}