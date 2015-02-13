// Project 4: AddEmployees.java
// Main application screen providing user options
// Name: Graham Thomas
// Student Number: 1479585

// import required classes
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.Box;
import java.sql.SQLException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.util.Calendar;
import javax.swing.JOptionPane;

// begin class AddEmployees which inherits from JFrame
public class AddEmployees extends JFrame
{
	private JButton btnGenericEmployee; // generic employee button
	private JButton btnSalariedEmployee; // salaried employee button
	private JButton btnCommissionEmployee; // commission employee button
	private JButton btnBasePlusCommissionEmployee; // base plus commission employee button
	private JButton btnHourlyEmployee; // hourly employee button
	private JButton btnIncreaseBaseSalary; // increase base salary for base plus commission employees button
	private JButton btnCalculateBonuses; // calculate employee bonuses button
	private JButton btnQueryDatabase; // query database button
	private JTable tblEmployeeTable; // table to display query results
	private JPanel pnlPanel1; // panel 1
	private JPanel pnlPanel2; // panel 2
	private JPanel pnlPanel3; // panel 3
	private JPanel pnlPanel4; // panel 4
	private Box boxAddEmployees; // box for arranging GUI components
	private ResultSetTableModel genericEmployeeModel; // data model for JTable
	
	private static final String DRIVER = "com.mysql.jdbc.Driver"; // jdbc driver class
	private static final String DATABASE_URL = "jdbc:mysql://localhost/employees"; // database URL
	private static final String USERNAME = "jhtp7"; // username for accessing database
	private static final String PASSWORD = "jhtp7"; // password for accessing database
	
	private static final String BASE_PLUS_COMMISSION_SALARY_INCREASE = "1.1"; // salary increase multiplier
	private static final String BIRTHDAY_BONUS = "100"; // birthday bonus amount
	private static final String SALES_BONUS = "100"; // sales bonus amount
	private static final String GROSS_SALES_TARGET = "10000"; // sales target over which sales bonus is applied
	private static final String GENERIC_EMPLOYEE_QUERY = "select * from employees"; // main query for JTable result set
	
	// SQL statement to update base salary for base plus commission employees
	private static final String UPDATE_BASE_SALARY = "update basePlusCommissionEmployees " +
													 "set baseSalary = baseSalary * " + BASE_PLUS_COMMISSION_SALARY_INCREASE;
	
	// SQL statement to apply bonuses for commission employees who have exceeded the sales target
	private static final String CALCULATE_COMMISSION_BONUS = "update commissionEmployees " +
															 "set bonus = " + SALES_BONUS + " " +
															 "where grossSales > " + GROSS_SALES_TARGET;

