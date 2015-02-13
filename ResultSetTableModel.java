// Project 4: ResultSetTableModel.java
// Data model for a JTable
// Name: Graham Thomas
// Student Number: 1479585

// import required classes
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

// begin class as a concrete implementation of AbstractTableModel
public class ResultSetTableModel extends AbstractTableModel
{
	private Connection connection; // connection to the database
	private Statement statement; // Statement object for executing SQL statements and returning results
	private ResultSet resultSet; // ResultSet object for holding query results
	private ResultSetMetaData metaData; // meta data for the result set returned by a query
	private int numberOfRows; // number of rows in the result set
	private boolean connectedToDatabase = false; // flag to track connectivity to the database, assume false initially
	private final EmployeeTypesEnum EMPLOYEE_TYPE; // employee type relating to this table model
	
	// begin six argument constructor
	public ResultSetTableModel( String driver, String url, String username,
								String password, String query, EmployeeTypesEnum employeeType )
		throws SQLException, ClassNotFoundException
	{	
		EMPLOYEE_TYPE = employeeType; // initialise employee type
		
		try // begin try block
		{
			Class.forName( driver ); // test that driver class can be found
			
			// initialise a connection to the database usiong the specified url, username and password
			connection = DriverManager.getConnection( url, username, password );
			
			// initialise a statement object for executing queries
			// the result set will be scrollable, not sensitive to changes in underlying data and not updatable
			statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
			connectedToDatabase = true; // connection to database established
		} // end try block
		catch( SQLException sqlException ) // catch SQLException
		{
			// throw new exception including details of current exception
			throw new SQLException( "Error connecting to database: " + sqlException.getMessage() );
		} // end catch SQLException
		
		try // begin try block
		{
			setQuery( query ); // set the query for this table model
		} // end try block
		catch( SQLException sqlException ) // catch SQLException
		{
			throw sqlException; // re-throw exception
		} // end catch SQLException
		
	} // end six argument constructor
	
	// begin five argument constructor
	public ResultSetTableModel( String driver, String url, String username,
								String password, String query )
		throws SQLException, ClassNotFoundException
	{
		// call six argument constructor with a null value for employee type if it is not relevant
		this( driver, url, username, password, query, null );
	} // end five argument constructor
	
	// method to set the query for this table model
	public void setQuery( String query ) throws SQLException, IllegalStateException 
	{
		if ( !connectedToDatabase ) // if not connected to database
        	throw new IllegalStateException( "Not Connected to Database" ); // throw an IllegalStateException
        
        try // begin try block
        {
			resultSet = statement.executeQuery( query ); // execute query and store results
			metaData = resultSet.getMetaData(); // get meta data from query results
			resultSet.last(); // move to last item in result set
			numberOfRows = resultSet.getRow(); // set the number of rows in the result set
			fireTableStructureChanged(); // notify any JTables using this model that the data has changed
		} // end try block
		catch( SQLException sqlException ) // catch SQLException
		{
			// throw new exception including details of current exception
			throw new SQLException( String.format( "Error setting query\n%s\n%s", sqlException.getMessage(), query ) );
		} // end catch SQLException
	} // end method setQuery
	
	// method to return the class of the specified column
	public Class getColumnClass( int column ) throws IllegalStateException
	{
		if ( !connectedToDatabase ) // if not connected to database
        	throw new IllegalStateException( "Not Connected to Database" ); // throw an IllegalStateException

		try // begin try block
		{
			// attempt to get the class name of the specified column
			String className = metaData.getColumnClassName( column + 1 );

			// return an object of the class that matches the column name
			return Class.forName( className );
		} // end try block
		catch ( Exception exception ) // catch Exception
		{
			exception.printStackTrace(); // print stack trace
		} // end catch Exception
		
		// if errors occurred above, return an Object class type
		return Object.class;
	} // end method getColumnClass
	
	// method to return the number of rows in this table model
	public int getRowCount() throws IllegalStateException
	{
		if ( !connectedToDatabase ) // if not connected to database
        	throw new IllegalStateException( "Not Connected to Database" ); // throw an IllegalStateException

		return numberOfRows; // return the number of rows
	} // end method getRowCount
	
	// method to return the number of columns in this table model
	public int getColumnCount() throws IllegalStateException
	{
		if ( !connectedToDatabase ) // if not connected to database
        	throw new IllegalStateException( "Not Connected to Database" ); // throw an IllegalStateException
        	
		try // begin try block
		{
			return metaData.getColumnCount(); // return the number of columns
		} // end try block
		catch ( SQLException sqlException ) // catch SQLException
		{
			sqlException.printStackTrace(); // print stack trace
		} // end catch SQLException
		
		return 0; // if errors occurred above, return a column count of 0
	} // end method getColumnCount
	
	// method to return the name of a specified column
	public String getColumnName( int column ) throws IllegalStateException
	{
		if ( !connectedToDatabase ) // if not connected to database
        	throw new IllegalStateException( "Not Connected to Database" ); // throw an IllegalStateException
        	
		try // begin try block
		{
			if( EMPLOYEE_TYPE != null ) // if an employee type has been specified for this table model
			{
				// return the descriptive alias for this column as per the EmployeeTypesEnum
				return EMPLOYEE_TYPE.getColumnHeader( metaData.getColumnName( column + 1 ) );
			}
			else // no employee type was specified for this table model
				return metaData.getColumnName( column + 1 ); // return the column name
		} // end try block
		catch ( SQLException sqlException ) // catch SQLException
		{
			sqlException.printStackTrace(); // print stack trace
		} // end catch SQLException
		
		// if errors occurred above, return an empty string as the column name
		return "";
	} // end method getColumnName
	
	// method to return the value at a specified row and column
	public Object getValueAt( int row, int column ) throws IllegalStateException
	{
		if ( !connectedToDatabase ) // if not connected to database
        	throw new IllegalStateException( "Not Connected to Database" ); // throw an IllegalStateException
        	
		try // begin try block
		{
			resultSet.absolute( row + 1 ); // move to the specified row in the result set
			return resultSet.getObject( column + 1 ); // return the object at the specified column
		} // end try block
		catch ( SQLException sqlException ) // catch SQLException
		{
			sqlException.printStackTrace(); // print stack trace
		} // end catch SQLException

		// if errors occurred above, return a null string
		return "";
	} // end method getValueAt
	
	// method to disconnect from the database
	public void disconnectFromDatabase()
	{
		if( connectedToDatabase ) // if connected to database
		{
			try // begin try block
			{                                            
				resultSet.close(); // close the result set                        
				statement.close(); // close the statement object
				connection.close(); // close the connection to the database
			} // end try block
			catch ( SQLException sqlException ) // catch SQLException
			{                                            
				sqlException.printStackTrace(); // print stack trace
			} // end catch SQLException
			finally // code to execute when try/catch blocks are finished
			{
				connectedToDatabase = false; // indicate that database is no longer connected
			} // end finally block
		} // end if
	} // end method disconnectFromDatabase
} // end class ResultSetTableModel