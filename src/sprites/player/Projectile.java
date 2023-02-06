package sprites.player;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import main.Main;
import sprites.slish.Slish;


public class Projectile {
  
  static ArrayList<Rectangle> bullet;
  ArrayList<Integer> direction;
  ArrayList<Integer> touched;
  
  public Projectile() {

    bullet = new ArrayList<Rectangle>();
    direction = new ArrayList<Integer>();
    touched = new ArrayList<Integer>();
    
    loadTexture();
  }
  
  //getters and setters
  
  public static Rectangle getBounds(int i) {
    return bullet.get(i);
  }
  public static int getCount() {
    return bullet.size();
  }
  
  //new projective
  
  private int lastLaunched = 0;
  private final int delay = 7;
  
  public void launchNew(int x, int y, int d) {
    if (bullet.size() < 50  && (Main.getTickCount() - lastLaunched >= delay)) {
      bullet.add(new Rectangle(x, y, 16, 16));
      direction.add(d);
      touched.add(0);
      lastLaunched = Main.getTickCount();
    }
  }
  
  //load texture
  private Image bulletTexture;
  
  private void loadTexture() {
    bulletTexture = new ImageIcon(getClass().getResource("/textures/player/playerBullet.png")).getImage();
  }
  
  //Tick and Render
  
  public void tick() {
    ArrayList<Integer> despawn = new ArrayList<Integer>();
    int speed = 10;
    int tmpLocX;
    int tmpLocY;
    Rectangle tmpR;
    
    for(int i = 0; i < bullet.size(); i++) {
      tmpR = bullet.get(i);
      tmpLocX = (int)tmpR.getX();
      tmpLocY = (int)tmpR.getY();
      
      if (direction.get(i) == 0) {
        tmpLocX -= speed;
      } else if (direction.get(i) == 1) {
        tmpLocY -= speed;
      } else if (direction.get(i) == 2) {
        tmpLocX += speed;
      } else if (direction.get(i) == 3) {
        tmpLocY += speed;
      }
      
      for (int j = 0; j < Slish.getSlishCount(); j++) {
        if (tmpR.intersects(Slish.getBounds(j)) && touched.get(i) > 1) {
          despawn.add(i);
        } else if (tmpR.intersects(Slish.getBounds(j))) {
          int t = touched.get(i);
          t++;
          touched.set(i, t);
        }
      }
      
      if(tmpLocX < 0 || tmpLocY < 0 || tmpLocX > 640 || tmpLocY > 480) {
        despawn.add(i);
      } else {
        tmpR.setLocation(tmpLocX, tmpLocY);
        bullet.set(i, tmpR);
      }
    }
    for(int i = 0; i < despawn.size(); i++) {
      try {
        int r = despawn.get(i);
        bullet.remove(r);
        direction.remove(r);
        touched.remove(r);
      } catch (Exception e) {}
    }
  }
  
  public void draw(Graphics g) {
    for(int i = 0; i < bullet.size(); i++) {
      g.drawImage(bulletTexture, (int)bullet.get(i).getX(), (int)bullet.get(i).getY(), null);
    }
  }
}
