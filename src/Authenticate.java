package src;

//import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.io.*;

public class Authenticate {
    private static final String RED = "\u001B[31m"; // to make things pretty :D
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String WHITE = "\u001B[37m";

    private static final String DELETE = "\033[F\u001b[0K"; // to remove passwords after they've been typed

    private static int attempts = 0; // number of user attempts
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

    private static boolean register(BufferedReader br) {
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
                            System.out.print(RED); // + "User already exists!");
                    else
                        System.out.println(RED + "Username contains profanity!");
                else
                    System.out.println(RED + "Username needs to between 2 and 20 characters!");
            else
                System.out.println(RED + "Username has invalid characters!");

            String password = "";

            if (UN) { // can do password check if username is ok
                System.out.println("Enter a Password then press ENTER.");
                System.out.println(PURPLE
                        + "Guidelines: \n- Password must not be commonly used \n- Password must be between 8 and 64 characters"
                        + WHITE);
                password = br.readLine();
                System.out.print(DELETE); // clear the screen
                System.out.println("Enter your Password again then press ENTER");
                String passwordConf = br.readLine();
                System.out.print(DELETE); // clear the screen

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
            }

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

    private static boolean login(BufferedReader br) {
        try {
            System.out.println("Enter your Username then press ENTER.");
            String username = br.readLine();
            System.out.println("Enter your Password then press ENTER.");
            String password = br.readLine();
            System.out.print(DELETE); // clear the screen
            if (detailsCorrect(username, password))
                return true;
            else {
                System.out.println(RED + "Invalid details!" + WHITE);
                attempts++;
                if (attempts >= 5) { // 5 login attempts
                    timer(); // 30 second timeout
                    attempts = 0;
                }
            }
        } catch (Exception e) {
            System.err.println(BLUE + "You caught an exception. Well done for hacking the code");
            e.printStackTrace();
        }
        return false;
    }

    // USERNAME CHECKS //
    private static boolean charset(String username) {
        boolean allCharsValid = true;
        for (char c : username.toCharArray()) {
            if (notvalid(c)) {
                allCharsValid = false;
                break; // don't need to do anymore checks
            }
        }
        return allCharsValid;
    }

    private static boolean notvalid(char c) {
        if ((c >= 48 && c <= 57) || (c >= 65 && c <= 90) || (c >= 97 && c <= 122) || (c == 95))
            return false; // is a 0-9 or A-Z or a-z or a _
        return true; // char isn't valid
    }

    private static boolean lengthu(String username) {
        if (username.length() >= 2 && username.length() <= 20)
            return true;
        return false;
    }

    private static boolean noProfanity(String username) {
        username = username.replaceAll("1", "i"); // remove leetspeak
        // username = username.replaceAll("!", "i");
        username = username.replaceAll("3", "e");
        username = username.replaceAll("4", "a");
        // username = username.replaceAll("@", "a");
        username = username.replaceAll("5", "s");
        username = username.replaceAll("7", "t");
        username = username.replaceAll("0", "o");
        username = username.replaceAll("9", "g");

        username = username.replaceAll("_", ""); // remove underscores

        String usernameo = removeConsecutiveDuplicates(username);
        String usernamet = removeConsecutiveDuplicatesDouble(username);

        // System.out.println(usernameo);
        // System.out.println(usernamet);
        
        Boolean one = check(usernameo); // remove repeating letters to one instance
        Boolean two = check(usernamet); // check then do again with repeating letters to two instances

        return (one && two); // if one of them is illegal then yep its ILLEGAL
    }

    private static boolean check(String username){
        // go through the badwords list
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/Word_Filter.csv"));
            String line = br.readLine();
            String[] split = line.split(",");// split and edit line

            while (line != null) { // while not finished reading
                if (split.length == 1)
                    if (username.contains(split[0])) { // if matches
                        br.close();
                        return false;
                    } else if (split.length == 2)
                        if (username.contains(split[0]) && !username.contains(split[1])) { // if matches
                            br.close();
                            return false;
                        }
                line = br.readLine();
                if (line != null)
                    split = line.split(","); // split and edit line
            }
            br.close();
        } catch (Exception e) {
            System.err.println(BLUE + "You caught an exception. Well done for hacking the code");
            e.printStackTrace();
        }
        return true;
    }

    public static String removeConsecutiveDuplicates(String input) {
        if(input.length() <= 1)
            return input;
        if(input.charAt(0) == input.charAt(1))
            return removeConsecutiveDuplicates(input.substring(1));
        else
            return input.charAt(0) + removeConsecutiveDuplicates(input.substring(1));
    }

