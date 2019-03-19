package touristoffice;

import javax.swing.table.AbstractTableModel;

public class CapacityTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private static String columnNames[] = {"Kategorie","Betriebe","Zimmer","Betten"};
	private int[][] data = new int[5][3];

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return Category.values().length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		
		if (col==0) {
			Category[] categories = Category.values();
			if (row == categories.length-1)
				return "Total";
			else if (row == categories.length-2)
				return categories[1]+" & "+categories[0];
			return categories[categories.length-1-row];
		}

		return data[row][col-1];
	}
	
	public void addHotel(Hotel h) {
		int size = Category.values().length-1;
		int row = size - h.category.ordinal();
		if (row == size) row = size-1;
		data[row][0]++;
		data[row][1] += h.noRooms;
		data[row][2] += h.noBeds;
		data[size][0]++;
		data[size][1] += h.noRooms;
		data[size][2] += h.noBeds;
	}
	
	public void removeHotel(Hotel h) {
		int size = Category.values().length-1;
		int row = size - h.category.ordinal();
		if (row == size) row = size-1;
		data[row][0]--;
		data[row][1] -= h.noRooms;
		data[row][2] -= h.noBeds;
		data[size][0]--;
		data[size][1] -= h.noRooms;
		data[size][2] -= h.noBeds;
	}
}
