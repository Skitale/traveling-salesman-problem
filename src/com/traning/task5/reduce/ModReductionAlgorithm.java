package com.traning.task5.reduce;

import com.traning.task5.TownUtils;
import com.traning.task5.entities.*;

import java.util.ArrayList;
import java.util.List;

public class ModReductionAlgorithm extends BaseReductionAlgorithm {
    public ModReductionAlgorithm(Model m) {
        super(m);
    }

    private double getStatus(List<List<Node>> res, List<Node> centers) {
        if (res.size() != centers.size()) throw new UnsupportedOperationException();
        double E = 0;
        int i = 0;
        for (List<Node> nodeList : res) {
            for (Node n : nodeList) {
                E += Math.pow(TownUtils.getDist(n, centers.get(i)), 2);
            }
            i++;
        }
        return E;
    }

    private List<Node> getNewMarked(List<List<Node>> clustes) {
        List<Node> marked = new ArrayList<>();
        double x = 0;
        double y = 0;
        for (List<Node> nodeList : clustes) {
            x = 0;
            y = 0;
            for (Node n : nodeList) {
                x += n.getX();
                y += n.getY();
            }
            x /= nodeList.size();
            y /= nodeList.size();
            Town t = new Town(x, y, 0);
            marked.add(t);
        }
        return marked;
    }

    private int getRandom(int from, int to) {
        return from + (int) (Math.random() * to);
    }

    private boolean isExist(List<Node> list, Node n) {
        for (Node node : list) {
            if (node.equals(n)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Composite innerClustering(List<Node> allListNodes, int num, int level) {
        if (allListNodes.size() <= getK3() || getK1() > allListNodes.size()) {
            Cluster composite = new Cluster(num);
            int i = 0;
            for (Node t : allListNodes) {
                t.setNum(i++);
                composite.add(t);
            }
            return composite;
        }
        //if(getK1() > allListNodes.size()) throw new UnsupportedOperationException();
        List<Node> list = new ArrayList<>(allListNodes);
        List<Node> markedTown = new ArrayList<>();

        for (int i = 0; i < getK1(); i++) {
            int k = getRandom(0, list.size());
            Node addNode = list.get(k);
            while (isExist(markedTown, addNode)) {
                addNode = list.get(getRandom(0, list.size()));
            }
            markedTown.add(addNode);
        }
        list.removeAll(markedTown);

        List<List<Node>> clustes = TownUtils.getClusters(markedTown, list);
        double E = getStatus(clustes, markedTown);

        double curE = E - 1;
        while (curE != E) {
            E = curE;
            List<Node> marked = getNewMarked(clustes);
            List<List<Node>> clusts = TownUtils.getClustersWithBool(marked, allListNodes, false);
            curE = getStatus(clusts, marked);
            clustes = clusts;
        }


        Cluster clu = new Cluster(num);
        int i = 0;
        for (List<Node> list1 : clustes) {
            if (list1.isEmpty()) continue;
            Composite res1 = innerClustering(list1, i++, level + 1);
            if (res1 != null) {
                clu.add(res1);
            }
        }
        return clu;
    }
}
