package uppgift;

public class RedBlackTree<Item extends Comparable<Item>> {
  private static final boolean RED = true;
  private static final boolean BLACK = false;

  private Node root;

  private class Node {
      private Item item;
      private Node left, right;
      private boolean color;
      private int size;

      public Node(Item item, boolean color) {
          this.item = item;
          this.color = color;
          this.size = 1;
      }
  }
  
}
