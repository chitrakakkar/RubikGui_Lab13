package com.Chitra;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by chitrakakkar on 4/26/16.
 * This class basically creates the table after getting the number of rows and column from ResultSet
 * And populate the data into the table
 */
public class RubikModel extends AbstractTableModel
{

    ResultSet resultSet;
    int numberOfRows;
    int numberOfColumns;

        // constructor gets the result set
        // and rowCount and columnCount
    public RubikModel(ResultSet rs)
    {
        this.resultSet = rs;
        setup();

    }
    //updates the result set

    public void updateResultSet(ResultSet newRS) {
        resultSet = newRS;
        setup();

    }

    private void setup()
    {

        countRows();

        try {
            numberOfColumns = resultSet.getMetaData().getColumnCount();

        } catch (SQLException se) {
            System.out.println("Error counting columns" + se);
        }

    }

    private void countRows()
    {
        numberOfRows = 0;
        try {
            //Move cursor to the start...
            resultSet.beforeFirst();
            // next() method moves the cursor forward one row and returns true if there is another row ahead
            while (resultSet.next()) {
                numberOfRows++;

            }
            resultSet.beforeFirst();

        } catch (SQLException se) {
            System.out.println("Error counting rows " + se);
        }
    }

    @Override
    public int getRowCount() {
        countRows();
        return numberOfRows;
    }

    @Override
    public int getColumnCount() {
        return numberOfColumns;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            resultSet.absolute(rowIndex + 1);
            Object o = resultSet.getObject(columnIndex + 1);
            return o.toString();
        } catch (SQLException se) {
            System.out.println(se);
            //se.printStackTrace();
            return se.toString();
        }
    }
    // deletes the row selected in GUI using predefined method called deleteRow
    public  boolean deleteRow(int row)
    {
        try
        {
            resultSet.absolute(row + 1);
            resultSet.deleteRow();
            //Tell table to redraw itself
            fireTableDataChanged();
            return true;
        }catch (SQLException se) {
            System.out.println("Delete row error " + se);
            return false;
        }
    }

    // Updates the new value into a row using result set whenever
    // user edits an editable cell which is Time _Taken in this case
    public void setValueAt(Object newValue, int row, int col) {
        //Make sure newValue is a positive number
        Double newTime;

        try {
            newTime = Double.parseDouble(newValue.toString());

            if (newTime < 0.0)
            {
                throw new NumberFormatException("Time Taken  must be a postive double number");
            }
        } catch (NumberFormatException ne) {
            //Error dialog box. First argument is the parent GUI component, which is only used to center the
            // dialog box over that component. We don't have a reference to any GUI components here
            // but are allowed to use null - this means the dialog box will show in the center of your screen.
            JOptionPane.showMessageDialog(null, "Try entering a positive number");
            //return prevents the following database update code happening...
            return;
        }

        try
        {
            resultSet.absolute(row + 1);
            resultSet.updateDouble(Main.Time_Taken, newTime);
            System.out.println("Updated the time");
            resultSet.updateRow();
            fireTableDataChanged();
        }
        catch (SQLException se)
        {
            System.out.println("Error updating the time" + se);
        }
    }

    // allows to edit the cell(Time taken in this case)
    public boolean isCellEditable(int row, int col)
    {
        // if(col == resultSet.findColumn(Main.Time_Taken)
        if (col == 2)
        {
            return true;
        }
        return false;
    }
    // Inserts the value into a row using result set
    public  boolean insertRow(String st, Double tt)
    {
        try {
            resultSet.moveToInsertRow();
            resultSet.updateString(Main.Solver_Name, st);
            resultSet.updateDouble(Main.Time_Taken,tt);
            resultSet.insertRow();
            fireTableDataChanged();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Error adding row");
            System.out.println(e);
            return false;

        }

    }
    @Override
    public String getColumnName(int col){
        //Get from ResultSet metadata, which contains the database column names
        //TODO translate DB column names into something nicer for display, so "YEAR_RELEASED" becomes "Year Released"
        try {
            return resultSet.getMetaData().getColumnName(col + 1);
        } catch (SQLException se) {
            System.out.println("Error fetching column names" + se);
            return "?";
        }
    }
}
