package com.traning.task5;

public class Town implements Node {
    private double x;
    private double y;
    private Composite parent;
    private int num;
    private Node nextNode;
    private Node prevNode;

    public Town(double x, double y, int num) {
        this.x = x;
        this.y = y;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public int solve() {
        return -1;
    }

    @Override
    public Composite getParent() {
        return parent;
    }

    @Override
    public void setParent(Composite parent) {
        this.parent = parent;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Town)) return false;

        Town town = (Town) o;

        if (getX() != town.getX()) return false;
        return getY() == town.getY();
    }

    @Override
    public int hashCode() {
        String s = String.valueOf(x) + String.valueOf(y);
        return s.hashCode();
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
    public String toString() {
        return "Town{" +
                "x=" + x +
                ", y=" + y +
                ", num=" + num +
                '}';
    }
}
