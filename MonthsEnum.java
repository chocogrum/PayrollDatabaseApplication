// Project 4: MonthsEnum.java
// Enum of the months of the year
// Name: Graham Thomas
// Student Number: 1479585

// begin class
public enum MonthsEnum
{
	// declare list of months with the following properties:
	// 1. Name of the month
	// 2. Number of the month as it appears in the calendar
	// 3. Number of days in the month when not a leap year

	JANUARY  ( "January"  , 1 , 31  ),
	FEBRUARY ( "February" , 2 , 28  ),
	MARCH    ( "March"    , 3 , 31  ),
	APRIL    ( "April"    , 4 , 30  ),
	MAY      ( "May"      , 5 , 31  ),
	JUNE     ( "June"     , 6 , 30  ),
	JULY     ( "July"     , 7 , 31  ),
	AUGUST   ( "August"   , 8 , 31  ),
	SEPTEMBER( "September", 9 , 30  ),
	OCTOBER  ( "October"  , 10, 31 ),
	NOVEMBER ( "November" , 11, 30 ),
	DECEMBER ( "December" , 12, 31 );
	
	private final String monthDescription; // month description
	private final int monthNumber; // month number
	private final int numberOfDays; // number of days in the month when not a leap year
	
	// begin constructor
	MonthsEnum( String monthDescription, int monthNumber, int numberOfDays )
	{
		this.monthDescription = monthDescription; // initialise month description
		this.monthNumber = monthNumber; // initialise month number
		this.numberOfDays = numberOfDays; // initialise number of days in month when not a leap year
	} // end constructor

	// override toString method to return a description of the month
	public String toString()
	{
		return monthDescription; // return the description
	} // end method toString
	
	// method to return the number of the month
	public int getMonthNumber()
	{
	   return monthNumber; // return the month number
	} // end method getMonthNumber
	
	// method to return the number of days in the month when not a leap year
	public int getNumberOfDays()
	{
		return numberOfDays; // return the number of days
	} // end method getNumberOfDays

} // end class MonthsEnum