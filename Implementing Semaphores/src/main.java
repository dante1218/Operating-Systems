import java.util.concurrent.Semaphore;

public class main {

	public static int num_contestant;
	public static int num_date;
	public static int num_rounds;
	public static int group_size;
	public static boolean[] ContestantArrival;//use together whit track[], track the order of contestant and determine which one meet SmartPants first
	public static boolean[] DateAvailable;//show the each date is available or not
	public static int contestant_done = 0;//a shared value, use to check how many contestants have finished all rounds
	public static int contestantInGroup = 0;
	public static SmartPants SP;//SmartPants
	public static Contestant[] contestant;//store every Contestants
	public static Date[] date;//store every Dates
	public static boolean checkContestantDone;//dates use this variable to check the show is end or not
	public static Semaphore idleDate;//use to block contestants when there are no available dates, initially set the num_date
	public static Semaphore checkArrival;//use to block contestants while there is a contestant is talking to SmartPants, initially set 1
	public static Semaphore makeGroup;//use to block a set of contestants until a group is made, initially set 0
	public static Semaphore protectUpdate;//use to protect the shared variable "contestant_done", initially set 1
	public static Semaphore protectAvailableDate;//use to prevent more than one contestants meet a date at same time, initially set 1
	public static Semaphore protectMakeGroup;//use to protect the shared variable contestantInGroup, initially set 1
	
	public static void main(String[] args){
		if(args.length < 3)
	    {
	        System.out.println("Invalid number of arguments.");
	        System.exit(1);
	    }//check command line
		
		num_contestant = Integer.parseInt(args[0]);;
		num_date = Integer.parseInt(args[1]);
		num_rounds = Integer.parseInt(args[2]);
		group_size = Integer.parseInt(args[3]);
		ContestantArrival = new boolean[num_contestant];
		DateAvailable = new boolean[num_date];
		checkContestantDone = false;
		idleDate = new Semaphore(num_date);
		checkArrival = new Semaphore(1);
		makeGroup = new Semaphore(0);
		protectUpdate = new Semaphore(1);
		protectAvailableDate = new Semaphore(1);
		protectMakeGroup = new Semaphore(1);
		
		for(int i=0; i<num_contestant; i++) {
			ContestantArrival[i] = false;
		}
		
		for(int i=0; i<num_date; i++) DateAvailable[i] = false;
		
		contestant = new Contestant[num_contestant];
		
		for(int i=0; i<num_contestant; i++) contestant[i] = new Contestant(i);
		
		SP = new SmartPants(num_contestant);
		
		date = new Date[num_date];
		
		for(int i=0; i<num_date; i++) date[i] = new Date(i);
	}

}
