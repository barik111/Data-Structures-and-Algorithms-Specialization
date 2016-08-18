public class Dijkstra {

    private static int distance(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, int t) {
        // int result = 0;
        int[] dist = new int[adj.length];
        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE;
        }
        dist[s] = 0;

        TreeMap<Integer, List<Integer>> queue = new TreeMap<>();
        queue.put(0, new ArrayList<>());
        queue.get(0).add(s);
        while (!queue.isEmpty()) {
            Map.Entry<Integer, List<Integer>> currentEntry = queue.pollFirstEntry();
            List<Integer> vertexWithEqualDist = currentEntry.getValue();
            for (int i = 0; i < vertexWithEqualDist.size(); i++) {
                Integer currentVertex = vertexWithEqualDist.get(i);
                List<Integer> neighbours = adj[currentVertex];
                List<Integer> neighboursCosts = cost[currentVertex];
                for (int j = 0; j < neighbours.size(); j++) {
                    if (dist[neighbours.get(j)] > currentEntry.getKey() + neighboursCosts.get(j)) {
                        dist[neighbours.get(j)] = currentEntry.getKey() + neighboursCosts.get(j);
                        List<Integer> list = queue.get(dist[neighbours.get(j)]);
                        if (list != null){
                            list.add(neighbours.get(j));
                        }else{
                            queue.put(dist[neighbours.get(j)], new ArrayList<>());
                            queue.get(dist[neighbours.get(j)]).add(neighbours.get(j));
                        }
                    }

                }
            }
        }
        return dist[t] != Integer.MAX_VALUE ? dist[t] : -1;
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
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        System.out.println(distance(adj, cost, x, y));
    }
}

