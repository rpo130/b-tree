package pr.rpo.tree.btree;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * 原始B树
 */
public class BTree implements Serializable {
    //默认阶
    private final int M = 4;
    //阶
    private int m;

    private Node root;

    //树高度
    private int height;

    //元素数量
    private int n;

    public BTree() {
        this.m = M;
        this.root = null;
    }

    public BTree(int order) {
        this.m = order;
        this.root = null;
    }

    public int size() {
        return n;
    }

    public int height() {
        return height;
    }

    public boolean contains(String key) {
        Node r = root;

        if(r.isEmpty()) {
            return false;
        }else {
            return contains(key, r);
        }
    }

    private boolean contains(String key, Node node) {
        if(node.isEmpty()) {
            return false;
        }else {
            for(int i = 0; i < node.size(); i++) {
                String k = node.keys[i];

                if(key.equals(k)) {
                    return true;
                }else if(key.compareTo(k) < 0){//左子树
                    if(node.isEndNode()) {
                        return false;
                    }else {
                        return contains(key, node.childs[i]);
                    }
                }else {//右子树
                    if(i == node.size()-1) {
                        if(node.isEndNode()) {
                            return false;
                        }else {
                            return contains(key, node.childs[i + 1]);
                        }
                    }else {
                        continue;
                    }
                }
            }
        }

        return false;
    }

    public Object get(String key) {
        Node r = root;

        if(r.isEmpty()) {
            return null;
        }else {
            return get(key, r);
        }
    }

    private Object get(String key, Node node) {

        if(node.isEmpty()) {
            return null;
        }else {
            for(int i = 0; i < node.size(); i++) {
                String k = node.keys[i];

                if(key.equals(k)) {
                    return node.vals[i];
                }else if(key.compareTo(k) < 0){//左子树
                    if(node.isEndNode()) {
                        return null;
                    }else {
                        return get(key, node.childs[i]);
                    }
                }else {//右子树
                    if(i == node.size()-1) {
                        if(node.isEndNode()) {
                            return null;
                        }else {
                            return get(key, node.childs[i + 1]);
                        }
                    }else {
                        continue;
                    }
                }
            }
        }
        return null;
    }

    public void put(String key, Object value) {
        if(root == null) {
            root = Node.NodeCreated(key, value, m);
            height++;
            n++;
        }else {
            this.insert(key, value, root);
        }
    }

    private void insert(String key, Object value, Node node) {

        int size = node.size();
        for(int i = size-1; i >= 0; i--) {
            String k = node.keys[i];
            if(key.compareTo(k) == 0) {
                return;
            }else if(key.compareTo(k) > 0) {//右子树
                if(node.isEndNode()) {
                    node.setKV(i + 1, key, value);
                    n++;
                    if(node.size() >= m) {
                        split(node);
                    }
                }else {
                    insert(key, value, node.childs[i+1]);
                }
                break;
            }else {//左子树
                if(node.isEndNode()) {
                    node.setKV(i+1, node.keys[i], node.vals[i]);

                    if(i == 0) {
                        node.setKV(0, key, value);
                        n++;
                        if(node.size() >= m) {
                            split(node);
                        }
                        break;
                    }

                }else {
                    if(i == 0) {
                        insert(key, value, node.childs[0]);
                        break;
                    }
                }
            }
        }
    }

    public void delete(String key) {
        if(root != null) {
            delete(key, root);
        }
    }

    private void delete(String key, Node node) {
        int size = node.size();
        for(int i = size - 1; i >= 0; i--) {
            String k = node.keys[i];
            if(key.compareTo(k) == 0) {
                if(node.isEndNode()) {
                    node.removeKV(i);
                    System.out.println("delete success");
                }
                n--;
                return ;
            }else if(key.compareTo(k) > 0) {
                if(node.isEndNode()) {
                    return ;
                }else {
                    delete(key, node.childs[i+1]);
                }
            }else {

                if(i == 0) {
                    if(node.isEndNode()) {
                        return ;
                    }else {
                        delete(key, node.childs[i]);
                    }
                }else {
                    continue;
                }
            }
        }
    }

