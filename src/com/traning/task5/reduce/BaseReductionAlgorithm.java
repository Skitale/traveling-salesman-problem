package com.traning.task5.reduce;

import com.traning.task5.entities.*;
import com.traning.task5.TownUtils;
import javafx.util.Pair;


import java.util.*;

public class BaseReductionAlgorithm {
    private Model m;
    private int k1;
    private int k2;
    private int k3;

    public BaseReductionAlgorithm(Model m) {
        this.m = m;
    }

    public Composite reduce(){
        List<Node> list = new ArrayList<>(m.getTownList());
        if(list.size() <= 2) throw new UnsupportedOperationException();
        return innerClustering(list, 0, 0);
    }

    protected Composite innerClustering(List<Node> list, int num, int level){
        if(list.size() <= k3 /*|| level >= k2*/) {
            Cluster composite = new Cluster(num);
            int i = 0;
            for(Node t : list){
                t.setNum(i++);
                composite.add(t);
            }
            return composite;
        }
        List<Node> markedTown = new ArrayList<>();
        Pair<Node, Node> maxPair = TownUtils.getMaxPair(list);
        markedTown.add(maxPair.getValue());
        markedTown.add(maxPair.getKey());
        list.removeAll(markedTown);

        int counter = 2;
        while (counter < k1) {
            //fixTown, toTown, dist
            Map<Pair<Node, Node>, Double> map = TownUtils.getMinDistToMarked(markedTown, list);
            Node maxTown = TownUtils.getNextMarkTown(map);
            if (maxTown != null) {
                markedTown.add(maxTown);
                list.remove(maxTown);
            }
            counter++;
        }

        List<List<Node>> res = TownUtils.getClusters(markedTown, list);

        Cluster clu = new Cluster(num);
        int i = 0;
        for(List<Node> list1 : res){
            Composite res1 = innerClustering(list1, i++,level + 1);
            if(res1 != null) {
                clu.add(res1);
            }
        }
        return clu;
    }

    public int getK1() {
        return k1;
    }

    public void setK1(int k1) {
        this.k1 = k1;
    }

    public int getK2() {
        return k2;
    }

    public void setK2(int k2) {
        this.k2 = k2;
    }

    public int getK3() {
        return k3;
    }

    public void setK3(int k3) {
        this.k3 = k3;
    }
}
