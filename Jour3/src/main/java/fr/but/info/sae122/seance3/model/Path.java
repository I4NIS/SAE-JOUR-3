package fr.but.info.sae122.seance3.model;

import java.util.LinkedList;
import java.util.List;

public class Path {

  private int flow;
  private final LinkedList<Edge> path;

  public Path() {
    path = new LinkedList<>();
  }

  public Path(String... nodes) {
    path = new LinkedList<>();
    for (int i=1; i<nodes.length; ++i) {
      addLastEdge(new Edge(nodes[i-1], nodes[i], 0));
    }
  }

  public int getFlow() {
    return flow;
  }

  public List<Edge> getPath() {
    return path;
  }

  public void addLastEdge(Edge edge) {
    if (! path.isEmpty())
      if (!path.getLast().getToNode().equals(edge.getFromNode()))
        throw new IllegalArgumentException("Edge is not compatible with existing path!");
    path.addLast(edge);
  }
  public void addFirstEdge(Edge edge) {
    if (! path.isEmpty())
      if (!path.getFirst().getFromNode().equals(edge.getToNode()))
        throw new IllegalArgumentException("Edge is not compatible with existing path!");
    path.addFirst(edge);
  }

  public void setFlow(int flow) {
    this.flow = flow;
  }
}
