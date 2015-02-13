// Project 4: UpdateHourlyEmployee.java
// Frame for querying database either through a set of
// pre-defined queries or by writing custom queries
// Name: Graham Thomas
// Student Number: 1479585

// import required classes
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTable;
import java.awt.Color;
import javax.swing.JButton;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// begin class QueryFrame which inherits from JFrame
public class QueryFrame extends JFrame
{	
	private JLabel lblInstructions; // Instructions label
	private JComboBox< DefinedQueriesEnum > cmbDefinedQueries; // Defined Queries combo box
	private JButton btnDefinedQueries; // Defined Queries button
	private JTextArea txtCustomQuery; // Custom Query text area
	private JButton btnCustomQuery; // Custom Query button
	private JPanel tablePanel; // panel for displaying a JTable
	private JTable tblQueryResults; // JTable to display query results
	
	private ResultSetTableModel queryResultsModel; // data model for the JTable
	
	private GridBagLayout gridBagLayout; // grid bag layout for arranging GUI components
	private GridBagConstraints gridBagConstraints; // grid bag constraints for the grid bag layout
	
	private final String DRIVER; // jdbc driver class
	private final String DATABASE_URL; // database URL
	private final String USERNAME; // username for accessing database
	private final String PASSWORD; // password for accessing database

