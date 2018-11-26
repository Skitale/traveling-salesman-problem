package com.traning.task5;

public class Main {
    public static void main(String[] args) {
        TaskWrapper taskWrapper = new TaskWrapper();
        taskWrapper.addTown(0,0, 0);
        taskWrapper.addTown(1,1, 1);
        taskWrapper.addTown(2,2,2);
        taskWrapper.addTown(3,3,3);
        taskWrapper.addTown(3,0,4);
        taskWrapper.addTown(0,3,5);
        taskWrapper.addTown(5,5,6);
        BaseAlg b = new BaseAlg(taskWrapper);
        b.solve();
        System.out.println();
    }
}
