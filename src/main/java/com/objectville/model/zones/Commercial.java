package com.objectville.model.zones;

public class Commercial extends Zone {
    public Commercial(int x, int y) { super(x, y, 'C'); }

    @Override
    public void updateLevelAndOutput() {
        int m = getMinUtility();
       boolean level1Requirement = (m>0) && (receivedPopulation > 0) && (receivedGoods > 0);
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

    if (level == 0) output = 0;
        else if (level == 1) output = m;
        else if (level == 2) output = 2 * m;
        else output = 2 * m + Math.min(receivedPopulation, receivedGoods);
    }
}