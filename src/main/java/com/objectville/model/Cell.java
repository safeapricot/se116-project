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

    public int getDemand(){
        return 0;
    }

    //Eğer cell, santral değilse boş döner
    public String getUtilityType() {
        return "";
    }

    //Eğer cell, santral değilse üretim kapasitesi 0
    public int getOutput() {
        return 0;
    }

    //Eğer cell, bina değilse gelen elektriği/suyu harcamadan devam eder

    public int consumeUtility(String utilityType, int incomingAmount) {
        return incomingAmount;
    }

}

