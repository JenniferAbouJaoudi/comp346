import CharStackExceptions.CharStackEmptyException;
import CharStackExceptions.CharStackFullException;
import CharStackExceptions.CharStackInvalidAceessException;
import CharStackExceptions.CharStackInvalidSizeException;

// Source code for stack manager:

public class StackManager
{
    // The Stack
	
	private static CharStack stack ;
    private static final int NUM_ACQREL = 4; // Number of Producer/Consumer threads
    private static final int NUM_PROBERS = 1; // Number of threads dumping stack
    private static int iThreadSteps = 3; // Number of steps they take
    private static char[] charArray ={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};//array that will be used for reference of alphabet letters
    private static int topPosition=0;
    
    // initialization of the Semaphores
    public static Semaphore sem1; 
    public static Semaphore sem2;   

    
    // The main()
    public static void main(String[] argv)
    {   
    	
    	try {
    		//initialize stack with default value
			stack = new CharStack(10);
			
			//index for charArray
	    	topPosition = stack.getTop();
		} catch (CharStackInvalidSizeException e1) {
			e1.printStackTrace();
		}
    	
    	sem1 = new Semaphore(1);  
    	sem2 = new Semaphore(0);
    	
    	
        try
        {
            System.out.println("Main thread starts executing.");
            System.out.println("Initial value of top = " + stack.getTop() + ".");
            System.out.println("Initial value of stack top = " + stack.pick() + ".");
            System.out.println("Main thread will now fork several threads.");
        }
        catch(CharStackEmptyException e)
        {
            System.out.println("Caught exception: StackCharEmptyException");
            System.out.println("Message : " + e.getMessage());
            System.out.println("Stack Trace : ");
            e.printStackTrace();
        }
                    /*
                   * The birth of threads
                    */
        Consumer ab1 = new Consumer();
        Consumer ab2 = new Consumer();
        System.out.println ("Two Consumer threads have been created: " + ab1.iTID+","+ab2.iTID); 
        Producer rb1 = new Producer();
        Producer rb2 = new Producer();
        System.out.println ("Two Producer threads have been created. : " + rb1.iTID+","+rb2.iTID); 
        CharStackProber csp = new CharStackProber();
        System.out.println ("One CharStackProber thread has been created.");
                  /*
                 * start executing
                  */
        rb1.start();
        ab1.start();
        rb2.start();
        ab2.start();
        csp.start();
                 /*
                  * Wait by here for all forked threads to die
                 */
        try
        {
            ab1.join();
            ab2.join();
            rb1.join();
            rb2.join();
            csp.join();
            // Some final stats after all the child threads terminated...
            System.out.println("System terminates normally.");
            System.out.println("Final value of top = " + stack.getTop() + ".");
            System.out.println("Final value of stack top = " + stack.pick() + ".");
            System.out.println("Final value of stack top-1 = " + stack.getAt(stack.getTop() - 1) + ".");
            System.out.println("Stack access count = " + stack.getAccessCounter());
        }
        catch(InterruptedException e)
        {
            System.out.println("Caught InterruptedException: " + e.getMessage());
            System.exit(1);
        }
        catch(Exception e)
        {
            System.out.println("Caught exception: " + e.getClass().getName());
            System.out.println("Message : " + e.getMessage());
            System.out.println("Stack Trace : ");
            e.printStackTrace();
        }
       

    } // main()
    /*
    * Inner Consumer thread class
    */
    static class Consumer extends BaseThread
    {
        private char copy; // A copy of a block returned by pop()
        public void run()
        {        	       
        	sem2.P();
        	sem1.P();

        	//LOCK ACCESS WHEN PROCESS IS ACCESSING CRITICAL SECTION
            System.out.println ("Consumer thread [TID=" + this.iTID + "] starts executing.");
            Log.getInstance().log("Consumer thread [TID=" + this.iTID + "] starts executing.");
    		Log.getInstance().log("\n");

            for (int i = 0; i < StackManager.iThreadSteps; i++)  {  
            	//access critical section
                try {  
					this.copy = stack.getAt(stack.getTop());
					//decrease our index for stackArray
					topPosition--;
					//Pop value that is consumed
					stack.pop();  

		    		Log.getInstance().log("\n");
				} catch (CharStackEmptyException e) {
					e.printStackTrace();
				} catch (CharStackInvalidAceessException e) {
					e.printStackTrace();
				}finally{ 

	                System.out.println("Consumer thread [TID=" + this.iTID + "] pops character =" + this.copy);
                    Log.getInstance().log("Consumer thread [TID=" + this.iTID + "] pops character =" + this.copy);
	        		Log.getInstance().log("\n");

                    sem1.V();
                    
             }  
            //SIGNAL THE NEXT PROCESS THAT IT CAN ACCESS CRITICAL SECTION
            System.out.println ("Consumer thread [TID=" + this.iTID + "] terminates.");
            Log.getInstance().log("Consumer thread [TID=" + this.iTID + "] terminates.");
    		Log.getInstance().log("\n");

          	}
          sem2.V();
        } 
     } // class Consumer
    /*
   * Inner class Producer
    */
   static class Producer extends BaseThread
    {
        private char block; // block to be returned
        public void run()
        {	
        	sem1.P();
        	System.out.println ("Producer thread [TID=" + this.iTID + "] starts executing.");
            Log.getInstance().log("Producer thread [TID=" + this.iTID + "] starts executing.");
    		Log.getInstance().log("\n");


            for (int i = 0; i < StackManager.iThreadSteps; i++)  {
                try { 
                	//check the first character on the top
                	System.out.println("Top element of the stack :" + stack.getAt(stack.getTop()));
                	Log.getInstance().log("Top element of the stack :" + stack.getAt(stack.getTop()));
	        		Log.getInstance().log("\n");

                	//push to charStack 
					stack.push(charArray[topPosition+1]);
					topPosition++;
					//assign the stack value to block
					this.block = stack.getAt(stack.getTop());   

				} catch (CharStackFullException e) {
					e.printStackTrace();
				} catch (CharStackInvalidAceessException e) {
					e.printStackTrace();
				}finally{
	                System.out.println("Producer thread [TID=" + this.iTID + "] pushes character =" + this.block);
	                Log.getInstance().log("Producer thread [TID=" + this.iTID + "] pushes character =" + this.block);
	        		Log.getInstance().log("\n");


				}
             }   
            //SIGNAL THE NEXT PROCESS THAT IT CAN ACCESS CRITICAL SECTION
            System.out.println("Producer thread [TID=" + this.iTID + "] terminates.");  
            Log.getInstance().log("Producer thread [TID=" + this.iTID + "] terminates.");
    		Log.getInstance().log("\n");

            sem1.V();

            sem2.V();
          } 

    } // class Producer
    /*
   * Inner class CharStackProber to dump stack contents
    */
    static class CharStackProber extends BaseThread
    {
        public void run()
        {        
        	sem1.P();
          	int j =0; 
        	for(int i = 0; i<6 ;i++){
	    		System.out.print("STACK S = "); 
	    		Log.getInstance().log("STACK S = ");
	        		while(j < stack.getSize()){
	        			try {  
							System.out.print("["+stack.getAt(j)+"]");
							Log.getInstance().log("["+stack.getAt(j)+"]");
						} catch (CharStackInvalidAceessException e) {
							e.printStackTrace();
						}
	        			j++;
	        		}
	        		Log.getInstance().log("\n");
	        		System.out.println();
	        	j=0;  

        	}     
        	sem1.V();
       }
    } // class CharStackProber / 
} // class StackManager