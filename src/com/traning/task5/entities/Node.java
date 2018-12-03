package com.traning.task5.entities;

public interface Node {

    Composite getParent();

    void setParent(Composite parent);

    double getX();

    double getY();

    int getNum();

    void setNum(int num);

    Node nextNode();

    void setNextNode(Node node);
}
