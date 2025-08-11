
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 * UG Navigate - Professional Campus Routing System
 * Combines modern UI design with comprehensive algorithmic implementation
 */
public class UGNavigateSystem extends JFrame {
    // Core components
    private CampusGraph campusGraph;
    private PathfindingEngine pathfindingEngine;

    // UI Components
    private JComboBox<String> sourceCombo, destinationCombo, timeOfDayCombo, landmarkCombo;
    private JCheckBox landmarkFilterCheckbox;
    private JEditorPane resultArea;
    private JTable routeTable, algorithmTable;
    private DefaultTableModel routeTableModel, algorithmTableModel;
    private JProgressBar progressBar;
    private JLabel statusLabel;

    // Data
    private List<Route> currentAlternativeRoutes;
    private RoutingResult lastResult;

    public UGNavigateSystem() {
        initializeLookAndFeel();
        initializeCampusData();
        pathfindingEngine = new PathfindingEngine(campusGraph);
        setupGUI();
        setStatusMessage("Ready - Select locations to find optimal routes");
    }

    private void initializeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Modern color scheme
            UIManager.put("Panel.background", new Color(248, 249, 250));
            UIManager.put("Button.background", new Color(0, 123, 255));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 12));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeCampusData() {
        campusGraph = new CampusGraph();

        // Try loading from CSV first, fallback to hardcoded data
        try (InputStream nodeStream = getClass().getResourceAsStream("/nodes.csv");
                InputStream edgeStream = getClass().getResourceAsStream("/edges.csv")) {

            if (nodeStream != null && edgeStream != null) {
                DataReader.loadNodes(nodeStream, campusGraph);
                DataReader.loadEdges(edgeStream, campusGraph);
            } else {
                setupHardcodedCampusData();
            }
        } catch (Exception e) {
            setupHardcodedCampusData();
        }
    }

    private void setupHardcodedCampusData() {
        // Comprehensive UG campus locations
        setupCampusNodes();
        setupCampusEdges();
    }

    private void setupCampusNodes() {
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
        campusGraph.addNode(15, "Physics Block", 5.6542, -0.1865, LandmarkType.ACADEMIC);
        campusGraph.addNode(16, "Mathematics Block", 5.6544, -0.1867, LandmarkType.ACADEMIC);
        campusGraph.addNode(17, "Law Faculty", 5.6547, -0.1872, LandmarkType.ACADEMIC);
    }

    private void setupCampusEdges() {
        // Connect nodes with realistic distances and traffic patterns
        campusGraph.addEdge(0, 1, 450, TrafficCondition.MODERATE);
        campusGraph.addEdge(0, 4, 320, TrafficCondition.LIGHT);
        campusGraph.addEdge(0, 12, 380, TrafficCondition.HEAVY);
        campusGraph.addEdge(1, 2, 280, TrafficCondition.LIGHT);
        campusGraph.addEdge(1, 5, 220, TrafficCondition.MODERATE);
        campusGraph.addEdge(1, 6, 180, TrafficCondition.HEAVY);
        campusGraph.addEdge(1, 8, 160, TrafficCondition.MODERATE);
        campusGraph.addEdge(1, 11, 140, TrafficCondition.LIGHT);
        campusGraph.addEdge(1, 13, 200, TrafficCondition.LIGHT);
        campusGraph.addEdge(2, 3, 350, TrafficCondition.LIGHT);
        campusGraph.addEdge(2, 7, 290, TrafficCondition.MODERATE);
        campusGraph.addEdge(3, 7, 200, TrafficCondition.LIGHT);
        campusGraph.addEdge(4, 10, 180, TrafficCondition.LIGHT);
        campusGraph.addEdge(4, 14, 160, TrafficCondition.LIGHT);
        campusGraph.addEdge(4, 15, 170, TrafficCondition.LIGHT);
        campusGraph.addEdge(5, 6, 190, TrafficCondition.HEAVY);
        campusGraph.addEdge(5, 8, 120, TrafficCondition.MODERATE);
        campusGraph.addEdge(6, 9, 240, TrafficCondition.HEAVY);
        campusGraph.addEdge(8, 11, 110, TrafficCondition.LIGHT);
        campusGraph.addEdge(8, 12, 90, TrafficCondition.MODERATE);
        campusGraph.addEdge(8, 17, 130, TrafficCondition.MODERATE);
        campusGraph.addEdge(10, 14, 130, TrafficCondition.LIGHT);
        campusGraph.addEdge(10, 15, 120, TrafficCondition.LIGHT);
        campusGraph.addEdge(11, 12, 80, TrafficCondition.LIGHT);
        campusGraph.addEdge(11, 13, 100, TrafficCondition.LIGHT);
        campusGraph.addEdge(11, 16, 90, TrafficCondition.LIGHT);
        campusGraph.addEdge(12, 13, 85, TrafficCondition.MODERATE);
        campusGraph.addEdge(13, 6, 120, TrafficCondition.MODERATE);
        campusGraph.addEdge(14, 15, 80, TrafficCondition.LIGHT);
        campusGraph.addEdge(15, 16, 70, TrafficCondition.LIGHT);
        campusGraph.addEdge(16, 17, 110, TrafficCondition.MODERATE);
    }

    private void setupGUI() {
        setTitle("UG Navigate - Advanced Campus Routing System v2.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main layout with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(248, 249, 250));

        // Header with branding
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Center content with tabs
        mainPanel.add(createTabbedContentPanel(), BorderLayout.CENTER);

        // Status bar
        mainPanel.add(createStatusPanel(), BorderLayout.SOUTH);

        add(mainPanel);

        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 123, 255));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("🗺️ UG Navigate");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Intelligent Campus Routing with Advanced Algorithms");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 220, 255));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        header.add(titlePanel, BorderLayout.WEST);
        return header;
    }

    private JTabbedPane createTabbedContentPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Route Planning Tab
        tabbedPane.addTab("🚶 Route Planning", createRoutePlanningPanel());

        // Algorithm Analysis Tab
        tabbedPane.addTab("⚡ Algorithm Analysis", createAlgorithmAnalysisPanel());

        // Campus Map Tab
        tabbedPane.addTab("🗺️ Campus Overview", createCampusOverviewPanel());

        return tabbedPane;
    }

    private JPanel createRoutePlanningPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Input panel
        panel.add(createInputPanel(), BorderLayout.NORTH);

        // Results split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(createResultsPanel());
        splitPane.setBottomComponent(createRouteTablePanel());
        splitPane.setResizeWeight(0.6);
        splitPane.setBorder(null);

        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Route Configuration"));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: Source and Destination
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("From:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        sourceCombo = new JComboBox<>(getLocationNames());
        sourceCombo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panel.add(sourceCombo, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("To:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        destinationCombo = new JComboBox<>(getLocationNames());
        destinationCombo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panel.add(destinationCombo, gbc);

        // Row 2: Time and Landmark filtering
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Time:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        timeOfDayCombo = new JComboBox<>(new String[] { "Normal Hours", "Morning Rush", "Evening Rush" });
        timeOfDayCombo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panel.add(timeOfDayCombo, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        landmarkFilterCheckbox = new JCheckBox("Via Landmark:");
        landmarkFilterCheckbox.setBackground(Color.WHITE);
        panel.add(landmarkFilterCheckbox, gbc);

        gbc.gridx = 3;
        gbc.weightx = 1.0;
        landmarkCombo = new JComboBox<>(getLandmarkTypes());
        landmarkCombo.setEnabled(false);
        landmarkCombo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panel.add(landmarkCombo, gbc);

        landmarkFilterCheckbox.addActionListener(e -> landmarkCombo.setEnabled(landmarkFilterCheckbox.isSelected()));

        // Find Routes Button
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0;
        JButton findButton = createStyledButton("Find Routes", "🔍");
        findButton.addActionListener(this::findRoutesAction);
        panel.add(findButton, gbc);

        return panel;
    }

    private JButton createStyledButton(String text, String icon) {
        JButton button = new JButton("<html><center>" + icon + "<br>" + text + "</center></html>");
        button.setPreferredSize(new Dimension(120, 50));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return button;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Route Details"));

        resultArea = new JEditorPane("text/html", getWelcomeHTML());
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRouteTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Alternative Routes"));

        String[] columns = { "Route", "Distance", "Time", "Traffic", "Path Summary" };
        routeTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        routeTable = new JTable(routeTableModel);
        routeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        routeTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        routeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));

        // Set column widths
        TableColumnModel columnModel = routeTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(1).setPreferredWidth(80);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(4).setPreferredWidth(400);

        routeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && currentAlternativeRoutes != null) {
                int selectedRow = routeTable.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < currentAlternativeRoutes.size()) {
                    displayRouteDetails(currentAlternativeRoutes.get(selectedRow), selectedRow + 1);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(routeTable);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAlgorithmAnalysisPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Algorithm performance table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Algorithm Performance Comparison"));

        String[] algColumns = { "Algorithm", "Distance (m)", "Execution Time (μs)", "Efficiency" };
        algorithmTableModel = new DefaultTableModel(algColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        algorithmTable = new JTable(algorithmTableModel);
        algorithmTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        algorithmTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));

        JScrollPane algScrollPane = new JScrollPane(algorithmTable);
        tablePanel.add(algScrollPane, BorderLayout.CENTER);

        panel.add(tablePanel, BorderLayout.CENTER);

        // Analysis text area
        JEditorPane analysisArea = new JEditorPane("text/html",
                "<html><body><h2>Algorithm Analysis</h2><p>Run route planning to see detailed algorithm performance analysis.</p></body></html>");
        analysisArea.setEditable(false);
        JScrollPane analysisScrollPane = new JScrollPane(analysisArea);
        analysisScrollPane.setPreferredSize(new Dimension(0, 200));
        panel.add(analysisScrollPane, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCampusOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JEditorPane mapArea = new JEditorPane("text/html", getCampusOverviewHTML());
        mapArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(mapArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 0, 0, 0));

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(200, 20));

        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(progressBar, BorderLayout.EAST);

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

    private void findRoutesAction(ActionEvent e) {
        String sourceName = (String) sourceCombo.getSelectedItem();
        String destName = (String) destinationCombo.getSelectedItem();

        if (sourceName.equals(destName)) {
            JOptionPane.showMessageDialog(this,
                    "Source and destination cannot be the same.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show progress
        setStatusMessage("Calculating optimal routes...");
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);

        // Use SwingWorker for background processing
        SwingWorker<RoutingResult, Void> worker = new SwingWorker<RoutingResult, Void>() {
            @Override
            protected RoutingResult doInBackground() throws Exception {
                // Dynamic traffic simulation
                TimeOfDay selectedTime = TimeOfDay.valueOf(
                        ((String) timeOfDayCombo.getSelectedItem()).replace(" ", "_").toUpperCase());
                campusGraph.updateTrafficConditions(selectedTime);

                CampusNode source = campusGraph.getNodeByName(sourceName);
                CampusNode destination = campusGraph.getNodeByName(destName);

                LandmarkType landmarkFilter = null;
                if (landmarkFilterCheckbox.isSelected()) {
                    landmarkFilter = LandmarkType.valueOf((String) landmarkCombo.getSelectedItem());
                }

                return pathfindingEngine.findOptimalRoutes(source, destination, landmarkFilter);
            }

            @Override
            protected void done() {
                try {
                    lastResult = get();
                    currentAlternativeRoutes = lastResult.getAlternativeRoutes();

                    updateRouteTable();
                    updateAlgorithmTable();

                    if (currentAlternativeRoutes.isEmpty()) {
                        resultArea.setText("<html><body><h2>No Route Found</h2>" +
                                "<p>Unable to find a path between " + sourceName + " and " + destName
                                + ".</p></body></html>");
                    } else {
                        displayRouteDetails(currentAlternativeRoutes.get(0), 1);
                        routeTable.setRowSelectionInterval(0, 0);
                    }

                    setStatusMessage(
                            "Route calculation complete - " + currentAlternativeRoutes.size() + " routes found");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    setStatusMessage("Error calculating routes");
                } finally {
                    progressBar.setVisible(false);
                    progressBar.setIndeterminate(false);
                }
            }
        };

        worker.execute();
    }

    private void updateRouteTable() {
        routeTableModel.setRowCount(0);

        for (int i = 0; i < currentAlternativeRoutes.size(); i++) {
            Route route = currentAlternativeRoutes.get(i);
            if (route == null || route.getPath().isEmpty())
                continue;

            String summary = route.getPath().stream()
                    .map(CampusNode::getName)
                    .collect(Collectors.joining(" → "));

            routeTableModel.addRow(new Object[] {
                    "#" + (i + 1),
                    String.format("%.0f m", route.getTotalDistance()),
                    String.format("%.1f min", route.getEstimatedTime()),
                    route.getAverageTrafficCondition(campusGraph).name(),
                    summary
            });
        }
    }

    private void updateAlgorithmTable() {
        algorithmTableModel.setRowCount(0);

        if (lastResult != null && lastResult.getAlgorithmResults() != null) {
            for (AlgorithmResult algResult : lastResult.getAlgorithmResults()) {
                String efficiency = getEfficiencyRating(algResult.getExecutionTime());
                algorithmTableModel.addRow(new Object[] {
                        algResult.getAlgorithmName(),
                        String.format("%.0f", algResult.getDistance()),
                        String.format("%d", algResult.getExecutionTime()),
                        efficiency
                });
            }
        }
    }

    private String getEfficiencyRating(long executionTime) {
        if (executionTime < 1000)
            return "Excellent";
        if (executionTime < 5000)
            return "Good";
        if (executionTime < 10000)
            return "Fair";
        return "Poor";
    }

    private void displayRouteDetails(Route route, int routeNumber) {
        if (route == null || route.getPath().isEmpty())
            return;

        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Segoe UI; padding: 10px;'>");
        html.append("<h2 style='color: #0066cc;'>Route #").append(routeNumber).append("</h2>");
        html.append("<div style='background: #f8f9fa; padding: 10px; border-radius: 5px; margin: 10px 0;'>");
        html.append("<strong>From:</strong> ").append(route.getPath().get(0).getName()).append("<br>");
        html.append("<strong>To:</strong> ").append(route.getPath().get(route.getPath().size() - 1).getName())
                .append("<br>");
        html.append("<strong>Distance:</strong> ").append(String.format("%.0f meters", route.getTotalDistance()))
                .append("<br>");
        html.append("<strong>Time:</strong> ").append(String.format("%.1f minutes", route.getEstimatedTime()))
                .append("<br>");
        html.append("<strong>Traffic:</strong> ").append(route.getAverageTrafficCondition(campusGraph))
                .append("</div>");

        html.append("<h3>Turn-by-Turn Directions:</h3>");
        html.append("<ol style='line-height: 1.6;'>");

        for (int i = 0; i < route.getPath().size() - 1; i++) {
            CampusNode current = route.getPath().get(i);
            CampusNode next = route.getPath().get(i + 1);
            CampusEdge edge = campusGraph.getEdge(current.getId(), next.getId());

            if (edge != null) {
                String bearing = pathfindingEngine.getBearing(current, next);
                html.append("<li>From <strong>").append(current.getName()).append("</strong>, ");
                html.append("walk ").append(String.format("%.0f", edge.getDistance())).append(" meters ");
                html.append("<span style='color: #0066cc;'>").append(bearing).append("</span> ");
                html.append("towards <strong>").append(next.getName()).append("</strong></li>");
            }
        }

        html.append("</ol>");
        html.append("<div style='background: #d4edda; padding: 10px; border-radius: 5px; margin: 10px 0;'>");
        html.append("🎯 <strong>You have arrived at your destination!</strong>");
        html.append("</div></body></html>");

        resultArea.setText(html.toString());
        resultArea.setCaretPosition(0);
    }

    private void setStatusMessage(String message) {
        statusLabel.setText(message);
    }

    private String getWelcomeHTML() {
        return "<html><body style='font-family: Segoe UI; padding: 20px;'>" +
                "<h1 style='color: #0066cc;'>Welcome to UG Navigate</h1>" +
                "<p>Your intelligent campus routing companion featuring:</p>" +
                "<ul>" +
                "<li><strong>Multiple Algorithms:</strong> Dijkstra, A*, Floyd-Warshall</li>" +
                "<li><strong>Traffic Simulation:</strong> Real-time traffic condition modeling</li>" +
                "<li><strong>Landmark Navigation:</strong> Route planning via specific landmarks</li>" +
                "<li><strong>Performance Analysis:</strong> Compare algorithm efficiency</li>" +
                "</ul>" +
                "<p>Select your <strong>source</strong> and <strong>destination</strong>, then click <strong>'Find Routes'</strong> to begin.</p>"
                +
                "</body></html>";
    }

    private String getCampusOverviewHTML() {
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Segoe UI; padding: 20px;'>");
        html.append("<h1>University of Ghana Campus Map</h1>");
        html.append("<h2>Available Locations</h2>");

        Map<LandmarkType, List<CampusNode>> locationsByType = campusGraph.getNodes().stream()
                .collect(Collectors.groupingBy(node -> node.getLandmarkType()));

        for (LandmarkType type : LandmarkType.values()) {
            List<CampusNode> locations = locationsByType.getOrDefault(type, Collections.emptyList());
            if (!locations.isEmpty()) {
                html.append("<h3>").append(type.name().replace("_", " ")).append(" (").append(locations.size())
                        .append(")</h3>");
                html.append("<ul>");
                locations.stream()
                        .sorted(Comparator.comparing(CampusNode::getName))
                        .forEach(node -> html.append("<li>").append(node.getName()).append("</li>"));
                html.append("</ul>");
            }
        }

        html.append("</body></html>");
        return html.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UGNavigateSystem().setVisible(true);
        });
    }
}

