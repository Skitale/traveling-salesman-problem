package com.traning.task5;

import com.traning.task5.entities.Composite;
import com.traning.task5.entities.Model;
import com.traning.task5.entities.Node;
import com.traning.task5.recovery.BaseRecoveryAlgorithm;
import com.traning.task5.reduce.BaseReductionAlgorithm;

public class BaseAlg {
    private Model tw;
    private int k1 = 8;
    private int k2 = 1;
    private int k3 = 7;
    private Composite root;
    private BaseReductionAlgorithm reducing;
    private BaseRecoveryAlgorithm recovering;

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

    private double getSolution(Composite composite){
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

}
