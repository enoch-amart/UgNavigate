// UG Navigate - Comprehensive Campus Routing System
// Main Application Class

import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// Main Application Class
public class UGNavigateSystem extends JFrame {
    private CampusGraph campusGraph;
    private PathfindingEngine pathfindingEngine;
    private JComboBox<String> sourceCombo, destinationCombo, landmarkCombo;
    private JTextArea resultArea;
    private JTable routeTable;
    private DefaultTableModel tableModel;
    private JCheckBox landmarkFilterCheckbox;

    public UGNavigateSystem() {
        initializeCampusData();
        // PathfindingEngine is initialized after the graph is fully built
        pathfindingEngine = new PathfindingEngine(campusGraph);
        setupGUI();
    }

    private void initializeCampusData() {
        campusGraph = new CampusGraph();
        setupCampusNodes();
        setupCampusEdges();
    }

    private void setupCampusNodes() {
        // Major UG campus locations with coordinates and landmark types
        campusGraph.addNode(0, "Main Entrance", 5.6531, -0.1864, LandmarkType.ENTRANCE);
        campusGraph.addNode(1, "Balme Library", 5.6545, -0.1875, LandmarkType.ACADEMIC);
        campusGraph.addNode(2, "Commonwealth Hall", 5.6558, -0.1889, LandmarkType.RESIDENTIAL);
        campusGraph.addNode(3, "Legon Hall", 5.6572, -0.1901, LandmarkType.RESIDENTIAL);
        campusGraph.addNode(4, "School of Medicine", 5.6539, -0.1851, LandmarkType.ACADEMIC);
        campusGraph.addNode(5, "Business School", 5.6551, -0.1867, LandmarkType.ACADEMIC);
        campusGraph.addNode(6, "Central Cafeteria", 5.6544, -0.1881, LandmarkType.DINING);
        campusGraph.addNode(7, "Sports Complex", 5.6566, -0.1894, LandmarkType.RECREATION);
        campusGraph.addNode(8, "Bank Area", 5.6548, -0.1873, LandmarkType.SERVICES);
        campusGraph.addNode(9, "Night Market", 5.6541, -0.1885, LandmarkType.DINING);
        campusGraph.addNode(10, "Engineering Block", 5.6537, -0.1859, LandmarkType.ACADEMIC);
        campusGraph.addNode(11, "Arts Block", 5.6549, -0.1871, LandmarkType.ACADEMIC);
        campusGraph.addNode(12, "Admin Block", 5.6546, -0.1869, LandmarkType.ADMINISTRATIVE);
        campusGraph.addNode(13, "JQB Library", 5.6543, -0.1877, LandmarkType.ACADEMIC);
        campusGraph.addNode(14, "Chemistry Block", 5.6540, -0.1863, LandmarkType.ACADEMIC);
    }

