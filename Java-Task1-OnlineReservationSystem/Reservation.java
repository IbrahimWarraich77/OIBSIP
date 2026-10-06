/**
 * Simple data holder for one booking.
 */
public class Reservation {
 
    private final String pnr;
    private final String passengerName;
    private final int trainNumber;
    private final String trainName;
    private final String classType;
    private final String journeyDate;
    private final String source;
    private final String destination;
 
    public Reservation(String pnr, String passengerName, int trainNumber, String trainName,
                       String classType, String journeyDate, String source, String destination) {
        this.pnr = pnr;
        this.passengerName = passengerName;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.classType = classType;
        this.journeyDate = journeyDate;
        this.source = source;
        this.destination = destination;
    }
 
    public String getPnr() {
        return pnr;
    }
 
    public String getPassengerName() {
        return passengerName;
    }
 
    public int getTrainNumber() {
        return trainNumber;
    }
 
    public String getTrainName() {
        return trainName;
    }
 
    public String getClassType() {
        return classType;
    }
 
    public String getJourneyDate() {
        return journeyDate;
    }
 
    public String getSource() {
        return source;
    }
 
    public String getDestination() {
        return destination;
    }
 
    /** Multi-line text used in confirmation and cancellation dialogs. */
    public String toDisplayText() {
        return "PNR Number     : " + pnr + "\n"
                + "Passenger Name : " + passengerName + "\n"
                + "Train          : " + trainNumber + " - " + trainName + "\n"
                + "Class          : " + classType + "\n"
                + "Date of Journey: " + journeyDate + "\n"
                + "From           : " + source + "\n"
                + "To             : " + destination;
    }
}