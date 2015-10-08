package as.leap.code;

/**
 * User：poplar
 * Date：15/9/17
 */
public enum MethodName {
  CREATE("create"), UPDATE("update"), DELETE("delete"), DELETEBATCH("deleteBatch"),
  FIND_BY_ID("findById"), FIND("find"), UN_KNOW("unKnow");

  private String methodName;

  MethodName(String name) {
    this.methodName = name;
  }

  public static MethodName getByMethodName(String methodName) {
    for (MethodName method : values()) {
      if (method.methodName.equals(methodName))
        return method;
    }
    return UN_KNOW;
  }

  public String getMethodName() {
    return methodName;
  }
}
