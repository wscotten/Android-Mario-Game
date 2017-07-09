package com.example.briant.bdubbsassignment4.Mario.Controller;

import android.graphics.Canvas;
import android.provider.Settings;
import android.view.SurfaceHolder;

import com.example.briant.bdubbsassignment4.Mario.View.GameView;

/**
 * Created by Lil Uzi on 5/10/2017.
 */

public class GameThread extends Thread {
  // don't need to look at this file either weeb,
  // just GameView and GameController
  private GameView view;
  private GameController controller;

  public GameThread(GameController controller, GameView view) {
    this.view = view;
    this.controller = controller;
  }

  public void run() {
    SurfaceHolder sh = view.getHolder();
    while (true) {
      Canvas canvas = sh.lockCanvas();
      if (canvas != null) {
        controller.update();
        view.draw(canvas);
        sh.unlockCanvasAndPost(canvas);
      }
    }
  }
}
