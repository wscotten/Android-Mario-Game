package com.example.briant.bdubbsassignment4.Mario.Model;

/**
 * Created by BrianT on 5/28/2017.
 */

public class Block extends  GameObject{
  private boolean breakable;
  private int blockType;

  public Block(int x, int y, int width, int height, int blockType){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.blockType = blockType;
  }

  public boolean is_breakable() {return breakable;}
  public int get_block_type() {return blockType;}
}
