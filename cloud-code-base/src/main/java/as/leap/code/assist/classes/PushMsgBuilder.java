package as.leap.code.assist.classes;


import as.leap.code.assist.Path;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * User：poplar
 * Date：15-8-25
 */
@Path("/marketing/push/msg")
public abstract class PushMsgBuilder {
  //发送条件
  protected String criteria;

  //发送内容
  protected ObjectNode data;

  protected String message;

  public PushMsgBuilder withInstallationId(String installationId) {
    this.criteria = "{\"installationId\":\"" + installationId + "\"}";
    return this;
  }

  public PushMsgBuilder withDeviceToken(String deviceToken) {
    this.criteria = "{\"deviceToken\":\"" + deviceToken + "\"}";
    return this;
  }

  public PushMsgBuilder withMsg(String msg){
    data = JsonNodeFactory.instance.objectNode();
    data.put("alert",msg);
    return this;
  }

  /**
   * build message like this:
   {
     "criteria":"{\"deviceToken\":\"APA91bFohTK3UAbjE-8K7R6KuPpmy7tA9fiD41jZFXL2TVzA6zSao2VIuOHfsDDrTB7DfHj7h4piA8w_rStUPvmlYgW4kQmVdgIIG4huZJiQOpgqXEirixz1S_AE18AbI6EMTUF4C2_4\"}",
     "creativeList": {
       "A": {
          "data": {
            "alert":"play poler"
          }
       }
     }
   }
   * @return PushMsgBuilder
   */
  protected PushMsgBuilder build() {
    ObjectNode result = JsonNodeFactory.instance.objectNode();
    result.put("criteria",criteria);
    ObjectNode A = JsonNodeFactory.instance.objectNode();
    ObjectNode msg = JsonNodeFactory.instance.objectNode();
    msg.put("data",data);
    A.put("A",msg);
    result.put("creativeList",A);
    message = result.toString();
    return this;
  }

  public abstract void push();

}
