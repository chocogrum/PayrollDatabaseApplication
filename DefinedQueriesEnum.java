// Project 4: DefinedQueriesEnum.java
// Enum of a set of pre-defined SQL queries for the employees database
// Name: Graham Thomas
// Student Number: 1479585

// begin class
public enum DefinedQueriesEnum
{
	// declare list of pre-defined SQL queries with the following properties:
	// 1. Description of the query
	// 2. The SQL query

	SALES_EMPLOYEES ( "Sales Department Employees",
					  "select * from employees where departmentName = 'SALES'" ),
	HOURLY_EMPLOYEES_30( "Hourly Employees Working Over 30 Hours",
						 "select * from employees e, hourlyEmployees he " +
						 "where e.socialSecurityNumber = he.socialSecurityNumber " +
						 "and he.hours > 30" ),
	COMMISSION_EMPLOYEES_RATE_DESC( "Commission Employees in Descending Order of Commission Rate",
								    "select * from employees e, commissionEmployees ce " +
								    "where e.socialSecurityNumber = ce.socialSecurityNumber " +
								    "order by commissionRate desc" );
	
	private final String queryDescription; // description of the query
	private final String query; // the SQL query
	
	// begin constructor
	DefinedQueriesEnum( String description, String query )
	{
		queryDescription = description; // initialise query description
		this.query = query; // initialise the SQL query
	} // end constructor
	
	// override toString method to return a description of the query
	public String toString()
	{
		return queryDescription; // return the query description
	} // end method toString
	
	// method to return the SQL query
	public String getQuery()
	{
	   return query; // return the SQL query
	} // end method getQuery
} // end class DefinedQueriesEnum