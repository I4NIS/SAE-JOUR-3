package fr.but.info.sae122.seance3.model;

import java.util.*;

public class BreadthFirstIterator implements Iterator<PathElement> {

  private final Set<String> visitedNodes;
  private final ArrayDeque<PathElement> queue;
  private final Graph graph;

  public BreadthFirstIterator(Graph graph, String firstNode) {
    this.queue = new ArrayDeque<>();
    this.visitedNodes = new HashSet<>();
    this.graph = graph;
    Collection<Edge> edges = graph.getEdgesFrom(firstNode);
    for(Edge edge : edges)
      if (edge.getFlow() < edge.getCapacity())
        queue.add(new PathElement(null, edge));
    visitedNodes.add(firstNode);
  }

  @Override
  public boolean hasNext() {
    return ! queue.isEmpty();
  }

  @Override
  public PathElement next() {
    var element = queue.removeFirst();
    var dest = element.getEdge().getToNode();
    if (! visitedNodes.contains(dest)) {
      Collection<Edge> edges = graph.getEdgesFrom(dest);
      for(Edge edge : edges)
        if (edge.getFlow() < edge.getCapacity())
          queue.add(new PathElement(element, edge));
      visitedNodes.add(dest);
    }
    return element;
  }

}
