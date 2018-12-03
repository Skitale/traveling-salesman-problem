package com.traning.task5.recovery;

import com.traning.task5.TownUtils;
import com.traning.task5.entities.Cluster;
import com.traning.task5.entities.Composite;
import com.traning.task5.entities.Node;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class BaseRecoveryAlgorithm {
    private Composite rootCluster;

    public void recovery(){
        if(rootCluster == null) return;
        innerSolving(rootCluster);
    }

    public void setCluster(Composite cluster) {
        this.rootCluster = cluster;
    }

    private void innerSolving(Composite cluster){
        setOptPathCycle(cluster);
        boolean flag = true;
        for(Node clust : cluster.getClusters()){
            if(clust instanceof Composite){
                innerSolving((Composite) clust);
                flag = false;
            }
        }
        //if all town do nothing
        if(flag) return;
        postUnion(cluster);
    }

    private void postUnion(Composite comp){
        if(comp.getClusters().size() < 2) throw new UnsupportedOperationException();
        List<Node> listClust = comp.getClusters();
        Composite n1 = (Composite) listClust.get(0);
        Composite n2 = (Composite) n1.nextNode();
        Pair<Node, Node> minDistPair = TownUtils.findMinNodesBClusters(n1, n2);
        Node firstInNode = minDistPair.getKey().nextNode();
        minDistPair.getKey().setNextNode(minDistPair.getValue());

        Node outNode = minDistPair.getValue();
        Composite cl = n2;
        for(int i = 1; i < listClust.size() - 1; i++){
            outNode = settingPathInCluster(cl, outNode);
            cl = (Composite) cl.nextNode();
        }
        settingLastPathInCluster(cl, outNode, firstInNode);
    }

    private void setOptPathCycle(Composite cluster){
        int n = cluster.getSize();
        List<Integer> order = new ArrayList<>();
        for(int i = 0; i < n; i++){
            order.add(i);
        }

        List<Integer> minOrder = new ArrayList<>(order);
        double min = getDistCycle(minOrder, cluster);
        do {
            double cur = getDistCycle(order, cluster);
            if(cur < min){
                min = cur;
                for(int i = 0; i < n; i++){
                    minOrder.set(i, order.get(i));
                }
            }
        } while (nextOrder(order));

        setPathForInternalSol(minOrder, cluster);
    }

    private void setPathForInternalSol(List<Integer> orderClust, Composite clust){
        if(orderClust.size() != clust.getSize()) throw new UnsupportedOperationException();
        for(int i = 0; i < orderClust.size() - 1; i++){
            int k1 = orderClust.get(i);
            int k2 = orderClust.get(i + 1);
            Node n1 = clust.getNodeByNum(k1);
            Node n2 = clust.getNodeByNum(k2);
            if(n1 != null && n2 != null){
                n1.setNextNode(n2);
            }
        }
        Node n1 = clust.getNodeByNum(orderClust.get(0));
        Node n2 = clust.getNodeByNum(orderClust.get(orderClust.size() - 1));
        if(n1 != null && n2 != null) {
            n2.setNextNode(n1);
        }
    }

    private void settingLastPathInCluster(Composite comp, Node maxNode, Node firstNode){
        Node rightNode = maxNode.nextNode();
        Node leftNode = TownUtils.getPrevNode(comp, maxNode);
        double minDist = TownUtils.getDist(rightNode, firstNode);
        Node nextOut = rightNode;

        if(leftNode != null){
            double dist = TownUtils.getDist(leftNode, firstNode);
            if(dist < minDist){
                nextOut = leftNode;
            }
        }

        if(nextOut == rightNode){
            TownUtils.reverseOrder(comp, maxNode, nextOut);
            nextOut.setNextNode(firstNode);
        } else { // nextOut == leftNode
            nextOut.setNextNode(firstNode);
        }
    }

    private Node settingPathInCluster(Composite comp, Node maxNode){
        Composite nextClust = (Composite) comp.nextNode();
        Node rightNode = maxNode.nextNode();
        Node leftNode = TownUtils.getPrevNode(comp, maxNode);
        double minDist = Double.MAX_VALUE;
        Node nextOut = null;
        Node nextIn = null;
        //right node
        Pair<Node, Node> rightPair = TownUtils.findMinNodesBClusters(rightNode, nextClust);
        minDist = TownUtils.getDist(rightPair.getKey(), rightPair.getValue());
        nextOut = rightPair.getKey();
        nextIn = rightPair.getValue();
        //left node
        if(leftNode != null){
            Pair<Node, Node> leftPair = TownUtils.findMinNodesBClusters(leftNode, nextClust);
            double dist = TownUtils.getDist(leftPair.getKey(), leftPair.getValue());
            if(dist < minDist){
                minDist = dist;
                nextOut = leftPair.getKey();
                nextIn = leftPair.getValue();
            }
        }

        if(nextOut == rightNode){
            TownUtils.reverseOrder(comp, maxNode, nextOut);
            nextOut.setNextNode(nextIn);
        } else if(nextOut == leftNode){
            nextOut.setNextNode(nextIn);
        }

        return nextIn;
    }

    private double getDistCycle(List<Integer> numClust, Composite cluster){
        if(numClust.size() != cluster.getSize()) throw new UnsupportedOperationException();
        if(numClust.size() == 1) return 0d;
        double sumDist = 0;
        for(int i = 0; i < numClust.size() - 1; i++){
            int k1 = numClust.get(i);
            int k2 = numClust.get(i + 1);
            Node n1 = cluster.getNodeByNum(k1);
            Node n2 = cluster.getNodeByNum(k2);
            if(n1 != null && n2 != null){
                sumDist += TownUtils.getDist(n1, n2);
            } else {
                throw new UnsupportedOperationException();
            }
        }
        if(numClust.size() == 2) return sumDist;
        Node n1 = cluster.getNodeByNum(numClust.get(0));
        Node n2 = cluster.getNodeByNum(numClust.get(numClust.size() - 1));
        if(n1 != null && n2 != null){
            sumDist += TownUtils.getDist(n1, n2);
        } else {
            throw new UnsupportedOperationException();
        }
        return sumDist;
    }

    private boolean nextOrder(List<Integer> curOrder){
        int j = curOrder.size() - 2;
        while (j != -1 && curOrder.get(j) >= curOrder.get(j + 1)) j--;
        if(j == -1) return false;
        int k = curOrder.size() - 1;
        while (curOrder.get(j) >= curOrder.get(k)) k--;
        int tmp = curOrder.get(j);
        curOrder.set(j, curOrder.get(k));
        curOrder.set(k, tmp);
        int l = j + 1;
        int r = curOrder.size() - 1;
        while (l < r){
            tmp = curOrder.get(l);
            curOrder.set(l++, curOrder.get(r));
            curOrder.set(r--, tmp);
        }
        return true;
    }
}
