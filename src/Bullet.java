public class Bullet {
  private int bx;
  private int by;
  public boolean hit;

  public Bullet() {
    this.by = MyFrame.shipY;
    this.bx = MyFrame.shipX + 17;
    this.hit = false;
  }
  public Bullet(int x, int y) {
    this.by = y;
    this.bx = x;

  }
  public int getX() {
    return bx;
  }
  public int getY() {
    return by;
  }
  public void setX(int x) {
    bx = x;
  }
  public void setY(int y) {
    by = y;
  }
}