    //节点分裂
    public void split(Node node) {
        int midSize = Math.round(node.size()/2);

        String fnkey = node.keys[midSize];
        Object fnval = node.vals[midSize];

        Node ln = new Node(m);

        for(int i = 0; i < midSize; i++) {
            ln.setKV(i, node.keys[i], node.vals[i]);
            if(!node.isEndNode()) {
                ln.setChild(i, node.childs[i]);
                node.childs[i].father = ln;
            }
        }
        if(!node.isEndNode()) {
            ln.setChild(midSize, node.childs[midSize]);
            node.childs[midSize].father = ln;
        }

        Node rn = new Node(m);
        int i;
        for(i = midSize+1; i < node.size(); i++) {
            rn.setKV(i-midSize-1, node.keys[i], node.vals[i]);
            if(!node.isEndNode()) {
                rn.setChild(i-midSize-1, node.childs[i]);
                node.childs[i].father = rn;
            }
        }
        if(!node.isEndNode()) {
           rn.setChild(i-midSize-1, node.childs[node.size()]);
           node.childs[node.size()].father = rn;
        }

        //root
        if(node.father == null) {
            node.empty();
            node.setKV(0, fnkey, fnval);
            rn.father = node;
            ln.father = node;

            node.setChild(0,ln);
            node.setChild(1,rn);
            height++;
        }else {
            Node fn = node.father;

            for(int ii = 0; ii <= fn.size(); ii++) {
                if(fn.childs[ii].hashCode() == node.hashCode()) {
                    i = ii;
                    break;
                }
            }

            fn.addKV(i, fnkey, fnval);
            rn.father = fn;
            ln.father = fn;

            fn.setChild(i,rn);
            fn.addChild(i,ln);
            if(fn.size() >= m) {
                split(fn);
            }
        }
    }

    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder();
       if(root.isEmpty()) {
           sb.append("empty");
       }else {
           sb.append(toString(root,"*"));
       }
        return sb.toString();
    }

    public String toString(Node node, String prefix) {
        StringBuilder sb = new StringBuilder();
            if(node == null) {
                sb.append("null\n");
            }else if(node.isEmpty()) {
                sb.append("empty\n");
            }else {
                if(node.isEndNode()) {
                    for(int i = 0; i < node.size(); i++) {
                        sb.append(prefix + "(" +node.keys[i] + "-" + node.vals[i] + ")" + "\n");
                    }
                }else {
                    for (int i = 0; i < node.size(); i++) {
                        sb.append(toString(node.childs[i], prefix + "    "));
                        sb.append(prefix + "(" +node.keys[i] + "-" + node.vals[i] + ")" + "\n");
                    }
                    sb.append(toString(node.childs[node.size()], prefix + "    "));
                }
            }
            return sb.toString();
    }

    public void travel() {
        travelInDepth(root);
    }

    private void travelInDepth(Node node) {
        if (node == null) return;
        //深度优先便利
        for(int i = 0; i < node.size(); i++) {
            System.out.println(node.keys[i] + "-" + node.vals[i]);
        }

        for(int i = 0; i <= node.size(); i++) {
            travelInDepth(node.childs[i]);
        }
    }

    public void travelNode(Consumer<Node> consumer) {
        travelNode(root, consumer);
    }

    public void travelNode(Node node, Consumer consumer) {
        if (node == null) return;

        consumer.accept(node);

        for(int i = 0; i <= node.size(); i++) {
            travelNode(node.childs[i], consumer);
        }
    }

    public static void main(String[] args) {
        BTree st = new BTree();

        st.put("www.cs.princeton.edu", "128.112.136.12");
        st.put("www.cs.princeton.edu", "128.112.136.11");
        st.put("www.princeton.edu",    "128.112.128.15");
        st.put("www.yale.edu",         "130.132.143.21");
        st.put("www.simpsons.com",     "209.052.165.60");
        st.put("www.apple.com",        "17.112.152.32");
        st.put("www.amazon.com",       "207.171.182.16");
        st.put("www.ebay.com",         "66.135.192.87");
        st.put("www.cnn.com",          "64.236.16.20");
        st.put("www.google.com",       "216.239.41.99");
        st.put("www.nytimes.com",      "199.239.136.200");
        st.put("www.microsoft.com",    "207.126.99.140");
        st.put("www.dell.com",         "143.166.224.230");
        st.put("www.slashdot.org",     "66.35.250.151");
        st.put("www.espn.com",         "199.181.135.201");
        st.put("www.weather.com",      "63.111.66.11");
        st.put("www.yahoo.com",        "216.109.118.65");

        System.out.println("size:    " + st.size());
        System.out.println("height:  " + st.height());
        System.out.println();
//        System.out.println(st.toString());
        st.travelNode(e -> System.out.println(Arrays.toString(e.keys) + "-size" + e.size()));
    }
}
