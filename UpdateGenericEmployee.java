// Project 4: UpdateGenericEmployee.java
// Add a new generic employee to the database
// Name: Graham Thomas
// Student Number: 1479585

// import required classes
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox; 
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTable;
import java.awt.Color;
import javax.swing.JButton;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.Calendar;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// begin class UpdateGenericEmployee which inherits from JFrame
public class UpdateGenericEmployee extends JFrame
{
	private JLabel lblSocialSecurityNumber; // label for the Social Security Number text field
	private JLabel lblFirstName; // label for the First Name text field
	private JLabel lblLastName; // label for the Last Name text field
	private JLabel lblBirthday; // label for the Birthday combo boxes
	private JLabel lblEmployeeType; // label for the Employee Type combo box
	private JLabel lblDepartmentName; // label for the Department Name text field
	
	private JTextField txtSocialSecurityNumber; // Social Security Number text field
	private JTextField txtFirstName; // First Name text field
	private JTextField txtLastName; // Last Name text field
	private JTextField txtDepartmentName; // Department Name text field
	
	private JComboBox< Integer > cmbDay; // Birthday day combo box
	private JComboBox< MonthsEnum > cmbMonth; // Birthday month combo box
	private JComboBox< Integer > cmbYear; // Birthday year combo box
	private JComboBox< EmployeeTypesEnum > cmbEmployeeType; // Employee Type combo box
	private BirthdayComboBoxListener birthdayComboBoxListener; // listener for the Birthday combo box
	
	private JTable employeeTable; // table to display query results
	private JPanel tablePanel; // panel to hold JTable
	private JButton btnSave; // button to save a generic employee record to the database
	
	private ResultSetTableModel genericEmployeeModel; // data model for JTable
	
	private GridBagLayout gridBagLayout; // grid bag layout for arranging GUI components
	private GridBagConstraints gridBagConstraints; // grid bag constraints for the grid bag layout
	
	private final String DRIVER; // jdbc driver class
	private final String DATABASE_URL; // database URL
	private final String USERNAME; // username for accessing database
	private final String PASSWORD; // password for accessing database
	private final int TEXT_FIELD_TEXT_LENGTH = 30; // maximum number of characters that can be entered into a text field
	
	private static final String GENERIC_EMPLOYEE_QUERY = "select * from employees"; // main query for JTable result set

