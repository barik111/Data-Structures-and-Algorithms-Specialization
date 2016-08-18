public class ConnectingPoints {
    private static double minimumDistance(int[] x, int[] y) {
        double result = 0;
        DisjointSet<Point> disjointSet = new DisjointSet<>();
        for (int i = 0; i < x.length; i++) {
            disjointSet.makeSet(new Point(x[i], y[i]));
        }

        TreeMap<Double, List<Pair>> queue = new TreeMap<>();
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x.length && j != i; j++) {
                double distance = distance(x, y, i, j);
                if (queue.get(distance) == null) {
                    queue.put(distance, new ArrayList<>());
                }
                queue.get(distance).add(new Pair(new Point(x[i], y[i]), new Point(x[j], y[j])));
            }
        }
        int vertexCount = 1;
        while (vertexCount < x.length) {
            Map.Entry<Double, List<Pair>> current = queue.pollFirstEntry();
            double currentDistance = current.getKey();
            List<Pair> currentList = current.getValue();

            for (Pair pair : currentList) {
                if (!disjointSet.findSet(pair.first).equals(disjointSet.findSet(pair.second))) {
                    disjointSet.union(pair.first, pair.second);
                    result += currentDistance;
                    vertexCount++;
                }
            }
        }
        return result;
    }

    public static double distance(int[] x, int[] y, int i, int j) {
        return Math.sqrt((x[j] - x[i]) * (x[j] - x[i]) +
                (y[j] - y[i]) * (y[j] - y[i]));
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = scanner.nextInt();
            y[i] = scanner.nextInt();
        }
        System.out.println(minimumDistance(x, y));
    }
}

class Point {
    final public int x;
    final public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

class Pair {
    Point first;
    Point second;

    public Pair(Point first, Point second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}

class DisjointSet<T> {
    /**
     * A node in the disjoint set forest.  Each tree in the forest is
     * a disjoint set, where the root of the tree is the set representative.
     */
    private static class Node<T> {
        /**
         * The node rank used for union by rank optimization
         */
        int rank;
        /**
         * The parent of this node in the tree.
         */
        T parent;

        Node(T parent, int rank) {
            this.parent = parent;
            this.rank = rank;
        }
    }

    /**
     * Map of Object -> Node, where each key is an object in the
     * disjoint set, and the Node represents its position and rank
     * within the set.
     */
    private final HashMap<T, Node<T>> objectsToNodes = new HashMap<>();

    /**
     * Returns the set token for the given object, or null if the
     * object does not belong to any set.  All object
     * in the same set have an identical set token.
     *
     * @param o The object to return the set token for
     * @return The set token, or <code>null</code>
     */
    public T findSet(Object o) {
        DisjointSet.Node<T> node = objectsToNodes.get(o);
        if (node == null) {
            return null;
        }
        if (o != node.parent) {
            node.parent = findSet(node.parent);
        }
        return node.parent;
    }

    /**
     * Adds a new set to the group of disjoint sets for the given object.
     * It is assumed that the object does not yet belong to any set.
     *
     * @param o The object to add to the set
     */
    public void makeSet(T o) {
        objectsToNodes.put(o, new Node<>(o, 0));
    }

    /**
     * Removes all elements belonging to the set of the given object.
     *
     * @param o The object to remove
     */
    public void removeSet(Object o) {
        Object set = findSet(o);
        if (set == null) {
            return;
        }
        for (Iterator<T> it = objectsToNodes.keySet().iterator(); it.hasNext(); ) {
            T next = it.next();
            //remove the set representative last, otherwise findSet will fail
            if (next != set && findSet(next) == set) {
                it.remove();
            }
        }
        objectsToNodes.remove(set);
    }

    /**
     * Copies all objects in the disjoint set to the provided list
     *
     * @param list The list to copy objects into
     */
    public void toList(List<? super T> list) {
        list.addAll(objectsToNodes.keySet());
    }

    /**
     * Unions the set represented by token x with the set represented by
     * token y. Has no effect if either x or y is not in the disjoint set, or
     * if they already belong to the same set.
     *
     * @param x The first set to union
     * @param y The second set to union
     */
    public void union(T x, T y) {
        T setX = findSet(x);
        T setY = findSet(y);
        if (setX == null || setY == null || setX == setY) {
            return;
        }
        Node<T> nodeX = objectsToNodes.get(setX);
        Node<T> nodeY = objectsToNodes.get(setY);
        //join the two sets by pointing the root of one at the root of the other
        if (nodeX.rank > nodeY.rank) {
            nodeY.parent = x;
        } else {
            nodeX.parent = y;
            if (nodeX.rank == nodeY.rank) {
                nodeY.rank++;
            }
        }
    }
}