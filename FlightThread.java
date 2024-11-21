/*
* Andrew Z.  
* Y
* Final Project
* runs a flight animation in a separate thread
* FlightThread.java
* 7/24/2020
*/

// link that I used to help me: https://www.tutorialspoint.com/java/java_multithreading.htm

import java.util.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;

public class FlightThread implements Runnable {
   
   private Thread t;
   private Plane plane;
   private Airport departure;
   private Airport arrival; 
   private int numSeconds;
   private Set<FlightThread> allFlights; 
   private Graphics g; //graphics object 
   private double x;
   private double y;  
   
   /**
   * constructs a flight
   * @param plane the plane that will be flown
   * @param arrival the destination/arrival airport
   * @param allFlights the set of all flight threads
   * @param g graphics
   */
   public FlightThread (Plane plane, Airport arrival, 
                        Set<FlightThread> allFlights, Graphics g) {
         
      this.plane = plane;
      this.departure = plane.getLocation();
      this.arrival = arrival;
      this.g = g;
      this.allFlights = allFlights; 
      
      numSeconds = (int)(AirlineMain.getAirportDistance(departure,arrival)/plane.getSpeed());
      
      x = departure.getX();
      y = departure.getY(); 
      
   }
   
   /**
   * runs the thread
   */
   public void run() {
      
      Image usaMap = MyUtils.getImage("usamapgrayblue.png");
      // add this to all flights
      allFlights.add(this); 

      try {
         for(int i = 0; i < numSeconds; i++) {
            
            // draw the line from the departure to the current location
            drawRoute();
            
            // finding next position: 
            // casting to double to account for int division always rounding down to 0
            y = y + (double)(arrival.getY() - departure.getY())/(numSeconds); 
            x = x + (double)(arrival.getX() - departure.getX())/(numSeconds);
            
            // sleep for 1 second
            Thread.sleep(1000);
              
         } 
      } catch (InterruptedException e) {
         System.out.println("Flight thread interrupted.");
      }
      
      // remove this from all flights after done flying
      allFlights.remove(this);
      // sets the location to arrival location
      plane.setLocation(arrival); 
      // sets the fly status to false
      plane.setFlyStatus(false);
      // updates coins
      AirlineMain.coins = AirlineMain.coins + getProfit();
      // updates lifetime earnings
      AirlineMain.totalEarnings = AirlineMain.totalEarnings + getProfit(); 
      // updates total distance
      AirlineMain.totalPixels = AirlineMain.totalPixels + 
                                AirlineMain.getAirportDistance(departure,arrival);
      // redraw dashboard
      AirlineMain.getDashButtons();
      AirlineMain.drawDashboard(g);
      AirlineMain.dashText("Your " + plane.getModelName() + " has landed in " + 
                           arrival.getText() + ", earning you $" + (int)getProfit(),g);
      
      // redraw map  
      AirlineMain.drawMap(usaMap, g);
   }
   
   /**
   * starts new thread
   */
   public void start () {
      if (t == null) {
         plane.setFlyStatus(true);
         t = new Thread (this);
         t.start ();
      }
   }
   
   /**
   * Draws line between current position and departure city
   */
   public void drawRoute() {
      g.setColor(Color.RED);
      g.drawLine(departure.getX(), departure.getY(), (int)x, (int)y);
   }
   
   /**
   * gets the profit that this plane makes
   * @return the profit
   */
   public double getProfit() {
   
      // numpassengers * distance * profit rate for plane * average of airport profit factors
      double profit = getNumPassengers() * AirlineMain.getAirportDistance(departure, arrival) * 
                      plane.getProfit() * 
                      ((departure.getProfitFactor() + arrival.getProfitFactor())/2);
                      
      if (AirlineMain.advertisement) {
         // double profits if the user owns the advertisement
         profit = profit * 2;
      }
      
      return profit;
   }
   
   /**
   * gets the number of passengers riding the plane
   * @return the number of passengers
   */
   public int getNumPassengers() {
      // percentage of planes capacity is the sum of two airport profit factors / 4. 
      double percentage = (departure.getProfitFactor() + arrival.getProfitFactor())/4;
      // percentage if >= 1, then full capacity. 
      if (percentage > 1) {
         percentage = 1; 
      } 
      // multiply capaciy by percentage
      return (int)Math.round(plane.getCapacity() * percentage);
   }
   
   /**
   * gets the total expected flight time seconds
   * @return the flight time
   */
   public int getFlightTime() {
      return numSeconds;
   }
   
   @Override
   public String toString() {
      return departure.getText()+" to "+arrival.getText()+" on "+plane.getModelName(); 
   }   

}