	// begin constructor
	public QueryFrame( String driver, String databaseURL, 
					   String username, String password )
	{
		super( "Database Queries" ); // call superclass constructor with frame title
		
		DRIVER = driver; // set the jdbc driver
		DATABASE_URL = databaseURL; // set the database URL
		USERNAME = username; // set the username for accessing the database
		PASSWORD = password; // set the password for accessing the database
		
		// initialise Instructions label
		lblInstructions = new JLabel( "Choose a defined query or create a custom query in the text area below" );
		
		// initialise Defined Queries combo box with element DefinedQueriesEnum
		cmbDefinedQueries = new JComboBox< DefinedQueriesEnum >( DefinedQueriesEnum.values() );
		
		txtCustomQuery = new JTextArea( 10, 30 ); // initialise Custom Query text area with 10 rows and 30 columns
		txtCustomQuery.setLineWrap( true ); // set line wrap true for text area
		txtCustomQuery.setWrapStyleWord( true ); // set wrap style word for text area
		
		try // begin try block
		{
			// initialise JTable data model to the currently selected query in the Defined Queries combo box
			// using five argument constructor as employee type is not relevant for this result model
			queryResultsModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME, PASSWORD,
														 ( ( DefinedQueriesEnum ) cmbDefinedQueries.getSelectedItem() ).getQuery() );
			
			btnDefinedQueries = new JButton( "Execute Defined Query" ); // initialise Defined Queries button
			btnDefinedQueries.addActionListener( // add an action listener for the Defined Queries button
				new ActionListener() // begin anonymous inner class
				{				
					// override method actionPerformed
					public void actionPerformed( ActionEvent event )
					{					
						try // begin try block
						{
							// set the query of the table model to the currently selected query in the Defined Queries combo box
							queryResultsModel.setQuery( ( ( DefinedQueriesEnum ) cmbDefinedQueries.getSelectedItem() ).getQuery() );
						} // end try block
						catch( SQLException sqlException ) // catch SQLException
						{
							// display message to user indicating that an error occurred
							JOptionPane.showMessageDialog(
								null, // use default parent component
								sqlException.getMessage(), // message to display
								"Table Model Error", // title of the message dialog
								JOptionPane.ERROR_MESSAGE ); // message type is error
							sqlException.printStackTrace(); // print stack trace
						} // end catch SQLException
					} // end method actionPerformed
				} // end anonymous inner class
			); // end registering of event handler for Defined Queries button
		
			btnCustomQuery = new JButton( "Execute Custom Query" ); // initialise Custom Query button
			btnCustomQuery.addActionListener( // add an action listener for the Custom Query button
				new ActionListener() // begin anonymous inner class
				{
					// override method actionPerformed
					public void actionPerformed( ActionEvent event )
					{
						try // begin try block
						{
							// set the query of the table model to that entered in the Custom Query text area
							queryResultsModel.setQuery( txtCustomQuery.getText() );
						} // end try block
						catch( SQLException sqlException ) // catch SQLException
						{
							// display message to user indicating that an error occurred
							JOptionPane.showMessageDialog(
								null, // use default parent component
								String.format( "Not a valid SQL query\n%s\n%s",
									txtCustomQuery.getText(),
									sqlException.getMessage() ), // message to display
								"Query Invalid", // title of the message dialog
								JOptionPane.ERROR_MESSAGE ); // message type is error
						} // end catch SQLException
					} // end method actionPerformed
				} // end anonymous inner class
			); // end registering of event handler for Custom Query button
		
			tblQueryResults = new JTable( queryResultsModel ); // initialise data table
			tblQueryResults.setGridColor( Color.BLACK ); // set table gridline colour to black
			tablePanel = new JPanel(); // initialise the table panel
			tablePanel.setLayout( new BorderLayout() ); // set border layout as the layout of the table panel
			
			// add data table to table panel within a scroll pane with center alignment
			tablePanel.add( new JScrollPane( tblQueryResults ), BorderLayout.CENTER );
		
			gridBagLayout = new GridBagLayout(); // initialise grid bag layout
			gridBagConstraints = new GridBagConstraints(); // initialise grid bag constraints
			setLayout( gridBagLayout ); // set the layout of this frame to grid bag layout
			
			// add GUI components to the frame with the specified constraints
			// component, gridx, gridy, gridwidth, gridheight, weightx, weighty, insets, anchor, fill		
			addComponent( lblInstructions, 0, 0, 2, 1, 0, 0, new Insets( 5, 5, 5, 5 ), gridBagConstraints.CENTER, gridBagConstraints.NONE );
			addComponent( cmbDefinedQueries, 0, 1, 1, 1, 0, 0, new Insets( 5, 5, 5, 5 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( btnDefinedQueries, 1, 1, 1, 1, 0, 0, new Insets( 5, 5, 5, 5 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( txtCustomQuery, 0, 2, 1, 1, 0, 0, new Insets( 5, 5, 5, 5 ), gridBagConstraints.WEST, gridBagConstraints.HORIZONTAL );
			addComponent( btnCustomQuery, 1, 2, 1, 1, 0, 0, new Insets( 5, 5, 5, 5 ), gridBagConstraints.NORTHWEST, gridBagConstraints.HORIZONTAL );
			addComponent( tablePanel, 0, 3, 3, 1, 1, 1, new Insets( 5, 0, 5, 0 ), gridBagConstraints.CENTER, gridBagConstraints.BOTH );
		
			// add a window listener to this frame
			addWindowListener(
				new WindowAdapter() // declare anonymous inner class
				{
					// override method windowClosing
					// when window is closing, disconnect from the database if connected
					public void windowClosing( WindowEvent event )
					{
						if( queryResultsModel != null ) // if the JTable data model has been initialised
							queryResultsModel.disconnectFromDatabase(); // disconnect the data model from database
					} // end method windowClosing
				} // end anonymous inner class
			); // end registering of event handler for the window
		
			pack(); // resize the frame to fit the preferred size of its components
			setVisible( true ); // set the frame to be visible
		} // end try block
		catch ( SQLException sqlException ) // catch SQLException
		{
			// display message to user indicating that an error occurred
			JOptionPane.showMessageDialog(
				null, // use default parent component
				sqlException.getMessage(), // message to display
				"Table Model Error", // title of the message dialog
				JOptionPane.ERROR_MESSAGE ); // message type is error
			sqlException.printStackTrace(); // print stack trace
			
			// if table model cannot be initialised then frame is useless
			// so set the frame invisible and dispose of it
			setVisible( false ); // set the frame to be invisible
			dispose(); // dispose of this frame
		} // end catch SQLException
		catch ( ClassNotFoundException classNotFoundException )
		{
			// display message to user indicating that an error occurred
			JOptionPane.showMessageDialog(
				null, // use default parent component
				String.format( "Could not find database driver %s\n%s", // message to display
					DRIVER,
					classNotFoundException.getMessage() ),
				"Driver Not Found", // title of the message dialog
				JOptionPane.ERROR_MESSAGE ); // message type is error
			classNotFoundException.printStackTrace(); // print stack trace
			
			// if table model cannot be initialised then frame is useless
			// so set the frame invisible and dispose of it
			setVisible( false ); // set the frame to be invisible
			dispose(); // dispose of this frame
		} // end catch ClassNotFoundException
	} // end constructor
	
	// add the specified component to the frame as per the specified grid bag constraints
	private void addComponent( Component component,
							   int column, int row,
							   int width, int height,
							   int weightx, int weighty,
							   Insets insets, int anchor, int fill )
	{
		gridBagConstraints.gridx = column; // set the gridx constraint
		gridBagConstraints.gridy = row; // set the gridy constraint
		gridBagConstraints.gridwidth = width; // set the gridwidth constraint
		gridBagConstraints.gridheight = height; // set the gridheight constraint
		gridBagConstraints.weightx = weightx; // set the weightx constraint
		gridBagConstraints.weighty = weighty; // set the weighty constraint
		gridBagConstraints.insets = insets; // set the insets constraint
		gridBagConstraints.anchor = anchor; // set the anchor constraint
		gridBagConstraints.fill = fill; // set the fill constraint
		gridBagLayout.setConstraints( component, gridBagConstraints ); // set the constraints for the component
		add( component ); // add the component to the frame
	} // end method addComponent
} // end class QueryFrame