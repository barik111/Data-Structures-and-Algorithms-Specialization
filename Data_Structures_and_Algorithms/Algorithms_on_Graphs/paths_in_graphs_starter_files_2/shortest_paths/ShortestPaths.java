public class ShortestPaths {

    private static void shortestPaths(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, long[] distance, int[] reachable, int[] shortest) {
        distance[s] = 0;
        List<Integer> reachableVertex = dfs(adj, s);
        for (Integer index : reachableVertex) {
            reachable[index] = 1;
        }
        List<Integer> cycle = new ArrayList<>();

        for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < reachableVertex.size(); j++) {
                Integer currentVertex = reachableVertex.get(j);
                List<Integer> neighbours = adj[currentVertex];
                List<Integer> neighboursCosts = cost[currentVertex];
                for (int k = 0; k < neighbours.size(); k++) {
                    Integer currentNeighbour = neighbours.get(k);
                    Integer currentCost = neighboursCosts.get(k);

                    if (distance[currentNeighbour] > distance[currentVertex] + currentCost) {
                        distance[currentNeighbour] = distance[currentVertex] + currentCost;
                        if (i == adj.length - 1) {
                            shortest[currentNeighbour] = 0;
                            cycle.add(currentNeighbour);
                        }
                    }
                }
            }

        }
        Set<Integer> allNonReachable = new HashSet<>();
        if(!cycle.isEmpty()) {
            allNonReachable.addAll(dfs(adj, cycle.get(0)));
            for (Integer index : allNonReachable) {
                shortest[index] = 0;
            }
        }
    }

    public static List<Integer> dfs(ArrayList<Integer>[] adj, int s) {
        List<Integer> result = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[adj.length];

        queue.add(s);
        while (!queue.isEmpty()) {
            Integer currentVertex = queue.poll();
            if (!visited[currentVertex]) {
                visited[currentVertex] = true;
                result.add(currentVertex);
                List<Integer> neighbours = adj[currentVertex];
                for (Integer neighbour : neighbours) {
                    queue.add(neighbour);
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
        ArrayList<Integer>[] cost = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y, w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            adj[x - 1].add(y - 1);
            cost[x - 1].add(w);
        }
        int s = scanner.nextInt() - 1;
        long distance[] = new long[n];
        int reachable[] = new int[n];
        int shortest[] = new int[n];
        for (int i = 0; i < n; i++) {
            distance[i] = Integer.MAX_VALUE;
            reachable[i] = 0;
            shortest[i] = 1;
        }
        shortestPaths(adj, cost, s, distance, reachable, shortest);
        for (int i = 0; i < n; i++) {
            if (reachable[i] == 0) {
                System.out.println('*');
            } else if (shortest[i] == 0) {
                System.out.println('-');
            } else {
                System.out.println(distance[i]);
            }
        }
    }

}

