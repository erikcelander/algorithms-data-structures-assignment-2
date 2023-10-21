package uppgift;

import java.util.Iterator;

public class RedBlackTree<Item extends Comparable<Item>> implements TreeTraversal<Item> {
  private static final boolean RED = true;
  private static final boolean BLACK = false;

  private Node root;

  private class Node implements TreeTraversal.Node<Item> {
    private Item item;
    private Node left, right;
    private boolean color;
    private int size;

    public Node(Item item, boolean color) {
      this.item = item;
      this.color = color;
      this.size = 1;
    }

    @Override
    public Item getItem() {
      return item;
    }
  }

  @Override
  public TreeTraversal.Node<Item> getRoot() {
    return root;
  }

  @Override
  public TreeTraversal.Node<Item> getLeftChild(TreeTraversal.Node<Item> node) {
    return ((Node) node).left;
  }

  @Override
  public TreeTraversal.Node<Item> getRightChild(TreeTraversal.Node<Item> node) {
    return ((Node) node).right;
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

  private boolean isRed(Node node) {
    if (node == null) {
      return false;
    }
    return node.color == RED;
  }

  public void add(Item item) {
    root = add(root, item);
    root.color = BLACK;
  }

  private Node add(Node node, Item item) {
    if (node == null) {
      return new Node(item, RED);
    }

    int compare = item.compareTo(node.item);
    if (compare < 0) {
      node.left = add(node.left, item);
    } else if (compare > 0) {
      node.right = add(node.right, item);
    } else {
      node.item = item;
    }

    if (isRed(node.right) && !isRed(node.left)) {
      node = rotateLeft(node);
    }
    if (isRed(node.left) && isRed(node.left.left)) {
      node = rotateRight(node);
    }
    if (isRed(node.left) && isRed(node.right)) {
      flipColors(node);
    }

    node.size = size(node.left) + size(node.right) + 1;
    return node;
  }

  private Node rotateLeft(Node node) {
    Node temp = node.right;
    node.right = temp.left;
    temp.left = node;
    temp.color = node.color;
    node.color = RED;
    temp.size = node.size;
    node.size = size(node.left) + size(node.right) + 1;
    return temp;
  }

  private Node rotateRight(Node node) {
    Node temp = node.left;
    node.left = temp.right;
    temp.right = node;
    temp.color = node.color;
    node.color = RED;
    temp.size = node.size;
    node.size = size(node.left) + size(node.right) + 1;
    return temp;
  }

  private void flipColors(Node node) {
    node.color = !node.color;
    node.left.color = !node.left.color;
    node.right.color = !node.right.color;
  }

  private Node find(Item item) {
    return find(root, item);
  }

  private Node find(Node node, Item item) {
    if (node == null) {
      return null;
    }

    int compare = item.compareTo(node.item);
    if (compare < 0) {
      return find(node.left, item);
    } else if (compare > 0) {
      return find(node.right, item);
    } else {
      return node;
    }
  }

  public boolean contains(Item item) {
    return find(item) != null;
  }

  public InOrderIterator<Item> inOrderIterator() {
    return new InOrderIterator<>(this);
  }

  public Iterator<Item> preOrderIterator() {
    return new PreOrderIterator<>(this);
  }

  public Iterator<Item> postOrderIterator() {
    return new PostOrderIterator<>(this);
  }

  public boolean isRBTreeValid() {
    if (root == null)
      return true; // An empty tree is valid

    // Check 1: Root is black
    if (isRed(root)) {
      System.out.println("Root is red");
      return false;
    }

    // Check 2: No red node has a red child
    if (!checkNoRedHasRedChild(root)) {
      System.out.println("Red node with red child found");
      return false;
    }

    // Check 3: Black height is consistent
    int blackHeight = -1; // Initial value
    if (!checkBlackHeight(root, 0, blackHeight)) {
      System.out.println("Black heights inconsistent");
      return false;
    }

    return true;
  }

  private boolean checkNoRedHasRedChild(Node node) {
    if (node == null)
      return true;

    if (isRed(node)) {
      if (isRed(node.left) || isRed(node.right)) {
        return false;
      }
    }

    return checkNoRedHasRedChild(node.left) && checkNoRedHasRedChild(node.right);
  }

  private boolean checkBlackHeight(Node node, int currentHeight, int blackHeight) {
    if (node == null) {
      if (blackHeight == -1) {
        blackHeight = currentHeight;
      }
      return currentHeight == blackHeight;
    }

    if (!isRed(node))
      currentHeight++;

    return checkBlackHeight(node.left, currentHeight, blackHeight)
        && checkBlackHeight(node.right, currentHeight, blackHeight);
  }

  public void printInOrderWithColor() {
    printInOrderWithColor(root);
  }

  private void printInOrderWithColor(Node node) {
    if (node == null)
      return;

    printInOrderWithColor(node.left);
    System.out.println(node.item + " (" + (node.color == RED ? "RED" : "BLACK") + ")");
    printInOrderWithColor(node.right);
  }

  public static void main(String[] args) {
    RedBlackTree<Integer> tree = new RedBlackTree<>();
    assert tree.isEmpty() : "Tree should be empty initially";
    assert tree.size() == 0 : "Size should be 0 initially";
    int[] nodes = { 50, 30, 70, 20, 40, 60, 80, 10, 35, 55, 75, 90 };
    for (int node : nodes) {
      tree.add(node);
    }

    System.out.println("In-order traversal with colors:");
    tree.printInOrderWithColor();

    if (tree.isRBTreeValid()) {
      System.out.println("Tree is a valid Red-Black Tree!");
    } else {
      System.out.println("Tree is not a valid Red-Black Tree!");
    }

    RedBlackTree<Integer> tree2 = new RedBlackTree<>();
    assert tree2.isEmpty() : "Tree should be empty initially";
    assert tree2.size() == 0 : "Size should be 0 initially";
    int[] nodes2 = { 100, 90, 80, 70, 60, 50, 40, 30, 20, 10 };
    for (int node : nodes2) {
      tree2.add(node);
    }

    System.out.println("In-order traversal with colors:");
    tree2.printInOrderWithColor();

    if (tree2.isRBTreeValid()) {
      System.out.println("Tree is a valid Red-Black Tree!");
    } else {
      System.out.println("Tree is not a valid Red-Black Tree!");
    }

    RedBlackTree<Integer> tree3 = new RedBlackTree<>();
    assert tree3.isEmpty() : "Tree should be empty initially";
    assert tree3.size() == 0 : "Size should be 0 initially";
    int[] nodes3 = { 50, 30, 70, 10, 40, 60, 80, 20, 90, 35, 55, 75 };
    for (int node : nodes3) {
      tree3.add(node);
    }

    System.out.println("In-order traversal with colors (Mixed Order Sequence):");
    tree3.printInOrderWithColor();

    if (tree3.isRBTreeValid()) {
      System.out.println("Tree is a valid Red-Black Tree!");
    } else {
      System.out.println("Tree is not a valid Red-Black Tree!");
    }

  }

}
