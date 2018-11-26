package com.traning.task5;

public class Town implements Node {
    private int x;
    private int y;
    private Composite parent;
    private int num;

    public Town(int x, int y, int num) {
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
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
}
