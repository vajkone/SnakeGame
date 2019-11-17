package com.dcs.snakegame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dcs.snakegame.GameEngine.GameEngine;
import com.dcs.snakegame.enums.Direction;
import com.dcs.snakegame.enums.GameState;
import com.dcs.snakegame.views.SnakeView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private GameEngine gameEngine;
    private SnakeView snakeView;
    private final Handler handler = new Handler();
    private final long updateDelay = 125;
    private Button retryButton;

    private float prevX, prevY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retryButton=(Button)findViewById(R.id.retry);

        retryButton.setVisibility(View.GONE);



        gameEngine = new GameEngine();
        gameEngine.initGame();

        snakeView = (SnakeView)findViewById(R.id.snakeView);
        snakeView.setOnTouchListener(this);

        startUpdateHandler();


    }

    public void retryClick(View target){
        recreate();
    }

    private void startUpdateHandler(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameEngine.Update();

                if (gameEngine.getCurrentGameState() == GameState.Running){
                    handler.postDelayed(this,updateDelay);
                }
                if (gameEngine.getCurrentGameState()==GameState.Lost){
                   OnGameLost();
                }
                snakeView.setSnakeViewMap(gameEngine.getMap());
                snakeView.invalidate();
            }
        }, updateDelay);
    }

    private void OnGameLost(){
        Toast.makeText(this,"You lost.",Toast.LENGTH_LONG).show();
        retryButton.setVisibility(View.VISIBLE);
    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:

                prevX=motionEvent.getX();
                prevY=motionEvent.getY();


                break;
            case MotionEvent.ACTION_UP:

                float newX=motionEvent.getX();
                float newY=motionEvent.getY();

                if (Math.abs(newX-prevX)>Math.abs(newY-prevY)){
                    //LEFT-RIGHT DIR
                    if(newX>prevX){
                        //right
                        gameEngine.UpdateDirection(Direction.East);
                    }else{
                        //left
                        gameEngine.UpdateDirection(Direction.West);
                    }
                }else{
                    //UP - DOWN DIR
                    if (newY>prevY){
                        //down - mert fentről nő lefelé
                        gameEngine.UpdateDirection(Direction.South);
                    }else{
                        //up
                        gameEngine.UpdateDirection(Direction.North);
                    }

                }

                break;

        }

        return true;
    }
}
