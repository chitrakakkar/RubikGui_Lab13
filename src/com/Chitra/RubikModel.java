package com.Chitra;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by chitrakakkar on 4/26/16.
 */
public class RubikModel extends AbstractTableModel
{

    ResultSet resultSet;
    int numberOfRows;
    int numberOfColumns;


    public RubikModel(ResultSet rs) {
        this.resultSet = rs;
        setup();

    }

    public void updateResultSet(ResultSet newRS) {
        resultSet = newRS;
        setup();

    }

    private void setup() {

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




    public void setValueAt(Object newValue, int row, int col) {

        //Make sure newValue is an integer AND that it is in the range of valid ratings

        Double newTime;

        try {
            newTime = Double.parseDouble(newValue.toString());

            if (newTime < 0.0) {
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
            resultSet.updateRow();
            fireTableDataChanged();
        }
        catch (SQLException se)
        {
            System.out.println("Error updating the time" + se);
        }
    }

    public boolean isCellEditable(int row, int col)
    {
        if (col == 2)
        {
            return true;
        }
        return false;
    }

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
