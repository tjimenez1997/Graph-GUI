import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.*;

public class GraphModel {
    //Class Variables for all theoretical components in the model for the Graph
    private LinkedList<Vertex> vertextNodes = new LinkedList<Vertex>();
    private LinkedList<Vertex> selectedVertext = new LinkedList<Vertex>();
    private LinkedList<Edge> edgeList = new LinkedList<>();
    private LinkedList<Edge> connectedEdgeList = new LinkedList<Edge>();

    GUI view;

    /**
     * Gives current GUI View Object to the Model to modify the View when Model Changes
     * @param currentView
     */
    public GraphModel(GUI currentView) {
        view = currentView;
    }

    /**
     * Adds Vertex Object to Linked List holding all existing Vertex's. Then instructs View to update by drawing a Vertex into the GUI
     * @param clickX X location of potential Vertex
     * @param clickY Y location of potential Vertex
     */
    public void addVertex(double clickX, double clickY) {
        Vertex vertex = new Vertex(clickX, clickY);
        vertextNodes.add(vertex);
        view.drawVertex(clickX, clickY);
        System.out.println("Vertex created x: " + clickX + " y: " + clickY);
    }

    public void clearSelectedEdge() {
        selectedVertext.clear();
    }

    public void clearConnectedEdge() {
        connectedEdgeList.clear();
    }

    public LinkedList<Edge> getConnectedEdgeList() {
        return connectedEdgeList;
    }

    public LinkedList<Vertex> getSelectedVertextList() {
        return selectedVertext;
    }

    /**
     * God method used to select Vertex's on the graph. Function changes based on information the Model sees from the GUI. Either adds edges,weight, and adds a record of them on the Graphs Model (The Linked List's storing each object)
     * @param clickX X of user click on Graph Pane (GUI)
     * @param clickY Y of user click on Graph Pane (GUI)
     */
    public void selectVertex(double clickX, double clickY) {
        //General Selection
        Iterator vertexIterator = vertextNodes.iterator();
        while (vertexIterator.hasNext()) {
            Vertex next = (Vertex) vertexIterator.next();
            if (next.isSelected(clickX, clickY)) {
                selectedVertext.add(next);
                view.highlightVertex(next.getCircle());
            }
        }
        //Adds Edge
        if (selectedVertext.size() == 2 && view.getRadioGroup().getSelectedToggle().getUserData().toString().equals("2")) {
            Vertex startPoint = selectedVertext.getFirst();
            Vertex endPoint = selectedVertext.getLast();
            if (!checkDuplicateEdge(startPoint, endPoint)) {
                Edge edge = new Edge(startPoint, endPoint);
                edgeList.add(edge);
                view.drawEdge(startPoint, endPoint);
                view.redrawVertex(selectedVertext.getFirst(), selectedVertext.getLast());
                System.out.println("New Edge Created");
            }
            view.unhighlightVertex(startPoint.getCircle());
            view.unhighlightVertex(endPoint.getCircle());
            selectedVertext.clear();
        }

        //Moves Vertex
        if (view.getRadioGroup().getSelectedToggle().getUserData().toString().equals("3")) {
            System.out.println("Moving Vertex");
        }

        //Add Weight (Custom Number)
        if (selectedVertext.size() == 2 && view.getRadioGroup().getSelectedToggle().getUserData().toString().equals("5")) {
            Vertex startPoint = selectedVertext.getFirst();
            Vertex endPoint = selectedVertext.getLast();
            Edge selected = findEdge(startPoint, endPoint);
            if (selected != null) {
                if (selected.getWeight() == 0) {
                    selected.setWeight(view.getWeightValue());
                    view.drawWeight(selected);
                } else {
                    view.eraseWeight(selected);
                    selected.setWeight(view.getWeightValue());
                    view.drawWeight(selected);
                }
            } else {
                System.out.println("No edge exists or same vertex");
            }
            view.unhighlightVertex(startPoint.getCircle());
            view.unhighlightVertex(endPoint.getCircle());
            selectedVertext.clear();
        }

        //Shortest Path
        if (selectedVertext.size() == 2 && view.getRadioGroup().getSelectedToggle().getUserData().toString().equals("4")) {
            if (shortestPathRequirementCheck()){
                shortestPath();
            } else {
                view.resetGUI();
                selectedVertext.clear();
            }

        }
    }

    /**
     * Used to check if Shortest Path can be calculated between two Vertex's. Returns true if shortest path can be calculated.
     * @return
     */
    private boolean shortestPathRequirementCheck(){
        if(!selectedVertext.getFirst().equals(selectedVertext.getLast())){
            Iterator edgeIterator = edgeList.iterator();
            while(edgeIterator.hasNext()){
                Edge current = (Edge) edgeIterator.next();
                if(current.getWeight() == 0){
                    System.out.println("All edges must have a weight for shortest path to be calculated");
                    return false;
                }
            }
            return true;
        }
        System.out.println("Can't calculate shortest path on the same vertex, or some Vertex is not connected to a edge");
        return false;
    }

