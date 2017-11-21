import common.BaseThread;

/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.  
 * 
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;

	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating. --> Done
	 * - Then sleep() for a random interval. --> Done
	 * - The print that they are done eating. --> Done
	 */
	public void eat()
	{
		try
		{
			System.out.println("Philosopher: " + getTID() + " started eating.");
			sleep((long)(Math.random() * TIME_TO_WASTE));
			System.out.println("Philosopher: " + getTID() + " finished eating.");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking. -- Done
	 * - Then sleep() for a random interval. --> Done
	 * - The print that they are done thinking. --> Done
	 */
	public void think() {
		try {
			System.out.println("Philosopher: " + getTID() + " started thinking.");
			sleep((long) (Math.random() * TIME_TO_WASTE));
			System.out.println("Philosopher: " + getTID() + " finished thinking.");
		}catch(InterruptedException e){
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
		// ...
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking. --> Done
	 * - Say something brilliant at random --> Done
	 * - The print that they are done talking. --> Done
	 */
	public void talk() {
		System.out.println("Philosopher: " + getTID() + " started talking.");
		saySomething();
		System.out.println("Philosopher: " + getTID() + " stopped talking.");
	}


	/**
	 * Override Thread.run()
	 */
	public void run()
	{
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			try {
				DiningPhilosophers.soMonitor.pickUp(getTID());
				sleep(100);
			}catch(InterruptedException e){
				System.out.println(e.toString());
			}

			eat();
			try {
				DiningPhilosophers.soMonitor.putDown(getTID());
				sleep(100);
			}catch(InterruptedException e){
				System.out.println(e.toString());
			}

			think();

			/*
			 * TODO:
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			*/
			if(((long)(Math.random()) % 2) == 0) // A random decison
			{
				// Some monitor ops down here...
				try {
					DiningPhilosophers.soMonitor.requestTalk();
					sleep(100);
				}catch (InterruptedException e){
					System.out.println(e.toString());
				}
				talk();
				try {
					DiningPhilosophers.soMonitor.endTalk();
					sleep(100);
				}catch (InterruptedException e){
					System.out.println(e.toString());
				}
				// ...
			}


		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething() {
		String[] astrPhrases = {
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	}
}
