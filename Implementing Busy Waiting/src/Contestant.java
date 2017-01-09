import java.util.Random;

public class Contestant extends Thread{
	public static long time = System.currentTimeMillis();
	private int id;
	private int rest_round = main.num_rounds;
	private int repeatDateCount = 0;
	private int[] repeatDate = new int[main.num_rounds];
	private boolean block = true;
	private Random random = new Random();
	private int[] haveContact;//use to store the id of dates, and these date has given her contact to this contestant
	private int count = 0;
	
	public Contestant(int num1) {
		    haveContact = new int[rest_round];//use to store the id of data who provide contact
		    for(int i=0; i<rest_round; i++) haveContact[i] = -1;
			id = num1;
			for(int i=0; i<main.num_rounds; i++ ) repeatDate[i] =-1;
			setName("Contestant-" +id);
			start();
	}
	
	public void run(){
		try {
			sleep(random.nextInt(5000 - 1000) +1000);
			main.track[main.count] = id;
			msg(" arrived the club.");//the track represents the order of the contestant, the first come people will be put in track[0], and so on.
			if(main.count < main.num_contestant) main.count++;
			if(main.count == main.num_contestant ) main.contestantArrive = true;
			while(main.ContestantArrival[id] != true) {System.out.print("");} //contestant is waiting SmartPants allow him to find date
			
			while(rest_round != 0){
			    for(int i=0; i<main.num_date; i++) {
			    	for(int j=0 ; j<repeatDateCount; j++){
			    		if(repeatDate[j] == i) {//check the date is met before or not
			    			block = false;
			    			break;
			    		}
			    	}
			    	if(block == false) {
			    		block = true;
			    		continue;//skip the current date because the contestant already met the date before
			    	}
			    	
				    if((blockContestant(i)) != true) {//prevent contestants meet same date at same time
				    	msg(" find date-" + i + "!");
				    	repeatDate[repeatDateCount] = i; //when the contestant meet a date, then we need to guaranteer that he will not see the date again, so we put the date in a array, and block the date for this contestant
				    	repeatDateCount++;
				    	while(main.DateAvailable[i] == true){System.out.print("");};//the contestant is waiting the date's decision
				    	if(main.date[i].giveContact == true) getInfo(i);//the date tell the contestant that she give or not give her contact
				    	--rest_round;//the contestant finish one round
				    }
				    if(rest_round == 0) break;//the contestant round is over, so he should get out of the loop and to find SmartPants
			    }
			    if(rest_round == 0) break;
			    yield();//no girl is available, so the contestant is waiting on the Juice Bar
			    sleep(random.nextInt(5000 - 1000) +1000);
			}
			msg(" finished his all rounds!");
			
			sleep(random.nextInt(5000 - 1000) +1000); //the contestant walks to the SmartPants;
			while(true){//the contestant pat SmartPants back and tell him that he is finished
			    if(main.SP.isInterrupted() == false) {
			    	setInterrupt(id);
			    	msg(" tells SmartPants that he is finished!");
			    	main.SP.interrupt();
			    	updateTheContestantDoneStatus();//the finished contestant updates contestant_done
			    	break;
			    }
			}
			
			while (main.show_ends == false){System.out.print("");};//the contestant waits the SmartPants to announce ENDING
			
		    msg("show is ended, " + getName() + " lingers outside the club to brag");
			sleep(random.nextInt(5000 - 1000) +1000);//lingers outside the club to brag
			
			if(id < main.num_contestant-1){
			    if(main.contestant[id+1].isAlive()) {//check next contestant is left or not
			    	int nextID = id+1;
			    	if(nextID == main.num_contestant-1) main.checkLast = true;
			        msg(" go home with Contetant-" + nextID + "!");
				    main.contestant[nextID].join();
			    }
			    else msg(" go home with himself!");
			}
			else if(main.checkLast != true) msg(" go home with himself!");//if no body join the last contestant, then he go home with himself
					
			//print outline of every contestant
			int checkGotDate = 0;
			String contact = "";
			for(int i=0; i<main.num_rounds; i++){
				if(haveContact[i] != -1) {
					contact = contact + " date-" + haveContact[i] + " ";
					checkGotDate++;
				}
			}
			if(checkGotDate != 0) msg(" HAS CONTACTS FROM "+ contact);//print the contacts every contestant got
			else msg(" DOES NOT GET ANY CONTACTS");//print, if the contestant didn't get any contacts
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void msg(String m) {
		 System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
	
	public synchronized void updateTheContestantDoneStatus(){
		++main.contestant_done;
	}
	
	public synchronized boolean blockContestant(int i){
		if(main.DateAvailable[i] == false) {
			main.DateAvailable[i] = true;//the array represent the status of every date, if it is false which means the date is available.
			return false;
		}
		return true;
	}
	
	public synchronized void getInfo(int num){
		haveContact[count++] = num;
	}
	
	public synchronized void setInterrupt(int num){
		main.interrupt = num;
	}

}
