// Project 4: PayrollTest.java
// Class for testing the payroll application
// Name: Graham Thomas
// Student Number: 1479585

// import required classes
import javax.swing.JFrame;

// begin class
public class PayrollTest
{
	public static void main( String args[] ) // method main
	{
		AddEmployees ae = new AddEmployees(); // create new AddEmployees object
		ae.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ); // set application to exit once AddEmployees frame is closed
	} // end method main
} // end class PayrollTest