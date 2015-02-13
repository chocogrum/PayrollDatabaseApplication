// Project 4: UpdateCommissionEmployee.java
// Concrete implementation of UpdateSpecificEmployee as a Commission Employee
// Name: Graham Thomas
// Student Number: 1479585

// import required classes
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Insets;

// begin class UpdateCommissionEmployee which is an implementation of abstract class UpdateSpecificEmployee
public class UpdateCommissionEmployee extends UpdateSpecificEmployee
{
	private JLabel lblGrossSales; // Gross Sales label
	private JLabel lblCommissionRate; // Commission Rate label
	
	private JTextField txtGrossSales; // Gross Sales text field
	private JTextField txtCommissionRate; // Commission Rate text field

	// begin constructor
	public UpdateCommissionEmployee( String driver, String databaseURL, 
								     String username, String password )
	{
		// call superclass constructor for a Commission Employee
		super( driver, databaseURL, username, password, EmployeeTypesEnum.COMMISSION_EMPLOYEE );
	} // end constructor
	
	// override method addTextFields to add the text fields for a commission employee to the frame
	protected int addTextFields()
	{
		// add labels and text fields to the frame with the specified constraints
		// component, gridx, gridy, gridwidth, gridheight, weightx, weighty, insets, anchor, fill
		addComponent( lblGrossSales, 0, 1, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( txtGrossSales, 1, 1, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( lblCommissionRate, 0, 2, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( txtCommissionRate, 1, 2, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		return 2; // let the calling method know that two text fields were added to the frame
	} // end method addTextFields
	
	// override method initialiseTextFields to initialise the GUI components for a commission employee
	protected void initialiseTextFields()
	{
		lblGrossSales = new JLabel( "Gross Sales" ); // initialise the Gross Sales label
		txtGrossSales = new JTextField( TEXT_FIELD_TEXT_LENGTH ); // initialise the Gross Sales text field with a column length of 30
		lblCommissionRate = new JLabel( "Commission Rate" ); // initialise the Commission Rate label
		txtCommissionRate = new JTextField( TEXT_FIELD_TEXT_LENGTH ); // initialise the Commission Rate text field with a column length of 30
	} // end method initialiseTextFields
	
	// override method validateTextFields to validate the data in the text fields in the frame
	protected String validateTextFields()
	{
		String errors = ""; // initialise an empty string to hold any errors that might occur

		// if the Gross Sales text field contains more than 30 characters
		if( textFieldTextTooLong( txtGrossSales.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is too long and should contain %d or less characters\n",
									 lblGrossSales.getText(),
									 TEXT_FIELD_TEXT_LENGTH );
		}
		
		// if no data entered in the Social Security Number text field
		if( textFieldTextEmpty( txtGrossSales.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is a required field\n",
									 lblGrossSales.getText() );
		}
		
		// if data entered in the Weekly Salary text field is not a valid integer
		if( !validateInteger( txtGrossSales.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s must be a valid integer\n", lblGrossSales.getText() );
		}
			
		// if the Commission Rate text field contains more than 30 characters
		if( textFieldTextTooLong( txtCommissionRate.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is too long and should contain %d or less characters\n",
									 lblCommissionRate.getText(),
									 TEXT_FIELD_TEXT_LENGTH );
		}
		
		// if no data entered in the Social Security Number text field
		if( textFieldTextEmpty( txtCommissionRate.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s is a required field\n",
									 lblCommissionRate.getText() );
		}
		
		// if data entered in the Weekly Salary text field is not a valid double
		if( !validateDouble( txtCommissionRate.getText() ) )
		{
			// add error message to errors string
			errors += String.format( "%s must be a valid number\n", lblCommissionRate.getText() );
		}
			
		return errors; // return string containing errors
	} // end method validateTextFields
	
	// method to create an insert statement for a row in the commissionEmployees table as per the data entered
	protected String createInsertStatement()
	{
		return "insert into " + TABLE_NAME + " (socialSecurityNumber" +
			   ", grossSales" +
			   ", commissionRate" +
			   ", bonus) values ('" + cmbSocialSecurityNumber.getSelectedItem() +
			   "', " + txtGrossSales.getText() +
			   ", " + txtCommissionRate.getText() +
			   ", 0)";
	} // end method createInsertStatement
} // end class UpdateCommissionEmployee