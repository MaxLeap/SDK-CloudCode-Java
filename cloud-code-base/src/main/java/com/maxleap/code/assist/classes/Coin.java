package com.maxleap.code.assist.classes;


import com.maxleap.code.assist.Path;
import com.maxleap.las.sdk.LASObject;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 货币
 * User：poplar
 * Date：15-6-2
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Path("/coins")
public class Coin extends LASObject {
  //货币名称
  private String name;
  //货币描述，用于显示
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

  @Override
  public String toString() {
    return "Coin{} " + super.toString();
  }
}
