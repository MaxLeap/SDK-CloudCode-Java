package myApp;

import com.maxleap.code.*;
import com.maxleap.code.impl.GlobalConfig;
import com.maxleap.code.impl.LoaderBase;
import myApp.service.UserService;
import org.springframework.context.ApplicationContext;

/**
 *
 */
public class Main extends LoaderBase implements Loader {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  @Override
  public void main(ApplicationContext context,GlobalConfig globalConfig) {
    UserService userService = context.getBean(UserService.class);
    defineFunction("hello", userService.hello());

    defineJob("myJob", userService.helloJob());
  }
}
