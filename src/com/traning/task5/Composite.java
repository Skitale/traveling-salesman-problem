package com.traning.task5;

import java.util.List;

public interface Composite {
    void add(Node cluster);
    void remove(Node cluster);
    List<Node> getNodes();
    int getNum();
    int getSize();
}
