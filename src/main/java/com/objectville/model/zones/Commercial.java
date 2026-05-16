package com.objectville.model.zones;

public class Commercial extends Zone {
    public Commercial(int x, int y) { super(x, y, 'C'); }

    @Override
    public void updateLevelAndOutput() {
        int m = getMinUtility();
        boolean basics = m > 0 && receivedPopulation > 0 && receivedGoods > 0;

        if (!basics && m == 0) level = 0;
        else if (level == 0 && basics) level = 1;
        else if (level == 1 && currentServices.get("Security")) level = 2;
        else if (level == 2) level = 3;

        if (level == 0) output = 0;
        else if (level == 1) output = m;
        else if (level == 2) output = 2 * m;
        else output = 2 * m + Math.min(receivedPopulation, receivedGoods);
    }
}