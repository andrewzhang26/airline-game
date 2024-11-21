/*
* Andrew Z.
* Y
* Final Project
* Represents a button on the main program user interface
* AdButton.java
* 7/24/20
*/

import java.util.*;
import java.awt.*; 

public class AdButton extends Button {    
   
   private double price;
   
   /**
   * constructs a button
   * @param x the x coordinate of the button's top left corner
   * @param y the y coordinate of the button's top left corner
   * @param w the width of the button
   * @param h the height of the button
   * @param color the color of the button
   * @param text the text that should appear on the button
   * @param price the price of the ad in coins
   */
   public AdButton (int x, int y, int w, int h, Color fillColor, String text, Color textColor, 
                  double price) {
      super(x,y,w,h,fillColor,text,textColor); 
      this.price = price;
   }
   
   /**
   * draws in the text associated with the button
   * @param g the graphics object
   */
   @Override
   public void drawText(Graphics g) {
      // writes in the text
      g.setFont(new Font("Dialog", Font.PLAIN, 18));
      g.setColor(getTextColor());
      
      String label; 
      if (AirlineMain.advertisement) {
         label = "TV Ad Owned! (2x profits)";
      } else {
         label = getText() + ": $" + (int)price;
      }
      g.drawString(label, getX()+3, getY() + 3*getHeight()/4);
   }

   /**
   * gets whether or not the ad can be bought
   * @param g graphics
   * @return can buy
   */
   public boolean canBuy(Graphics g) {
      // only allow if they don't already own
      if (AirlineMain.advertisement) { 
         AirlineMain.dashText("You already own the TV ad!",g);
         return false; 
      // and only if they have enough coins
      } else if (AirlineMain.coins < getPrice()) {
         AirlineMain.dashText("You do not have enough coins for this ad.",g);
         return false;
      } else {
         return true; 
      }
   }
   
   /**
   * gets the price of the ad 
   * @return the price
   */
   public double getPrice() {
      return price; 
   }
     
}