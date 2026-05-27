package com.objectville.model.zones;

public class Industrial extends Zone {

    public Industrial(int x,int y) {
        super(x, y, 'I');
        }
    @Override
    public void updateLevelAndOutput() {
        int m = getMinUtility();
        boolean basics = m > 0;
        boolean services = currentServices.get("Security") && currentServices.get("Health") && currentServices.get("Education");

        if (!basics) {
            level = 0;
        } else if (level == 0) level = 1;
        else if (level == 1 && services) level = 2;
        else if (level == 2 && services) level = 3;

        if (level == 0) output = 0;
        else if (level == 1) output = m;
        else if (level == 2) output = 2 * m;
        else output = 2 * m + Math.min(receivedPopulation, receivedGoods);
    }
}


