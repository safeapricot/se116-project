package com.objectville.model.zones;

import com.objectville.interfaces.UtilityConsumer;

import java.io.Serializable;

public class Housing extends Zone implements UtilityConsumer {
    public Housing(int x, int y) { super(x, y, 'H'); }

    @Override
    public void updateLevelAndOutput() {
        int m = getMinUtility();
        boolean basics = m > 0;
        boolean services = currentServices.get("Security") && currentServices.get("Health") && currentServices.get("Education");

        if (!basics) {
            level = 0;
        } else {
            int targetLevel;
            if (services && receivedLifestyle > 0) targetLevel = 3;
            else if (services) targetLevel = 2;
            else targetLevel = 1;

            if (targetLevel > level) level = level + 1;
            else if (targetLevel < level) level = level - 1;
        }

        if (level == 0) output = 0;
        else if (level == 1) output = m;
        else if (level == 2) output = 2 * m;
        else output = 2 * m + receivedLifestyle;
    }

    @Override
    public void consumeElectricity(int amount) {

    }

    @Override
    public void consumeWater(int amount) {

    }

    @Override
    public void consumeInternet(int amount) {

    }

    @Override
    public boolean isFullySupplied() {
        return false;
    }
}