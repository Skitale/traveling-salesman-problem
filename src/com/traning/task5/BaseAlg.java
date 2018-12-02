package com.traning.task5;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseAlg {
    private Model tw;
    private int k1 = 8;
    private int k2 = 1;
    private int k3 = 7;
    private Cluster cluster;

    public BaseAlg(Model tw) {
        this.tw = tw;
        cluster = new Cluster(0);
    }
    
    public double solve(){
        clustering();
        innerSolving(cluster);
        return getSolution(cluster);
    }

    private double getSolution(Composite composite){
        if(composite.getSize() < 1) return -1;
        Node node = composite.getNodes().get(0);
        Node cur = node.nextNode();
        double sol = TownUtils.getDist(node, cur);
        while (cur != node){
            Node tmp = cur.nextNode();
            sol += TownUtils.getDist(cur, tmp);
            cur = tmp;
        }
        return sol;
    }
    
    private void clustering(){
        List<Town> list = new ArrayList<>(tw.getTownList());
        if(list.size() <= 2) throw new UnsupportedOperationException();
        cluster = innerClustering(list, 0,0);
    }

    private void innerSolving(Composite cluster){
        setOptPathCycle(cluster);
        Cluster cluster1 = (Cluster) cluster;
        boolean flag = true;
        for(Node clust : cluster.getClusters()){
            if(clust instanceof Composite){
                innerSolving((Composite) clust);
                flag = false;
            }
        }
        if(flag) return;
        //if all town do nothing
        postUnion(cluster1);
    }

    private void postUnion(Cluster comp){
        if(comp.getClusters().size() < 2) throw new UnsupportedOperationException();
        List<Node> listClust = comp.getClusters();
        Cluster n1 = (Cluster) listClust.get(0);
        Cluster n2 = (Cluster) n1.nextNode();
        Pair<Node, Node> minDistPair =  findMinNodesBClusters(n1, n2);
        Node firstInNode = minDistPair.getKey().nextNode();
        minDistPair.getKey().setNextNode(minDistPair.getValue());
        Node outNode = minDistPair.getValue();
        Cluster cl = n2;
        for(int i = 1; i < listClust.size() - 1; i++){
            outNode = settingPathInCluster(cl, outNode);
            cl = (Cluster) cl.nextNode();
        }
        settingLastPathInCluster(cl, outNode, firstInNode);
    }

    private void settingLastPathInCluster(Cluster comp, Node maxNode, Node firstNode){
        Node rightNode = maxNode.nextNode();
        Node leftNode = getPrevNode(comp, maxNode);
        double minDist = TownUtils.getDist(rightNode, firstNode);
        Node nextOut = rightNode;

        if(leftNode != null){
            double dist = TownUtils.getDist(leftNode, firstNode);
            if(dist < minDist){
                nextOut = leftNode;
            }
        }

        if(nextOut == rightNode){
            reverseOrder(comp, maxNode, nextOut);
            nextOut.setNextNode(firstNode);
        } else { // nextOut == leftNode
            nextOut.setNextNode(firstNode);
        }
    }

    private Node settingPathInCluster(Cluster comp, Node maxNode){
        Cluster nextClust = (Cluster) comp.nextNode();
        Node rightNode = maxNode.nextNode();
        Node leftNode = getPrevNode(comp, maxNode);
        double minDist = Double.MAX_VALUE;
        Node nextOut = null;
        Node nextIn = null;
        //right node
        Pair<Node, Node> rightPair = findMinNodesBClusters(rightNode, nextClust);
        minDist = TownUtils.getDist(rightPair.getKey(), rightPair.getValue());
        nextOut = rightPair.getKey();
        nextIn = rightPair.getValue();
        //left node
        if(leftNode != null){
            Pair<Node, Node> leftPair = findMinNodesBClusters(leftNode, nextClust);
            double dist = TownUtils.getDist(leftPair.getKey(), leftPair.getValue());
            if(dist < minDist){
                minDist = dist;
                nextOut = leftPair.getKey();
                nextIn = leftPair.getValue();
            }
        }

        if(nextOut == rightNode){
            reverseOrder(comp, maxNode, nextOut);
            nextOut.setNextNode(nextIn);
        } else if(nextOut == leftNode){
            nextOut.setNextNode(nextIn);
        }

        return nextIn;
    }

    private void reverseOrder(Cluster cluster, Node finish, Node start){
        if(cluster.getClusters().size() < 2) return;
        Node cur = start;
        Node next = start.nextNode();
        start.setNextNode(null);
        while (cur != finish){
            Node tmp = next.nextNode();
            next.setNextNode(cur);
            cur = next;
            next = tmp;
        }
    }

    private Node getPrevNode(Cluster comp, Node node){
        if(comp.getClusters().size() < 2) return null;
        for(Node n : comp.getNodes()){
            if(n.nextNode().equals(node)){
                return n;
            }
        }
        throw new UnsupportedOperationException();
    }

    private Pair<Node, Node> findMinNodesBClusters(Cluster n1, Cluster n2){
        double min = Double.MAX_VALUE;
        Pair<Node, Node> pair = null;
        for(Node node1 : n1.getNodes()){
            for(Node node2 : n2.getNodes()){
                double cur = TownUtils.getDist(node1, node2);
                if(cur < min){
                    min = cur;
                    pair = new Pair<>(node1, node2);
                }
            }
        }
        return pair;
    }

    private Pair<Node, Node> findMinNodesBClusters(Node node, Cluster n2){
        double min = Double.MAX_VALUE;
        Pair<Node, Node> pair = null;
        for(Node node2 : n2.getNodes()){
            double cur = TownUtils.getDist(node, node2);
            if(cur < min){
                min = cur;
                pair = new Pair<>(node, node2);
            }
        }
        return pair;
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

    private Cluster innerClustering(List<Town> l, int num, int level){
        List<Town> list = new ArrayList<>(l);
        if(list.size() <= k3 /*|| level >= k2*/) {
            Cluster composite = new Cluster(num);
            int i = 0;
            for(Town t : list){
                t.setNum(i++);
                composite.add(t);
            }
            return composite;
        }
        List<Town> markedTown = new ArrayList<>();
        Pair<Town, Town> maxPair = getMaxPair(list);
        markedTown.add(maxPair.getValue());
        markedTown.add(maxPair.getKey());
        list.removeAll(markedTown);

        int counter = 2;
        while (counter < k1) {
            //fixTown, toTown, dist
            Map<Pair<Town, Town>, Double> map = getMinDistToMarked(markedTown, list);
            Town maxTown = getNextMarkTown(map);
            if (maxTown != null) {
                markedTown.add(maxTown);
                list.remove(maxTown);
            }
            counter++;
        }

        List<List<Town>> res = getClusters(markedTown, list);

        Cluster clu = new Cluster(num);
        int i = 0;
        for(List<Town> list1 : res){
            Composite res1 = innerClustering(list1, i++,level + 1);
            if(res1 != null) {
                clu.add((Node) res1);
            }
        }
        return clu;
    }

    private List<List<Town>> getClusters(List<Town> markedList, List<Town> unmarkedList){
        List<List<Town>> result = new ArrayList<>();
        for(Town tm : markedList){
            result.add(new ArrayList<>());
            result.get(result.size() - 1).add(tm);
        }

        for(Town t : unmarkedList){
            double minD = Double.MAX_VALUE;
            int numMarked = -1;
            for(Town tm : markedList){
                double curD = TownUtils.getDist(t, tm);
                if(curD < minD){
                    minD = curD;
                    numMarked = markedList.indexOf(tm);
                }
            }
            result.get(numMarked).add(t);
        }
        return result;
    }

    private Pair<Town, Town> getMaxPair(List<Town> list){
        if(list.size() <= 2) throw new UnsupportedOperationException();
        Pair<Town, Town> maxPair = new Pair<>(list.get(0), list.get(1));
        double dist = Double.MIN_VALUE;
        for(Town t1 : list){
            for(Town t2 : list){
                double d = TownUtils.getDist(t1, t2);
                if(!t1.equals(t2) && d > dist){
                    dist = d;
                    maxPair = new Pair<>(t1, t2);
                }
            }
        }
        return maxPair;
    }

    private Map<Pair<Town, Town>, Double> getMinDistToMarked(List<Town> markedList, List<Town> unmarkedList){
        //fixTown, toTown, dist
        Map<Pair<Town, Town>, Double> map = new HashMap<>();
        for(Town t : unmarkedList){
            double minD = Double.MAX_VALUE;
            Town mT = null;
            for(Town tm : markedList){
                double curD = TownUtils.getDist(t, tm);
                if(curD < minD){
                    minD = curD;
                    mT = tm;
                }
            }
            map.put(new Pair<>(mT, t), minD);
        }
        return map;
    }

    private Town getNextMarkTown(Map<Pair<Town, Town>, Double> map){
        double maxD = Double.MIN_VALUE;
        Town maxT = null;
        for(Map.Entry<Pair<Town, Town>, Double> entry : map.entrySet()){
            if(entry.getValue() > maxD){
                maxD = entry.getValue();
                maxT = entry.getKey().getValue();
            }
        }
        return maxT;
    }
}
