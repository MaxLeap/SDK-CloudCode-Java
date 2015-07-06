package as.leap.code;

import as.leap.code.impl.GlobalConfig;

import java.util.Map;

/**
 * load all the define.
 * Created by stream.
 */
public interface Loader {

  Map<String, Definer> definers();

  /**
   * user have to implements this method for own code.
   */
  void main(GlobalConfig globalConfig);

}
