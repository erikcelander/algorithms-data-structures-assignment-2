package uppgift;

import java.util.Iterator;
import java.util.Stack;

public class PreOrderIterator<Item extends Comparable<Item>> implements Iterator<Item> {

    private Stack<TreeTraversal.Node<Item>> stack = new Stack<>();
    private TreeTraversal<Item> tree;

    public PreOrderIterator(TreeTraversal<Item> tree) {
        this.tree = tree;
        // push root to stack
        if (tree.getRoot() != null)
            stack.push(tree.getRoot());
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public Item next() {
        TreeTraversal.Node<Item> current = stack.pop();
        if (tree.getRightChild(current) != null)
            stack.push(tree.getRightChild(current));
        if (tree.getLeftChild(current) != null)
            stack.push(tree.getLeftChild(current));
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
