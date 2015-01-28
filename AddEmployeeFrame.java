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

public class AddEmployeeFrame extends JFrame
{
	/*private JLabel lblSocialSecurityNumber;
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
	private JPanel pnlDepartmentName;*/
	
	//private List< DatabaseInputPanel > panelList;
	
	private Box boxEmployee;
	private JTable employeeTable;
	private JPanel tablePanel;
	private JButton saveButton;

	public AddEmployeeFrame( String frameTitle, ResultSetTableModel tableModel )
	{
		super( frameTitle );
		boxEmployee = Box.createVerticalBox();
		//panelList = new ArrayList< DatabaseInputPanel >();
		
		for( int i = 0; i < tableModel.getColumnCount(); i++ )
		{
			boxEmployee.add( new DatabaseInputPanel( tableModel.getColumnName( i ) ) );
			//panelList.add( new DatabaseInputPanel( tableModel.getColumnName( i ) ) );
		}
		
		saveButton = new JButton( "Save" );
		
		employeeTable = new JTable( tableModel );
		employeeTable.setGridColor( Color.BLACK );
		
		tablePanel = new JPanel();
		tablePanel.setLayout( new BorderLayout() );
		tablePanel.add( new JScrollPane( employeeTable ), BorderLayout.CENTER );
		
		boxEmployee.add( saveButton );
		boxEmployee.add( tablePanel );
		
		add( boxEmployee );
		pack();
		setVisible( true );
		
		/*lblSocialSecurityNumber = new JLabel( "Social Security Number" );
		lblFirstName = new JLabel( "First Name" );
		lblLastName = new JLabel( "Last Name" );
		lblBirthday = new JLabel( "Birthday" );
		lblEmployeeType = new JLabel( "Employee Type" );
		lblDepartmentName = new JLabel( "Department Name" );
		
		txtSocialSecurityNumber = new JTextField( 1, 30 );
		txtFirstName = new JTextField( 1, 30 );
		txtLastName = new JTextField( 1, 30 );
		txtBirthday = new JTextField( 1, 30 );
		txtEmployeeType = new JTextField( 1, 30 );
		txtDepartmentName = new JTextField( 1, 30 );
		
		pnlSocialSecurityNumber = new JPanel();
		pnlSocialSecurityNumber.add( lblSocialSecurityNumber, txtSocialSecurityNumber );
		pnlFirstName = new JPanel();
		pnlFirstName.add( lblFirstName, txtFirstName );
		pnlLastName = new JPanel();
		pnlLastName.add( lblLastName, txtLastName );
		pnlBirthday = new JPanel();
		pnlBirthday.add( lblBirthday, txtBirthday );
		pnlEmployeeType = new JPanel();
		pnlEmployeeType.add( lblEmployeeType, txtEmployeeType );
		pnlDepartmentName = new JPanel();
		pnlDepartmentName.add( lblDepartmentName, txtDepartmentName );
		
		boxGenericEmployee = Box.createVerticalBox();
		boxGenericEmployee.add( pnlSocialSecurityNumber );
		boxGenericEmployee.add( pnlSocialSecurityNumber );
		boxGenericEmployee.add( pnlSocialSecurityNumber );
		boxGenericEmployee.add( pnlSocialSecurityNumber );
		boxGenericEmployee.add( pnlSocialSecurityNumber );
		boxGenericEmployee.add( pnlSocialSecurityNumber );*/
	}
}