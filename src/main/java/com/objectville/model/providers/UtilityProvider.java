package com.objectville.model.providers;

import com.objectville.model.Cell;

public abstract class UtilityProvider extends Cell {
    public UtilityProvider(int x, int y, char symbol) {
        super(x, y, symbol);
    }
    protected int capacity = 100;
    protected String utilityType;

    public int getCapacity() { return capacity; }
    public String getUtilityType() { return utilityType; }
    @Override
    public boolean isConnectable() {
        return true;
    }
}
