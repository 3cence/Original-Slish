package sprites.slish;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import main.Main;
import sprites.player.Player;
import sprites.player.Projectile;

public class Slish {

  Image calm, angered;
  
  private static ArrayList<Rectangle> slish;
  private final ArrayList<Integer> skin;
  private final ArrayList<Integer> lastHit;
  private static int kills;
  
  public Slish() {
    slish = new ArrayList<Rectangle>();
    lastHit = new ArrayList<Integer>();
    skin = new ArrayList<Integer>();
    kills = 0;
     
    calm = new ImageIcon(getClass().getResource("/textures/slish/slish-calm.png")).getImage();
    angered = new ImageIcon(getClass().getResource("/textures/slish/slish-angered.png")).getImage();
  }
  
  //getters and setters
  public static int getSlishCount() {
    return slish.size();
  }
  public static Rectangle getBounds(int i) {
    return slish.get(i);
  }
  public static int getKills() {
    return kills;
  }
  
  public void spawn() {
    Random r = new Random();
    Rectangle rec = new Rectangle(r.nextInt(640), r.nextInt(480), 32, 32);
    
    if (rec.intersects(Player.getSpawnBounds())) {
      System.out.println("Stopped Spawn");
    } else {
      slish.add(rec);
      lastHit.add(0);
      skin.add(0);
    }
  }
  
  public void tick(int px, int py) {
    Rectangle hitBox;
    int tmpSkin;
    px -= 8;
    py -= 8;
    ArrayList<Rectangle> bullet = new ArrayList<Rectangle>();
    ArrayList<Integer> toRemove = new ArrayList<Integer>();
    int bulletCount = Projectile.getCount();
    int speed = 2;
    
    if (slish.size() == 0) {
      this.spawn();
    }
    
    for(int i = 0; i < bulletCount; i++) {
      bullet.add(Projectile.getBounds(i));
    }
    
    for(int i = 0; i < slish.size(); i++) {
      hitBox = slish.get(i);
      tmpSkin = skin.get(i);
      
      //Collision testing
      for(int j = 0; j < bulletCount; j++) {
        speed = 2;
        try {
          if (hitBox.intersects(bullet.get(j)) && Main.getTickCount() - lastHit.get(i) > 5) {
            tmpSkin++;
            lastHit.set(i, Main.getTickCount());
            if (tmpSkin >= 1) {
              speed = 4;
            }
            if (tmpSkin > 1) {
              try {
                toRemove.add(i);
              } catch (Exception e) {}
            }
        }
        } catch(Exception e) {
          System.out.println("Here AGAINa");
        }
      }
      
      //PathFinding
      
      int x = (int)hitBox.getX(), y = (int)hitBox.getY();
      int pxd = Math.abs(px - x), pyd = Math.abs(py - y);
     
      if (pxd >= pyd) {
        if(px > x) {
          x += speed;
        } else if (px < x){
          x -= speed;
        }
      } else if (pxd < pyd) {
        if(py > y) {
          y += speed;
        } else if (py < y){
          y -= speed;
        }
      }
        
      if (pxd > pyd) {
        if(py > y) {
          y += speed / (pxd / pyd);
        } else if (py < y){
          y -= speed / (pxd / pyd);
        }
      } else if (pxd < pyd) {
        if(px > x) {
          x += speed / (pyd / pxd);
      } else if (px < x) {
          x -= speed / (pyd / pxd);
      }
    }
      
      hitBox.setLocation(x, y);
      slish.set(i, hitBox);
      skin.set(i, tmpSkin);
    }
    for (int i = 0; i < toRemove.size(); i++) {
      try {
        int r = toRemove.get(i);
        slish.remove(r);
        skin.remove(r);
        lastHit.remove(r);
        kills++;
      } catch (Exception e) {
        System.out.println("Remove error");
      }
    }
  }
  
  public void draw(Graphics g) {
    for(int i = 0; i < slish.size(); i++) {
      if(skin.get(i) == 0) {
        g.drawImage(calm, (int)slish.get(i).getX(), (int)slish.get(i).getY(), null);
      } else {
        g.drawImage(angered, (int)slish.get(i).getX(), (int)slish.get(i).getY(), null);
      }
    }
    g.setColor(Color.WHITE);
    g.setFont(new Font("uroob", Font.PLAIN, 25));
    g.drawString("Kills: " + kills, 10, 65);
  }
}
