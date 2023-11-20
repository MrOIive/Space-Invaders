import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

@SuppressWarnings("serial")
public class MyPanel extends JPanel {

  final int PANEL_WIDTH = 740;
  final int PANEL_HEIGHT = 600;
  final int IMAGE_SIZE = 30;
  
  Image backgroundImage;
  Image shipImage;
  Image bulletImage;
  Image exInvader;
  public static Image ufoImage;
  public static Image bombImage;
  public static JLabel liveL;
  public static JLabel waveL;
  public static JLabel scoreL;
  public static JLabel label;

  public static boolean gameover = false;
  public static Invader[][] invaders = new Invader[11][5];
  
  boolean firstTime = true;
  public static ArrayList<Bullet> list = new ArrayList<Bullet>();

  private int tick = 0;
  public static int waves = 1;
  public static int score = 0;
  public static int maxTickR = 7;
  public static int maxTickL = 0;
  public static int lowestPoint = 4;
  private int rol = 1;
  private boolean jsShifted = true;
  public static int totalTicks = 0;
  public int delay = 1500;

  Random r = new Random();
  int timeRand = r.nextInt(2000) + 500;
  int ufoRand = r.nextInt(8000) + 7000;
  
  public static final int UFOPOSX = 800;
  public static final int UFOPOSY = 50;
  public static int ufoCPosX = UFOPOSX;
  public static int ufoCPosY = UFOPOSY;
  boolean ufoOut = false;
  boolean bombOut = false;
  boolean ufoClear = true;
  public static int bombX = 0;
  public static int bombY = 0;
  public static int bombW = 7;
  public static int bombH = 12;

  MyPanel() {}

  public void makePanel() {
    this.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
    this.setLayout(null);

    label = new JLabel();
    label.setFont(new Font("Comic Sans", Font.BOLD, 60));
    label.setBounds(0,0,PANEL_WIDTH, PANEL_HEIGHT);
    label.setVerticalAlignment(JLabel.CENTER);
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setForeground(Color.WHITE);

    waveL = new JLabel("Wave: 1");
    waveL.setFont(new Font("Comic Sans", Font.BOLD, 20));
    waveL.setForeground(Color.WHITE);
    waveL.setBounds(314, 10, 496, 30);

    scoreL = new JLabel("Score: 0");
    scoreL.setFont(new Font("Comic Sans", Font.BOLD, 20));
    scoreL.setForeground(Color.WHITE);
    scoreL.setBounds(100, 10, 314, 30);

    liveL = new JLabel("Lives: ");
    liveL.setFont(new Font("Comic Sans", Font.BOLD, 20));
    liveL.setForeground(Color.WHITE);
    liveL.setBounds(496, 10, 740, 30);

    this.add(label);
    this.add(waveL);
    this.add(scoreL);
    this.add(liveL);

     backgroundImage = new ImageIcon("spaceBackground.jpg").getImage();
     shipImage = new ImageIcon("ship.png").getImage();
     bulletImage = new ImageIcon("bullet.png").getImage();
     exInvader = new ImageIcon("explosion").getImage();
     ufoImage = new ImageIcon("ufo.png").getImage();
     bombImage = new ImageIcon("bullet.png").getImage();
     int addX = 15;
     int addY = 100;
     for (int y = 0; y < 5; y++) {
        for (int x = 0; x < 11; x++) {
          int t = 0;
          if (y == 4 || y == 3) {t = 1;}
          if (y == 2 || y == 1) {t = 2;}
          if (y == 0) {t = 3;}
          invaders[x][y] = new Invader(t, addX, addY);
          addX += 40;
        }
        addX = 15;
        addY += 40;
     }
  }
  
