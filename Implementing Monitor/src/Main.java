
public class Main {
	public static int num_Passenger;
	public static int num_Car;
	public static int num_Controller;
	public static int num_Seat;
	public static Passenger passenger[];
	public static Car car[];
	public static Controller controller[];
	public static int numberOfFinishedPassenger = 0;
	public static int numberOfWaitingPassenger;
	public static boolean passengerFinished = false;
	
	public static void main(String[] args){
		if(args.length < 4)
	    {
	        System.out.println("Invalid number of arguments.");
	        System.exit(1);
	    }//check command line
		
		num_Passenger = Integer.parseInt(args[0]);
		num_Car = Integer.parseInt(args[1]);
		num_Controller = Integer.parseInt(args[2]);
		num_Seat = Integer.parseInt(args[3]);
		
		passenger = new Passenger[num_Passenger];
		car = new Car[num_Car];
		controller = new Controller[num_Controller];
		numberOfWaitingPassenger = num_Passenger;
		GetIntoCar gic = new GetIntoCar();
		AskController ac = new AskController();
		
		System.out.println("There are totally " + num_Passenger + " Passengers, " + num_Car + " Cars, " + num_Controller + " Controllers, " + num_Seat + " Seats.");
		System.out.println("Start: ");
		
		for(int i=0; i<num_Passenger; i++) passenger[i] = new Passenger(i, gic, ac);
		for(int i=0; i<num_Car; i++) car[i] = new Car(i, gic, ac);
		for(int i=0; i<num_Controller; i++) controller[i] = new Controller(i, ac);
	}

}
