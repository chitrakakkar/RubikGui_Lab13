package com.Chitra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by chitrakakkar on 4/25/16.
 * This class designs the GUI with Info from TableModel
 * Adds listener for all buttons on the GUI
 * Calls user defined methods like insert row / delete row from TableModel, finally resultSet
 */
public class RubikGui extends JFrame implements WindowListener
{
    private JTable rubikTable;
    private JTextField SolverNameText;
    private JTextField TimeText;
    private JButton ADDButton;
    private JButton DELETEButton;
    private JButton quitButton;
    private JPanel rootPanel;

    RubikGui(final RubikModel RubikDataTableModel)
    {
        setContentPane(rootPanel);
        pack();
        setTitle("Rubik_Solver");
        addWindowListener(this);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500,500);
        rubikTable.setGridColor(Color.black);
        rubikTable.setModel(RubikDataTableModel);
       // System.out.println();


        ADDButton.addActionListener(e ->
        {
            String solverName = SolverNameText.getText();
            System.out.println("Solver " + solverName);
            if (solverName == null || solverName.trim().equals("")) {
                JOptionPane.showMessageDialog(rootPane, "Please enter a name for the solver");
                return;
            }

            Double timeTaken = Double.parseDouble(TimeText.getText());
           //try {
               boolean insertRow = RubikDataTableModel.insertRow(solverName, timeTaken);
            SolverNameText.setText("");
            TimeText.setText("");
               if (!insertRow)
               {
                   JOptionPane.showMessageDialog(rootPane, "Error adding new solver");
               }

           //}
//           catch (SQLException se)
//           {
//               System.out.println("SQL exception occurred");
//           }

        })
        ;

        DELETEButton.addActionListener(e ->
        {
            int currentRow = rubikTable.getSelectedRow();
            if(currentRow == -1)
            {
                JOptionPane.showMessageDialog(rootPane, "Please choose a row to delete");
            }
            boolean deleted = RubikDataTableModel.deleteRow(currentRow);
            SolverNameText.setText("");
            TimeText.setText("");
            if (deleted)
            {
                Main.loadAllData();
            } else
            {
                JOptionPane.showMessageDialog(rootPane, "Error deleting row");
            }

        });

        quitButton.addActionListener(e -> {

            Main.shutdown();
            System.exit(-1);
        });


    }

    @Override
    public void windowOpened(WindowEvent e)
    {
    }

    @Override
    public void windowClosing(WindowEvent e)
    {
        Main.shutdown();
        System.out.println("Closing");

    }
    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }


}
