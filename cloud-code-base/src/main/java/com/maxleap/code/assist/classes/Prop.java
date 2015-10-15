package com.maxleap.code.assist.classes;

import com.maxleap.code.assist.Path;
import com.maxleap.las.sdk.MLObject;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 道具
 * User：poplar
 * Date：15-6-2
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Path("/props")
public class Prop extends MLObject {
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
