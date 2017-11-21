/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca  
 */
public class Monitor  
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	private static int[] chopsticks;
	private Philosopher[] philosopher_list;
	private static boolean can_talk = true;

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		chopsticks = new int[piNumberOfPhilosophers];
		for (int i = 0; i < chopsticks.length; i++){
			chopsticks[i] = -1;
		}
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	public void setPhilosophers(Philosopher[] philosopher_list){
		this.philosopher_list = philosopher_list;
	}

	private int philosopher_index(final int piTID){
		int philosopher_seating = -1;
		for (int i = 0; i < philosopher_list.length; i++){
			if (philosopher_list[i].getTID() == piTID){
				philosopher_seating = i;
				break;
			}
		}
		return philosopher_seating;
	}

	private int get_left_chopstick_index(int philosopher_index){
		if (philosopher_index == 0){
			return chopsticks.length - 1;
		}
		return philosopher_index - 1;
	}

	private int get_right_chopstick_index(int philosopher_index){
		if (philosopher_index == (chopsticks.length - 1)){
			return 0;
		}
		return philosopher_index + 1;
	}

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
			throws InterruptedException {

		int philosopher_seating = philosopher_index(piTID);
		int left_chopstick_index = get_left_chopstick_index(philosopher_seating);
		int right_chopstick_index = get_right_chopstick_index(philosopher_seating);

		while ((chopsticks[left_chopstick_index] != -1) && (chopsticks[right_chopstick_index] != -1)) {
			wait();
		}
		chopsticks[left_chopstick_index] = piTID;
		chopsticks[right_chopstick_index] = piTID;
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
			throws InterruptedException {

		int philosopher_seating = philosopher_index(piTID);
		int left_chopstick_index = get_left_chopstick_index(philosopher_seating);
		int right_chopstick_index = get_right_chopstick_index(philosopher_seating);

		chopsticks[left_chopstick_index] = -1;
		chopsticks[right_chopstick_index] = -1;
		notify();
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
			throws InterruptedException {
		while(!can_talk){
			wait();
		}
		can_talk = false;
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
			throws InterruptedException {
		can_talk = true;
		notify();
	}
}

// EOF
