package fr.but.info.sae122.seance3.model;

public final class PathElement {
  private final PathElement parent;
  private final Edge value;
  private final int maxFlow;

  public PathElement(PathElement parent, Edge value) {
    this.parent = parent;
    this.value = value;
    int flow = value.getCapacity() - getEdge().getFlow();
    if (parent != null) {
      if (parent.maxFlow < flow)
        maxFlow = getParent().maxFlow;
      else
        maxFlow = flow;
    } else
      maxFlow = flow;
  }

  public int getMaxFlow() {
    return maxFlow;
  }

  public PathElement getParent() {
    return parent;
  }

  public Edge getEdge() {
    return value;
  }

  @Override
  public String toString() {
    return "Element[" +
        "parent=" + parent + ", " +
        "value=" + value + ']';
  }
}
