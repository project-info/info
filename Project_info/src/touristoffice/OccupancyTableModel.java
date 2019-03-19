package touristoffice;

import javax.swing.table.AbstractTableModel;

public class OccupancyTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private static String columnNames[] = {"Kategorie","Zimmer","Zimmerauslastung","Betten","Bettenauslastung"};
	private int[][] data = new int[5][4];

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public int getRowCount() {
		return 5;
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

		return "0";
	}

}
