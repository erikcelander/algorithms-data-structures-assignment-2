package uppgift;

public class RedBlackTree<Item extends Comparable<Item>> implements TreeTraversal<Item> {
  public class Node implements TreeTraversal.Node<Item> {
    Item item;
    Node left, right;
    boolean color = BLACK;

    Node(Item item, Node left, Node right, boolean color) {
      this.item = item;
      this.left = left;
      this.right = right;
      this.color = color;
    }

    @Override
    public Item getItem() {
      return item;
    }
  }

  public static final boolean RED = true;
  public static final boolean BLACK = false;
  private Node root;

  public RedBlackTree() {
    root = null;
  }

  private Node rotateLeft(Node h) {
    Node x = h.right;
    h.right = x.left;
    x.left = h;
    x.color = h.color;
    h.color = RED;
    return x;
  }

  private Node rotateRight(Node h) {
    Node x = h.left;
    h.left = x.right;
    x.right = h;
    x.color = h.color;
    h.color = RED;
    return x;
  }

  private void flipColors(Node h) {
    h.color = !h.color;
    if (h.left != null)
      h.left.color = !h.left.color;
    if (h.right != null)
      h.right.color = !h.right.color;
  }

  private boolean isRed(Node node) {
    if (node == null)
      return false;
    return node.color == RED;
  }

  @Override
  public Node getRoot() {
    return root;
  }

  @Override
  public Node getLeftChild(TreeTraversal.Node<Item> node) {
    return ((Node) node).left;
  }

  @Override
  public Node getRightChild(TreeTraversal.Node<Item> node) {
    return ((Node) node).right;
  }

  public void add(Item item) {
    root = add(root, item);
    root.color = BLACK;
  }

  private Node add(Node h, Item item) {
    if (h == null)
      return new Node(item, null, null, RED);

    int cmp = item.compareTo(h.item);
    if (cmp < 0)
      h.left = add(h.left, item);
    else if (cmp > 0)
      h.right = add(h.right, item);

    if (isRed(h.right) && !isRed(h.left))
      h = rotateLeft(h);
    if (isRed(h.left) && isRed(h.left.left))
      h = rotateRight(h);
    if (isRed(h.left) && isRed(h.right))
      flipColors(h);

    return h;
  }

  public boolean contains(Item item) {
    Node x = root;
    while (x != null) {
      int cmp = item.compareTo(x.item);
      if (cmp < 0)
        x = x.left;
      else if (cmp > 0)
        x = x.right;
      else
        return true;
    }
    return false;
  }

  public int height() {
    return height(root);
  }

  private int height(Node node) {
    if (node == null) {
      return -1;
    }

    return 1 + Math.max(height(node.left), height(node.right));
  }

  public void remove(Item item) {
    if (!contains(item))
      return;

    if (!isRed(root.left) && !isRed(root.right))
      root.color = RED;

    root = remove(root, item);
    if (root != null)
      root.color = BLACK;
  }

  private Node remove(Node h, Item item) {
    if (item.compareTo(h.item) < 0) {
      if (!isRed(h.left) && !isRed(h.left.left))
        h = moveRedLeft(h);
      h.left = remove(h.left, item);
    } else {
      if (isRed(h.left))
        h = rotateRight(h);
      if (item.compareTo(h.item) == 0 && (h.right == null))
        return null;
      if (!isRed(h.right) && !isRed(h.right.left))
        h = moveRedRight(h);
      if (item.compareTo(h.item) == 0) {
        Node x = min(h.right);
        h.item = x.item;
        h.right = deleteMin(h.right);
      } else
        h.right = remove(h.right, item);
    }
    return balance(h);
  }

  private Node moveRedLeft(Node h) {
    flipColors(h);
    if (isRed(h.right.left)) {
      h.right = rotateRight(h.right);
      h = rotateLeft(h);
      flipColors(h);
    }
    return h;
  }

  private Node moveRedRight(Node h) {
    flipColors(h);
    if (isRed(h.left.left)) {
      h = rotateRight(h);
      flipColors(h);
    }
    return h;
  }

  private Node balance(Node h) {
    if (isRed(h.right))
      h = rotateLeft(h);
    if (isRed(h.left) && isRed(h.left.left))
      h = rotateRight(h);
    if (isRed(h.left) && isRed(h.right))
      flipColors(h);

    return h;
  }

  private Node deleteMin(Node h) {
    if (h.left == null)
      return null;

    if (!isRed(h.left) && !isRed(h.left.left))
      h = moveRedLeft(h);

    h.left = deleteMin(h.left);

    return balance(h);
  }

  private Node min(Node h) {
    if (h.left == null)
      return h;
    else
      return min(h.left);
  }

  public InOrderIterator<Item> inOrderIterator() {
    return new InOrderIterator<>(this);
  }

  public PreOrderIterator<Item> preOrderIterator() {
    return new PreOrderIterator<>(this);
  }

  public PostOrderIterator<Item> postOrderIterator() {
    return new PostOrderIterator<>(this);
  }

}