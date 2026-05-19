package com.objectville.model;
public abstract class Cell {
    protected int x, y;
    protected char symbol;

    public Cell(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public char getSymbol() { return symbol; }

    // UtilityProvider'in roadları ve zoneları kullanarak yayması için yeni özellik (BFS algorithm)
    public abstract boolean isConnectable();

    @Override
    public String toString() { return String.valueOf(symbol); }
}

