package com.example.briant.bdubbsassignment4.Mario.Model;

import com.example.briant.bdubbsassignment4.Mario.View.GameView;

/**
 * Created by Lil Uzi on 5/24/2017.
 */

public class Troopa extends GameObject {
  private boolean objectBelow;
  private int animation = 1;
  private int marioStartY;
  private String direction;
  private int speed;
  private String color;
  private boolean jumpedOn;
  private int timeUntilPopUpFromShell;

  public Troopa(int x, int y, int width, int height, int color) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.color = color % 2 == 0 ? "red" : "green";
    this.direction = color % 2 == 0 ? "up" : "left";
    timeUntilPopUpFromShell = 0;
    speed = 20;
  }

  public boolean get_jumped_on() {return jumpedOn;}
  public void set_jumped_on() {
    jumpedOn = true;
    timeUntilPopUpFromShell = 100;
  }

  public void update(Block[] blocks) {
    synchronized (this) {
      if(jumpedOn) {
        if(timeUntilPopUpFromShell <= 0) {
          jumpedOn = false;
        } else {
          timeUntilPopUpFromShell -= 1;
        }
      }
      update_pos();
      check_unit_collision(blocks);
    }
  }

  private void update_pos() {
    if(jumpedOn) {
      y += speed;
      return;
    }
    if(color == "red") {
      if(direction == "up") {
        y -= speed;
      } else {
        y += speed;
      }
    } else {
      if (direction == "left") {
        x += speed;
      } else {
        x -= speed;
      }
    }
  }

  private void check_unit_collision(Block[] blocks) {
    objectBelow = false;

    int overlapRight, overlapLeft, overlapBelow, overlapAbove;
    for(int i = 0; i < blocks.length; i++) {
      if(blocks[i] != null) {
        if(color == "red" || jumpedOn) {
          if((x < blocks[i].get_x() &&
              x > blocks[i].get_x() - blocks[i].get_width()) ||
              (x - width < blocks[i].get_x()) &&
              (x  > blocks[i].get_x() - blocks[i].get_width())){
            overlapAbove = blocks[i].get_y() + blocks[i].get_height() - y;
            overlapBelow = y + height - blocks[i].get_y();
            if(overlapAbove > 0 && blocks[i].get_y() < y || y < 0) {
              stop_and_prevent_overlap("up", overlapAbove);
            } else if(overlapBelow > 0 && y < blocks[i].get_y()) {
              stop_and_prevent_overlap("down", overlapBelow);
            }
          }
        } else {
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
  }

  private void stop_and_prevent_overlap(String direction, int overlap) {
    if(direction == "left") {
      this.direction = "right";
      x -= overlap;
    } else if(direction == "right") {
      this.direction = "left";
      x += overlap;
    } else if(direction== "up") {
      this.direction = "down";
      y += 20;
    } else if(direction == "down") {
      this.direction = "up";
      y -= 20;
    }
  }

  public String get_image() {
    if(jumpedOn) {
      if (timeUntilPopUpFromShell > 20) {
        return color + "_hidden_" + "0";
      } else {
        return color + "_hidden_" + (timeUntilPopUpFromShell % 2);
      }
    }
    animation = animation == 2 ? 1 : 2;
    if(color == "red") {
      return color + "_right_" + animation;
    } else {
      return  color + "_" + direction + "_" + animation;
    }
  }
}
