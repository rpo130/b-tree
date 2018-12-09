package pr.rpo.tree;

import java.util.List;

public class BTree {
    private int m;
    private Node root;
    private int height;
    private int n;

    public BTree(int order) {
        this.m = order;
        this.root = new Node(order);
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

    public boolean contains(String key, Node node) {
        if(node.isEmpty()) {
            return false;
        }else {
            for(int i = 0; i < node.size(); i++) {
                String k = node.keys.get(i);

                if(key.equals(k)) {
                    return true;
                }else if(key.compareTo(k) < 0){
                    if(node.isEndNode()) {
                        return false;
                    }else {
                        return contains(key, node.childs.get(i));
                    }
                }else {
                    if(i == node.size()-1) {
                        if(node.isEndNode()) {
                            return false;
                        }else {
                            return contains(key, node.childs.get(i + 1));
                        }
                    }else {
                        continue;
                    }
                }
            }
        }

        return false;
    }

    public void put(String key, String value) throws Exception {
        this.insert(key, value, root);
    }

    public void insert(String key, String value, Node node) throws Exception {

        if(node.isEmpty()) {
            node.setKV(0, key, value);
            n++;
        }else {
            int size = node.size();
            for(int i = size-1; i >= 0; i--) {
                String k = node.keys.get(i);
                if(key.compareTo(k) == 0) {
                    return;
                }else if(key.compareTo(k) > 0) {
                    if(node.isEndNode()) {
                        node.setKV(i + 1, key, value);
                        n++;
                        if(node.size() >= m) {
                            split(node);
                        }
                    }else {
                        insert(key, value, node.childs.get(i+1));
                    }
                    break;
                }else {
                    if(node.isEndNode()) {
                        node.setKV(i+1, node.keys.get(i), node.vals.get(i));

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
                            insert(key, value, node.childs.get(0));
                            break;
                        }
                    }
                }
            }

        }
    }

    public void split(Node node) throws Exception {
        int midSize = Math.round(node.size()/2);

        String fnkey = node.keys.get(midSize);
        String fnval = node.vals.get(midSize);

        Node ln = new Node(m);
        for(int i = 0; i < midSize; i++) {
            ln.setKV(i, node.keys.get(i), node.vals.get(i));
            if(!node.isEndNode()) {
                ln.setChild(i, node.childs.get(i));
                node.childs.get(i).father = ln;
            }
        }
        if(!node.isEndNode()) {
            ln.setChild(midSize, node.childs.get(midSize));
            node.childs.get(midSize).father = ln;
        }

        Node rn = new Node(m);
        int i = 0;
        for(i = midSize+1; i < node.size(); i++) {
            rn.setKV(i-midSize-1, node.keys.get(i), node.vals.get(i));
            if(!node.isEndNode()) {
                rn.setChild(i-midSize-1, node.childs.get(i));
                node.childs.get(i).father = rn;
            }
        }
        if(!node.isEndNode()) {
           rn.setChild(i-midSize-1, node.childs.get(node.size()));
           node.childs.get(node.size()).father = rn;
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
            //int i;
            i = fn.childs.indexOf(node);

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
                        sb.append(prefix + "(" +node.keys.get(i) + "-" + node.vals.get(i) + ")" + "\n");
                    }
                }else {
                    for (int i = 0; i < node.size(); i++) {
                        sb.append(toString(node.childs.get(i), prefix + "    "));
                        sb.append(prefix + "(" +node.keys.get(i) + "-" + node.vals.get(i) + ")" + "\n");
                    }
                    sb.append(toString(node.childs.get(node.size()), prefix + "    "));
                }
            }
            return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        BTree st = new BTree(4);

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

        System.out.println("cs.princeton.edu:  " + st.contains("www.cs.princeton.edu"));
        System.out.println("hardvardsucks.com: " + st.contains("www.harvardsucks.com"));
        System.out.println("simpsons.com:      " + st.contains("www.simpsons.com"));
        System.out.println("apple.com:         " + st.contains("www.apple.com"));
        System.out.println("ebay.com:          " + st.contains("www.ebay.com"));
        System.out.println("dell.com:          " + st.contains("www.dell.com"));

        System.out.println("size:    " + st.size());
        System.out.println("height:  " + st.height());
        System.out.println();
        System.out.println(st.toString());
    }
}
