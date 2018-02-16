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
            try {
              OutputStream os = new FileOutputStream(filename, true);
              PrintWriter pw = new PrintWriter(os);
              Date d = new Date();
              msgsToProcess.forEach((msg) -> { pw.println(d + " " + msg); });
              pw.close();
            } catch (FileNotFoundException e) { System.err.println("Unable to open log file: " + filename); }
          });
        }
      }
    }
  }
}
