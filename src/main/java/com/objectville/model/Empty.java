package com.objectville.model;

public class Empty extends Cell {
    public Empty(int x, int y) { super(x, y, 'E'); }


    // UtilityProvider emptylerde durması için.
    @Override
    public boolean isConnectable() {
        return false;
    }
}