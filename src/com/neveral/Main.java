package com.neveral;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                WelcomeFrame welcomeFrame = new WelcomeFrame();
                //UserFrame userFrame = new UserFrame();
            }
        });
    }
}
