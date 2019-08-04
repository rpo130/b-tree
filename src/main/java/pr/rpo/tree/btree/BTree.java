package pr.rpo.tree.btree;

import pr.rpo.Pair;

import java.io.Serializable;
import java.util.function.Consumer;

public class BTree implements Serializable {
    //默认阶
    private final int M = 4;

    private int m;
    private Node root;
    private int height;
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

    private boolean contains(String key) {
        if(nodeContains(key) != null) {
            return true;
        }else {
            return false;
        }
    }

    private Node nodeContains(String key) {

        if(root.isEmpty()) {
            return null;
        }

        Node node = null;
        do {
            if(node == null) {
                node = root;
            }else {
                node = node.childs[node.findIndex(key)];
            }

            if(node.contains(key)) {
                return node;
            }
        }while(!node.isEndNode());

        return null;
    }

    public Object get(String key) {

        Node node = nodeContains(key);
        if(node != null) {
            return node.get(key);
        }else {
            return null;
        }
    }

    public void put(String key, Object value) {
        if(root == null) {
            root = new Node(key, value, m);
            height++;
            n++;
        }else {
            Node node = nodeContains(key);
            if(node == null) {
                this.insert(key, value);
            }else {
                //
            }
        }
    }

    private Node findInLeafNode(String key) {
        Node node = root;

        while(!node.isEndNode()) {
            int index = node.findIndex(key);
            node = node.childs[index];
        }
        return node;
    }

    private void insert(String key, Object value) {

        Node node = findInLeafNode(key);
        node.addKV(key, value);
        n++;
        if(node.isRoot()) {
            checkAndSplit(node);
        }else {
            do {
                checkAndSplit(node);
                node = node.father;
                if(node == null) {
                    break;
                }
            }while(needSplit(node));
        }
    }

    private boolean needSplit(Node node) {
        if(node.size() >= m) {
            return true;
        }else {
            return false;
        }
    }

    private void checkAndSplit(Node node) {
        if(needSplit(node)) {
            split(node);
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
        if(needSplit(node)) {
            int splitIndex = (node.size() - 1) / 2;//floor
            if(node.isRoot()) {
                Pair<Pair<String, Object>,Node> p = node.split(splitIndex);
                String k = p.first().first();
                Object v = p.first().senond();
                Node rnode = p.senond();

                Node fnode = new Node(m);
                fnode.addKV(k,v,node,rnode);

                node.father = fnode;
                rnode.father = fnode;

                root = fnode;
            }else {
                Pair<Pair<String, Object>,Node> p = node.split(splitIndex);
                String k = p.first().first();
                Object v = p.first().senond();
                Node rnode = p.senond();

                Node fnode = node.father;

                rnode.father = fnode;

                fnode.addKV(k,v,node,rnode);
            }
        }
    }

    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder();
       if(root == null || root.isEmpty()) {
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
        if (node == null) {
            return;
        }
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
        if (node == null) {
            return;
        }

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
        System.out.println(st.toString());
//        st.travelNode(e -> System.out.println(Arrays.toString(e.keys) + "-size" + e.size()));
    }
}
