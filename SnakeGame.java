import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class SnakeGame extends JPanel implements ActionListener,KeyListener{

    private class Tile{
        int x;
        int y;

        Tile(int x, int y){
            this.x=x;
            this.y=y;
        }
    }

    //snake              
    Tile snakeHead;
    ArrayList<Tile> snakebody;

    //food
    Tile food;

    Random random;

    //game logic
    Timer gameloop;
    int velocityX;
    int velocityY;

    boolean gameover=false;

    int boardwidth;
    int boardheight;
    int tileSize=25;

    SnakeGame(int boardwidth,int boardheight){
        this.boardwidth=boardwidth;
        this.boardheight=boardheight;
        setPreferredSize(new Dimension(this.boardwidth, this.boardheight));
        setBackground(Color.BLACK);

        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5,5);
        snakebody = new ArrayList<Tile>();

        food = new Tile(10,10);
        random=new Random();
        placeFood();

        velocityX=0;
        velocityY=1;

        gameloop=new Timer(200,this);
        gameloop.start();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        //grid
        // for(int i=0;i<boardwidth;i++){
        //     g.drawLine(i*tileSize, 0, i*tileSize, boardheight);
        //     g.drawLine(0, i*tileSize, boardwidth, i*tileSize);
        // }

        //food
        g.setColor(Color.red);
        g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);

        //snake head
        g.setColor(Color.yellow);
        g.fillRect(snakeHead.x * tileSize,snakeHead.y * tileSize,tileSize,tileSize);

        //snake body
        for (int i =0;i<snakebody.size();i++){
            Tile snakePart = snakebody.get(i);
            g.fillRect(snakePart.x*tileSize,snakePart.y*tileSize,tileSize,tileSize);
        }

        //score
        g.setFont(new Font("Arial",Font.PLAIN,16));
        if(gameover){
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakebody.size()), tileSize-16, tileSize);
        }
        else{
            g.drawString("Score: " + String.valueOf(snakebody.size()), tileSize-16, tileSize);
        }
    }

    public void placeFood(){
        food.x=random.nextInt(boardwidth/tileSize);
        food.y=random.nextInt(boardheight/tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x== tile2.x && tile1.y==tile2.y;
    }

    public void move(){

        //eat food
        if (collision(snakeHead,food)){
            snakebody.add(new Tile(food.x,food.y));
            placeFood();
        }

        //snake body move
        for(int i=snakebody.size()-1;i>=0;i--){
            Tile snakePart = snakebody.get(i);
            if(i==0){
                snakePart.x=snakeHead.x;
                snakePart.y=snakeHead.y;
            }
            else{
                Tile prevSnakePart= snakebody.get(i-1);
                snakePart.x=prevSnakePart.x;
                snakePart.y=prevSnakePart.y;
            }
        }

        //snakehead
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over condition
        for(int i=0;i<snakebody.size();i++){
            Tile snakePart=snakebody.get(i);
            if(collision(snakeHead,snakePart)){
                gameover=true;
            }
        }

        if(snakeHead.x*tileSize<0 || snakeHead.x*tileSize>boardwidth || snakeHead.y*tileSize <0 || snakeHead.y*tileSize >boardheight){
            gameover=true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if(gameover){
            gameloop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP && velocityY!=1){
            velocityX=0;
            velocityY=-1;
        }

        if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityY!=-1){
            velocityX=0;
            velocityY=1;
        }

        if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityX!=1){
            velocityX=-1;
            velocityY= 0;
        }

        if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocityX!=-1){
            velocityX=1;
            velocityY=0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

}
