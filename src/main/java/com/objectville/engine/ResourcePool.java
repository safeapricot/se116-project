package com.objectville.engine;

import com.objectville.model.Cell;
import com.objectville.model.zones.Commercial;
import com.objectville.model.zones.Housing;
import com.objectville.model.zones.Industrial;

// initially ResourcePooL'da L büyüktü onu düzelttim.
// ayrıca uzun bir sure sanki BFS yokmus gibi bu class'ı yazmıştım ama sonradan fark ettim...
public class ResourcePool {
    private int totalPopulation=0;
    private int totalGoods=0;
    private int totalLifeStyle=0;
    private int totalElectricityConsumed;
    private int totalWaterConsumed;
    private int totalInternetConsumed;
    private int totalElectricityProduced;
    private int totalWaterProduced;
    private int totalInternetProduced;

    public void collectProductions(Cell[][] grid){
        for(int i=0; i<grid.length; i++){
            for(int j=0; j< grid[0].length; j++){
                if(grid [i][j] instanceof Housing){
                    totalPopulation += ((Housing) grid[i][j]).getOutput();}
                else if(grid[i][j] instanceof Industrial){
                    totalGoods += ((Industrial)grid[i][j]).getOutput();}
                else if(grid[i][j] instanceof Commercial){
                    totalLifeStyle += ((Commercial) grid[i][j]).getOutput();
                }


            }
        }
    }

    public void distributeRecources(Cell[][] grid){
        int commercialCount= 0;
        int housingCount= 0;
        int industrialCount=0;

        for(int i=0; i< grid.length; i++){
            for(int j=0; j< grid[0].length; j++){
                if(grid[i][j] instanceof Commercial){
                    commercialCount++; }
                else if (grid[i][j] instanceof Housing){
                  housingCount++;}
                else if(grid[i][j] instanceof Industrial){
                    industrialCount++;}
            }
        }
    }
//biraz yapabildim ama tam emin değilim tekrar dönücem.


}
