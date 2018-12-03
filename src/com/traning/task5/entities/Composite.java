package com.traning.task5.entities;

import java.util.List;

public interface Composite extends Node {
    void add(Node cluster);

    void remove(Node cluster);

    List<Node> getNodes();

    List<Node> getClusters();

    Node getNodeByNum(int num);

    int getSize();
}