  public void nextWave() {
	  int addX = 15;
	  int addY = 100;
	  for (int y = 0; y < 5; y++) {
	    for (int x = 0; x < 11; x++) {
	       int t = 0;
	       if (y == 4 || y == 3) {t = 1;}
	       if (y == 2 || y == 1) {t = 2;}
	       if (y == 0) {t = 3;}
	       invaders[x][y].setType(t);
	       invaders[x][y].setStatus(true);
	       invaders[x][y].setX(addX);
	       invaders[x][y].setY(addY);
	       invaders[x][y].changeImage();
	       addX += 40;
	     }
	     addX = 15;
	     addY += 40;
	  }
	  delay = (int) (delay * .9);
	  waves++;
	  tick = 0;
	  maxTickR = 7;
	  maxTickL = 0;
	  lowestPoint = 4;
	  jsShifted = true;
	  rol = 1;
	  waveL.setText("Wave: "+waves);
	  repaint();
	  Invader.nInvaders = 55;
  }

  protected void paintComponent(Graphics g) {
	  if (!gameover) {
	    super.paintComponent(g); 
	    Graphics2D g2D = (Graphics2D) g;
   		g2D.drawImage(backgroundImage, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, null);
   		g2D.drawImage(shipImage, MyFrame.shipX, MyFrame.shipY, IMAGE_SIZE+5, IMAGE_SIZE+5, null);
   		for (int j = 0; j < MyFrame.bs.size(); j++) {
   			if (!MyFrame.bs.get(j).hit) 
   				g2D.drawImage(bulletImage, MyFrame.bs.get(j).getX(), MyFrame.bs.get(j).getY(), 5, 10, null);
   		}

   		for (int i = 0; i < list.size(); i++) {
   			g2D.drawImage(bulletImage, list.get(i).getX(), list.get(i).getY(), 5, 10, null);
   		}
   		for (int y = 0; y < 5; y++) {
   			for (int x = 0; x < 11; x++) {
   				if (invaders[x][y].getStatus() || invaders[x][y].getType() == 5) {
   					Image image = invaders[x][y].getImage();
   					g2D.drawImage(image, invaders[x][y].getX(), invaders[x][y].getY(), IMAGE_SIZE, IMAGE_SIZE, null);
   				}
   			}
   		}	
   		Image image = new ImageIcon("ship.png").getImage();
   		if (MyFrame.lives == 3) {
   			g2D.drawImage(image, 575, 10, 30, 30, null);
   			g2D.drawImage(image, 625, 10, 30, 30, null);
   			g2D.drawImage(image, 675, 10, 30, 30, null);
   		} else if (MyFrame.lives == 2) {
   			g2D.drawImage(image, 575, 10, 30, 30, null);
   			g2D.drawImage(image, 625, 10, 30, 30, null);
   		} else if (MyFrame.lives == 1) {
   			g2D.drawImage(image, 575, 5, 30, 30, null);
   		}
   		if (ufoOut) 
   			g2D.drawImage(ufoImage, ufoCPosX, ufoCPosY, 80, 40, null);
   		if (bombOut) 
			g2D.drawImage(bombImage, bombX, bombY, bombW, bombH, null);
   	 }
  }

  void invaderLoop() {
      while (!gameover) {
    	  try {
    		  Thread.sleep(delay);
    	  }
    	  catch(InterruptedException e) {}
    	  if (gameover)
              break;
    	  if (Invader.nInvaders != 0)
    	  	moveInvaders();
      }
  }

