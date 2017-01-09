public class main {

	public static int num_contestant;
	public static int num_date;
	public static int num_rounds;
	public static int track[];//track the order of arrival contestant
	public static boolean[] ContestantArrival;//use together whit track[], track the order of contestant and determine which one meet SmartPants first
	public static int count = 0;//use together with track[] to determine the order of contestant's arrival
	public static volatile boolean[] DateAvailable;//show the each date is available or not
	public static boolean contestantArrive = false;//is all contestants arrived, I will set it to true, and then SmartPants will allow contestants to begin their game
	public static int contestant_done = 0;//a shared value, use to check how many contestants have finished all rounds
	public static volatile boolean show_ends = false;
	public static SmartPants SP;//SmartPants
	public static Contestant[] contestant;//store every Contestants
	public static Date[] date;//store every Dates
	public static int interrupt;//store the contestant who interrupt SmartPants
	public static boolean checkLast;
	
	public static void main(String[] args){
		if(args.length < 2)
	    {
	        System.out.println("Invalid number of arguments.");
	        System.exit(1);
	    }//check command line
		
		num_contestant = Integer.parseInt(args[0]);;
		num_date = Integer.parseInt(args[1]);
		num_rounds = Integer.parseInt(args[2]);
		track = new int[num_contestant];
		ContestantArrival = new boolean[num_contestant];
		DateAvailable = new boolean[num_date];
		checkLast = false;
		
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
	
	public static void result(int array[], int count){
		
	}

}
