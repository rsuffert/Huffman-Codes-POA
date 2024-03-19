import java.util.List;
import java.util.ArrayList;

public class Test {
        public static void main(String[] args) {
        List<HuffmanEncoder.Char> c = new ArrayList<>();
        c.add(new HuffmanEncoder.Char('a', 45));
        c.add(new HuffmanEncoder.Char('b', 13));
        c.add(new HuffmanEncoder.Char('c', 12));
        c.add(new HuffmanEncoder.Char('d', 16));
        c.add(new HuffmanEncoder.Char('e', 9));
        c.add(new HuffmanEncoder.Char('f', 5));
        HuffmanEncoder.Node root = HuffmanEncoder.generateTree(c);
        
        HuffmanEncoder.printTree(root);
        System.out.println("Max. bit length: " + HuffmanEncoder.getMaxBitLength(root));
        String encodingE = HuffmanEncoder.getEncoding(root, 'e');
        System.out.println("Encoding(e): " + encodingE);
        System.out.println("|Encoding(e)|: " + HuffmanEncoder.getBitLength(root, 'e'));
        System.out.println("Decode(Encoding(e)): " + HuffmanEncoder.decodeChar(root, encodingE));
        String encoding = HuffmanEncoder.encode(root, "abef");
        System.out.println("Encode(abef): "+ encoding);
        System.out.println("Decode(Encode(abef)): " + HuffmanEncoder.decode(root, encoding));
    }
}
