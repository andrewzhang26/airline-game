/*
* Andrew Z.
* Y
* Final Project
* Represents an aircraft
* Plane.java
* 7/24/20
*/

public class Plane {
   
   /**
   * the names of all the planes (in order of quality) 
   */
   public static final String[] PLANE_NAMES = new String[] {"Minijet 200", "Bowing 373", 
                                                            "Bowing 999", "Bowing 474", 
                                                            "Skybus 380", "Spaceship 2000"};
   
   /**
   * the prices of all the planes (corresponding with PLANE_NAMES)
   */
   public static final double[] PLANE_PRICES = new double[] {2000.0, 37300.0, 99000.0, 474000.0,
                                                             830000.0, 2000000.0};
   
   private String modelName; 
   private Airport location; 
   private int range; // maximum flying range
   private int capacity; // maximum passenger capacity
   private double profit; // profit per pixel per person
   // profit factor represents things like the plane's fuel efficieny and operational costs
   private int speed; // pixels per second
   private boolean flying; 
   
   /**
   * constructs a plane based on its model name with the appropriate stats
   * @param modelName name of the model of aircraft
   * @param location the airport at which it should be initially located
   */
   public Plane(String modelName, Airport location) {
      if (modelName.equals(PLANE_NAMES[0])) {
         // minijet
         range = 245;
         capacity  = 10;
         profit = 0.6;
         speed = 3;  
      } else if (modelName.equals(PLANE_NAMES[1])) {
         // bowing 373
         range = 479;
         capacity  = 18;
         profit = 0.7;
         speed = 4;  
      } else if (modelName.equals(PLANE_NAMES[2])) {
         // bowing 999
         range = 909;
         capacity  = 33;
         profit = 1.5;
         speed = 9;  
      } else if (modelName.equals(PLANE_NAMES[3])) {
         range = 1047;
         capacity  = 147;
         profit = 1.1;
         speed = 7;
      } else if (modelName.equals(PLANE_NAMES[4])) {
         // skybus
         range = 1183;
         capacity  = 380;
         profit = 1;
         speed = 6;  
      } else if (modelName.equals(PLANE_NAMES[5])) {
         // spaceship
         range = 2222;
         capacity  = 2;
         profit = 22;
         speed = 222;  
      } else {
         // if the model name doesn't match one of the set names
         throw new IllegalArgumentException();
      }
      // common assignments for all models of aircraft
      this.modelName = modelName; 
      this.location = location; 
      flying = false;
   }
   
   /**
   * gets the model name of the plane
   * @return the model name
   */
   public String getModelName() {
      return modelName; 
   }
      
   /**
   * gets the flying range of the plane
   * @return the flying range
   */
   public int getRange() {
      return range; 
   }
   
   /**
   * gets the passenger capacity of the plane
   * @return the capacity
   */
   public int getCapacity() {
      return capacity;
   }
   
   /**
   * gets the profit per pixel for this plane
   * @return the profit
   */
   public double getProfit() {
      return profit;
   }

   /**
   * gets the speed (pixels per second)
   * @return the profit
   */   
   public int getSpeed() {
      return speed;
   }
   
   /**
   * gets the plane's location 
   * @return the plane's airport
   */
   public Airport getLocation() {
      return location;
   }
   
   /**
   * set the plane's location
   */
   public void setLocation(Airport location) {
      this.location = location;
   }
   
   /**
   * gets the plane's flight status
   * @return whether or not the plane is flying
   */   
   public boolean isFlying() {
      return flying;
   }
   
   /**
   * sets the plane's flight status
   * @param whether or not the plane is flying
   */   
   public void setFlyStatus(boolean flying) {
      this.flying = flying;
   }   
   

   @Override
   public String toString() {
      return modelName + " at " + location.getText() + 
             ", Currently Flying: " + flying; //+
             //"\nMaximum Range: " + range +
             //"\nPassenger Capacity: " + capacity + 
             //"\nProfit Rate: " + profit +
             //"\nSpeed (px/s): " + speed;
   }
   
}