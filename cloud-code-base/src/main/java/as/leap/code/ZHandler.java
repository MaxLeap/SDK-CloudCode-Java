package as.leap.code;

/**
 * ZCloud Handler.
 * Created by stream.
 */
public interface ZHandler<T extends Request, R extends Response> {
  R handle(T request);
}
