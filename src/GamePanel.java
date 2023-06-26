import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[UNIT_SIZE];
    final int y[] = new int[UNIT_SIZE];
    int bodyParts = 5;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running){
            for (int i=0; i<SCREEN_HEIGHT/UNIT_SIZE; i++) {
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i=0;i<bodyParts;i++){
                if(i==0){
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }else{
                    g.setColor(new Color(45,180,0));
                    if(applesEaten>=10){
                        g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    }
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        }else{
            gameOver(g);
            bodyParts = 6;
        }
        g.setColor(Color.PINK);
        g.setFont(new Font("Arial",Font.BOLD,40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("score: "+applesEaten, (SCREEN_WIDTH-metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
    }
    public void checkCollision(){
        for(int i=bodyParts;i>0;i--){
            if((x[0]==x[i]) && (y[0]==y[i])){
                running=false;
            }
        }
        if(x[0] < 0){
            running=false;
        }
        if(x[0] > SCREEN_WIDTH){
            running=false;
        }
        if(y[0] < 0){
            running=false;
        }
        if(y[1] > SCREEN_HEIGHT){
            running=false;
        }
        if(!running){
            timer.stop();
        }
    }
    public void newApple(){
        int checkX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        int checkY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        for(int i=bodyParts;i>0;i--){
            if((checkX==x[i]) && (checkY==y[i])){
                checkX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
                checkY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
            }else{
                appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
                appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
            }
        }
    }
    public void checkApple(){
        if((appleX == x[0]) && (appleY == y[0])){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void move(){
        for(int i=bodyParts;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction){
        case 'U':
            y[0] = y[0] - UNIT_SIZE;
            break;
        case 'D':
            y[0] = y[0] + UNIT_SIZE;
            break;
        case 'L':
            x[0] = x[0] - UNIT_SIZE;
            break;
        case 'R':
            x[0] = x[0] + UNIT_SIZE;
            break;
        }
    }
    public void gameOver(Graphics g){
        g.setColor(Color.PINK);
        g.setFont(new Font("Arial",Font.BOLD,75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH-metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }
    
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if(direction!='D'){
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if(direction!='U'){
                    direction = 'D';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(direction!='L'){
                    direction = 'R';
                }
                break;
            case KeyEvent.VK_LEFT:
                if(direction!='R'){
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_B:
                if(!running){
                    applesEaten=0;
                    direction = 'R';
                    bodyParts = 6;
                    x[0] = 25;
                    y[0] = 25;
                    startGame();
                }
                break;
            }
        }
    }
}
