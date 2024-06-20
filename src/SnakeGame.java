import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener,KeyListener{
    
    //create a new class to keep track of all the x and y positions of the tile
    private class Tile {
        int x;
        int y;

        Tile (int x , int y) {
            this.x=x;
            this.y=y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    //snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food
    Tile food;
    Random random;

    //game logic
    Timer gameloop;
    int velocityx;
    int velocityy;
    boolean gameOver = false;
 
    SnakeGame(int boardWidth,int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
        setBackground(Color.black);
        //make game listen to key presses
        addKeyListener(this);
        setFocusable(true);


        snakeHead = new Tile(5,5); //default starting place
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityx=0;
        velocityy=0;

        gameloop = new Timer(100,this);
        gameloop.start();

    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //for easier visualization draw grids 
        for (int i =0;i<boardWidth/tileSize;i++) {
            g.drawLine(i*tileSize,0,i*tileSize,boardHeight); //vertical lines
            g.drawLine(0,i*tileSize,boardWidth,i*tileSize); //horizontal lines
        }

        //food
        g.setColor(Color.red);
        //g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);
        g.fill3DRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize,true);

        //Snake Head
        g.setColor(Color.green);
        //g.fillRect(snakeHead.x*tileSize,snakeHead.y*tileSize,tileSize,tileSize);
        g.fill3DRect(snakeHead.x*tileSize,snakeHead.y*tileSize,tileSize,tileSize,true);

        //Snake Body
        for (int i=0;i<snakeBody.size();i++) {
            Tile snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize,true);
        }

        //score
        g.setFont(new Font("Arial",Font.PLAIN,16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game over: " + String.valueOf(snakeBody.size()*10), tileSize-16, tileSize);
        }
        else {
            g.drawString("Score: "+String.valueOf(snakeBody.size()*10), tileSize-16, tileSize);
        }
        
    }

    public void placeFood () {
        //randomly set x and y coordinates of food
        //random number from 0-24
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    //function to detect collision btw snake body and food
    public boolean collision(Tile tile1 ,Tile tile2 ){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
 
    public void move() {
        //eat food 
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x,food.y));
            placeFood();
        }

        //snake body moves along with the head - move all the tiles with in the snakebody before you move the head
        //iterate backwards in the array list - each part copies the x & y positions of the tile before it
        for (int i = snakeBody.size()-1; i>=0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i==0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //snake haed
        //every 100ms updating x & y positions
        snakeHead.x += velocityx;
        snakeHead.y += velocityy;

        //game over conditions
        //1.Snake collides with its own body
        for (int i=0; i<snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        //2.Snake hits one of the four walls
        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth ||
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight) {
            gameOver = true;
        }


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //every 100ms we will call this(actionPerformed) over and overagain
        move();
        //repaint calls draw over and overagain
        repaint();
        if (gameOver) {
            gameloop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_UP && velocityy != 1) {
            velocityx=0;
            velocityy=-1;
        }
        else if (e.getKeyCode()==KeyEvent.VK_DOWN && velocityy != -1){
            velocityx=0;
            velocityy=1;
        }
        else if (e.getKeyCode()==KeyEvent.VK_RIGHT && velocityx != -1) {
            velocityx=1;
            velocityy=0;
        }
        else if (e.getKeyCode()==KeyEvent.VK_LEFT && velocityx != 1) {
            velocityx=-1;
            velocityy=0;
        }

    }

    //do not need these methods 
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
