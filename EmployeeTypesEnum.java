// Project 4: EmployeeTypesEnum.java
// Enum of the different employee types
// Name: Graham Thomas
// Student Number: 1479585

// import required classes
import java.util.HashMap;
import java.util.Map;

// begin class
public enum EmployeeTypesEnum
{
	// declare list of employee types with the following properties:
	// 1. Name of the table in which data for the employee type can be found
	// 2. Description of the employee type
	// 3. Reference for the employee type that is used in the base employees table
	// 4. String array of the column names in the table for that employee type
	// 5. String array of column aliases corresponding to entries in the array of column names
	
	// generic employee as found in the employees table
	GENERIC_EMPLOYEE  ( "employees", "Generic Employee", "employee",
						 new String[] { "socialSecurityNumber",
										"firstName",
										"lastName",
										"birthday",
										"employeeType",
										"departmentName" },
						 new String[] { "Social Security Number",
										"First Name",
										"Last Name",
										"Birthday",
										"Employee Type",
										"Department Name" }
					   ),
	
	// salaried employee as found in the salariedEmployees table
	SALARIED_EMPLOYEE  ( "salariedEmployees", "Salaried Employee", "salariedEmployee",
						 new String[] { "socialsecurityNumber",
										"weeklySalary",
										"bonus" },
						 new String[] { "Social Security Number",
										"Weekly Salary",
										"Bonus" }
					   ),
	
	// commission employee as found in the commissionEmployees table
	COMMISSION_EMPLOYEE ( "commissionEmployees" , "Commission Employee", "commissionEmployee",
						  new String[] { "socialSecurityNumber",
										 "grossSales",
										 "commissionRate",
										 "bonus" },
						  new String[] { "Social Security Number",
										 "Gross Sales",
										 "Commission Rate",
										 "Bonus" }
						),
	
	// base plus commission employee as found in the basePlusCommissionEmployees table
	BASE_PLUS_COMMISSION_EMPLOYEE ( "basePlusCommissionEmployees", "Base Plus Commission Employee", "basePlusCommissionEmployee",
									new String[] { "socialSecurityNumber",
												   "grossSales",
												   "commissionRate",
												   "baseSalary",
												   "bonus" },
									new String[] { "Social Security Number",
												   "Gross Sales",
												   "Commission Rate",
												   "Base Salary",
												   "Bonus" }
								  ),
	
	// hourly employee as found in the hourlyEmployees table
	HOURLY_EMPLOYEE ( "hourlyEmployees", "Hourly Employee", "hourlyEmployee",
					  new String[] { "socialSecurityNumber",
									 "hours",
									 "wage",
									 "bonus" },
					  new String[] { "Social Security Number",
									 "Hours",
									 "Wage",
									 "Bonus" }
					);
	
	private final String tableName; // table name
	private final String description; // employee type description
	private final String employeesTableReference; // reference used for this employee type in the base employees table
	private final Map< String, String > columnNamesMap; // map of table's column names to descriptive aliases
	
	// begin constructor
	EmployeeTypesEnum( String tableName, String description, String reference,
					   String[] columnNames, String[] columnHeaders )
	{
		this.tableName = tableName; // initialise table name
		this.description = description; // initialise description
		employeesTableReference = reference; // initialise reference to employees table
		columnNamesMap = new HashMap< String, String >(); // initialise map of table's column names and aliases
		
		// loop through the string array of column names
		for( int i = 0; i < columnNames.length; i++ )
			columnNamesMap.put( columnNames[ i ], columnHeaders[ i ] ); // add the column name and its alias to the map
	} // end constructor
	
	// method to return a descriptive alias for a given column name
	public String getColumnHeader( String columnName )
	{
		String columnHeader = columnNamesMap.get( columnName ); // look up the column name in the map
		if( columnHeader == null ) // if column name not found
			columnHeader = columnName; // set column alias to be same as column name
		
		return columnHeader; // return column alias
	} // end method getColumnHeader
	
	// method to return the table name associated with this employee type
	public String getTableName()
	{
		return tableName; // return the table name
	} // end method getTableName
	
	// method to get the employee type reference used in the base employees table
	public String getEmployeesTableReference()
	{
		return employeesTableReference; // return the reference
	} // end method getEmployeesTableReference

	// override toString method to return a description of the employee type
	public String toString()
	{
		return description; // return the employee type description
	} // end method toString

} // end class EmployeeTypesEnum