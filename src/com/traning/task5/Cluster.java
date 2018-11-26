package com.traning.task5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cluster implements Node, Composite {
    private List<Node> nodes;
    private Composite parentCluster;
    private int num;

    public Cluster(int num) {
        this.nodes = new ArrayList<>();
        this.num = num;
    }

    @Override
    public void add(Node cluster) {
        nodes.add(cluster);
        cluster.setParent(this);
    }

    @Override
    public void remove(Node cluster) {
        nodes.remove(cluster);
        cluster.setParent(null);
    }

    @Override
    public int solve() {
        return 0;
    }

    @Override
    public Composite getParent() {
        return parentCluster;
    }

    @Override
    public void setParent(Composite parent) {
        parentCluster = parent;
    }

    @Override
    public int getX() {
        if(nodes.isEmpty()) throw new UnsupportedOperationException();
        int xS = 0;
        for(Node node : nodes){
            xS += node.getX();
        }
        return xS / nodes.size();
    }

    @Override
    public int getY() {
        if(nodes.isEmpty()) throw new UnsupportedOperationException();
        int yS = 0;
        for(Node node : nodes){
            yS += node.getY();
        }
        return yS / nodes.size();
    }

    @Override
    public List<Node> getNodes(){
        List<Node> result = new ArrayList<>();
        for(Node node : nodes){
            if(node instanceof Composite){
                Composite t = (Composite) node;
                result.addAll(t.getNodes());
            } else {
                result.add(node);
            }
        }
        return result;
    }

    @Override
    public int getNum() {
        return num;
    }

    @Override
    public int getSize() {
        return nodes.size();
    }
}
