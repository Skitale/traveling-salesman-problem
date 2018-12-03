package com.traning.task5;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TownUtils {
    public static double getDist(Node t1, Node t2){
        double dX = t1.getX() - t2.getX();
        double dY = t1.getY() - t2.getY();
        dX = Math.pow(dX, 2);
        dY = Math.pow(dY, 2);
        return Math.sqrt(dX + dY);
    }

    public static Pair<Node, Node> getMaxPair(List<Node> list){
        if(list.size() <= 2) throw new UnsupportedOperationException();
        Pair<Node, Node> maxPair = new Pair<>(list.get(0), list.get(1));
        double dist = Double.MIN_VALUE;
        for(Node t1 : list){
            for(Node t2 : list){
                double d = TownUtils.getDist(t1, t2);
                if(!t1.equals(t2) && d > dist){
                    dist = d;
                    maxPair = new Pair<>(t1, t2);
                }
            }
        }
        return maxPair;
    }

    public static Map<Pair<Node, Node>, Double> getMinDistToMarked(List<Node> markedList, List<Node> unmarkedList){
        //fixTown, toTown, dist
        Map<Pair<Node, Node>, Double> map = new HashMap<>();
        for(Node t : unmarkedList){
            double minD = Double.MAX_VALUE;
            Node mT = null;
            for(Node tm : markedList){
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

    public static Node getNextMarkTown(Map<Pair<Node, Node>, Double> map){
        double maxD = Double.MIN_VALUE;
        Node maxT = null;
        for(Map.Entry<Pair<Node, Node>, Double> entry : map.entrySet()){
            if(entry.getValue() > maxD){
                maxD = entry.getValue();
                maxT = entry.getKey().getValue();
            }
        }
        return maxT;
    }

    public static List<List<Node>> getClusters(List<Node> markedList, List<Node> unmarkedList){
        List<List<Node>> result = new ArrayList<>();
        for(Node tm : markedList){
            result.add(new ArrayList<>());
            result.get(result.size() - 1).add(tm);
        }

        for(Node t : unmarkedList){
            double minD = Double.MAX_VALUE;
            int numMarked = -1;
            for(Node tm : markedList){
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

    public static Node getPrevNode(Composite comp, Node node){
        if(comp.getClusters().size() < 2) return null;
        for(Node n : comp.getNodes()){
            if(n.nextNode().equals(node)){
                return n;
            }
        }
        throw new UnsupportedOperationException();
    }

    public static void reverseOrder(Composite cluster, Node finish, Node start){
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

    public static Pair<Node, Node> findMinNodesBClusters(Composite n1, Composite n2){
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

    public static Pair<Node, Node> findMinNodesBClusters(Node node, Composite n2){
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
}
