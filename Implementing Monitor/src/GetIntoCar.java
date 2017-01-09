import java.util.Vector;

public class GetIntoCar extends Object{
	private Vector<Object> line = new Vector<Object>();//use to enforce FCFS order for passenger
	private Object[] passengerInTheSameCar = new Object[Main.num_Car];//notification objects, use to block passenger who are in the same car
	private int passengerInLine = 0;//use to track how many passengers in the line
	private boolean allowToLoad = false;//use to check it is time to load passenger or not
	private int[] passengerOrder = new int[Main.num_Passenger];//use to track the order of passengers who are in the line because we need enforce FCFS order
	private int[] passengerInWhichCar = new int[Main.num_Passenger];//use to track passenger is in which car
	private int count = 0;
	private int count1 = 0;
	
	public void lineUp(int id){
		Object inLine = new Object();
		formLine(id, inLine);
	    synchronized (inLine){
	    	while(true){
    			try{
    				inLine.wait();
    			    break;
    			}
    			catch(InterruptedException e){
    				continue;
    			}
    		}
        }
	}
	
	private synchronized void formLine(int id, Object l){
		Main.passenger[id].msg("the passenger is in the line now");
    	passengerInLine++;//increase the number of passengerInLine by one
    	if(count == 0){
    		for(int j=0; j<Main.num_Car; j++) passengerInTheSameCar[j] = new Object();//initialize passengerInTheSameCar[]
    		for(int j=0; j<Main.num_Passenger; j++) passengerInWhichCar[j] = -1;//initialize passengerInWhichCar[]
    	}
    	passengerOrder[count] = id;//track the order of passenger coming
		count++;
		line.addElement(l);
		
		//if all passengers are in the line, then the last passenger will notify a car
		if(passengerInLine == Main.num_Passenger){
			allowToLoad = true;
			notify();
		}
	}
	
	public synchronized void waitPassenger(){
		while(!allowToLoad){//if the passenger doesn't line up yet, then car must wait
			try {
				wait();
				if(Main.numberOfFinishedPassenger == Main.num_Passenger) break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized int loadPassenger(int id, int availableSeat){
		while(availableSeat != 0 && passengerInLine > 0){//if the car is not full or there are passengers waiting, then keep to load passengers 
			passengerInWhichCar[passengerOrder[count1]] = id;//records which car, the passenger takes
			Main.car[id].msg("passenger-" + passengerOrder[count1] + " gets into the car");
			count1++;
			availableSeat--;//one passenger gets on the car, so number of available Seat decreased by one
			passengerInLine--;//a passenger is in the car, so the number of passenger in the line is decreased by one
			synchronized(line.elementAt(0)){
			    line.elementAt(0).notify();//notify the first people of the line, enforce FCFS
			}
			line.removeElementAt(0);
		}
		
		//if there are passengers still waiting, then the car will notify next car to load passenger; otherwise, it set allowToLoad to false which means stop loading
		if(passengerInLine == 0) allowToLoad = false;
		else notify();
		
		return availableSeat;
	}
	
	public void passengerInCar(int id){
		Main.passenger[id].msg("is in car " + passengerInWhichCar[id]);
		synchronized(passengerInTheSameCar[passengerInWhichCar[id]]){
			try {
				passengerInTheSameCar[passengerInWhichCar[id]].wait();//passengers who are in the same car will block in same condition variable
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized int releasePassenger(int id, int availableSeat){
		Main.car[id].msg("the car arrives, unload all passengers");
		Main.numberOfFinishedPassenger = Main.numberOfFinishedPassenger + (Main.num_Seat-availableSeat);
		Main.numberOfWaitingPassenger = Main.numberOfWaitingPassenger - (Main.num_Seat-availableSeat);
		System.out.println("there are " + Main.numberOfFinishedPassenger +" passenegers already finished!");
		System.out.println("there are " + Main.numberOfWaitingPassenger +" passenegers are still waiting for car!");
		availableSeat = Main.num_Seat;//passenger gone, the car becomes empty again
		synchronized(passengerInTheSameCar[id]){
			passengerInTheSameCar[id].notifyAll();//after a car arrives destination, it will unload all passengers who are on this car
		}
		return availableSeat;
	}
	
	public synchronized void passengerFinishedReleaseAllCar(){
		notifyAll();//when there are no more passengers, the last passenger will notify all cars to let them finish
	}
	
}
