package hw2.b;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.*;


public class FileLogger implements Runnable
{
  private ExecutorService backgroundExec = Executors.newCachedThreadPool();
  ArrayList<String> msgsToProcess;
  private BlockingQueue<String> queue;
  private String filename;
  
  public FileLogger(String filename, ArrayBlockingQueue<String> queue)
  {
    this.filename = filename;
    this.queue = queue;
    msgsToProcess = new ArrayList<>();
  }

  public synchronized void log(String msg)
  {
    // timestamp when log method was called with this message
    Date d = new Date();
    try
    {
      // argument 'true' means append to existing file
      OutputStream os = new FileOutputStream(filename, true);
      PrintWriter pw = new PrintWriter(os);
      pw.println(d + " " + msg);
      pw.close();
    }
    catch (FileNotFoundException e)
    {
      System.err.println("Unable to open log file: " + filename); 
    }
  }

  public void run(){
    while(true){
      queue.drainTo(msgsToProcess);
      backgroundExec.execute(() -> msgsToProcess.forEach(this::log));
    }
  }
}
