package com.objectville.interfaces;

public interface UtilityConsumer {
    int consumeUtility(String utilityType, int incomingAmount);

    void consumeElectricity (int amount);
    void consumeWater(int amount);
    void consumeInternet (int amount);

    boolean isFullySupplied();

}
