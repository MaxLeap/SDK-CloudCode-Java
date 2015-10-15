package com.maxleap.code.impl;

import com.maxleap.las.sdk.MLObject;
import com.maxleap.las.sdk.types.MLPointer;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by stream on 10/13/14.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Song extends MLObject {
  private String title;

  private MLPointer trackId;

  private Integer playTimes;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public MLPointer getTrackId() {
    return trackId;
  }

  public void setTrackId(MLPointer trackId) {
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
