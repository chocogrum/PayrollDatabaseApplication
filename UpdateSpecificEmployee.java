// Project 4: UpdateSpecificEmployee.java
// Add a new specific type of employee to the database
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
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// the specific employee types have many elements in common which are represented by this class
// for example, every employee type has a Social Security Number
// the elements that differ between each of the employee types are represented by abstract methods
// many of the fields and methods in this class are declared as protected so they can be accessed by subclasses
// the abstract methods must be implemented by any subclass
// creating this class as abstract allows the common elements to be re-used across all employee types

// begin abstract class UpdateSpecificEmployee which inherits from JFrame
public abstract class UpdateSpecificEmployee extends JFrame
{
	protected JLabel lblSocialSecurityNumber; // Social Security Number label
	
	protected JComboBox< String > cmbSocialSecurityNumber; // Social Security Number

	private JTable tblData; // table to display query results
	private JPanel tablePanel; // panel to hold JTable
	private JButton btnSave; // button to save a specific employee record to the database
	
	protected ResultSetTableModel mainResultModel; // data model for the JTable
	protected ResultSetTableModel ssnResultModel; // data model for populating the Social Security Number combo box
	
	protected GridBagLayout gridBagLayout; // grid bag layout for arranging GUI components
	protected GridBagConstraints gridBagConstraints; // grid bag constraints for the grid bag layout
	
	protected final String DRIVER; // jdbc driver class
	protected final String DATABASE_URL; // database URL
	protected final String USERNAME; // username for accessing database
	protected final String PASSWORD; // password for accessing database
	protected final EmployeeTypesEnum EMPLOYEE_TYPE; // employee type that an object of this class represents
	protected final String TABLE_NAME; // name of the database table in which records of the specified employee type are stored
	protected final String MAIN_QUERY; // query used to populate JTable
	protected final String SOCIAL_SECURITY_NUMBER_QUERY; // query to return a list of Social Security Numbers with the specified employee type
	protected final int TEXT_FIELD_TEXT_LENGTH = 30; // maximum number of characters that can be entered into a text field
	private int numberOfTextFields = 0; // number of additional text fields added by a subclass implementation

	// begin constructor
	public UpdateSpecificEmployee( String driver, String databaseURL, 
								   String username, String password,
								   EmployeeTypesEnum employeeType )
	{
		super( employeeType.toString() ); // call superclass constructor and set the title of the JFrame
		
		EMPLOYEE_TYPE = employeeType; // initialise the employee type of this instance
		DRIVER = driver; // initialise the jdbc driver
		DATABASE_URL = databaseURL; // initialise the database URL
		USERNAME = username; // initialise the username for accessing the database
		PASSWORD = password; // initialise the password for accessing the database
		TABLE_NAME = EMPLOYEE_TYPE.getTableName(); // get the name of the table corresponding to the specified employee type
		MAIN_QUERY = "select * from " + TABLE_NAME; // initialise the query for populating the JTable
		
		// initialise the query to return all Social Security Numbers with the specified employee type
		SOCIAL_SECURITY_NUMBER_QUERY = "select socialSecurityNumber " +
									   "from employees " +
									   "where employeeType = '" + EMPLOYEE_TYPE.getEmployeesTableReference() + "'";
		
		try // begin try block
		{
			// initialise JTable data model for the specified employee type
			mainResultModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME, PASSWORD, MAIN_QUERY, EMPLOYEE_TYPE );
			
			// initialise data model for populating the Social Security Number combo box
			// using five argument constructor as employee type is not relevant for this result model
			ssnResultModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME, PASSWORD,
												   	  SOCIAL_SECURITY_NUMBER_QUERY );
			
			lblSocialSecurityNumber = new JLabel( "Social Security Number" ); // initialise Social Security Number label
		
			cmbSocialSecurityNumber = new JComboBox< String >(); // initialise Social Security Number combo box with String elements
			populateSSNComboBox(); // populate the Social Security Number combo box
		
