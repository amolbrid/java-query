package javaquery;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;


public class MethodNameParserTest {
  @Test
  public void queryBySingleColumn() {
    Query query = new MethodNameParser().parse("getUserById");
    assertThat(query.toSql()).isEqualTo("SELECT * FROM user WHERE Id = ?");
  }

  @Test
  public void queryByTwoColumn() {
    Query query = new MethodNameParser().parse("getUserByIdAndName");
    assertThat(query.toSql()).isEqualTo("SELECT * FROM user WHERE Id = ? And Name = ?");
  }

  @Test
  public void queryByThreeColumn() {
    Query query = new MethodNameParser().parse("getUserByIdAndNameAndStatus");
    assertThat(query.toSql()).isEqualTo("SELECT * FROM user WHERE Id = ? And Name = ? And Status = ?");
    validate(query, query.where(), "Id=?AndName=?AndStatus=?");
  }

  @Test
  public void queryByOneLikeColumn() {
    Query query = new MethodNameParser().parse("getUserByNameLike");
    assertThat(query.toSql()).isEqualTo("SELECT * FROM user WHERE Name Like ?");
  }

  @Test
  public void queryByOneLikeAndOneEqual() {
    Query query = new MethodNameParser().parse("getUserByNameLikeAndActive");
    assertThat(query.toSql()).isEqualTo("SELECT * FROM user WHERE Name Like ? And Active = ?");
    validate(query, query.where(), "NameLike?AndActive=?");
  }

  @Test
  public void queryByGetUserByNameLikeOrStatus() {
    Query query = new MethodNameParser().parse("getUserByNameLikeOrStatus");
    assertThat(query.toSql()).isEqualTo("SELECT * FROM user WHERE Name Like ? Or Status = ?");
  }

  @Test
  public void usesGivenTableNameInQuery() {
    Query query = new MethodNameParser("users").parse("getByNameLikeOrStatus");
    assertThat(query.toSql()).isEqualTo("SELECT * FROM users WHERE Name Like ? Or Status = ?");
  }

  private void validate(Query query, Node where, String expected) {
    assertThat(query.tableName()).isEqualTo("user");
    StringBuilder str = new StringBuilder();
    buildNodeString(where, str);
    assertThat(str.toString()).isEqualTo(expected);
  }

  private void buildNodeString(Node node, StringBuilder stringBuilder) {
    if (node.left() != null) {
      buildNodeString(node.left(), stringBuilder);
    }
    stringBuilder.append(node.value());

    if(node.right() != null) {
      buildNodeString(node.right(), stringBuilder);
    }
  }
}
