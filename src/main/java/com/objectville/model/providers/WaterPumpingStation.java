package com.objectville.model.providers;
public class WaterPumpingStation extends UtilityProvider {
    public WaterPumpingStation(int x, int y) {
        super(x, y, 'W');
        this.utilityType = "Water";

    }
}
