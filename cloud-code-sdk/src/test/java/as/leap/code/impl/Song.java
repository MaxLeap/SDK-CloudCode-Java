package as.leap.code.impl;

import as.leap.las.sdk.LASObject;
import as.leap.las.sdk.types.LASPointer;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by stream on 10/13/14.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Song extends LASObject {
  private String title;

  private LASPointer trackId;

  private Integer playTimes;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public LASPointer getTrackId() {
    return trackId;
  }

  public void setTrackId(LASPointer trackId) {
    this.trackId = trackId;
  }

  public Integer getPlayTimes() {
    return playTimes;
  }

  public void setPlayTimes(Integer playTimes) {
    this.playTimes = playTimes;
  }

  @Override
  public String toString() {
    return "Song{" +
        "title='" + title + '\'' +
        ", trackId=" + trackId +
        ", playTimes=" + playTimes +
        "} " + super.toString();
  }
}
