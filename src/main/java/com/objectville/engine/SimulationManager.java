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
            System.out.println("Tick " + tick);

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
        int numHouses = 0, numIndustrial = 0, numCommercial = 0;
        for (Cell[] row : grid)
            for (Cell cell : row) {
                if (cell instanceof Housing) numHouses++;
                else if (cell instanceof Industrial) numIndustrial++;
                else if (cell instanceof Commercial) numCommercial++;
            }

        int totalConsumers = numIndustrial + numCommercial;
        int perPop = (totalConsumers > 0) ? pool.getPopulation() / totalConsumers : 0;
        int perGoods = (numCommercial > 0) ? pool.getGoods() / numCommercial : 0;
        int perLifestyle = (numHouses > 0) ? pool.getLifestyle() / numHouses : 0;

        for (Cell[] row : grid)
            for (Cell cell : row) {
                if (cell instanceof Housing h) {
                    h.receivedLifestyle = perLifestyle;
                    if (perLifestyle > 0)
                        System.out.println("House at (" + h.getY() + "," + h.getX() + ") received " + perLifestyle + " lifestyle");
                } else if (cell instanceof Commercial c) {
                    c.receivedPopulation = perPop;
                    if (perPop > 0)
                        System.out.println("Commercial at (" + c.getY() + "," + c.getX() + ") received " + perPop + " population");
                    c.receivedGoods = perGoods;
                    if (perGoods > 0)
                        System.out.println("Commercial at (" + c.getY() + "," + c.getX() + ") received " + perGoods + " goods");
                } else if (cell instanceof Industrial i) {
                    i.receivedPopulation = perPop;
                    if (perPop > 0)
                        System.out.println("Industrial at (" + i.getY() + "," + i.getX() + ") received " + perPop + " population");
                }
            }

        pool.setPopulation(0);
        pool.setGoods(0);
        pool.setLifestyle(0);
    }

    private void updateZonesAndAccumulate(ResourcePool pool, Cell[][] grid) {
        for (Cell[] row : grid)
            for (Cell cell : row) {
                if (cell instanceof Housing h) {
                    int oldLevel = h.getLevel();
                    h.updateLevelAndOutput();
                    int newLevel = h.getLevel();
                    if (h.getOutput() > 0 || newLevel < oldLevel)
                        System.out.println("House at (" + h.getY() + "," + h.getX() + ") generated " + h.getOutput() + " population");
                    if (newLevel > oldLevel)
                        System.out.println("House at (" + h.getY() + "," + h.getX() + ") levels up from " + oldLevel + " to " + newLevel);
                    else if (newLevel < oldLevel)
                        System.out.println("House at (" + h.getY() + "," + h.getX() + ") levels down from " + oldLevel + " to " + newLevel);
                    pool.addPopulation(h.getOutput());
                } else if (cell instanceof Industrial i) {
                    int oldLevel = i.getLevel();
                    i.updateLevelAndOutput();
                    int newLevel = i.getLevel();
                    if (i.getOutput() > 0 || newLevel < oldLevel)
                        System.out.println("Industrial at (" + i.getY() + "," + i.getX() + ") generated " + i.getOutput() + " goods");
                    if (newLevel > oldLevel)
                        System.out.println("Industrial at (" + i.getY() + "," + i.getX() + ") levels up from " + oldLevel + " to " + newLevel);
                    else if (newLevel < oldLevel)
                        System.out.println("Industrial at (" + i.getY() + "," + i.getX() + ") levels down from " + oldLevel + " to " + newLevel);
                    pool.addGoods(i.getOutput());
                } else if (cell instanceof Commercial c) {
                    int oldLevel = c.getLevel();
                    c.updateLevelAndOutput();
                    int newLevel = c.getLevel();
                    if (c.getOutput() > 0 || newLevel < oldLevel)
                        System.out.println("Commercial at (" + c.getY() + "," + c.getX() + ") generated " + c.getOutput() + " lifestyle");
                    if (newLevel > oldLevel)
                        System.out.println("Commercial at (" + c.getY() + "," + c.getX() + ") levels up from " + oldLevel + " to " + newLevel);
                    else if (newLevel < oldLevel)
                        System.out.println("Commercial at (" + c.getY() + "," + c.getX() + ") levels down from " + oldLevel + " to " + newLevel);
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
                                + service.getServiceType().toLowerCase(java.util.Locale.ENGLISH) + " service");
                    }
                }
            }
        }
    }
}