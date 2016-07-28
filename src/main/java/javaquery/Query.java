package javaquery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Query {
  private List<String> columns;
  private Node where;
  private String tableName;

  public Query(String tableName) {
    this.tableName = tableName;

    columns = new ArrayList<>();
  }

  public void addColumn(String column) {
    columns.add(column);
  }

  public Iterator<String> columns() {
    return columns.iterator();
  }

  public void where(Node node) {
    this.where = node;
  }

  public Node where() {
    return where;
  }

  public String tableName() {
    return tableName;
  }

  public String toSql() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SELECT ");
    stringBuilder.append(columns.stream().collect(Collectors.joining(",")));
    stringBuilder.append(" FROM ");
    stringBuilder.append(tableName);
    if (where != null) {
      stringBuilder.append(" WHERE ");
      buildNodeString(where, stringBuilder);
    }

    return stringBuilder.toString().trim();
  }

  private void buildNodeString(Node node, StringBuilder stringBuilder) {
    if (node.left() != null) {
      buildNodeString(node.left(), stringBuilder);
    }
    stringBuilder.append(node.value());
    stringBuilder.append(' ');
    if(node.right() != null) {
      buildNodeString(node.right(), stringBuilder);
    }
  }
}
