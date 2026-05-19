package com.objectville.model.services;

import com.objectville.model.Cell;

//abstract
public abstract class Service extends Cell {

    public Service(int x, int y, char symbol) {
        super(x, y, symbol);
    }


    @Override
    public boolean isConnectable() {
        return true;
    }
}
