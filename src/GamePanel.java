import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT/UNIT_SIZE);
    static final int DELAY = 75;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 5;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'D';
    boolean running = false;
    Timer timer;
    Random random;


    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.darkGray);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        x[0] = SCREEN_WIDTH/2;
        y[0] = SCREEN_HEIGHT/2;

        for(int i = 0; i < bodyParts; i++){
            x[i] = x[0];
            y[i] = y[0];
        }

        newApple();
        running = false;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void restartGame(){
        bodyParts = 5;
        applesEaten = 0;
        direction = 'D';

        for(int i = 0; i < GAME_UNITS; i++){
            x[i] = 0;
            y[i] = 0;
        }

        startGame();
    }

    public void startScreen(Graphics g) {
        // Draw the snake and apple even when paused
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        for(int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.green);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(new Color(50, 180, 0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }

        // Start message
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Press Arrow Key to Start", (SCREEN_WIDTH - metrics.stringWidth("Press Arrow Key to Start"))/2, SCREEN_HEIGHT/2);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g){
        if(running){
            for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
                g.drawLine(i*UNIT_SIZE, 0,  i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);

            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(50, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.cyan);
            g.setFont(new Font("Ink Free", Font.BOLD, 35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        } else if (applesEaten == 0){
            startScreen(g);
        }else {
            gameOver(g);
        }

    }
    public void newApple(){
        appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }
    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case  'U' :
                y[0]  =  y[0] - UNIT_SIZE;
                break;
            case  'D' :
                y[0]  =  y[0] + UNIT_SIZE;
                break;
            case  'L' :
                x[0]  =  x[0] - UNIT_SIZE;
                break;
            case  'R' :
                x[0]  =  x[0] + UNIT_SIZE;
                break;
        }

    }
    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
                    applesEaten++;
                    newApple();
        }
    }
    public void checkCollisions(){

        //this checks if head collides with body
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        //will check if head touches left boarder
        if(x[0] < 0){
            running = false;
        }
        //check if head touches  right boarder
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        // check if head touches top boarder
        if(y[0] < 0){
            running = false;
        }
        // check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if(!running){
            timer.stop();
        }

    }
    public void gameOver(Graphics g){
        //Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        //Game over score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 35));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/2, 100);

        g.setColor(Color.CYAN);
        g.setFont(new Font("Ink Free", Font.BOLD, 25));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press SPACE to restart!", (SCREEN_WIDTH - metrics3.stringWidth("Press SPACE to restart!"))/2, SCREEN_HEIGHT/2 + 100);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){

//            if(!running && applesEaten == 0 &&)

            if(!running && applesEaten == 0){
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        direction = 'L';
                        running = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        direction = 'R';
                        running = true;
                        break;
                    case KeyEvent.VK_UP:
                        direction = 'U';
                        running = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        direction = 'D';
                        running = true;
                        break;
                }
            }

            if(!running && e.getKeyCode() == KeyEvent.VK_SPACE){
                restartGame();
            }

            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
