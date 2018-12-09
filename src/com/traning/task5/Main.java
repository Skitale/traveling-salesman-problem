package com.traning.task5;

import com.traning.task5.entities.Model;
import com.traning.task5.parsers.Parser;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Model> models =  Parser.parseFolderWithFiles("./resources/");
        for(Model mo : models){
            BaseAlg ba = new BaseAlg(mo);
            ModAlg ma = new ModAlg(mo);
            System.out.println("---- " + mo.getName() + " ----");
            System.out.print("result for base alg = " + ba.solve());
            System.out.print(", mod alg = " + ma.solve());
            System.out.print(", optimum = " + mo.getOptimum());
            System.out.println();
        }
    }
}
