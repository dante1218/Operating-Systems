import java.util.concurrent.Semaphore;


public class SmartPants extends Thread{
	public static long time = System.currentTimeMillis();
	private int count;
	protected Semaphore waitingDone;//use to block SmartPants until all contestant are done, initially set 0
	
	public SmartPants(int num){
		setName("SmartPants");
		waitingDone = new Semaphore(0);
		count = 0;
		start();
	}
	
	public void run(){
		while(count < main.num_contestant){
			for(int i=0; i<main.num_contestant; i++){
				if(main.ContestantArrival[i] == true){
					msg(" allow contestant-" + i + " to start his Date");
					main.ContestantArrival[i] = false;
					main.contestant[i].talkToSmartPants.release();//after talk, SmartPants will allow the current contestant move on
					count++;
				}
			}
		}

	    try {
			waitingDone.acquire();//SmartPants will be blocked until all contestants are finished
			msg("All Contestants are finished, " + getName() + " annonce that SHOW IS END!!!");
			for(int i=0; i<main.num_contestant; i++) {
				main.contestant[i].waitingEnd.release();
				if(i<main.num_date) main.date[i].DateAvailable.release();
			}//after SmartPants announce the end, he will let all dates and contestants know, and so they can leave
	    }
	    catch (InterruptedException e) {
			e.printStackTrace();
		}
		   
	}
	
	public void msg(String m) {
		 System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
}
