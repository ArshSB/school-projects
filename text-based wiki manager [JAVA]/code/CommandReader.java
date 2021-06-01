// CLASS: CommandReader
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class processes a text file which includes commands to run the program
// as stipulated in the assignment. It processes commands based on if the input is valid
// and calls the appropriate methods in DocumentManager and UserManager to perform
// the operations listed in the text file
//-----------------------------------------

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class CommandReader {

    private UserManager userManager; // used to call such as user history and create user
    private DocumentManager docManager; // used to call DocumentManager methods
    private boolean quit; // used to check if QUIT command was processed, in which case the program should terminate

    public CommandReader() {

        userManager = new UserManager();
        docManager = new DocumentManager(userManager);
        quit = false; // set default value of quit
        getInputFile(); // start processing file and initiate program

    }

    public UserManager getUserManager() { return userManager; }

    public void setUserManager(UserManager userManager) { this.userManager = userManager; }

    public DocumentManager getDocManager() { return docManager; }

    public void setDocManager(DocumentManager docManager) { this.docManager = docManager; }

    public boolean getQuit() { return quit; }

    public void setQuit(boolean quit) { this.quit = quit; }

    //------------------------------------------------------
    // PURPOSE: this method asks the user for name of command file and processes them line by line
    // by calling processCommand. It also catches program-based errors such as invalid file
    // Returns: Void, only prints to STDOUT to inform user the outcome of command and program in general
    //------------------------------------------------------
    public void getInputFile() {

        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter name of file:");
        String fileName = scan.nextLine();

        try {

            File file = new File(fileName);
            scan = new Scanner(file);

            // only process file is end of file is not reached and QUIT command is not realized
            while (scan.hasNext() && quit == false) {

                // call processCommand on each line in file to initiate operation
                String process = processCommand(scan.nextLine().trim());

                if (!process.equals("EMPTY") && quit == false)
                    System.out.println(process + "\n");

            }

            // if QUIT command is realized, end program and inform user
            if (quit)
                System.out.println("\n\nBYE! Program finished successfully\n\n");

            // otherwise end program and inform user end of file was reached
            else
                System.out.println("\n\nEND OF FILE REACHED! Program finished successfully\n\n");

        }

        catch (FileNotFoundException e) {
            System.out.println("File not found. Terminating program.");
        }

        catch (IndexOutOfBoundsException e) {
            System.out.println("Error occurred while processing commands. Terminating program without QUIT command.");
        }

        catch (NumberFormatException e) {
            System.out.println("Error occurred while processing command that require integer as parameter. Terminating program without QUIT command.");
        }

        catch (Exception e) {
            System.out.println("Unknown error occurred. Terminating program without QUIT command.");
        }
    }

    // helper method to processCommand
    // this method combines a string array into a sentence (separated by whitespace) starting from given index
    private String combineArray(String[] arr, int start) {

        String result = "";

        for (int counter = start; counter < arr.length; counter++)
            result += arr[counter] + " ";

        return result.trim();

    }


    //------------------------------------------------------
    // PURPOSE: this method processed a command line and calls the appropriate method of DocumentManager
    // or UserManager if the given command is valid.
    // Parameters: String line - the command to be processed
    // Returns: (String) informs user whether the command was properly processed or not
    //------------------------------------------------------
    private String processCommand(String line) {

        // placeholder to inform user outcome of command. "EMPTY" represents a comment line was detected
        String result = "EMPTY";
        // split the command line into words for processing
        String[] tokens = line.split("\\s+");
        int len = tokens.length;

        if (tokens[0].charAt(0) == '#'); // if comment line is detected, do nothing by returning "EMPTY"

        // the following else if and else statements check if first word of the tokenized line match
        // any of the valid commands and calls the appropriate method in DocumentManager/UserManager
        // and stores the outcome into result

        else if (tokens[0].equals("USER") && len == 2)
            result = userManager.createUser(tokens[1]);

        else if (tokens[0].equals("CREATE") && len == 3)
            result = docManager.createDocument(tokens[1], tokens[2]);

        else if (tokens[0].equals("APPEND") && len >= 4) {

            String content = combineArray(tokens, 3);
            result = docManager.appendLine(tokens[1], tokens[2], content);

        }

        else if (tokens[0].equals("REPLACE") && len >= 5) {

            String content = combineArray(tokens, 4);
            result = docManager.replaceLine(tokens[1], tokens[2], Integer.parseInt(tokens[3]), content);

        }

        else if (tokens[0].equals("DELETE") && len == 4)
            result = docManager.deleteLine(tokens[1], tokens[2], Integer.parseInt(tokens[3]));

        else if (tokens[0].equals("PRINT") && len == 2)
            result = docManager.printDocument(tokens[1]);

        else if (tokens[0].equals("RESTORE") && len == 4)
            result = docManager.restoreDocument(tokens[1], tokens[2], Integer.parseInt(tokens[3]));

        else if (tokens[0].equals("HISTORY") && len == 2)
            result = docManager.printHistory(tokens[1]);

        else if (tokens[0].equals("USERREPORT") && len == 2)
            result = userManager.printUserHistory(tokens[1]);

        else if (tokens[0].equals("QUIT"))
            quit = true;

        else {
            result = "INVALID COMMAND";
            docManager.increment();
        }

        return result;

    }
}
