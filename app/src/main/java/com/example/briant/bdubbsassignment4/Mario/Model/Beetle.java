package com.example.briant.bdubbsassignment4.Mario.Model;

import com.example.briant.bdubbsassignment4.Mario.View.GameView;

/**
 * Created by Lil Uzi on 5/24/2017.
 */

public class Beetle extends GameObject {
  private int animation = 1;
  private String direction;
  private int speed = 10;

  public Beetle(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    direction = "left";
  }

  public void update(Block[] blocks) {
    synchronized (this) {
      update_pos();
      check_unit_collision(blocks);
    }
  }

  private void update_pos() {
    if (direction == "left") {
      x += speed;
    } else {
      x -= speed;
    }
  }

  private void check_unit_collision(Block[] blocks) {
    int overlapRight, overlapLeft, overlapBelow, overlapAbove;
    for(int i = 0; i < blocks.length; i++) {
      if(blocks[i] != null) {
        if((y > blocks[i].get_y() &&
            y < blocks[i].get_y() + blocks[i].get_height()) ||
            (y + height > blocks[i].get_y() &&
            y + height < blocks[i].get_y() + blocks[i].get_height())) {
          overlapLeft = x + width - blocks[i].get_x();
          overlapRight = blocks[i].get_x() + width - x;
          if(overlapLeft > 0 && x < blocks[i].get_x()) {
            stop_and_prevent_overlap("left", overlapLeft);
          } else if(overlapRight > 0 && blocks[i].get_x() < x) {
            stop_and_prevent_overlap("right", overlapRight);
          }
        }
      }
    }
  }

  private void stop_and_prevent_overlap(String direction, int overlap) {
    if(direction == "left") {
      this.direction = "right";
      x -= overlap;
    } else {
      this.direction = "left";
      x += overlap;
    }
  }

  public String get_image() {
    animation = animation == 2 ? 1 : 2;
    return direction + "_" + animation;
  }
}
