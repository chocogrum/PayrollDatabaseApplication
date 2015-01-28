import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.util.List;
import java.util.ArrayList; 
import javax.swing.Box;
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

public class UpdateGenericEmployee extends JFrame
{
	private JLabel lblSocialSecurityNumber;
	private JLabel lblFirstName;
	private JLabel lblLastName;
	private JLabel lblBirthday;
	private JLabel lblEmployeeType;
	private JLabel lblDepartmentName;
	
	private JTextField txtSocialSecurityNumber;
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JTextField txtBirthday;
	private JTextField txtEmployeeType;
	private JTextField txtDepartmentName;
	
	private JPanel pnlSocialSecurityNumber;
	private JPanel pnlFirstName;
	private JPanel pnlLastName;
	private JPanel pnlBirthday;
	private JPanel pnlEmployeeType;
	private JPanel pnlDepartmentName;
	
	private Box boxGenericEmployee;
	private JTable employeeTable;
	private JPanel tablePanel;
	private JButton btnSave;
	
	private ResultSetTableModel genericEmployeeModel;
	
	private final String DRIVER;
	private final String DATABASE_URL;
	private final String USERNAME;
	private final String PASSWORD;
	
	private static final String GENERIC_EMPLOYEE_QUERY =
		"select socialSecurityNumber as \"Social Security Number\"" +
		", firstName as \"First Name\"" +
		", lastName as \"Last Name\"" +
		", birthday as \"Birthday\"" +
		", employeeType as \"Employee Type\"" +
		", departmentName as \"Department Name\" from employees";

	public UpdateGenericEmployee( String driver, String databaseURL, 
								  String username, String password )
	{
		super( "Generic Employee" );
		
		DRIVER = driver;
		DATABASE_URL = databaseURL;
		USERNAME = username;
		PASSWORD = password;
		
		try
		{
			genericEmployeeModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME,
															PASSWORD, GENERIC_EMPLOYEE_QUERY );
		}
		catch ( SQLException sqlException ) 
		{
			sqlException.printStackTrace();
			System.exit( 1 );
		}
		catch ( ClassNotFoundException classNotFoundException )
		{
			System.out.println( "Could not find database driver " + DRIVER );
			System.exit( 1 );
		}
		
		lblSocialSecurityNumber = new JLabel( "Social Security Number" );
		lblFirstName = new JLabel( "First Name" );
		lblLastName = new JLabel( "Last Name" );
		lblBirthday = new JLabel( "Birthday" );
		lblEmployeeType = new JLabel( "Employee Type" );
		lblDepartmentName = new JLabel( "Department Name" );
		
		txtSocialSecurityNumber = new JTextField( 30 );
		txtFirstName = new JTextField( 30 );
		txtLastName = new JTextField( 30 );
		txtBirthday = new JTextField( 30 );
		txtEmployeeType = new JTextField( 30 );
		txtDepartmentName = new JTextField( 30 );
		
		pnlSocialSecurityNumber = new JPanel();
		pnlSocialSecurityNumber.add( lblSocialSecurityNumber );
		pnlSocialSecurityNumber.add( txtSocialSecurityNumber );
		pnlFirstName = new JPanel();
		pnlFirstName.add( lblFirstName );
		pnlFirstName.add( txtFirstName );
		pnlLastName = new JPanel();
		pnlLastName.add( lblLastName );
		pnlLastName.add( txtLastName );
		pnlBirthday = new JPanel();
		pnlBirthday.add( lblBirthday );
		pnlBirthday.add( txtBirthday );
		pnlEmployeeType = new JPanel();
		pnlEmployeeType.add( lblEmployeeType );
		pnlEmployeeType.add( txtEmployeeType );
		pnlDepartmentName = new JPanel();
		pnlDepartmentName.add( lblDepartmentName );
		pnlDepartmentName.add( txtDepartmentName );
		
		boxGenericEmployee = Box.createVerticalBox();
		boxGenericEmployee.add( pnlSocialSecurityNumber );
		boxGenericEmployee.add( pnlFirstName );
		boxGenericEmployee.add( pnlLastName );
		boxGenericEmployee.add( pnlBirthday );
		boxGenericEmployee.add( pnlEmployeeType );
		boxGenericEmployee.add( pnlDepartmentName );
		
		btnSave = new JButton( "Save" );
		btnSave.addActionListener(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent event )
				{
					executeStatement( createInsertStatement() );
				}
			}
		);
		
		employeeTable = new JTable( genericEmployeeModel );
		employeeTable.setGridColor( Color.BLACK );
		
		tablePanel = new JPanel();
		tablePanel.setLayout( new BorderLayout() );
		tablePanel.add( new JScrollPane( employeeTable ), BorderLayout.CENTER );
		
		boxGenericEmployee.add( btnSave );
		boxGenericEmployee.add( tablePanel );
		
		add( boxGenericEmployee );
		pack();
		setVisible( true );
	}
	
	private void executeStatement( String insertStatement )
	{
		Connection connection = null;
		Statement statement = null;
		
		try
		{
			Class.forName( DRIVER );
			connection = DriverManager.getConnection( DATABASE_URL, USERNAME, PASSWORD );
			statement = connection.createStatement();
			statement.executeUpdate( insertStatement );
			statement.executeUpdate( "commit" );
			genericEmployeeModel.setQuery( GENERIC_EMPLOYEE_QUERY );
		}
		catch( SQLException sqlException )
		{
			sqlException.printStackTrace();
		}
		catch( ClassNotFoundException classNotFoundException )
		{
			classNotFoundException.printStackTrace();
		}
		finally
		{
			try
			{
				statement.close();
				connection.close();
			}
			catch( Exception exception )
			{
				exception.printStackTrace();
			}
		}
	}
	
	private String createInsertStatement()
	{
		return "insert into employees (socialSecurityNumber" +
			   ", firstName" +
			   ", lastName" +
		       ", birthday" +
			   ", employeeType" +
			   ", departmentName) values (" + txtSocialSecurityNumber.getText() +
			   ", '" + txtFirstName.getText() +
			   "', '" + txtLastName.getText() +
			   "', '" + txtBirthday.getText() +
			   "', '" + txtEmployeeType.getText() +
			   "', '" + txtDepartmentName.getText() + "')";
	}
}