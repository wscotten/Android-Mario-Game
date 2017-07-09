package com.example.briant.bdubbsassignment4.Mario.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashMap;

import com.example.briant.bdubbsassignment4.Mario.Controller.GameController;
import com.example.briant.bdubbsassignment4.Mario.Model.Block;
import com.example.briant.bdubbsassignment4.Mario.Model.Fireball;
import com.example.briant.bdubbsassignment4.Mario.Model.Map;
import com.example.briant.bdubbsassignment4.Mario.Model.PowerUps;
import com.example.briant.bdubbsassignment4.Mario.Model.QuestionBlock;
import com.example.briant.bdubbsassignment4.Mario.Model.Troopa;
import com.example.briant.bdubbsassignment4.Mario.Model.Beetle;
import com.example.briant.bdubbsassignment4.Mario.Model.Plant;
import com.example.briant.bdubbsassignment4.Mario.R;
import com.example.briant.bdubbsassignment4.Mario.Controller.GameThread;

/**
 * Created by Lil Uzi on 5/24/2017.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
  private Bitmap currentMapImage;
  private Bitmap currentView;
  private Bitmap mario;
  private Bitmap[] mapImages; //14511x1313
  private Bitmap[] blockImages;
  private Bitmap[] questionBlockImages;
  private Bitmap[] powerUpImages;
  private Bitmap[] fireballImages;
  private HashMap<String, Bitmap> marioImages;
  private HashMap<String, Bitmap> troopaImages;
  private HashMap<String, Bitmap> beetleImages;
  private HashMap<String, Bitmap> plantImages;
  private HashMap<String, Bitmap> superMarioImages;
  private HashMap<String, Bitmap> fireMarioImages;

  private GameController controller;
  private Rect rect;

  private int viewWidth; //1810
  private int viewHeight; //891
  private int currentLevel;
  private Map[] levels;

  public void draw(Canvas c) {
    super.draw(c);
    if(controller.get_level() < 3 && controller.get_game()) {
      currentMapImage = mapImages[controller.get_level()];
      currentLevel = controller.get_level();
      draw_map(c);
      draw_enemies(c);
      draw_blocks(c);
      draw_mario(c);
      draw_fireball(c);
    }
  else {
      c.drawColor(Color.BLACK); // Set the background to black
      Paint p = new Paint();
      p.setARGB(255, 255, 255, 255);
      p.setTextSize((int) (viewWidth * .05));
      c.drawText("Press start for new game", (int) (viewWidth * .1),
          viewHeight / 2, p);

    }
    draw_game_pad(c);
  }

  void set_rect(int leftX, int upY, int rightX, int downY) {
    rect.set(viewWidth - leftX + controller.get_map().get_x(),
        (int) ((double) upY * viewHeight) / currentMapImage.getHeight(),
        viewWidth - rightX + controller.get_map().get_x(),
        (int) ((double) downY * viewHeight) / currentMapImage.getHeight());
  }

  public void draw_fireball(Canvas c){
    Fireball fireball = controller.get_mario().get_fireball();
    if(fireball!= null) {
      set_rect(fireball.get_x(), fireball.get_y(),
          fireball.get_x() - fireball.get_width(),
          fireball.get_y() + fireball.get_height());
      c.drawBitmap(fireballImages[0], null, rect, null);
    }
  }
  public void draw_map(Canvas c) {
    int distance = controller.get_map().get_x();
    rect.set(0, 0, viewWidth, viewHeight);
    currentView = Bitmap.createBitmap(currentMapImage,
        currentMapImage.getWidth() - viewWidth - distance, 0,
        viewWidth, currentMapImage.getHeight());
    c.drawBitmap(currentView, null, rect, null);
    for (int i = 0; i < controller.get_lives(); i++) {
      rect.set((int) (viewWidth * (i * .06)), 0,
          (int) (viewWidth * (i + 1) * .06), (int) (viewHeight * .10));
      c.drawBitmap(marioImages.get("still_left"), null, rect, null);
    }
    Paint p = new Paint();
    p.setARGB(255, 255, 255, 255);
    p.setTextSize((int) (viewWidth * .05));
    Integer game_score = new Integer(controller.get_game_score());
    String stringScore = "Score " + (game_score.toString());

    c.drawText(stringScore, (int) (viewWidth * .3), 90, p);

  }

  public void draw_blocks(Canvas c) {
    synchronized (levels[currentLevel].get_blocks()) {
      Block blocks[] = levels[currentLevel].get_blocks();
      PowerUps powerUps[] = levels[currentLevel].get_power_ups();
      int level_progress = controller.get_map().get_x();
      for (int i = 0; i < blocks.length; i++) {
        if (blocks[i] != null && blocks[i].get_x() >= level_progress &&
            blocks[i].get_x() - blocks[i].get_width() -
                viewWidth <= level_progress) {
          set_rect(blocks[i].get_x(), blocks[i].get_y(),
              blocks[i].get_x() - blocks[i].get_width(),
              blocks[i].get_y() + blocks[i].get_height());
          c.drawBitmap(blockImages[blocks[i].get_block_type()], null, rect,
              null);

          if (blocks[i] != null && blocks[i] instanceof QuestionBlock) {
            if (((QuestionBlock) blocks[i]).get_has_been_hit()){
              //draw the power up corresponding to the question block
              //the index in powerup[] is a field of question block
              c.drawBitmap(questionBlockImages[1],
                  null, rect, null);
              PowerUps tempPowerup = powerUps[((QuestionBlock) blocks[i])
                  .get_power_up_index()];
              if(tempPowerup
                  != null) {
                tempPowerup.reveal_power_up();
                if (!tempPowerup.get_has_been_hit()) {
                  set_rect(tempPowerup.get_x(), tempPowerup.get_y(),
                      tempPowerup.get_x() -tempPowerup.get_width(),
                      tempPowerup.get_y() + tempPowerup.get_height());
                  //powerup[index].getpoweruptype is which image to draw
                  c.drawBitmap(
                      powerUpImages[tempPowerup.get_power_up_type()],
                      null, rect, null);
                }
              }

            }
          }
        }

      }
    }
  }

  public void draw_enemies(Canvas c) {
    currentLevel = controller.get_level();
    Troopa troopas[] = levels[currentLevel].get_troopas();
    Beetle beetles[] = levels[currentLevel].get_beetles();
    Plant plants[] = levels[currentLevel].get_plants();
    int level_progress = controller.get_map().get_x();
    for (int i = 0; i < troopas.length; i++) {
      if (troopas[i] != null && troopas[i].get_x() >= level_progress &&
          troopas[i].get_x() - troopas[i].get_width() -
              viewWidth <= level_progress) {
        set_rect(troopas[i].get_x(), troopas[i].get_y(),
            troopas[i].get_x() - troopas[i].get_width(),
            troopas[i].get_y() + troopas[i].get_height());
        c.drawBitmap(troopaImages.get(troopas[i].get_image()), null, rect,
            null);
      }
    }
    for (int i = 0; i < beetles.length; i++) {
      if (beetles[i] != null && beetles[i].get_x() >= level_progress &&
          beetles[i].get_x() - beetles[i].get_width() -
              viewWidth <= level_progress) {
        set_rect(beetles[i].get_x(), beetles[i].get_y(),
            beetles[i].get_x() - beetles[i].get_width(),
            beetles[i].get_y() + beetles[i].get_height());
        c.drawBitmap(beetleImages.get(beetles[i].get_image()), null, rect,
            null);
      }
    }
    for (int i = 0; i < plants.length; i++) {
      if (plants[i] != null && plants[i].get_x() >= level_progress &&
          plants[i].get_x() - plants[i].get_width() -
              viewWidth <= level_progress) {
        set_rect(plants[i].get_x(), plants[i].get_y(),
            plants[i].get_x() - plants[i].get_width(),
            plants[i].get_y() + plants[i].get_height());
        c.drawBitmap(plantImages.get(plants[i].get_image()), null, rect,
            null);
      }
    }
  }

  public void draw_mario(Canvas c) {
    int distanceInLevel = controller.get_map().get_x();
    set_rect(controller.mario.get_x(), controller.mario.get_y(),
        controller.mario.get_x() - controller.mario.get_width(),
        controller.mario.get_y() + controller.mario.get_height());
    c.drawBitmap(marioImages.get(controller.mario.get_image(distanceInLevel)),
        null, rect, null);
  }

  public void draw_game_pad(Canvas c) {
    Paint p = new Paint();
    p.setStyle(Paint.Style.FILL);
    p.setStrokeJoin(Paint.Join.ROUND);
    p.setStrokeCap(Paint.Cap.ROUND);
    p.setColor(Color.rgb(227, 222, 219));

    c.drawCircle((int) (viewWidth * .8), (int) (viewHeight * .65), 65, p);
    c.drawCircle((int) (viewWidth * .8), (int) (viewHeight * .9), 65, p);
    c.drawCircle((int) (viewWidth * .87), (int) (viewHeight * .775), 65, p);
    c.drawCircle((int) (viewWidth * .73), (int) (viewHeight * .775), 65, p);

    double ratio = viewWidth / viewHeight;
    c.drawRect((int) (viewWidth * .05), (int) (viewHeight * .85),
        (int) (viewWidth * (.05 + .12 / ratio)), (int) (viewHeight * .97), p);
    c.drawRect((int) (viewWidth * .17), (int) (viewHeight * .85),
        (int) (viewWidth * (.17 + .12 / ratio)), (int) (viewHeight * .97), p);
    c.drawRect((int) (viewWidth * .94), 0,
        (int) (viewWidth * (.94 + .12 / ratio)), (int) (viewHeight * .12), p);
  }

  public void surfaceCreated(SurfaceHolder holder) {
    rect = new Rect();
    viewWidth = getWidth();
    viewHeight = getHeight();
    currentLevel = 0;
    make_mario_images();
    make_block_images();
    make_map_images();
    make_troopa_images();
    make_beetle_images();
    make_plant_images();
    make_power_up_images();
    controller = new GameController(this, viewWidth, viewHeight, levels);
  }

  private void make_mario_images() {
    marioImages = new HashMap<String, Bitmap>();
    superMarioImages = new HashMap<String, Bitmap>();
    fireMarioImages = new HashMap<String, Bitmap>();

    marioImages.put("still_left", BitmapFactory.decodeResource(
        getResources(), R.drawable.mario_still_left));
    marioImages.put("still_right", BitmapFactory.decodeResource(
        getResources(), R.drawable.mario_still_right));
    marioImages.put("moving_left_1", BitmapFactory.decodeResource(
        getResources(), R.drawable.mario_moving_left_1));
    marioImages.put("moving_left_2", BitmapFactory.decodeResource(
        getResources(), R.drawable.mario_moving_left_2));
    marioImages.put("moving_left_3", BitmapFactory.decodeResource(
        getResources(), R.drawable.mario_moving_left_3));
    marioImages.put("moving_right_1", BitmapFactory.decodeResource(
        getResources(), R.drawable.mario_moving_right_1));
    marioImages.put("moving_right_2", BitmapFactory.decodeResource(
        getResources(), R.drawable.mario_moving_right_2));
    marioImages.put("moving_right_3", BitmapFactory.decodeResource(
        getResources(), R.drawable.mario_moving_right_3));
    marioImages.put("jump_left", BitmapFactory.decodeResource(
        getResources(), R.drawable.mario_jump_left));
    marioImages.put("jump_right", BitmapFactory.decodeResource(
        getResources(), R.drawable.mario_jump_right));


    marioImages.put("super_still_left", BitmapFactory.decodeResource(
        getResources(), R.drawable.super_mario_still_left));
    marioImages.put("super_still_right", BitmapFactory.decodeResource(
        getResources(), R.drawable.super_mario_still_right));
    marioImages.put("super_moving_left_1", BitmapFactory.decodeResource(
        getResources(), R.drawable.super_mario_left_1));
    marioImages.put("super_moving_left_2", BitmapFactory.decodeResource(
        getResources(), R.drawable.super_mario_left_2));
    marioImages.put("super_moving_left_3", BitmapFactory.decodeResource(
        getResources(), R.drawable.super_mario_left_3));
    marioImages.put("super_moving_right_1", BitmapFactory.decodeResource(
        getResources(), R.drawable.super_mario_right_1));
    marioImages.put("super_moving_right_2", BitmapFactory.decodeResource(
        getResources(), R.drawable.super_mario_right_2));
    marioImages.put("super_moving_right_3", BitmapFactory.decodeResource(
        getResources(), R.drawable.super_mario_right_3));
    marioImages.put("super_jump_left", BitmapFactory.decodeResource(
        getResources(), R.drawable.super_mario_jump_left));
    marioImages.put("super_jump_right", BitmapFactory.decodeResource(
        getResources(), R.drawable.super_mario_jump_right));


    marioImages.put("fire_still_left", BitmapFactory.decodeResource(
        getResources(), R.drawable.fire_mario_still_left));
    marioImages.put("fire_still_right", BitmapFactory.decodeResource(
        getResources(), R.drawable.fire_mario_still_right));
    marioImages.put("fire_moving_left_1", BitmapFactory.decodeResource(
        getResources(), R.drawable.fire_mario_left_1));
    marioImages.put("fire_moving_left_2", BitmapFactory.decodeResource(
        getResources(), R.drawable.fire_mario_left_2));
    marioImages.put("fire_moving_left_3", BitmapFactory.decodeResource(
        getResources(), R.drawable.fire_mario_left_3));
    marioImages.put("fire_moving_right_1", BitmapFactory.decodeResource(
        getResources(), R.drawable.fire_mario_right_1));
    marioImages.put("fire_moving_right_2", BitmapFactory.decodeResource(
        getResources(), R.drawable.fire_mario_right_2));
    marioImages.put("fire_moving_right_3", BitmapFactory.decodeResource(
        getResources(), R.drawable.fire_mario_right_3));
    marioImages.put("fire_jump_left", BitmapFactory.decodeResource(
        getResources(), R.drawable.fire_mario_jump_left));
    marioImages.put("fire_jump_right", BitmapFactory.decodeResource(
        getResources(), R.drawable.fire_mario_jump_right));

    fireballImages = new Bitmap[2];
    fireballImages[0] = BitmapFactory.decodeResource(
        getResources(), R.drawable.fireball_1);
  }

  public void make_power_up_images() {
    powerUpImages = new Bitmap[3];
    powerUpImages[0] = BitmapFactory.decodeResource(
        getResources(), R.drawable.coin_3);
    powerUpImages[1] = BitmapFactory.decodeResource(
        getResources(), R.drawable.mushroom_large);
    powerUpImages[2] = BitmapFactory.decodeResource(
        getResources(), R.drawable.flower_red);
  }

  public void make_block_images() {
    blockImages = new Bitmap[8];
    questionBlockImages = new Bitmap[2];
    blockImages[0] = BitmapFactory.decodeResource(
        getResources(), R.drawable.block_ground);
    blockImages[1] = BitmapFactory.decodeResource(
        getResources(), R.drawable.breakable_block);
    blockImages[2] = BitmapFactory.decodeResource(
        getResources(), R.drawable.block);
    blockImages[3] = BitmapFactory.decodeResource(
        getResources(), R.drawable.tube_small);
    blockImages[4] = BitmapFactory.decodeResource(
        getResources(), R.drawable.tube_medium);
    blockImages[5] = BitmapFactory.decodeResource(
        getResources(), R.drawable.tube_tall);
    blockImages[6] = BitmapFactory.decodeResource(
        getResources(), R.drawable.question_box_lit);
    questionBlockImages[0] = BitmapFactory.decodeResource(
        getResources(), R.drawable.question_box_lit);
    questionBlockImages[1] = BitmapFactory.decodeResource(
        getResources(), R.drawable.question_box_depleted);

  }

  public void make_map_images() {
    //maps = images, levels = map objects
    mapImages = new Bitmap[3];
    mapImages[0] = BitmapFactory.decodeResource(
        getResources(), R.drawable.map1);
    mapImages[1] = BitmapFactory.decodeResource(
        getResources(), R.drawable.map3);
    mapImages[2] = BitmapFactory.decodeResource(
        getResources(), R.drawable.map2);
    currentMapImage = mapImages[0];
    levels = new Map[3];
      for(int i = 0; i < 3;i++)
      {
      levels[i] = new Map(mapImages[i], i);
      }

  }

  public void make_troopa_images() {
    troopaImages = new HashMap<String, Bitmap>();
    troopaImages.put("green_left_1", BitmapFactory.decodeResource(
        getResources(), R.drawable.green_troopa_left_1));
    troopaImages.put("green_left_2", BitmapFactory.decodeResource(
        getResources(), R.drawable.green_troopa_left_2));
    troopaImages.put("green_right_1", BitmapFactory.decodeResource(
        getResources(), R.drawable.green_troopa_right_1));
    troopaImages.put("green_right_2", BitmapFactory.decodeResource(
        getResources(), R.drawable.green_troopa_right_2));
    troopaImages.put("green_hidden_0", BitmapFactory.decodeResource(
        getResources(), R.drawable.green_troopa_hidden_1));
    troopaImages.put("green_hidden_1", BitmapFactory.decodeResource(
        getResources(), R.drawable.green_troopa_hidden_2));

    troopaImages.put("red_left_1", BitmapFactory.decodeResource(
        getResources(), R.drawable.red_troopa_left_1));
    troopaImages.put("red_left_2", BitmapFactory.decodeResource(
        getResources(), R.drawable.red_troopa_left_2));
    troopaImages.put("red_right_1", BitmapFactory.decodeResource(
        getResources(), R.drawable.red_troopa_right_1));
    troopaImages.put("red_right_2", BitmapFactory.decodeResource(
        getResources(), R.drawable.red_troopa_right_2));
    troopaImages.put("red_hidden_0", BitmapFactory.decodeResource(
        getResources(), R.drawable.red_troopa_hidden_1));
    troopaImages.put("red_hidden_1", BitmapFactory.decodeResource(
        getResources(), R.drawable.red_troopa_hidden_2));
  }

  public void make_beetle_images() {
    beetleImages = new HashMap<String, Bitmap>();
    beetleImages.put("right_1", BitmapFactory.decodeResource(
        getResources(), R.drawable.beetle_right_1));
    beetleImages.put("right_2", BitmapFactory.decodeResource(
        getResources(), R.drawable.beetle_right_2));
    beetleImages.put("left_1", BitmapFactory.decodeResource(
        getResources(), R.drawable.beetle_left_1));
    beetleImages.put("left_2", BitmapFactory.decodeResource(
        getResources(), R.drawable.beetle_left_2));
  }

  public void make_plant_images() {
    plantImages = new HashMap<String, Bitmap>();
    plantImages.put("1", BitmapFactory.decodeResource(
        getResources(), R.drawable.plant_1));
    plantImages.put("2", BitmapFactory.decodeResource(
        getResources(), R.drawable.plant_2));
  }

  public boolean onTouchEvent(MotionEvent e) {
    return controller.process_input(e);
  }

  // no idea what any of this means stay above this part of the file
  public GameView(Context context) {
    super(context);
    getHolder().addCallback(this);
    setFocusable(true);
  }

  public void surfaceChanged(
      SurfaceHolder holder, int format, int width, int height) {
  }

  public void surfaceDestroyed(SurfaceHolder holder) {
  }
}
