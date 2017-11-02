import java.util.Queue;

// Source code for semaphore class:

class Semaphore
{

    private int value;
    private int count;
    
    public Semaphore()
    {
        this.value = 0;
    }
    public Semaphore(int value)
    {	
        this.value = value;
    }
    public synchronized void Wait()
    {  
    /*number of threads/process waiting can be obtained using the absoluate value of the integer */
       this.value--;
    	
       if(this.value < 0)
        { 
            try
            {
                wait();
            }
            catch(InterruptedException e)
            {
                System.out.println ("Semaphore::Wait() - caught InterruptedException: " + e.getMessage() );
                e.printStackTrace();
            }
         	System.out.println("IM ACCESSING CRITICAL SECTION");
        }
    }
    public synchronized void Signal()
    { 
         ++this.value;
        if(this.value <= 0){
        	notify();
        }
    	System.out.println("WHO'S NEXT");
    }
    public synchronized void P()
    {
        this.Wait();
    }
    public synchronized void V()
    {
        this.Signal();
    }
}