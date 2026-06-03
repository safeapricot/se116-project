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
            pool.consumePopulation(perZone * totalReceivers);
        }

        if (!commercials.isEmpty()) {
            int perC = pool.getGoods() / commercials.size();
            for (Commercial c : commercials) c.receivedGoods = perC;
            pool.consumeGoods(perC * commercials.size());
        }

        if (!houses.isEmpty()) {
            int perH = pool.getLifestyle() / houses.size();
            for (Housing h : houses) h.receivedLifestyle = perH;
            pool.consumeLifestyle(perH * houses.size());
        }
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

    // servicelari paylastirmak icin gereken method
    public void provideServices() {
        Cell[][] grid = map.getCellGrid();
        for (Cell[] cells : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                if (cells[j] instanceof Service) {
                    Service service = (Service) cells[j];
                    setRadius(service, grid); // teker teker service'lari arayip radiuslari ciziyoruz
                }
            }
        }
    }

    // yaricaplari kullanarak alanlari olusturma methodu. anlamasi benim icin daha kolay oldugu icin euclid kullanmaya karar verdim.
    private void setRadius(Service service, Cell[][] grid) {
        int x = service.getX();
        int y = service.getY();
        int r = service.getRadius();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] instanceof Zone) {
                    Zone currentZone = (Zone) grid[i][j]; // zone olarak downcasting yapiyoruz cunku getX ve getY kullanmamiz lazim.

                    double distance = Math.sqrt(Math.pow(currentZone.getX() - x, 2) + Math.pow(currentZone.getY() - y, 2)); // euclid

                    if (distance <= r) {
                        currentZone.currentServices.put(service.getServiceType(), true);

                        // simdi konsola yazdirilmasi gereken yer
                        if (currentZone.getClass().equals(Housing.class)) { // burada output.txt'ye olabildigi kadar benzetmeye calisiyorum. normalde bizim class'in adi House olsaydi cok daha basit olurdu ama bizimki Housing.
                            System.out.println("House at " + "(" + currentZone.getX() + "," + currentZone.getY() + ") recieved " + service.getServiceType() + " service");
                        } else if ((currentZone.getClass().equals(Commercial.class)) || (currentZone.getClass().equals(Industrial.class))) {
                            System.out.println(currentZone.getClass() + " at " + "(" + currentZone.getX() + "," + currentZone.getY() + ") received " + service.getServiceType() + " service");
                        }
                    }
                }

            }
        }
    }

    public void runSimulation(int tick, Cell[][] grid) {
        ResourcePool pool = new ResourcePool();
        for (int i = 1; i <= tick; i++) {
            System.out.println("Tick " + i);
            provideServices();
            map.moveUtility();
            distributeResources(pool, grid);
            updateZonesAndAccumulate(pool, grid);

            for (int a = 0; a < grid.length; a++) {
                for (int b = 0; b < grid[a].length; b++) {
                    if (grid[a][b] instanceof Zone) {
                        ((Zone) grid[a][b]).resetTurnData();
                    }
                }


            }
        }
    }


    public void updateZonesAndAccumulateWithLog(ResourcePool pool, Cell[][] grid) {
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell instanceof Zone z) {
                    Zone zone = (Zone) cell;
                    int oldLevel = z.level;

                    String typeName = zone.getClass().getSimpleName();

                    if (typeName.equals("Housing")) {
                        typeName = "House";
                    }
                    z.updateLevelAndOutput();

                    int newLevel = z.level;
                    int currentOutput = z.getOutput();

                    if (currentOutput > 0) {
                        String resourceType = "";
                        if (typeName.equals("House")) {
                            resourceType = "population";
                            pool.addPopulation(currentOutput);
                        } else if (typeName.equals("Industrial")) {
                            resourceType = "Goods";
                            pool.addGoods(currentOutput);
                        } else if (typeName.equals("Commercial")) {
                            resourceType = "Lifestyle";
                            pool.addLifestyle(currentOutput);
                        }
                        System.out.println(typeName + " at " + "(" + zone.getX() + ") recieved " + zone.getY() + ") generated " + currentOutput + " " + resourceType);

                        if (newLevel > oldLevel) {
                            System.out.println(typeName + " at (" + zone.getX() + "," + zone.getY() + ") levels up from " + oldLevel + " to " + newLevel);
                        } else if (newLevel < oldLevel) {
                            System.out.println(typeName + " at (" + zone.getX() + "," + zone.getY() + ") levels down from " + oldLevel + " to " + newLevel);
                        }
                    }
                }
            }
        }
    }
}