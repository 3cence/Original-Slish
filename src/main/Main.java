package main;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

public class Main {
  public static void main(String[] args) {
    // THIS COMMENT IS FROM THE FUTURE!!!
    new Main();
    Thread w = new Thread(new Window());
    w.start();
  }
  
  private Font uroobFont;
  
  public Main() {
    loadFonts();
  }
  
  private void loadFonts() {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    
    try {
      uroobFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("textures/fonts/Uroob.ttf"));
      ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("textures/fonts/Uroob.ttf")));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static double distance(int x1, int y1, int x2, int y2) {
    return Math.sqrt((Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)));
  }
  
  private static int ticks = 0;
  
  public static int getTickCount() {
    return ticks;
  }
  public static void tickInc() {
    ticks++;
  }
}