    private void setupCampusEdges() {
        // Connect nodes with realistic distances and traffic patterns
        campusGraph.addEdge(0, 1, 450, TrafficCondition.MODERATE); // Main Gate to Balme
        campusGraph.addEdge(0, 4, 320, TrafficCondition.LIGHT); // Main Gate to Medicine
        campusGraph.addEdge(0, 12, 380, TrafficCondition.HEAVY); // Main Gate to Admin
        campusGraph.addEdge(1, 2, 280, TrafficCondition.LIGHT); // Balme to Commonwealth
        campusGraph.addEdge(1, 5, 220, TrafficCondition.MODERATE); // Balme to Business
        campusGraph.addEdge(1, 6, 180, TrafficCondition.HEAVY); // Balme to Cafeteria
        campusGraph.addEdge(1, 8, 160, TrafficCondition.MODERATE); // Balme to Bank
        campusGraph.addEdge(1, 11, 140, TrafficCondition.LIGHT); // Balme to Arts
        campusGraph.addEdge(1, 13, 200, TrafficCondition.LIGHT); // Balme to JQB
        campusGraph.addEdge(2, 3, 350, TrafficCondition.LIGHT); // Commonwealth to Legon
        campusGraph.addEdge(2, 7, 290, TrafficCondition.MODERATE); // Commonwealth to Sports
        campusGraph.addEdge(3, 7, 200, TrafficCondition.LIGHT); // Legon to Sports
        campusGraph.addEdge(4, 10, 180, TrafficCondition.LIGHT); // Medicine to Engineering
        campusGraph.addEdge(4, 14, 160, TrafficCondition.LIGHT); // Medicine to Chemistry
        campusGraph.addEdge(5, 6, 190, TrafficCondition.HEAVY); // Business to Cafeteria
        campusGraph.addEdge(5, 8, 120, TrafficCondition.MODERATE); // Business to Bank
        campusGraph.addEdge(6, 9, 240, TrafficCondition.HEAVY); // Cafeteria to Night Market
        campusGraph.addEdge(8, 11, 110, TrafficCondition.LIGHT); // Bank to Arts
        campusGraph.addEdge(8, 12, 90, TrafficCondition.MODERATE); // Bank to Admin
        campusGraph.addEdge(10, 14, 130, TrafficCondition.LIGHT); // Engineering to Chemistry
        campusGraph.addEdge(11, 12, 80, TrafficCondition.LIGHT); // Arts to Admin
        campusGraph.addEdge(11, 13, 100, TrafficCondition.LIGHT); // Arts to JQB
        campusGraph.addEdge(12, 13, 85, TrafficCondition.MODERATE); // Admin to JQB
        campusGraph.addEdge(13, 6, 120, TrafficCondition.MODERATE); // JQB to Cafeteria
    }

