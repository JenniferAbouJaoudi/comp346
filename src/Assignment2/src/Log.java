import java.io.*;

public class Log {
    private static Log instance;
    private final String filename = "Output_i.txt";
    private Log(){

    }

    public static Log getInstance(){
        if (instance == null){
            instance = new Log();
            instance.log("------------------------------\n");
        }
        return instance;
    }

    public void log(String info){
        try {
            FileWriter fw = new FileWriter(filename, true);
            fw.write(info + "\n");
            fw.close();
        }
        catch(IOException ioe){
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
}

