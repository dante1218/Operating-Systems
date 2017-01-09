import java.util.Random;


public class Car extends Thread{
	public static long time = System.currentTimeMillis();
	private GetIntoCar gic= null;
	private AskController ac= null;
	private int IDnum;
	private int availableSeat = Main.num_Seat;
	private Random random = new Random();
	
	public Car(int id, GetIntoCar gic, AskController ac){
		IDnum = id;
		this.gic = gic;
		this.ac = ac;
		setName("Car-" + id);
		start();
	}
	
	public void run(){
		while(true){
			msg("the car is waiting");
			gic.waitPassenger();
			availableSeat = gic.loadPassenger(IDnum, availableSeat);
			if(Main.numberOfFinishedPassenger == Main.num_Passenger) break;			
			msg("the car checks if there is an available controller");
			ac.checkControllerStatus(IDnum);
			msg("the car asks for permission to depart");
			ac.askPermission(IDnum);
			msg("the car departs and rides around the park for a random amount of time");
			try {
				sleep(random.nextInt(300 - 100) + 100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			availableSeat = gic.releasePassenger(IDnum, availableSeat);
			if(Main.numberOfFinishedPassenger == Main.num_Passenger) break;	
		}
		msg("Finished!");
	}
	
	public void msg(String m) {
		 System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
}
