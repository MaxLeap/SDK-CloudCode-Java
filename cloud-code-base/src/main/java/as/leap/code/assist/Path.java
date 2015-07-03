package as.leap.code.assist;

import java.lang.annotation.*;

/**
 * User：poplar
 * Date：15-6-3
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Path {
  String value();
}
