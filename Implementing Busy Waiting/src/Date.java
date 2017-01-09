import java.util.Random;

public class Date extends Thread{
	public static long time = System.currentTimeMillis();
	protected boolean giveContact;
	private int id;
	private Random random = new Random();
	//private int count = 1;
	
	public Date(int num){
		id = num;
		setName("Date-" +id);
		start();		
	}
	
	public void run(){
		    try {
		    	while(true){
		    	    while(main.DateAvailable[id] == false && main.show_ends == false){System.out.print("");} // when the array is not true which means the date is waiting to be approached
		    	    if(main.show_ends == true) break;
		    	    int priority = getPriority();//get the date's default priority
			        setPriority(MAX_PRIORITY);//increase her priority
				    sleep(random.nextInt(5000 - 1000) + 1000);//talk with the contestant
				    if((random.nextInt(2 - 0) +1) == 1) {//making decision; 1, she will give contact, 2 she will not
				    	msg(" gives contact!"); 
				    	giveContact = true;
				    }
				    else {
				    	msg(" does not give contact!");
				    	giveContact = false;
				    }
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
