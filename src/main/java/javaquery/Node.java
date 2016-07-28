package javaquery;

public class Node {
  private String value;
  private Node left;
  private Node right;

  public Node(String value) {
    this.value = value;
  }

  public Node left(String value) {
    this.left = new Node(value);
    return left;
  }

  public Node left(Node node) {
    this.left = node;
    return left;
  }

  public Node right(String value) {
    this.right = new Node(value);
    return right;
  }

  public Node right(Node node) {
    this.right = node;
    return right;
  }

  public Node left() {
    return left;
  }

  public Node right() {
    return right;
  }

  public String value() {
    return value;
  }

  public void value(String value) {
    this.value = value;
  }
}
