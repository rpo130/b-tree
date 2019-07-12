package pr.rpo.tree.btree;

import java.io.Serializable;

//BTREE 节点
public class Node implements Serializable {
    //阶
    public int m;

    //父节点
    public Node father;

    public String[] keys;
    public Object[] vals;
    public Node[] childs;

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
        //额外加1，临时使用
        this.keys = new String[order];
        this.vals = new Object[order];
        this.childs = new Node[order + 1];

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
        keys[index] = key;
        vals[index] = value;
    }

    public void addKV(int index, String key, Object value) {
        for(int i = m - 1; i > index; i--) {
            keys[i] = keys[i-1];
            vals[i] = vals[i-1];
        }
        keys[index] = key;
        vals[index] = value;
    }

    public void removeKV(int index) {
        for(int i = index; i < m - 1; i++) {
            keys[i] = keys[i+1];
            vals[i] = vals[i+1];
        }
    }

    public void setChild(int index, Node child) {
        if(index >= m) throw new IllegalArgumentException("数组越界");
        childs[index] = child;
    }

    public void addChild(int index, Node child) {
        for(int i = m; i > index; i--) {
            childs[i] = childs[i-1];
        }
        childs[index] = child;
    }

    public void empty() {
        int i = 0;
        for(i = 0; i < m; i++) {
            this.keys[i] = null;
            this.vals[i] = null;
            this.childs[i] = null;
        }
        this.childs[i] = null;
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
