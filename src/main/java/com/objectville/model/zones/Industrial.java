package com.objectville.model.zones;

public class Industrial extends Zone {
    public Industrial(int x, int y) { super(x, y, 'I'); }

    @Override
    public void updateLevelAndOutput() {
        int m = getMinUtility();
        boolean basics = m > 0 && receivedPopulation > 0;
        boolean security = currentServices.get("Security");

        if (!basics) {
            level = 0;
        } else {
            int target;
            if (security && receivedPopulation > 0) target = 3;
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