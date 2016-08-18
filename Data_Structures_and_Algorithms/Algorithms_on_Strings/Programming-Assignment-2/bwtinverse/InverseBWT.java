import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

public class InverseBWT {
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

    String inverseBWT(String bwt) {
        StringBuilder result = new StringBuilder();
        List<String> matrix = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < bwt.length(); i++) {
            matrix.add("" + bwt.charAt(i));
            indexes.add(i);
        }
        Collections.sort(indexes, (o1, o2) -> matrix.get(o1).compareTo(matrix.get(o2)));
        Integer current = indexes.get(0);
        for (int i = 0; i < bwt.length() - 1; i++) {
            int index = indexes.indexOf(current);
            String next = String.valueOf(bwt.charAt(index));
            result.append(next);
            current = index;
        }

        return result.reverse().toString() + "$";
    }

    public static String generateString() {
        Character[] c = {'A', 'C', 'G', 'T'};
        String result = "";
        for(int i = 0; i < 1_000_000 - 1; i++){
            result += c[ThreadLocalRandom.current().nextInt(0, 4)];
            if(i == 99701)  result += "$";
        }
        return result;
    }

    static public void main(String[] args) throws IOException {
        // new InverseBWT().run();
        String text = generateString();
        System.out.println(System.currentTimeMillis());
        new InverseBWT().inverseBWT(text);
        System.out.println(System.currentTimeMillis());
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String bwt = scanner.next();
        System.out.println(inverseBWT(bwt));
    }


}


