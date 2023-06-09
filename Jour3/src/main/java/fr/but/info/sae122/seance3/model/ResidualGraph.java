package fr.but.info.sae122.seance3.model;

public class ResidualGraph {
  public static Graph createFromGraph(Graph origin) {
    var residual = new Graph();
    origin.getNodes().forEach(residual::addNode);
    for (var edge : origin.getEdges()) {
      var left = edge.getCapacity()-edge.getFlow();
      if (left > 0)
        residual.addEdge(edge.getFromNode(), edge.getToNode(), left); // remaining flow
      if (edge.getFlow() > 0)
        residual.addEdge(edge.getToNode(), edge.getFromNode(), edge.getFlow()); // reverse flow
    }
    return residual;
  }
}
