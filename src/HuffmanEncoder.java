import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * Utility class for building Huffman codes.
 * @author Ricardo B. Süffert.
 * @version 2.1.
 */
public class HuffmanEncoder {

    /**
     * Internal class that represents a node of the Huffman encoding tree.
     */
    public static record Node(Char hc, Node left, Node right) {}

    /**
     * Internal class that represents a character with its frequency.
     */
    public static record Char(Character character, float frequency) {}

    /**
     * Builds the Huffman encoding tree of a given set of characters and their frequencies.
     * @param characters the list of characters and frequencies for which the tree should be built.
     * @return the root node of the tree.
     */
    public static Node generateTree(List<Char> characters) {
        // create a priority queue that keeps the Huffman characters ordered by their frequency in ascending order
        PriorityQueue<Node> pq = new PriorityQueue<>((node1, node2) -> Float.compare(node1.hc.frequency(), node2.hc.frequency()));
        for (Char c : characters) {
            Node n = new Node(c, null, null);
            pq.add(n);
        }

        // iterate over the ordered characters to build the tree
        for (int i=1; i<characters.size(); i++) {
            Node right = pq.poll(), left = pq.poll();
            Char hc = new Char(null, right.hc.frequency()+left.hc.frequency());
            Node n = new Node(hc, left, right);
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
     * @param left whether or not {@code n} is a left node.
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
     * Calculates the maximum number of bits needed to represent this encoding. By definition, this is the same as the depth of the Huffman encoding tree.
     * @param root the root of the Huffman encoding tree.
     * @return the maximum number of bits needed to represent this encoding.
     */
    public static int getMaxBitLength(Node root) {
        return getDepth(root, 0);
    }

    /**
     * Recursively calculates the depth of a binary tree.
     * @param n the initial node.
     * @param depth the depth of {@code n}.
     * @return the depth of the subtree of {@code n}.
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
     * @return the encoding of {@code c}.
     * @throws NoSuchElementException if {@code c} is not in the Huffman encoding tree.
     */
    public static String encodeChar(Node root, char c) throws NoSuchElementException {
        String encoding = encodeChar(root, c, "");
        if (encoding == "") throw new NoSuchElementException(String.format("Character '%c' is not present in the given tree.", c));
        return encoding;
    }

    /**
     * Recursively finds out the encoding of a given character and a given Huffman encoding tree.
     * @param n the node os the subtree to be investigated.
     * @param c the character whose encoding is to be found out.
     * @param enc the encoding of the character up to node {@code n}.
     * @return the encoding of {@code c}.
     */
    private static String encodeChar(Node n, char c, String enc) {
        if (n.hc.character() != null && n.hc.character() == c) return enc; // investigate current node

        if (n.left != null) { // investigate left node
            String e = encodeChar(n.left, c, enc+"0");
            if (!e.equals("")) return e; // if c has been found in the subtree, return it
        }
        if (n.right != null) { // investigate right node
            String e = encodeChar(n.right, c, enc+"1");
            if (!e.equals("")) return e; // if c has been found in the subtree, return it
        }

        return ""; // signal c has not been found in current subtree
    }

    /**
     * Decodes a given char encoding according to a given Huffman encoding tree.
     * @param root the root node of the Huffman encoding tree.
     * @param encoding the binary encoding of the character.
     * @return the character associated with the given encoding in the given tree.
     * @throws IllegalArgumentException if the given encoding is not binary.
     * @throws NoSuchElementException if the given encoding is not mapped to any character in the given tree.
     */
    public static char decodeChar(Node root, String encoding) throws IllegalArgumentException, NoSuchElementException {
        Node currentNode = root;
        for (int i=0; i<encoding.length(); i++) {
            char bit = encoding.charAt(i);

            boolean foundMapping = false;
            if (bit == '0') { // if bit is '0', move to the left node
                if (currentNode.left != null) {
                    foundMapping = true;
                    currentNode = currentNode.left;
                }
            }
            else if (bit == '1') { // if bit is '1', mode to the right node
                if (currentNode.right != null) {
                    foundMapping = true;
                    currentNode = currentNode.right;
                }
            }
            else throw new IllegalArgumentException("The given encoding is not binary");

            if (!foundMapping) throw new NoSuchElementException("The given encoding does not have a representation in the given tree");
        }

        if (currentNode.hc.character() == null) throw new NoSuchElementException("The given encoding does not have a representation in the given tree");
        return currentNode.hc.character(); // return the character of the final node
    }

    /**
     * Calculates how many bits the encoded representation of a given character has.
     * @param root the root of the Huffman encoding tree.
     * @param c the character whose quantity of encoding bits is to be found out.
     * @return the number of bits of the representation of {@code c}.
     * @throws NoSuchElementException if {@code c} is not in the given Huffman encoding tree.
     */
    public static int getEncodingBitLength(Node root, char c) throws NoSuchElementException {
        return encodeChar(root, c).length();
    }

    /**
     * Encodes a plain text given a Huffman encoding tree.
     * @param root the root node of the Huffman encoding tree.
     * @param text the plain text to be encoded.
     * @return the string encoding of {@code text}.
     * @throws NoSuchElementException if a character in {@code text} is not mapped in the given Huffman encoding tree.
     */
    public static String encodeText(Node root, String text) throws NoSuchElementException {
        StringBuilder sb = new StringBuilder();

        // iterate over the characters in the text
        for (int i=0; i<text.length(); i++) {
            char c = text.charAt(i);
            String enc = encodeChar(root, c); // encode them
            sb.append(enc); // add to the result
        }

        return sb.toString(); // return encoding
    }

    /**
     * Decodes an encoded text given its Huffman encoding tree.
     * @param root the root node of the Huffman encoding tree.
     * @param encodedText the binary string representing the encoded text.
     * @return the string decoding of {@code encodedText}.
     * @throws IllegalArgumentException if the given encoding is not binary.
     */
    public static String decodeText(Node root, String encodedText) throws IllegalArgumentException {
        StringBuilder text = new StringBuilder();

        // iterate over the bits of encodedText
        StringBuilder encodedChar = new StringBuilder();
        for (int i=0; i<encodedText.length(); i++) {
            encodedChar.append(encodedText.charAt(i));

            char c;
            try { // when a possible decoding is found, append it to the resulting string
                c = decodeChar(root, encodedChar.toString());
                encodedChar.setLength(0); // clear/reset
                text.append(c);
            }
            catch (NoSuchElementException e) { // otherwise keep iterating until a possible decoding is found
                continue;
            }
        }

        return text.toString();
    }
    
    /**
     * Calculates the average bit length (ABL) of the given Huffman encoding tree.
     * @param root the root of the Huffman encoding tree whose ABL is to be calculated.
     * @return the ABL of the given Huffman encoding tree.
     */
    public static double getABL(Node root) {
        if (root == null) return 0.0;
        return getABL(root, root);
    }

    /**
     * Recursively calculates the average bit length (ABL) of the given Huffman encoding tree.
     * @param root the root of the Huffman encoding tree whose ABL is to be calculated.
     * @param n the current subtree being investigated.
     * @return the ABL of the given Huffman encoding tree.
     */
    private static double getABL(Node root, Node n) {
        double abl = 0.0;

        if (n.hc.character != null) // investigate if current node is a character (leaf) node
            return n.hc.frequency * getEncodingBitLength(root, n.hc.character); // return its ABL
        else { // investigate child nodes
            if (n.left != null) abl += getABL(root, n.left); // investigate left node
            if (n.right != null) abl += getABL(root, n.right); // investigate right node
        }

        return abl;
    }

    /**
     * Calculates the compression factor for an ASCII text using a given Huffman encoding tree.
     * @param root the root of the Huffman encoding tree.
     * @param originalAsciiText the text whose compression factor is to be calculated.
     * @return how many times the compression of {@code originalAsciiText} is shorter in size than it.
     */
    public static double getCompressionFactor(Node root, String originalAsciiText) {
        String encoding = encodeText(root, originalAsciiText);
        return (originalAsciiText.length()*8) / ((double) encoding.length());
    }
}