import  java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
public class flappyBird extends  JPanel implements ActionListener,KeyListener{

    // width and height of screen
    int width = 360;
    int height = 640;

    //intializing variables
    Image bgImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //Bird variables
    int birdX = width/8;
    int birdY = height/2;
    int birdWidth = 35;
    int birdHeight = 25;





    // Pipes variables

    int pipeWidth = 64;
    int pipeHeight = 512;
    int pipeX = width;
    int pipeY = 0;



    //game logic variables
    Timer loop;
    Timer placePipesTimer;
    int gravity = 1;
    int velocityY=0;
    int velocityX= -5; // moves pipes to the left
    ArrayList<Pipe> pipes;
    Random random = new Random();
    boolean gameOver = false;
    double score = 0;
    double highestScore;
//    double pipeSpeed = 360;
//    double speedInterval = 1000;
//    double gameTime = 0;


    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img){
            this.img = img;
        }
    }
    flappyBird(){
        setPreferredSize(new Dimension(width,height));

        setFocusable(true);
        addKeyListener(this);

        //loading images
        bgImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./flappybirdbg.png"))).getImage();
        birdImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./flappybird.png"))).getImage();
        topPipeImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./toppipe.png"))).getImage();
        bottomPipeImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./bottompipe.png"))).getImage();


        // game loop

        loop = new Timer(1000/60,this); // 60fps
        loop.start();



        pipes = new ArrayList<Pipe>();

        // pipes timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();
    }
    public void placePipes (){

        // 0 - 128 - (0 -> 256)
        int randomY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int opening = height/4;
        Pipe toppipe = new Pipe(topPipeImg);
        toppipe.y = randomY;
        pipes.add(toppipe);

        Pipe bottompipe = new Pipe(bottomPipeImg);
        bottompipe.y = toppipe.y + pipeHeight + opening;
        pipes.add(bottompipe);
    }


    //User Interface
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw (Graphics g){
        //background
        g.drawImage(bgImg,0,0,width,height,null);

        //bird
        g.drawImage(birdImg,birdX,birdY,birdWidth,birdHeight,null);

        //pipes
        for(int i=0 ;i<pipes.size(); i++){
            Pipe pipe  = pipes.get(i);
            g.drawImage(pipe.img ,pipe.x , pipe.y , pipe.width , pipe.height , null);
        }

        //score
        g.setColor(Color.black);
        g.setFont(new Font("Arial",Font.BOLD,24));

        if(gameOver){
            g.drawString("Game Over : " + String.valueOf((int)score),15,40);
        }
        else {
            g.drawString("Score : "+String.valueOf((int)score),15,40);
        }

        g.drawString("High Score : "+String.valueOf((int)highestScore),150    ,40);

    }


    public void movement(){

        velocityY += gravity;
        birdY += velocityY;
        birdY = Math.max(birdY,0);

        //pipes
        for(int i=0; i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && birdX > pipe.x + pipeWidth){
                pipe.passed = true;
                score += 0.5; // score are divided between two pipes
            }

            //High Score
            highestScore = Math.max(highestScore , score);

            if(collision(pipe) || birdY > height ){
                gameOver = true;
            }
        }

    }


    public boolean collision (Pipe b){
        return birdX < b.x + b.width &&
                birdX + birdWidth > b.x &&
                birdY < b.y + b.height &&
                birdY + birdHeight > b.y;
    }
    @Override
    public void actionPerformed(ActionEvent e ) {
        movement();
        repaint();
        if(gameOver){
            placePipesTimer.stop();
            loop.stop();
        }
//    gameTime+= 20;
//    if(gameTime % speedInterval == 0){
//        pipeSpeed +=1;
//    }
//    pipeX += (int) pipeSpeed;


    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY  = -10;

            if(gameOver){
                birdY = birdY;
                velocityY = 0;
                pipes.clear();
                score =0;
                gameOver = false;
                loop.start();
                placePipesTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
