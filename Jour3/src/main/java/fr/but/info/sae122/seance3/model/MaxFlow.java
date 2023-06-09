package fr.but.info.sae122.seance3.model;

import java.util.NoSuchElementException;

public class MaxFlow {
  private final Graph graph;
  private final String source;
  private final String sink;

  public MaxFlow(Graph graph, String source, String sink) {
    this.graph = graph;
    this.source = source;
    this.sink = sink;
  }

  public Graph getGraph() {
    return graph;
  }

  public String getSource() {
    return source;
  }

  public String getSink() {
    return sink;
  }

  public void increaseFlow(Path path) throws NoSuchElementException {
    for (Edge e : path.getPath()) {
      try {
        var edge = graph.getEdge(e.getFromNode(), e.getToNode());
        // forward edge
        edge.setFlow(edge.getFlow() + path.getFlow());
      } catch (IllegalArgumentException ex) {
        // reverse edge
        var edge = graph.getEdge(e.getToNode(), e.getFromNode());
        edge.setFlow(edge.getFlow() - path.getFlow());
      }
    }
  }

  public int getSourceFlow() {
    return graph.getEdgesFrom(source).stream().mapToInt(Edge::getFlow).sum();
  }
  public int getSinkFlow() {
    return graph.getEdgesTo(sink).stream().mapToInt(Edge::getFlow).sum();
  }

  public int computeMaxFlow() {
    try {
      while (true) {
        AugmentingPath path = new AugmentingPath(graph, source, sink);
        increaseFlow(path);
      }
    } catch (NoSuchElementException ex) {
      // no more augmenting path, flow is maximal
      return getSourceFlow();
    }
  }
}
