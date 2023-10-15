package uppgift;

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
    // root.color = BLACK;
  }

  private Node add(Node node, Item item) {
    if (node == null) {
      System.out.println("Adding new node with value: " + item);
      return new Node(item, RED);
    }

    int compare = item.compareTo(node.item);
    if (compare < 0) {
      System.out.println("Going left from node with value: " + node.item);
      node.left = add(node.left, item);
    } else if (compare > 0) {
      System.out.println("Going right from node with value: " + node.item);
      node.right = add(node.right, item);
    } else {
      System.out.println("Replacing node with value: " + node.item);
      node.item = item;
    }

    // Convert right-leaning red link to left-leaning red link
    if (isRed(node.right) && !isRed(node.left)) {
      System.out.println("Converting right-leaning red link to left-leaning red link at node with value: " + node.item);
      node = rotateLeft(node);
      // Check for two red children after rotation
      if (isRed(node.left) && isRed(node.right)) {
        System.out.println("Finding node with two red children and flipping colors at node with value: " + node.item);
        flipColors(node);
      }
    }

    // Find node with red left child and red left grandchild and rotate right
    if (isRed(node.left) && isRed(node.left.left)) {
      System.out
          .println("Finding node with red left child and red left grandchild and rotating right at node with value: "
              + node.item);
      node = rotateRight(node);
      // Check for two red children after rotation
      if (isRed(node.left) && isRed(node.right)) {
        System.out.println("Finding node with two red children and flipping colors at node with value: " + node.item);
        flipColors(node);
      }
    }

    node.size = size(node.left) + size(node.right) + 1;

    // Enforce that root is always black
    if (node == root) {
      System.out.println("Setting root node with value: " + node.item + " to BLACK");
      node.color = BLACK;
    }

    System.out.println("End of add method for node with value: " + node.item);
    return node;
  }

  // enforce that red links lean left by rotating left
  private Node rotateLeft(Node node) {

    // assign right node to temp
    Node temp = node.right;

    // assign left node of temp to right node of node
    node.right = temp.left;
    // assign node to left node of temp
    temp.left = node;
    // assign color of temp to color of node
    temp.color = node.color;
    // assign color of node to red
    node.color = RED;
    // assign size of temp to size of node
    temp.size = node.size;
    // assign size of node to size of left node + size of right node + 1
    node.size = size(node.left) + size(node.right) + 1;

    return temp;
  }

  private Node rotateRight(Node node) {

    // enforce that red links lean left by rotating right
    Node temp = node.left;
    // assign right node of temp to left node of node
    node.left = temp.right;
    temp.right = node;
    temp.color = node.color;
    node.color = RED;
    temp.size = node.size;
    node.size = size(node.left) + size(node.right) + 1;
    return temp;
  }

  // flip the colors of a node and its two red children nodes
  private void flipColors(Node node) {
    node.color = RED;
    node.left.color = BLACK;
    node.right.color = BLACK;
  }

  public void printInOrder(Node node) {
    if (node == null) {
      return;
    }

    printInOrder(node.left);
    System.out.println(node.item + " (" + (node.color == RED ? "RED" : "BLACK") + ")");
    printInOrder(node.right);
  }

  public boolean hasConsecutiveRedLinks(Node node) {
    if (node == null) {
      return false;
    }

    if (isRed(node) && (isRed(node.left) || isRed(node.right))) {
      return true;
    }

    return hasConsecutiveRedLinks(node.left) || hasConsecutiveRedLinks(node.right);
  }

  public static void main(String[] args) {
    RedBlackTree<Integer> rbt = new RedBlackTree<>();

    // Test: isEmpty on an empty tree
    assert rbt.isEmpty() : "Tree should be empty initially";

    // Test: size on an empty tree
    assert rbt.size() == 0 : "Size should be 0 initially";

    // Add elements to the tree in a specific order to ensure a red node
    rbt.add(10); // This should cause a rotation
    rbt.add(20); // This should be red
    rbt.add(30); // This will be the root and should be black

    // Print tree using in-order traversal
    System.out.println("In-order traversal of insert order of 10 then 20 then 30:");
    rbt.printInOrder(rbt.root);
    System.out.println();

    // Basic assertions to validate properties of Red-Black Tree
    assert !rbt.isRed(rbt.root) : "Root should always be black";
    assert rbt.isRed(rbt.root.left) : "Left child of root should be red";
    assert rbt.isRed(rbt.root.right) : "Right child of root should be red";
    System.out.println();
    System.out.println();

    RedBlackTree<Integer> rbt2 = new RedBlackTree<>();

    // Test: isEmpty on an empty tree
    assert rbt2.isEmpty() : "Tree should be empty initially";

    // Test: size on an empty tree
    assert rbt2.size() == 0 : "Size should be 0 initially";

    // Add elements to the tree in a specific order to ensure a red node
    rbt2.add(10); // This should cause a rotation
    // This should be red
    rbt2.add(30); // This will be the root and should be black
    rbt2.add(20);

    // Print tree using in-order traversal
    System.out.println("In-order traversal of insert order 10 then 30 then 20:");
    rbt2.printInOrder(rbt2.root);
    System.out.println();

    // Basic assertions to validate properties of Red-Black Tree
    assert !rbt2.isRed(rbt2.root) : "Root should always be black";
    assert rbt2.isRed(rbt2.root.left) : "Left child of root should be red";
    assert rbt2.isRed(rbt2.root.right) : "Right child of root should be red";
    System.out.println();
    System.out.println();

    RedBlackTree<Integer> rbt3 = new RedBlackTree<>();

    // Test: isEmpty on an empty tree
    assert rbt3.isEmpty() : "Tree should be empty initially";

    // Test: size on an empty tree
    assert rbt3.size() == 0 : "Size should be 0 initially";

    // Add elements to the tree in the specified order
    int[] nodes = { 50, 30, 70, 20, 40, 60, 80, 10, 35, 55, 75, 90 };
    for (int node : nodes) {
      rbt3.add(node);
    }

    // Print tree using in-order traversal
    System.out.println("In-order traversal for the larger dataset:");
    rbt3.printInOrder(rbt3.root);
    System.out.println();

    // Basic assertions to validate properties of Red-Black Tree
    assert !rbt3.isRed(rbt3.root) : "Root should always be black";

    System.out.println("All tests passed!");
  }

}
