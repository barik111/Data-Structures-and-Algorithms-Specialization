import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BurrowsWheelerTransform {
    class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    String BWT(String text) {
        StringBuilder result = new StringBuilder();
        StringBuilder textBuilder = new StringBuilder(text);
        TreeSet<String> tree = new TreeSet<>();
        int last = text.length() - 1;
        for (int i = 0; i < text.length(); i++) {
            tree.add(textBuilder.toString());
            Character first = textBuilder.charAt(0);
            textBuilder.deleteCharAt(0);
            textBuilder.append(first);
        }
        while (!tree.isEmpty()) {
            result.append(tree.pollFirst().charAt(last));
        }

        return result.toString();
    }

    static public void main(String[] args) throws IOException {
        new BurrowsWheelerTransform().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        System.out.println(BWT(text));
    }
}
