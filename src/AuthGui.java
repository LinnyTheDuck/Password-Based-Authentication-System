package src;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.*;

public class AuthGui{
    private static boolean choice = false; // auto default to false
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type in 'R' to register, 'L' to login, then press ENTER: ");

        try {
            while(!choice){
                String option = br.readLine();
                if(option.equals("R"))
                    register(br);
                else if(option.equals("L"))
                    login(br);
                else
                    System.err.println("Invalid Input, try again.");
            }
        } catch (Exception e) {
            System.err.println("You caught an exception. Well done for hacking the code");
            e.printStackTrace();
        }
        


        //gui_1();
    }

    public static void register(BufferedReader br){
        try {
            System.out.println("Enter a Username then press ENTER. \nGuidelines:");
            String username = br.readLine();
            username = username.toLowerCase(); // make it all not case sensitive

            System.out.println("Enter a Password then press ENTER. \nGuidelines:");
            String password = br.readLine();
            
            // for USERNAME
            // check charset a-z A-Z 0-9 _
            // check length of username is between 1 and 20 chars
            // check profanity filter
            // check if not already in database

            // for PASSWORD
            // 8 - 64 chars
            // must not match in the banned passwords list

            // if password and username are both valid
                // salt and hash password
                // put in database
            // else print error message
        } catch (Exception e) {
            System.err.println("You caught an exception. Well done for hacking the code");
            e.printStackTrace();
        }
    }

    public static void login(BufferedReader br){
        // 5 login attempts
    }

    public static void gui_1(){
        JFrame frame = new JFrame("Authentication System"); //Create and set up the window.
        frame.setVisible(true); // make visible
        frame.setSize(350, 500);

        Container c= frame.getContentPane(); // fill out the frame
        c.setLayout(null);

        JButton register = new JButton("Register"); // register button
        register.setBounds(50,50,100,50); //Setting the location and size of the JButton
        frame.add(register); //adding JButton to the frame's content pane

        JButton login = new JButton("Login"); //login button
        login.setBounds(200,50,100,50);
        frame.add(login);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}