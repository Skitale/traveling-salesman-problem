package com.traning.task5;

import com.traning.task5.entities.Composite;
import com.traning.task5.entities.Model;
import com.traning.task5.entities.Node;
import com.traning.task5.recovery.BaseRecoveryAlgorithm;
import com.traning.task5.reduce.BaseReductionAlgorithm;

public class BaseAlg {
    private Model tw;
    protected int k1 = 8;
    protected int k2 = 1;
    protected int k3 = 10;
    protected Composite root;
    protected BaseReductionAlgorithm reducing;
    protected BaseRecoveryAlgorithm recovering;

    public BaseAlg(Model tw) {
        this.tw = tw;
        reducing = new BaseReductionAlgorithm(tw);
        reducing.setK1(k1); reducing.setK2(k2); reducing.setK3(k3);
        recovering = new BaseRecoveryAlgorithm();
    }
    
    public double solve(){
        root = reducing.reduce();
        recovering.setCluster(root);
        recovering.recovery();
        return getSolution(root);
    }

    protected double getSolution(Composite composite){
        if(composite.getSize() < 1) return -1;
        Node node = composite.getNodes().get(0);
        Node cur = node.nextNode();
        double sol = TownUtils.getDist(node, cur);
        while (cur != node){
            Node tmp = cur.nextNode();
            sol += TownUtils.getDist(cur, tmp);
            cur = tmp;
        }
        return sol;
    }


    public int getK1() {
        return k1;
    }

    public int getK2() {
        return k2;
    }

    public int getK3() {
        return k3;
    }
}