  public void invaderBulletLoop() {
    while(!gameover) {
      try {
        Thread.sleep(timeRand);
      } catch (InterruptedException ex) {}
      if (gameover) {
          break;
       }
      timeRand = r.nextInt(2000) + 500;
      boolean rand = false;
      int rr = 0;
      while (!rand) {
        rr = r.nextInt(11);
        if (invaders[rr][0].getStatus())
          rand = true;
      }
      list.add(0, new Bullet(invaders[rr][0].getX() + 15, invaders[0][0].getY()));
      if (!gameover) {
      	try {
      		String soundName = "bullet.wav";    
      		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
      		Clip clip = AudioSystem.getClip();
      		clip.open(audioInputStream);
      		clip.start();
      	} catch (Exception ex) {}
      }
      if (firstTime) {
        moveInvaderBullet();
        firstTime = false;
      }
    }
  }
  public void moveInvaderBullet() {
    Timer t = new Timer();
    TimerTask taskPerformer = new TimerTask() {
        @Override
        public void run() {
          for (int i = 0; i < list.size(); i++) {
            if (gameover)
              t.cancel();
            list.get(i).setY(list.get(i).getY() + 1);
            int x = list.get(i).getX();
            int y = list.get(i).getY();
            repaint();
            if ((MyFrame.shipX+35 >= x && MyFrame.shipX <= x) && (MyFrame.shipY+35 >= y && MyFrame.shipY <= y)) {
              MyFrame.lives--;
              MyFrame.move = false;
              shipImage = new ImageIcon("explosion.png").getImage();
              try {
              	String soundName = "bomb_explosion.wav";    
              	AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
              	Clip clip = AudioSystem.getClip();
              	clip.open(audioInputStream);
              	clip.start();
              } catch (Exception ex) {}
              repaint();
              list.remove(i);
              if (MyFrame.lives == 0) {
                label.setText("Game Over");
                repaint();
                gameover = true;
              } else {
                TimerTask taskPerformer = new TimerTask() {
                  @Override
                  public void run() {
                    shipImage = new ImageIcon("ship.png").getImage();
                    MyFrame.shipX = 365;
                    MyFrame.shipY = 550;
                    MyFrame.move = true;
                    repaint();
                  }
                };
                new Timer().schedule(taskPerformer, 500);
              }
            } else if (y >= PANEL_WIDTH) {
              list.remove(i);
            }
          }
        }
      };
      t.schedule(taskPerformer, 1, 1);
  }

  public void moveInvaders() {
	 boolean done = false;
	 while (!done) {
	  int countR = 0;
	  int countL = 0;
	  for (int y = 0; y < 5; y++) {
		  if (!invaders[Math.abs(maxTickL)][y].getStatus())
			  countL++;
		  if (!invaders[11 - (maxTickR-6)][y].getStatus())
			  countR++;
	  }
	  if (countL == 5) 
		  maxTickL--;
	  if (countR == 5)
		  maxTickR++;
	  if (countR != 5 && countL != 5) 
		  done = true;
	 }
	 done = false;
	 while (!done) {
	 	int countD = 0;
	 	for (int x = 0; x < 11; x++) {
	 		if (!MyPanel.invaders[x][MyPanel.lowestPoint].getStatus())
	 			countD++;
	 	}
	 	if (countD == 11)
	 		MyPanel.lowestPoint--;
	 	else 
	 		done = true;
	 }
    totalTicks++;
    if (!jsShifted && (tick == maxTickR || tick == maxTickL)) {
      rol++;
      for (int y = 0; y < 5; y++) {
        for (int x = 0; x < 11; x++) {
          invaders[x][y].setY(invaders[x][y].getY() + 45);
          invaders[x][y].changeImage();
        }
      }
      jsShifted = true;
      if (invaders[0][lowestPoint].getY() >= 500) {
    	  label.setText("Game Over");
    	  shipImage = new ImageIcon("explosion.png").getImage();
          repaint();
          try {
            String soundName = "bomb_explosion.wav";    
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
          } catch (Exception ex) {}
          gameover = true;
      }
    } else {
      int rolturn = rol % 2;
      if (rolturn == 1) {
        for (int y = 0; y < 5; y++) {
          for (int x = 0; x < 11; x++) {
            invaders[x][y].setX(invaders[x][y].getX() + 40);
            invaders[x][y].changeImage();
          }
        }
        tick++;
      } else {
        for (int y = 0; y < 5; y++) {
          for (int x = 0; x < 11; x++) {
            invaders[x][y].setX(invaders[x][y].getX() - 40);
            invaders[x][y].changeImage();
          }
        }
        tick--;
      }
      jsShifted = false;
    }
    repaint();
  }

