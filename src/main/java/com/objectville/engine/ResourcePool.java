package com.objectville.engine;

public class ResourcePool {
    private int population;
    private int lifestyle;
    private int goods;

    //First constructor for to start from scratch, according to the normal rules of the game.
    public ResourcePool() {
        this.population = 0;
        this.lifestyle = 0;
        this.goods = 0;
    }

    //Second constructor for testing and quick value assignment
    //OVERLOADİNG
    public ResourcePool(int population, int lifestyle, int goods) {
        this.population = population;
        this.lifestyle = lifestyle;
        this.goods = goods;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getLifestyle() {
        return lifestyle;
    }

    public void setLifestyle(int lifestyle) {
        this.lifestyle = lifestyle;
    }

    public int getGoods() {
        return goods;
    }

    public void setGoods(int goods) {
        this.goods = goods;
    }

    public void addPopulation(int a) {
        this.population =population + a;
    }

    public void addLifestyle(int a) {
        this.lifestyle = lifestyle + a;
    }
    public void addGoods(int a) {
        this.goods = goods + a;
    }

    public void consumePopulation(int a) {
        if(population >= a) {
            population = population - a;
        }else{
            System.out.println("Not enough population");
        }
    }

    public void consumeLifestyle(int a) {
        if(lifestyle >= a) {
            lifestyle = lifestyle - a;
        }
        else {
            System.out.println("Not enough lifestyle");
        }
    }

    public void consumeGoods(int a) {
        if(goods >= a) {
            goods = goods - a;
        }
        else {
            System.out.println("Not enough goods");
        }
    }
    public void printPoolState(){
        System.out.println("Population: " + population);
        System.out.println("Lifestyle: " + lifestyle);
        System.out.println("Goods: " + goods);
    }
}
