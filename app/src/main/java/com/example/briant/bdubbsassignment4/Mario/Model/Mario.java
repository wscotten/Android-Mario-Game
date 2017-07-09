package com.example.briant.bdubbsassignment4.Mario.Model;

import com.example.briant.bdubbsassignment4.Mario.View.GameView;

import java.util.Date;

/**
 * Created by Lil Uzi on 5/24/2017.
 */

public class Mario extends GameObject {
  private  Date date;
  private long time_since_hit_enemy = 0;
  private int timeConstant = 1000;
  private int speed = 0;
  private int baseHeight;
  private boolean moving = false;
  private boolean jumping;
  private boolean ascending;
  private boolean objectBelow;
  private boolean hitEnemy = false;
  private boolean superMario = false;
  private boolean fireMario = false;
  private int jumpSpeed = 40;
  private int animation = 1;
  private int start_x;
  private int start_y;
  private int y_at_start_of_jump;
  private String direction;
  private int lives = 3;
  private int score = 0;
  private Fireball fireball;



  public Mario(int x, int y, int width, int height) {
    this.start_x = x;
    this.start_y = y;
    set_starting_mario_pos();
    y_at_start_of_jump = y;
    direction = "left";
    this.width = width;
    baseHeight = height;
    this.height = height;
    fireball = null;
  }
  public void set_starting_mario_pos(){
    this.x = start_x;
    this.y = start_y;
  }
  public void set_mario_small(){fireMario = false;
    superMario=false;
    height = baseHeight;
    y = this.start_y;
  }

  public boolean has_fireball(){
    if(fireball!=null){
      return true;
    }
    return false;
  }
  public int get_score_(){return this.score;}
  public void reset_score(){score = 0;}
  public boolean get_hit_enemy(){return hitEnemy;}
  public void set_hit_enemy(boolean hit){hitEnemy = hit;}
  public int get_lives(){return lives;}
  public boolean get_jump() {return jumping;}
  public boolean get_object_below(){return objectBelow;}
  public void update_direction(String d) {direction = d;}
  public void decrement_lives(){this.lives -=1;}
  public void reset_lives(){this.lives=3;}
  public Fireball get_fireball(){return this.fireball;}
  public void delete_fireball(){fireball = null;}

  public boolean get_super_mario(){return superMario;}
  public boolean get_fire_mario(){return fireMario;}
  public void set_jump(boolean j) {jumping = j;}
  public void set_ascending(boolean a) {ascending = a;}
  public void set_object_below(boolean o) {objectBelow = o;}


  private void stop_and_prevent_overlap(String direction, int overlap) {
    if(direction == "below") {
      if(!ascending) {
        y -= overlap;
        jumping = false;
      }
      objectBelow = true;
    } else if(direction == "above") {
      if(overlap <= jumpSpeed) {
        y += overlap;
        ascending = false;
      }
    } else if(direction == "left") {
      x -= overlap;
    } else if(direction == "right") {
      x += overlap;
    }
  }


  public void update_movement(boolean m) {
    moving = m;
    if (!m) animation = 1;
  }

  public void set_jump_and_ascending(boolean jump) {
    this.jumping = jump;
    this.ascending = true;
  }

  public void update(int distanceInLevel, Block[] blocks, Troopa[] troopas,
      Beetle[] beetles, Plant[] plants, PowerUps[] powerUps) {
    synchronized (this) {
      update_ypos(troopas, beetles, plants);
      update_xpos(distanceInLevel, troopas, beetles, plants);
      check_unit_collision(blocks);
      check_unit_collision(powerUps);
    }
  }

  private void update_xpos(int distanceInLevel, Troopa[] troopas,
      Beetle[] beetles, Plant[] plants) {
    if (direction == "left") {
      if (moving) {
        speed = speed < 50 ? speed + 5 : 50;
      } else {
        if (speed > 0) {
          speed -= 5;
        }
        if (speed < 0) {
          speed += 5;
        }
      }
    } else {
      if (moving) {
        speed = speed > -50 ? speed - 5 : -50;
      } else {
        if (speed > 0) {
          speed -= 5;
        }
        if (speed < 0) {
          speed += 5;
        }
      }
    }
    if ((speed > 0 && x < 14380) ||
        ( speed < 0 && x > 50 && x > distanceInLevel + 170)) {
      hitEnemy = check_if_hits_troopa('x', x + speed, troopas);
      hitEnemy = check_if_hits_beetle('x', x + speed, beetles);
      check_if_hits_plant('x', x + speed, plants);
      x += speed;
    } else if (x + speed > 14400) {
      speed = 0;
      x = 14400;
    } else if (x + speed < distanceInLevel + width) {
      speed = 0;
      x = distanceInLevel + width;
    }
  }

