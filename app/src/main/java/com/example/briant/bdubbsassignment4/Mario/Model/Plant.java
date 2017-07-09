package com.example.briant.bdubbsassignment4.Mario.Model;

import com.example.briant.bdubbsassignment4.Mario.View.GameView;

/**
 * Created by Lil Uzi on 5/24/2017.
 */

public class Plant extends GameObject {
  private String animation = "1";
  private boolean jumpedOn;
  private int speed = 10;
  private String direction = "up";
  private int baseHeight;
  private int wait;

  public Plant(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    baseHeight = y;
    this.width = width;
    this.height = height;

  }

  public void set_jumped_on() {jumpedOn = true;}
  public boolean get_jumped_on() {return jumpedOn;}

  public void update() {
    synchronized (this) {
      update_ypos();
      animation = animation == "1" ? "2" : "1";
    }
  }

  private void update_ypos() {
    if(wait > 0) {
      wait -= 1;
      return;
    }
    if(direction == "up") {
      y += speed;
      if(y > baseHeight + height) {
        direction = "down";
        y += 40;
        wait = 1000;
      }
    } else {
      y -= speed;
      if(y < baseHeight) {
        direction = "up";
        y -= 40;
        wait = 1000;
      }
    }
  }

  public String get_image() {
    return animation;
  }
}
