package com.neveral;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Neveral on 17.11.14.
 */
public class UserFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JComboBox comboBox1;
    private JTextField drivingPermit;
    private JTable table1;
    private JButton queryButton;
    private JPanel statusPanel;
    private JLabel statusLabel;
    private JScrollPane tableScrollPanel;
    private JButton xmlButton;
    private String query = "";

    public UserFrame() {
        super("State Traffic Safety Inspectorate");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(420, 400));
        setPreferredSize(new Dimension(420, 400));
        setMaximumSize(new Dimension(420, 600));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        //========status bar===============
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setMaximumSize(new Dimension(getWidth()-100, 16));
        statusLabel.setText("Введите необходимые данные!");
        //========status bar===============

        // Будем использовать прокрутку
        //table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // сделаем так, чтобы редактироваться сразу могла, только
        //одна ячейка
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table1.setEnabled(false);
        JDBCAdapter adapter = new JDBCAdapter();
        table1.setModel(adapter);
        //drivingPermit.setText("7712035993");

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println(comboBox1.getSelectedIndex());/*
                switch (comboBox1.getSelectedIndex()) {
                    case 1: query = "SELECT date_and_time, amount from fine " +
                                    "join owner " +
                                    "on fine.owner_id = owner.owner_id and driving_permit = ?";
                                    break;
                    case 2: query = "select brand, model, manufact_year from car " +
                                    "join owner " +
                                    "on car.owner_id = owner.owner_id and driving_permit = ?";
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
                else if(drivingPermit.getText().isEmpty()) {
                    statusLabel.setText("Введите номер водительского удостоверения!");
                }
                else if (drivingPermit.getText().matches("[0-9]{10}")){
                    String[] statements = new String[1];
                    statements[0] = drivingPermit.getText();
                    adapter.executePreparedQuery(query, 1, statements);
                    //adapter.executeQuery(query);
                    statusLabel.setText("Запрос выполнен!");
                }else
                    statusLabel.setText("Номер ВУ введен некорректно");
            }
        });

        xmlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table1.getRowCount() != 0)
                {
                    try {
                        adapter.createXML();
                        statusLabel.setText("Файл XML сохранен в корневом каталоге приложения");
                    }
                    catch(Exception ex) {
                        System.err.println(ex);
                        statusLabel.setText("Экспорт в XML не может быть выполнен! ");
                    }
                }else {
                    statusLabel.setText("Нет данных для экспорта в XML");
                }

            }
        });
    }

}
