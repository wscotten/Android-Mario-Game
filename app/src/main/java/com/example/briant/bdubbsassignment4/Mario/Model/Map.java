package com.example.briant.bdubbsassignment4.Mario.Model;

import android.graphics.Bitmap;

/**
 * Created by Brian Truong and William Scotten on 5/24/2017.
 */

public class Map {
  private int level;
  private int x;
  private Bitmap image;
  private Block blocks[];
  private Troopa troopas[];
  private Beetle beetles[];
  private Plant plants[];
  private PowerUps powerUps[];
  private int numX = 100;
  private int numY = 10;

  private GameObject[][] game_array;

  public Map(Bitmap img, int level) {
    powerUps = new PowerUps[6];
    x = 0;
    this.level = level;
    this.image = img;
    load_level(level);
  }

  public void set_map_at_start(){x = 0;}
  public int get_x() {return x;}
  public Bitmap get_image() {return image;}
  public GameObject[][] get_game_array() {return game_array;}
  public Block[] get_blocks() {return blocks;}
  public Troopa[] get_troopas() {return troopas;}
  public Beetle[] get_beetles() {return beetles;}
  public Plant[] get_plants() {return plants;}

  public void update(int marioPosition) {
    if (marioPosition > x + 800 && x < 12600) {
      x = marioPosition - 800;
    }
    for(int i = 0; i < troopas.length; i++) {
      if(troopas[i] != null) {
        troopas[i].update(blocks);
      }
    }
    for(int i = 0; i < beetles.length; i++) {
      if(beetles[i] != null) {
        beetles[i].update(blocks);
      }
    }
    for(int i = 0; i < plants.length; i++) {
      if(plants[i] != null) {
        plants[i].update();
      }
    }
  }

  public void load_level(int level){
    if(level == 0){
    load_level_1();
    }
    else if (level == 1){
      load_level_2();
    }
    else if(level == 2){
      load_level_3();
    }
  }

  public void load_level_1(){
    make_level_1_blocks(126, 126);
    make_level_1_enemies(125, 125);
  }

  public void load_level_2(){
    make_level_2_blocks(126, 126);
    make_level_2_enemies(120, 120);
  }

  public void load_level_3(){
    make_level_3_blocks(126, 126);
    make_level_3_enemies(120, 120);
  }

  public void make_level_3_blocks(int width, int height){
    int num_blocks = (int) ((double) image.getWidth() / width);
    blocks = new Block[num_blocks + 100];
    for(int i = 0; i < 4; i++)
    {
      blocks[i] = new Block(
          ((i+1) * width), image.getHeight() -  height, width,
          3 * height, 4);
    }
    int index = 4;
    for (int i = 7; i < num_blocks; i+=7) {
      int diffHeight = 2*(i%2);
      blocks[index] = new Block(
          ((i+1) * width), image.getHeight() - ((4+diffHeight) * height), width,
          (4+diffHeight) * height, 4);
      index +=1;
    }
  }

