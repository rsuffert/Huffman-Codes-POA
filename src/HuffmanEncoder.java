import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * Utility class for building Huffman codes.
 */
public class HuffmanEncoder {

    /**
     * Internal class that represents a node of the Huffman encoding tree.
     */
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
        if (n.hc.character() != null) // print node character
            System.out.print(" " + n.hc.character());
        System.out.println();

        printTree(n.left, depth+1, true); // print left tree
        printTree(n.right, depth+1, false); // print right tree
    }

    /**
     * Calculates the maximum number of bits needed to represent this encoding.
     * @param root the root of the Huffman encoding tree.
     * @return the maximum number of bits needed to represent this encoding.
     */
    public static int getMaxBitLength(Node root) {
        return getDepth(root, 0);
    }

    /**
     * Recursively calculates the depth of a binary tree.
     * @param n the initial node.
     * @param depth the depth of 'n'.
     * @return the depth of the subtree of 'n'.
     */
    private static int getDepth(Node n, int depth) {
        if (n == null) return depth;

        int leftDepth = 0, rightDepth = 0;
        if (n.left != null)  leftDepth  = getDepth(n.left, depth+1);
        if (n.right != null) rightDepth = getDepth(n.right, depth+1);

        // return maximum depth among current depth, left depth and right depth
        int maxDepth = depth;
        if (leftDepth > maxDepth) maxDepth = leftDepth;
        if (rightDepth > maxDepth) maxDepth = rightDepth;
        return maxDepth;
    }

    /**
     * Returns the Huffman encoding for a given character and a given Huffman tree.
     * @param root the root of the Huffman encoding tree.
     * @param c the character whose encoding is to be found out.
     * @return the encoding of 'c'.
     * @throws NoSuchElementException if 'c' is not in the Huffman encoding tree.
     */
    public static String getEncoding(Node root, char c) throws NoSuchElementException {
        String encoding = getEncoding(root, c, "");
        if (encoding == "") throw new NoSuchElementException(String.format("Character '%c' is not present in the given tree.", c));
        return encoding;
    }

    /**
     * Recursively finds out the encoding of a given character and a given Huffman encoding tree.
     * @param n the node os the subtree to be investigated.
     * @param c the character whose encoding is to be found out.
     * @param enc the encoding of the character up to node 'n'.
     * @return the encoding of 'c'.
     */
    private static String getEncoding(Node n, char c, String enc) {
        if (n.hc.character() != null && n.hc.character() == c) return enc; // investigate current node

        if (n.left != null) { // investigate left node
            String e = getEncoding(n.left, c, enc+"0");
            if (!e.equals("")) return e; // if c has been found in the subtree, return it
        }
        if (n.right != null) { // investigate right node
            String e = getEncoding(n.right, c, enc+"1");
            if (!e.equals("")) return e; // if c has been found in the subtree, return it
        }

        return ""; // signal c has not been found in current subtree
    }

    /**
     * Calculates how many bits the encoded representation of a given character has.
     * @param root the root of the Huffman encoding tree.
     * @param c the character whose quantity of encoding bits is to be found out.
     * @return the number of bits of the representation of 'c'.
     * @throws NoSuchElementException if 'c' is not in the given Huffman encoding tree.
     */
    public static int getBitLength(Node root, char c) throws NoSuchElementException {
        return getEncoding(root, c).length();
    }
}