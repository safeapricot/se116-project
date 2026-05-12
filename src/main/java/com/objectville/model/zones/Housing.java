package com.objectville.model.zones;

public class Housing extends Zone {
    public Housing(int x, int y) { super(x, y, 'H'); }

    @Override
    public void updateLevelAndOutput() {
        int m = getMinUtility();
        boolean basics = m > 0;
        boolean services = currentServices.get("Security") && currentServices.get("Health") && currentServices.get("Education");

        if (!basics) { level = 0; }
        else if (level == 0) level = 1;
        else if (level == 1 && services) level = 2;
        else if (level == 2 && receivedLifestyle > 0) level = 3;

        if (level == 0) output = 0;
        else if (level == 1) output = m;
        else if (level == 2) output = 2 * m;
        else output = 2 * m + receivedLifestyle;
    }
}