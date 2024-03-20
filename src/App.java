import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class App {
    public static void main(String[] args) {
        System.out.println("\nHUFFMAN CODES SAMPLE APPLICATION:\n");
        // create frequency map
        Map<Character, Float> frequencyMap = new HashMap<>();
        frequencyMap.put(' ', 0.18f);
        frequencyMap.put('E', 0.11f);
        frequencyMap.put('T', 0.09f);
        frequencyMap.put('A', 0.08f);
        frequencyMap.put('O', 0.07f);
        frequencyMap.put('I', 0.07f);
        frequencyMap.put('N', 0.06f);
        frequencyMap.put('S', 0.06f);
        frequencyMap.put('H', 0.06f);
        frequencyMap.put('R', 0.06f);
        frequencyMap.put('D', 0.04f);
        frequencyMap.put('L', 0.04f);
        frequencyMap.put('U', 0.03f);
        frequencyMap.put('C', 0.03f);
        frequencyMap.put('M', 0.03f);
        frequencyMap.put('W', 0.03f);
        frequencyMap.put('F', 0.02f);
        frequencyMap.put('G', 0.02f);
        frequencyMap.put('Y', 0.02f);
        frequencyMap.put('P', 0.02f);
        frequencyMap.put('B', 0.015f);
        frequencyMap.put('V', 0.01f);
        frequencyMap.put('K', 0.01f);
        frequencyMap.put('X', 0.005f);
        frequencyMap.put('Q', 0.002f);
        frequencyMap.put('J', 0.002f);
        frequencyMap.put('Z', 0.001f);
        frequencyMap.put(',', 0.015f);
        frequencyMap.put('.', 0.015f);
        frequencyMap.put('!', 0.005f);
        frequencyMap.put('?', 0.005f);
        frequencyMap.put(':', 0.002f);
        frequencyMap.put(';', 0.002f);
        frequencyMap.put('\'', 0.005f);
        frequencyMap.put('"', 0.002f);

        // create Huffman characters list and generate the Huffman encoding tree
        List<HuffmanEncoder.Char> characters = new ArrayList<>(frequencyMap.size());
        for (Map.Entry<Character, Float> entry : frequencyMap.entrySet())
            characters.add(new HuffmanEncoder.Char(entry.getKey(), entry.getValue()));
        HuffmanEncoder.Node root = HuffmanEncoder.generateTree(characters);

        // compress and decompress a sample text
        String text = "The quick brown fox jumps over the lazy dog.".toUpperCase();
        String encodedText = HuffmanEncoder.encodeText(root, text);
        System.out.printf("- Initial text (%d bits, %d bytes): %s\n", text.length()*8, text.length(), text);
        System.out.printf("- Huffman compression (%d bits, %d bytes): %s\n", encodedText.length(), (int) Math.ceil(encodedText.length()/8.0), encodedText);
        System.out.printf("- Huffman decompression: %s\n", HuffmanEncoder.decodeText(root, encodedText));

        // calculate ABL
        double accumulator = 0.0;
        for (HuffmanEncoder.Char c : characters)
            accumulator += c.frequency() * HuffmanEncoder.getEncodingBitLength(root, c.character());
        System.out.printf("- ABL = %.2f\n", accumulator);
    } 
}
