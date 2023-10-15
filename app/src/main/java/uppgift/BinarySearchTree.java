package uppgift;

import java.util.Iterator;
import java.util.Stack;

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

    // compare item to node.item
    int compare = item.compareTo(node.item);
    if (compare < 0) {
      // if item is less than node.item, go left
      node.left = add(node.left, item);
    } else if (compare > 0) {
      // if item is greater than node.item, go right
      node.right = add(node.right, item);
    } else {
      // if item is equal to node.item, update node.item
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

    // compare item to node.item
    int compare = item.compareTo(node.item);
    if (compare < 0) {
      // if item is less than node.item, go left
      return contains(node.left, item);
    } else if (compare > 0) {
      // if item is greater than node.item, go right
      return contains(node.right, item);
    } else {
      // if item is equal to node.item, return node.item
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

  private Node min(Node node) {
    if (node.left == null)
      return node;
    return min(node.left);
  }

  private Node deleteMin(Node node) {
    if (node.left == null)
      return node.right;
    node.left = deleteMin(node.left);
    node.size = size(node.left) + size(node.right) + 1;
    return node;
  }

  private class InOrderIterator implements Iterator<Item> {
    private Stack<Node> stack = new Stack<>();

    public InOrderIterator() {
      pushLeft(root);
    }

    // push all left nodes to stack
    private void pushLeft(Node node) {
      while (node != null) {
        stack.push(node);
        node = node.left;
      }
    }

    // check if stack is not empty
    public boolean hasNext() {
      return !stack.isEmpty();
    }

    // pop node from stack and push all left nodes of right child to stack
    public Item next() {
      Node current = stack.pop();
      pushLeft(current.right);
      return current.item;
    }
  }

  public Iterator<Item> inOrderIterator() {
    return new InOrderIterator();
  }

  private class PreOrderIterator implements Iterator<Item> {
    private Stack<Node> stack = new Stack<>();

    public PreOrderIterator() {
      // push root to stack
      if (root != null)
        stack.push(root);
    }

    public boolean hasNext() {
      return !stack.isEmpty();
    }

    // pop node from stack and push right child first and then left child
    public Item next() {
      Node current = stack.pop();
      if (current.right != null)
        stack.push(current.right);
      if (current.left != null)
        stack.push(current.left);
      return current.item;
    }
  }

  public Iterator<Item> preOrderIterator() {
    return new PreOrderIterator();
  }

  public static void main(String[] args) {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();

    // Test: isEmpty on an empty tree
    assert bst.isEmpty() : "Tree should be empty initially";

    // Test: size on an empty tree
    assert bst.size() == 0 : "Size should be 0 initially";

    // Add elements to the tree
    int[] elements = { 5, 3, 8, 1, 4, 7, 9 };
    for (int el : elements) {
      bst.add(el);
    }

    // 5
    // / \
    // 3 8
    // / \ / \
    // 1 4 7 9

    assert !bst.isEmpty() : "Tree should not be empty after adding elements";
    assert bst.size() == 7 : "Size should be 7 after adding seven elements";

    // Test: contains
    assert bst.contains(5) : "Tree should contain 5";
    assert !bst.contains(10) : "Tree should not contain 10";

    // Test: height
    assert bst.height() == 2 : "Height of the tree should be 2";

    // Test: InOrderIterator
    // Test: InOrderIterator
    Iterator<Integer> inOrderIterator = bst.inOrderIterator();
    int[] inOrderExpectedResult = { 1, 3, 4, 5, 7, 8, 9 };
    int index = 0;
    StringBuilder inOrderOutput = new StringBuilder("In-order output: ");
    while (inOrderIterator.hasNext()) {
      int currentValue = inOrderIterator.next();
      inOrderOutput.append(currentValue);
      if (inOrderIterator.hasNext()) {
        inOrderOutput.append(", ");
      }
      assert currentValue == inOrderExpectedResult[index++]
          : "In-order traversal is incorrect at value " + currentValue;
    }
    System.out.println(inOrderOutput.toString());

    // Test: PreOrderIterator
    Iterator<Integer> preOrderIterator = bst.preOrderIterator();
    int[] preOrderExpectedResult = { 5, 3, 1, 8, 7, 4, 9 };
    index = 0;
    StringBuilder preOrderOutput = new StringBuilder("Pre-order output: ");
    while (preOrderIterator.hasNext()) {
      int currentValue = preOrderIterator.next();
      preOrderOutput.append(currentValue);
      if (preOrderIterator.hasNext()) {
        preOrderOutput.append(", ");
      }
      assert currentValue == preOrderExpectedResult[index++]
          : "Pre-order traversal is incorrect at value " + currentValue;
    }
    System.out.println(preOrderOutput.toString());

    System.out.println("All tests passed!");
  }

}
