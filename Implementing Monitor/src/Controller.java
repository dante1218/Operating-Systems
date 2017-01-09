import java.util.Random;


public class Controller extends Thread{
	public static long time = System.currentTimeMillis();
	private int IDnum;
	private AskController ac= null;
	private Random random = new Random();
	
	public Controller(int id, AskController ac){
		IDnum = id;
		this.ac = ac;
		setName("Controller-" + id);
		start();
	}
	
	public void run(){
		while(true){
			msg("the controller is waiting for next car");
			ac.controllerWait(IDnum);
			if(Main.numberOfFinishedPassenger == Main.num_Passenger) break;	
			msg("the controller is going to check the ticket");
			try {
				sleep(random.nextInt(300 - 100) + 100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			msg("the controller gives the car permission");
			ac.givePermission(IDnum);
			if(Main.numberOfFinishedPassenger == Main.num_Passenger) break;	
		}
		msg("Finished!");
	}

	public void msg(String m) {
		 System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
}
