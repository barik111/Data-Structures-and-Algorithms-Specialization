import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

class Equation {
    Equation(double a[][], double b[]) {
        this.a = a;
        this.b = b;
    }

    double a[][];
    double b[];
}

class Position {
    Position(int raw, int column) {
        this.column = column;
        this.raw = raw;
    }

    int column;
    int raw;
}

class EnergyValues {
    static Equation ReadEquation() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();

        double a[][] = new double[size][size];
        double b[] = new double[size];
        for (int raw = 0; raw < size; ++raw) {
            for (int column = 0; column < size; ++column)
                a[raw][column] = scanner.nextInt();
            b[raw] = scanner.nextInt();
        }
        return new Equation(a, b);
    }

    static Position SelectPivotElement(double a[][], int step) {
        // This algorithm selects the first free element.
        // You'll need to improve it to pass the problem.
        int max = step;
        for (int i = step + 1; i < a.length; i++) {
            if (Math.abs(a[i][step]) > Math.abs(a[max][step]))
                max = i;
        }
        if (Math.abs(a[max][step]) < 0.00001) throw new IllegalArgumentException();

        return new Position(max, step);
    }

    static void SwapLines(double a[][], double b[], boolean used_raws[], Position pivot_element) {
        int size = a.length;

        for (int column = 0; column < size; ++column) {
            double tmpa = a[pivot_element.column][column];
            a[pivot_element.column][column] = a[pivot_element.raw][column];
            a[pivot_element.raw][column] = tmpa;
        }

        double tmpb = b[pivot_element.column];
        b[pivot_element.column] = b[pivot_element.raw];
        b[pivot_element.raw] = tmpb;

        boolean tmpu = used_raws[pivot_element.column];
        used_raws[pivot_element.column] = used_raws[pivot_element.raw];
        used_raws[pivot_element.raw] = tmpu;

        pivot_element.raw = pivot_element.column;
    }

    static void ProcessPivotElement(double a[][], double b[], Position pivot_element) {
        for (int i = pivot_element.column + 1; i < b.length; i++) {
            double alpha = a[i][pivot_element.column] / a[pivot_element.raw][pivot_element.column];
            b[i] -= b[pivot_element.raw] * alpha;
            for (int j = pivot_element.column; j < a[0].length; j++) {
                a[i][j] -= a[pivot_element.raw][j] * alpha;
            }
        }
    }

    static void MarkPivotElementUsed(Position pivot_element, boolean used_raws[], boolean used_columns[]) {
        used_raws[pivot_element.raw] = true;
        used_columns[pivot_element.column] = true;
    }

    static double[] SolveEquation(Equation equation) {
        double a[][] = equation.a;
        double b[] = equation.b;
        int size = a.length;

        boolean[] used_columns = new boolean[size];
        boolean[] used_raws = new boolean[size];
        for (int step = 0; step < size; ++step) {
            Position pivot_element = SelectPivotElement(a, step);
            SwapLines(a, b, used_raws, pivot_element);
            ProcessPivotElement(a, b, pivot_element);
            MarkPivotElementUsed(pivot_element, used_raws, used_columns);
        }

        for (int i = a.length - 1; i >= 0; i--) {
            double firstCoef = a[i][i];
            if (firstCoef == 0)
                throw new IllegalArgumentException();
            for (int j = i + 1; j < a[0].length; j++) {
                b[i] -= b[j] * a[i][j];
            }
            b[i] /= firstCoef;
        }

        return b;
    }

    static void PrintColumn(double column[]) {
        for (double aColumn : column) System.out.format(Locale.US, "%.10f%n", aColumn);

    }

    public static void main(String[] args) throws IOException {
        Equation equation = ReadEquation();
        double[] solution = SolveEquation(equation);
        PrintColumn(solution);
    }
}