  public void ufoLoop() {
    while(!gameover) {
    System.out.print("");
    if (ufoClear) {
      try {
        Thread.sleep(ufoRand);
      } catch (InterruptedException ex) {}
      if (gameover) {
          break;
       }
      try {
        String soundName = "saucer.wav";    
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
      } catch (Exception ex) {} 
      ufoRand = r.nextInt(8000) + 7000;
      Timer t = new Timer();
      TimerTask tt = new TimerTask() {
    	  @Override
    	  public void run() {
    		  if (ufoImage == new ImageIcon("ufo.png").getImage()) {
    		  	ufoCPosX -= 1;
    		  	repaint();
    		  }
    		  if (ufoCPosX <= -100 || gameover) {
    			  t.cancel();
    			  ufoCPosX = UFOPOSX;
    			  ufoOut = false;
    		  }
    	  }
      };
      ufoOut = true;
      t.schedule(tt, 50, 3);
      
      Timer b = new Timer();
      TimerTask bb = new TimerTask() {
    	  @Override
    	  public void run() {
    		  Bullet bullet = new Bullet(MyFrame.shipX, UFOPOSY);
    		  bombX = MyFrame.shipX;
    		  bombY = UFOPOSY;
    		  bombOut = true;
    		  try {
    	        String soundName = "bomb.wav";    
    	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
    	        Clip clip = AudioSystem.getClip();
    	        clip.open(audioInputStream);
    	        clip.start();
    	       } catch (Exception ex) {}
    		  
    		  Timer n = new Timer();
    	      TimerTask nn = new TimerTask() {
    	    	  @Override
    	    	  public void run() {
    	    		boolean hit = false;
    	    		if (!hit && !gameover) {
    	    		  bombY++;
    	    		  bullet.setY(bombY);
    	    		  int x = bullet.getX();
    	    		  int y = bullet.getY();
    	    		  if (gameover) {
    	    			  n.cancel();
    	    		  }
    	    		  
    	    		  if ((MyFrame.shipX+30 >= x && MyFrame.shipX-30 <= x) && (MyFrame.shipY+30 >= y && MyFrame.shipY-30 <= y)) {
    	    			  MyFrame.lives--;
    	    			  bullet.setX(UFOPOSX);
    	    			  bullet.setY(UFOPOSY);
    	                  MyFrame.move = false;
    	                  shipImage = new ImageIcon("explosion.png").getImage();
    	                  try {
    	                  	String soundName = "bomb_explosion.wav";    
    	                  	AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
    	                  	Clip clip = AudioSystem.getClip();
    	                  	clip.open(audioInputStream);
    	                  	clip.start();
    	                  } catch (Exception ex) {}
    	                  repaint();
    	                  if (MyFrame.lives == 0) {
    	                    label.setText("Game Over");
    	                    repaint();
    	                    gameover = true;
    	                    hit = true;
    	                    n.cancel();
    	                  } else {
    	                    TimerTask taskPerformer = new TimerTask() {
    	                      @Override
    	                      public void run() {
    	                        shipImage = new ImageIcon("ship.png").getImage();
    	                        MyFrame.shipX = 365;
    	                        MyFrame.shipY = 550;
    	                        MyFrame.move = true;
    	                        bombOut = false;
        	                    ufoClear = true;
    	                        repaint();
    	                        n.cancel();
    	                      }
    	                    };
    	                    new Timer().schedule(taskPerformer, 500);
    	                    hit = true;
    	                  }
    	                } else if (y >= PANEL_WIDTH) {
    	                  bombOut = false;
    	                  hit = true;
    	                  ufoClear = true;
    	                  n.cancel();
    	                }
    	    		}
    	    	  }
    	      };
    	      n.schedule(nn, 2, 2);
    	  }
      };
      b.schedule(bb, 1000);
      ufoClear = false;
    }
    }
  }
}
