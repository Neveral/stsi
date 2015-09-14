package com.neveral;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Neveral on 23.11.14.
 */
public class AdminFrame extends JFrame{
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JScrollPane tableScrollPanel;
    private JTable table1;
    private JButton queryButton;
    private JTextField nameField;
    private JComboBox comboBox1;
    private JPanel statusPanel;
    private static JLabel statusLabel = new JLabel("Text");
    private JScrollPane scroller;
    private JTextField surNameField;
    private JPanel addFinePanel;
    private JTextField dtField;
    private JTextField amount;
    private JButton insertButton;
    private JTextField driveField;
    private JTextField amountField;
    private String query = "";

    public AdminFrame() {
        super("State Traffic Safety Inspectorate");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(400, 220));
        setPreferredSize(new Dimension(400, 320));
        setMaximumSize(new Dimension(400, 600));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        //========status bar===============
        statusPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
        statusPanel.setMinimumSize(new Dimension(getWidth() - 100, 48));
        //statusPanel.setPreferredSize(new Dimension(getWidth() - 100, 48));
        statusPanel.setMaximumSize(new Dimension(getWidth() - 100, 48));
        scroller = new JScrollPane(statusLabel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.setSize(statusPanel.getSize());
        //scroller.setViewportView(statusLabel);
        statusPanel.add(scroller);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setText("Введите необходимые данные!");
        //========status bar===============

        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JDBCAdapter adapter = new JDBCAdapter();
        table1.setModel(adapter);
        addFinePanel.setVisible(false);

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.setVisible(true);
                addFinePanel.setVisible(false);
                adapter.clearAll();
                clearAddFinePanel();
                switch (comboBox1.getSelectedIndex()) {
                    case 1: query = "SELECT date_and_time, amount from fine " +
                            "join owner " +
                            "on fine.owner_id = owner.owner_id and first_name = ? and last_name = ?";
                            break;
                    case 2: query = "select brand, model, manufact_year from car " +
                            "join owner " +
                            "on car.owner_id = owner.owner_id and first_name = ? and last_name = ?";
                            break;
                    case 3: query = "select first_name, last_name, birth_date, driving_permit from owner " +
                            "where first_name = ? and last_name = ?";
                            break;
                    case 4: contentPanel.setVisible(false);
                            addFinePanel.setVisible(true);
                            statusLabel.setText("Введите необходимые данные!");
                        break;
                    default: query = "";
                }

            }
        });

        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (query.isEmpty()) {
                    statusLabel.setText("Выберите сервис!");
                }
                else if(nameField.getText().isEmpty() || surNameField.getText().isEmpty()) {
                    statusLabel.setText("Введите имя и фамилию");
                }
                else if (nameField.getText().matches("[а-яА-Я]+") && surNameField.getText().matches("[а-яА-Я]+")){
                    String[] statements = new String[2];
                    statements[0] = nameField.getText();
                    statements[1] = surNameField.getText();
                    adapter.executePreparedQuery(query, 2, statements);
                    //adapter.executeQuery(query);
                    statusLabel.setText("Запрос выполнен!");
                }else
                    statusLabel.setText("Имя или фамилия введены неверно");
            }
        });

        /*dtField.setText("2014-11-12 08:12:00");
        amountField.setText("350");
        driveField.setText("7712035993");*/
        dtField.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(new Date()));

        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dt = dtField.getText();
                String amount = amountField.getText();
                String drive = driveField.getText();
                if (dt.isEmpty() || amount.isEmpty() || drive.isEmpty())
                    setStatusLabelText("Заполните все поля!");
                else {
                    if (!dt.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"))
                        setStatusLabelText("Дата и время введены неверно!");
                    else if (!amount.matches("\\d{1,8}"))
                        setStatusLabelText("Сумма штрафа введена неверно!");
                    else if (!drive.matches("\\d{10}"))
                        setStatusLabelText("Номер в/у введен неверно!");
                    else {
                        query = "SELECT owner_id from owner where driving_permit = ?";
                        String[] statements = new String[1];
                        statements[0] = driveField.getText();
                        adapter.executePreparedQuery(query, 1, statements);
                        if (table1.getRowCount() != 0) {
                            //System.out.println(table1.getValueAt(0,0));
                            String owner_id = table1.getValueAt(0, 0).toString();
                            setStatusLabelText("Штраф добавлен");
                            query = "INSERT INTO fine SET date_and_time = \'" + dt + "\', amount = \'" + amount + "\', owner_id = \'" + owner_id + "\';";
                            adapter.addData(query);
                            clearAddFinePanel();
                        } else {
                            setStatusLabelText("Такого водителя нет в базе данных ГИБДД!");
                        }
                    }
                }
            }
        });

    }
    public static void setStatusLabelText(String text) {
        statusLabel.setText(text);
    }
    public void clearAddFinePanel() {
        dtField.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(new Date()));
        amountField.setText("");
        driveField.setText("");
        query="";
    }
}
