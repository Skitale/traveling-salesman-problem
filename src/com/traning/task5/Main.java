package com.traning.task5;

import com.traning.task5.entities.Model;
import com.traning.task5.parsers.Parser;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Model m = Parser.parse(new File("D:/Box/traveling-salesman-problem/resources/2.txt"));
        Model taskWrapper = new Model();
        taskWrapper.addTown(0,0, 0);
        taskWrapper.addTown(1,1, 1);
        taskWrapper.addTown(2,2,2);
        taskWrapper.addTown(3,3,3);
        taskWrapper.addTown(3,0,4);
        taskWrapper.addTown(0,3,5);
        taskWrapper.addTown(5,5,6);
        BaseAlg b = new BaseAlg(m);
        double d = b.solve();
        System.out.println(d);
    }
}
