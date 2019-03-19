package touristoffice;

import java.io.Serializable;

public class Occupancy implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final int maxHistoryYears = 5;
	
	//ID format is HHHHHHYYYYMM with H hotelID Y year and M month
    String id; 
    int noRooms;
    int noBeds;
    
    public Occupancy(String id, int noRooms, int noBeds) {
    	Utils.checkIntPositive(noRooms, "Anzahl Räume nicht positiv");
		Utils.checkIntPositive(noBeds, "Anzahl Betten nicht positiv");

    	this.id = id;
    	this.noRooms = noRooms;
    	this.noBeds = noBeds;
    }
    
    public String constructID(int hotelID, int year, int month) {
    	return String.format("%06d%04d%02d", hotelID, year, month);
    }
}
