package com.traning.task5;

public class TownUtils {
    public static double getDist(Town t1, Town t2){
        double dX = t1.getX() - t2.getX();
        double dY = t1.getY() - t2.getY();
        dX = Math.pow(dX, 2);
        dY = Math.pow(dY, 2);
        return Math.sqrt(dX + dY);
    }
}
