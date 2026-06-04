package com.objectville.model.zones;

import com.objectville.interfaces.UtilityConsumer;

import java.io.Serializable;

public class Housing extends Zone implements UtilityConsumer {
    public Housing(int x, int y) { super(x, y, 'H'); }

    @Override
    public void updateLevelAndOutput() {
        int m = getMinUtility();
        boolean level1Requirement =(m>0);
        boolean hasSecurity= currentServices.containsKey("Security") && currentServices.get("Security");
        boolean hasHealth= currentServices.containsKey("Health") && currentServices.get("Health");
        boolean hasEducation = currentServices.containsKey("Education")  && currentServices.get("Education");

        boolean level2Requirement = level1Requirement && hasSecurity && hasHealth && hasEducation;
        boolean level3Requirement = level2Requirement && (receivedLifestyle > 0);

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
        else output = 2 * m + receivedLifestyle;
    }

    @Override
    public void consumeElectricity(int amount) {
    //ilerde elektriğe özel hesaplama olur diye eklendi.Şuanda sadece consumeUtility kullanılıyor.
    }

    @Override
    public void consumeWater(int amount) {
//suya özel hesaplama için
    }

    @Override
    public void consumeInternet(int amount) {

    }

    @Override
    public boolean isFullySupplied() {
        return false;
    }

    @Override
    public String toString(){
        return this.getClass().getSimpleName() + " Zone (level : " + level + " Output : " + output + ") ";
    }
}