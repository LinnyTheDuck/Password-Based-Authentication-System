package src;

import java.io.*;

public class Authenticate {
    public static final String RED = "\u001B[31m"; // to make things pretty :D
    public static final String YELLOW = "\u001B[33m";
    public static final String GREEN = "\u001B[32m";
    public static final String CYAN = "\u001B[36m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String WHITE = "\u001B[37m";

    public static final String DELETE = "\033[F\u001b[0K"; // to remove passwords after they've been typed

    private static boolean choice = false; // auto default to false

    public static void main(String[] args) {
        try {
            Boolean REG = false, LOG = false;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            while (!choice) {
                System.out.println(WHITE + "Type in 'R' to register, 'L' to login, 'X' to exit, then press ENTER: ");
                String option = br.readLine();

                if (option.equals("R"))
                    REG = register(br);
                else if (option.equals("L"))
                    LOG = login(br);
                else if (option.equals("X"))
                    choice = true; // exit the while loop and program
                else
                    System.err.println(RED + "Invalid Input, try again.");

                if (REG) {
                    System.out.println(GREEN + "Sucsessful Registration!");
                    REG = false;
                } else if (LOG) {
                    System.out.println(RED + "W" + YELLOW + "e" + GREEN + "l" + CYAN + "c" + BLUE + "o" + PURPLE + "m"
                            + RED + "e" + YELLOW + "!");
                    LOG = false;
                }

            }
        } catch (Exception e) {
            System.err.println(BLUE + "You caught an exception. Well done for hacking the code");
            e.printStackTrace();
        }
    }

    public static boolean register(BufferedReader br) {
        try {
            boolean UN = false, PW = false;

            System.out.println("Enter a Username then press ENTER.");
            System.out.println(PURPLE
                    + "Guidelines: \n- Username is not case sensitive \n- Must be between 2 and 20 characters \n- Must only consist of the characters: 0-9 a-z A-Z _ \n- Must not include profanity"
                    + WHITE);
            String username = br.readLine();
            username = username.toLowerCase(); // make it all not case sensitive

            if (charset(username)) // check charset a-z A-Z 0-9 _
                if (lengthu(username)) // check length of username is between 2 and 20 chars
                    if (noProfanity(username)) // check profanity filter
                        if (notInDatabase(username)) // check if not already in database
                            UN = true;
                        else
                            System.out.println(RED + "User already exists!");
                    else
                        System.out.println(RED + "Username contains profanity!");
                else
                    System.out.println(RED + "Username needs to between 2 and 20 characters!");
            else
                System.out.println(RED + "Username has invalid characters!");

            System.out.println("Enter a Password then press ENTER.");
            System.out.println(PURPLE
                    + "Guidelines: \n- Password must not be commonly used \n- Password must be between 8 and 64 characters"
                    + WHITE);
            String password = br.readLine();
            System.out.println(DELETE); // clear the screen
            System.out.println("Enter your Password again then press ENTER");
            String passwordConf = br.readLine();
            System.out.println(DELETE); // clear the screen

            if (lengthp(password)) // 8 - 64 chars
                if (notBanned(password)) // must not match in the banned passwords list
                    if (password.equals(passwordConf))
                        PW = true;
                    else
                        System.out.println(RED + "Passwords do not match!");
                else
                    System.out.println(RED + "Password is too common!");
            else
                System.out.println(RED + "Password must be between 8 and 64 characters!");

            if (PW && UN) { // if password and username are both valid
                addUser(username, password); // salt and hash password & put in database
                return true;
            } else {
                System.err.println(RED + "Invalid details!");
                return false;
            }
        } catch (Exception e) {
            System.err.println(BLUE + "You caught an exception. Well done for hacking the code");
            e.printStackTrace();
        }

        return false; // just incase
    }

    public static boolean login(BufferedReader br) {
        try {
            int attempts = 0;
            while (attempts < 5) { // 5 login attempts
                System.out.println("Enter your Username then press ENTER.");
                String username = br.readLine();
                System.out.println("Enter your Password then press ENTER.");
                String password = br.readLine();
                System.out.println(DELETE); // clear the screen
                if (detailsCorrect(username, password))
                    return true;
            }
        } catch (Exception e) {
            System.err.println(BLUE + "You caught an exception. Well done for hacking the code");
            e.printStackTrace();
        }
        return false;
    }

    // USERNAME CHECKS //
    public static boolean charset(String username) {
        boolean allCharsValid = true;
        for (char c : username.toCharArray()) {
            if (notvalid(c)) {
                allCharsValid = false;
                break; // don't need to do anymore checks
            }
        }
        return allCharsValid;
    }

    public static boolean notvalid(char c) {
        if ((c >= 48 && c <= 57) || (c >= 65 && c <= 90) || (c >= 97 && c <= 122) || (c == 95))
            return false; // is a 0-9 or A-Z or a-z or a _
        return true; // char isn't valid
    }

    public static boolean lengthu(String username) {
        if (username.length() >= 2 && username.length() <= 20)
            return true;
        return false;
    }

    public static boolean noProfanity(String username) {
        return true;
    }

    public static boolean notInDatabase(String username) {
        return true;
    }

    // PASSWORD CHECKS //
    public static boolean lengthp(String password) {
        if (password.length() >= 8 && password.length() <= 64)
            return true;
        return false;
    }

    public static boolean notBanned(String password) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/rockyou.txt"));
            String line = br.readLine();
            while (line != null) { // while not finished reading
                if (line.equals(password)) { // if matches
                    br.close();
                    return false; // return false;
                }
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            System.err.println(BLUE + "You caught an exception. Well done for hacking the code");
            e.printStackTrace();
        }
        return true;
    }

    // DATABASE STUFF //
    public static void addUser(String username, String password) {

    }

    public static boolean detailsCorrect(String username, String password) {
        return false;
    }
}