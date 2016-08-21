package javaquery;

import javaquery.annotations.Table;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public class MethodHandler implements InvocationHandler {
  private SqlAdapter sqlAdapter;

  public MethodHandler(SqlAdapter sqlAdapter) {
    this.sqlAdapter = sqlAdapter;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    MethodNameParser parser;

    Table table = method.getAnnotation(Table.class);
    if (table != null) {
      parser = new MethodNameParser(table.name());
    } else {
      parser = new MethodNameParser();
    }

    Query query = parser.parse(method.getName());

    ParameterizedType returnType = (ParameterizedType) method.getGenericReturnType();
    Class<?> klass = (Class<?>) returnType.getActualTypeArguments()[0];

    return sqlAdapter.execute(query.toSql(), klass);
  }
}
