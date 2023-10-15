package uppgift;

import java.util.Iterator;
import java.util.Stack;

public class PostOrderIterator<Item extends Comparable<Item>> implements Iterator<Item> {

    private Stack<TreeTraversal.Node<Item>> stack = new Stack<>();
    private Stack<TreeTraversal.Node<Item>> output = new Stack<>();

    public PostOrderIterator(TreeTraversal<Item> tree) {
        if (tree.getRoot() != null) {
            stack.push(tree.getRoot());
            while (!stack.isEmpty()) {
                TreeTraversal.Node<Item> current = stack.pop();
                output.push(current);

                // push left child first and then right child to stack
                if (tree.getLeftChild(current) != null) {
                    stack.push(tree.getLeftChild(current));
                }

                if (tree.getRightChild(current) != null) {
                    stack.push(tree.getRightChild(current));
                }
            }
        }
    }

    @Override
    public boolean hasNext() {
        return !output.isEmpty();
    }

    @Override
    public Item next() {
        return output.pop().getItem();
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
