public class ValidateForMySql
{
	public static boolean validateInt( String value )
	{
		try
		{
			Integer.parseInt( value );
			return true;
		}
		catch( NumberFormatException numberFormatException )
		{
			return false;
		}
		
	}
	
	public static boolean validateDate( String value )
	{
		int day;
		int month;
		int year;
	
		try
		{
			Integer.parseInt( value );
			return true;
		}
		catch( NumberFormatException numberFormatException )
		{
			return false;
		}
		
	}
}