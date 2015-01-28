import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Box;
import java.awt.Dimension;

public class DatabaseInputPanel extends JPanel
{
	private JTextField textField;
	private JLabel fieldLabel;
	private Box horizontalBox;

	public DatabaseInputPanel( String labelText, String textFieldText )
	{
		super();
		horizontalBox = Box.createHorizontalBox();
		textField = new JTextField( textFieldText, 30 );
		fieldLabel = new JLabel( labelText );
		fieldLabel.setPreferredSize( new Dimension( 160, 15 ) );
		add( fieldLabel );
		add( textField );
	}
	
	public DatabaseInputPanel( String labelText )
	{
		this( labelText, "" );
	}
	
	public void setTextFieldText( String text )
	{
		textField.setText( text );
	}
	
	public void setLabelText( String text )
	{
		fieldLabel.setText( text );
	}
	
	public String getTextFieldText()
	{
		return textField.getText();
	}
	
	public String getLabelText()
	{
		return fieldLabel.getText();
	}
}