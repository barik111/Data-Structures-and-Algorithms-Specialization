import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

public class Diet {

    BufferedReader br;
    PrintWriter out;
    StringTokenizer st;
    boolean eof;

    int solveDietProblem(int n, int m, double A[][], double[] b, double[] c, double[] x) {
        Double max = Double.NaN;
        double[] mainSolution = new double[m];
        double[][] newA;
        double[] newB;
        List<int[]> combinations = combinations(IntStream.range(0, n + m + 1).toArray(), m, 0, new int[m], new ArrayList<>());
        for (int[] currentCombination : combinations) {
            newA = new double[m][m];
            newB = new double[m];
            cutRow(A, b, newA, newB, currentCombination);
            double[] solution;
            try {
                solution = gaussianElimination(newA, newB);
            } catch (IllegalArgumentException e) {
                continue;
            }
            int[] currentComplement = complementOfIndexes(IntStream.range(0, n + m + 1).toArray(), currentCombination);
            newA = new double[currentComplement.length][m];
            newB = new double[currentComplement.length];
            cutRow(A, b, newA, newB, currentComplement);
            if (checkSolution(solution, newA, newB)) {
                double solutionValue = Arrays.stream(multiplyArrays(solution, c)).sum();
                if (max.isNaN() || solutionValue > max) {
                    max = solutionValue;
                    mainSolution = Arrays.copyOf(solution, solution.length);
                }
            }

        }
        if (max.isNaN()) return -1;
        if (max > 999_999_990D) return 1;
        System.arraycopy(mainSolution, 0, x, 0, x.length);
        return 0;
    }

    public static double[] multiplyArrays(double[] a, double b[]) {
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] * b[i];
        }
        return result;
    }


    public static boolean checkSolution(double[] solution, double[][] A, double[] b) {
        for (int i = 0; i < A.length; i++) {
            double[] inequality = A[i];
            if (Arrays.stream(multiplyArrays(solution, inequality)).sum() > b[i])
                return false;
        }
        return true;
    }

    public static int[] complementOfIndexes(int[] all, int[] indexes) {
        return Arrays.stream(all)
                .filter(a -> Arrays.stream(indexes)
                        .noneMatch(i -> i == a)).toArray();
    }

    public static void cutRow(double[][] A, double[] b, double[][] newA, double[] newB, int[] indexes) {
        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i] < A.length) {
                newA[i] = Arrays.copyOf(A[indexes[i]], A[i].length);
                newB[i] = b[indexes[i]];
            } else if (indexes[i] == A.length + A[0].length) {
                Arrays.fill(newA[i], 1);
                newB[i] = 1_000_000_000D;
            } else {
                newA[i][indexes[i] - A.length] = -1;
            }
        }
    }

    public static List<int[]> combinations(int[] arr, int len, int startPosition, int[] combination, List<int[]> result) {
        if (len == 0) {
            result.add(combination.clone());
            return result;
        }
        for (int i = startPosition; i <= arr.length - len; i++) {
            combination[combination.length - len] = arr[i];
            combinations(arr, len - 1, i + 1, combination, result);
        }
        return result;
    }

    public static double[] gaussianElimination(double A[][], double[] b) {
        int N = A[0].length;
        for (int p = 0; p < N; p++) {
            int max = p;
            for (int i = p + 1; i < N; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            if (Math.abs(A[max][p]) <= 0.000001) {
                throw new IllegalArgumentException();
            }
            double[] temp = A[p];
            A[p] = A[max];
            A[max] = temp;
            double t = b[p];
            b[p] = b[max];
            b[max] = t;

            for (int i = p + 1; i < N; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < N; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        double[] solution = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++) {
                sum += A[i][j] * solution[j];
            }
            solution[i] = (b[i] - sum) / A[i][i];
        }

        return solution;
    }

    void solve() throws IOException {
        int n = nextInt();
        int m = nextInt();
        double[][] A = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                A[i][j] = nextInt();
            }
        }
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = nextInt();
        }
        double[] c = new double[m];
        for (int i = 0; i < m; i++) {
            c[i] = nextInt();
        }
        double[] ansx = new double[m];
        int anst = solveDietProblem(n, m, A, b, c, ansx);
        if (anst == -1) {
            out.printf("No solution\n");
            return;
        }
        if (anst == 0) {
            out.printf("Bounded solution\n");
            for (int i = 0; i < m; i++) {
                out.printf(Locale.US, "%.18f%c", ansx[i], i + 1 == m ? '\n' : ' ');
            }
            return;
        }
        if (anst == 1) {
            out.printf("Infinity\n");
            return;
        }
    }

    Diet() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out);
        solve();
        out.close();
    }

    public static void main(String[] args) throws IOException {
        new Diet();
    }

    String nextToken() {
        while (st == null || !st.hasMoreTokens()) {
            try {
                st = new StringTokenizer(br.readLine());
            } catch (Exception e) {
                eof = true;
                return null;
            }
        }
        return st.nextToken();
    }

    int nextInt() throws IOException {
        return Integer.parseInt(nextToken());
    }
}