// ===== CORE DATA STRUCTURES =====

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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        CampusNode that = (CampusNode) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class CampusEdge {
    private final CampusNode source;
    private final CampusNode destination;
    private final double distance;
    private TrafficCondition trafficCondition;

    public CampusEdge(CampusNode source, CampusNode destination, double distance, TrafficCondition trafficCondition) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.trafficCondition = trafficCondition;
    }

    // Getters and setters
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

    public void setTrafficCondition(TrafficCondition trafficCondition) {
        this.trafficCondition = trafficCondition;
    }

    public double getAdjustedDistance() {
        return distance * trafficCondition.getDistanceMultiplier();
    }

    public double getEstimatedTime() {
        double baseTime = distance / 83.33; // 5 km/h = 83.33 m/min
        return baseTime * trafficCondition.getTimeMultiplier();
    }
}

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

    public void addNode(CampusNode node) {
        nodes.put(node.getId(), node);
        adjacencyList.put(node.getId(), new ArrayList<>());
    }

    public void addEdge(int sourceId, int destId, double distance, TrafficCondition traffic) {
        CampusNode source = nodes.get(sourceId);
        CampusNode dest = nodes.get(destId);

        if (source != null && dest != null) {
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

    public void updateTrafficConditions(TimeOfDay timeOfDay) {
        for (List<CampusEdge> edges : adjacencyList.values()) {
            for (CampusEdge edge : edges) {
                TrafficCondition newCondition = calculateTrafficCondition(edge, timeOfDay);
                edge.setTrafficCondition(newCondition);
            }
        }
    }

    private TrafficCondition calculateTrafficCondition(CampusEdge edge, TimeOfDay timeOfDay) {
        LandmarkType destType = edge.getDestination().getLandmarkType();

        switch (timeOfDay) {
            case MORNING_RUSH:
                if (destType == LandmarkType.ACADEMIC || destType == LandmarkType.ADMINISTRATIVE) {
                    return TrafficCondition.HEAVY;
                }
                if (destType == LandmarkType.DINING || destType == LandmarkType.SERVICES) {
                    return TrafficCondition.MODERATE;
                }
                break;
            case EVENING_RUSH:
                if (destType == LandmarkType.RESIDENTIAL || destType == LandmarkType.DINING) {
                    return TrafficCondition.HEAVY;
                }
                if (destType == LandmarkType.RECREATION) {
                    return TrafficCondition.MODERATE;
                }
                break;
            case NORMAL_HOURS:
            default:
                // Return original condition or light traffic
                break;
        }

        // Default conditions based on landmark type
        switch (destType) {
            case DINING:
            case SERVICES:
                return TrafficCondition.MODERATE;
            case ENTRANCE:
            case ADMINISTRATIVE:
                return TrafficCondition.HEAVY;
            default:
                return TrafficCondition.LIGHT;
        }
    }
}

// ===== PATHFINDING ENGINE =====

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

        // Initialize matrices
        for (int i = 0; i < n; i++) {
            Arrays.fill(fwDistances[i], Double.POSITIVE_INFINITY);
            Arrays.fill(fwNext[i], -1);
            fwDistances[i][i] = 0;
        }

        // Populate with direct edges
        for (CampusNode node : graph.getNodes()) {
            int u = node.getId();
            for (CampusEdge edge : graph.getEdges(u)) {
                int v = edge.getDestination().getId();
                fwDistances[u][v] = edge.getAdjustedDistance();
                fwNext[u][v] = v;
            }
        }

        // Floyd-Warshall algorithm
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
        Route dijkstraRoute = runDijkstra(source, destination, null, null);
        long dijkstraTime = (System.nanoTime() - startTime) / 1000;
        algorithmResults.add(new AlgorithmResult("Dijkstra",
                dijkstraRoute != null ? dijkstraRoute.getTotalDistance() : 0, dijkstraTime));

        // Run A* Algorithm
        startTime = System.nanoTime();
        Route aStarRoute = runAStar(source, destination);
        long aStarTime = (System.nanoTime() - startTime) / 1000;
        algorithmResults.add(new AlgorithmResult("A*",
                aStarRoute != null ? aStarRoute.getTotalDistance() : 0, aStarTime));

        // Run Floyd-Warshall (lookup only)
        startTime = System.nanoTime();
        Route floydRoute = runFloydWarshall(source, destination);
        long floydTime = (System.nanoTime() - startTime) / 1000;
        algorithmResults.add(new AlgorithmResult("Floyd-Warshall",
                floydRoute != null ? floydRoute.getTotalDistance() : 0, floydTime));

        // Generate alternative routes
        List<Route> alternativeRoutes = generateAlternativeRoutes(source, destination, landmarkFilter, dijkstraRoute);

        // Sort routes by efficiency (distance + time factor)
        alternativeRoutes.sort((r1, r2) -> {
            double score1 = r1.getTotalDistance() + r1.getEstimatedTime() * 50; // Weight time higher
            double score2 = r2.getTotalDistance() + r2.getEstimatedTime() * 50;
            return Double.compare(score1, score2);
        });

        Route optimalRoute = alternativeRoutes.isEmpty() ? null : alternativeRoutes.get(0);

        return new RoutingResult(optimalRoute, alternativeRoutes, algorithmResults);
    }

    private List<Route> generateAlternativeRoutes(CampusNode source, CampusNode destination,
            LandmarkType landmarkFilter, Route optimalRoute) {
        Set<Route> routes = new HashSet<>();

        // 1. Direct optimal route
        if (optimalRoute != null && !optimalRoute.getPath().isEmpty()) {
            routes.add(optimalRoute);
        }

        // 2. Route via specific landmark type
        if (landmarkFilter != null) {
            Route landmarkRoute = findRouteThroughLandmark(source, destination, landmarkFilter);
            if (landmarkRoute != null && !landmarkRoute.getPath().isEmpty()) {
                routes.add(landmarkRoute);
            }
        }

        // 3. Low-traffic route (avoid heavy traffic)
        Route lightTrafficRoute = runDijkstra(source, destination, EnumSet.of(TrafficCondition.HEAVY), null);
        if (lightTrafficRoute != null && !lightTrafficRoute.getPath().isEmpty()) {
            routes.add(lightTrafficRoute);
        }

        // 4. Alternative route excluding key nodes from optimal path
        if (optimalRoute != null && optimalRoute.getPath().size() > 2) {
            Set<Integer> excludedNodes = new HashSet<>();
            excludedNodes.add(optimalRoute.getPath().get(1).getId()); // Exclude second node
            Route divergentRoute = runDijkstra(source, destination, null, excludedNodes);
            if (divergentRoute != null && !divergentRoute.getPath().isEmpty()) {
                routes.add(divergentRoute);
            }
        }

        // 5. Scenic route via recreational areas
        Route scenicRoute = findRouteThroughLandmark(source, destination, LandmarkType.RECREATION);
        if (scenicRoute != null && !scenicRoute.getPath().isEmpty()) {
            routes.add(scenicRoute);
        }

        return new ArrayList<>(routes);
    }

    private Route runDijkstra(CampusNode source, CampusNode destination,
            EnumSet<TrafficCondition> excludedConditions, Set<Integer> excludedNodes) {
        Map<Integer, Double> distances = new HashMap<>();
        Map<Integer, CampusNode> previous = new HashMap<>();
        PriorityQueue<DijkstraNode> pq = new PriorityQueue<>();

        // Initialize distances
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

                // Skip excluded conditions
                if (excludedConditions != null && excludedConditions.contains(edge.getTrafficCondition())) {
                    continue;
                }

                // Skip excluded nodes
                if (excludedNodes != null && excludedNodes.contains(neighbor.getId())) {
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

        if (sourceId >= fwDistances.length || destId >= fwDistances.length ||
                fwDistances[sourceId][destId] == Double.POSITIVE_INFINITY) {
            return new Route();
        }

        List<CampusNode> path = new ArrayList<>();
        int currentId = sourceId;
        while (currentId != destId) {
            path.add(graph.getNode(currentId));
            currentId = fwNext[currentId][destId];
            if (currentId == -1)
                return new Route();
        }
        path.add(destination);

        return new Route(path, fwDistances[sourceId][destId], graph);
    }

    private Route findRouteThroughLandmark(CampusNode source, CampusNode destination, LandmarkType landmarkType) {
        List<CampusNode> potentialLandmarks = graph.getNodes().stream()
                .filter(node -> node.getLandmarkType() == landmarkType &&
                        !node.equals(source) && !node.equals(destination))
                .collect(Collectors.toList());

        if (potentialLandmarks.isEmpty()) {
            return new Route();
        }

        Route bestRoute = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (CampusNode landmark : potentialLandmarks) {
            Route part1 = runDijkstra(source, landmark, null, null);
            Route part2 = runDijkstra(landmark, destination, null, null);

            if (part1 != null && !part1.getPath().isEmpty() &&
                    part2 != null && !part2.getPath().isEmpty()) {
                double totalDistance = part1.getTotalDistance() + part2.getTotalDistance();
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
            return new Route();
        }

        return new Route(path, distances.get(destination.getId()), graph);
    }

    private Route combineRoutes(Route route1, Route route2) {
        List<CampusNode> combinedPath = new ArrayList<>(route1.getPath());
        combinedPath.remove(combinedPath.size() - 1); // Remove duplicate landmark
        combinedPath.addAll(route2.getPath());

        double totalAdjustedDistance = route1.getTotalDistance() + route2.getTotalDistance();

        return new Route(combinedPath, totalAdjustedDistance, graph);
    }

    public String getBearing(CampusNode node1, CampusNode node2) {
        double lat1 = Math.toRadians(node1.getLatitude());
        double lon1 = Math.toRadians(node1.getLongitude());
        double lat2 = Math.toRadians(node2.getLatitude());
        double lon2 = Math.toRadians(node2.getLongitude());

        double dLon = lon2 - lon1;
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        double bearing = Math.toDegrees(Math.atan2(y, x));
        bearing = (bearing + 360) % 360;

        String[] directions = { "North", "North-East", "East", "South-East",
                "South", "South-West", "West", "North-West" };
        return directions[(int) Math.round(bearing / 45) % 8];
    }
}

// ===== ALGORITHM SUPPORT CLASSES =====

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

// ===== ROUTE AND RESULT CLASSES =====

class Route {
    private final List<CampusNode> path;
    private final double adjustedDistance;
    private double totalDistance;
    private double estimatedTime;

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

    // Getters
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

        if (edgeCount == 0)
            return TrafficCondition.LIGHT;

        double avgMultiplier = totalMultiplier / edgeCount;
        if (avgMultiplier >= 1.4)
            return TrafficCondition.HEAVY;
        if (avgMultiplier >= 1.1)
            return TrafficCondition.MODERATE;
        return TrafficCondition.LIGHT;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Route route = (Route) obj;
        return Objects.equals(path, route.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}

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
    private final long executionTime;

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

// ===== ENUMS =====

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

enum TimeOfDay {
    NORMAL_HOURS, MORNING_RUSH, EVENING_RUSH
}

// ===== DATA LOADER =====

class DataReader {
    private static final Map<String, TrafficCondition> originalEdgeConditions = new HashMap<>();

    public static void loadNodes(InputStream input, CampusGraph graph) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 5) {
                    graph.addNode(new CampusNode(
                            Integer.parseInt(values[0].trim()),
                            values[1].trim(),
                            Double.parseDouble(values[2].trim()),
                            Double.parseDouble(values[3].trim()),
                            LandmarkType.valueOf(values[4].trim())));
                }
            }
        }
    }

    public static void loadEdges(InputStream input, CampusGraph graph) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 4) {
                    int sourceId = Integer.parseInt(values[0].trim());
                    int destId = Integer.parseInt(values[1].trim());
                    double distance = Double.parseDouble(values[2].trim());
                    TrafficCondition condition = TrafficCondition.valueOf(values[3].trim());

                    graph.addEdge(sourceId, destId, distance, condition);

                    // Store original conditions for traffic simulation
                    originalEdgeConditions.put(sourceId + "-" + destId, condition);
                    originalEdgeConditions.put(destId + "-" + sourceId, condition);
                }
            }
        }
    }

    public static TrafficCondition getOriginalTrafficCondition(int sourceId, int destId) {
        return originalEdgeConditions.get(sourceId + "-" + destId);
    }
}