	// begin constructor
	public AddEmployees()
	{
		super( "Add Employees" ); // call superclass constructor with window title
		
		try // begin try block
		{
			// initialise JTable data model for a generic employee
			genericEmployeeModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME,
															PASSWORD, GENERIC_EMPLOYEE_QUERY,
															EmployeeTypesEnum.GENERIC_EMPLOYEE );
															
			btnGenericEmployee = new JButton( "Add Generic Employee" ); // initialise generic employee button
			
			// add action listener to generic employee button
			btnGenericEmployee.addActionListener(
				new ActionListener() // declare anonymous inner class
				{
					// override method actionPerformed
					public void actionPerformed( ActionEvent event )
					{
						// create a new UpdateGenericEmployee object
						UpdateGenericEmployee genericEmployee =
							new UpdateGenericEmployee( DRIVER, DATABASE_URL, USERNAME, PASSWORD );
					} // end method actionPerformed
				} // end anonymous inner class
			); // end registering of event handler for generic employee button
		
			btnSalariedEmployee = new JButton( "Add Salaried Employee" ); // initialise salaried employee button
			
			// add action listener to salaried employee button
			btnSalariedEmployee.addActionListener(
				new ActionListener() // declare anonymous inner class
				{
					// override method actionPerformed
					public void actionPerformed( ActionEvent event )
					{
						// create a new UpdateSalariedEmployee object
						UpdateSalariedEmployee salariedEmployee =
							new UpdateSalariedEmployee( DRIVER, DATABASE_URL, USERNAME, PASSWORD );
					} // end method actionPerformed
				} // end anonymous inner class
			); // end registering of event handler for salaried employee button
		
			btnCommissionEmployee = new JButton( "Add Commission Employee" ); // initialise commission employee button
			
			// add action listener to commission employee button
			btnCommissionEmployee.addActionListener(
				new ActionListener() // declare anonymous inner class
				{
					// override method actionPerformed
					public void actionPerformed( ActionEvent event )
					{
						// create a new UpdateCommissionEmployee object
						UpdateCommissionEmployee commissionEmployee =
							new UpdateCommissionEmployee( DRIVER, DATABASE_URL, USERNAME, PASSWORD );
					} // end method actionPerformed
				} // end anonymous inner class
			); // end registering of event handler for commission employee button
		
			// initialise base plus commission employee button
			btnBasePlusCommissionEmployee = new JButton( "Add Base Plus Commission Employee" );
			
			// add action listener to base plus commission employee button
			btnBasePlusCommissionEmployee.addActionListener(
				new ActionListener() // declare anonymous inner class
				{
					// override method actionPerformed
					public void actionPerformed( ActionEvent event )
					{
						// create a new UpdateBasePlusCommissionEmployee object
						UpdateBasePlusCommissionEmployee basePlusCommissionEmployee =
							new UpdateBasePlusCommissionEmployee( DRIVER, DATABASE_URL, USERNAME, PASSWORD );
					} // end method actionPerformed
				} // end anonymous inner class
			); // end registering of event handler for base plus commission employee button
		
			btnHourlyEmployee = new JButton( "Add Hourly Employee" ); // initialise hourly employee button
			
			// add action listener to hourly employee button
			btnHourlyEmployee.addActionListener(
				new ActionListener() // declare anonymous inner class
				{
					// override method actionPerformed
					public void actionPerformed( ActionEvent event )
					{
						// create a new UpdateHourlyEmployee object
						UpdateHourlyEmployee genericEmployee =
							new UpdateHourlyEmployee( DRIVER, DATABASE_URL, USERNAME, PASSWORD );
					} // end method actionPerformed
				} // end anonymous inner class
			); // end registering of event handler for hourly employee button
		
			// initialise increase salary button
			btnIncreaseBaseSalary = new JButton( "Increase Base Salary for Base Plus Commission Employees" );
			
			// add action listener to increase salary button
			btnIncreaseBaseSalary.addActionListener(
				new ActionListener() // declare anonymous inner class
				{
					// override method actionPerformed
					public void actionPerformed( ActionEvent event )
					{
						try // begin try block
						{
							// execute update statement to increase salary for base plus commission employees
							executeStatement( UPDATE_BASE_SALARY );
							
							// display confirmation message to user that salary has been updated
							JOptionPane.showMessageDialog(
								null, // use default parent component
								"Base salary updated for Base Plus Commission Employees", // message to display
								"Base Salary Updated", // title of the message dialog
								JOptionPane.INFORMATION_MESSAGE ); // message type is information
						} // end try block
						catch( SQLException sqlException ) // catch SQLException
						{
							// display message to user indicating that an error occurred
							JOptionPane.showMessageDialog(
								null, // use default parent component
								String.format( "Could not update base salary for Base Plus Commission Employees\n%s",
											   sqlException.getMessage() ), // message to display
								"Base Salary Not Updated", // title of the message dialog
								JOptionPane.ERROR_MESSAGE ); // message type is error
							sqlException.printStackTrace(); // print stack trade
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
					} // end method actionPerformed
				} // end anonymous inner class
			); // end registering of event handler for increase salary button
		
			btnCalculateBonuses = new JButton( "Calculate Bonuses" ); // initialise calculate bonuses button
			
			// add action listener to calculate bonuses button
			btnCalculateBonuses.addActionListener(
				new ActionListener() // declare anonymous inner class
				{
					// override method actionPerformed
					public void actionPerformed( ActionEvent event )
					{
						try // begin try block
						{
							// reset bonuses to 0 for each employee type
							resetBonuses( EmployeeTypesEnum.SALARIED_EMPLOYEE );
							resetBonuses( EmployeeTypesEnum.COMMISSION_EMPLOYEE );
							resetBonuses( EmployeeTypesEnum.BASE_PLUS_COMMISSION_EMPLOYEE );
							resetBonuses( EmployeeTypesEnum.HOURLY_EMPLOYEE );
							
							// apply sales bonus for commission employees
							executeStatement( CALCULATE_COMMISSION_BONUS );
							
							// apply birthday bonuses for each employee type
							addBirthdayBonuses( EmployeeTypesEnum.SALARIED_EMPLOYEE );
							addBirthdayBonuses( EmployeeTypesEnum.COMMISSION_EMPLOYEE );
							addBirthdayBonuses( EmployeeTypesEnum.BASE_PLUS_COMMISSION_EMPLOYEE );
							addBirthdayBonuses( EmployeeTypesEnum.HOURLY_EMPLOYEE );
							
							// display confirmation message to user that bonuses have been applied
							JOptionPane.showMessageDialog(
								null, // use default parent component
								"Bonuses calculated and applied", // message to display
								"Bonuses Calculated", // title of the message dialog
								JOptionPane.INFORMATION_MESSAGE ); // message type is information
						}
						catch( SQLException sqlException ) // catch SQLException
						{
							// display message to user indicating that an error occurred
							JOptionPane.showMessageDialog(
								null, // use default parent component
								String.format( "Could not calculate employee bonuses\n%s",
									sqlException.getMessage() ), // message to display
								"Bonuses Not Applied", // title of the message dialog
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
					} // end method actionPerformed
				} // end anonymous inner class
			); // end registering of event handler for calculate bonuses button
		
			btnQueryDatabase = new JButton( "Query Database" ); // initialise query database button
			
			// add action listener to query database button
			btnQueryDatabase.addActionListener(
				new ActionListener() // declare anonymous inner class
				{
					// override method actionPerformed
					public void actionPerformed( ActionEvent event )
					{
						// create a new QueryFrame object
						QueryFrame queryFrame = new QueryFrame( DRIVER, DATABASE_URL, USERNAME, PASSWORD );
					} // end method actionPerformed
				} // end anonymous inner class
			); // end registering of event handler for query database button
				
			tblEmployeeTable = new JTable( genericEmployeeModel ); // initialise data table
			tblEmployeeTable.setGridColor( Color.BLACK ); // set table gridline colour to black
		
			pnlPanel1 = new JPanel(); // initialise panel 1 using default layout
			pnlPanel1.add( btnGenericEmployee ); // add generic employee button to panel 1
		
			pnlPanel2 = new JPanel(); // initialise panel 2 using default layout
			pnlPanel2.add( btnSalariedEmployee ); // add salaried employee button to panel 2
			pnlPanel2.add( btnCommissionEmployee ); // add commission employee button to panel 2
			pnlPanel2.add( btnBasePlusCommissionEmployee ); // add base plus commission employee button to panel 2
			pnlPanel2.add( btnHourlyEmployee ); // add hourly employee button to panel 2
		
			pnlPanel3 = new JPanel(); // initialise panel 3
			pnlPanel3.setLayout( new BorderLayout() ); // apply border layout to panel 3
			
			// add data table to panel 3 within a scroll pane with center alignment
			pnlPanel3.add( new JScrollPane( tblEmployeeTable ), BorderLayout.CENTER );
		
			pnlPanel4 = new JPanel(); // initialise panel 4 using default layout
			pnlPanel4.add( btnIncreaseBaseSalary ); // add increase salary button to panel 4
			pnlPanel4.add( btnCalculateBonuses ); // add calculate bonuses button to panel 4
			pnlPanel4.add( btnQueryDatabase ); // add query database button to panel 4
		
			boxAddEmployees = Box.createVerticalBox(); // create a vertical box for adding GUI components
			boxAddEmployees.add( pnlPanel1 ); // add panel 1 to the box
			boxAddEmployees.add( pnlPanel2 ); // add panel 2 to the box
			boxAddEmployees.add( pnlPanel3 ); // add panel 3 to the box
			boxAddEmployees.add( pnlPanel4 ); // add panel 4 to the box
	
			add( boxAddEmployees ); // add the box to this frame
			pack(); // resize the frame to fit the preferred size of its components
			setVisible( true ); // set the frame to be visible
		
			// add a window listener to this frame
			addWindowListener(
				new WindowAdapter() // declare anonymous inner class
				{
					// override method windowClosing
					// when window is closing, disconnect from the database if connected
					public void windowClosing( WindowEvent event )
					{
						if( genericEmployeeModel != null ) // if the data model has been iniitliased
							genericEmployeeModel.disconnectFromDatabase(); // disconnect the data model from database
					} // end method windowClosing
					
					// override method windowActivated
					// when window is activated, update table model
					public void windowActivated( WindowEvent event )
					{
						try // begin try block
						{
							if( genericEmployeeModel != null ) // if the data model has been iniitliased
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
					} // end method windowActived
				} // end anonymous inner class
			); // end registering of event handler for the window
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
	
	// method to reset employee bonuses to 0 in the database
	private void resetBonuses( EmployeeTypesEnum employeeType ) throws SQLException, ClassNotFoundException
	{
		try // begin try block
		{
			// execute an update statement to set bonuses to 0 for the specified employee type
			executeStatement( "update " + employeeType.getTableName() + " " + "set bonus = 0" );
		} // end try block
		catch( SQLException sqlException ) // catch SQLException
		{
			throw sqlException; // re-throw exception
		} // end catch SQLException
		catch( ClassNotFoundException classNotFoundException ) // catch ClassNotFoundException
		{
			throw classNotFoundException; // re-throw exception
		} // end catch ClassNotFoundException
	} // end method resetBonuses
	
	// method to increase an employees' bonuses if their birthday falls in the current month
	private void addBirthdayBonuses( EmployeeTypesEnum employeeType ) throws SQLException, ClassNotFoundException
	{
		Calendar today = Calendar.getInstance(); // create a calendar instance
		int currentMonth = today.get( Calendar.MONTH ) + 1; // create a variable to hold the current month
		
		// SQL statement to add a birthday bonus for any employees of the specified type whose birthday is this month
		String employeeStatement = "update " + employeeType.getTableName() + " " +
								   "set bonus = bonus + " + BIRTHDAY_BONUS + " " +
								   "where socialSecurityNumber in (" +
								   "select socialSecurityNumber " +
								   "from employees " +
								   "where employeeType = '" + employeeType.getEmployeesTableReference() + "' " +
								   "and MONTH(birthday) = " + currentMonth + ")";
		
		try // begin try block
		{
			executeStatement( employeeStatement ); // execute the update statement
		} // end try block
		catch( SQLException sqlException ) // catch SQLException
		{
			throw sqlException; // re-throw the exception
		} // end catch SQLException
		catch( ClassNotFoundException classNotFoundException ) // catch ClassNotFoundException
		{
			throw classNotFoundException; // re-throw the exception
		} // end catch ClassNotFoundException
	} // end method addBirthdayBonuses
} // end class AddEmployees