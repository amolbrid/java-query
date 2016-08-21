package javaquery;

import java.util.List;

/**
 * SqlAdapter that executes SQL query on underline database.
 *
 * @author Amol Brid
 */
public interface SqlAdapter {
  /**
   * Executes SQL and returns matching rows as instance of given class.
   * @param sql sql to execute.
   * @param klass type of return objects.
   * @return list of objects matching given sql. If no matching objects are found, returns empty list.
   */
  <T> List<T> execute(String sql, Class<T> klass);
}
