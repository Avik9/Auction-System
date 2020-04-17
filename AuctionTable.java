/**
 *
 *
 *  This is the Auction Table class that contains a HashMap with all the auctions.
 *
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import big.data.DataSource;

public class AuctionTable extends HashMap implements Serializable
{
    public static HashMap<String, Auction> auctionTable; // the main HashMap for the class

    /**
     * Default constructor for the class.
     */
    public AuctionTable()
    {
        auctionTable = new HashMap<>();
    }

    /**
     * Returns the main HashMap of the class.
     *
     * @return
     *      The HashMap of the class
     */
    public static HashMap<String, Auction> getCurrentAuctionTable()
    {
        return auctionTable;
    }

    /**
     * Returns the auction with the given ID.
     *
     * @param id
     *      ID of the auction to be retrieved.
     *
     * @return
     *      Returns the auction that was asked for.
     */
    public static Auction getAuction(String id)
    {
        if(auctionTable.keySet().contains(id))
        {
            return auctionTable.get(id);
        }
        else
        {
            System.out.println("The action you requested for does not exist");

            return null;
        }
    }

    /**
     * Builds a HashMap by retrieving data from the URL given.
     *
     * @param URL
     *      URL to retrieve data from.
     *
     * @return
     *      Returns the Auction Table with all the data retrieved.
     *
     * @throws IllegalArgumentException
     *      Throws this when the URL is incorrect.
     */
    public AuctionTable buildFromURL(String URL) throws IllegalArgumentException
    {
        try
        {
            System.out.println("Loading ...");

            DataSource ds = DataSource.connect(URL).load();

            String[] sellerNames = ds.fetchStringArray("listing/seller_info/seller_name");
            String[] currentBid = ds.fetchStringArray("listing/auction_info/current_bid");
            String[] time = ds.fetchStringArray("listing/auction_info/time_left");
            String[] bidderName = ds.fetchStringArray("listing/auction_info/high_bidder/bidder_name");
            String[] auctionID = ds.fetchStringArray("listing/auction_info/id_num");
            String[] cpu = ds.fetchStringArray("listing/item_info/cpu");
            String[] memory = ds.fetchStringArray("listing/item_info/memory");
            String[] hardDrive = ds.fetchStringArray("listing/item_info/hard_drive");
            String[] itemInfo = new String[cpu.length];

            double bid;
            int[] days = new int[time.length];
            int[] hours = new int[time.length];

            for(int i = 0; i < cpu.length; i++) {
                itemInfo[i] = cpu[i] + " - " + memory[i] + " - " + hardDrive[i];

                currentBid[i] = (currentBid[i].trim()).substring(1);
                bid = Double.parseDouble(currentBid[i].replace(",", ""));

                if (time[i].contains("day")) {
                    days[i] = (Integer.parseInt((time[i].trim()).substring(0, time[i].indexOf("day") - 1))) * 24;
                }

                if (time[i].contains("hour")) {
                    hours[i] = Integer.parseInt((time[i].trim()).substring(time[i].indexOf(", ") + 2, time[i].indexOf("hours") - 1));
                } else if (time[i].contains("hrs") && !time[i].contains("day")) {
                    hours[i] = Integer.parseInt((time[i].trim()).substring(0, time[i].indexOf("hrs") - 1));
                }

                Auction temp = new Auction(days[i] + hours[i], bidderName[i], bid, auctionID[i], sellerNames[i], itemInfo[i]);

                this.putAuction(temp.getAuctionID(), temp);
            }

            System.out.println("Auction data loaded successfully!");
        }

        catch(Exception e)
        {
            System.out.println("Incorrect URL. Please try again");
        }

        return this;
    }

    /**
     * Adds an auction to the list.
     *
     * @param auctionID
     *      The auction ID of the auction being added.
     *
     * @param auction
     *      The auction being added.
     *
     * @throws IllegalArgumentException
     *      Throws the exception when the auction already exists in the Auction Table.
     *
     */
    public void putAuction(String auctionID, Auction auction) throws IllegalArgumentException
    {
        try
        {
            if (auctionTable.containsKey(auctionID))
            {
                throw new IllegalArgumentException();
            }

            else
            {
                auctionTable.put(auctionID, auction);
            }
        }

        catch(IllegalArgumentException i)
        {
            System.out.println("The Auction you are trying to add already exists. Check the table and try again.");
        }
    }

    /**
     * Passes the time by the inputted number of hours.
     *
     * @param numHours
     *      The number of hours to pass time by.
     *
     * @throws IllegalArgumentException
     *      Exception is thrown when the input type is incorrect.
     */
    public void letTimePass(int numHours) throws IllegalArgumentException
    {
        if(numHours < 0)
        {
            throw new IllegalArgumentException();
        }

        else
        {
            auctionTable.forEach((key, auction) ->
            {
                auctionTable.get(key).decrementTimeRemaining(numHours);
            });

            System.out.println("\nTime Passing ...\n\nAuction Times Updated.");
        }
    }

    /**
     * Removes all the expired auctions from the table.
     */
    public void removeExpiredAuctions()
    {
        ArrayList<String> remove = new ArrayList<>();

        auctionTable.forEach((key, auction) ->
        {
            if(auctionTable.get(key).getTimeRemaining() <= 0)
            {
                remove.add(key);
            }
        });

        for(String x : remove)
        {
            auctionTable.remove(x);
        }
    }

    /**
     * Prints the table in a neatly formatted manner.
     */
    public void printTable()
    {
        System.out.format(" %10s | \t   %s\t    |   \t   %s\t\t | \t\t\t%s\t\t | \t%3s\t | %s", "Auction ID", "Bid",
                "Seller", "Buyer", "Time", "Item Info");

        System.out.println("\n====================================================================================================================================");

        auctionTable.forEach((key, auction) ->
        {
            System.out.println(auctionTable.get(key).toString());
        });
    }
}
