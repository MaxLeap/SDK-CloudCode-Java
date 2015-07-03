package as.leap.code.assist.entity;

import as.leap.code.assist.Path;
import as.leap.las.sdk.LASObject;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * User：poplar
 * Date：15-6-2
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Path("/receiptRegulars")
public class ReceiptRegular extends LASObject {
  //发票名称
  private String name;
  //应用商店的productID
  private String storeProdId;
  //iOS、Amazon、GooglePlay
  private String platform;
  //数量
  private Double amount;
  //次数
  private int count;
  //货币种类
  private String coinId;
  //已成功请求次数
  private int queryCount;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStoreProdId() {
    return storeProdId;
  }

  public void setStoreProdId(String storeProdId) {
    this.storeProdId = storeProdId;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getCoinId() {
    return coinId;
  }

  public void setCoinId(String coinId) {
    this.coinId = coinId;
  }

  public int getQueryCount() {
    return queryCount;
  }

  public void setQueryCount(int queryCount) {
    this.queryCount = queryCount;
  }
}
