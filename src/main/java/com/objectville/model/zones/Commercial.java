package com.objectville.model.zones;

public class Commercial extends Zone {
    public Commercial(int x, int y) { super(x, y, 'C'); }

    @Override
    public void updateLevelAndOutput() {
        int m = getMinUtility();
        boolean security = currentServices.get("Security");
        boolean excess = receivedPopulation > 1 && receivedGoods > 1;

        if (m == 0) {
            level = 0;
        } else {
            int target;
            if (security && excess) target = 3;
            else if (security) target = 2;
            else target = 1;

            if (target > level) level++;
            else if (target < level) level--;
        }

        if (level == 0) output = 0;
        else if (level == 1) output = m;
        else if (level == 2) output = 2 * m;
        else output = 2 * m + Math.min(receivedPopulation, receivedGoods);
    }
}