    private void setupGUI() {
        setTitle("UG Navigate - Campus Routing System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.NORTH);

        JPanel centerPanel = createResultsPanel();
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = createRouteTablePanel();
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(1000, 700);
        setLocationRelativeTo(null);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Route Planning"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("From:"), gbc);
        gbc.gridx = 1;
        sourceCombo = new JComboBox<>(getLocationNames());
        panel.add(sourceCombo, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        panel.add(new JLabel("To:"), gbc);
        gbc.gridx = 3;
        destinationCombo = new JComboBox<>(getLocationNames());
        panel.add(destinationCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        landmarkFilterCheckbox = new JCheckBox("Filter by Landmark:");
        panel.add(landmarkFilterCheckbox, gbc);
        gbc.gridx = 1;
        landmarkCombo = new JComboBox<>(getLandmarkTypes());
        landmarkCombo.setEnabled(false);
        panel.add(landmarkCombo, gbc);

        landmarkFilterCheckbox.addActionListener(e -> landmarkCombo.setEnabled(landmarkFilterCheckbox.isSelected()));

        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        JButton findButton = new JButton("Find Routes");
        findButton.setPreferredSize(new Dimension(120, 50));
        findButton.addActionListener(this::findRoutes);
        panel.add(findButton, gbc);

        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Route Analysis"));

        resultArea = new JTextArea(15, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRouteTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Alternative Routes"));

        String[] columns = { "Route #", "Distance (m)", "Est. Time (min)", "Traffic", "Path Summary" };
        tableModel = new DefaultTableModel(columns, 0);
        routeTable = new JTable(tableModel);
        routeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScrollPane = new JScrollPane(routeTable);
        tableScrollPane.setPreferredSize(new Dimension(0, 150));
        panel.add(tableScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private String[] getLocationNames() {
        return campusGraph.getNodes().stream()
                .map(CampusNode::getName)
                .sorted()
                .toArray(String[]::new);
    }

    private String[] getLandmarkTypes() {
        return Arrays.stream(LandmarkType.values())
                .map(Enum::name)
                .sorted()
                .toArray(String[]::new);
    }

    private void findRoutes(ActionEvent e) {
        String sourceName = (String) sourceCombo.getSelectedItem();
        String destName = (String) destinationCombo.getSelectedItem();

        if (sourceName.equals(destName)) {
            JOptionPane.showMessageDialog(this, "Source and destination cannot be the same.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        CampusNode source = campusGraph.getNodeByName(sourceName);
        CampusNode destination = campusGraph.getNodeByName(destName);

        LandmarkType landmarkFilter = null;
        if (landmarkFilterCheckbox.isSelected()) {
            landmarkFilter = LandmarkType.valueOf((String) landmarkCombo.getSelectedItem());
        }

        RoutingResult result = pathfindingEngine.findOptimalRoutes(source, destination, landmarkFilter);

        if (result.getOptimalRoute() == null || result.getOptimalRoute().getPath().isEmpty()) {
            resultArea.setText("No path found between " + sourceName + " and " + destName + ".");
            tableModel.setRowCount(0);
            return;
        }

        displayResults(result);
        updateRouteTable(result.getAlternativeRoutes());
    }

    private void displayResults(RoutingResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== UG NAVIGATE ROUTING ANALYSIS ===\n");
        sb.append("Generated at: ").append(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .append("\n\n");

        sb.append("ALGORITHM PERFORMANCE COMPARISON:\n");
        sb.append("─".repeat(60)).append("\n");
        sb.append(String.format("%-20s %15s %15s\n", "Algorithm", "Distance (m)", "Exec. Time (µs)"));
        sb.append("─".repeat(60)).append("\n");

        for (AlgorithmResult algResult : result.getAlgorithmResults()) {
            sb.append(String.format("%-20s %13.0fm %13dµs\n",
                    algResult.getAlgorithmName(),
                    algResult.getDistance(),
                    algResult.getExecutionTime()));
        }
        sb.append("(Floyd-Warshall pre-calculated in ").append(pathfindingEngine.getFloydWarshallBuildTime())
                .append("ms)\n\n");

        sb.append("OPTIMAL ROUTE DETAILS (Route #1):\n");
        sb.append("─".repeat(60)).append("\n");
        Route optimal = result.getOptimalRoute();
        sb.append("Distance: ").append(String.format("%.0f meters\n", optimal.getTotalDistance()));
        sb.append("Estimated Time: ").append(String.format("%.1f minutes\n", optimal.getEstimatedTime()));
        sb.append("Average Traffic: ").append(optimal.getAverageTrafficCondition(campusGraph)).append("\n\n");

        sb.append("Route Path:\n");
        for (int i = 0; i < optimal.getPath().size(); i++) {
            CampusNode node = optimal.getPath().get(i);
            sb.append(String.format("%d. %s", i + 1, node.getName()));
            if (node.getLandmarkType() != LandmarkType.GENERAL) {
                sb.append(" (").append(node.getLandmarkType()).append(")");
            }
            if (i < optimal.getPath().size() - 1) {
                CampusNode nextNode = optimal.getPath().get(i + 1);
                double segmentDistance = campusGraph.getEdge(node.getId(), nextNode.getId()).getDistance();
                // Get the bearing for the next step
                String bearing = pathfindingEngine.getBearing(node, nextNode);

                sb.append(String.format("\n   → Walk %.0fm %s towards %s...\n\n", segmentDistance, bearing,
                        nextNode.getName()));
            } else {
                sb.append(" [DESTINATION]\n");
            }
        }

        resultArea.setText(sb.toString());
        resultArea.setCaretPosition(0);
    }

    private void updateRouteTable(List<Route> routes) {
        tableModel.setRowCount(0);

        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            if (route == null || route.getPath().isEmpty())
                continue;

            String summary = route.getPath().stream()
                    .map(CampusNode::getName)
                    .collect(Collectors.joining(" → "));

            tableModel.addRow(new Object[] {
                    "#" + (i + 1),
                    String.format("%.0f", route.getTotalDistance()),
                    String.format("%.1f", route.getEstimatedTime()),
                    route.getAverageTrafficCondition(campusGraph).name(),
                    summary
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new UGNavigateSystem().setVisible(true);
        });
    }
}

// Campus Node class representing locations
class CampusNode {
    private final int id;
    private final String name;
    private final double latitude;
    private final double longitude;
    private final LandmarkType landmarkType;

    public CampusNode(int id, String name, double latitude, double longitude, LandmarkType landmarkType) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.landmarkType = landmarkType == null ? LandmarkType.GENERAL : landmarkType;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public LandmarkType getLandmarkType() {
        return landmarkType;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        CampusNode that = (CampusNode) obj;
        return id == that.id;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}

// Campus Edge representing connections between locations
class CampusEdge {
    private final CampusNode source;
    private final CampusNode destination;
    private final double distance;
    private final TrafficCondition trafficCondition;

    public CampusEdge(CampusNode source, CampusNode destination, double distance, TrafficCondition trafficCondition) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.trafficCondition = trafficCondition;
    }

    // Getters
    public CampusNode getSource() {
        return source;
    }

    public CampusNode getDestination() {
        return destination;
    }

    public double getDistance() {
        return distance;
    }

    public TrafficCondition getTrafficCondition() {
        return trafficCondition;
    }

    public double getAdjustedDistance() {
        return distance * trafficCondition.getDistanceMultiplier();
    }

    public double getEstimatedTime() {
        // Average walking speed of 5 km/h = 83.33 m/min
        double baseTime = distance / 83.33;
        return baseTime * trafficCondition.getTimeMultiplier();
    }
}

// Campus Graph class
class CampusGraph {
    private final Map<Integer, CampusNode> nodes;
    private final Map<Integer, List<CampusEdge>> adjacencyList;

    public CampusGraph() {
        nodes = new HashMap<>();
        adjacencyList = new HashMap<>();
    }

    public void addNode(int id, String name, double lat, double lng, LandmarkType type) {
        CampusNode node = new CampusNode(id, name, lat, lng, type);
        nodes.put(id, node);
        adjacencyList.put(id, new ArrayList<>());
    }

    public void addEdge(int sourceId, int destId, double distance, TrafficCondition traffic) {
        CampusNode source = nodes.get(sourceId);
        CampusNode dest = nodes.get(destId);

        if (source != null && dest != null) {
            // Add edges in both directions for an undirected graph
            adjacencyList.get(sourceId).add(new CampusEdge(source, dest, distance, traffic));
            adjacencyList.get(destId).add(new CampusEdge(dest, source, distance, traffic));
        }
    }

    public CampusNode getNode(int id) {
        return nodes.get(id);
    }

    public CampusNode getNodeByName(String name) {
        return nodes.values().stream()
                .filter(node -> node.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Collection<CampusNode> getNodes() {
        return nodes.values();
    }

    public List<CampusEdge> getEdges(int nodeId) {
        return adjacencyList.getOrDefault(nodeId, Collections.emptyList());
    }

    public CampusEdge getEdge(int sourceId, int destId) {
        return getEdges(sourceId).stream()
                .filter(edge -> edge.getDestination().getId() == destId)
                .findFirst()
                .orElse(null);
    }

    public int getNodeCount() {
        return nodes.size();
    }
}

// Pathfinding Engine - Core algorithmic implementation
class PathfindingEngine {
    private final CampusGraph graph;
    private double[][] fwDistances;
    private int[][] fwNext;
    private long floydWarshallBuildTime;

    public PathfindingEngine(CampusGraph graph) {
        this.graph = graph;
        precomputeFloydWarshall();
    }

    public long getFloydWarshallBuildTime() {
        return floydWarshallBuildTime;
    }

    private void precomputeFloydWarshall() {
        long startTime = System.currentTimeMillis();
        int n = graph.getNodeCount();
        fwDistances = new double[n][n];
        fwNext = new int[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(fwDistances[i], Double.POSITIVE_INFINITY);
            Arrays.fill(fwNext[i], -1);
            fwDistances[i][i] = 0;
        }

        for (CampusNode node : graph.getNodes()) {
            int u = node.getId();
            for (CampusEdge edge : graph.getEdges(u)) {
                int v = edge.getDestination().getId();
                // Use adjusted distance for pathfinding
                fwDistances[u][v] = edge.getAdjustedDistance();
                fwNext[u][v] = v;
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (fwDistances[i][k] != Double.POSITIVE_INFINITY &&
                            fwDistances[k][j] != Double.POSITIVE_INFINITY &&
                            fwDistances[i][k] + fwDistances[k][j] < fwDistances[i][j]) {

                        fwDistances[i][j] = fwDistances[i][k] + fwDistances[k][j];
                        fwNext[i][j] = fwNext[i][k];
                    }
                }
            }
        }
        floydWarshallBuildTime = System.currentTimeMillis() - startTime;
    }

    public RoutingResult findOptimalRoutes(CampusNode source, CampusNode destination, LandmarkType landmarkFilter) {
        List<AlgorithmResult> algorithmResults = new ArrayList<>();

        // Run Dijkstra's Algorithm
        long startTime = System.nanoTime();
        Route dijkstraRoute = runDijkstra(source, destination, null);
        long dijkstraTime = (System.nanoTime() - startTime) / 1000;
        algorithmResults.add(new AlgorithmResult("Dijkstra", dijkstraRoute.getTotalDistance(), dijkstraTime));

        // Run A* Algorithm
        startTime = System.nanoTime();
        Route aStarRoute = runAStar(source, destination);
        long aStarTime = (System.nanoTime() - startTime) / 1000;
        algorithmResults.add(new AlgorithmResult("A*", aStarRoute.getTotalDistance(), aStarTime));

        // Run Floyd-Warshall (lookup only)
        startTime = System.nanoTime();
        Route floydRoute = runFloydWarshall(source, destination);
        long floydTime = (System.nanoTime() - startTime) / 1000;
        algorithmResults.add(new AlgorithmResult("Floyd-Warshall", floydRoute.getTotalDistance(), floydTime));

        List<Route> alternativeRoutes = generateAlternativeRoutes(source, destination, landmarkFilter, dijkstraRoute);
        alternativeRoutes.sort(new RouteComparator());

        Route optimalRoute = alternativeRoutes.isEmpty() ? null : alternativeRoutes.get(0);

        return new RoutingResult(optimalRoute, alternativeRoutes, algorithmResults);
    }

    private List<Route> generateAlternativeRoutes(CampusNode source, CampusNode destination,
            LandmarkType landmarkFilter, Route optimalRoute) {
        Set<Route> routes = new HashSet<>();

        // 1. The optimal route
        if (optimalRoute != null && !optimalRoute.getPath().isEmpty()) {
            routes.add(optimalRoute);
        }

        // 2. Route via a specific landmark type (if requested)
        if (landmarkFilter != null) {
            Route landmarkRoute = findRouteThroughLandmark(source, destination, landmarkFilter);
            if (!landmarkRoute.getPath().isEmpty()) {
                routes.add(landmarkRoute);
            }
        }

        // 3. Low-traffic route (avoids HEAVY traffic)
        Route lightTrafficRoute = runDijkstra(source, destination, EnumSet.of(TrafficCondition.HEAVY));
        if (!lightTrafficRoute.getPath().isEmpty()) {
            routes.add(lightTrafficRoute);
        }

        // 4. Generate a different path by temporarily "removing" an edge from the
        // optimal path
        if (optimalRoute != null && optimalRoute.getPath().size() > 1) {
            CampusNode firstNode = optimalRoute.getPath().get(0);
            CampusNode secondNode = optimalRoute.getPath().get(1);
            Set<Integer> excludedNodes = new HashSet<>();

            // Exclude the second node in the path to force a different route from the start
            excludedNodes.add(secondNode.getId());

            Route divergentRoute = runDijkstraWithExclusions(source, destination, excludedNodes);
            if (!divergentRoute.getPath().isEmpty()) {
                routes.add(divergentRoute);
            }
        }

        return new ArrayList<>(routes);
    }

    private Route runDijkstra(CampusNode source, CampusNode destination, EnumSet<TrafficCondition> excludedConditions) {
        Map<Integer, Double> distances = new HashMap<>();
        Map<Integer, CampusNode> previous = new HashMap<>();
        PriorityQueue<DijkstraNode> pq = new PriorityQueue<>();

        for (CampusNode node : graph.getNodes()) {
            distances.put(node.getId(), Double.POSITIVE_INFINITY);
        }
        distances.put(source.getId(), 0.0);
        pq.add(new DijkstraNode(source, 0.0));

        while (!pq.isEmpty()) {
            DijkstraNode current = pq.poll();

            if (current.distance > distances.get(current.node.getId())) {
                continue;
            }
            if (current.node.getId() == destination.getId()) {
                break;
            }

            for (CampusEdge edge : graph.getEdges(current.node.getId())) {
                if (excludedConditions != null && excludedConditions.contains(edge.getTrafficCondition())) {
                    continue; // Skip edges with excluded traffic conditions
                }

                CampusNode neighbor = edge.getDestination();
                double newDist = distances.get(current.node.getId()) + edge.getAdjustedDistance();

                if (newDist < distances.get(neighbor.getId())) {
                    distances.put(neighbor.getId(), newDist);
                    previous.put(neighbor.getId(), current.node);
                    pq.add(new DijkstraNode(neighbor, newDist));
                }
            }
        }
        return reconstructPath(source, destination, previous, distances);
    }

    private Route runDijkstraWithExclusions(CampusNode source, CampusNode destination, Set<Integer> excludedNodeIds) {
        Map<Integer, Double> distances = new HashMap<>();
        Map<Integer, CampusNode> previous = new HashMap<>();
        PriorityQueue<DijkstraNode> pq = new PriorityQueue<>();

        for (CampusNode node : graph.getNodes()) {
            distances.put(node.getId(), Double.POSITIVE_INFINITY);
        }
        distances.put(source.getId(), 0.0);
        pq.add(new DijkstraNode(source, 0.0));

        while (!pq.isEmpty()) {
            DijkstraNode current = pq.poll();

            if (current.distance > distances.get(current.node.getId())) {
                continue;
            }
            if (current.node.getId() == destination.getId()) {
                break;
            }

            for (CampusEdge edge : graph.getEdges(current.node.getId())) {
                CampusNode neighbor = edge.getDestination();
                // Skip excluded nodes
                if (excludedNodeIds.contains(neighbor.getId())) {
                    continue;
                }

                double newDist = distances.get(current.node.getId()) + edge.getAdjustedDistance();
                if (newDist < distances.get(neighbor.getId())) {
                    distances.put(neighbor.getId(), newDist);
                    previous.put(neighbor.getId(), current.node);
                    pq.add(new DijkstraNode(neighbor, newDist));
                }
            }
        }
        return reconstructPath(source, destination, previous, distances);
    }

    private Route runAStar(CampusNode source, CampusNode destination) {
        Map<Integer, Double> gScore = new HashMap<>();
        Map<Integer, CampusNode> previous = new HashMap<>();
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>();

        for (CampusNode node : graph.getNodes()) {
            gScore.put(node.getId(), Double.POSITIVE_INFINITY);
        }
        gScore.put(source.getId(), 0.0);
        openSet.add(new AStarNode(source, heuristic(source, destination)));

        while (!openSet.isEmpty()) {
            AStarNode current = openSet.poll();

            if (current.node.getId() == destination.getId()) {
                return reconstructPath(source, destination, previous, gScore);
            }

            for (CampusEdge edge : graph.getEdges(current.node.getId())) {
                CampusNode neighbor = edge.getDestination();
                double tentativeGScore = gScore.get(current.node.getId()) + edge.getAdjustedDistance();

                if (tentativeGScore < gScore.getOrDefault(neighbor.getId(), Double.POSITIVE_INFINITY)) {
                    previous.put(neighbor.getId(), current.node);
                    gScore.put(neighbor.getId(), tentativeGScore);
                    double fScore = tentativeGScore + heuristic(neighbor, destination);
                    openSet.add(new AStarNode(neighbor, fScore));
                }
            }
        }
        return new Route(); // Path not found
    }

    private Route runFloydWarshall(CampusNode source, CampusNode destination) {
        int sourceId = source.getId();
        int destId = destination.getId();

        if (fwDistances[sourceId][destId] == Double.POSITIVE_INFINITY) {
            return new Route(); // No path
        }

        List<CampusNode> path = new ArrayList<>();
        int currentId = sourceId;
        while (currentId != destId) {
            path.add(graph.getNode(currentId));
            currentId = fwNext[currentId][destId];
            if (currentId == -1)
                return new Route(); // Path broken
        }
        path.add(destination);

        return new Route(path, fwDistances[sourceId][destId], graph);
    }

    private Route findRouteThroughLandmark(CampusNode source, CampusNode destination, LandmarkType landmarkType) {
        List<CampusNode> potentialLandmarks = graph.getNodes().stream()
                .filter(node -> node.getLandmarkType() == landmarkType && !node.equals(source)
                        && !node.equals(destination))
                .collect(Collectors.toList());

        if (potentialLandmarks.isEmpty()) {
            return new Route();
        }

        Route bestRoute = null;
        double minDistance = Double.POSITIVE_INFINITY;

        // Find the landmark that provides the shortest total path
        for (CampusNode landmark : potentialLandmarks) {
            Route part1 = runDijkstra(source, landmark, null);
            Route part2 = runDijkstra(landmark, destination, null);

            if (!part1.getPath().isEmpty() && !part2.getPath().isEmpty()) {
                double totalDistance = part1.getAdjustedDistance() + part2.getAdjustedDistance();
                if (totalDistance < minDistance) {
                    minDistance = totalDistance;
                    bestRoute = combineRoutes(part1, part2);
                }
            }
        }
        return bestRoute == null ? new Route() : bestRoute;
    }

    private double heuristic(CampusNode a, CampusNode b) {
        // Haversine distance for geographic coordinates
        final int EARTH_RADIUS = 6371000; // meters
        double lat1 = Math.toRadians(a.getLatitude());
        double lon1 = Math.toRadians(a.getLongitude());
        double lat2 = Math.toRadians(b.getLatitude());
        double lon2 = Math.toRadians(b.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double val = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(val), Math.sqrt(1 - val));

        return EARTH_RADIUS * c;
    }

    private Route reconstructPath(CampusNode source, CampusNode destination,
            Map<Integer, CampusNode> previous, Map<Integer, Double> distances) {
        LinkedList<CampusNode> path = new LinkedList<>();
        CampusNode current = destination;

        while (current != null) {
            path.addFirst(current);
            current = previous.get(current.getId());
        }

        if (path.isEmpty() || !path.getFirst().equals(source)) {
            return new Route(); // No valid path
        }

        return new Route(path, distances.get(destination.getId()), graph);
    }

    private Route combineRoutes(Route route1, Route route2) {
        List<CampusNode> combinedPath = new ArrayList<>(route1.getPath());
        // Remove the duplicate landmark node before combining
        combinedPath.remove(combinedPath.size() - 1);
        combinedPath.addAll(route2.getPath());

        double totalAdjustedDistance = route1.getAdjustedDistance() + route2.getAdjustedDistance();

        return new Route(combinedPath, totalAdjustedDistance, graph);
    }

    // ADDED to PathfindingEngine class
    String getBearing(CampusNode node1, CampusNode node2) {
        double lat1 = Math.toRadians(node1.getLatitude());
        double lon1 = Math.toRadians(node1.getLongitude());
        double lat2 = Math.toRadians(node2.getLatitude());
        double lon2 = Math.toRadians(node2.getLongitude());

        double dLon = lon2 - lon1;
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        double bearing = Math.toDegrees(Math.atan2(y, x));
        bearing = (bearing + 360) % 360;

        String[] directions = { "North", "North-East", "East", "South-East", "South", "South-West", "West",
                "North-West" };
        return directions[(int) Math.round(bearing / 45) % 8];
    }
}

// Supporting classes for algorithms
class DijkstraNode implements Comparable<DijkstraNode> {
    final CampusNode node;
    final double distance;

    DijkstraNode(CampusNode node, double distance) {
        this.node = node;
        this.distance = distance;
    }

    @Override
    public int compareTo(DijkstraNode other) {
        return Double.compare(this.distance, other.distance);
    }
}

class AStarNode implements Comparable<AStarNode> {
    final CampusNode node;
    final double fScore;

    AStarNode(CampusNode node, double fScore) {
        this.node = node;
        this.fScore = fScore;
    }

    @Override
    public int compareTo(AStarNode other) {
        return Double.compare(this.fScore, other.fScore);
    }
}

// Route class representing a path between locations
class Route {
    private final List<CampusNode> path;
    private final double adjustedDistance; // Distance considering traffic multipliers
    private double totalDistance; // Raw physical distance
    private double estimatedTime; // Time considering traffic multipliers

    public Route() {
        this.path = Collections.emptyList();
        this.adjustedDistance = 0.0;
        this.totalDistance = 0.0;
        this.estimatedTime = 0.0;
    }

    public Route(List<CampusNode> path, double adjustedDistance, CampusGraph graph) {
        this.path = path;
        this.adjustedDistance = adjustedDistance;
        calculateMetrics(graph);
    }

    private void calculateMetrics(CampusGraph graph) {
        this.totalDistance = 0;
        this.estimatedTime = 0;
        if (path.size() < 2)
            return;

        for (int i = 0; i < path.size() - 1; i++) {
            CampusNode source = path.get(i);
            CampusNode dest = path.get(i + 1);
            CampusEdge edge = graph.getEdge(source.getId(), dest.getId());
            if (edge != null) {
                this.totalDistance += edge.getDistance();
                this.estimatedTime += edge.getEstimatedTime();
            }
        }
    }

    public List<CampusNode> getPath() {
        return path;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public double getAdjustedDistance() {
        return adjustedDistance;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public TrafficCondition getAverageTrafficCondition(CampusGraph graph) {
        if (path.size() < 2)
            return TrafficCondition.LIGHT;

        double totalMultiplier = 0;
        int edgeCount = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            CampusEdge edge = graph.getEdge(path.get(i).getId(), path.get(i + 1).getId());
            if (edge != null) {
                totalMultiplier += edge.getTrafficCondition().getDistanceMultiplier();
                edgeCount++;
            }
        }

        double avgMultiplier = totalMultiplier / edgeCount;
        if (avgMultiplier >= TrafficCondition.HEAVY.getDistanceMultiplier() - 0.1)
            return TrafficCondition.HEAVY;
        if (avgMultiplier >= TrafficCondition.MODERATE.getDistanceMultiplier() - 0.1)
            return TrafficCondition.MODERATE;
        return TrafficCondition.LIGHT;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Route route = (Route) obj;
        return Objects.equals(path, route.path);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(path);
    }
}

// Route comparator for sorting
class RouteComparator implements Comparator<Route> {
    @Override
    public int compare(Route r1, Route r2) {
        // Primary sort by adjusted distance, secondary by time
        int distanceCompare = Double.compare(r1.getAdjustedDistance(), r2.getAdjustedDistance());
        if (distanceCompare != 0)
            return distanceCompare;
        return Double.compare(r1.getEstimatedTime(), r2.getEstimatedTime());
    }
}

// Result classes
class RoutingResult {
    private final Route optimalRoute;
    private final List<Route> alternativeRoutes;
    private final List<AlgorithmResult> algorithmResults;

    public RoutingResult(Route optimalRoute, List<Route> alternativeRoutes, List<AlgorithmResult> algorithmResults) {
        this.optimalRoute = optimalRoute;
        this.alternativeRoutes = alternativeRoutes;
        this.algorithmResults = algorithmResults;
    }

    public Route getOptimalRoute() {
        return optimalRoute;
    }

    public List<Route> getAlternativeRoutes() {
        return alternativeRoutes;
    }

    public List<AlgorithmResult> getAlgorithmResults() {
        return algorithmResults;
    }
}

class AlgorithmResult {
    private final String algorithmName;
    private final double distance;
    private final long executionTime; // In microseconds

    public AlgorithmResult(String algorithmName, double distance, long executionTime) {
        this.algorithmName = algorithmName;
        this.distance = distance;
        this.executionTime = executionTime;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public double getDistance() {
        return distance;
    }

    public long getExecutionTime() {
        return executionTime;
    }
}

// Enums for classification
enum LandmarkType {
    ENTRANCE, ACADEMIC, RESIDENTIAL, DINING, RECREATION, SERVICES, ADMINISTRATIVE, GENERAL
}

enum TrafficCondition {
    LIGHT(1.0, 1.0),
    MODERATE(1.2, 1.3),
    HEAVY(1.5, 1.8);

    private final double distanceMultiplier;
    private final double timeMultiplier;

    TrafficCondition(double distanceMultiplier, double timeMultiplier) {
        this.distanceMultiplier = distanceMultiplier;
        this.timeMultiplier = timeMultiplier;
    }

    public double getDistanceMultiplier() {
        return distanceMultiplier;
    }

    public double getTimeMultiplier() {
        return timeMultiplier;
    }
}