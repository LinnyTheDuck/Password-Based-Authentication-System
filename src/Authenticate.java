package src;

import java.io.*;

public class Authenticate{
    private static boolean choice = false; // auto default to false
    public static void main(String[] args) {
        try {
            Boolean REG = false, LOG = false;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Type in 'R' to register, 'L' to login, then press ENTER: ");

            while(!choice){
                String option = br.readLine();

                if(option.equals("R"))
                    REG = register(br);
                else if(option.equals("L"))
                    LOG = login(br);
                else
                    System.err.println("Invalid Input, try again.");
                
                if(REG){
                    System.out.println("Sucsessful Registration!");
                    REG = false;
                }
                else if(LOG){
                    System.out.println("Welcome!");
                    LOG = false;
                }

            }
        } catch (Exception e) {
            System.err.println("You caught an exception. Well done for hacking the code");
            e.printStackTrace();
        }
    }

    public static boolean register(BufferedReader br){
        try {
            boolean UN = false, PW = false;

            System.out.println("Enter a Username then press ENTER. \nGuidelines:");
            String username = br.readLine();
            username = username.toLowerCase(); // make it all not case sensitive

            System.out.println("Enter a Password then press ENTER. \nGuidelines:");
            String password = br.readLine();
            
            if(charset(username)) // check charset a-z A-Z 0-9 _
                if(lengthu(username)) // check length of username is between 2 and 20 chars
                    if(noProfanity(username)) // check profanity filter
                        if(notInDatabase(username)) // check if not already in database
                            UN = true;
                        else
                            System.out.println("User already exists!");
                    else
                        System.out.println("Username contains profanity!");
                else
                    System.out.println("Username needs to between 2 and 20 characters!");
            else
                System.out.println("Username has invalid characters!");

            if(lengthp(password)) // 8 - 64 chars
                if(notBanned(password)) // must not match in the banned passwords list
                    PW = true;
                else
                    System.out.println("Password is too common!");
            else
                System.out.println("Password must be between 8 and 64 characters!");

            if(PW && UN){ // if password and username are both valid
                addUser(username, password); // salt and hash password & put in database
                return true;
            }
            else{
                System.err.println("Invalid details!");
                return false;
            }
        } catch (Exception e) {
            System.err.println("You caught an exception. Well done for hacking the code");
            e.printStackTrace();
        }

        return false; // just incase
    }

    public static boolean login(BufferedReader br){
        int attempts = 0;
        while(attempts < 5){ // 5 login attempts

        }
        
        return false;
    }

    // USERNAME CHECKS //
    public static boolean charset(String username){
        boolean allCharsValid = true;
        for (char c : username.toCharArray()){
            if(notvalid(c)){
                allCharsValid = false;
                break; // don't need to do anymore checks
            }
        }
        return allCharsValid;
    }

    public static boolean notvalid(char c){
        if((c >= 48 && c <= 57) || (c >= 65 && c <= 90) || (c >= 97 && c <= 122) || (c == 95))
            return false; // is a 0-9 or A-Z or a-z or a _
        return true; // char isn't valid
    }

    public static boolean lengthu(String username){
        if(username.length() > 2 && username.length() < 20)
            return true;
        return false;
    }

    public static boolean noProfanity(String username){
        return false;
    }

    public static boolean notInDatabase(String username){
        return false;
    }

    // PASSWORD CHECKS //
    public static boolean lengthp (String password){
        if(password.length() > 8 && password.length() < 64)
            return true;
        return false;
    }

    public static boolean notBanned (String password){
        try {
            BufferedReader br = new BufferedReader(new FileReader("rockyou.txt"));
            String line = br.readLine();
            while (line != null){ // while not finished reading
                if(line.equals(password)){ // if matches
                    br.close();
                    return false; //return false;
                }
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            System.err.println("You caught an exception. Well done for hacking the code");
            e.printStackTrace();
        }
        return false;
    }

    // DATABASE STUFF //
    public static void addUser(String username, String password){

    }
}