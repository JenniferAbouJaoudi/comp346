 
// Source code for semaphore class:

class Semaphore
{

    private int value;
    
    public Semaphore() {
        this.value = 0;
    }

    public Semaphore(int value) {
        this.value = value;

    }

    public synchronized void Wait(){
       this.value--;   
       if(this.value < 0) {
            try {
            	this.getWaitList();
                wait();
               // this.value--;
            }
            catch(InterruptedException e) {
                System.out.println ("Semaphore::Wait() - caught InterruptedException: " + e.getMessage() );
                e.printStackTrace();
            }
         }
    }

    public void getWaitList(){
        System.out.println("Number of waiting threads/prcess :" + Math.abs(this.value));
    }

    public synchronized void Signal() {
        ++this.value;
        if(this.value <= 0){
        	notify();
        }
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