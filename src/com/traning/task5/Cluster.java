package com.traning.task5;

import java.util.*;

public class Cluster implements Composite {
    private List<Node> nodes;
    private Composite parentCluster;
    private int num;
    private Node nextNode;
    private Node prevNode;
    private List<Node> rawTowns;

    public Cluster(int num) {
        this.nodes = new ArrayList<>();
        this.rawTowns = new ArrayList<>();
        this.num = num;
    }

    @Override
    public void add(Node cluster) {
        nodes.add(cluster);
        cluster.setParent(this);
        if(cluster instanceof Composite){
            rawTowns.addAll(((Composite) cluster).getNodes());
        } else {
            rawTowns.add(cluster);
        }
    }

    @Override
    public void remove(Node cluster) {
        nodes.remove(cluster);
        cluster.setParent(null);
        if(cluster instanceof Composite){
            ((Composite) cluster).getNodes().remove(cluster);
        } else {
            rawTowns.remove(cluster);
        }
    }

    public Node getNodeByNum(int i){
        if(getSize() <= i || i < 0) throw new UnsupportedOperationException();
        for(Node n : nodes){
            if(n.getNum() == i){
                return n;
            }
        }
        return null;
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
    public double getX() {
        if(rawTowns.isEmpty()) throw new UnsupportedOperationException();
        double xS = 0;
        for(Node node : rawTowns){
            xS += node.getX();
        }
        return xS / rawTowns.size();
    }

    @Override
    public double getY() {
        if(rawTowns.isEmpty()) throw new UnsupportedOperationException();
        double yS = 0;
        for(Node node : rawTowns){
            yS += node.getY();
        }
        return yS / rawTowns.size();
    }

    @Override
    public List<Node> getNodes(){
        return new ArrayList<>(rawTowns);
    }

    @Override
    public List<Node> getClusters(){
        return new ArrayList<>(nodes);
    }

    @Override
    public int getNum() {
        return num;
    }

    @Override
    public Node nextNode() {
        return nextNode;
    }

    @Override
    public void setNextNode(Node node) {
        nextNode = node;
        if(node == null) return;
        node.setPrevNode(this);
    }

    @Override
    public Node prevNode() {
        return prevNode;
    }

    @Override
    public void setPrevNode(Node node) {
        prevNode = node;
    }

    @Override
    public int getSize() {
        return nodes.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cluster)) return false;

        Cluster cluster = (Cluster) o;

        if (getX() != cluster.getX()) return false;
        return getY() == cluster.getY();
    }

    @Override
    public int hashCode() {
        String s = String.valueOf(getX()) + String.valueOf(getY());
        return s.hashCode();
    }
}
