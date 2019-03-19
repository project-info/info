package touristoffice;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class DialogHotel {
	private JDialog dlg;
	private JFrame parent = null;
	private DefaultComboBoxModel<String> selectHotelModel;
	private JComboBox<String> selectHotel;
	private JComboBox<Category> selectCategory;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtZIP;
	private JTextField txtCity;
	private JTextField txtPhone;
	private JTextField txtOwner;
	private JTextField txtContact;
	private JTextField txtNoRooms;
	private JTextField txtNoBeds;

	private Hotel resHotel = null;
	private Hotel selectedHotel = null;
	private boolean create;
	
	public DialogHotel(JFrame parent) {
		this.parent = parent;
	}

	//to be called from within EDT
	public Hotel doDialog(boolean create) {
		resHotel = null;
		this.create = create;
		createAndShow();
		return resHotel;
	}

	private void createAndShow() {
		dlg = new JDialog(parent,"Hoteldaten Eingabe", true);
		dlg.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		int line = 0;

		if (create) {
			c.gridx = 0;
			c.gridy = line++;
			c.gridwidth = 3;
			c.fill = GridBagConstraints.HORIZONTAL;
			dlg.add(new JLabel("Neues Hotel erfassen"),c);
		}
		else {
			c.gridx = 0;
			c.gridy = line++;
			dlg.add(new JLabel("Auswahl:"),c);
			selectHotelModel = new DefaultComboBoxModel<String>(Hotel.getHotelNames().toArray(new String[1]));
			selectHotel = new JComboBox<>(selectHotelModel);
			selectHotel.addActionListener(this::hotelSelected);
			AutoCompletion.enable(selectHotel);
			c.gridx = 1;
			c.gridwidth = 3;
			c.fill = GridBagConstraints.HORIZONTAL;
			dlg.add(selectHotel,c);
		}

		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = line++;
		c.gridwidth = 1;
		dlg.add(new JLabel("Kategorie:"),c);
		selectCategory = new JComboBox<>(Category.values()); 
		c.gridx = 1;
		c.gridwidth = 1;
		dlg.add(selectCategory,c);

		c.gridx = 0;
		c.gridy = line++;
		c.gridwidth = 1;
		dlg.add(new JLabel("Name:"),c);
		txtName = new JTextField(40); 
		c.gridx = 1;
		c.gridwidth = 3;
		dlg.add(txtName,c);

		c.gridx = 0;
		c.gridy = line++;
		c.gridwidth = 1;
		dlg.add(new JLabel("Adresse:"),c);
		txtAddress = new JTextField(40); 
		c.gridx = 1;
		c.gridwidth = 3;
		dlg.add(txtAddress,c);

		c.gridx = 0;
		c.gridy = line++;
		c.gridwidth = 1;
		dlg.add(new JLabel("Plz:"),c);
		txtZIP = new JTextField(10); 
		c.gridx = 1;
		dlg.add(txtZIP,c);
		c.gridx = 2;
		dlg.add(new JLabel("Stadt:"),c);
		txtCity = new JTextField(20);
		c.gridx = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		dlg.add(txtCity,c);
		c.fill = GridBagConstraints.NONE;

		c.gridx = 0;
		c.gridy = line++;
		c.gridwidth = 1;
		dlg.add(new JLabel("Besitzer:"),c);
		txtOwner = new JTextField(40); 
		c.gridx = 1;
		c.gridwidth = 3;
		dlg.add(txtOwner,c);

		c.gridx = 0;
		c.gridy = line++;
		c.gridwidth = 1;
		dlg.add(new JLabel("Telefon:"),c);
		txtPhone = new JTextField(10); 
		c.gridx = 1;
		dlg.add(txtPhone,c);
		c.gridx = 2;
		dlg.add(new JLabel("Kontakt:"),c);
		txtContact = new JTextField(20);
		c.gridx = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		dlg.add(txtContact,c);
		c.fill = GridBagConstraints.NONE;

		c.gridx = 0;
		c.gridy = line++;
		c.gridwidth = 1;
		dlg.add(new JLabel("Raumanzahl:"),c);
		txtNoRooms = new JTextField(10); 
		txtNoRooms.setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1;
		dlg.add(txtNoRooms,c);
		c.gridx = 2;
		dlg.add(new JLabel("Bettenanzahl:"),c);
		txtNoBeds = new JTextField(10);
		txtNoBeds.setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 3;
		dlg.add(txtNoBeds,c);
		
		c.gridx = 0;
		c.gridy = line;
		c.gridwidth = 4;
		JPanel btnPanel = new JPanel(new GridLayout());
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(this::btnClickOK);
		btnPanel.add(btnOK);
		JButton btnCancel = new JButton("Abbrechen");
		btnCancel.addActionListener(this::btnClickCancel);
		btnPanel.add(btnCancel);
		c.anchor = GridBagConstraints.CENTER;
		dlg.add(btnPanel,c);

		if (!create && selectHotelModel.getSize() > 0)
			selectHotel.setSelectedIndex(0);
		
		dlg.pack();
		dlg.setVisible(true);
	}
	
	private void btnClickOK(ActionEvent e) {
		Category category = (Category)selectCategory.getSelectedItem();
		String name = txtName.getText();
		String owner = txtOwner.getText();
		String contact = txtContact.getText();
		String address = txtAddress.getText();
		String city = txtCity.getText();
		String cityCode = txtZIP.getText();
		String phone = txtPhone.getText();
		int noRooms = 0;
		int noBeds = 0;
		
		try {
			noRooms = Utils.readInteger(txtNoRooms,"Anzahl Räume muss eine ganze Zahl sein");
			noBeds = Utils.readInteger(txtNoBeds,"Anzahl Betten muss eine ganze Zahl sein");
			if (create) 
				resHotel = new Hotel(category, name, owner, contact, address, city, cityCode, phone, noRooms, noBeds);
			else
				if (selectedHotel != null) {
					resHotel = new Hotel(selectedHotel);
					resHotel.setData(category, name, owner, contact, address, city, cityCode, phone, noRooms, noBeds);
				}
			dlg.dispose();
		}
		catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(dlg, ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
		} 
	}
	
	private void btnClickCancel(ActionEvent e) {
		resHotel=null;
		dlg.dispose();
	}
	
	private void hotelSelected(ActionEvent e) {
		selectedHotel = Hotel.getHotelByName(selectHotel.getItemAt(selectHotel.getSelectedIndex()));
		
		selectCategory.setSelectedItem(selectedHotel.category);
		txtName.setText(selectedHotel.name); 
		txtOwner.setText(selectedHotel.owner);
		txtContact.setText(selectedHotel.contact);
		txtAddress.setText(selectedHotel.address);
		txtCity.setText(selectedHotel.city);
		txtZIP.setText(selectedHotel.cityCode);
		txtPhone.setText(selectedHotel.phone);
		txtNoRooms.setText(Integer.toString(selectedHotel.noRooms));
		txtNoBeds.setText(Integer.toString(selectedHotel.noBeds));
		
	}
}
