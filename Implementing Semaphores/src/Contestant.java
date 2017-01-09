import java.util.Random;
import java.util.concurrent.Semaphore;

public class Contestant extends Thread{
	public static long time = System.currentTimeMillis();
	private int id;
	private int rest_round = main.num_rounds;
	private Random random = new Random();
	private int[] haveContact;//use to store the id of dates, and these date has given her contact to this contestant
	private int count = 0;
	protected Semaphore talkToSmartPants;//use to block a contestant until he finished the conversation with SmartPants, initially set 0
	protected Semaphore waitingEnd;//use to block a contestant until SmartPants announce that show is ended, initially set 0
	
	public Contestant(int num1) {
		    haveContact = new int[rest_round];//use to store the id of data who provide contact
		    for(int i=0; i<rest_round; i++) haveContact[i] = -1;
			id = num1;
			waitingEnd  = new Semaphore(0);
			talkToSmartPants = new Semaphore(0);
			setName("Contestant-" +id);
			start();
	}
	
	public void run(){
		try {
			sleep(random.nextInt(300 - 100) + 100);
			msg(" arrived the club.");//the track represents the order of the contestant, the first come people will be put in track[0], and so on.
			
			main.checkArrival.acquire();//if there is another contestant is talking with SmartPants, then any other contestants will be blocked in Semaphore until SmartPants is free
			main.ContestantArrival[id] = true;
			talkToSmartPants.acquire();//the contestant is talking to SmartPants
			main.checkArrival.release();//after the talking, the contestant free other contestants to talk to SmartPants
			
			while(rest_round != 0){
				main.idleDate.acquire();//if there is no available dates, then this contestant will be block in the semaphore
			    for(int i=0; i<main.num_date; i++) {
				    if((blockContestant(i)) != true) {//prevent contestants meet same date at same time, and also if the data is talking to another contestant, then the other contestants will not be allowed to access this data
				    	msg(" find date-" + i + "!");
				    	main.date[i].makeDesicion.acquire();//the contestant is waiting the date's decision
				    	if(main.date[i].giveContact == true) getInfo(i);//the date tell the contestant that she give or not give her contact
				    	--rest_round;//the contestant finish one round
				    	main.idleDate.release();//after the contestant finished the talk with date[i], he free a contestant who is blocked in the "idleDate" Semaphore
				    	break;
				    }
			    }
			}
			msg(" finished his all rounds!");
			
			updateTheContestantDoneStatus();//update the shared variable contestant_done. if this is the last contestant, then he will tell SmartPants that all contestants are done
			waitingEnd.acquire();//the contestant waits the SmartPants to announce ENDING
				
		   // msg("show is ended, " + getName() + " lingers outside the club to brag");
			sleep(random.nextInt(300 - 100) + 100);//lingers outside the club to brag
			
			group();//make a group
					
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
	
	public void updateTheContestantDoneStatus() throws InterruptedException{
		main.protectUpdate.acquire();//protect the shared variable contestant_done, only one contestant can update this variable at one time
		++main.contestant_done;
		if(main.contestant_done == main.num_contestant) {
			main.SP.waitingDone.release();//last contestant  will let SmartPants know that they are all done
			main.checkContestantDone = true;
		}
		main.protectUpdate.release();
	}
	
	public boolean blockContestant(int i) throws InterruptedException{
		main.protectAvailableDate.acquire();//protect main.date[i].DateAvailable and main.DateAvailable[i]
		if(main.DateAvailable[i] == false) {
			main.date[i].DateAvailable.release();//release a date who is waiting a contestant
			main.DateAvailable[i] = true;//the array represent the status of every date, if it is false which means the date is available.
			main.protectAvailableDate.release();
			return false;
		}
		main.protectAvailableDate.release();
		return true;
	}
	
	public void getInfo(int num){
		haveContact[count++] = num;
	}//record the dates who provide contact to this contestant

	public void group() throws InterruptedException{
		main.protectMakeGroup.acquire();//protect the shared variable contestantInGroup, only one contestant can update this variable at one time
		main.contestantInGroup++;
		if((main.contestantInGroup % main.group_size == 0) || (main.contestantInGroup == main.num_contestant)){//if there are group_size people, then they will go home in group
			msg("group created, these contestants are levaving.");                                             //else these contestant will wait until there are enough people
			main.makeGroup.release(main.group_size);                                                           //moreover, if there are only few people left, even they cannot make a group with group_size, then they will make a group to leave as well
			main.makeGroup = new Semaphore(0);
			main.protectMakeGroup.release();
		}
		else {
			main.protectMakeGroup.release();
			main.makeGroup.acquire();
		}
	}
}
