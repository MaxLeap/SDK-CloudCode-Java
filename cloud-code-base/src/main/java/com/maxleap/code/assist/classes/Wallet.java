package com.maxleap.code.assist.classes;

import com.maxleap.code.assist.Path;
import com.maxleap.las.sdk.MLObject;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * 钱包
 * User：poplar
 * Date：15-6-2
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Path("/wallets")
public class Wallet extends MLObject {
  //用户ID
  private String uId;
  //coinId和数量的kv
  private Map<String, Double> products;

  public String getuId() {
    return uId;
  }

  public void setuId(String uId) {
    this.uId = uId;
  }

  public Map<String, Double> getProducts() {
    return products;
  }

  public void setProducts(Map<String, Double> products) {
    this.products = products;
  }
}
