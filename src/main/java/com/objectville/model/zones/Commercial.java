package com.objectville.model.zones;

public class Commercial extends Zone {
    public Commercial(int x, int y) { super(x, y, 'C'); }

    @Override
    public void updateLevelAndOutput() {
        int m = getMinUtility();
        boolean basics = m > 0 && receivedPopulation > 0 && receivedGoods > 0;
        boolean services = currentServices.get("Security") && currentServices.get("Health") && currentServices.get("Education");

        // SEVİYE
        if (!basics) {
            level = 0;
        } else {
            int targetLevel;
            if (services && receivedGoods > 0 && receivedPopulation > 0) targetLevel = 3;
            else if (services) targetLevel = 2;
            else targetLevel = 1;

            // max 1 adım değiş
            if (targetLevel > level) level = level + 1;
            else if (targetLevel < level) level = level - 1;
        }

        // ÜRETİM
        if (level == 0) output = 0;
        else if (level == 1) output = m;
        else if (level == 2) output = 2 * m;
        else output = 2 * m + Math.min(receivedPopulation, receivedGoods);
    }
}