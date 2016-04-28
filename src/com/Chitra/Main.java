package com.Chitra;

import java.sql.*;
/* This class connects to the database created on the shell using the JDBC driver
 * Also, This class uses Prepared statement to stop any sql injections and prepare the statements for table creation, insertion and updation */
// So first, table is created-> and if this set up is done successfully, data is loaded into the table and to the GUI
// A table model object is created which takes the Result set which has all the data
// Creates the table and Send it to the GUI where Jtable gets the table model to communicate with table created in the main class
// so the route is ResultSet-> TableModel-> Gui;

public class Main
{
    // establish the connection with
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "newrubik";
    private static final String USER = "root";
    private static final String PASS = "password";


    static Statement statement = null;
    static Connection conn = null;
    static ResultSet rs = null;
    public static PreparedStatement PrepStatement = null;

    public final static String rubik_Table_Name = "Rubik_Solver2";
    public final static String Solver_Name = "Solver_Name";
    public final static String Time_Taken = "Time_Taken";
    public final static String PK_COLUMN = "id";

    private static RubikModel rubikModel;

    public static void main(String[] args)
    {

        if(!setup())
        {

            System.exit(-1);
        }
        if(!loadAllData())
        {
            System.exit(-1);
        }

        RubikGui rubikGui = new RubikGui(rubikModel);

    }
    public static boolean setup()
    {
        try
        {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe)
        {
            System.out.println("No database drivers found. Quitting");
            return false;
        }
        try {
            conn = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASS);
            //statement = conn.createStatement();
             statement= conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //String createTableCUBE = "CREATE TABLE  if not exists testTable (Cube_Solver varchar(30), Time_Taken double)";
//            if (!rubikTableExists()) {
//                String createTableRubik = "CREATE TABLE " + rubik_Table_Name + " (" + PK_COLUMN + " int NOT NULL AUTO_INCREMENT, " + Solver_Name + " varchar(100), " + Time_Taken + " Double,Primary Key(" + PK_COLUMN + "))";
//

                String createTableRubik = "CREATE TABLE  if not exists " + rubik_Table_Name + "(" + PK_COLUMN + " int NOT NULL AUTO_INCREMENT, " + Solver_Name + " varchar(150)," + Time_Taken + " Double, PRIMARY KEY (" + PK_COLUMN + "))";
                statement.executeUpdate(createTableRubik);
                String addDataSQL = "INSERT INTO " + rubik_Table_Name + "(" + Solver_Name + " , " + Time_Taken + ")" + " VALUES ('CubeStormer II robot', 5.270)";
                PrepStatement = conn.prepareStatement(addDataSQL);
                PrepStatement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO " + rubik_Table_Name + "(" + Solver_Name + "," + Time_Taken + " )" + "  VALUES ('Fakhri Raihaan', 27.93)";
                PrepStatement = conn.prepareStatement(addDataSQL);
                PrepStatement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO " + rubik_Table_Name + "(" + Solver_Name + "," + Time_Taken + " )" + " VALUES ('Ruxin Liu', 99.33)";
                PrepStatement = conn.prepareStatement(addDataSQL);
                PrepStatement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO " + rubik_Table_Name + "(" + Solver_Name + "," + Time_Taken + " )" + " VALUES ('Mats Valk ', 6.27)";
                PrepStatement = conn.prepareStatement(addDataSQL);
                PrepStatement.executeUpdate(addDataSQL);
                System.out.println("Added four rows of data");
            return true;

            }



        catch (SQLException se)
        {

            se.printStackTrace();
            System.out.println("Here");
            return false;
        }

    }
    private static boolean rubikTableExists() throws SQLException {
        String checkTablePresentQuery = " SHOW TABLES LIKE '" + rubik_Table_Name + " '";
        ResultSet tablesRS = statement.executeQuery(checkTablePresentQuery);
        if (tablesRS.next())
        {    //If ResultSet has a next row, it has at least one row... that must be our table
            return true;
        }
        return false;
    }


    public static boolean loadAllData()
    {
        try {
            String getAllData = "SELECT * FROM " + rubik_Table_Name;
            rs = PrepStatement.executeQuery(getAllData);
            if (rubikModel == null)
            {
                rubikModel = new RubikModel(rs);
            }
            else
            {
                rubikModel.updateResultSet(rs);
            }
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Error loading or reloading data");
            System.out.println(e);
            e.printStackTrace();
            return false;

        }
    }


    public static void shutdown()
    {
        try {
            if (rs != null) {
                rs.close();
                System.out.println("Result set closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            if (statement != null)
            {
                statement.close();
                System.out.println("Statement closed");
            }
        } catch (SQLException se){
            //Closing the connection could throw an exception too
            se.printStackTrace();
        }

        try {
            if (conn != null)
            {
                conn.close();
                System.out.println("Database connection closed");
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
    }
}
