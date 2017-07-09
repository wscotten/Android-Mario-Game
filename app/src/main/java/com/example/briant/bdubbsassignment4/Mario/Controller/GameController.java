package com.example.briant.bdubbsassignment4.Mario.Controller;

import android.view.MotionEvent;

import com.example.briant.bdubbsassignment4.Mario.Model.Block;
import com.example.briant.bdubbsassignment4.Mario.Model.Mario;
import com.example.briant.bdubbsassignment4.Mario.Model.Map;
import com.example.briant.bdubbsassignment4.Mario.Model.PowerUps;
import com.example.briant.bdubbsassignment4.Mario.View.GameView;

/**
 * Created by Lil Uzi on 5/25/17.
 */

public class GameController {
  private int viewWidth;
  private int viewHeight;
  public Mario mario;
  private Map[] levels;
  private Map map;
  private GameThread gameThread;
  private double ratio;
  private boolean reset_game;
  private boolean game = true;
  private int gameScore = 0;
  private int currentLevel = 0;

  public GameController(GameView view, int viewWidth, int viewHeight, Map[] levels){
    this.viewWidth = viewWidth;
    this.viewHeight = viewHeight;
    ratio = viewWidth / viewHeight;
    this.levels = levels;
    map = levels[currentLevel];
    mario = new Mario(500, map.get_image().getHeight() - 250, 125, 125);
    gameThread = new GameThread(this, view);
    gameThread.start();
  }

  public int get_level(){return currentLevel;}
  public int get_game_score(){return  gameScore;}
  public boolean get_game(){return game;}
  public int get_lives(){return mario.get_lives();}
  public Map get_map() {return map;}
  public Mario get_mario() {return mario;}

  public void update() {
    synchronized (mario) {
      check_new_game();
      mario.update(map.get_x(), map.get_blocks(), map.get_troopas(),
          map.get_beetles(), map.get_plants(), map.get_power_ups());
      if(mario.get_fireball()!=null) {
        if(mario.get_fireball().update(map.get_blocks(), map.get_plants(),
            map.get_troopas()) || (mario.get_fireball().get_x() >
            map.get_x() + viewWidth) ||
            mario.get_fireball().get_x() < map.get_x()) {
          mario.delete_fireball();
        }
      }
      map.update(mario.get_x());
      gameScore = mario.get_score_();
    }
  }

  public void check_new_game(){
    if(mario.get_x() > map.get_image().getWidth() - 210 || mario.get_y() >
        map.get_image().getHeight() || reset_game || mario.get_hit_enemy()) {
      if(mario.get_x() > map.get_image().getWidth() - 210 ){
        currentLevel += 1;
        if(currentLevel>2){
          game = false;
          mario.set_starting_mario_pos();
          return;
        }
        map = levels[currentLevel];
        mario.set_hit_enemy(false);
        map.set_map_at_start();
        mario.set_starting_mario_pos();
      }
      else if(reset_game){
        currentLevel = 0;
        map = levels[currentLevel];
        for(int i = 0; i < 3; i++) {
          map.load_level(i);
        }
        mario.set_hit_enemy(false);
        map.set_map_at_start();
        mario.set_starting_mario_pos();
        mario.set_mario_small();
        mario.reset_score();
      }
      else if(!reset_game ) {
        mario.decrement_lives();
        mario.set_hit_enemy(false);
        mario.set_ascending(false);
        mario.set_jump(false);
        map.set_map_at_start();
        mario.set_starting_mario_pos();
        mario.set_mario_small();
        map.load_level(currentLevel);
      }

    }
      if(mario.get_lives() == 0)
      {
        game = false;
      }

    reset_game = false;
  }


  public boolean process_input(MotionEvent e) {
    double x = e.getX();
    double y = e.getY();
    if (e.getAction() == MotionEvent.ACTION_DOWN) {
      //circle buttons
      if (y >= viewHeight * .7 && y <= viewHeight * .83) {
        if (x > viewWidth * .69 && x < viewWidth * .76) {
          mario.update_movement(true);
          mario.update_direction("left");
        }
        if (x > viewWidth * .83 && x < viewWidth * .9) {
          mario.update_movement(true);
          mario.update_direction("right");
        }
      }//up
      if (x >= viewWidth * .76 && x <= viewWidth * .83) {
        if (y > viewHeight * .57 && y < viewHeight * .7) {
          if (!mario.get_jump()) {
            mario.set_jump_and_ascending(true);
          }
        }//down
        if (y > viewHeight * .83 && y < viewHeight * .96) {
          mario.crouch();
        }
      }
      //square buttons//right
      if (x >= (viewWidth * .17) && x <= (viewWidth * (.17 + .12 / ratio))) {
        if (y >= viewHeight * .85 && y <= (viewHeight * .97)) {
          System.out.println("yoyo " + mario.get_fire_mario());
          if (mario.get_fire_mario()) {
            if (!mario.has_fireball()) {
              mario.make_fireball();
            }
          }
        }
      }//left
      if (x >= (viewWidth * .05) && x <= (viewWidth *(.05 + .12 / ratio))) {
        if (y >= viewHeight * .85 && y <= (viewHeight * .97)) {
          if (!mario.get_jump() && mario.get_object_below()) {
            mario.set_jump_and_ascending(true);
          }
        }
      }
      if(x >= viewWidth * .94 && y>= 0 && x<=viewWidth *(.94+.12/ratio) &&
          y <= viewHeight *.12 ){
        reset_game = true;
        mario.reset_lives();
        game = true;
      }
      return true;
    } else if (e.getAction() == MotionEvent.ACTION_UP) {
      mario.update_movement(false);
    }
    return false;
  }
}