			btnSave = new JButton( "Save" ); // initialise the Save button
			btnSave.addActionListener( // add an action listener to the Save button
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
									String.format( "Could not add %s to database\n%s",
										EMPLOYEE_TYPE.toString(),
										sqlException.getMessage() ), // message to display
									String.format( "Error Adding %s To Database",
										EMPLOYEE_TYPE.toString() ), // title of the message dialog
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
		
			tblData = new JTable( mainResultModel ); // initialise data table
			tblData.setGridColor( Color.BLACK ); // set table gridline colour to black
			tablePanel = new JPanel(); // initialise the table panel
			tablePanel.setLayout( new BorderLayout() ); // set border layout as the layout of the table panel
			
			// add data table to table panel within a scroll pane with center alignment
			tablePanel.add( new JScrollPane( tblData ), BorderLayout.CENTER );
		
			initialiseTextFields(); // call abstract method to initialise additional text fields
			
			gridBagLayout = new GridBagLayout(); // initialise grid bag layout
			gridBagConstraints = new GridBagConstraints(); // initialise grid bag constraints
			setLayout( gridBagLayout ); // set the layout of this frame to grid bag layout
		
			// add Social Security Number label and combo box to the frame with the specified constraints
			// component, gridx, gridy, gridwidth, gridheight, weightx, weighty, insets, anchor, fill
			addComponent( lblSocialSecurityNumber, 0, 0, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( cmbSocialSecurityNumber, 1, 0, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.EAST, gridBagConstraints.NONE );
			
			// call abstract method to add any additional text fields to the frame
			// method will return the number of text fields added
			numberOfTextFields = addTextFields();
			
			// add the save button and JTable to the frame with the specified constraints
			// the number of additional text fields added above will determine the position of these components
			// component, gridx, gridy, gridwidth, gridheight, weightx, weighty, insets, anchor, fill
			addComponent( btnSave, 0, numberOfTextFields + 1, 2, 1, 0, 0, new Insets( 5, 0, 5, 0 ), gridBagConstraints.CENTER, gridBagConstraints.NONE );
			addComponent( tablePanel, 0, numberOfTextFields + 2, 3, 1, 1, 1, new Insets( 5, 0, 5, 0 ), gridBagConstraints.CENTER, gridBagConstraints.BOTH );
			
			// add a window listener to this frame
			addWindowListener(
				new WindowAdapter() // declare anonymous inner class
				{
					// override method windowClosing
					// when window is closing, disconnect from the database if connected
					public void windowClosing( WindowEvent event )
					{
						if( mainResultModel != null ) // if the JTable data model has been initialised
							mainResultModel.disconnectFromDatabase(); // disconnect the data model from database
							
						if( mainResultModel != null ) // if the Social Security Number combo box data model has been initialised
							mainResultModel.disconnectFromDatabase(); // disconnect the data model from database
					} // end method windowClosing

					// override method windowActivated
					// when window is activated, update table model
					public void windowActivated( WindowEvent event )
					{
						try // begin try block
						{
							if( mainResultModel != null ) // if the data model has been iniitliased
								mainResultModel.setQuery( MAIN_QUERY ); // set the table model query
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
	
	// method to populate the Social Security Number combo box
	private void populateSSNComboBox()
	{
		cmbSocialSecurityNumber.removeAllItems(); // remove all existing items from the combo box
		for( int i = 0; i < ssnResultModel.getRowCount(); i++ ) // loop through all rows in the result set
		{
			// add each Social Security Number to the combo box
			cmbSocialSecurityNumber.addItem( ( String ) ssnResultModel.getValueAt( i, 0 ) );
		} // end for loop
		
		// set the preferred size of the combo box to have a width of 300
		// this will ensure the box still displays properly even if it contains no items
		cmbSocialSecurityNumber.setPreferredSize( new Dimension( 300, ( int ) cmbSocialSecurityNumber.getPreferredSize().getHeight() ) );
	} // end method populateSSNComboBox
	
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
			mainResultModel.setQuery( MAIN_QUERY ); // set the query for the generic employee table model
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
	
	// method to check if the specified text is a valid double number
	protected boolean validateDouble( String text )
	{
		try // begin try block
		{
			Double.parseDouble( text ); // attempt to parse the text to a double
			return true; // if this statement is reached, text is a valid double so return true
		} // end try block
		catch( NumberFormatException numberFormatException ) // catch NumberFormatException
		{
			// specified text is not a valid double
			numberFormatException.printStackTrace(); // print stack trace
		} // end catch NumberFormatException
		
		// if this statement is reached, text is not a valid double so return false
		return false;
	} // end method validateDouble
	
	// method to check if the specified text is a valid integer
	protected boolean validateInteger( String text )
	{
		try // begin try block
		{
			Integer.parseInt( text ); // attempt to parse the text to an integer
			return true; // if this statement is reached, text is a valid integer so return true
		} // end try block
		catch( NumberFormatException numberFormatException ) // catch NumberFormatException
		{
			// specified text is not a valid integer
			numberFormatException.printStackTrace(); // print stack trace
		} // end catch NumberFormatException
		
		// if this statement is reached, text is not a valid integer so return false
		return false;
	} // end method validateInteger
	
	// method to check if the specified text is longer than 30 characters
	protected boolean textFieldTextTooLong( String text )
	{
		// return true if text is longer than 30 characters otherwise return false
		return ( text.length() > TEXT_FIELD_TEXT_LENGTH ) ? true : false;
	} // end method textFieldTextTooLong
	
	// method to test if the specified text is zero in length
	protected boolean textFieldTextEmpty( String text )
	{
		// return true if text is zero in length otherwise return false
		return ( text.length() < 1 ) ? true : false;
	} // end method textFieldTextEmpty
	
	// add the specified component to the frame as per the specified grid bag constraints
	protected void addComponent( Component component,
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
	
	// abstract method to initialise any additional text fields
	protected abstract void initialiseTextFields();
	
	// abstract method to add any additional text fields to the frame and return the number of text fields added
	protected abstract int addTextFields();
	
	// abstract method to validate the text in the text fields and return a string containing any errors
	protected abstract String validateTextFields();
	
	// abstract method to create and return an insert statement for the employee type
	protected abstract String createInsertStatement();
} // end method UpdateSpecificEmployee