import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

@SuppressWarnings("serial")
public class MyFrame extends JFrame implements KeyListener {
  MyPanel panel;
  public static int shipX = 365;
  public static int shipY = 550;
  public static boolean move = true;
  
  public static int lives = 3;

  int index = 0;
  int saveX = 0;
  int saveY = 0;
  boolean ufoHit = false;
  boolean firstTime = true;
  public static ArrayList<Bullet> bs = new ArrayList<Bullet>();

  MyFrame() {}

  public void makeFrame() {

    panel = new MyPanel();
    panel.makePanel();

    Bullet e = new Bullet();
    bs.add(e);
    bs.remove(0);
    this.setTitle("Space Invaders");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(740,600);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    this.setIconImage(new ImageIcon("spaceicon.jpeg").getImage());
    this.addKeyListener(this);
    this.add(panel);
    this.pack();

    this.setVisible(true);

    Thread invaderBulletThread = new Thread() {
    	@Override
    	public void run() {
    		panel.invaderBulletLoop();
    	}
    };
    Thread invaderThread = new Thread() {
    	@Override
    	public void run() {
    		panel.invaderLoop();
    	}
    };
    invaderBulletThread.start();
    invaderThread.start();
    panel.ufoLoop();
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {
    if (move && !MyPanel.gameover && Invader.nInvaders != 0) {
      switch(e.getKeyCode()) {
        case KeyEvent.VK_RIGHT: if (695 > shipX) {shipX+=10;  repaint();}
          return;
        case KeyEvent.VK_LEFT: if (0 < shipX) {shipX-=10; repaint();}
          return;
      }
    }
     if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) && !MyPanel.gameover && Invader.nInvaders != 0 && move) {
      if (bs.size() <= 2) {
        bs.add(0, new Bullet()); 
        MyPanel.score--;
        MyPanel.scoreL.setText("Score: "+MyPanel.score);
        try {
        	String soundName = "bullet.wav";    
        	AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
        	Clip clip = AudioSystem.getClip();
        	clip.open(audioInputStream);
        	clip.start();
        } catch (Exception ex) {}
        if (firstTime) {
          moveBullet();
          firstTime = false;
        }
        return;
      }
    }
  }

  public void moveBullet() {  
   Timer ttt = new Timer();
   TimerTask tt = new TimerTask () {
   @Override
     public void run() {
	   int size = bs.size();
        for (int i = 0; i < bs.size(); i++) {
          if (MyPanel.gameover)
            ttt.cancel();
          index = i;
          if (size != bs.size()) 
        	  break;
          bs.get(i).setY(bs.get(i).getY() - 1);
          int bx = bs.get(i).getX();
          int by = bs.get(i).getY();
          for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 11; x++) {
              if (!bs.get(i).hit) {
                bs.get(i).hit = MyPanel.invaders[x][y].watchBullet(bx, by);
                saveX = x;
                saveY = y;
              }
            }
          }
          
          panel.repaint();
          if (bs.get(i).hit && by > 0) {
        	 try {
              String soundName = "explosion.wav";    
              AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
              Clip clip = AudioSystem.getClip();
              clip.open(audioInputStream);
              clip.start();
            } catch (Exception ex) {}
        	bs.remove(i);
            myTimerTask(saveX, saveY);
          } else if (by <= 0) {
            bs.remove(i);
          } else if (!ufoHit && (MyPanel.ufoCPosX+80 >= bx && MyPanel.ufoCPosX <= bx) && (MyPanel.ufoCPosY+40 >= by && MyPanel.ufoCPosY <= by)) {
        	  try {
                 String soundName = "explosion.wav";    
                 AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
                 Clip clip = AudioSystem.getClip();
                 clip.open(audioInputStream);
                 clip.start();
              } catch (Exception ex) {}
        	  MyPanel.score += 100;
        	  bs.remove(i);
        	  ufoHit = true;
        	  MyPanel.ufoImage = new ImageIcon("explosion.png").getImage();
        	  panel.repaint();
        	  TimerTask t = new TimerTask() {
        	    @Override
        	    public void run() {
        	    	MyPanel.ufoCPosX = -100;
        	    	MyPanel.ufoImage = new ImageIcon("ufo.png").getImage();
        	    	ufoHit = false;
        	    }
        	  };
        	  new Timer().schedule(t, 250);
          } else if ((MyPanel.bombX+7 >= bx && MyPanel.bombX <= bx) && (MyPanel.bombY+12 >= by && MyPanel.bombY <= by)) {
        	  try {
                  String soundName = "explosion.wav";    
                  AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
                  Clip clip = AudioSystem.getClip();
                  clip.open(audioInputStream);
                  clip.start();
               } catch (Exception ex) {}
         	  MyPanel.score += 25;
         	  MyPanel.bombY = 625;
         	  bs.remove(i);
         	  panel.repaint();
          }
        }
      }
    };
    ttt.schedule(tt, 1, 1);
  }
  void myTimerTask(int x, int y) {
	  TimerTask t = new TimerTask() {
         @Override
         public void run() {
             MyPanel.invaders[x][y].setType(0);
             panel.repaint();
             if (Invader.nInvaders == 0) {
   	      	  panel.nextWave();
   	        }
         }
      	};
       new Timer().schedule(t, 75);
  }
}