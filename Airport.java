/*
* Andrew Z.
* Y
* Final Project
* Represents an Airport (and airport button)
* Airport.java
* 7/24/20
*/

import java.awt.*;

public class Airport extends Button {
   
   /**
   * the side length of the airport's point on the map
   */
   public static final int POINT_SIZE = 6; 
   
   /**
   * the color of unowned airports
   */
   public static final Color UNOWNED_COLOR = new Color(120,120,120);
   
   /**
   * the color of owned airports
   */
   public static final Color OWNED_COLOR = new Color(10,10,10);
    
   private double profitFactor; 
   private boolean owned; 
   
   /**
   * constructs an airport
   * @param name the name of the city in which the airport is located
   * @param x the x coord of the airports location
   * @param y the y coord of the airports location
   * @param profitFactor a scalar for the profit of flights arriving at this airport
   */
   public Airport(String name, int x, int y, double profitFactor) {
      super(x, y, POINT_SIZE, POINT_SIZE, UNOWNED_COLOR, name, UNOWNED_COLOR); 
      this.profitFactor = profitFactor;
      owned = false; // default not owned
   } 
   
   /**
   * gets the price to purchase the airport
   * @return the price to purchase
   */
   public double getPrice() {
      return profitFactor*profitFactor*10000 + 1000;
      // price is equal to the profitFactor squared times 10,000 + 1000 (arbitrary choice)
   }
   
   /**
   * sets the ownership of the airport
   * @param the ownership
   */
   public void setOwnership(boolean owned) {
      this.owned = owned; 
      if (owned) {
         setColor(OWNED_COLOR); // changes color of purchased airports to black
      } else {
         setColor(UNOWNED_COLOR); // default gray color
      }
   }
   
   /**
   * gets whether or not the airport is owned
   * @return whether or not the airport is owned
   */
   public boolean isOwned() {
      return owned; 
   }
   
   /**
   * gets whether or not the plane can fly here from the departure location
   * @param plane the plane 
   * @param departure the departure airport
   * @param g graphics
   * @return whether or not the plane can fly here
   */
   public boolean canFlyHere(Plane plane, Airport departure, Graphics g) {
      // checks several conditions
      if (!this.owned) {
         AirlineMain.dashText("Please select an airport that you own.",g);   
         return false;
      } else if (departure.getText().equals(this.getText())){
         AirlineMain.dashText("You cannot fly to the same airport.",g);
         return false; 
      } else if (AirlineMain.getAirportDistance(departure,this) > plane.getRange()) {
         AirlineMain.dashText("This airport is outside the range of your aircraft.",g);
         return false;
      } else {
         return true; 
      }
   }
   
   @Override
   public boolean isHit(int x, int y) {
      // airport buttons are centered so adding POINT_SIZE/2 accounts for that 
      return super.isHit(x + POINT_SIZE/2, y + POINT_SIZE/2);
   }
   
   @Override
   public void draw(Graphics g) {
      // drawing filled rectangle with color
      g.setColor(getColor()); 
      // draws the square centered at the city's location
      g.fillRect(getX() - POINT_SIZE/2, getY() - POINT_SIZE/2, POINT_SIZE, POINT_SIZE); 
   }
   
   /**
   * draws in the text associated with the button
   * @param g the graphics object
   */
   @Override
   public void drawText(Graphics g) {
      // draws the string to the side of the point
      g.setFont(new Font("Dialog", Font.PLAIN, 13));
      String label = getText(); 
      // shows the price of the airport on the map if it's not owned
      if (!owned) {
         label = label + " $" + (int)getPrice(); 
      }
      // draws the string to the screen
      g.drawString(label, getX() + POINT_SIZE, getY() + POINT_SIZE);

   }

   /**
   * gets the profit factor of the airport
   * @return the profit factor
   */
   public double getProfitFactor() {
      return profitFactor;
   }

   @Override
   public String toString() {
      return getText() + " at ("+getX()+", "+getY()+")" +
             "\nOwned: " + owned +
             "\nProfit Factor: " + profitFactor; 
   }         
}