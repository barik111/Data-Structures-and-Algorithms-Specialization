import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SuffixArrayMatching {
    class fastscanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        fastscanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextint() throws IOException {
            return Integer.parseInt(next());
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


    public int[] computeSuffixArray(String text) {
        char[] textArray = text.toCharArray();
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

    public List<Integer> findOccurrences(String pattern, String text, int[] suffixArray) {
        List<Integer> result = new ArrayList<>();
        int min = 0;
        int max = text.length();
        int length = text.length();
        int patternLength = pattern.length();
        while (min < max) {
            int mid = (min + max) / 2;
            String suffix = text.substring(suffixArray[mid], Math.min(suffixArray[mid] + patternLength, length));
            if (pattern.compareTo(suffix) > 0) {
                min = mid + 1;
            } else {
                max = mid;
            }
        }
        int start = min;
        max = text.length();
        while (min < max) {
            int mid = (min + max) / 2;
            String suffix = text.substring(suffixArray[mid], Math.min(suffixArray[mid] + patternLength, length));
            if (pattern.compareTo(suffix) < 0) {
                max = mid;
            } else {
                min = mid + 1;

            }
        }
        int end = max;
        if (start <= end) {
            for (int i = start; i < end; i++) {
                result.add(suffixArray[i]);
            }
        }
        return result;
    }

    static public void main(String[] args) throws IOException {
        new SuffixArrayMatching().run();
    }

    public void print(boolean[] x) {
        for (int i = 0; i < x.length; ++i) {
            if (x[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }

    public void run() throws IOException {
        fastscanner scanner = new fastscanner();
        String text = scanner.next() + "$";
        int[] suffixArray = computeSuffixArray(text);
        int patternCount = scanner.nextint();
        boolean[] occurs = new boolean[text.length()];
        for (int patternIndex = 0; patternIndex < patternCount; ++patternIndex) {
            String pattern = scanner.next();
            List<Integer> occurrences = findOccurrences(pattern, text, suffixArray);
            for (int x : occurrences) {
                occurs[x] = true;
            }
        }
        print(occurs);
    }
}
