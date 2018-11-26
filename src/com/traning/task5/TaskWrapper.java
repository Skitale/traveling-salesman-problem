package com.traning.task5;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskWrapper {
    private List<Town> townList;
    private Map<Pair<Town, Town>, Integer> dist;

    public TaskWrapper() {
        townList = new ArrayList<>();
        dist = new HashMap<>();
    }

    public void addTown(int x, int y, int num){
        Town newTown = new Town(x, y, num);
        Town existTown = findTown(newTown);
        if(existTown == null){
            townList.add(newTown);
        }
    }

    public void addRelation(Town t1, Town t2, int distance){
        dist.put(new Pair<>(t1, t2), distance);
    }

    public Town findTown(Town town){
        for(Town t : townList){
            if(t.equals(town)){
                return t;
            }
        }
        return null;
    }

    public List<Town> getTownList() {
        return townList;
    }

    public int getDistance(Town t1, Town t2){
        return dist.getOrDefault(new Pair<>(t1, t2), -1);
    }
}
