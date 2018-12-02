package com.traning.task5;

public interface Node {
    int solve();
    Composite getParent();
    void setParent(Composite parent);
    double getX();
    double getY();
    int getNum();
    Node nextNode();
    void setNextNode(Node node);
    Node prevNode();
    void setPrevNode(Node node);
}
