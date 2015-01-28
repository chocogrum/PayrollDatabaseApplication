import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.Box;
import java.sql.SQLException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AddEmployees extends JFrame
{
	private JButton btnGenericEmployee;
	private JButton btnSalariedEmployee;
	private JButton btnCommissionEmployee;
	private JButton btnBasePlusCommissionEmployee;
	private JButton btnHourlyEmployee;
	private JTable tblEmployeeTable;
	private JPanel pnlPanel1;
	private JPanel pnlPanel2;
	private JPanel pnlPanel3;
	private Box boxAddEmployees;
	private ResultSetTableModel genericEmployeeModel;
	private ResultSetTableModel salariedEmployeeModel;
	private ResultSetTableModel hourlyEmployeeModel;
	private ResultSetTableModel commissionEmployeeModel;
	private ResultSetTableModel basePlusCommissionEmployeeModel;
	
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String DATABASE_URL = "jdbc:mysql://localhost/payroll";
	private static final String USERNAME = "jhtp7";
	private static final String PASSWORD = "jhtp7";
	
	private static final String GENERIC_EMPLOYEE_QUERY =
		"select socialSecurityNumber as \"Social Security Number\"" +
		", firstName as \"First Name\"" +
		", lastName as \"Last Name\"" +
		", birthday as \"Birthday\"" +
		", employeeType as \"Employee Type\"" +
		", departmentName as \"Department Name\" from employees";
												     	 
	private static final String SALARIED_EMPLOYEE_QUERY =
		"select socialSecurityNumber as \"Social Security Number\"" +
		", weeklySalary as \"Weekly Salary\"" +
		", bonus as \"Bonus\" from salariedEmployees";
														  
	private static final String HOURLY_EMPLOYEE_QUERY =
		"select socialSecurityNumber as \"Social Security Number\"" +
		", hours as \"Hours\"" +
		", wage as \"Wage\"" +
		", bonus as \"Bonus\" from hourlyEmployees";
														
	private static final String COMISSION_EMPLOYEE_QUERY =
		"select socialSecurityNumber as \"Social Security Number\"" +
		", grossSales as \"Gross Sales\"" +
		", commissionRate as \"Commission Rate\"" +
		", bonus as \"Bonus\" from commissionEmployees";
														  
	private static final String BASE_PLUS_COMMISSION_EMPLOYEE_QUERY =
		"select socialSecurityNumber as \"Social Security Number\"" +
		", grossSales as \"Gross Sales\"" +
		", commissionRate as \"Commission Rate\"" +
		", baseSalary as \"Base Salary\"" +
		", bonus as \"Bonus\" from basePluscommissionEmployees";

	public AddEmployees()
	{
		super( "Add Employees" );
		
		try
		{
			genericEmployeeModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME,
															PASSWORD, GENERIC_EMPLOYEE_QUERY );
															
			salariedEmployeeModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME,
															 PASSWORD, SALARIED_EMPLOYEE_QUERY );
															 
			hourlyEmployeeModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME,
														   PASSWORD, HOURLY_EMPLOYEE_QUERY );
														   
			commissionEmployeeModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME,
															   PASSWORD, COMISSION_EMPLOYEE_QUERY );
															   
			basePlusCommissionEmployeeModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME,
																	   PASSWORD, BASE_PLUS_COMMISSION_EMPLOYEE_QUERY );
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
	
		btnGenericEmployee = new JButton( "Add Generic Employee" );
		btnGenericEmployee.addActionListener( new ButtonListener( genericEmployeeModel,
																  "Add Generic Employee" ) );
		
		btnSalariedEmployee = new JButton( "Add Salaried Employee" );
		btnSalariedEmployee.addActionListener( new ButtonListener( salariedEmployeeModel,
																   "Add Salaried Employee" ) );
		
		btnCommissionEmployee = new JButton( "Add Commission Employee" );
		btnCommissionEmployee.addActionListener( new ButtonListener( commissionEmployeeModel,
															         "Add Commission Employee" ) );
		
		btnBasePlusCommissionEmployee = new JButton( "Add Base Plus Commission Employee" );
		btnBasePlusCommissionEmployee.addActionListener( new ButtonListener( basePlusCommissionEmployeeModel,
																     		 "Add Base Plus Commission Employee" ) );
		
		btnHourlyEmployee = new JButton( "Add Hourly Employee" );
		btnHourlyEmployee.addActionListener( new ButtonListener( hourlyEmployeeModel,
																 "Add Hourly Employee" ) );
		
		tblEmployeeTable = new JTable( genericEmployeeModel );
		tblEmployeeTable.setGridColor( Color.BLACK );
		
		pnlPanel1 = new JPanel();
		pnlPanel1.add( btnGenericEmployee );
		
		pnlPanel2 = new JPanel();
		pnlPanel2.add( btnSalariedEmployee );
		pnlPanel2.add( btnCommissionEmployee );
		pnlPanel2.add( btnBasePlusCommissionEmployee );
		pnlPanel2.add( btnHourlyEmployee );
		
		pnlPanel3 = new JPanel();
		pnlPanel3.setLayout( new BorderLayout() );
		pnlPanel3.add( new JScrollPane( tblEmployeeTable ), BorderLayout.CENTER );
		
		boxAddEmployees = Box.createVerticalBox();
		boxAddEmployees.add( pnlPanel1 );
		boxAddEmployees.add( pnlPanel2 );
		boxAddEmployees.add( pnlPanel3 );
	
		add( boxAddEmployees );
		pack();
		setVisible( true );
		
		addWindowListener(
			new WindowAdapter()
			{
				public void windowClosing( WindowEvent event )
				{
					System.out.println( "Closing databases..." );
					genericEmployeeModel.disconnectFromDatabase();
					salariedEmployeeModel.disconnectFromDatabase();
					hourlyEmployeeModel.disconnectFromDatabase();
					commissionEmployeeModel.disconnectFromDatabase();
					basePlusCommissionEmployeeModel.disconnectFromDatabase();
					System.out.println( "Databases closed." );
				}
			}
		);
	}
	
	private class ButtonListener implements ActionListener
	{
		private ResultSetTableModel tableModel;
		private String frameTitle;
		
		public ButtonListener( ResultSetTableModel tableModel, String title )
		{
			this.tableModel = tableModel;
			frameTitle = title;
		}
		
		public void actionPerformed( ActionEvent actionEvent )
		{
			AddEmployeeFrame addEmployeeFrame = new AddEmployeeFrame( frameTitle, tableModel );
		}
	}
}