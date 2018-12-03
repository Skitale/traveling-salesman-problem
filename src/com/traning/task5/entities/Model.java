package com.traning.task5.entities;

import com.traning.task5.entities.Town;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<Town> townList;
    private double optimum;

    public Model() {
        townList = new ArrayList<>();
    }

    public void addTown(double x, double y, int num) {
        Town newTown = new Town(x, y, num);
        Town existTown = findTown(newTown);
        if (existTown == null) {
            townList.add(newTown);
        }
    }

    public Town findTown(Town town) {
        for (Town t : townList) {
            if (t.equals(town)) {
                return t;
            }
        }
        return null;
    }

    public List<Town> getTownList() {
        return townList;
    }

    public double getOptimum() {
        return optimum;
    }

    public void setOptimum(double optimum) {
        this.optimum = optimum;
    }
}
