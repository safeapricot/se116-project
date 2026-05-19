package com.objectville.engine;

// initially ResourcePooL'da L büyüktü onu düzelttim.
public class ResourcePool {
    private int currentElectricity;
    private int currentWater;
    private int currentInternet;

    // default olarak değerlerimizi 0 alıyoruz.
    public ResourcePool() {
        this.currentElectricity = 0;
        this.currentWater = 0;
        this.currentInternet = 0;
    }


    // simdi providerların eklemesi için gereken methodlar
    public void addElectricity(int amount) {
        this.currentElectricity += amount;
    }
    public void addWater(int amount) {
        this.currentWater += amount;
    }
    public void addInternet(int amount) {
        this.currentInternet += amount;
    }

    // simdi de binaların tuketmek icin kullanacagi methodlar

    /* !! ÖNEMLİ - Burada ilk başta direkt void döndürsem daha rahat olur diye dusunmustum ancak
    boolean dondurursak cok daha rahat olucak kullanma bakımından cunku true false olarak rahat
    bir sekilde kullanilabilir olur.
     */
    public boolean consumeElectricity(int amount) {
        if (this.currentElectricity >= amount) {
            this.currentElectricity -= amount;
            return true;
        }
        return false;
    }
    public boolean consumeWater(int amount) {
        if (this.currentWater >= amount) {
            this.currentWater -= amount;
            return true;
        }
        return false;
    }
    public boolean consumeInternet(int amount) {
        if (this.currentInternet >= amount) {
            this.currentInternet -= amount;
            return true;
        }
        return false;
    }

}
