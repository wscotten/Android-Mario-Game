package com.example.briant.bdubbsassignment4.Mario.Model;

import java.util.Date;

/**
 * Created by BrianT on 6/9/2017.
 */

public class Fireball extends GameObject {
  private String xDirection;
  private String yDirection;
  private boolean objectBelow = false;
  boolean overlapSide = false;
  private boolean hitBlock;
  private int bounceHeight = 100;
  private int bounceSpeed = 50;
  private int start_y;


  public Fireball(int x, int y, int height, int width, int mario_width,
                  int mario_height,String x_direction) {
    if(x_direction.equals("right")){
      this.x = x-mario_width;
    }else{
      this.x = x;
    }
    this.y = y+(mario_height/2);
    start_y = y+(mario_height/2);
    this.height = height;
    this.xDirection = x_direction;
    this.yDirection = "down";
    this.width = width;
  }

  public boolean update(Block[] blocks, Plant[] plants, Troopa[] troopas) {
    if(xDirection.equals("left")){
      x+=30;
      } else {
        x-=30;
      }
    check_unit_collision(blocks);
    check_unit_collision(plants);
    check_unit_collision(troopas);

    if(hitBlock) {
      return true;
    }

    return  false;
  }



  public void check_unit_collision(Block[] blocks) {
    hitBlock = false;
    int overlapRight, overlapLeft, overlapBelow, overlapAbove;
    for (int i = 0; i < blocks.length; i++) {
      if (blocks[i] != null) {
        if ((y >= blocks[i].get_y() &&
            y < blocks[i].get_y() + blocks[i].get_height()) ||
            (y + height > blocks[i].get_y() &&
                y + height < blocks[i].get_y() + blocks[i].get_height())) {
          overlapLeft = x + width - blocks[i].get_x();
          overlapRight = blocks[i].get_x() + width - x;
          if (overlapLeft > 0 && x < blocks[i].get_x()) {
            hitBlock = true;
          } else if (overlapRight > 0 && blocks[i].get_x() < x) {
            hitBlock = true;
          }
        }
      }
    }

  }
  public void check_unit_collision(Troopa[] troopas) {
    hitBlock = false;
    int overlapRight, overlapLeft, overlapBelow, overlapAbove;
    for (int i = 0; i < troopas.length; i++) {
      if (troopas[i] != null) {
        if ((y >= troopas[i].get_y() &&
            y < troopas[i].get_y() + troopas[i].get_height()) ||
            (y + height > troopas[i].get_y() &&
                y + height < troopas[i].get_y() + troopas[i].get_height())) {
          overlapLeft = x + width - troopas[i].get_x();
          overlapRight = troopas[i].get_x() + width - x;
          if (overlapLeft > 0 && x < troopas[i].get_x()) {
            hitBlock = true;
            troopas[i] = null;

          } else if (overlapRight > 0 && troopas[i].get_x() < x) {
            hitBlock = true;
            troopas[i] = null;

          }
        }
      }
    }

  }
  public void check_unit_collision(Plant[] plants) {
    hitBlock = false;
    int overlapRight, overlapLeft, overlapBelow, overlapAbove;
    for (int i = 0; i < plants.length; i++) {
      if (plants[i] != null) {
        if ((y >= plants[i].get_y() &&
            y < plants[i].get_y() + plants[i].get_height()) ||
            (y + height > plants[i].get_y() &&
                y + height < plants[i].get_y() + plants[i].get_height())) {
          overlapLeft = x + width - plants[i].get_x();
          overlapRight = plants[i].get_x() + width - x;
          if (overlapLeft > 0 && x < plants[i].get_x()) {
            hitBlock = true;
            plants[i] = null;
          } else if (overlapRight > 0 && plants[i].get_x() < x) {
            hitBlock = true;
            plants[i] = null;

          }
        }
      }
    }

  }
}