  private void update_ypos(Troopa[] troopas, Beetle[] beetles, Plant[] plants) {
    if(!jumping){
      y_at_start_of_jump = y;
    }
    if (ascending) {
      if(y < jumpSpeed * 2) {
        ascending = false;
        return;
      }
      if (y > (y_at_start_of_jump - 500)) {
        hitEnemy = check_if_hits_troopa('y', y - jumpSpeed, troopas);
        hitEnemy = check_if_hits_beetle('y', y - jumpSpeed, beetles);
        check_if_hits_plant('y', y - jumpSpeed, plants);
        y -= jumpSpeed;
      } else {
        ascending = false;
      }
    } else if(!objectBelow) {
      check_if_hits_plant('y', y + jumpSpeed, plants);
      if(check_if_hits_troopa('y', y + jumpSpeed, troopas) ||
          check_if_hits_beetle('y', y + jumpSpeed, beetles)) {
        y_at_start_of_jump = y;
        speed = 0;
        ascending = true;
        y -= 150;
      } else {
        y += jumpSpeed;
      }
    }
  }

  private boolean check_if_hits_troopa(char axis, int num, Troopa[] troopas) {
    if(hitEnemy) {
      return true;
    }
    if(axis == 'x') {
      int x = num;
    } else {
      int y = num;
    }
    Date date = new Date();
    for (int i = 0; i < troopas.length; i++) {
      if (troopas[i] != null) {
        if(Math.abs(troopas[i].get_x() - x) <= troopas[i].get_width() &&
            Math.abs(troopas[i].get_y() - y) <= troopas[i].get_height()) {
          if (date.getTime() > time_since_hit_enemy + timeConstant) {
            time_since_hit_enemy = date.getTime();
            if(!ascending && axis == 'y') {
              if(troopas[i].get_jumped_on()) {
                troopas[i] = null;
                score += 1000;
                break;
              } else {
                troopas[i].set_jumped_on();
              }
            }
            if (superMario || fireMario) {
              superMario = false;
              fireMario = false;
              height = baseHeight;
              y += baseHeight;
            } else {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  private boolean check_if_hits_beetle(char axis, int num, Beetle[] beetles) {
    if(hitEnemy) {
      return true;
    }
    if(axis == 'x') {
      int x = num;
    } else {
      int y = num;
    }
    Date date = new Date();
    for (int i = 0; i < beetles.length; i++) {
      if (beetles[i] != null) {
        if(Math.abs(beetles[i].get_x() - x) <= beetles[i].get_width() &&
            Math.abs(beetles[i].get_y() - y) <= beetles[i].get_height()) {
          if (date.getTime() > time_since_hit_enemy + timeConstant) {
            time_since_hit_enemy = date.getTime();
            if(!ascending && axis == 'y') {
              beetles[i] = null;
              score += 1000;
            }
            if (superMario || fireMario) {
              superMario = false;
              fireMario = false;
              height = baseHeight;
              y += baseHeight;
            } else {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  private void check_if_hits_plant(char axis, int num, Plant[] plants) {
    if(axis == 'x') {
      int x = num;
    } else {
      int y = num;
    }
    Date date = new Date();
    for (int i = 0; i < plants.length; i++) {
      if (plants[i] != null) {
        if(Math.abs(plants[i].get_x() - x) <= plants[i].get_width() &&
            Math.abs(plants[i].get_y() - y) <= plants[i].get_height()) {
          if (date.getTime() > time_since_hit_enemy + timeConstant) {
            time_since_hit_enemy = date.getTime();
            if (superMario || fireMario) {
              superMario = false;
              fireMario = false;
              height = baseHeight;
              y += baseHeight;
            } else {
              hitEnemy = true;
            }
          }
        }
      }
    }
  }

  private void check_unit_collision(Block[] blocks) {
    synchronized (this) {
      synchronized (blocks) {
        objectBelow = false;
        int overlapRight, overlapLeft, overlapBelow, overlapAbove;
        for (int i = 0; i < blocks.length; i++) {
          if (blocks[i] != null) {
            if ((x < blocks[i].get_x() &&
                x > blocks[i].get_x() - blocks[i].get_width()) ||
                (x - width < blocks[i].get_x()) &&
                    (x > blocks[i].get_x() - blocks[i].get_width())) {
              overlapAbove = blocks[i].get_y() + blocks[i].get_height() - y;
              overlapBelow = y + height - blocks[i].get_y();
              if (overlapAbove > 0 && blocks[i].get_y() < y) {
                stop_and_prevent_overlap("above", overlapAbove);
                if (blocks[i] instanceof QuestionBlock) {
                  ((QuestionBlock) blocks[i]).hit_block();
                } else if (blocks[i].get_block_type() == 1) {
                  if (fireMario || superMario) {
                    blocks[i] = null;
                    ascending = false;
                    continue;
                  }
                }

              } else if (overlapBelow > 0 && y < blocks[i].get_y()) {
                stop_and_prevent_overlap("below", overlapBelow);
              }
            }
            if ((y >= blocks[i].get_y() &&
                y < blocks[i].get_y() + blocks[i].get_height()) ||
                (y + height > blocks[i].get_y() &&
                    y + height < blocks[i].get_y() + blocks[i].get_height())) {
              overlapLeft = x + width - blocks[i].get_x();
              overlapRight = blocks[i].get_x() + width - x;
              if (overlapLeft > 0 && x < blocks[i].get_x()) {
                stop_and_prevent_overlap("left", overlapLeft);
              } else if (overlapRight > 0 && blocks[i].get_x() < x) {
                stop_and_prevent_overlap("right", overlapRight);
              }
            }
          }
        }
      }
    }
  }

  public void check_unit_collision(PowerUps[] powerUps){
    synchronized (powerUps) {
      objectBelow = false;
      int overlapRight, overlapLeft, overlapBelow, overlapAbove;
      for (int i = 0; i < powerUps.length; i++) {
        if (powerUps[i] != null) {
          if ((x < powerUps[i].get_x() &&
              x > powerUps[i].get_x() - powerUps[i].get_width()) ||
              (x - width < powerUps[i].get_x()) &&
                  (x > powerUps[i].get_x() - powerUps[i].get_width())) {
            overlapAbove = powerUps[i].get_y() + powerUps[i].get_height() - y;
            overlapBelow = y + height - powerUps[i].get_y();
            if (overlapAbove > 0 && powerUps[i].get_y() < y ||
                overlapBelow > 0 && y < powerUps[i].get_y()) {
              if (powerUps[i].get_has_been_revealed()) {
                powerUps[i].hit_power_up();
                if (powerUps[i].get_power_up_type() == 1) {
                  if (!superMario && !fireMario) {
                    y -= baseHeight;
                  }
                  superMario = true;
                  height = 2 * baseHeight;
                } else if (powerUps[i].get_power_up_type() == 2) {
                  if (!superMario && !fireMario) {
                    y -= baseHeight;
                  }
                  superMario = false;
                  fireMario = true;
                  height = 2 * baseHeight;
                }
                if (powerUps[i].get_power_up_type() == 0) {
                  score += 200;
                } else {
                  score += 1000;
                }
                powerUps[i] = null;
              }

            }
          }
          if (powerUps[i] != null) {
            if ((y > powerUps[i].get_y() &&
                y < powerUps[i].get_y() + powerUps[i].get_height()) ||
                (y + height > powerUps[i].get_y() &&
                y + height < powerUps[i].get_y() + powerUps[i].get_height())) {
              overlapLeft = x + width - powerUps[i].get_x();
              overlapRight = powerUps[i].get_x() + width - x;
              if ((overlapLeft > 0 && x < powerUps[i].get_x()) ||
                  (overlapRight > 0 && powerUps[i].get_x() < x)) {
                if (powerUps[i].get_has_been_revealed()) {
                  powerUps[i].hit_power_up();
                  if (powerUps[i].get_power_up_type() == 1) {
                    if (!superMario && !fireMario) {
                      y -= baseHeight;
                    }
                    superMario = true;
                    height = 2 * baseHeight;
                  } else if (powerUps[i].get_power_up_type() == 2) {
                    if (!superMario && !fireMario) {
                      y -= baseHeight;
                    }
                    superMario = false;
                    fireMario = true;
                    height = 2 * baseHeight;
                  }
                  if (powerUps[i].get_power_up_type() == 0) {
                    score += 200;
                  } else {
                    score += 1000;
                  }
                  powerUps[i] = null;
                }

              }
            }
          }
        }
      }
    }
  }

  public String get_image(int distanceInLevel) {
    String powerup = "";
    if(fireMario){
      powerup = "fire_";
    }
    else if(superMario){
      powerup = "super_";
    }
    if (jumping) {
      return powerup + "jump_" + direction;
    } else if ((speed > 0 && x < 14380) ||
        (speed < 0 && x > 50 && x > distanceInLevel + 170)) {
      animation = animation > 2 ? 1 : ++animation;
      return powerup + "moving_" + direction + "_" + animation;
    } else {
      return powerup + "still_" + direction;
    }
  }

  public void make_fireball(){
      int fireballWidth = 50;
      fireball = new Fireball(x + fireballWidth, y, fireballWidth,
          fireballWidth,width, height, direction);
  }

  public void crouch() {
    // only when Mario big boy
  }
}
