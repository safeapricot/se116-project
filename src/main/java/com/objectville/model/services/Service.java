package com.objectville.model.services;

import com.objectville.model.Cell;


public abstract class Service extends Cell {
    protected int radius;
    protected String serviceType;

    public Service(int x, int y, char symbol, int radius, String serviceType) {
        super(x, y, symbol);
        this.radius = radius;
        this.serviceType = serviceType;
    }

    public int getRadius() {
        return radius;
    }

    public String getServiceType() {
        return serviceType;
    }

    @Override
    public boolean isConnectable() {
        return true;
    }
}


