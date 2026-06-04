package com.objectville.model.zones;

public class Industrial extends Zone {
    public Industrial(int x, int y) { super(x, y, 'I'); }

    @Override
    public void updateLevelAndOutput() {
        int m = getMinUtility();
        boolean security = currentServices.get("Security");
        boolean excess = receivedPopulation > 1;

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
        else output = 2 * m + receivedPopulation;
    }

    @Override
    public int getMinUtility() {
        return Math.min(currentUtilities.get("Electricity"), currentUtilities.get("Water"));
    }

    @Override
    public boolean requiresUtility(String type) {
        return !type.equals("Internet");
    }
}