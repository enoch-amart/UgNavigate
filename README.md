# UG Navigate: Optimal Routing Solution for University of Ghana Campus

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-007396?style=for-the-badge&logo=java&logoColor=white)
![Algorithms](https://img.shields.io/badge/Algorithms-FF6B6B?style=for-the-badge&logo=algorithm&logoColor=white)

## 🎯 Project Overview

**UG Navigate** is an intelligent campus routing system developed as part of DCIT 204 - Data Structures and Algorithms 1 coursework at the University of Ghana. This application provides optimal pathfinding solutions for navigating the UG campus, incorporating multiple advanced algorithms and real-time traffic simulation.

### 🌟 Key Features

- **Multi-Algorithm Pathfinding**: Dijkstra's, A*, and Floyd-Warshall algorithms
- **Dynamic Traffic Simulation**: Real-time traffic condition modeling based on time of day
- **Landmark-Based Navigation**: Route planning through specific campus landmarks
- **Interactive GUI**: Modern Swing-based user interface with tabbed navigation
- **Performance Analysis**: Algorithm execution time comparison and efficiency metrics
- **Alternative Route Generation**: Multiple route options with detailed turn-by-turn directions

## 🏗️ Architecture & Design

### Core Components

1. **CampusGraph**: Graph representation of the university campus
2. **PathfindingEngine**: Multi-algorithm routing engine
3. **UGNavigateSystem**: Main GUI application with modern interface
4. **Route Management**: Route calculation, optimization, and presentation

### Data Structures Used

- **Graph (Adjacency List)**: Campus layout representation
- **Priority Queue**: Dijkstra's and A* algorithm implementation
- **HashMap**: Node lookup and distance tracking
- **Dynamic Programming**: Floyd-Warshall all-pairs shortest path
- **Custom Collections**: Route management and filtering

## 🔧 Technologies & Tools

- **Language**: Java 8+
- **GUI Framework**: Java Swing with custom styling
- **Algorithms**: Dijkstra, A*, Floyd-Warshall
- **Design Patterns**: MVC, Observer, Strategy
- **Data Processing**: CSV file loading support

## 📋 Requirements

- Java Development Kit (JDK) 8 or higher
- Minimum 4GB RAM
- Display resolution: 1024x768 or higher

## 🚀 Installation & Setup

### Clone the Repository
```bash
git clone https://github.com/yourusername/ug-navigate.git
cd ug-navigate
```

### Compile and Run
```bash
# Compile the Java files
javac *.java

# Run the application
java UGNavigateSystem
```

### Alternative: Using IDE
1. Import the project into your preferred Java IDE (Eclipse, IntelliJ IDEA, NetBeans)
2. Ensure JDK 8+ is configured
3. Run the `UGNavigateSystem.main()` method

## 📊 Campus Locations

The system includes 18 major campus locations categorized by type:

### Academic Buildings
- Balme Library
- School of Medicine
- Business School
- Engineering Block
- Arts Block
- JQB Library
- Chemistry Block
- Physics Block
- Mathematics Block
- Law Faculty

### Residential Areas
- Commonwealth Hall
- Legon Hall

### Services & Amenities
- Main Entrance
- Central Cafeteria
- Night Market
- Bank Area
- Sports Complex
- Admin Block

## 🎮 How to Use

### Basic Navigation
1. **Launch** the application
2. **Select** source location from dropdown
3. **Choose** destination location
4. **Configure** time of day and optional landmark preferences
5. **Click** "Find Routes" to calculate optimal paths

### Advanced Features
- **Landmark Filtering**: Route through specific landmark types
- **Traffic Simulation**: Different conditions based on time of day
- **Alternative Routes**: Compare multiple routing options
- **Algorithm Analysis**: View performance metrics for different algorithms

### Interface Tabs
- **Route Planning**: Main navigation interface
- **Algorithm Analysis**: Performance comparison and metrics
- **Campus Overview**: Complete location directory

## 🧮 Algorithms Implemented

### 1. Dijkstra's Algorithm
- **Purpose**: Single-source shortest path
- **Time Complexity**: O((V + E) log V)
- **Use Case**: Optimal route calculation with traffic weights

### 2. A* Search Algorithm
- **Purpose**: Informed search with heuristics
- **Time Complexity**: O(b^d) where b is branching factor
- **Heuristic**: Haversine distance between geographic coordinates
- **Use Case**: Efficient pathfinding with geographic awareness

### 3. Floyd-Warshall Algorithm
- **Purpose**: All-pairs shortest paths
- **Time Complexity**: O(V³)
- **Use Case**: Precomputed route lookup for instant results

### 4. Traffic Simulation
```java
// Dynamic traffic condition calculation
switch (timeOfDay) {
    case MORNING_RUSH:
        // Heavy traffic near academic buildings
    case EVENING_RUSH:
        // Heavy traffic near residential areas
    case NORMAL_HOURS:
        // Standard traffic conditions
}
```

## 📈 Performance Metrics

The application provides real-time performance analysis:

- **Execution Time**: Measured in microseconds (μs)
- **Route Distance**: Calculated in meters
- **Efficiency Rating**: Excellent/Good/Fair/Poor based on execution time
- **Memory Usage**: Optimized graph representation

## 🏛️ Campus Integration

### Landmark Types
```java
enum LandmarkType {
    ENTRANCE, ACADEMIC, RESIDENTIAL, 
    DINING, RECREATION, SERVICES, 
    ADMINISTRATIVE, GENERAL
}
```

### Traffic Conditions
```java
enum TrafficCondition {
    LIGHT(1.0, 1.0),      // No traffic penalty
    MODERATE(1.2, 1.3),   // 20% distance, 30% time penalty
    HEAVY(1.5, 1.8)       // 50% distance, 80% time penalty
}
```

## 🔄 Algorithm Optimization Techniques

### Divide and Conquer
- Graph partitioning for large-scale routing
- Hierarchical pathfinding for efficiency

### Greedy Approach
- Heuristic-based A* implementation
- Traffic-aware route selection

### Dynamic Programming
- Floyd-Warshall precomputation
- Memoization for repeated route queries

## 📊 Sample Output

```
Route #1: Main Entrance → Balme Library
Distance: 450 meters
Time: 5.4 minutes
Traffic: MODERATE
Path: Main Entrance → Bank Area → Arts Block → Balme Library
```

## 🎨 User Interface Features

### Modern Design Elements
- **Color Scheme**: Professional blue (#007bff) with light backgrounds
- **Typography**: Segoe UI font family for clarity
- **Icons**: Emoji-based navigation icons
- **Layout**: Responsive tabbed interface

### Interactive Components
- **Combo Boxes**: Location and preference selection
- **Tables**: Route comparison and algorithm metrics
- **Progress Indicators**: Real-time calculation feedback
- **Status Bar**: Current operation status

## 🧪 Testing & Validation

The system has been tested with:
- **Route Accuracy**: Verified against manual calculations
- **Algorithm Correctness**: Compared outputs across different algorithms
- **Performance Benchmarks**: Execution time analysis
- **UI Responsiveness**: User interaction testing

## 🔮 Future Enhancements

- **Real-time GPS Integration**: Live location tracking
- **Mobile Application**: Android/iOS companion app
- **Crowd-sourced Traffic**: User-reported traffic conditions
- **3D Campus Visualization**: Interactive 3D campus map
- **Voice Navigation**: Audio turn-by-turn directions
- **Accessibility Features**: Support for users with disabilities

## 📚 Academic Context

This project demonstrates practical application of:
- **Graph Theory**: Campus as weighted directed graph
- **Algorithm Analysis**: Time and space complexity evaluation
- **Software Engineering**: Object-oriented design principles
- **User Experience Design**: Intuitive interface development
- **Data Structures**: Efficient storage and retrieval mechanisms

## 👥 Contributors

**DCIT 204 Group Project**
- Enoch Amarteifio __ 10716297
- Donkor Obadiah __ 22033341
- Be-Ir Aidan __ 22034367
- Sekyere Gibson __ 22039273
- Addo Kingsley Nana Yaw Agyapong __ 22241382
- Adzraku Prosper Awoenam __ 22042713
- Akuoko Priscilla __ 22056561
- Atisu Sandra Ehornam __ 22058697
- Peprah Jephthah __ 22036173



## 📄 License

This project is developed for educational purposes as part of the University of Ghana Computer Science curriculum.

## 🤝 Contributing

This is an academic project. For questions or suggestions:
Contact Enoch Amarteifio at amarteifioenoch4@gmail.com or eaamarteifio002@st.ug.edu.gh

## 🆘 Support & Troubleshooting

### Common Issues

1. **Application won't start**
   - Verify Java 8+ installation
   - Check classpath configuration

2. **No routes found**
   - Ensure source ≠ destination
   - Check campus graph connectivity

3. **Performance issues**
   - Close other applications
   - Increase JVM heap size: `java -Xmx2g UGNavigateSystem`

### Debug Mode
```bash
java -Djava.util.logging.level=FINE UGNavigateSystem
```

---

**🎓 Academic Excellence | 🏛️ University of Ghana | 💻 Computer Science**
