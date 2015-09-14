package com.neveral;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Neveral on 17.11.14.
 */
public class WelcomeFrame extends JFrame{
    private JRadioButton adminRadioButton;
    private JButton continueButton;
    private JRadioButton userRadioButton;
    private JPanel welcomePanel;
    private JPanel choosePanel;

    public WelcomeFrame() {
        super("State Traffic Safety Inspectorate");
        setContentPane(welcomePanel);
        setMinimumSize(new Dimension(280, 80));
        setPreferredSize(new Dimension(280, 90));
        setSize(new Dimension(280, 100));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(adminRadioButton.isSelected()) {
                    LoginDialog loginDialog = new LoginDialog(WelcomeFrame.this);
                    loginDialog.setVisible(true);
                    if (loginDialog.isSucceeded()) {
                        setVisible(false);
                        dispose();
                        AdminFrame adminFrame = new AdminFrame();
                    }
                }else {
                    setVisible(false);
                    dispose();
                    UserFrame userFrame = new UserFrame();
                }
            }
        });
    }
}
