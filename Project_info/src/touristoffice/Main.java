package touristoffice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

public class Main {
	private static JFrame frame;
	
	private static String filenameHotels="hotels.dat";
	private static CapacityTableModel capacitiesModel = new CapacityTableModel();
	private static JTable capacitiesTable = new JTable(capacitiesModel);
	private static OccupancyTableModel occupanciesModel = new OccupancyTableModel();
	private static JTable occupanciesTable = new JTable(occupanciesModel);

	public static void main(String[] args) {
		if (!readData())
			return;
		SwingUtilities.invokeLater(Main::createAndShow);
	}
	

	private static void createAndShow() {
		frame = new JFrame("Touristeninformation");
		frame.setLayout(new BorderLayout());
		JLabel titleLabel = new JLabel("<HTML><SPAN style='color:yellow'>N</SPAN><SPAN style='color:white'>iederösterreich Touristeninformation</SPAN>");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(titleLabel.getFont().deriveFont(32.0f));
		titleLabel.setOpaque(true);
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBackground(Color.BLUE);
		frame.add(titleLabel,BorderLayout.NORTH);
		
		JTabbedPane tp = new JTabbedPane();
		tp.addTab("Hotels", createHotelsPanel());
		tp.addTab("Belegung", createOccupancyPanel());
		frame.add(tp,BorderLayout.CENTER);
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				writeData();
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowOpened(WindowEvent e) {
			}
			
		});
		frame.setVisible(true);
	}
	
	private static JPanel createHotelsPanel() {
		JPanel p = new JPanel(new BorderLayout());

		JPanel top = new JPanel(new FlowLayout());
		top.add(new JLabel("Hotel Übersicht:"));
		p.add(top,BorderLayout.NORTH);

		
		capacitiesTable.setPreferredScrollableViewportSize(new Dimension(capacitiesTable.getPreferredSize().width,
															   capacitiesTable.getRowHeight()*5));
		//right adjust table columns 1 to 3
		for (int col=1; col<4; ++col) {
			TableColumn column = capacitiesTable.getColumn(capacitiesTable.getColumnName(col));
			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(JLabel.RIGHT);
			column.setCellRenderer(renderer);
		}
		
		JScrollPane scrollpane = new JScrollPane(capacitiesTable);
		p.add(scrollpane,BorderLayout.CENTER);
	  
		JPanel buttonPanel = new JPanel(new GridLayout());
		JButton btnCreate = new JButton("Hotel anlegen");
		btnCreate.setActionCommand("create");
		btnCreate.addActionListener(Main::btnClickEditCreateHotel);
		buttonPanel.add(btnCreate);
		JButton btnEdit = new JButton("Hotel bearbeiten");
		btnEdit.setActionCommand("edit");
		btnEdit.addActionListener(Main::btnClickEditCreateHotel);
		buttonPanel.add(btnEdit);
		p.add(buttonPanel,BorderLayout.SOUTH);
		return p;
	}

	private static JPanel createOccupancyPanel() {
		JPanel p = new JPanel(new BorderLayout());

		JPanel top = new JPanel(new FlowLayout());
		top.add(new JLabel("Belegungen für:"));
		
		Calendar currDate = Calendar.getInstance();
		int currMonth = currDate.get(Calendar.MONTH);
		String months[] = new String[12];
		Locale locale = Locale.GERMAN;
		int pos = 0;
		for (Month m : Month.values())
			months[pos++] = m.getDisplayName(TextStyle.FULL, locale);
		JComboBox<String> comboMonth = new JComboBox<>(months);
		comboMonth.setSelectedIndex((currMonth+11)%12); //select previous month
		top.add(comboMonth);
		
		int currYear = currDate.get(Calendar.YEAR);
		Integer years[] = new Integer[Occupancy.maxHistoryYears+1];
		pos = 0;
		for (int i = currYear; i >= currYear-Occupancy.maxHistoryYears; --i)
			years[pos++] = i;
		JComboBox<Integer> comboYear = new JComboBox<>(years);
		comboYear.setSelectedIndex(currMonth == 11 ? 1 : 0);
		top.add(comboYear);
		
		p.add(top,BorderLayout.NORTH);
		
		occupanciesTable.setPreferredScrollableViewportSize(new Dimension(occupanciesTable.getPreferredSize().width,
															   occupanciesTable.getRowHeight()*5));
		//right adjust table columns 1 to 4
		for (int col=1; col<5; ++col) {
			TableColumn column = occupanciesTable.getColumn(occupanciesTable.getColumnName(col));
			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(JLabel.RIGHT);
			column.setCellRenderer(renderer);
		}
		
		JScrollPane scrollpane = new JScrollPane(occupanciesTable);
		p.add(scrollpane,BorderLayout.CENTER);
	  
		JPanel buttonPanel = new JPanel(new GridLayout());
		JButton btnEdit = new JButton("Belegungen bearbeiten");
		btnEdit.setActionCommand("create");
		btnEdit.addActionListener(Main::btnClickEditOccupancy);
		buttonPanel.add(btnEdit);
		p.add(buttonPanel,BorderLayout.SOUTH);
		return p;
	}
	
	private static boolean readData() {
	    try (FileInputStream fileIn = new FileInputStream(filenameHotels); 
	    	 ObjectInputStream in = new ObjectInputStream(fileIn)) {
	    	int maxID = -1;
		    while (true) {
		    	try {
		    		Hotel h = (Hotel) in.readObject();
		    		if (h.id > maxID)
		    			maxID = h.id;
		    		capacitiesModel.addHotel(h);
		    		Hotel.addHotel(h);
			    } catch(EOFException e) {
				    Hotel.updateCount(maxID+1);
			    	return true;
			    }
		    }
	    } catch(IOException e) {
	    	System.err.println("Fehler beim Dateizugriff");
	    	e.printStackTrace();
	    	return false;
	    } catch(ClassNotFoundException e) {
	    	System.err.println("Interner Programmfehler:");
	    	System.err.println("Hotel class not found");
	    	e.printStackTrace();
	    	return false;
	    } 
	}
	
	private static void writeData() {
	    try (FileOutputStream fileOut = new FileOutputStream(filenameHotels);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
	    	for (Hotel h : Hotel.getHotels())
	    		out.writeObject(h);
	    }
	    catch(IOException i) {
	    	System.err.println("Fehler beim Dateizugriff:");
	    	i.printStackTrace();
	    }	
	}
	
	private static void btnClickEditCreateHotel(ActionEvent e) {
		boolean create = e.getActionCommand().equals("create");
		DialogHotel dlg = new DialogHotel(frame);
		Hotel res = dlg.doDialog(create);
		if (res != null)
			if (create) {
				capacitiesModel.addHotel(res);
				Hotel.addHotel(res);
			}
			else {
				Hotel prev = Hotel.updateHotel(res);
				capacitiesModel.removeHotel(prev);
				capacitiesModel.addHotel(res);
			}
		capacitiesTable.repaint();
	}
	
	private static void btnClickEditOccupancy(ActionEvent e) {
		
	}
}
