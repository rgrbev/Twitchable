package com.twitchable.project.model;

/**
 * Created by ristes on 3/9/16.
 */
public enum Provider {
  TWITCH {
    @Override
    public String getLoginUrl() { return "/login/twitch"; }
  };

  public String getLoginUrl() {
    return null;
  }
}
