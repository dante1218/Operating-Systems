import java.util.Random;
import java.util.concurrent.Semaphore;

public class Date extends Thread{
	public static long time = System.currentTimeMillis();
	protected boolean giveContact;
	private int id;
	private Random random = new Random();
	protected Semaphore DateAvailable;//use to block a date until a contestant apporach her, initially set 0
	protected Semaphore makeDesicion;//use to block a contestant until the date made a decision, initially set 0
	
	public Date(int num){
		id = num;
		DateAvailable = new Semaphore(0);
		makeDesicion = new Semaphore(0);
		setName("Date-" +id);
		start();		
	}
	
	public void run(){
		    try {
		    	while(main.checkContestantDone == false){
		    		DateAvailable.acquire();//the date is waiting for next contestant
		    		if(main.checkContestantDone == true) break;
		    	    int priority = getPriority();//get the date's default priority
			        setPriority(MAX_PRIORITY);//increase her priority
				    sleep(random.nextInt(300 - 100) + 100);//talk with the contestant
				    if((random.nextInt(2 - 0) +1) == 1) {//making decision; 1, she will give contact, 2 she will not
				    	msg(" gives contact!"); 
				    	giveContact = true;
				    }
				    else {
				    	msg(" does not give contact!");
				    	giveContact = false;
				    }
				    makeDesicion.release();//after give her answer, she will let the contestant move on
				    setPriority(priority);//set her priority back to previous value
				    main.DateAvailable[id] = false;//make her array value to be false which means she is available again
		    	}
		    	//msg("Show is ended, " + getName() + " left the club");
		    	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	public void msg(String m) {
		 System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}

}
