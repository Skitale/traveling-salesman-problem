package com.traning.task5.parsers;

import com.traning.task5.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    public static Model parse(File file){
        Model model = new Model();
        try {
            Reader r = new InputStreamReader(new FileInputStream(file), "UTF-8");
            Scanner s = new Scanner(r).useDelimiter("(\r\n)");
            int n = -1;
            double op = -1d;
            if(s.hasNextInt()){
                n = s.nextInt();
            }

            if(s.hasNextDouble()){
                op = s.nextDouble();
            }
            model.setOptimum(op);

            s.useDelimiter("(\r\n)|( )");
            for(int i = 0; i < n; i++){
                s.next();
                double x = -1;
                double y = -1;
                if(s.hasNextDouble()){
                    x = s.nextDouble();
                }

                if(s.hasNextDouble()){
                    y = s.nextDouble();
                }
                model.addTown(x, y, i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static List<Model> parseFolderWithFiles(String pathToFolder) {
        File folder = new File(pathToFolder);
        File[] files = folder.listFiles();
        List<Model> modelList = new ArrayList<>();
        assert files != null;
        for (File file : files) {
            modelList.add(parse(file));
        }
        return modelList;
    }
}
