/*
* Andrew Z.
* Y
* Final Project
* Represents a button on the main program user interface
* SaveButton.java
* 7/24/20
*/

import java.util.*;
import java.awt.*; 

public class SaveButton extends Button {    
   
   /**
   * constructs a button
   * @param x the x coordinate of the button's top left corner
   * @param y the y coordinate of the button's top left corner
   * @param w the width of the button
   * @param h the height of the button
   * @param color the color of the button
   * @param text the text that should appear on the button
   */
   public SaveButton (int x, int y, int w, int h, Color fillColor, String text, 
                            Color textColor) {
      super(x,y,w,h,fillColor,text,textColor); 
   }
   
   @Override
   public void drawText(Graphics g) {
      // writes in the text
      g.setFont(new Font("Dialog", Font.BOLD, 15));
      super.drawText(g); 
   }
}