    private static String removeConsecutiveDuplicatesDouble(String input) {
        char[] array = input.toCharArray(); 
        String output = "";
        
        if(input.length() <= 1)
            return input;

        for(int i = 0; i < array.length - 2; i++){
            if(array[i] == array[i+1]){
                if(array[i+1] != array[i+2])
                    output += array[i]; // append i to output
            } else
                output += array[i]; // append i to output
        }
        output += array[array.length - 2];
        output += array[array.length -1];
        return output;
    }

    private static boolean notInDatabase(String username) {
        try {
            File f = new File("src/database.csv"); // check if cvs exists, if not the create it
            if (!f.exists())
                return true;

            // try open csv database and search for each split[0]
            BufferedReader br = new BufferedReader(new FileReader("src/database.csv"));
            String line = br.readLine();
            String[] split = line.split(","); // split and edit line

            while (line != null) { // while not finished reading
                if (username.equals(split[0])) { // if matches
                    br.close();
                    return false;
                }
                line = br.readLine();
                if (line != null)
                    split = line.split(","); // split and edit line
            }
            br.close();

            return true;

        } catch (Exception e) {
            System.err.println(BLUE + "You caught an exception. Well done for hacking the code");
            e.printStackTrace();
            // return true; // no csv database yet so exception thrown
        }
        return true;
    }

    // PASSWORD CHECKS //
    private static boolean lengthp(String password) {
        if (password.length() >= 8 && password.length() <= 64)
            return true;
        return false;
    }

    private static boolean notBanned(String password) {
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

    // GENERATE HASHING //
    private static String generateHash(String password) {
        byte[] salt = generateSalt16Byte();
        String salt64 = base64Encoding(salt);
        //Argon2PasswordEncoder encoder = new Argon2PasswordEncoder();
        String hash = "$argon2id$v=13$m=65536,t=16,p=2$" + salt64 + "$" + base64Encoding(generateArgon2id(password, salt));
        //encoder.encode(password);
        return hash;
        //return password; // temporary
    }

    private static byte[] generateSalt16Byte() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private static byte[] generateArgon2id(String password, byte[] salt) {
        int opsLimit = 4;
        int memLimit = 1024 * 64;
        int outputLength = 32; // 32 bytes is reccommended
        int parallelism = 2;

        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(opsLimit)
                .withMemoryAsKB(memLimit)
                .withParallelism(parallelism)
                .withSalt(salt);
        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(builder.build());

        byte[] result = new byte[outputLength];
        gen.generateBytes(password.getBytes(StandardCharsets.UTF_8), result, 0,
                result.length);
        return result;
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    // CHECK HASHING //
    private static boolean hashMatch(String password, String input){
        String[] split = input.split("\\$");
        //System.out.println(split[0]);
        byte[] salt = base64Decoding(split[1]);
        String hash = base64Encoding(generateArgon2id(password, salt));
        if(hash.equals(split[2]))
            return true;
        else
            return false;
    }

    private static byte[] base64Decoding(String input) {
        try {
            return Base64.getDecoder().decode(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println(BLUE + "You caught an exception. Well done for hacking the code");
            e.printStackTrace();
        }
        return null;
    }
    
    // DATABASE STUFF //
    private static void addUser(String username, String password) {
        try {
            File f = new File("src/database.csv"); // check if cvs exists, if not the create it
            if (!f.exists())
                f.createNewFile();

            FileWriter fw = new FileWriter(f, true); // append mode
            BufferedWriter bw = new BufferedWriter(fw);

            password = generateHash(password); // hash password
            bw.write(username + "," + password); // insert username and password
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            System.err.println(BLUE + "You caught an exception. Well done for hacking the code");
            e.printStackTrace();
        }
    }

    private static boolean detailsCorrect(String username, String password) {
        try {
            File f = new File("src/database.csv"); // check if cvs exists, if not the create it
            if (!f.exists())
                return false;

            // try open csv database and search for each split[0]
            BufferedReader br = new BufferedReader(new FileReader("src/database.csv"));
            String line = br.readLine();
            String[] split = line.split(","); // split and edit line

            while (line != null) { // while not finished reading
                //System.out.println(hash(password)); // for debug
                if (username.equals(split[0]) && hashMatch(password, split[3])) { // if matches
                    br.close();
                    return true;
                }
                line = br.readLine();
                if (line != null)
                    split = line.split(","); // split and edit line
            }
            br.close();

            return false;
        } catch (Exception e) {
            System.err.println(BLUE + "You caught an exception. Well done for hacking the code");
            e.printStackTrace();
        }
        return false;
    }

    // TIMER //
    private static void timer() {
        try {
            int count = 30;
            System.out.println("Timeout: " + count + " Seconds");
            for (int i = count; count > 0; count--) {
                Thread.sleep(1000);
                System.out.println(DELETE + "Timeout: " + (count - 1) + " Seconds");
            }
        } catch (Exception e) {
            System.err.println(BLUE + "You caught an exception. Well done for hacking the code");
            e.printStackTrace();
        }
    }
}