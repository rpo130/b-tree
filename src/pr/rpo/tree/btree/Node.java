package pr.rpo.tree.btree;

import java.util.ArrayList;
import java.util.List;

//BTREE 节点
public class Node {
    //阶
    public int m;

    //父节点
    public Node father;

    public List<String> keys;
    public List<Object> vals;
    public List<Node> childs;

    public static Node NodeCreated(String key, Object val, int order) {
        Node nn = new Node(order);
        nn.addKV(0, key, val);
        return nn;
    }

    public Node(int order) {
        if(order < 2) {
            throw new IllegalArgumentException();
        }
        this.m = order;
        this.father = null;
        this.keys = new ArrayList<>(0);
        this.vals = new ArrayList<>(0);
        this.childs = new ArrayList<>(0);

        for(int i = 0; i < order; i++) {
            this.keys.add(null);
            this.vals.add(null);
            this.childs.add(null);
        }
        this.childs.add(null);
    }

    public int size() {
        int size = 0;
        for(String s : keys) {
            if(s != null) {
                size++;
            }
        }
        return size;
    }

    public boolean isEmpty() {
        return this.size() == 0;
     }

     //已经处于最底层
    public boolean isEndNode() {
        boolean empty = true;
        for(Node s : childs) {
            if(s != null) {
                return false;
            }
        }
        return empty;
    }

    public void setKV(int index, String key, Object value) {
        if(index >= m) throw new IllegalArgumentException("数组越界");
        keys.set(index, key);
        vals.set(index, value);
    }

    public void addKV(int index, String key, Object value) {
        keys.add(index, key);
        keys.remove(m);
        vals.add(index, value);
        vals.remove(m);
    }


    public void setChild(int index, Node child) {
        if(index >= m+1) throw new IllegalArgumentException("数组越界");
        childs.set(index, child);
    }

    public void addChild(int index, Node child) {
        childs.add(index, child);
        childs.remove(m+1);
    }

    public void empty() {
        int i = 0;
        for(i = 0; i < m; i++) {
            this.keys.set(i, null);
            this.vals.set(i, null);
            this.childs.set(i, null);
        }
        this.childs.set(i, null);
    }

    public static void main(String[] args) throws Exception {
        Node n = new Node(4);
        assert n.size() == 0;
        assert n.isEmpty() == true;
        n.setKV(0, "a","b");
        assert n.size() == 1;
        assert n.isEmpty() == false;
        n.empty();
        assert n.isEmpty() == true;
    }
}
