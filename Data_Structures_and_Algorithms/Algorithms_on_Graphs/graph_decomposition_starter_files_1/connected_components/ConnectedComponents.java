public class ConnectedComponents {
    private static int numberOfComponents(ArrayList<Integer>[] adj) {
        int result = 0;
        Queue<Integer> queueMain = new LinkedList<>();
        for (int i = 0; i < adj.length; i++) {
            queueMain.add(i);
        }

        while (!queueMain.isEmpty()) {
            result++;
            List<Integer> visited = new ArrayList<>();
            Queue<Integer> queueSecondary = new LinkedList<>();
            queueSecondary.add(queueMain.poll());
            while (!queueSecondary.isEmpty()) {
                Integer currentVertex = queueSecondary.poll();
                if (!visited.contains(currentVertex)) {
                    queueMain.remove(currentVertex);
                    visited.add(currentVertex);
                    List<Integer> currentVertexNeighbors = adj[currentVertex];
                    queueSecondary.addAll(currentVertexNeighbors);
                }
            }
        }

        return result;

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
            adj[y - 1].add(x - 1);
        }
        System.out.println(numberOfComponents(adj));
    }
}

