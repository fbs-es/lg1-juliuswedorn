package fbs.lg1;

public class Tree {

    private int[] values = {8, 3, 10, 1, 6, 14, 4, 7, 13};
    private Node root;

    public Tree() {
        for (int val : values) {
            this.root = insert(this.root, val);
        }

        printTree(this.root, 0);

        System.out.println();
        System.out.print("Preorder:  ");
        printPreorder(root);
        System.out.println();

        System.out.print("Inorder:   ");
        printInorder(root);
        System.out.println();

        System.out.print("Postorder: ");
        printPostorder(root);
        System.out.println();
    }

     Node insert(Node node, int val) {
        if (node == null) return new Node(new Node[2], val);
        int i = val < node.getValue() ? 0 : 1;
        node.getkids()[i] = insert(node.getkids()[i], val);
        return node;
    }


    void printTree(Node root, int level) {
        if (root == null) return;
        printTree(root.getkids()[1], level + 1);
        for (int i = 0; i < level; i++) System.out.print("   ");
        System.out.println(root.getValue());
        printTree(root.getkids()[0], level + 1);
    }

    void printPreorder(Node root) {
        if (root == null) return;
        System.out.print(root.getValue() + ", ");
        printPreorder(root.getkids()[0]);
        printPreorder(root.getkids()[1]);
    }

    void printPostorder(Node root) {
        if (root == null) return;
        printPostorder(root.getkids()[0]);
        printPostorder(root.getkids()[1]);
        System.out.print(root.getValue() + ", ");
    }

    void printInorder(Node root) {
        if (root == null) return;
        printInorder(root.getkids()[0]);
        System.out.print(root.getValue() + ", ");
        printInorder(root.getkids()[1]);
    }
}