	// begin constructor
	public UpdateGenericEmployee( String driver, String databaseURL, 
								  String username, String password )
	{
		super( "Generic Employee" ); // call superclass constructor and set frame title
		
		DRIVER = driver; // set the jdbc driver
		DATABASE_URL = databaseURL; // set the database URL
		USERNAME = username; // set the username for accessing the database
		PASSWORD = password; // set the password for accessing the database
		
		try // begin try block
		{
			// initialise JTable data model for a generic employee
			genericEmployeeModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME,
															PASSWORD, GENERIC_EMPLOYEE_QUERY,
															EmployeeTypesEnum.GENERIC_EMPLOYEE );
															
			lblSocialSecurityNumber = new JLabel( "Social Security Number" ); // initialise Social Security Number label
			lblFirstName = new JLabel( "First Name" ); // initialise First Name label
			lblLastName = new JLabel( "Last Name" ); // initialise Last Name label
			lblBirthday = new JLabel( "Birthday" ); // initialise Birthday label
			lblEmployeeType = new JLabel( "Employee Type" ); // initialise Employee Type label
			lblDepartmentName = new JLabel( "Department Name" ); // initialise Department Name label
		
			txtSocialSecurityNumber = new JTextField( 30 ); // initialise Social Security Number text field with a column width of 30
			txtFirstName = new JTextField( 30 ); // initialise First Name text field with a column width of 30
			txtLastName = new JTextField( 30 ); // initialise Last Name text field with a column width of 30
			txtDepartmentName = new JTextField( 30 ); // initialise Department Name text field with a column width of 30
		
			cmbYear = new JComboBox< Integer >(); // initialise Birthday year combo box with Integer elements
			cmbMonth = new JComboBox< MonthsEnum >( MonthsEnum.values() ); // initialise Birthday month combo box with MonthsEnum elements
			cmbDay = new JComboBox< Integer >(); // initialise Birthday day combo box with Integer elements
			birthdayComboBoxListener = new BirthdayComboBoxListener(); // initialise Birthday combo box listener
		
			// obtain the current year from a Calendar instance
			int currentYear = Calendar.getInstance().get( Calendar.YEAR );
		
			// loop through all the years from the current year to 100 years ago
			for( int i = currentYear; i >= currentYear - 100; i-- )
				cmbYear.addItem( i ); // add the year to the Birthday year combo box
		
			// add items to the Birthday day combo box depending on the currently selected month and year
			setDayComboBox( ( MonthsEnum ) cmbMonth.getSelectedItem(), ( int ) cmbYear.getSelectedItem() );
		
			cmbMonth.addItemListener( birthdayComboBoxListener ); // add an item listener to the Birthday month combo box
			cmbYear.addItemListener( birthdayComboBoxListener ); // add an item listener to the Birthday year combo box
		
			cmbEmployeeType = new JComboBox< EmployeeTypesEnum >(); // initialise Employee Type combo box with EmployeeTypesEnum elements
			cmbEmployeeType.addItem( EmployeeTypesEnum.SALARIED_EMPLOYEE ); // add Salaried Employee to Employee Type combo box
			cmbEmployeeType.addItem( EmployeeTypesEnum.COMMISSION_EMPLOYEE ); // add Commission Employee to Employee Type combo box
			cmbEmployeeType.addItem( EmployeeTypesEnum.BASE_PLUS_COMMISSION_EMPLOYEE ); // add Base Plus Commission Employee to Employee Type combo box
			cmbEmployeeType.addItem( EmployeeTypesEnum.HOURLY_EMPLOYEE ); // add Hourly Employee to Employee Type combo box
		
			btnSave = new JButton( "Save" ); // initialise Save button
			btnSave.addActionListener( // add an action listener to the save button
				new ActionListener() // begin anonymous inner class
				{
					// override method actionPerformed
					public void actionPerformed( ActionEvent event )
					{
						// validate the text fields and capture any errors in a string
						String errors = validateTextFields();
				
						if( errors.equals( "" ) ) // if no errors
						{
							try // begin try block
							{
								executeStatement( createInsertStatement() ); // create an insert statement and execute it
							} // end try block
							catch( SQLException sqlException ) // catch SQLException
							{
								// display message to user indicating that an error occurred
								JOptionPane.showMessageDialog(
									null, // use default parent component
									String.format( "Could not add employee to database\n%s",
										sqlException.getMessage() ), // message to display
									"Error Adding Employee To Database", // title of the message dialog
									JOptionPane.ERROR_MESSAGE ); // message type is error
								sqlException.printStackTrace(); // print stack trace
							} // end catch SQLException
							catch ( ClassNotFoundException classNotFoundException ) // catch ClassNotFoundException
							{
								// display message to user indicating that an error occurred
								JOptionPane.showMessageDialog(
									null, // use default parent component
									classNotFoundException.getMessage(), // message to display
									"Error", // title of the message dialog
									JOptionPane.ERROR_MESSAGE ); // message type is error
								classNotFoundException.printStackTrace(); // print stack trace
							} // end catch ClassNotFoundException
						} // end if
						else // errors were encountered validating text fields
						{
							// display message to the user indicating the errors that need to be fixed
							JOptionPane.showMessageDialog( null, // use default parent component
														   "Cannot insert record. Please fix the following errors:\n\n" + errors, // message to display
														   "Cannot Insert Record", // title of the message dialog
														   JOptionPane.ERROR_MESSAGE ); // message type is error
						} // end else
					} // end method actionPerformed
				} // end anonymous inner class
			); // end registering of event handler for Save button
		
			employeeTable = new JTable( genericEmployeeModel ); // initialise data table
			employeeTable.setGridColor( Color.BLACK ); // set table gridline colour to black
			tablePanel = new JPanel(); // initialise the table panel
			tablePanel.setLayout( new BorderLayout() ); // set border layout as the layout of the table panel
			
			// add data table to table panel within a scroll pane with center alignment
			tablePanel.add( new JScrollPane( employeeTable ), BorderLayout.CENTER );
		
			gridBagLayout = new GridBagLayout(); // initialise grid bag layout
			gridBagConstraints = new GridBagConstraints(); // initialise grid bag constraints
			setLayout( gridBagLayout ); // set the layout of this frame to grid bag layout
		
			// add GUI components to the frame with the specified constraints
			// component, gridx, gridy, gridwidth, gridheight, weightx, weighty, insets, anchor, fill
			addComponent( lblSocialSecurityNumber, 0, 0, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( txtSocialSecurityNumber, 1, 0, 3, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( lblFirstName, 0, 1, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( txtFirstName, 1, 1, 3, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( lblLastName, 0, 2, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( txtLastName, 1, 2, 3, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( lblBirthday, 0, 3, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( cmbDay, 1, 3, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( cmbMonth, 2, 3, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( cmbYear, 3, 3, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( lblEmployeeType, 0, 4, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( cmbEmployeeType, 1, 4, 3, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( lblDepartmentName, 0, 5, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( txtDepartmentName, 1, 5, 3, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( btnSave, 1, 6, 2, 1, 0, 0, new Insets( 5, 0, 5, 0 ), gridBagConstraints.CENTER, gridBagConstraints.NONE );
			addComponent( tablePanel, 0, 7, 4, 1, 1, 1, new Insets( 5, 0, 5, 0 ), gridBagConstraints.CENTER, gridBagConstraints.BOTH );
			
			// add a window listener to this frame
			addWindowListener(
				new WindowAdapter() // declare anonymous inner class
				{
					// override method windowClosing
					// when window is closing, disconnect from the database if connected
					public void windowClosing( WindowEvent event )
					{
						if( genericEmployeeModel != null ) // if the data model has been initialised
							genericEmployeeModel.disconnectFromDatabase(); // disconnect the data model from database
					} // end method windowClosing

					// override method windowActivated
					// when window is activated, update table model
					public void windowActivated( WindowEvent event )
					{
						try // begin try block
						{
							if( genericEmployeeModel != null ) // if the data model has been initialised
								genericEmployeeModel.setQuery( GENERIC_EMPLOYEE_QUERY ); // set the table model query
						} // end try block
						catch( SQLException sqlException ) // catch SQLException
						{
							// display message to user indicating that an error occurred
							JOptionPane.showMessageDialog(
								null, // use default parent component
								sqlException.getMessage(), // message to display
								"Table Model Error", // title of the message dialog
								JOptionPane.ERROR_MESSAGE ); // message type is error
							sqlException.printStackTrace(); // print stack trace
							
							// if query cannot be set then frame is useless
							// so set the frame invisible and dispose of it
							setVisible( false ); // set the frame to be invisible
							dispose(); // dispose of this frame
						} // end catch SQLException
					} // end method windowActivated
				} // end anonymous inner class
			); // end registering of event handler for the window
		
			pack(); // resize the frame to fit the preferred size of its components
			setVisible( true ); // set this frame to be visible
		} // end try block
		catch ( SQLException sqlException ) // catch SQLException
		{
			// display message to user indicating that an error occurred
			JOptionPane.showMessageDialog(
				null, // use default parent component
				sqlException.getMessage(), // message to display
				"Table Model Error", // title of the message dialog
				JOptionPane.ERROR_MESSAGE ); // message type is error
			sqlException.printStackTrace(); // print stack trace
			
			// if table model cannot be initialised then frame is useless
			// so set the frame invisible and dispose of it
			setVisible( false ); // set the frame to be invisible
			dispose(); // dispose of this frame
		} // end catch SQLException
		catch ( ClassNotFoundException classNotFoundException )
		{
			// display message to user indicating that an error occurred
			JOptionPane.showMessageDialog(
				null, // use default parent component
				String.format( "Could not find database driver %s\n%s", // message to display
					DRIVER,
					classNotFoundException.getMessage() ),
				"Driver Not Found", // title of the message dialog
				JOptionPane.ERROR_MESSAGE ); // message type is error
			classNotFoundException.printStackTrace(); // print stack trace
			
			// if table model cannot be initialised then frame is useless
			// so set the frame invisible and dispose of it
			setVisible( false ); // set the frame to be invisible
			dispose(); // dispose of this frame
		} // end catch ClassNotFoundException
	} // end constructor
	
	// inner class to implement ItemListener interface
	private class BirthdayComboBoxListener implements ItemListener
	{
		// override method itemStateChanged
		public void itemStateChanged( ItemEvent event )
		{
			if( event.getStateChange() == ItemEvent.SELECTED ) // if an item has been selected
			{
				// refresh items in the Birthday day combo box depending on the currently selected month and year
				setDayComboBox( ( MonthsEnum ) cmbMonth.getSelectedItem(), ( int ) cmbYear.getSelectedItem() );
			} // end if
		} // end method itemStateChanged
	} // end inner class BirthdayComboBoxListener
	
	// method to execute a SQL statement
	private void executeStatement( String sqlStatement ) throws SQLException, ClassNotFoundException
	{
		Connection connection = null; // connection to the database
		Statement statement = null; // Statement object for executing the SQL statement
		
		try // begin try block
		{
			Class.forName( DRIVER ); // test that driver class can be found
			
			// initialise a connection to the database usiong the specified url, username and password
			connection = DriverManager.getConnection( DATABASE_URL, USERNAME, PASSWORD );
			
			statement = connection.createStatement(); // initialise a statement object for executing SQL statements
			statement.executeUpdate( sqlStatement ); // execute the SQL statement
			genericEmployeeModel.setQuery( GENERIC_EMPLOYEE_QUERY ); // set the query for the generic employee table model
		} // end try block
		catch( SQLException sqlException ) // catch SQLException
		{
			sqlException.printStackTrace();; // print stack trace
			
			// throw new exception including details of current exception
			throw new SQLException( String.format( "Error executing SQL statement\n%s\n%s", sqlException.getMessage(), sqlStatement ) );
		} // end catch SQLException
		catch( ClassNotFoundException classNotFoundException ) // catch ClassNotFoundException
		{
			classNotFoundException.printStackTrace(); // print stack trace
			
			// throw new exception including details of current exception
			throw new ClassNotFoundException( String.format( "Could not find database driver %s\n%s", DRIVER, classNotFoundException.getMessage() ) );
		} // end catch ClassNotFoundException
		finally // code to execute when try/catch blocks are finished
		{
			try // begin try block
			{
				statement.close();  // close the statement object
				connection.close(); // close the connection to the database
			} // end try block
			catch( Exception exception ) // catch Exception
			{
				exception.printStackTrace(); // print stack trace
			} // end catch Exception
		} // end finally block
	} // end method executeStatement
	
	// method to re-populate the items in the Birthday day combo box depending on the specified month and year
	private void setDayComboBox( MonthsEnum month, int year )
	{
		int currentlySelectedDay; // int to store the currently selected day
	
		if( cmbDay.getItemCount() > 0 ) // if combo box has already been populated
			currentlySelectedDay = ( int ) cmbDay.getSelectedItem(); // store the currently selected day
		else // combo box is not populated
			currentlySelectedDay = 1; // set the currently selected day to be 1
		
		cmbDay.removeAllItems(); // remove all items from the combo box
		
		int numberOfDays = month.getNumberOfDays(); // get the number of days in the specified month when not a leap year
		
		if( month == MonthsEnum.FEBRUARY && year % 4 == 0 ) // if the specified month is February and it is a leap year
			numberOfDays++; // add another day
		
		for( int i = 1; i <= numberOfDays; i++ ) // iterate through 1 to the number of days
			cmbDay.addItem( i ); // add the number to the combo box
		
		// if the previously selected day is greater than the number of items now in the combo box
		// eg previously selected day might have been 31 but combo box might now only contain 30 days
		if( cmbDay.getItemCount() < currentlySelectedDay )
			cmbDay.setSelectedIndex( cmbDay.getItemCount() - 1 ); // set the selected item to be the last in the combo box
		else // previously selected day is not greater than the number of items now in the combo box
			cmbDay.setSelectedIndex( currentlySelectedDay - 1 ); // set the selected item to be the previously selected item
	} // end method setDayComboBox
	
	// method to validate the data in the text fields in the frame
	private String validateTextFields()
	{
		String errors = ""; // initialise an empty string to hold any errors that might occur
		
		// if the Social Security Number text field contains more than 30 characters
		if( textFieldTextTooLong( txtSocialSecurityNumber.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is too long and should contain %d or less characters\n",
									 lblSocialSecurityNumber.getText(),
									 TEXT_FIELD_TEXT_LENGTH );
		}
		
		// if no data entered in the Social Security Number text field
		if( textFieldTextEmpty( txtSocialSecurityNumber.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is a required field\n",
									 lblSocialSecurityNumber.getText() );
		}
		
		try // begin try block
		{
			// if the Social Security Number entered already exists in the employees table
			if( ssnExistsInEmployees( txtSocialSecurityNumber.getText() ) )
			{
				// add error message to errors string
				errors += String.format( "Social Security Number %s already exists\n", txtSocialSecurityNumber.getText() );
			}
		} // end try block
		catch( SQLException sqlException ) // catch SQLException
		{
			// could not check employees table for Social Security Number so inform user
			errors += String.format( "DATABASE ERROR: Unable to determine if Social Security Number %s already exists in database\n",
				txtSocialSecurityNumber.getText() );
			sqlException.printStackTrace(); // print stack trace
		} // end catch SQLException
		catch ( ClassNotFoundException classNotFoundException )
		{
			// could not check employees table for Social Security Number so inform user
			errors += String.format( "DATABASE ERROR: Unable to determine if Social Security Number %s already exists in database\n",
				txtSocialSecurityNumber.getText() );
			classNotFoundException.printStackTrace(); // print stack trace
		} // end catch ClassNotFoundException
		
		// if the First Name text field contains more than 30 characters
		if( textFieldTextTooLong( txtFirstName.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is too long and should contain %d or less characters\n",
									 lblFirstName.getText(),
									 TEXT_FIELD_TEXT_LENGTH );
		}
		
		// if no data entered in the First Name text field
		if( textFieldTextEmpty( txtFirstName.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is a required field\n",
									 lblFirstName.getText() );
		}
		
		// if the Last Name text field contains more than 30 characters
		if( textFieldTextTooLong( txtLastName.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is too long and should contain %d or less characters\n",
									 lblLastName.getText(),
									 TEXT_FIELD_TEXT_LENGTH );
		}
		
		// if no data entered in the Last Name text field
		if( textFieldTextEmpty( txtLastName.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is a required field\n",
									 lblLastName.getText() );
		}
		
		// if the Department Name text field contains more than 30 characters
		if( textFieldTextTooLong( txtDepartmentName.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is too long and should contain %d or less characters\n",
									 lblDepartmentName.getText(),
									 TEXT_FIELD_TEXT_LENGTH );
		}
		
		// if no data entered in the Department Name text field
		if( textFieldTextEmpty( txtDepartmentName.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is a required field\n",
									 lblDepartmentName.getText() );
		}
		
		return errors; // return string containing errors
	} // end method validateTextFields
	
	// method to check whether the specified Social Security Number exists in the employees table
	private boolean ssnExistsInEmployees( String ssn ) throws SQLException, ClassNotFoundException
	{
		int numberOfRows = 0; // initialise number of rows returned by query to 0
		Connection connection = null; // connection to the database
		Statement statement = null; // Statement object for executing the SQL statement
		ResultSet ssnResultSet = null; // initialise a result set for storing the results of the query
		
		// query to return all records from the employees table for the specified Social Security Number
		String query =
		"select * " +
		"from employees " +
		"where socialSecurityNumber = '" + ssn + "'";

		try // begin try block
		{
			Class.forName( DRIVER ); // test that driver class can be found
			
			// initialise a connection to the database usiong the specified url, username and password
			connection = DriverManager.getConnection( DATABASE_URL, USERNAME, PASSWORD );
			statement = connection.createStatement(); // initialise a statement object for executing SQL statements
			ssnResultSet = statement.executeQuery( query ); // execute the query and store the results
			ssnResultSet.last(); // move to the last row of the result set
			numberOfRows = ssnResultSet.getRow(); // get the row number
		} // end try block
		catch( SQLException sqlException ) // catch SQLException
		{
			sqlException.printStackTrace(); // print stack trace
			
			// throw new exception including details of current exception
			throw new SQLException( String.format( "Error executing SQL query\n%s\n%s", sqlException.getMessage(), query ) );
		} // end catch SQLException
		catch( ClassNotFoundException classNotFoundException ) // catch ClassNotFoundException
		{
			classNotFoundException.printStackTrace(); // print stack trace
			
			// throw new exception including details of current exception
			throw new ClassNotFoundException( String.format( "Could not find database driver %s\n%s", DRIVER, classNotFoundException.getMessage() ) );
		} // end catch ClassNotFoundException
		finally // code to execute when try/catch blocks are finished
		{
			try // begin try block
			{
				statement.close();  // close the statement object
				connection.close(); // close the connection to the database
			} // end try block
			catch( Exception exception ) // catch Exception
			{
				exception.printStackTrace(); // print stack trace
			} // end catch Exception
		} // end finally block
		
		if( numberOfRows > 0 ) // if query returned at least one result
			return true; // Social Security Number already exists in employees table so return true
		else // query returned no rows
			return false; // Social Security Number does not exist in employees table so return false
	} // end method ssnExistsInEmployees
	
	// method to check if the specified text is longer than 30 characters
	private boolean textFieldTextTooLong( String text )
	{
		// return true if text is longer than 30 characters otherwise return false
		return ( text.length() > TEXT_FIELD_TEXT_LENGTH ) ? true : false;
	} // end method textFieldTextTooLong
	
	// method to test if the specified text is zero in length
	private boolean textFieldTextEmpty( String text )
	{
		// return true if text is zero in length otherwise return false
		return ( text.length() < 1 ) ? true : false;
	} // end method textFieldTextEmpty
	
	// add the specified component to the frame as per the specified grid bag constraints
	private void addComponent( Component component,
							   int column, int row,
							   int width, int height,
							   int weightx, int weighty,
							   Insets insets, int anchor, int fill )
	{
		gridBagConstraints.gridx = column; // set the gridx constraint
		gridBagConstraints.gridy = row; // set the gridy constraint
		gridBagConstraints.gridwidth = width; // set the gridwidth constraint
		gridBagConstraints.gridheight = height; // set the gridheight constraint
		gridBagConstraints.weightx = weightx; // set the weightx constraint
		gridBagConstraints.weighty = weighty; // set the weighty constraint
		gridBagConstraints.insets = insets; // set the insets constraint
		gridBagConstraints.anchor = anchor; // set the anchor constraint
		gridBagConstraints.fill = fill; // set the fill constraint
		gridBagLayout.setConstraints( component, gridBagConstraints ); // set the constraints for the component
		add( component ); // add the component to the frame
	} // end method addComponent
	
	// method to create an insert statement for a row in the employees table as per the data entered
	private String createInsertStatement()
	{
		// create and return the insert statement by gathering values entered in the GUI components
		return "insert into employees (socialSecurityNumber" +
			   ", firstName" +
			   ", lastName" +
		       ", birthday" +
			   ", employeeType" +
			   ", departmentName) values ('" + txtSocialSecurityNumber.getText() +
			   "', '" + txtFirstName.getText() +
			   "', '" + txtLastName.getText() +
			   "', '" + String.format( "%s-%s-%s", cmbYear.getSelectedItem().toString(),
			   								       ( ( MonthsEnum ) cmbMonth.getSelectedItem() ).getMonthNumber(),
			   								       cmbDay.getSelectedItem().toString()
			   						 ) +
			   "', '" + ( ( EmployeeTypesEnum )cmbEmployeeType.getSelectedItem() ).getEmployeesTableReference() +
			   "', '" + txtDepartmentName.getText() + "')";
	} // end method createInsertStatement
} // end class UpdateGenericEmployee