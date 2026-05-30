package com.objectville.model.zones;

public class Industrial extends Zone {

    public Industrial(int x,int y) {
        super(x, y, 'I');
        }
    @Override
    public void updateLevelAndOutput() {
        int m = getMinUtility();
        boolean basics = m > 0 && receivedPopulation > 0;
        boolean services = currentServices.get("Security") && currentServices.get("Health") && currentServices.get("Education");

        if (!basics) {
            level = 0;
        } else {
            int targetLevel;
            if (services && receivedPopulation > 0) targetLevel = 3;
            else if (services) targetLevel = 2;
            else targetLevel = 1;

            if (targetLevel > level) level = level + 1;
            else if (targetLevel < level) level = level - 1;
        }

        if (level == 0) output = 0;
        else if (level == 1) output = m;
        else if (level == 2) output = 2 * m;
        else output = 2 * m + receivedPopulation;
    }

    @Override
    public int getMinUtility() {
        return Math.min(currentUtilities.get("Electricity"), currentUtilities.get("Water"));
    }
}


