package as.leap.code;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by stream.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassManager {
  String value() default "";
}
