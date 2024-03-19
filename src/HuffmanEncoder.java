import java.util.List;
import java.util.PriorityQueue;
import java.util.ArrayList;

public class HuffmanEncoder {
    public static class Node {
        public HuffmanCharacter hc;
        public Node left, right;
    }

    /**
     * Builds the Huffman encoding tree of a given set of characters and their frequencies.
     * @param characters the list of characters & frequencies for which the tree should be built.
     * @return the root node of the tree.
     */
    public static Node encode(List<HuffmanCharacter> characters) {
        // create a priority queue that keeps the Huffman characters ordered by their frequency in ascending order
        PriorityQueue<Node> pq = new PriorityQueue<>((node1, node2) -> Float.compare(node1.hc.frequency(), node2.hc.frequency()));
        for (int i=0; i<characters.size(); i++) {
            Node n = new Node();
            n.hc = characters.get(i);
            pq.add(n);
        }

        // iterate over the ordered characters to build the tree
        for (int i=1; i<characters.size(); i++) {
            Node n = new Node();
            n.right = pq.poll();
            n.left  = pq.poll();
            n.hc = new HuffmanCharacter(null, n.right.hc.frequency()+n.left.hc.frequency());
            pq.add(n);
        }

        return pq.poll();
    }

    /**
     * Prints a string representation of the Huffman encoding tree.
     * @param root the root of the Huffman encoding tree to be printed.
     */
    public static void printTree(Node root) {
        printTree(root, 0, false);
    }

    /**
     * Recursive method that effectively prints the Huffman encoding tree.
     * @param n the node where the tree starts.
     * @param depth the depth of the passed node.
     * @param left whether or not 'n' is a left node.
     */
    private static void printTree(Node n, int depth, boolean left) {
        if (n == null) return;
        
        for(int i=0; i<depth; i++) // indentation
            System.out.print("       ");
        if (depth > 0) // print binary encoding
            System.out.print(String.format("|__ (%c)", left==true? '0' : '1'));
        else if (n.left != null || n.right != null) // print symbol of root if the tree isn't made up of a single element (if it is, print only the element)
            System.out.print("[    ROOT    ]");
        if (n.hc.character() != null) { // print node character
            System.out.print(" " + n.hc.character());
        }
        System.out.println();

        printTree(n.left, depth+1, true); // print left tree
        printTree(n.right, depth+1, false); // print right tree
    }

    public static void main(String[] args) {
        List<HuffmanCharacter> c = new ArrayList<>();
        c.add(new HuffmanCharacter('a', 45));
        c.add(new HuffmanCharacter('b', 13));
        c.add(new HuffmanCharacter('c', 12));
        c.add(new HuffmanCharacter('d', 16));
        c.add(new HuffmanCharacter('e', 9));
        c.add(new HuffmanCharacter('f', 5));
        Node root = HuffmanEncoder.encode(c);
        HuffmanEncoder.printTree(root);
    }
}
