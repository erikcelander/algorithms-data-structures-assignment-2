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

  public Node add(Item item) {
    root = add(root, item);
    return root;
  }

  private Node add(Node x, Item item) {
    if (x == null) {
      return new Node(item);
    }

    // compare item to x.item
    int cmp = item.compareTo(x.item);
    if (cmp < 0) {
      // if item is less than x.item, go left
      x.left = add(x.left, item);
    } else if (cmp > 0) {
      // if item is greater than x.item, go right
      x.right = add(x.right, item);
    } else {
      // if item is equal to x.item, update x.item
      x.item = item;
    }

    // update size
    x.size = 1 + size(x.left) + size(x.right);

    return x;
  }

  public Item contains(Item item) {
    return contains(root, item);
  }

  private Item contains(Node x, Item item) {
    if (x == null) {
      return null;
    }

    // compare item to x.item
    int cmp = item.compareTo(x.item);
    if (cmp < 0) {
      // if item is less than x.item, go left
      return contains(x.left, item);
    } else if (cmp > 0) {
      // if item is greater than x.item, go right
      return contains(x.right, item);
    } else {
      // if item is equal to x.item, return x.item
      return x.item;
    }
  }

}