    /**
     * Adds all possible Edges to the Graph Area. Each edge is added to the Edge Linked List if it does not exist yet
     */
    public void addAllEdges() {
        Vertex current, next;
        for (int currentIndex = 0; currentIndex < vertextNodes.size() - 1; currentIndex++) {
            current = vertextNodes.get(currentIndex);
            for (int nextIndex = currentIndex + 1; nextIndex < vertextNodes.size(); nextIndex++) {
                next = vertextNodes.get(nextIndex);
                if (!checkDuplicateEdge(current, next)) { //Only creates edge if it doesn't exist
                    Edge edge = new Edge(current, next);
                    System.out.println("Edge Created");
                    edgeList.add(edge);
                    view.drawEdge(current, next);
                    view.redrawVertex(current, next);
                }
            }
        }
    }

    /**
     *  Adds random weights (1-25) to all existing edges on the Graph Model. Updates GUI (the view) once all Weights are added.
     */
    public void addRandomWeights() {
        Iterator edgeIterator = edgeList.iterator();
        Vertex startPoint, endPoint;
        Random r = new Random();
        while (edgeIterator.hasNext()) {
            Edge current = (Edge) edgeIterator.next();
            int randomNumber = r.nextInt(25) + 1;
            startPoint = current.getStart();
            endPoint = current.getEnd();
            //New Weight being Added to Edge
            if (current.getWeight() == 0) {
                current.setWeight(randomNumber);
                view.drawWeight(current);
                view.unhighlightVertex(startPoint.getCircle());
                view.unhighlightVertex(endPoint.getCircle());
            } else { //Weight Exists on Edge (New Random Weight being added)
                System.out.println("Replacing Existing Weight");
                view.eraseWeight(current);
                current.setWeight(randomNumber);
                view.drawWeight(current);
            }
        }
    }

    /**
     * Links a Line (GUI Representation of Edge) to a Edge object. Used by GUI when Line Object is created.
     * @param line
     */
    public void linkLineToEdge(Line line) {
        edgeList.getLast().setLine(line);
    }

    /**
     * Links a Circle (GUI Representation of Vertex) to a Vertex Object. Used by GUI when Line Object is created.
     * @param circle
     */
    public void linkCircleToVertex(Circle circle) {
        vertextNodes.getLast().setCircle(circle);
    }

    /**
     * Finds a edge using the starting and ending Vertex. Returns null if no edge is found
     * @param start Vertex representing either the start/endpoint of a edge
     * @param end Vertex representing either the start/endpoint of a edge
     * @return
     */
    private Edge findEdge(Vertex start, Vertex end) {
        Iterator edgeIterator = edgeList.iterator();
        while (edgeIterator.hasNext()) {
            Edge current = (Edge) edgeIterator.next();
            if (current.getStart().equals(start) && current.getEnd().equals(end) || current.getStart().equals(end) && current.getEnd().equals(start)) {
                return current;
            }
        }
        return null;
    }

    /**
     * Checks if Edge already exists between two Vertex's or the same Vertex was selected (In this case a edge cannot be created)
     * @param startPoint Vertex representing either the start/endpoint of a edge
     * @param endPoint Vertex representing either the start/endpoint of a edge
     * @return
     */
    private boolean checkDuplicateEdge(Vertex startPoint, Vertex endPoint) {
        Iterator edgeIterator = edgeList.iterator();
        if (startPoint.equals(endPoint)) {
            System.out.println("Cannot create edge on the same Vertex");
            return true;
        }
        while (edgeIterator.hasNext()) {
            Edge next = (Edge) edgeIterator.next();
            if (next.getStart().equals(startPoint) && next.getEnd().equals(endPoint) || next.getStart().equals(endPoint) && next.getEnd().equals(startPoint)) {
                System.out.println("Duplicate Edge Detected: No New Edge Created");
                return true;
            }
        }
        return false;
    }


    /**
     * Checks for Edges connected to a specific vertex and adds it to a list (which is used by other methods)
     * @param vertexToCheck
     */
    public void getConnected(Vertex vertexToCheck) {
        Iterator edgeIterator = edgeList.iterator();
        while (edgeIterator.hasNext()) {
            Edge current = (Edge) edgeIterator.next();
            if (current.getStart().equals(vertexToCheck) || current.getEnd().equals(vertexToCheck)) {
                connectedEdgeList.add(current);
            }
        }
    }

