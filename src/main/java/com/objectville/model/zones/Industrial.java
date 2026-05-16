package com.objectville.model.zones;

public class Industrial extends Zone {
    public Industrial(int x, int y) { super(x, y, 'I'); }

    @Override
    public void updateLevelAndOutput() {
        int m = Math.min(currentUtilities.get("Electricity"), currentUtilities.get("Water"));
        boolean basics = m > 0 && receivedPopulation > 0;

        if (m == 0 || currentUtilities.get("Electricity") == 0 || currentUtilities.get("Water") == 0) level = 0;
        else if (level == 0 && basics) level = 1;
        else if (level == 1 && currentServices.get("Security")) level = 2;
        else if (level == 2) level = 3; // "Excess population" logic handles via output

        if (level == 0) output = 0;
        else if (level == 1) output = m;
        else if (level == 2) output = 2 * m;
        else output = 2 * m + receivedPopulation;
    }
}