package uppgift;

import java.util.Iterator;
import java.util.Stack;

public class InOrderIterator<Item extends Comparable<Item>> implements Iterator<Item> {

  private Stack<TreeTraversal.Node<Item>> stack = new Stack<>();
  private TreeTraversal<Item> tree;


    public InOrderIterator(TreeTraversal<Item> tree) {
      this.tree = tree;
      pushLeft(tree.getRoot());
  }

    private void pushLeft(TreeTraversal.Node<Item> node) {
      while (node != null) {
          stack.push(node);
          node = tree.getLeftChild(node);
      }
  }

  public boolean hasNext() {
    return !stack.isEmpty();
  }

  public Item next() {
      TreeTraversal.Node<Item> current = stack.pop();
      pushLeft(tree.getRightChild(current));
      return current.getItem();
  }

  @Override
    public String toString() {
      StringBuilder output = new StringBuilder();
      while (this.hasNext()) {
        output.append(this.next());
        if (this.hasNext()) {
          output.append(", ");
        }
      }
      return output.toString();
    }
}
