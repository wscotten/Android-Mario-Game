package com.example.briant.bdubbsassignment4.Mario.Model;

/**
 * Created by BrianT on 6/5/2017.
 */

public class PowerUps extends  GameObject{
  private int powerUpType;
  private boolean has_been_hit = false;
  private boolean has_been_revealed = false;
   PowerUps(int x, int y, int width, int height, int powerUpType){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.powerUpType = powerUpType;
     //0 = coin, 1 = mushroom, 2 = flower
  }
  public boolean get_has_been_hit(){return has_been_hit;}
  public void hit_power_up(){has_been_hit = true;}
  public int get_power_up_type(){return powerUpType;}
  public void reveal_power_up(){has_been_revealed = true;}
  public boolean get_has_been_revealed(){return has_been_revealed;}
}
