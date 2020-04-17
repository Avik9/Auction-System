/**
 *
 *
 *  This is a main class where auction System is managed. The system consists of different auctions running together
 *  that are saved into an object for future use in order the program needs to shut. Reads the file written with the
 *  saved auction and even reads URLs that contain auction information.
 *
 */

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class AuctionSystem
{
    private static AuctionTable auctions = new AuctionTable(); // Creates an auction table for the class.
    private static String username = ""; // Starts without a username
    private static Scanner sc = new Scanner(System.in); // scanner for the class
    private static boolean quitRunning = false; // determines if the code should keep running

    /**
     * Main method that runs the entire project.
     *
     * @param args
     */
    public static void main(String[] args)
    {
        System.out.println("Starting ...\n");
        checkFile();

        System.out.print("Please select a username: ");
        username = sc.nextLine();

        System.out.println(username);

        while(!quitRunning)
        {
            menu();
        }
    }

    /**
     * Method checks if there is a saved file with previous auction. If there is a previous file, it is
     * loaded to work with.
     */
    public static void checkFile()
    {
        try
        {
            FileInputStream file = new FileInputStream("auction.obj");

            ObjectInputStream inStream = new ObjectInputStream(file);

            auctions.auctionTable = (HashMap<String, Auction>) inStream.readObject();

            inStream.close();

            System.out.println("Loading previous Auction Table ...");

        }

        catch(FileNotFoundException f)
        {
            System.out.println("No previous auction table detected.\n\nCreating new table...\n");
        }

        catch (ClassNotFoundException c)
        {
            System.out.println(c.toString());
        }

        catch (IOException i)
        {
            System.out.println(i.toString());
        }

    }

    /**
     * The menu that presents options to manipulate the auction system.
     */
    public static void menu()
    {
        System.out.println(
        "\nMenu:\n" +
            "\t(D) - Import Data from URL\n" +
            "\t(A) - Create a New Auction\n" +
            "\t(B) - Bid on an Item\n" +
            "\t(I) - Get Info on Auction\n" +
            "\t(P) - Print All Auctions\n" +
            "\t(R) - Remove Expired Auctions\n" +
            "\t(T) - Let Time Pass\n" +
//            "\t(S) - Save and Quit\n" +
            "\t(Q) - Quit");

        runMenu();
    }

    /**
     * Runs the main menu.
     */
    public static void runMenu()
    {
        System.out.print("\nPlease select an option: ");
        char letter = ((sc.next()).toUpperCase()).charAt(0);

        switch (letter)
        {
            case ('D'): importFromURL();
                break;

            case ('A'): createAuction();
                break;

            case ('B'): bid();
                break;

            case ('I'): getInfo();
                break;

            case ('P'): auctions.printTable();
                break;

            case ('R'): auctions.removeExpiredAuctions(); System.out.println("Removing expired auctions ... \n All expired auctions removed.");
                break;

            case ('T'): timePass();
                break;

            case ('Q'): saveAndQuit();
                break;

//            case ('Q'): quitRunning = true;
//                break;

            default: System.out.println("The option selected is incorrect. Please try again."); runMenu();
                break;
        }
    }

    /**
     *  Imports from the URL provided
     */
    public static void importFromURL()
    {
        System.out.println("Please enter a URL: ");
        auctions.buildFromURL(sc.next());
    }

    /**
     * Creates an auction from the user's input.
     */
    public static void createAuction() {
        System.out.println("Creating new Auction as " + username + ".");
        System.out.println("Please enter an Auction ID: ");
        String auctionID = sc.next();

        System.out.println("Please enter an Auction time (hours): ");
        int hours = sc.nextInt();

        System.out.println("Please enter some Item Info: ");
        String info = sc.nextLine();

        System.out.println();

        Auction tempAuction = new Auction(hours, auctionID, username, info);

        auctions.putAuction(tempAuction.getAuctionID(), tempAuction);

        System.out.println("Auction " + tempAuction.getAuctionID() + " inserted into table.");
    }

    /**
     * Changes the current bid in an auction
     */
    public static void bid()
    {
        try
        {
            System.out.print("Please enter an Auction ID: ");
            String auctionID = sc.next();

            Auction tempAuction = auctions.getCurrentAuctionTable().get(auctionID);

            System.out.println("Auction " + tempAuction.getAuctionID() + " is OPEN");

            if(tempAuction.getTimeRemaining() > 0)
            {
                System.out.println("Auction " + tempAuction.getAuctionID() + " is OPEN");

                if (tempAuction.getCurrentBid() > 0.0) {
                    System.out.println("\t\t Current Bid: " + tempAuction.getCurrentBid());
                } else {
                    System.out.println("\t\t Current Bid: NONE");


                    System.out.println("What would you like to bid?: ");
                    tempAuction.newBid(username, sc.nextDouble());
                }
            }
            else
            {
                System.out.println("Auction " + tempAuction.getAuctionID() + " is CLOSED");

                System.out.println("\t\t Current Bid: " + tempAuction.getCurrentBid());

                System.out.println("You can no longer bid on this item.");
            }

        }

        catch (ClosedAuctionException c)
        {
            System.out.println("The auction has been closed. Please try bidding on some other auction");
        }

        System.out.println("Bid accepted");
    }

    /**
     * Gets information about a selected Auction.
     */
    public static void getInfo()
    {
        System.out.print("Please enter an Auction ID: ");
        String auctionID = sc.next();

        if(auctions.getCurrentAuctionTable().keySet().contains(auctionID))
        {
            Auction currentAuction = auctions.getAuction(auctionID);

            currentAuction.printAuctionInfo();
        }

        else
        {
            System.out.println("The following auction does not exist on the table.");
        }
    }

    /**
     * Passes the time by certain hours.
     */
    public static void timePass()
    {
        System.out.print("How many hours should pass?: ");
        int time = sc.nextInt();

        auctions.letTimePass(time);
    }

    /**
     * Saves the program to a file and quits the program.
     */
    public static void saveAndQuit()
    {
        try
        {
            System.out.println("Writing Auction Table to file ...");

            File toDeleteFile = new File("auctionTable.obj");

            toDeleteFile.delete();

            FileOutputStream file = new FileOutputStream("auction.obj");

            ObjectOutputStream outStream = new ObjectOutputStream(file);

            outStream.writeObject(auctions.getCurrentAuctionTable());

            outStream.close();

            System.out.println("Done!\n\nGoodbye.");

            quitRunning = true;
        }

        catch (IOException i)
        {
            System.out.println(i.toString());
        }
    }
}
