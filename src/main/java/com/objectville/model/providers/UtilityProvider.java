package com.objectville.model.providers;

import com.objectville.model.Cell;

public abstract class UtilityProvider extends Cell {
    public UtilityProvider(int x, int y, char symbol) {
        super(x, y, symbol);
    }

    @Override
    public boolean isConnectable() {
        return true;
    }
}