    /**
     * Implementation of Dijkstra’s algorithm used to find the shortest path between any connected Vertex's the user makes
     */
    private void shortestPath() {
        System.out.println("Shortest Path");
        Map<Vertex, Integer> vertexCosts = new HashMap<Vertex, Integer>();
        Map<Vertex, Vertex> vertexPaths = new HashMap<Vertex, Vertex>();
        Set<Vertex> visitedVertex = new HashSet<Vertex>();

        vertexCosts.put(selectedVertext.getFirst(), 0);

        //Sets All Costs to Infinity (Greater than possible cost)
        Iterator vertexIterator = vertextNodes.iterator();
        while (vertexIterator.hasNext()) {
            Vertex current = (Vertex) vertexIterator.next();
            if (!current.equals(selectedVertext.getFirst())) {
                vertexCosts.put(current, Integer.MAX_VALUE);
            }
        }

        //Min heap to extract shortest neighbor
        PriorityQueue<Vertex> minimumHeap = new PriorityQueue<Vertex>(new Comparator<Vertex>() {
            @Override
            public int compare(Vertex o1, Vertex o2) { //Custom Comparator used to sort Vertex's on heap based on recorded Vertex Cost from the vertexCosts Map
                Integer value1 = vertexCosts.get(o1);
                Integer value2 = vertexCosts.get(o2);
                if (value1 < value2) {
                    return -1;
                }
                if (value1 == value2) {
                    return 0;
                }
                return 1;
            }

        });
        //Adds First Vertex (Cost = 0)
        minimumHeap.add(selectedVertext.getFirst());

        while (!minimumHeap.isEmpty()) {
            clearConnectedEdge(); //Clears Previous Connected Edges
            Vertex minHeapVertex = minimumHeap.poll(); //Removes Lowest Cost Vertex from Heap
            getConnected(minHeapVertex); //Gets connected neighbors to the minimum cost Vertex into connectedEdgeList
            visitedVertex.add(minHeapVertex); //Adds Lowest Cost Vertex to Visited

            Iterator neighborIterator = getConnectedEdgeList().iterator();
            while (neighborIterator.hasNext()) {
                Edge current = (Edge) neighborIterator.next();
                Integer pathCost = null;
                Vertex neighborVertex = current.getEnd();
                if (current.getStart().equals(minHeapVertex) && !visitedVertex.contains(current.getEnd())) {
                    pathCost = vertexCosts.get(minHeapVertex) + edgeCost(minHeapVertex, neighborVertex); //Adds Path Cost of current Edge + Previous Edges
                }
                if (current.getEnd().equals(minHeapVertex) && !visitedVertex.contains(current.getStart())) {
                    neighborVertex = current.getStart(); //Orients the neighbor Vertex (next potential path) to the right Vertex. (A edge can have the same two vertex's in two different orientations)
                    pathCost = vertexCosts.get(minHeapVertex) + edgeCost(minHeapVertex, neighborVertex);//Adds Path Cost of current Edge + Previous Edges
                }
                if (pathCost != null && pathCost < vertexCosts.get(neighborVertex)) { //If this path costs less than previous lowest recorded path cost
                    vertexCosts.put(neighborVertex, pathCost); //Records lowest calculated path cost to a particular vertex
                    vertexPaths.put(neighborVertex, minHeapVertex); //Records Vertex Path for Directions of Shortest Path to any Vertex
                    minimumHeap.add(neighborVertex); //Adds the new lowest path cost to the heap
                }

            }
        }

        //Last Selected Vertex is the destination of the shortest path calculation
        Vertex current = selectedVertext.getLast();
        boolean noConnectedEdges = false; //Used to check if selected vertex's have a edge connecting between them. Helps handle the GUI view update differently depending on circumstances
        while(current != selectedVertext.getFirst()){ //Loop iterates until the line connected to the origin vertex is highlighted
            Vertex next = vertexPaths.get(current);
            Edge edgeToHighlight = findEdge(current,next);
            if(edgeToHighlight != null) { //If edgeToHighlight is null, no recorded path was calculated and the shortest path failed (Most likely there are no edges connected between the two selected Vertex's)
                view.highlightLine(edgeToHighlight.getLine());
            } else{
                noConnectedEdges = true;
                break;
            }
            current = next;
        }
        if(!noConnectedEdges) {
            view.highlightVertex(selectedVertext.getFirst().getCircle()); //Regular selection method removes highlight
        } else {
            System.out.println("No connected edges between the selected Vertex's");
            view.resetGUI();
        }
        clearSelectedEdge();
        clearConnectedEdge();
    }

    /**
     * Returns the Edge Weight Value of a edge between two Vertex's
     * @param start Start/Endpoint of a Edge
     * @param end Start/Endpoint of a Edge
     * @return
     */
    private Integer edgeCost(Vertex start, Vertex end){
        Edge selectedEdge = findEdge(start,end);
        return selectedEdge.getWeight();
    }

}
