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
  private String filename;
  private BlockingQueue<String> queue;
  private ArrayList<String> msgsToProcess;
  private ExecutorService backgroundExec = Executors.newCachedThreadPool();
  
  public FileLogger(String filename, ArrayBlockingQueue<String> queue)
  {
    this.filename = filename;
    this.queue = queue;
    msgsToProcess = new ArrayList<>();
  }

  /**
   * Adds message to blocking queue
   * @param msg
   */
  public void log(String msg) {
    synchronized(queue) {
      queue.add(msg);
    }
  }

  public void run(){
    while(true) {
      //Synchronzied on queue because if queue is modified during draining, inconsistent results can follow
      synchronized(queue){
        if(queue.drainTo(msgsToProcess) > 0){
          //Schedules new event synchronized on the instance so that only 1 thread may write to file at a time
          backgroundExec.execute(() -> {
            synchronized (FileLogger.this) {
              try {
                OutputStream os = new FileOutputStream(filename, true);
                PrintWriter pw = new PrintWriter(os);
                msgsToProcess.forEach((msg) -> {
                  // timestamp when log method was called with this message
                  Date d = new Date();

                  pw.println(d + " " + msg);
                });
                pw.close();
              } catch (FileNotFoundException e) { System.err.println("Unable to open log file: " + filename); }
            }
          });
        }
      }
    }
  }
}
