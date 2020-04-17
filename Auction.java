/**
 *
 *  This is one of the basic class where an item called Auction is created.
 *
 */

import java.io.Serializable;

public class Auction implements Serializable {

    private int timeRemaining; // Amount of time remaining
    private double currentBid; // current bid of the auction
    private String auctionID, sellerName, buyerName, itemInfo; // information about the auction

    /**
     * Default Constructor for the Class
     */
    public Auction()
    {
        timeRemaining = -1;
        currentBid = -1.0;
        auctionID = "";
        sellerName = "";
        buyerName = "";
        itemInfo = "";
    }

    /**
     * Contructor for putAuction() method.
     *
     * @param time
     *      The time of the auction
     *
     * @param aucID
     *      Auction ID
     *
     * @param sellerName
     *      Name of the seller
     *
     * @param item
     *      Item information
     */
    public Auction(int time, String aucID, String sellerName, String item)
    {
        timeRemaining = time;
        auctionID = aucID;
        this.sellerName = sellerName;
        itemInfo = item;
    }

    /**
     * Contructor for the buildFromURL() method
     *
     * @param time
     *      The time of the auction
     *
     * @param biddername
     *      Name of the bidder
     *
     * @param bidAmt
     *      Bid on the current auction
     *
     * @param aucID
     *      Auction ID
     *
     * @param sellerName
     *      Name of the seller
     *
     * @param item
     *      Item information
     */
    public Auction(int time, String biddername, double bidAmt, String aucID, String sellerName, String item)
    {
        timeRemaining = time;
        buyerName = biddername;
        currentBid = bidAmt;
        auctionID = aucID;
        this.sellerName = sellerName;
        itemInfo = item;
    }

    /**
     * Returns the time remaining in the auction
     *
     * @return
     *      Integer with the remaining time.
     */
    public int getTimeRemaining() {
        return timeRemaining;
    }

    /**
     * Returns the bid on the auction
     *
     * @return
     *      Double with the current bid.
     */
    public double getCurrentBid() {
        return currentBid;
    }

    /**
     * Returns the auction ID of the auction
     *
     * @return
     *      String with the auction ID.
     */
    public String getAuctionID() {
        return auctionID;
    }

    /**
     * Returns the buyer's name in the auction
     *
     * @return
     *      String with the buyer's name.
     */
    public String getBuyerName() {
        return buyerName;
    }

    /**
     * Returns the item's information in the auction
     *
     * @return
     *      String with the item's information.
     */
    public String getItemInfo() {
        return itemInfo;
    }

    /**
     * Returns the seller's name in the auction
     *
     * @return
     *      String with the seller's name.
     */
    public String getSellerName() {
        return sellerName;
    }

    /**
     * Decreses the amount of time remaining in the auction
     *
     * @param time
     *      The amount of time to be decreased.
     *
     */
    public void decrementTimeRemaining(int time)
    {
        if(getTimeRemaining()<time)
        {
            timeRemaining = 0;
        }
        else
        {
            timeRemaining -= time;
        }
    }

    /**
     * Creating a new bid for the auction.
     *
     * @param biddername
     *      Name of the new bidder.
     *
     * @param bidAmt
     *      New bid amount.
     *
     * @throws ClosedAuctionException
     *      Exception thrown if the auction is closed.
     */
    public void newBid(String biddername, double bidAmt) throws ClosedAuctionException
    {
        if(timeRemaining <= 0)
        {
            throw new ClosedAuctionException();
        }

        else
        {
            if(bidAmt > getCurrentBid())
            {
                currentBid = bidAmt;
                buyerName = biddername;
            }
        }
    }

    /**
     * Prints the information about the auction.
     */
    public void printAuctionInfo()
    {
        System.out.println("Auction " + this.getAuctionID() + ":\n" +
                "\tSeller: " + this.getSellerName() + "\n" +
                "\tBuyer: "+ this.getBuyerName() + "\n" +
                "\tTime: "+ this.getTimeRemaining() + "\n" +
                "\tInfo: " + this.getItemInfo());
    }

    /**
     * Overwrites the toString method to format the information of an auction to a table format.
     *
     * @return
     *      A String with formatted table's information.
     */
    public String toString()
    {
        String ans = String.format(" %10s | $ %10.1f0 | %22s | %21s | %3d hours | %s", getAuctionID(), getCurrentBid(),
                getSellerName(), getBuyerName(), getTimeRemaining(), getItemInfo());

        return ans;
    }
}
