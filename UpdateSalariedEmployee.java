// Project 4: UpdateSalariedEmployee.java
// Concrete implementation of UpdateSpecificEmployee as a Salaried Employee
// Name: Graham Thomas
// Student Number: 1479585

// import required classes
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Insets;

// begin class UpdateSalariedEmployee which is an implementation of abstract class UpdateSpecificEmployee
public class UpdateSalariedEmployee extends UpdateSpecificEmployee
{
	private JLabel lblWeeklySalary; // Weekly Salary Label
	private JTextField txtWeeklySalary; // Weekly Salary Text Field

	// begin constructor
	public UpdateSalariedEmployee( String driver, String databaseURL, 
								   String username, String password )
	{
		// call superclass constructor for a Salaried Employee
		super( driver, databaseURL, username, password, EmployeeTypesEnum.SALARIED_EMPLOYEE );
	} // end constructor
	
	// override method addTextFields to add the text fields for a salaried employee to the frame
	protected int addTextFields()
	{
		// add Weekly Salary label and text field to the frame with the specified constraints
		// component, gridx, gridy, gridwidth, gridheight, weightx, weighty, insets, anchor, fill
		addComponent( lblWeeklySalary, 0, 1, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( txtWeeklySalary, 1, 1, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		return 1; // let the calling method know that one text field was added to the frame
	} // end method addTextFields
	
	// override method initialiseTextFields to initialise the GUI components for a salaried employee
	protected void initialiseTextFields()
	{
		lblWeeklySalary = new JLabel( "Weekly Salary" ); // initialise the Weekly Salary label
		txtWeeklySalary = new JTextField( TEXT_FIELD_TEXT_LENGTH ); // initialise the Weekly Salary text field with a column length of 30
	} // end method initialiseTextFields
	
	// override method validateTextFields to validate the data in the text fields in the frame
	protected String validateTextFields()
	{
		String errors = ""; // initialise an empty string to hold any errors that might occur

		// if the Weekly Salary text field contains more than 30 characters
		if( textFieldTextTooLong( txtWeeklySalary.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is too long and should contain %d or less characters\n",
									 lblWeeklySalary.getText(),
									 TEXT_FIELD_TEXT_LENGTH );
		}
		
		// if no data entered in the Social Security Number text field
		if( textFieldTextEmpty( txtWeeklySalary.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is a required field\n",
									 lblWeeklySalary.getText() );
		}
		
		// if data entered in the Weekly Salary text field is not a valid double
		if( !validateDouble( txtWeeklySalary.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s must be a valid number\n", lblWeeklySalary.getText() );
		}
			
		return errors; // return string containing errors
	} // end method validateTextFields
	
	// method to create an insert statement for a row in the salariedEmployees table as per the data entered
	protected String createInsertStatement()
	{
		// create and return the insert statement by gathering values entered in the GUI components
		return "insert into " + TABLE_NAME + " (socialSecurityNumber" +
			   ", weeklySalary" +
			   ", bonus) values ('" + cmbSocialSecurityNumber.getSelectedItem() +
			   "', " + txtWeeklySalary.getText() +
			   ", 0)";
	} // end method createInsertStatement
} // end class UpdateSalariedEmployee