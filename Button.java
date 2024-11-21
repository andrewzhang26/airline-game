/*
* Andrew Z.
* Y
* Final Project
* Represents a button on the main program user interface
* Button.java
* 7/24/20
*/

import java.awt.*; 

public abstract class Button { // abstract for the different types of buttons
   
   private int x;
   private int y;
   private int w;
   private int h;
   private Color fillColor;
   private String text; 
   private Color textColor;
   
   /**
   * constructs a button
   * @param x the x coordinate of the button's top left corner
   * @param y the y coordinate of the button's top left corner
   * @param w the width of the button
   * @param h the height of the button
   * @param color the color of the button
   * @param text the text that should appear on the button
   */
   public Button (int x, int y, int w, int h, Color fillColor, String text, Color textColor) {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.fillColor = fillColor; 
      this.text = text; 
      this.textColor = textColor; 
   }
   
   /**
   * gets the x coord of button
   * @return the x coord
   */
   public int getX() {
      return x;
   }
   
   /**
   * gets the y coord of button
   * @return the x coord
   */
   public int getY() {
      return y;
   }
   
   /**
   * gets the width of button
   * @return the width
   */
   public int getWidth() {
      return w;
   }
   
   /**
   * gets the height of button
   * @return the height
   */
   public int getHeight() {
      return h;
   }   
   
   /**
   * gets the button text
   * @return the text
   */
   public String getText() {
      return text;
   }
   
   /**
   * gets the text color
   * @return the text color
   */
   public Color getTextColor() {
      return textColor;
   }
   
   /**
   * gets the fill color
   * @return the fill color
   */
   public Color getColor() {
      return fillColor;
   }
   
   /**
   * sets the fill color
   */
   public void setColor(Color fillColor) {
      this.fillColor = fillColor; 
   }
   
   /**
   * decides whether or not a click was inside the rectangular button 
   * @param x the x coordinate of the click
   * @param y the y coordinate of the click
   * @return whether or not the button was hit
   */
   public boolean isHit(int x, int y) {
      // creates inclusive upper and lower bounds for x and y coordinates to fall in
      int xLower = this.x;
      // Minus 1 because pixels mark spaces. In order to achive getWidth() spaces/pixels
      // on the axis. (Don't want the final "fencepost")
      int xUpper = this.x + w - 1;
      // same process as x 
      int yLower = this.y;
      int yUpper = this.y + h - 1;
      // returns whether or not the coordinates fit within the bounds
      return xLower <= x && x <= xUpper && yLower <= y && y <= yUpper;
   }
   
   /**
   * draws the button to the Drawing Panel using a Graphics object
   * @param g the graphics object
   */
   public void draw(Graphics g) {
      // drawing filled rectangle with color
      g.setColor(fillColor); 
      g.fillRect(x, y, w, h); 
   }
   
   /**
   * draws in the text associated with the button
   * @param g the graphics object
   */
   public void drawText(Graphics g) {
      // writes in the text
      g.setColor(textColor);
      g.drawString(text, x + 3, y + 3*h/4);
   }
      
}