package as.leap.code;

/**
 * Created by stream.
 */
public enum RequestCategory {

  FUNCTION("function"), JOB("job"), EntityManager("entityManager"), HEALTH("health");

  private String alias;

  RequestCategory(String alias) {
    this.alias = alias;
  }

  public String alias() {
    return alias;
  }

  public static RequestCategory aliasOf(String alias) {
    for (RequestCategory requestCategory : values()) {
      if (requestCategory.alias().equals(alias)) return requestCategory;
    }
    return null;
  }
}
