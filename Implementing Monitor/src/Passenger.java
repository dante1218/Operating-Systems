import java.util.Random;

public class Passenger extends Thread{
	public static long time = System.currentTimeMillis();
	private GetIntoCar gic = null;
	private AskController ac = null;
	private int IDnum;
	private Random random = new Random();
	
    public Passenger(int id, GetIntoCar gic, AskController ac){
    	IDnum = id;
    	this.gic = gic;
    	this.ac = ac;
    	setName("Passenger-" + id);
    	start();
	}
    
	public void run(){
		msg("the passenger wanders around for a while");
		try {
			sleep(random.nextInt(300 - 100) + 100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		msg("the passenger gose to line up");
		gic.lineUp(IDnum);
		gic.passengerInCar(IDnum);
		msg("the passenger get off from the car");
		msg("Finished!");
		if(Main.numberOfFinishedPassenger == Main.num_Passenger)//if there is no passenger, then the last passenger will notify all cars and controllers, and then let them finish
			releaseCarAndController();
	}
	
	public void msg(String m) {
		 System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
	
	public synchronized void releaseCarAndController(){
		if(Main.passengerFinished == false){
		    Main.passengerFinished = true;
		    for(int i=0; i<Main.num_Car; i++) gic.passengerFinishedReleaseAllCar();
		    for(int i=0; i<Main.num_Controller; i++) ac.passengerFinishedReleaseAllController();
		}
	}
}
