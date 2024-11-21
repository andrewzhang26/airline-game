/*
* Andrew Z.
* Y
* Final Project
* Runs the main game for the airline
* AirlineMain.java
* 7/24/20
*/

import java.util.*; 
import java.awt.*; 
import java.io.*;
import javax.imageio.*;
// for clicking
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;



public class AirlineMain {  
   
   /**
   * the width of the window (map width)
   */
   public static final int WIDTH = 1360;
   
   /**
   * the height of the image (map height)
   */
   public static final int HEIGHT = 640;
   
   /**
   * the height of the dashboard in pixels (at bottom of screen)
   */
   public static final int DASHBOARD = 500;
   
   /**
   * the color of the dashboard 
   */
   public static final Color DASHBOARD_COLOR = new Color(40,40,40);

   public static Map<String,Airport> airports;
   public static ArrayList<Plane> planes;
   public static Set<FlightThread> allFlights;
   public static double coins; 
   public static Set<Button> dashButtons;
   public static boolean advertisement; 
   public static double totalEarnings;
   public static double totalPixels; 
   
   /**
   * the main program coordinating all tasks
   * @param args console input
   */
   public static void main(String[] args) {
      
      // holds all airports
      airports = getAirports(); 
      
      // holds planes owned by the user
      planes = new ArrayList<>();
      
      // holds all active flights
      allFlights = new HashSet<>(); 
      
      // user intro
      Scanner console = new Scanner(System.in);
      System.out.println("Welcome to the Realistic Airline Simulator!");
      System.out.println("   1) Load Saved Game");
      System.out.println("   2) New Game");
      int loadOperation = MyUtils.getNumber(console, "Select an option (1 or 2): ", 1, 2);
      
      // setting up variables from save file
      if (loadOperation == 1) {
         try {
            loadSave(console);
         } catch (Exception e) {
            System.out.println("Warning: the save could not be loaded correctly."); 
         }
      } else {
         // default coins
         coins = 50000;
         System.out.println("Loading new game...");
      }
      
      // setting up drawing panel and graphics
      DrawingPanel window = new DrawingPanel(WIDTH, HEIGHT+DASHBOARD);
      Graphics g = window.getGraphics(); 
      
      // drawing map
      Image usaMap = MyUtils.getImage("usamapgrayblue.png");
      drawMap(usaMap,g);
      
      // setting up dashboard
      getDashButtons(); 
      drawDashboard(g);
      updateCoins(g);
      dashText("Welcome to the Realistic Airline Simulator!", g);
      
      // setting up mouse listener
      MouseListener listener = new MouseListener(window,usaMap,g,console);
      window.addMouseListener(listener);
      
   }
   
   /**
   * loads a user's saved game data from a file
   * @param console user input
   */
   public static void loadSave(Scanner console) {
      Scanner saveFile = MyUtils.getUserFile(console, "Enter the name of the save file: ");
      
      // reading game stats data
      Scanner lineScanner = new Scanner(saveFile.nextLine()).useDelimiter(",");
      coins = lineScanner.nextDouble();
      totalEarnings = lineScanner.nextDouble();
      totalPixels = lineScanner.nextDouble();
      advertisement = lineScanner.nextBoolean(); 
      
      // airport data
      lineScanner = new Scanner(saveFile.nextLine()).useDelimiter(",");
      while(lineScanner.hasNext()) {
         airports.get(lineScanner.next()).setOwnership(true);
      }
      
      // plane data
      while(saveFile.hasNextLine()) {
         lineScanner = new Scanner(saveFile.nextLine()).useDelimiter(",");
         String planeName = lineScanner.next();
         String cityName = lineScanner.next();
         planes.add(new Plane(planeName, airports.get(cityName)));  
      }
      
   }
   
   /**
   * saves user's game data
   * @param console the scanner object for reading user input
   * @param g the graphics object
   */
   public static void saveData(Scanner console) { 
      // getting output file
      PrintStream ps = MyUtils.getOutFile(console, "Name of file to save game data to: ");
      // printing stats
      ps.println(coins+","+totalEarnings+","+totalPixels+","+advertisement);
      // printing all owned airports
      Set<String> ownedAirports = getOwnedAirports();
      for (String name : ownedAirports) {
         ps.print(name + ",");
      }
      ps.println();
      // printing all owned planes and their locations
      for (Plane p : planes) {
         ps.println(p.getModelName() + "," + p.getLocation().getText());
      }
      System.out.println("Game data saved! You may now close the program.");
   } 
   
