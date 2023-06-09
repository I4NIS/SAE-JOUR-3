package fr.but.info.sae122.seance3.model;

import java.util.Objects;

public final class Edge {
  private final String fromNode;
  private final String toNode;
  private int flow;
  private int capacity;

  public Edge(
      String fromNode,
      String toNode,
      int capacity) {
    if (capacity<0) throw new IllegalArgumentException("Capacity cannot be negative");
    this.fromNode = fromNode;
    this.toNode = toNode;
    this.capacity = capacity;
    this.flow = 0;
  }

  public String getFromNode() {
    return fromNode;
  }

  public String getToNode() {
    return toNode;
  }

  public int getFlow() {
    return flow;
  }

  public void setFlow(int flow) {
    if (flow < 0) throw new IllegalArgumentException("flow cannot be negative");
    if (flow > capacity) throw new IllegalArgumentException("flow cannot exceed capacity");
    this.flow = flow;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    if (capacity<0) throw new IllegalArgumentException("capacity cannot be negative");
    if (capacity<flow) throw new IllegalArgumentException("capacity cannot be lower than current flow");
    this.capacity = capacity;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (Edge) obj;
    return Objects.equals(this.fromNode, that.fromNode) &&
        Objects.equals(this.toNode, that.toNode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromNode, toNode);
  }

  @Override
  public String toString() {
    return "Edge[" +
        "fromNode=" + fromNode + ", " +
        "toNode=" + toNode + ", " +
        "flow=" + flow + " / " + capacity + ']';
  }
}