  public void make_level_2_blocks(int width, int height){
    int num_blocks = (int) ((double) image.getWidth() / width);
    blocks = new Block[num_blocks + 100];
    //blocks on ground
    for (int i = 0; i < num_blocks; i++) {
      blocks[i] = new Block(
          ((i +1) * width), image.getHeight() - height, width, height, 0);
      if (i > 4 && i < num_blocks*3/4 && (i % 15 == 0 || i % 15 == 1 || i % 15 == 2 )) {
        blocks[i] = null;
      }
    }
    int index = num_blocks+1;
    for(int i= 0; i < num_blocks; i++) {
      if (i > 5 && i < num_blocks*3/4 && (i % 15 == 14 || i % 15 == 3)) {
        blocks[index] = new Block(
            ((i+1) * width), image.getHeight() - 2 * height, width, height, 0);
        index+=1;
      }
    }
    //floating blocks
    int power_up_index = 0;
    int count = 10;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 4; j++) {
          blocks[index] = new Block( //make regular block
              ((count) * width), image.getHeight() - (4 * height), width,
              height, 1);
          index += 1;
          if(j == 1 || j == 2) {//the two middle blocks are question marks
            int block_type = count%3;
            blocks[index] = new QuestionBlock(
                ((count) * width), image.getHeight() - (8 * height), width,
                height, 6, power_up_index);
            add_power_ups((count) * width, image.getHeight() - (9 * height),
                width, height, block_type, power_up_index);
            power_up_index +=1;
            index += 1;
          }
        count += 1;
      }
      count += 28;
    }

    int stair_height = 7;
    for(int i = num_blocks-3; i > num_blocks -10; i--) {
      int j = 1;
      while(j < stair_height) {
       blocks[index] = new Block( //arbtrarily chosen x, y coord
            (i * width), image.getHeight() - ((j+1) * height), width,
            height, 2);
        j++;
        index+=1;
      }
      if(j == stair_height) {
        stair_height--;
      }
    }
  }

  public void make_level_1_blocks(int width, int height) {
    int num_blocks = (int) ((double) image.getWidth() / width);
    blocks = new Block[num_blocks + 30];
    //blocks on ground
    for (int i = 0; i < num_blocks; i++) {
      blocks[i] = new Block(
          ((i + 1) * width), image.getHeight() - height, width, height, 0);
      if ((i % 31 == 0 || i % 31 == 1 || i % 31 == 2 ) & i > 3) {
        blocks[i] = null;
      }
    }
    //floating blocks
    int power_up_index = 0;
    int count = 10;
    int index = num_blocks + 1;
    for (int i = 0; i < 3; i++) { //make 3 sets of 4 blocks //type 6 = ?block
      for (int j = 0; j < 4; j++) {
        if(j == 1 || j == 2) {//the two middle blocks are question marks
          blocks[index] = new QuestionBlock( //arbtrarily chosen x, y coord
              ((count) * width), image.getHeight() - (4 * height), width,
              height, 6, power_up_index);
          add_power_ups((count) * width, image.getHeight() - (5 * height),
              width, height, j, power_up_index);
          //add an element to the powerup[] one height above the question block
          power_up_index +=1;
          index += 1;
        }
        else {
          blocks[index] = new Block( //make regular block
              ((count) * width), image.getHeight() - (4 * height), width,
              height, 1);
          index += 1;
        }
        if (j > 0 && j < 3) {
          blocks[index] = new Block( // make a second set of blocks above it
              ((count) * width), image.getHeight() - (8 * height), width,
              height, 1);
          index += 1;
        }
        count += 1;
      }
      count += 30;
    }
    count = 15; //these are pipes
    for (int i = 0; i < 3; i++) {
      blocks[index] = new Block(
          ((count) * width), image.getHeight() - (4 * height), width,
          3 * height, 4);
      index += 1;
      count += 45;
    }

    //random block raised above ground
    blocks[index+1] = new Block(
        (20* width), image.getHeight() -  2*height, width,
         height, 0);
  }

  private void make_level_1_enemies(int width, int height) {
      troopas = null;
      troopas = new Troopa[10];
      beetles = null;
      beetles = new Beetle[10];
      plants = null;
      plants = new Plant[10];
      for (int i = 1; i < troopas.length; i++) {
        troopas[i] = new Troopa(((i * 9) * width) - 200,
            image.getHeight() - height * 4, width, height, i % 2);
      }
  }

  private void make_level_2_enemies(int width, int height) {
      troopas = null;
      troopas = new Troopa[10];
      beetles = null;
      beetles = new Beetle[10];
      plants = null;
      plants = new Plant[10];
      for (int i = 1; i < troopas.length; i++) {
        troopas[i] = new Troopa(((i * 9) * width) - 200,
            image.getHeight() - height * 4, width, height, i % 2);
      }
      for (int i = 1; i < beetles.length; i++) {
        beetles[i] = new Beetle(((i * 9) * width),
            image.getHeight() - 125 * 2, width, height);
      }
  }

  private void make_level_3_enemies(int width, int height) {
    troopas = null;
    troopas = new Troopa[10];
    beetles = null;
    beetles = new Beetle[10];
    plants = null;
    plants = new Plant[16];
    for (int i = 1; i < troopas.length; i++) {
      troopas[i] = new Troopa(((i * 9) * width) - 200,
          image.getHeight() - height * 4, width, height, i % 2);
    }
    for (int i = 1; i < beetles.length; i++) {
      beetles[i] = new Beetle(((i * 9) * width),
          image.getHeight() - height * 2, width, height);
    }
    int index = 0;
    for (int i = 7; i < plants.length * 7; i += 7) {
      int diffHeight = 2 * (i % 2);
      plants[index] = new Plant(
          ((i + 1) * (width + 6) - 3), image.getHeight() - ((5 + diffHeight) * height),
          width, height);
      index +=1;
    }
  }

  public void add_power_ups(int x, int y, int width, int height, int type,
                            int index) {
    powerUps[index] = new PowerUps(x, y, width, height, type);

  }
  public PowerUps[] get_power_ups(){
    return this.powerUps;
  }
}