   /**
   * reads the airport file and gets a map with all the airport objects
   * @return a map data structure with all the airport objects
   */
   public static Map<String, Airport> getAirports() {
      // gets a scanner on the airports file
      Scanner cityScan = MyUtils.getFile("airports.txt");
      // creates a Map to store the Airports
      Map<String, Airport> airports = new HashMap<>(); 
      while(cityScan.hasNextLine()) { 
         // read the comma separated line
         Scanner line = new Scanner(cityScan.nextLine()).useDelimiter(",");
         String name = line.next();
         int x = line.nextInt();
         int y = line.nextInt();
         double factor = line.nextDouble();
         // adds the Airport to the map using it's name as the key value
         airports.put(name,new Airport(name,x,y,factor));
      }
      return airports; 
   }
   
   /**
   * gets the set of airport names owned by user
   * @return the set of airport names
   */
   public static Set<String> getOwnedAirports() {
      Set<String> airportNames = airports.keySet(); 
      // new set of owned airports
      Set<String> toReturn = new HashSet<>(); 
      for (String airportName : airportNames) {
         // adds each owned airport to the toReturn set 
         if (airports.get(airportName).isOwned()) {
            toReturn.add(airportName);
         }
      }
      return toReturn;
   }

   /**
   * gets the dashboard buttons
   */
   public static void getDashButtons() {
   
      // initializing set
      dashButtons = new HashSet<>();
   
      // Buttons for buying planes:
      // creates and adds a new button for each of the different types of planes
      for (int i = 0; i < Plane.PLANE_NAMES.length && i < Plane.PLANE_PRICES.length; i++) {
         // sets the loation of the buy plane buttons in a column
         Button b = new BuyPlaneButton (10, HEIGHT + 10 + i*50, 250, 40, Color.RED, 
                                        Plane.PLANE_NAMES[i], Color.BLACK, Plane.PLANE_PRICES[i],
                                        planes);
         // adds the button to the arraylist of all dashboard buttons
         dashButtons.add(b);
      }
      
      // Buttons for initiating flights using planes: 
      for (int i = 0; i < planes.size(); i++) {
         // variables that anchor the top left the plane button display
         int w = 300; 
         int h = 40;
         int x = 280;
         int y = HEIGHT + 110; 
         int border = 10;
         // x, y, w, h, fill color, text color, plane
         // uses mod and int division to help determine where the button should be located so that
         // buttons appear in a table formation w/ rows and cols
         Button b = new PlaneButton (280 + (w+border)*(i%3), y + (h+border)*(i/3), w, h, 
                                     Color.BLUE, Color.WHITE, planes.get(i));
         dashButtons.add(b);
      }
      
      // Button for buying airport:
      Button b = new BuyAirportButton(280, HEIGHT + 60, 250, 40, Color.ORANGE, "Buy an Airport", 
                                      Color.BLACK);
      dashButtons.add(b);
      
      // BUtton for buying TV ad
      b = new AdButton(540, HEIGHT + 60, 250, 40, Color.MAGENTA, "TV Ad (2x profits)", 
                       Color.WHITE, 200000);
      dashButtons.add(b);
      
      // Button for saving game: 
      dashButtons.add(new SaveButton (1210, HEIGHT + 10, 140, 40, Color.GREEN, 
                                      "SAVE: see console", Color.BLACK));
      
   }
      
   /**
   * draws the dashboard
   * @param g the graphics object
   */
   public static void drawDashboard(Graphics g) {
      
      // draws the background fill for the dash
      g.setColor(DASHBOARD_COLOR);
      g.fillRect(0, HEIGHT, WIDTH, DASHBOARD);
      
      // draws all dashboard buttons in the set
      for (Button b : dashButtons) {
         b.draw(g);
         b.drawText(g);
      }
      
      // updates the coin and stats display
      updateCoins(g); 
      
   }   
   
