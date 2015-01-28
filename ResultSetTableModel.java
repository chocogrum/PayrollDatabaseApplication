import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

public class ResultSetTableModel extends AbstractTableModel
{
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;
	
	public ResultSetTableModel( String driver, String url, String username, String password, String query )
		throws SQLException, ClassNotFoundException
	{
		Class.forName( driver );
		connection = DriverManager.getConnection( url, username, password );
		statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
		setQuery( query );
	}
	
	public void setQuery( String query ) throws SQLException, IllegalStateException 
	{
		resultSet = statement.executeQuery( query );
		metaData = resultSet.getMetaData();
		resultSet.last();
		numberOfRows = resultSet.getRow();
		fireTableStructureChanged();
	}
	
	public int getRowCount()
	{
		return numberOfRows;
	}
	
	public int getColumnCount()
	{
		try 
		{
			return metaData.getColumnCount(); 
		}
		catch ( SQLException sqlException ) 
		{
			sqlException.printStackTrace();
		}
		
		return 0;
	}
	
	public String getColumnName( int column ) throws IllegalStateException
	{
		try 
		{
			return metaData.getColumnName( column + 1 );  
		}
		catch ( SQLException sqlException ) 
		{
			sqlException.printStackTrace();
		}
      
		return "";
	}
	
	public Object getValueAt( int row, int column )
	{
		try 
		{
			resultSet.absolute( row + 1 );
			return resultSet.getObject( column + 1 );
		}
			catch ( SQLException sqlException ) 
		{
			sqlException.printStackTrace();
		}

		return "";
	}
	
	public void disconnectFromDatabase()            
	{
		try                                          
		{                                            
			resultSet.close();                        
			statement.close();                        
			connection.close();                       
		}                               
			catch ( SQLException sqlException )          
		{                                            
			sqlException.printStackTrace();           
		}
	}
  
}