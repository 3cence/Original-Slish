package main;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import sprites.player.Player;
import sprites.slish.Slish;

public class Window extends JFrame implements Runnable {
  private static final long serialVersionUID = 1L;

  private Player player;
  private Slish slish;
  private static int running;
  private static boolean run;
  private static int best;
  private Image background, gameover;
  
  public Window(int i) {
    running = 0;
    run = true;
    goDelay = 0;
  }
  
  public Window() {
    setTitle("Slish Adventure");
    setUndecorated(true);
    setSize(640, 640/ 12 * 9);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    running = 0;
    run = true;
    best = 0;
    
    Image i = new ImageIcon(this.getClass().getResource("/textures/slish/slish-calm.png")).getImage();
    setIconImage(i);
    background = new ImageIcon(getClass().getResource("/textures/backgrounds/background.png")).getImage();
    gameover = new ImageIcon(getClass().getResource("/textures/backgrounds/game_over.png")).getImage();
    
    setBackground(Color.BLACK);
    setForeground(Color.WHITE);
    
    player = new Player();
    addKeyListener(player);
    
    slish = new Slish();
    
    setVisible(true);
  }
  
  public static int getBest() {
    return best;
  }
  
  private void attemptSpawn() {
    Random r = new Random();
    if(r.nextInt(40) == 1) {
      int type = 0;
      if (type == 0) {
        slish.spawn();
      }
    }
  }

  private static int goDelay = 0;
  public void paint(Graphics g) {
    BufferStrategy bs = getBufferStrategy();
    if(bs == null) {
      this.createBufferStrategy(3);
      return;
    }
    g = bs.getDrawGraphics();
    if (running >= 2) {
      g.drawImage(gameover, 0, 0, null);
      g.setFont(new Font("uroob", Font.PLAIN, 30));
      g.drawString("You got " + Slish.getKills() + " Kills", 245, 410);
      g.setFont(new Font("Norasi", Font.BOLD, 15));
      g.drawString("Escape to exit", 10, 20);
      g.drawString("Enter to Play Again", 10, 35);
      if (goDelay >= 2) {
        run = false;
      } else {
        goDelay++;
      }
    } else {
      g.setColor(getBackground());
      g.drawImage(background, 0, 0, null);
  
      player.drawBullets(g);
      g.setColor(getForeground());
      player.draw(g);
      slish.draw(g);
      player.drawStats(g);
    }
    g.dispose();
    bs.show();
  }
  
  public static void stopRunning() {
    running++;
  }
  
  private void tick() {
    player.tick();
    player.tickBullet();
    slish.tick(Player.getPlayerX(), Player.getPlayerY());
    attemptSpawn();
  }
  
  public void run() {
    try {
      while (true) {
        while(run) {
          tick();
          repaint();
          Main.tickInc();
          Thread.sleep(50);
        }
        Thread.sleep(50);
      }
    } catch(Exception e) {
      System.out.println(e.getStackTrace());
    }
  }
}