   /**
   * draws the current number of coins to the dashboard (and other stats)
   */
   public static void updateCoins(Graphics g) {
      // all numbers are used to determine position on panel
      // covering old coin count
      g.setColor(DASHBOARD_COLOR); 
      g.fillRect(1210, HEIGHT + 60, 150, 160);
      
      // drawing new coins
      g.setColor(new Color(237,187,21));
      g.setFont(new Font("Dialog", Font.PLAIN, 20));
      g.drawString("$" + (int)coins, 1210, HEIGHT + 80); 
      
      // other stats: 
      g.setColor(Color.WHITE);
      g.drawString("Lifetime Stats:", 1210, HEIGHT + 120);
      // total earnings
      g.drawString("Earnings:", 1210, HEIGHT + 150);
      g.drawString("$" + (int)totalEarnings, 1210, HEIGHT + 170);
      // total pixels
      g.drawString("Pixels Travelled:", 1210, HEIGHT + 200);
      g.drawString((int)totalPixels + "", 1210, HEIGHT + 220);
      
   }

   /**
   * prints a message to the dashboard 
   * @param message the message to be drawn 
   * @param g the graphics object
   */
   public static void dashText(String message, Graphics g) {
      // fills/covers in the previous message
      g.setColor(DASHBOARD_COLOR);
      // numbers are made to fit in the correct position 
      g.fillRect(280, HEIGHT, 910, 50);
      // writes the new message 
      g.setColor(new Color(232, 26, 174));
      g.setFont(new Font("Dialog", Font.PLAIN, 20));
      // 280 and 680 are just a set position
      g.drawString(message, 280, 680);      
   }
   
   /**
   * draws the map screen to the panel
   * @param usaMap the usa map image
   * @param g the graphics object
   */
   public static void drawMap(Image usaMap,Graphics g) {
      
      // drawing image of US
      g.drawImage(usaMap, 0, 0, WIDTH, HEIGHT, null);
      
      // drawing airports on map
      Set<String> airportNames = airports.keySet(); 
      for (String airportName : airportNames) { 
         airports.get(airportName).draw(g); 
         airports.get(airportName).drawText(g);
      }
      
      // redraws routes if there are active flights
      if (!allFlights.isEmpty()) {
         for (FlightThread flight : allFlights) {
            flight.drawRoute();
         }
      }

   }
   
   /**
   * gets the distance in px between two airports
   * @param a1 the first airport
   * @param a2 the second airport
   * @return the distance
   */
   public static double getAirportDistance(Airport a1, Airport a2) {
      return MyUtils.distance(a1.getX(), a1.getY(), a2.getX(), a2.getY());
   }
   
   // constants for the different actions that airport buttons (on the map) perform
   public static enum AirportButton {
      DESTINATION, PLANE_LOCATION, PURCHASE;
   }
   
   /**
   * class for responding to mouse clicks
   */
   public static class MouseListener extends MouseInputAdapter 
   {
      private DrawingPanel panel;
      private Graphics g;
      private boolean useDashButtons;
      private boolean useAirportButtons;
      private AirportButton airportButtonUse; 
      private Plane tempPlane;
      private Image usaMap;
      private Scanner console; 
      
      /**
      * constructs mouse listener
      * @param panel the drawing panel
      * @param usaMap the image of the usa for displaying the map
      * @param g graphics object
      * @param console scanner for reading system.in
      */
      public MouseListener(DrawingPanel panel, Image usaMap, Graphics g, Scanner console) 
      {
         this.panel = panel;
         this.g = g;
         this.usaMap = usaMap;
         this.useDashButtons = true;
         this.useAirportButtons = false; 
         this.console = console;
      }
      
