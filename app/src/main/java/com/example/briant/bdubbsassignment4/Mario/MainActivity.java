package com.example.briant.bdubbsassignment4.Mario;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;

import com.example.briant.bdubbsassignment4.Mario.Controller.GameController;
import com.example.briant.bdubbsassignment4.Mario.View.GameView;

public class MainActivity extends AppCompatActivity implements
    SurfaceHolder.Callback {
  // no idea what any of this means dont worry about it stay out of  this file
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GameView view = new GameView(this);
    setContentView(view);
    view.getHolder().addCallback(this);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
  }

  public void surfaceCreated(SurfaceHolder holder) {
  }

  public void surfaceChanged(SurfaceHolder holder, int format, int width,
                             int height) {
  }

  public void surfaceDestroyed(SurfaceHolder holder) {
  }
}
