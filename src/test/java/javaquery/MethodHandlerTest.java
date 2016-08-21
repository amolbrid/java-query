package javaquery;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javaquery.annotations.Table;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

public class MethodHandlerTest {
  @Test
  public void callsSqlAdapterExecutesMethod() throws Throwable {
    SqlAdapter mockAdapter = mock(SqlAdapter.class);
    MethodHandler handler = new MethodHandler(mockAdapter);
    Method method = findMethod("getUsers");

    handler.invoke(null, method, null);

    verify(mockAdapter).execute("SELECT * FROM users", User.class);
  }

  @Test
  public void usesTableAnnotationForTableName() throws Throwable {
    SqlAdapter mockAdapter = mock(SqlAdapter.class);
    MethodHandler handler = new MethodHandler(mockAdapter);
    Method method = findMethod("getAllInternalUsers");

    handler.invoke(null, method, null);

    verify(mockAdapter).execute("SELECT * FROM internal_users", User.class);
  }

  private Method findMethod(String name) {
    Method[] methods = UsersQuery.class.getDeclaredMethods();
    for (Method method : methods) {
      if (method.getName().equals(name)) {
        return method;
      }
    }

    return null;
  }

  interface UsersQuery {
    List<User> getUsers();

    @Table(name = "internal_users")
    List<User> getAllInternalUsers();
  }

  private class User {
    String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
