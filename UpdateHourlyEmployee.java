// Project 4: UpdateHourlyEmployee.java
// Concrete implementation of UpdateSpecificEmployee as an Hourly Employee
// Name: Graham Thomas
// Student Number: 1479585

// import required classes
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Insets;

// begin class UpdateHourlyEmployee which is an implementation of abstract class UpdateSpecificEmployee
public class UpdateHourlyEmployee extends UpdateSpecificEmployee
{
	private JLabel lblHours; // Hours label
	private JLabel lblWage; // Wage label
	
	private JTextField txtHours; // Hours text field
	private JTextField txtWage; // Wage text field

	// begin constructor
	public UpdateHourlyEmployee( String driver, String databaseURL, 
								 String username, String password )
	{
		// call superclass constructor for an Hourly Employee
		super( driver, databaseURL, username, password, EmployeeTypesEnum.HOURLY_EMPLOYEE );
	} // end constructor
	
	// override method addTextFields to add the text fields for an hourly employee to the frame
	protected int addTextFields()
	{
		// add labels and text fields to the frame with the specified constraints
		// component, gridx, gridy, gridwidth, gridheight, weightx, weighty, insets, anchor, fill
		addComponent( lblHours, 0, 1, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( txtHours, 1, 1, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( lblWage, 0, 2, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( txtWage, 1, 2, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		return 2; // let the calling method know that two text fields were added to the frame
	} // end method addTextFields
	
	// override method initialiseTextFields to initialise the GUI components for an hourly employee
	protected void initialiseTextFields()
	{
		lblHours = new JLabel( "Hours" ); // initialise the Hours label
		txtHours = new JTextField( TEXT_FIELD_TEXT_LENGTH ); // initialise the Hours text field with a column length of 30
		lblWage = new JLabel( "Wage" ); // initialise the Wage label
		txtWage = new JTextField( TEXT_FIELD_TEXT_LENGTH ); // initialise the Wage text field with a column length of 30
	} // end method initialiseTextFields
	
	// override method validateTextFields to validate the data in the text fields in the frame
	protected String validateTextFields()
	{
		String errors = ""; // initialise an empty string to hold any errors that might occur
		
		// if the Hours text field contains more than 30 characters
		if( textFieldTextTooLong( txtHours.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is too long and should contain %d or less characters\n",
									 lblHours.getText(),
									 TEXT_FIELD_TEXT_LENGTH );
		}
		
		// if no data entered in the Social Security Number text field
		if( textFieldTextEmpty( txtHours.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is a required field\n",
									 lblHours.getText() );
		}
		
		// if data entered in the Weekly Salary text field is not a valid integer
		if( !validateInteger( txtHours.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s must be a valid integer\n", lblHours.getText() );
		}
			
		// if the Wage text field contains more than 30 characters
		if( textFieldTextTooLong( txtWage.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is too long and should contain %d or less characters\n",
									 lblWage.getText(),
									 TEXT_FIELD_TEXT_LENGTH );
		}
		
		// if no data entered in the Social Security Number text field
		if( textFieldTextEmpty( txtWage.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is a required field\n",
									 lblWage.getText() );
		}
		
		// if data entered in the Weekly Salary text field is not a valid double
		if( !validateDouble( txtWage.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s must be a valid number\n", lblWage.getText() );
		}
			
		return errors; // return string containing errors
	} // end method validateTextFields
	
	// method to create an insert statement for a row in the hourlyEmployees table as per the data entered
	protected String createInsertStatement()
	{
		return "insert into " + TABLE_NAME + " (socialSecurityNumber" +
			   ", hours" +
			   ", wage" +
			   ", bonus) values ('" + cmbSocialSecurityNumber.getSelectedItem() +
			   "', " + txtHours.getText() +
			   ", " + txtWage.getText() +
			   ", 0)";
	} // end method createInsertStatement
} // end class UpdateHourlyEmployee