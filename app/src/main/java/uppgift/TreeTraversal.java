package uppgift;

public interface TreeTraversal<Item extends Comparable<Item>> {
    Node<Item> getRoot();
    Node<Item> getLeftChild(Node<Item> node);
    Node<Item> getRightChild(Node<Item> node);

    interface Node<Item> {
        Item getItem();
    }
}