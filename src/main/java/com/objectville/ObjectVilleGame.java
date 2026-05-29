package com.objectville;

import com.objectville.engine.CityMap;
import com.objectville.engine.SimulationManager;

public class ObjectVilleGame {
    public static void main(String[] args){
        if(args.length!=2){ //nolur nolmaz yanlış girilirse diye.
           System.out.println(" please run it correctly");
           return;
       }
       String mapFile = args[0];
       int tourNumber = Integer.parseInt(args[1]);

        System.out.println( mapFile);
//harita oluştur ve cell leri kontrol et
        CityMap map = new CityMap();
        map.loadMap(mapFile);
        map.generateCells();

        SimulationManager sManager = new SimulationManager(map);//simülasyonu başlat
    }
}
