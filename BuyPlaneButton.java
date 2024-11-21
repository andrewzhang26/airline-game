/*
* Andrew Z.
* Y
* Final Project
* Represents a button on the main program user interface
* BuyPlaneButton.java
* 7/24/20
*/

import java.util.*;
import java.awt.*; 

public class BuyPlaneButton extends Button {    
   
   private double price;
   private ArrayList<Plane> planes; 
   
   /**
   * constructs a button
   * @param x the x coordinate of the button's top left corner
   * @param y the y coordinate of the button's top left corner
   * @param w the width of the button
   * @param h the height of the button
   * @param color the color of the button
   * @param text the text that should appear on the button
   * @param price the price of the plane in coins
   * @param planes the list of the user-owned planes
   */
   public BuyPlaneButton (int x, int y, int w, int h, Color fillColor, String text, Color textColor, 
                  double price, ArrayList<Plane> planes) {
      super(x,y,w,h,fillColor,text,textColor); 
      this.price = price;
      this.planes = planes; 
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
      g.drawString(getText() + ": $" + price, getX()+3, getY() + 3*getHeight()/4);
   }

   /**
   * gets the price of the plane 
   * @return the price
   */
   public double getPrice() {
      return price; 
   }
   
   /**
   * gets whether or not the plane can be bought
   * @param g graphics
   * @return can buy
   */
   public boolean canBuy(Graphics g) {
      // only allow user to buy if they own airports
      if (AirlineMain.getOwnedAirports().size() == 0) { 
         AirlineMain.dashText("You must buy an airport before purchasing an aircraft.",g);
         return false; 
      // and only if they have enough coins
      } else if (AirlineMain.coins < getPrice()) {
         AirlineMain.dashText("You do not have sufficient coins to buy this aircraft.",g);
         return false;
      } else {
         return true; 
      }
   }
   
   /**
   * buys the plane and modifies coin count (assumes user has enough coins)
   * @param g the graphics object
   * @return the new plane
   */
   public Plane buyPlane(Airport location, Graphics g){
      AirlineMain.coins = AirlineMain.coins - price; 
      try {
         return new Plane(getText(), location); 
      } catch (Exception e) {
         AirlineMain.dashText("Error purchasing the plane.",g);
         return null; 
      }
   }   
       
}