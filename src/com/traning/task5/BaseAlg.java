package com.traning.task5;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseAlg {
    private TaskWrapper tw;
    private int k1 = 2;
    private int k2 = 2;
    private int k3 = 2;
    private Cluster cluster;

    public BaseAlg(TaskWrapper tw) {
        this.tw = tw;
        cluster = new Cluster(0);
    }
    
    public int solve(){
        clustering();
        return -1;
    }
    
    private void clustering(){
        List<Town> list = new ArrayList<>(tw.getTownList());
        if(list.size() <= 2) throw new UnsupportedOperationException();
        cluster = innerClustering(list, 0,0);


    }

    private Cluster innerClustering(List<Town> l, int num, int level){
        if(level >= k2) return null;
        List<Town> list = new ArrayList<>(l);
        if(list.size() <= k3) {
            Cluster composite = new Cluster(num);
            int i = 0;
            for(Town t : list){
                t.setNum(i++);
                composite.add(t);
            }
            return composite;
        }
        List<Town> markedTown = new ArrayList<>();
        Pair<Town, Town> maxPair = getMaxPair(list);
        markedTown.add(maxPair.getValue());
        markedTown.add(maxPair.getKey());
        list.removeAll(markedTown);

        int counter = 0;
        while (counter < k1) {
            //fixTown, toTown, dist
            Map<Pair<Town, Town>, Double> map = getMinDistToMarked(markedTown, list);
            Town maxTown = getNextMarkTown(map);
            if (maxTown != null) {
                markedTown.add(maxTown);
                list.remove(maxTown);
            }
            counter++;
        }

        List<List<Town>> res = getClusters(markedTown, list);

        Cluster clu = new Cluster(num);
        int i = 0;
        for(List<Town> list1 : res){
            Composite res1 = innerClustering(list1, i++,level + 1);
            clu.add((Node)res1);
        }
        return clu;
    }

    private List<List<Town>> getClusters(List<Town> markedList, List<Town> unmarkedList){
        List<List<Town>> result = new ArrayList<>();
        for(Town tm : markedList){
            result.add(new ArrayList<>());
            result.get(result.size() - 1).add(tm);
        }

        for(Town t : unmarkedList){
            double minD = Double.MAX_VALUE;
            int numMarked = -1;
            for(Town tm : markedList){
                double curD = TownUtils.getDist(t, tm);
                if(curD < minD){
                    minD = curD;
                    numMarked = markedList.indexOf(tm);
                }
            }
            result.get(numMarked).add(t);
        }
        return result;
    }

    private Pair<Town, Town> getMaxPair(List<Town> list){
        if(list.size() <= 2) throw new UnsupportedOperationException();
        Pair<Town, Town> maxPair = new Pair<>(list.get(0), list.get(1));
        double dist = Double.MIN_VALUE;
        for(Town t1 : list){
            for(Town t2 : list){
                double d = TownUtils.getDist(t1, t2);
                if(!t1.equals(t2) && d > dist){
                    dist = d;
                    maxPair = new Pair<>(t1, t2);
                }
            }
        }
        return maxPair;
    }

    private Map<Pair<Town, Town>, Double> getMinDistToMarked(List<Town> markedList, List<Town> unmarkedList){
        //fixTown, toTown, dist
        Map<Pair<Town, Town>, Double> map = new HashMap<>();
        for(Town t : unmarkedList){
            double minD = Double.MAX_VALUE;
            Town mT = null;
            for(Town tm : markedList){
                double curD = TownUtils.getDist(t, tm);
                if(curD < minD){
                    minD = curD;
                    mT = tm;
                }
            }
            map.put(new Pair<>(mT, t), minD);
        }
        return map;
    }

    private Town getNextMarkTown(Map<Pair<Town, Town>, Double> map){
        double maxD = Double.MIN_VALUE;
        Town maxT = null;
        for(Map.Entry<Pair<Town, Town>, Double> entry : map.entrySet()){
            if(entry.getValue() > maxD){
                maxD = entry.getValue();
                maxT = entry.getKey().getValue();
            }
        }
        return maxT;
    }
}
