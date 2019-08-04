package pr.rpo.tree.btree;

import pr.rpo.Pair;

import java.io.Serializable;

class Node implements Serializable {
    public int m;

    public Node father;

    public String[] keys;
    public Object[] vals;
    public Node[] childs;

    public Node(String key, Object val, int order) {
        if (order < 2) {
            throw new IllegalArgumentException();
        }
        this.m = order;
        this.father = null;
        //额外加1，临时使用
        this.keys = new String[order];
        this.vals = new Object[order];
        this.childs = new Node[order + 1];

        keys[0] = key;
        vals[0] = val;
    }

    public Node(int order) {
        if (order < 2) {
            throw new IllegalArgumentException();
        }
        this.m = order;
        this.father = null;
        //plus 1 for split node status
        this.keys = new String[order];
        this.vals = new Object[order];
        this.childs = new Node[order + 1];

    }

    public int size() {
        int size = 0;
        //should not contain null between data
        for (String s : keys) {
            if (s != null) {
                size++;
            } else {
                break;
            }
        }
        return size;
    }

    public boolean isRoot() {
        if (father == null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public boolean isEndNode() {
        for (Node s : childs) {
            //should not contain null between data
            if (s != null) {
                return false;
            } else {
                break;
            }
        }
        return true;
    }

    public Object get(String key) {
        if (contains(key)) {
            int index = findIndex(key);
            return vals[index];
        } else {
            return null;
        }
    }

    public void addKV(String key, Object value) {
        int i = findIndex(key);
        addKV(i, key, value);
    }

    public void addKVWithLC(String key, Object value, Node leftChild) {
        int i = findIndex(key);
        addKV(i, key, value);
        addChild(i, leftChild);
    }

    public void addKV(String key, Object value, Node leftChild, Node rightNode) {
        int i = findIndex(key);
        addKVWithLC(key, value, leftChild);
        setChilds(i + 1, rightNode);
    }

    private void addKV(int index, String key, Object value) {
        if (index >= m) {
            throw new IllegalArgumentException("index out");
        }

        for (int i = m - 1; i > index; i--) {
            keys[i] = keys[i - 1];
            vals[i] = vals[i - 1];
        }
        keys[index] = key;
        vals[index] = value;
    }

    public void removeKV(int index) {
        for (int i = index; i < m - 1; i++) {
            keys[i] = keys[i + 1];
            vals[i] = vals[i + 1];
        }
    }

    private void removeKVAndRC(int index) {
        if (index > m) {
            throw new IndexOutOfBoundsException();
        }
        int size = size();
        for (int i = index; i < size - 1; i++) {
            keys[i] = keys[i + 1];
            vals[i] = vals[i + 1];
            childs[i + 1] = childs[i + 2];
        }
    }

    private void removeKVAndRCFrom(int index) {
        if (index > m) {
            throw new IndexOutOfBoundsException();
        }

        int size = size();
        for (int i = index; i < size; i++) {
            keys[i] = null;
            vals[i] = null;
            childs[i + 1] = null;
        }
    }

    private void setChilds(int index, Node child) {
        childs[index] = child;
        child.father = this;
    }

    private void addChild(int index, Node child) {
        for (int i = m; i > index; i--) {
            childs[i] = childs[i - 1];
        }
        childs[index] = child;
        child.father = this;
    }

    public void empty() {
        int i = 0;
        for (i = 0; i < m; i++) {
            this.keys[i] = null;
            this.vals[i] = null;
            this.childs[i] = null;
        }
        this.childs[i] = null;
    }

    public boolean contains(String key) {
        int i = size();
        while (i-- != 0) {
            if (keys[i].compareTo(key) == 0) {
                return true;
            }
        }
        return false;
    }

    public int findIndex(String key) {
        int s = size();
        int i;

        for (i = 0; i < s; i++) {
            if (keys[i] == null) {
                break;
            }

            if (key.compareTo(keys[i]) < 0) {
                break;
            }
        }
        return i;
    }

    public Pair<Pair<String, Object>, Node> split(int splitIndex) {
            String fkey = keys[splitIndex];
            Object fval = vals[splitIndex];

            Node rnode = new Node(m);

            int ri = 0;
            int i = 0;
            for (i = splitIndex + 1; i < size(); i++) {
                rnode.keys[ri] = keys[i];
                rnode.vals[ri] = vals[i];
                rnode.childs[ri] = childs[i];
                if(rnode.childs[ri] != null) {
                    rnode.childs[ri].father = rnode;
                }
                ri++;
            }
            rnode.childs[ri] = childs[i];
            if(rnode.childs[ri] != null) {
                rnode.childs[ri].father = rnode;
            }

            removeKVAndRCFrom(splitIndex);

            return Pair.from(Pair.from(fkey, fval), rnode);
    }
}
