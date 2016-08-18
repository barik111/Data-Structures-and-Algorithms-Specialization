import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SuffixArrayLong {
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

    public class Suffix implements Comparable {
        String suffix;
        int start;

        Suffix(String suffix, int start) {
            this.suffix = suffix;
            this.start = start;
        }

        @Override
        public int compareTo(Object o) {
            Suffix other = (Suffix) o;
            return suffix.compareTo(other.suffix);
        }
    }

    public int charToNum(Character c) {
        switch (c) {
            case '$':
                return 1;
            case 'A':
                return 2;
            case 'C':
                return 3;
            case 'G':
                return 4;
            case 'T':
                return 5;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int[] sortCharacters(char[] text) {
        int[] order = new int[text.length];
        int[] count = new int[6];
        for (int i = 0; i < order.length; i++) {
            count[charToNum(text[i])]++;
        }
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        for (int i = order.length - 1; i >= 0; i--) {
            Character c = text[i];
            count[charToNum(c)]--;
            order[count[charToNum(c)]] = i;
        }
        return order;
    }

    public int[] computeCharClasses(char[] text, int[] order) {
        int[] clazz = new int[text.length];
        clazz[order[0]] = 0;
        for (int i = 1; i < text.length; i++) {
            if (text[order[i]] != text[order[i - 1]]) {
                clazz[order[i]] = clazz[order[i - 1]] + 1;
            } else {
                clazz[order[i]] = clazz[order[i - 1]];
            }
        }
        return clazz;
    }

    public int[] sortDoubled(char[] text, int l, int[] order, int[] clazz) {
        int[] newOrder = new int[text.length];
        int[] count = new int[text.length];
        for (int i = 0; i < text.length; i++) {
            count[clazz[i]]++;
        }
        for (int i = 1; i < text.length; i++) {
            count[i] += count[i - 1];
        }
        for (int i = text.length - 1; i >= 0; i--) {
            int start = (order[i] - l + text.length) % text.length;
            int cl = clazz[start];
            count[cl]--;
            newOrder[count[cl]] = start;
        }
        return newOrder;
    }

    public int[] updateClasses(int[] order, int[] clazz, int l) {
        int n = order.length;
        int[] newClass = new int[n];
        newClass[order[0]] = 0;
        for (int i = 1; i < n; i++) {
            int current = order[i];
            int prev = order[i - 1];
            int mid = current + l;
            int midPrev = (prev + l) % n;
            if ((clazz[current] != clazz[prev]) || clazz[mid] != clazz[midPrev]) {
                newClass[current] = newClass[prev] + 1;
            } else {
                newClass[current] = newClass[prev];
            }
        }

        return newClass;
    }

    // Build suffix array of the string text and
    // return an int[] result of the same length as the text
    // such that the value result[i] is the index (0-based)
    // in text where the i-th lexicographically smallest
    // suffix of text starts.
    public int[] computeSuffixArray(String text) {
        char[] textArray = text.toCharArray();
        //Arrays.sort(textArray);
        int[] order = sortCharacters(textArray);
        int[] clazz = computeCharClasses(textArray, order);
        int l = 1;
        while (l < text.length()) {
            order = sortDoubled(textArray, l, order, clazz);
            clazz = updateClasses(order, clazz, l);
            l *= 2;
        }
        return order;
    }


    static public void main(String[] args) throws IOException {
        new SuffixArrayLong().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] suffix_array = computeSuffixArray(text);
        print(suffix_array);
    }
}
