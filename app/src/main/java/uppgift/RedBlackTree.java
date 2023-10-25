package uppgift;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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

  public int height() {
    return height(root);
  }

  private int height(Node node) {
    if (node == null) {
      return -1;
    }

    // recursive calls
    return 1 + Math.max(height(node.left), height(node.right));
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
      return true; 

    if (isRed(root)) {
      System.out.println("Root is red");
      return false;
    }

    if (!checkNoRedHasRedChild(root)) {
      System.out.println("Red node with red child found");
      return false;
    }

    int blackHeight = -1; 
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

  public void delete(Item item) {
    if (contains(item)) {
      root = delete(root, item);
      if (root != null)
        root.color = BLACK;
    }
  }

  private Node delete(Node node, Item item) {
    if (item.compareTo(node.item) < 0) {
      if (!isRed(node.left) && !isRed(node.left.left)) {
        node = moveRedLeft(node);
      }
      node.left = delete(node.left, item);
    } else {
      if (isRed(node.left)) {
        node = rotateRight(node);
      }
      if (item.compareTo(node.item) == 0 && (node.right == null)) {
        return null;
      }
      if (!isRed(node.right) && !isRed(node.right.left)) {
        node = moveRedRight(node);
      }
      if (item.compareTo(node.item) == 0) {
        Node min = getMin(node.right);
        node.item = min.item;
        node.right = deleteMin(node.right);
      } else {
        node.right = delete(node.right, item);
      }
    }
    return balance(node);
  }

  private Node moveRedLeft(Node node) {
    flipColors(node);
    if (isRed(node.right.left)) {
      node.right = rotateRight(node.right);
      node = rotateLeft(node);
      flipColors(node);
    }
    return node;
  }

  private Node moveRedRight(Node node) {
    flipColors(node);
    if (isRed(node.left.left)) {
      node = rotateRight(node);
      flipColors(node);
    }
    return node;
  }

  private Node deleteMin(Node node) {
    if (node.left == null)
      return null;

    if (!isRed(node.left) && !isRed(node.left.left))
      node = moveRedLeft(node);

    node.left = deleteMin(node.left);

    return balance(node);
  }

  private Node getMin(Node node) {
    while (node.left != null) {
      node = node.left;
    }
    return node;
  }

  private Node balance(Node node) {
    if (isRed(node.right)) {
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


  public static void main(String[] args) {
    RedBlackTree<Integer> tree = new RedBlackTree<>();
    List<Integer> numbers = new ArrayList<>();

    for (int i = 0; i < 100; i++) {
        numbers.add(i);
    }
    Collections.shuffle(numbers);
    for (int num : numbers) {
        tree.add(num);
    }

    Collections.shuffle(numbers);
    for (int i = 0; i < 50; i++) {
        tree.delete(numbers.get(i));
    }

    for (int i = 0; i < 25; i++) {
        tree.add(numbers.get(i));
    }

    long start = System.nanoTime();
    tree.add(150);
    long duration = System.nanoTime() - start;
    System.out.println("Insertion time: " + duration + " nanoseconds");

    start = System.nanoTime();
    tree.delete(50);
    duration = System.nanoTime() - start;
    System.out.println("Deletion time: " + duration + " nanoseconds");

    System.out.println("Height of tree: " + tree.height());

    if (tree.isRBTreeValid()) {
        System.out.println("Tree is a valid Red-Black Tree!");
    } else {
        System.out.println("Tree is not a valid Red-Black Tree!");
    }
}



}