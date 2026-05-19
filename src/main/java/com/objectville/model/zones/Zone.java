package com.objectville.model.zones;
import com.objectville.model.Cell;
import java.util.HashMap;
import java.util.Map;

public abstract class Zone extends Cell {
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
    }

    public int getMinUtility() {
        return Math.min(currentUtilities.get("Electricity"),
                Math.min(currentUtilities.get("Water"), currentUtilities.get("Internet")));
    }

    public abstract void updateLevelAndOutput();

    public int getOutput() { return output; }
    public int getDemand() { return Math.max(1, output); }

    // BFS algorithm için utility consuming methodu
    // mantığı kafamda tam anlayamadım ileride belki değiştirilirse daha iyi olur

    public int consumeUtility(String utilityType, int incomingAmount) {
        int currentAmount = currentUtilities.getOrDefault(utilityType, 0);
        int missingAmount = getDemand() - incomingAmount;

        if (missingAmount == 0) {
            return incomingAmount;
        }

        int amountToConsume = Math.min(incomingAmount, missingAmount);
        currentUtilities.put(utilityType, currentAmount + amountToConsume);
        return incomingAmount - amountToConsume;
    }

    @Override
    public boolean isConnectable() {
        return true;
    }
}