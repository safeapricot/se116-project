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

    public SimulationManager(CityMap map){
        this.map=map;
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

        for (Cell[] row : grid)
            for (Cell cell : row) {
                if (cell instanceof Housing) houses.add((Housing) cell);
                else if (cell instanceof Industrial) industrials.add((Industrial) cell);
                else if (cell instanceof Commercial) commercials.add((Commercial) cell);
            }

        // population -> industrial ve commercial'a esit paylas
        int totalReceivers = industrials.size() + commercials.size();
        if (totalReceivers > 0) {
            int perZone = pool.getPopulation() / totalReceivers;
            for (Industrial i : industrials) i.receivedPopulation = perZone;
            for (Commercial c : commercials) c.receivedPopulation = perZone;
        }

        // goods -> commercial'a
        if (!commercials.isEmpty()) {
            int perC = pool.getGoods() / commercials.size();
            for (Commercial c : commercials) c.receivedGoods = perC;
        }

        // lifestyle -> house'a
        if (!houses.isEmpty()) {
            int perH = pool.getLifestyle() / houses.size();
            for (Housing h : houses) h.receivedLifestyle = perH;
        }
    }

    private void updateZonesAndAccumulate(ResourcePool pool, Cell[][] grid) {
        for (Cell[] row : grid)
            for (Cell cell : row) {
                if (cell instanceof Housing) {
                    Housing h = (Housing) cell;
                    h.updateLevelAndOutput();
                    pool.addpopulation(h.getOutput());
                } else if (cell instanceof Industrial) {
                    Industrial i = (Industrial) cell;
                    i.updateLevelAndOutput();
                    pool.addgoods(i.getOutput());
                } else if (cell instanceof Commercial) {
                    Commercial c = (Commercial) cell;
                    c.updateLevelAndOutput();
                    pool.addlifestyle(c.getOutput());
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
    // yaricaplari kullanarak alanlari olusturma methodu. anlamasi benim icin daha kolay oldugu icin euclid kullanmaya karar verdim.
    private void setRadius(Service service, Cell[][] grid) {
        int x = service.getX();
        int y = service.getY();
        int r = service.getRadius();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] instanceof Zone) {
                    Zone currentZone = (Zone) grid[i][j]; // zone olarak downcasting yapiyoruz cunku getX ve getY kullanmamiz lazim.

                    double distance = Math.sqrt(Math.pow(currentZone.getX() - x, 2) + Math.sqrt(Math.pow(currentZone.getY() - y,2))); // euclid

                    if (distance <= r) {
                        currentZone.currentServices.put(service.getServiceType(),true);

                        // simdi konsola yazdirilmasi gereken yer
                        // TODO: burasi ileride bufferedwriter olarak degisecek !!!
                        if (currentZone.getClass().equals(Housing.class)) { // burada output.txt'ye olabildigi kadar benzetmeye calisiyorum. normalde bizim class'in adi House olsaydi cok daha basit olurdu ama bizimki Housing.
                            System.out.println("House at " + "(" + currentZone.getX() + "," + currentZone.getY() + ") recieved " + service.getServiceType() + " service");
                        } else if ( (currentZone.getClass().equals(Commercial.class)) ||  (currentZone.getClass().equals(Industrial.class)) )   {
                            System.out.println(currentZone.getClass() + " at " + "(" + currentZone.getX() + "," + currentZone.getY() + ") received " + service.getServiceType() + " service");
                        }


                    }
                }

            }

        }

    }
}
