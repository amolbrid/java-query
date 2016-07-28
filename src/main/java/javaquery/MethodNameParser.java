package javaquery;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class MethodNameParser {
  private static final String REG_EX = "(?<=[a-z])(?=[A-Z])";

  public Query parse(String functionName) {
    String[] tokens = functionName.split(REG_EX);

    if (tokens.length < 2) {
      throw new IllegalArgumentException("");
    }

    Queue<String> queue = new ArrayDeque<>(Arrays.asList(tokens));

    String select = queue.remove();
    String tableName = queue.remove();
    if (!select.equals("get")) {
      throw new IllegalArgumentException("Must start with 'get'");
    }

    Query query = new Query(tableName);
    query.addColumn("*");

    while (!queue.isEmpty()) {
      String token = queue.remove();
      if (token.equalsIgnoreCase("by")) {
        query.where(buildWhere(queue));
      }
    }

    return query;
  }

  private Node buildWhere(Queue<String> queue) {
    Node parent = null;
    while (!queue.isEmpty()) {
      String field = queue.poll();
      String comparisonOperator = queue.poll();
      String logicalOperator = null;

      if (comparisonOperator == null) {
        // when there is no comparison operator specified. Like "ById"
        comparisonOperator = "=";
      } else if (isLogicalOperator(comparisonOperator)) {
        // when no comparison operator is specified and has logical operator. Like "ByIdAndName"
        logicalOperator = comparisonOperator;
        comparisonOperator = "=";
      } else {
        // when both comparison operator and logical operator is specified. Like "ByNameLikeAndStatus"
        logicalOperator = queue.poll();
      }

      if (logicalOperator == null) {
        Node condition = buildCondition(field, comparisonOperator);
        if (parent == null) {
          parent = condition;
        } else {
          parent.right(condition);
        }
      } else if (isLogicalOperator(logicalOperator)) {
        Node condition = buildCondition(field, comparisonOperator);
        if (parent == null) {
          parent = new Node(logicalOperator);
          parent.left(condition);
        } else {
          parent.right(condition);
          Node and = new Node(logicalOperator);
          and.left(parent);
          parent = and;
        }
      }
    }

    return parent;
  }

  private boolean isLogicalOperator(String operator) {
    return operator.equalsIgnoreCase("and") || operator.equalsIgnoreCase("or");
  }

  private Node buildCondition(String field, String operator) {
    Node parent = new Node(operator);
    parent.left(field);
    parent.right("?");

    return parent;
  }
}
