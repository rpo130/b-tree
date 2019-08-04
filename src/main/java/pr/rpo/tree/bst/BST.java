package pr.rpo.tree.bst;

public class BST {

    Node root;

    public BST() {
        root = null;
    }

    public Node nodeCreate(int value) {
        return new Node(value, null, null);
    }

    public void add(Node start, Node newNode) {
        if(root == null) {
            root = newNode;
            return;
        }

        if(newNode.value > start.value) {
            if(start.right == null) {
                start.right = newNode;
            }else {
                add(start.right, newNode);
            }
        }

        if(newNode.value < start.value) {
            if(start.left == null) {
                start.left = newNode;
            }else {
                add(start.left, newNode);
            }
        }
    }

    public void Search(int value, Node start) {
        if(start == null) {
            System.out.println("node not found");
            return;
        }

        if(start.value == value) {
            System.out.println("node fount");
            return;
        }

        if(value > start.value) {
            Search(value, start.right);
        }

        if(value < start.value) {
            Search(value, start.left);
        }

    }


    public static void main(String[] args) {
        BST bst = new BST();
        bst.add(bst.root, bst.nodeCreate(10));
        bst.add(bst.root, bst.nodeCreate(12));
        bst.add(bst.root, bst.nodeCreate(9));
        bst.add(bst.root, bst.nodeCreate(13));
        bst.add(bst.root, bst.nodeCreate(6));
        bst.Search(12, bst.root);
        bst.Search(100, bst.root);

    }
}
