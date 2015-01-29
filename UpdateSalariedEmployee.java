import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.util.List;
import javax.swing.JComboBox;
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
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.Calendar;
import java.awt.FlowLayout;
import javax.swing.JOptionPane;

public class UpdateSalariedEmployee extends JFrame
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
	private JTextField txtDepartmentName;
	
	private JComboBox< Integer > cmbDay;
	private JComboBox< MonthsEnum > cmbMonth;
	private JComboBox< Integer > cmbYear;
	private JComboBox< String > cmbEmployeeType;
	private BirthdayComboBoxListener birthdayComboBoxListener;
	
	private JPanel pnlLblSocialSecurityNumber;
	private JPanel pnlLblFirstName;
	private JPanel pnlLblLastName;
	private JPanel pnlLblBirthday;
	private JPanel pnlLblEmployeeType;
	private JPanel pnlLblDepartmentName;
	
	private JPanel pnlTxtSocialSecurityNumber;
	private JPanel pnlTxtFirstName;
	private JPanel pnlTxtLastName;
	private JPanel pnlCmbBirthday;
	private JPanel pnlCmbEmployeeType;
	private JPanel pnlTxtDepartmentName;
	
	private Box boxSocialSecurityNumber;
	private Box boxFirstName;
	private Box boxLastName;
	private Box boxBirthday;
	private Box boxEmployeeType;
	private Box boxDepartmentName;
	
	private Box boxGenericEmployee;
	private JTable employeeTable;
	private JPanel tablePanel;
	private JButton btnSave;
	
	private ResultSetTableModel salariedEmployeeModel;
	
	private final String DRIVER;
	private final String DATABASE_URL;
	private final String USERNAME;
	private final String PASSWORD;
	
	private static final String SALARIED_EMPLOYEE_QUERY =
		"select socialSecurityNumber as \"Social Security Number\"" +
		", weeklySalary as \"Weekly Salary\"" +
		", bonus as \"Bonus\" from salariedEmployees";
	
	private static final String SALARIED_EMPLOYEE_SSN_QUERY =
		"select socialSecurityNumber from employees " +
		"where employeeType = 'Salaried Employee' "+
		"and socialSecurityNumber not in (select socialSecurityNumber from salariedEmployees)";

	public UpdateSalariedEmployee( String driver, String databaseURL, 
								  String username, String password )
	{
		super( "Generic Employee" );
		
		DRIVER = driver;
		DATABASE_URL = databaseURL;
		USERNAME = username;
		PASSWORD = password;
		
		try
		{
			salariedEmployeeModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME,
															PASSWORD, SALARIED_EMPLOYEE_QUERY );
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
		txtDepartmentName = new JTextField( 30 );
		
		cmbYear = new JComboBox< Integer >();
		cmbMonth = new JComboBox< MonthsEnum >();
		cmbDay = new JComboBox< Integer >();
		birthdayComboBoxListener = new BirthdayComboBoxListener();
		
		int currentYear = Calendar.getInstance().get( Calendar.YEAR );
		
		for( int i = currentYear; i >= currentYear - 100; i-- )
			cmbYear.addItem( i );
			
		for( MonthsEnum month : MonthsEnum.values() )
			cmbMonth.addItem( month );
		
		setDayComboBox( ( MonthsEnum ) cmbMonth.getSelectedItem(), ( int ) cmbYear.getSelectedItem() );
		
		cmbMonth.addItemListener( birthdayComboBoxListener );
		cmbYear.addItemListener( birthdayComboBoxListener );
		
		cmbEmployeeType = new JComboBox< String >( new String[]{ "Base Plus Commission Employee",
																 "Commission Employee",
																 "Hourly Employee",
																 "Salaried Employee" } );
		
		pnlLblSocialSecurityNumber = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		pnlLblSocialSecurityNumber.add( lblSocialSecurityNumber );
		pnlLblFirstName = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		pnlLblFirstName.add( lblFirstName );
		pnlLblLastName = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		pnlLblLastName.add( lblLastName );
		pnlLblBirthday = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		pnlLblBirthday.add( lblBirthday );
		pnlLblEmployeeType = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		pnlLblEmployeeType.add( lblEmployeeType );
		pnlLblDepartmentName = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		pnlLblDepartmentName.add( lblDepartmentName );
		
		pnlTxtSocialSecurityNumber = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		pnlTxtSocialSecurityNumber.add( txtSocialSecurityNumber );
		pnlTxtFirstName = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		pnlTxtFirstName.add( txtFirstName );
		pnlTxtLastName = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		pnlTxtLastName.add( txtLastName );
		pnlCmbBirthday = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		pnlCmbBirthday.add( cmbDay );
		pnlCmbBirthday.add( cmbMonth );
		pnlCmbBirthday.add( cmbYear );
		pnlCmbEmployeeType = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		pnlCmbEmployeeType.add( cmbEmployeeType );
		pnlTxtDepartmentName = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		pnlTxtDepartmentName.add( txtDepartmentName );
		
		boxSocialSecurityNumber = Box.createHorizontalBox();
		boxSocialSecurityNumber.add( pnlLblSocialSecurityNumber );
		boxSocialSecurityNumber.add( pnlTxtSocialSecurityNumber );
		boxFirstName = Box.createHorizontalBox();
		boxFirstName.add( pnlLblFirstName );
		boxFirstName.add( pnlTxtFirstName );
		boxLastName = Box.createHorizontalBox();
		boxLastName.add( pnlLblLastName );
		boxLastName.add( pnlTxtLastName );
		boxBirthday = Box.createHorizontalBox();
		boxBirthday.add( pnlLblBirthday );
		boxBirthday.add( pnlCmbBirthday );
		boxEmployeeType = Box.createHorizontalBox();
		boxEmployeeType.add( pnlLblEmployeeType );
		boxEmployeeType.add( pnlCmbEmployeeType );
		boxDepartmentName = Box.createHorizontalBox();
		boxDepartmentName.add( pnlLblDepartmentName );
		boxDepartmentName.add( pnlTxtDepartmentName );
		
		boxGenericEmployee = Box.createVerticalBox();
		boxGenericEmployee.add( boxSocialSecurityNumber );
		boxGenericEmployee.add( boxFirstName );
		boxGenericEmployee.add( boxLastName );
		boxGenericEmployee.add( boxBirthday );
		boxGenericEmployee.add( boxEmployeeType );
		boxGenericEmployee.add( boxDepartmentName );
		
		btnSave = new JButton( "Shave" );
		btnSave.addActionListener(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent event )
				{
					String errors = validateTextFields();
				
					if( errors.equals( "" ) )
						executeStatement( createInsertStatement() );
					else
						JOptionPane.showMessageDialog( null,
		  	                               			   "Cannot insert record. Please fix the following errors:\n\n" + errors,
		  	                               			   "Cannot Insert Record",
		  	                               			   JOptionPane.ERROR_MESSAGE
		  	                                         );
				}
			}
		);
		
		employeeTable = new JTable( salariedEmployeeModel );
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
	
	private class BirthdayComboBoxListener implements ItemListener
	{
		public void itemStateChanged( ItemEvent event )
		{
			if( event.getStateChange() == ItemEvent.SELECTED )
				setDayComboBox( ( MonthsEnum ) cmbMonth.getSelectedItem(), ( int ) cmbYear.getSelectedItem() );
		}
	}
	
	private void executeStatement( String sqlStatement )
	{
		Connection connection = null;
		Statement statement = null;
		
		try
		{
			Class.forName( DRIVER );
			connection = DriverManager.getConnection( DATABASE_URL, USERNAME, PASSWORD );
			statement = connection.createStatement();
			statement.executeUpdate( sqlStatement );
			salariedEmployeeModel.setQuery( SALARIED_EMPLOYEE_QUERY );
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
	
	private void setDayComboBox( MonthsEnum month, int year )
	{
		int currentlySelectedDay;
	
		if( cmbDay.getItemCount() > 0 )
			currentlySelectedDay = ( int ) cmbDay.getSelectedItem();
		else
			currentlySelectedDay = 1;
		
		cmbDay.removeAllItems();
		
		int numberOfDays = month.getNumberOfDays();
		
		if( month == MonthsEnum.FEBRUARY && year % 4 == 0 )
			numberOfDays++;
		
		for( int i = 1; i <= numberOfDays; i++ )
			cmbDay.addItem( i );
		
		if( cmbDay.getItemCount() < currentlySelectedDay )
			cmbDay.setSelectedIndex( cmbDay.getItemCount() - 1 );
		else
			cmbDay.setSelectedIndex( currentlySelectedDay - 1 );
	}
	
	private String validateTextFields()
	{
		String errors = "";
		
		if( !validateInt( txtSocialSecurityNumber.getText() ) )
			errors += "Social Security Number must be an integer\n";
		
		if( txtFirstName.getText().length() < 1 ||  txtFirstName.getText().length() > 30 )
			errors += "First Name must be between 1 and 30 characters\n";
		
		if( txtLastName.getText().length() < 1 ||  txtFirstName.getText().length() > 30 )
			errors += "Last Name must be between 1 and 30 characters\n";
		
		if( txtDepartmentName.getText().length() < 1 ||  txtFirstName.getText().length() > 30 )
			errors += "Department Name must be between 1 and 30 characters\n";
		
		return errors;
	}
	
	private static boolean validateInt( String value )
	{
		try
		{
			Integer.parseInt( value );
			return true;
		}
		catch( NumberFormatException numberFormatException )
		{
			return false;
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
			   "', '" + String.format( "%s-%s-%s", cmbYear.getSelectedItem().toString(),
			   								       ( ( MonthsEnum ) cmbMonth.getSelectedItem() ).getMonthNumber(),
			   								       cmbDay.getSelectedItem().toString()
			   						 ) +
			   "', '" + ( String ) cmbEmployeeType.getSelectedItem() +
			   "', '" + txtDepartmentName.getText() + "')";
	}
}