package com.objectville.model.providers;

public class PowerPlant extends UtilityProvider {
    private int productionAmount;
    public PowerPlant(int x, int y) {
        super(x, y, 'P');
        this.productionAmount = 100;
        this.utilityType = "Electricity";
    }


}
