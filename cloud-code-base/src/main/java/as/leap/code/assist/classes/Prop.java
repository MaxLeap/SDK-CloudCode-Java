package as.leap.code.assist.classes;

import as.leap.code.assist.Path;
import as.leap.las.sdk.LASObject;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 道具
 * User：poplar
 * Date：15-6-2
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Path("/props")
public class Prop extends LASObject {
  //道具名称
  private String name;
  //道具描述，用于显示
  private String desc;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
}
