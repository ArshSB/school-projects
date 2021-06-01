// CLASS: WikiTime
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class manages the current time (based on how many commands were processed)
// as explained in assignment. It acts as superclass to DocumentManager and UserManager
// so both subclasses can update the time whenever their respective methods are called
//-----------------------------------------

public class WikiTime {

    private static int time;
    // current time value of program (number of commands processed, regardless if they were successful or not)

    public WikiTime() { time = 0; }

    public int getTime() { return time; }

    public void setTime(int time) { this.time = time; }

    // increment time by 1, used when a command was processed in DocumentManager or UserManager
    public void increment() { time++; }

}
