/*
* Andrew Z. (template created by the CS Teachers) 
* Y
* Final Project
* Methods that are reusable
* MyUtils.java
* 7/24/2020
*/

import java.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.*;

public class MyUtils
{
   /**
   * Gets user integer input within a range and handles exceptions
   *
   * @param console the Scanner object that reads user input
   * @param prompt the message that the user is prompted with
   * @param min the minimum value (inclusive) that is accepted
   * @param max the maximum value (inclusive) that is accepted
   * @return the integer that the user inputted within the range specified  
   */
   public static int getNumber(Scanner console, String prompt, int min, int max) {
      while (true) {
         System.out.print(prompt);
         try {
            int number = console.nextInt();
            console.nextLine(); // clear scanner buffer
            if (number < min || number > max) { // if out of range
               System.out.println("Your number needs to be between "+min+" and "+max+".");   
            } else {
               return number; // return: breaks the infinite loop
            }
         } catch (Exception e) { // catches the exception that the user doesn't enter an int
            System.out.println("You must enter an *integer* between "+min+" and "+max+".");
            console.nextLine(); // clear scanner buffer
         }
      }
   }
   
   /**
   * Gets the distance between two points
   * @param x1 the x coord of first point
   * @param y1 the y coord of first point
   * @param x2 the x coord of second point
   * @param y2 the y coord of second point
   */
   public static double distance(int x1, int y1, int x2, int y2) {
      // distance formula/pythagorean theorem
      return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
   }
   
   /**
   * Gets a file or null if not found
   * @param fileName the name of the file
   * @return the Scanner on the file
   */
   public static Scanner getFile(String fileName) {
      try {
         Scanner fileScanner = new Scanner(new File(fileName));
         return fileScanner; 
      } catch (FileNotFoundException e) {
         e.printStackTrace();
         return null;  
      }
   }
   
   /**
   * Gets an image or null if not found
   * @param fileName the name of the file
   * @return the Scanner on the file
   */
   // https://stackoverflow.com/questions/7225957/java-awt-image-from-file
   public static Image getImage(String fileName) {
      try {
         Image img = ImageIO.read(new File(fileName));
         return img;
      } catch (Exception e) {
         e.printStackTrace();
         return null; 
      }
   }
   
   /**
   * Robustly gets a file from the user and creates a file scanner, handling exceptions
   *  
   * @param console the Scanner object that reads user input
   * @param prompt the message that the user is prompted with
   * @return the Scanner object for reading the file 
   */
   public static Scanner getUserFile(Scanner console, String prompt) {
      // keeps looping until valid filename given
      System.out.print(prompt);
      while (true) {
         String fileName = console.next(); 
         try { 
            Scanner fileScanner = new Scanner(new File(fileName));
            console.nextLine(); //clear scanner buffer in case user types in multiple words
            return fileScanner; // breaks while loop
         } catch (Exception e) { // catches the exception where file name isn't valid
            System.out.print("File not found. Try again: ");
            console.nextLine(); // clearing scanner buffer
         }
      }
   }
   
   /**
   * Robustly creates a PrintStream to open a file for writing
   *
   * @param console Scanner object for user input
   * @param prompt the message that the user is prompted with
   * @return the PrintStream object for writing to the file
   */
   public static PrintStream getOutFile(Scanner console, String prompt) {
      System.out.print(prompt);
      while (true) {
         String fileName = console.next(); 
         try { 
            PrintStream ps = new PrintStream(new File(fileName));
            console.nextLine(); //clear scanner buffer in case user types in multiple words
            return ps; // breaks while loop
         } catch (Exception e) { // catches the exception where file name isn't valid
            System.out.print("File not found. Try again: ");
            console.nextLine(); // clearing scanner buffer
         }
      }
   }
    
}