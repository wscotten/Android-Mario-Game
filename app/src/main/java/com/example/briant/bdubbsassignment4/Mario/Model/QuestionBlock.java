package com.example.briant.bdubbsassignment4.Mario.Model;

/**
 * Created by BrianT on 6/5/2017.
 */

public class QuestionBlock extends Block {
  private int powerUpIndex;
  private boolean hasBeenHit;
  QuestionBlock(int x, int y, int width, int height, int blockType, int index){
    super(x,y,width,height,blockType);
    powerUpIndex = index;
    hasBeenHit = false;
  }
  public int get_power_up_index(){ return powerUpIndex;}
  public boolean get_has_been_hit(){return hasBeenHit;}
  public void hit_block(){hasBeenHit = true;}
  }

