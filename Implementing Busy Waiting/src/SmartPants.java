
public class SmartPants extends Thread{
	public static long time = System.currentTimeMillis();
	private int index;
	
	public SmartPants(int num){
		setName("SmartPants");
		index = num;
		start();
	}
	
	public void run(){
			while(main.contestantArrive == false){System.out.print("");}//SmartPants will wait until all contestant arrive
			
			for(int i=0; i<index; i++) {
				if(main.ContestantArrival[main.track[i]] != true){ //SmartPants will allow contestant to find date in the FCFS order
				    msg(" allow contestant-" + main.track[i] + " to start his Date");
					main.ContestantArrival[main.track[i]] = true; 
				}
			}

			while(true){
			    try {
			    	System.out.print("");
			        sleep(100000);//all the contestants are going to find Date, so SmartPants is waiting for them
			    }
			    
			    catch (InterruptedException e) {
		            msg(" congratulates contestant-"+ main.interrupt + ", and check the status of the show!" );
		            if(main.contestant_done == main.num_contestant) main.show_ends = true;
		            if(main.show_ends == true) {
		    	        msg("All Contestants are finished, " + getName() + " annonce that SHOW IS END!!!");
		    	        break;
		            }
		        }
		   }//SmartPants will sleep until contestant finished his game and interrupt him, and then SmartPants will congratulate the contestant and check the status of the show
	}
	
	public void msg(String m) {
		 System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
}
