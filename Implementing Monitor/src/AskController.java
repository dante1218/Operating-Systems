import java.util.Vector;

public class AskController extends Object{
	private Vector<Object> carWaitResponse = new Vector<Object>();//use to track the order of asking car
	private int availableController = 0;//use to show how many controllers are available now
	private Object carWaitAvailableController = new Object();//notification object, use to block car when it is waiting for available controller
	
	public synchronized void controllerWait(int id){
		try {
			synchronized(carWaitAvailableController){
				carWaitAvailableController.notifyAll();//the controller becomes available, so it notify all cars which are waiting for an available controller
			}
			availableController++;//a new controller arrives; or after a controller gave permission, it becomes available again
			Main.controller[id].msg("the controller is waiting for next car");
			wait();//the controller is waiting for next coming car
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void givePermission(int id){
		Main.controller[id].msg("the controller gives the car permission");
		synchronized(carWaitResponse.elementAt(0)){
			carWaitResponse.elementAt(0).notify();//the controller notify the specific car 
		}
		carWaitResponse.removeElementAt(0);
	}//the controller gives permission to a specific car
	
	public void checkControllerStatus(int id){
		Main.car[id].msg("the car is checking if there is an available controller");
		while(check(id)){//check if there are available controllers
			synchronized(carWaitAvailableController){
				try {
					carWaitAvailableController.wait();//if there is no available controller, then the car must wait
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
		}
	}//before a car ask for permission, it must check if there is an available controller; if yes, then ask for permission; if no, then wait for a controller becomes available
	
	private synchronized boolean check(int id){
		if(availableController == 0) {
			Main.car[id].msg("there is no available controller now, the car is waiting");
			return true;//no controller is available, return true
		}
		availableController--;//if there is an available controller, then the car will go to ask for it, so decrease availableController by one
		Main.car[id].msg("there are available controllers");
		return false;//there is available controller, return false
	}//check the controller's status
	
	public void askPermission(int id){
		Main.car[id].msg("the car asks permission of a random available controller for depart");
		Object carWait = new Object();
		notifyController(carWait);//notify the available controller
		synchronized(carWait){
			try {
				carWait.wait();//the car is asking permission, so it must wait for controller's response
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}//notify an available controller and ask for permission, then waiting for response. After getting permission, notify all cars which are waiting available controller
	
	private synchronized void notifyController(Object c){
		carWaitResponse.addElement(c);
		notify();//notify a random and available controller
	}
	
	public synchronized void passengerFinishedReleaseAllController(){
		notifyAll();//when there are no more passengers, the last passenger will notify all controllers to let them finish
	}
}