      /**
      * responds to mouse clicks
      * @param event the mouse event click
      */
      public void mousePressed(MouseEvent event) 
      {
         // get the x and y coords of the click
         int x = event.getX() / panel.getZoom();
         int y = event.getY() / panel.getZoom();
         
         if (useDashButtons) {
            // checks all the dashboard buttons
            for (Button b : dashButtons) {
               if (b.isHit(x,y)) {
               
                  // handles buy plane buttons
                  if (b instanceof BuyPlaneButton) {
                     // casting button to BuyPlaneBUtton
                     BuyPlaneButton buyB = (BuyPlaneButton)b;
                     if (buyB.canBuy(g)) { // prints message if can't buy 
                        // gets the plane at null location (will change) and changes coin count
                        tempPlane = buyB.buyPlane(null, g); 
                        // tempPlane shouldn't be null but technically possible
                        if (tempPlane != null) {
                           useDashButtons = false; 
                           useAirportButtons = true;
                           airportButtonUse = AirportButton.PLANE_LOCATION; 
                           updateCoins(g);
                           dashText("Coins Received! Select an airport you own to start flying "
                                    + "your new aircraft.",g);
                        }
                     }
                     
                  // handles buy airport button   
                  } else if (b instanceof BuyAirportButton) {
                     dashText("Select an airport on the map to buy. More expensive airports yield"
                              + " greater profits.",g);
                     useAirportButtons = true; 
                     // allows dash buttons to stay active if user wants to cancel
                     airportButtonUse = AirportButton.PURCHASE; 
                     // next click on map routes to purchase airport process
                  
                  // handles plane buttons (for starting flights)
                  } else if (b instanceof PlaneButton) {
                     PlaneButton planeB = (PlaneButton)b;
                     if (planeB.canFly(g)) {
                        tempPlane = planeB.getPlane();
                        
                        // draw circle (radius range) centered at airport the plane is located at
                        g.setColor(Color.RED);
                        int range = tempPlane.getRange();
                        g.drawOval(tempPlane.getLocation().getX() - range,
                                   tempPlane.getLocation().getY() - range, 2*range, 2*range);
                        
                        // redraw dashboard in case the red circle overlaps the dash
                        drawDashboard(g);
                        dashText("Select a destination city.",g);
                        useAirportButtons = true;
                        airportButtonUse = AirportButton.DESTINATION; 
                     }
                  
                  // TV ad button
                  } else if (b instanceof AdButton) {
                     AdButton adB = (AdButton)b;
                     if (adB.canBuy(g)) {
                        // update price and refresh dashboard
                        coins = coins - adB.getPrice();
                        advertisement = true; 
                        drawDashboard(g);
                        dashText("Purchase successful! All flights will now have 2x profits!",g);
                     } 
                  
                  } else if (b instanceof SaveButton) {
                     saveData(console);
                  }
                  
                  break; // breaks after a button is hit 
               }
            }   
         } 
         
         if (useAirportButtons) {
            // checks each airport in the set by looping through the key set
            Set<String> airportNames = airports.keySet(); 
            for (String a : airportNames) {
               if (airports.get(a).isHit(x,y)) {
               
                  // used for the second click in the plane purchasing process: choosing location
                  if (airportButtonUse == AirportButton.PLANE_LOCATION) {
                     if (airports.get(a).isOwned()) {
                        tempPlane.setLocation(airports.get(a));
                        planes.add(tempPlane);
                        useAirportButtons = false;
                        useDashButtons = true; 
                        
                        // updates dashboard buttons and adds plane to it
                        getDashButtons();
                        drawDashboard(g);
                        dashText("Congrats! You just bought a new " + tempPlane.getModelName(),g);
                     } else {
                        dashText("Please select an airport that you own.",g);
                     }
                  
                  // used for the second click in the plane flying process: choosing destination 
                  } else if (airportButtonUse == AirportButton.DESTINATION) {
                     if (airports.get(a).canFlyHere(tempPlane, tempPlane.getLocation(), g)) {
                        
                        // if the plane can fly to the destination, create new flight thread
                        FlightThread flight = new FlightThread(tempPlane, airports.get(a), 
                                                               allFlights, g);
                        
                        int numPassengers = flight.getNumPassengers();
                        double profit = flight.getProfit(); 
                        
                        // start thread
                        flight.start();
                        
                        // updating button status
                        useAirportButtons = false;
                        //refreshing map (get rid of circle)
                        drawMap(usaMap, g);
                        //refreshing dashboard
                        getDashButtons(); 
                        drawDashboard(g); 
                        dashText("Starting flight! Duration: " + flight.getFlightTime() + 
                                 "s. Passengers: "+numPassengers+"/"+tempPlane.getCapacity()+
                                 ". You will earn $" + (int)profit,g);                    
                     }
                  
                  // used for selecting an airport to purchase on the map
                  } else if (airportButtonUse == AirportButton.PURCHASE) {
                     // can only buy airport if user has enough coins and is not already owned
                     if (coins >= airports.get(a).getPrice() && !airports.get(a).isOwned()) {
                        // update airport status, dash messages and dashboard and button status
                        airports.get(a).setOwnership(true);
                        coins = coins - airports.get(a).getPrice(); 
                        dashText("Congrats! You just bought airport space at " + 
                                 airports.get(a).getText(),g);
                        useAirportButtons = false;
                        useDashButtons = true; 
                        // refresh main map after buying a new airport
                        drawMap(usaMap ,g);
                        updateCoins(g); 
                     } else {
                        dashText("You cannot buy this airport. Please select another one or do" +
                                 " something else.",g);
                     }
                  } 
                  
                  break; // break loop after a button found
               }
            }   
         }
      }   
   
   }
   
}