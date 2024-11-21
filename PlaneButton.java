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

public class PlaneButton extends Button {    
   
   private Plane plane; 
   
   /**
   * constructs a button
   * @param x the x coordinate of the button's top left corner
   * @param y the y coordinate of the button's top left corner
   * @param w the width of the button
   * @param h the height of the button
   * @param color the color of the button
   * @param text the text that should appear on the button
   * @param plane the plane that this button is associated with
   */
   public PlaneButton (int x, int y, int w, int h, Color fillColor, Color textColor, Plane plane) {
      super(x,y,w,h,fillColor,plane.getModelName(),textColor);
      this.plane = plane;   
      if (getPlane().isFlying()) {
         setColor(new Color(41,179,242)); // different color if the plane is flying
      }
   }
   
   /**
   * gets the plane object this button is associated with
   * @return the plane object
   */
   public Plane getPlane() {
      return plane;
   }
   
   /**
   * decides whether or not the plane can fly (for basic requirements)
   * @param graphics object
   * @return whether or not the plane can fly 
   */
   public boolean canFly(Graphics g) {
      if (AirlineMain.getOwnedAirports().size() < 2) { 
         AirlineMain.dashText("You must own multiple airports before flying.",g);
         return false;
      } else if (plane.isFlying()) {
         AirlineMain.dashText("This plane is already flying.",g);
         return false;
      } else {
         return true; 
      }
   }
   
   /**
   * draws in the text associated with the button
   * @param g the graphics object
   */
   @Override
   public void drawText(Graphics g) {
      // writes in the text
      g.setFont(new Font("Dialog", Font.BOLD, 16));
      g.setColor(getTextColor());
      
      String label = plane.getModelName();
      if (plane.isFlying()) {
         label = label + " flying from " + plane.getLocation().getText(); 
      } else {
         label = "Fly: " + label + " at " + plane.getLocation().getText();
      }
      g.drawString(label, getX()+3, getY() + 3*getHeight()/4);
   }
   
     
}