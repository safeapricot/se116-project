package com.objectville.model.zones;

public class Industrial extends Zone {

    public Industrial(int x, int y) {
        super(x, y, 'I');
    }

    @Override
    public void updateLevelAndOutput() {
        int m = getMinUtility();

        boolean level1Requirement = (m > 0) && (receivedPopulation > 0);
        boolean level2Requirement = level1Requirement && currentServices.get("Security");
        boolean level3Requirement = level2Requirement && (receivedPopulation > 1);

        if (!level1Requirement) {
            level = 0;
        } else if (level3Requirement) {
            if (level < 3) {
                level++;
            }
        } else if (level2Requirement) {
            if (level < 2) {
                level++;
            } else if (level > 2) {
                level--;
            }
        } else if (level1Requirement) {
            if (level == 0) {
                level++;
            } else if (level > 1) {
                level--;
            }
        }
    }


    @Override
    public int getMinUtility() {
        return Math.min(currentUtilities.get("Electricity"), currentUtilities.get("Water"));
    }
}


