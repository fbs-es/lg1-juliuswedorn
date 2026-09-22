package fbs.lg1;

public class Node {
    private Node[] kids;
    private int value;
    Node(Node[] kids,int value) {
        this.kids = kids;
        this.value = value;
    }

    Node[] getkids ()
    {
        return this.kids;
    }
    int getValue(){
        return this.value;
    }
}
