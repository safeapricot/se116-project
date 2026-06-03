package com.objectville.model.zones;
import com.objectville.interfaces.UtilityConsumer;
import com.objectville.model.Cell;
import java.util.HashMap;
import java.util.Map;

public abstract class Zone extends Cell implements UtilityConsumer {
    protected int level = 0;
    protected int output = 0;
    protected int demand = 1;
    public Map<String, Integer> currentUtilities = new HashMap<>();
    public Map<String, Boolean> currentServices = new HashMap<>();
    public int receivedPopulation = 0;
    public int receivedGoods = 0;
    public int receivedLifestyle = 0;

    public Zone(int x, int y, char symbol) {
        super(x, y, symbol);
        resetTurnData();
    }

    public void resetTurnData() {
        currentUtilities.put("Electricity", 0);
        currentUtilities.put("Water", 0);
        currentUtilities.put("Internet", 0);
        currentServices.put("Security", false);
        currentServices.put("Health", false);
        currentServices.put("Education", false);
        receivedPopulation = 0;
        receivedGoods = 0;
        receivedLifestyle = 0;
    }

    public int getMinUtility() {
        return Math.min(currentUtilities.get("Electricity"),
                Math.min(currentUtilities.get("Water"), currentUtilities.get("Internet")));
    }

    public abstract void updateLevelAndOutput();

    public int getOutput() { return output; }
    public int getDemand() { return Math.max(1, output); }

    // BFS algorithm için utility consuming methodu
    
    public int consumeUtility(String utilityType, int incomingAmount) {
        if (!requiresUtility(utilityType)) {
            return incomingAmount;
        }
        int currentAmount = currentUtilities.getOrDefault(utilityType, 0);
        // getOrDefault = bir ihtimal null dondurup hata vermemesi icin kullandik.
        // eger null dondureceksen dondurme 0 ver diyoruz.
        int missingAmount = Math.max(0, getDemand() - currentAmount);//incomingi değil de currenti çıkarmamız gerekiyor diye düşündüm o yüzden düzelttim.tekrar bakarız

        if (missingAmount == 0) {
            return incomingAmount;
        }

        int amountToConsume = Math.min(incomingAmount, missingAmount);


        // log icin durum raporu

        String label = this.getClass().getSimpleName();
        if (label.equals("Housing")) label = "House";
        System.out.println(label + " at (" + this.getY() + "," + this.getX() + ") received "
                + amountToConsume + " " + utilityType.toLowerCase());

        currentUtilities.put(utilityType, currentAmount + amountToConsume);
        return incomingAmount - amountToConsume;
    }


    @Override
    public boolean isConnectable() {
        return true;
    }

    @Override
    public void consumeElectricity(int amount) {
        consumeUtility("Electricity", amount);
    }

    @Override
    public void consumeWater(int amount){
        consumeUtility("Water", amount);
    }
    @Override
    public void consumeInternet(int amount) {
        consumeUtility("Internet", amount);
    }

    @Override
    public boolean isFullySupplied() {
        return getMinUtility() >= getDemand();
    }
    @Override
    public boolean requiresUtility(String type) { return true; }
}