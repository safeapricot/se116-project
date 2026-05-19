package com.objectville.model;

public class Road extends Cell {
    public Road(int x, int y) { super(x, y, 'R'); }

    @Override
    public boolean isConnectable() {
        return true;
    }
}
