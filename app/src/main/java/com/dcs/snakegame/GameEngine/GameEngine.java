package com.dcs.snakegame.GameEngine;

import android.graphics.CornerPathEffect;

import com.dcs.snakegame.classes.Coordinate;
import com.dcs.snakegame.enums.Direction;
import com.dcs.snakegame.enums.GameState;
import com.dcs.snakegame.enums.TileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {

    public static final int gameWidth = 28;
    public static final int gameHeight = 42;

    private Random random = new Random();
    private boolean increaseTail = false;

    private List<Coordinate> walls = new ArrayList<>();
    private List<Coordinate> snake = new ArrayList<>();
    private List<Coordinate> apples = new ArrayList<>();

    private Direction currentDirection = Direction.North;

    private GameState currentGameState = GameState.Running;

    private Coordinate getSnakeHead(){
        return snake.get(0);

    }





    public GameEngine(){

    }

    public void initGame(){

        Addwalls();
        AddSnake();
        AddApple();

    }

    private void AddApple() {

        Coordinate coordinate = null;

        boolean added = false;

        while (!added){
            int x=1+random.nextInt(gameWidth-2);
            int y = 1+random.nextInt(gameHeight-2);

            coordinate=new Coordinate(x,y);
            boolean collision=false;
            for ( Coordinate s : snake){
                if (s.equals(coordinate)){
                    collision=true;
                    break;
                }
            }


            for (Coordinate a : apples){
                if (a.equals(coordinate)){
                    collision=true;
                    break;
                }
            }
            added=!collision;
        }

        apples.add(coordinate);

    }

    public void UpdateDirection(Direction newDirection){
        if (Math.abs(newDirection.ordinal()-currentDirection.ordinal())%2==1){
            currentDirection= newDirection;
        }
    }

    public void Update(){
        switch (currentDirection) {
            case North:
                UpdateSnake(0,-1);
                break;
            case East:
                UpdateSnake(1,0);
                break;
            case South:
                UpdateSnake(0,1);
                break;
            case West:
                UpdateSnake(-1,0);
                break;
        }

        //falbaütközés
        for (Coordinate w : walls){
            if(snake.get(0).equals(w)){
                currentGameState = GameState.Lost;
            }
        }

        //saját magunkba ütöközés

        for (Coordinate snakePart: snake){
            if (snakePart.equals((getSnakeHead()))){
                continue;
            }

        }

        for (int i = 1; i < snake.size(); i++) {

            if (getSnakeHead().equals(snake.get(i))){
                currentGameState=GameState.Lost;
                return;
            }
        }

        //almaevés

        Coordinate appletoRemove=null;
        for (Coordinate apple: apples){
            if (getSnakeHead().equals(apple)){
                appletoRemove=apple;
                increaseTail=true;

            }
        }
        if (appletoRemove!=null){
            apples.remove(appletoRemove);
            AddApple();
        }

    }

    private void UpdateSnake(int x, int y) {

        int newX = snake.get(snake.size()-1).getX();
        int newY = snake.get(snake.size()-1).getY();

        for (int i = snake.size()-1; i > 0; i--) {
            snake.get(i).setX(snake.get(i-1).getX());
            snake.get(i).setY(snake.get(i-1).getY());

        }

        if (increaseTail){
            snake.add(new Coordinate(newX,newY));
            increaseTail=false;
        }

        snake.get(0).setX(snake.get(0).getX()+x);
        snake.get(0).setY(snake.get(0).getY()+y);

    }

    private void AddSnake() {

        snake.clear();

        snake.add(new Coordinate(gameWidth/2,gameHeight/2));
        snake.add(new Coordinate(gameWidth/2-1,gameHeight/2));
        snake.add(new Coordinate(gameWidth/2-2,gameHeight/2));
        snake.add(new Coordinate(gameWidth/2-3,gameHeight/2));
    }

    public TileType[][] getMap(){
        TileType[][] map = new TileType[gameWidth][gameHeight];

        for (int x = 0; x < gameWidth; x++) {
            for (int y = 0; y < gameHeight; y++) {
                map[x][y]=TileType.Nothing;
            }
        }

        for (Coordinate wall: walls){
            map[wall.getX()][wall.getY()] = TileType.Wall;
        }

        for (Coordinate s : snake){
            map[s.getX()][s.getY()] = TileType.SnakeTail;

        }
        map[snake.get(0).getX()][snake.get(0).getY()]=TileType.SnakeHead;

        for (Coordinate a : apples){
            map[a.getX()][a.getY()]=TileType.Apple;
        }

        return map;
    }

    private void Addwalls() {

        //Top and bottom walls
        for (int x=0; x<gameWidth; x++){
            walls.add(new Coordinate(x,0));
            walls.add((new Coordinate(x,gameHeight-1)));
        }

        for (int y = 0; y < gameHeight; y++) {
            walls.add(new Coordinate(0,y));
            walls.add(new Coordinate(gameWidth-1,y));
        }

    }

    public GameState getCurrentGameState(){
        return currentGameState;
    }


}
