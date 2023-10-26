import javax.swing.*;
import java.awt.*;

public class Invader {

  private int type;
  private int xx;
  private int yy;
  private Image image;
  private boolean status = true;

  public static int nInvaders = 55;

  public Invader(int type, int x, int y) {
    this.type = type;
    xx = x;
    yy = y;

    image = new ImageIcon("invader"+type+"0.png").getImage();
  }

  public int getType() {
    return this.type;
  }
  public void setType(int t) {
    this.type = t;
  }
  public int getX() {
    return xx;
  }
  public int getY() {
    return yy;
  }
  public void setX(int x) {
    xx = x;
  }
  public void setY(int y) {
    yy = y;
  }

  public Image getImage() {
    return image;
  }

  public boolean getStatus() {
    return status;
  }
  public void setStatus(boolean s) {
	  status = s;
  }

  public boolean watchBullet(int x, int y) {

    if(status && ((xx+30 >= x && xx <= x) && (yy+30 >= y && yy <= y))) {
      status = false;
      nInvaders--;
      if (type == 1) {
        MyPanel.score += 10;
      } else if (type == 2) {
        MyPanel.score += 20;
      } else {
        MyPanel.score += 40;
      }
      MyPanel.scoreL.setText("Score: "+MyPanel.score);
      image = new ImageIcon("explosion.png").getImage();
      type = 5;
      return true;
      } else if (y <= 0) {
        return true;
      }
    return false;
  }

  public void changeImage() {
    int mod = MyPanel.totalTicks % 2;
    image = new ImageIcon("invader"+this.type+mod+".png").getImage();
  }
}