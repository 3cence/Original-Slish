package sprites.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;

import main.Window;
import sprites.slish.Slish;

public class Player implements KeyListener {
  
  private static int posX;
  private static int posY;
  private static int health;
  private static Projectile projectile;
  
  public Player() {
    posX = 288;
    posY = 208;
    health = 100;
    
    projectile = new Projectile();
    
    mLeft = false;
    mUp = false;
    mRight = false;
    mDown = false;
    
    loader();
  }
  
  //Getters and Setters
  
  public static int getPlayerX() {
    return posX + 25;
  }
  public static int getPlayerY() {
    return posY + 25;
  }
  public static Rectangle getBounds() {
    return new Rectangle(posX, posY, 64, 64);
  }
  public static Rectangle getSpawnBounds() {
    return new Rectangle(posX - 64, posY - 64, 192, 192);
  }
  
  //Drawing
  
  private Image playerIcon;
  private boolean loaded = false;
  
  public void loader() {
    playerIcon = new ImageIcon(this.getClass().getResource("/textures/player/playerSkin.png")).getImage();
    loaded = true;
  }
  
  public void draw(Graphics g) {
    if (loaded) {
      g.drawImage(playerIcon, posX, posY, null);
    } else {
      loader();
    }
    if (health <= 0) {
      health = 0;
      Window.stopRunning();
    }
  }

  public void drawBullets(Graphics g) {
    projectile.draw(g);
  }
  
  public void drawStats(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(10, 10, 210, 35);
    g.setColor(Color.RED);
    g.fillRect(15, 15, health * 2, 25);
    g.setColor(Color.WHITE);
    g.setFont(new Font("uroob", Font.PLAIN, 25));
    if (health < 10) {
      g.drawString("0" + health + "%", 90, 34);
    } else {
      g.drawString(health + "%", 90, 34);
    }
  }

  //Update Position
  private boolean mLeft, mUp, mRight, mDown;
  
  public void tick() {
    int speed = 5;
    Rectangle playerR = new Rectangle(posX, posY, 64, 64);
    for (int i = 0; i < Slish.getSlishCount(); i++) {
      if (playerR.intersects(Slish.getBounds(i))) {
        health--;
      }
    }
    
    if(mLeft) {
      posX -= speed;
      if (posX < 0) {
        posX = 0;      
      }
    } 
    if(mUp) {
        posY -= speed;
      if (posY < 0) {
        posY = 0;
      }
    } 
    if(mRight) {
      posX += speed;
      if (posX > 575) {
        posX = 575;
      }
    }
    if(mDown) {
      posY += speed;
      if (posY > 410) {
        posY = 410;
      }
    }
  }
  public void tickBullet() {
    projectile.tick();
  }
  //Input

  public void keyPressed(KeyEvent e) {
    int keyPressed = e.getKeyCode();
    //System.out.println(keyPressed);
    int bulletOffset = 25;
    
    if(keyPressed == 65 && !mLeft) {
      mLeft = true;
    } else if(keyPressed == 87 && !mUp) {
      mUp = true;
    } else if(keyPressed == 68 && !mRight) {
      mRight = true;
    } else if(keyPressed == 83 && !mDown) {
      mDown = true;
    } else if(keyPressed == 37) {
      projectile.launchNew(posX + bulletOffset, posY + bulletOffset, 0);
    } else if(keyPressed == 38) {
      projectile.launchNew(posX + bulletOffset, posY + bulletOffset, 1);
    } else if(keyPressed == 39) {
      projectile.launchNew(posX + bulletOffset, posY + bulletOffset, 2);
    } else if(keyPressed == 40) {
      projectile.launchNew(posX + bulletOffset, posY + bulletOffset, 3);
    } else if(keyPressed == 27) {
      System.exit(0);
    } else if(keyPressed == 10) {
      new Window(1);
      new Player();
      new Projectile();
      new Slish();
    }
  }

  public void keyReleased(KeyEvent e) {
    int keyPressed = e.getKeyCode();
    
    if(keyPressed == 65) {
      mLeft = false;
    } else if(keyPressed == 87) {
      mUp = false;
    } else if(keyPressed == 68) {
      mRight = false;
    } else if(keyPressed == 83) {
      mDown = false;
    }
  }
  
  public void keyTyped(KeyEvent e) {
    //Unused
  }
}
