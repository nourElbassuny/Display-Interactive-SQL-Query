<h1>Display-Interactive-SQL-Query</h1>
<h4> allows the user to enter any query into the
 program. The example displays the result of a query in a JTable, using a TableModel object to provide the ResultSet data to the JTable.AJTable is a swing GUI component that can be bound to a database to display the results of a query.</h4>

 <p> Class ResultSetTable Model performs the connection to the database via a TableModel and maintains the ResultSet. Class DisplayQueryResults creates the GUI and
 specifies an instance of class ResultSetTableModel to provide data for the JTable.</p>
