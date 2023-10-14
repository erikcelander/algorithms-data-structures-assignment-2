package uppgift;

public class BinarySearchTree<Item extends Comparable<Item>> {
  private Node root;

  private class Node {
    private Item item;
    private Node left, right;
    private int size;

    public Node(Item item) {
      this.item = item;
      this.size = 1;
    }
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public int size() {
    return size(root);
  }

  private int size(Node x) {
    if (x == null) {
      return 0;

    }
    return x.size;
  }

}
