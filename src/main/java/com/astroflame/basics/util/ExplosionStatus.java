package com.astroflame.basics.util;

public enum ExplosionStatus {

    COOL,
    WARM,
    HOT,
    EXPLOSIVE;

    private static String currentStatus = COOL.toString();

    public static void setStatus(int currentEnergy) {
        if (currentEnergy >= 750) {
            currentStatus = COOL.toString();
        } else if (currentEnergy >= 500) {
            currentStatus = WARM.toString();
        } else if (currentEnergy >= 250) {
            currentStatus = HOT.toString();
        } else {
            currentStatus = EXPLOSIVE.toString();
        }
    }

    public static String getStatus() {
        return currentStatus;
    }

    public static Boolean isHazardous() {
        return currentStatus.equals(HOT.toString()) || currentStatus.equals(EXPLOSIVE.toString());
    }

}
