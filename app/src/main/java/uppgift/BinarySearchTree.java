package uppgift;

import java.util.Iterator;

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

  private int size(Node node) {
    if (node == null) {
      return 0;

    }
    return node.size;
  }

  public void add(Item item) {
    root = add(root, item);
  }

  private Node add(Node node, Item item) {
    if (node == null) {
      return new Node(item);
    }

    // compare item to x.item
    int compare = item.compareTo(node.item);
    if (compare < 0) {
      // if item is less than x.item, go left
      node.left = add(node.left, item);
    } else if (compare > 0) {
      // if item is greater than x.item, go right
      node.right = add(node.right, item);
    } else {
      // if item is equal to x.item, update x.item
      node.item = item;
    }

    // update size
    node.size = 1 + size(node.left) + size(node.right);

    return node;
  }

  public boolean contains(Item item) {
    return contains(root, item) != null;
  }

  private Item contains(Node node, Item item) {
    if (node == null) {
      return null;
    }

    // compare item to x.item
    int compare = item.compareTo(node.item);
    if (compare < 0) {
      // if item is less than x.item, go left
      return contains(node.left, item);
    } else if (compare > 0) {
      // if item is greater than x.item, go right
      return contains(node.right, item);
    } else {
      // if item is equal to x.item, return x.item
      return node.item;
    }
  }

  public int height() {
    return height(root);
  }

  private int height(Node node) {
    if (node == null)
      return -1; // base case

    // recursive calls
    return 1 + Math.max(height(node.left), height(node.right));
  }

  public void remove(Item item) {
    root = remove(root, item);
  }

  private Node remove(Node node, Item item) {
    if (node == null)
      return null;

    // find node to remove
    int cmp = item.compareTo(node.item);
    if (cmp < 0)
      node.left = remove(node.left, item);
    else if (cmp > 0)
      node.right = remove(node.right, item);

    else {
      if (node.right == null)
        return node.left;
      if (node.left == null)
        return node.right;

      // replace node with minimum node of right sub-tree
      Node temp = node;
      node = min(temp.right);
      node.right = deleteMin(temp.right);
      node.left = temp.left;
    }
    node.size = size(node.left) + size(node.right) + 1;
    return node;
  }

  private Node min(Node x) {
    if (x.left == null)
      return x;
    return min(x.left);
  }

  private Node deleteMin(Node x) {
    if (x.left == null)
      return x.right;
    x.left = deleteMin(x.left);
    x.size = size(x.left) + size(x.right) + 1;
    return x;
  }



  public static void main(String[] args) {
    BinarySearchTree<String> bst = new BinarySearchTree<>();

    // Test: isEmpty on an empty BST
    assert bst.isEmpty() : "BST should be empty initially";

    // Test: size on an empty BST
    assert bst.size() == 0 : "Size should be 0 initially";

    // Test: add
    bst.add("A");
    assert bst.size() == 1 : "Size should be 1 after adding one item";
    assert !bst.isEmpty() : "BST should not be empty after adding an item";

    // Test: add more items
    bst.add("B");
    bst.add("C");
    assert bst.size() == 3 : "Size should be 3 after adding three items";

    // Test: contains
    assert bst.contains("A") : "BST should contain 'A'";
    assert bst.contains("B") : "BST should contain 'B'";
    assert bst.contains("C") : "BST should contain 'C'";
    assert !bst.contains("D") : "BST should not contain 'D'";

    // Test: remove
    bst.remove("A");
    assert bst.size() == 2 : "Size should be 2 after removing one item";
    assert !bst.contains("A") : "BST should not contain 'A' after removal";

    // Test: Exception handling for remove
    try {
      bst.remove("Z");
      assert false : "Expected exception when removing a non-existent item";
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    // Test: height
    assert bst.height() == 1 : "Height should be 1";

    System.out.println("All tests passed!");
  }

}
