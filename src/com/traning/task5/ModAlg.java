package com.traning.task5;

import com.traning.task5.entities.Model;
import com.traning.task5.reduce.ModReductionAlgorithm;

public class ModAlg extends BaseAlg {
    public ModAlg(Model tw) {
        super(tw);
        k1 = 8;
        k2 = 1;
        k3 = 10;
        reducing = new ModReductionAlgorithm(tw);
        reducing.setK1(k1); reducing.setK2(k2); reducing.setK3(k3);
    }
}
