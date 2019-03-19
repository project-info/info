package touristoffice;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Hotel implements Serializable {

	private static final long serialVersionUID = 1L;

	private static int count=0;
	private static Set<Hotel> hotels = new HashSet<Hotel>();
	private static Map<String,Hotel> hotelsByName = new TreeMap<>();
	private static Map<Integer,Hotel> hotelsByID = new HashMap<>();
	
	//data container only so we can use package access
	int id;
	Category category;
	String name;
	String owner;
	String contact;
	String address;
	String city;
	String cityCode;
	String phone;
	int noRooms;
	int noBeds;
	
	public Hotel(Category category, String name, String owner, String contact, String address,
			     String city, String cityCode, String phone, int noRooms, int noBeds) {
		Hotel.checkData(true, category, name, owner, contact, address, city, cityCode, phone, noRooms, noBeds);
		
		id = count++;
		this.category = category;
		this.name = name;
		this.owner = owner;
		this.contact = contact;
		this.address = address;
		this.city = city;
		this.cityCode = cityCode;
		this.phone = phone;
		this.noRooms = noRooms;
		this.noBeds = noBeds;
	}
	
	public Hotel(Hotel h) {
		id = h.id;
		this.category = h.category;
		this.name = h.name;
		this.owner = h.owner;
		this.contact = h.contact;
		this.address = h.address;
		this.city = h.city;
		this.cityCode = h.cityCode;
		this.phone = h.phone;
		this.noRooms = h.noRooms;
		this.noBeds = h.noBeds;
	}

	private static final void checkData(boolean checkForUniqueName, Category category, String name, String owner,
				String contact, String address, String city, String cityCode, String phone, int noRooms, int noBeds) {
		Utils.checkStringNotEmpty(name,"Hotelname ist leer");
		Utils.checkStringNotEmpty(owner,"Hotelbesitzer ist leer");
		Utils.checkStringNotEmpty(contact,"Kontakt ist leer");
		Utils.checkStringNotEmpty(address,"Adresse ist leer");
		Utils.checkStringNotEmpty(city,"Stadt ist leer");
		Utils.checkStringNotEmpty(cityCode,"PLZ ist leer");
		Utils.checkStringNotEmpty(phone,"Telefonnummer ist leer");
		Utils.checkIntPositive(noRooms, "Anzahl Räume nicht positiv");
		Utils.checkIntPositive(noBeds, "Anzahl Betten nicht positiv");

		if (checkForUniqueName)
			Utils.checkUniqueName(name, getHotelNames(), "Hotelname bereits vergeben");
	}
	
	public void setData(Category category, String name, String owner, String contact, String address,
		     String city, String cityCode, String phone, int noRooms, int noBeds) {
		checkData(!this.name.equals(name), category, name, owner, contact, address, city, cityCode, phone, noRooms, noBeds);
		this.category = category;
		this.name = name;
		this.owner = owner;
		this.contact = contact;
		this.address = address;
		this.city = city;
		this.cityCode = cityCode;
		this.phone = phone;
		this.noRooms = noRooms;
		this.noBeds = noBeds;
	}

	public void setData(Hotel h) {
		assert(id == h.id);
		this.category = h.category;
		this.name = h.name;
		this.owner = h.owner;
		this.contact = h.contact;
		this.address = h.address;
		this.city = h.city;
		this.cityCode = h.cityCode;
		this.phone = h.phone;
		this.noRooms = h.noRooms;
		this.noBeds = h.noBeds;
	}
	
	public static Set<Hotel> getHotels() {
		return hotels;
	}
	
	public static Set<String> getHotelNames() {
		return hotelsByName.keySet();
	}
	
	public static Hotel getHotelByName(String name) {
		return hotelsByName.get(name);
	}
	
	public static void addHotel(Hotel h) {
		hotels.add(h);
		hotelsByName.put(h.name, h);
		hotelsByID.put(h.id, h);
	}
	
	public static Hotel updateHotel(Hotel h) {
		Hotel change = hotelsByID.get(h.id);
		Hotel old = new Hotel(change);
		
		if (!change.name.equals(h.name)) {
			hotelsByName.remove(change.name);
			hotelsByName.put(h.name, change);
		}
		change.setData(h);
		return old;
	}

	public static void updateCount(int newCount) {
		if (newCount < count)
			throw new IllegalArgumentException("Consistency check for hotel id failed!");
		count = newCount;
	}
}
