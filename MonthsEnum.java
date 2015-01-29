public enum MonthsEnum
{
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
	
	private final String monthDescription;
	private final int monthNumber;
	private final int numberOfDays;
	
	MonthsEnum( String monthDescription, int monthNumber, int numberOfDays )
	{
		this.monthDescription = monthDescription;
		this.monthNumber = monthNumber;
		this.numberOfDays = numberOfDays;
	}

	public String toString()
	{
		return monthDescription;
	}
	
	public int getMonthNumber()
	{
	   return monthNumber;
	}
	
	public int getNumberOfDays()
	{
		return numberOfDays;
	}

};