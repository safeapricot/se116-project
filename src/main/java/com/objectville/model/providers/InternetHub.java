package com.objectville.model.providers;

public class InternetHub extends UtilityProvider {
    public InternetHub(int x, int y) {
        super(x, y, 'T');
        this.utilityType = "Internet";
    }
}
