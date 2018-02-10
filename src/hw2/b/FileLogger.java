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
  private ExecutorService backgroundExecutor;
  
  public FileLogger(String filename, ArrayBlockingQueue<String> queue)
  {
    this.filename = filename;
    this.queue = queue;
    msgsToProcess = new ArrayList<>();
    backgroundExecutor = Executors.newCachedThreadPool();
  }

  /**
   * Adds message to blocking queue
   * @param msg
   */
  public void log(String msg) {
    synchronized(queue) { queue.add(msg); }
  }

  public void run(){
    while(true) {
      //Synchronized on queue because if queue is modified during draining, inconsistent results can follow
      synchronized(queue){
        if(queue.drainTo(msgsToProcess) > 0){
          //Schedules writes to file locked on the Logger instance to avoid threads attempting to open multiple streams
          backgroundExecutor.execute(() -> {
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