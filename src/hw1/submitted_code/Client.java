package hw1.e;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * The most obvious change is running a new thread in the doLookup method. During the thread's execution, the getValueFromDB
 * method has a check to see if the key is in the cache, if it is not the key is added to the cache and it is sorted.
 * This creates a race condition where a thread may have added to the list and experiences a context switch before the
 * cache is sorted. To fix this I synchronized all access to the cache on the cache's intrinsic lock, which will ensure
 * that the cache will stay sorted since only one thread can access its state at any given time.
 */
public class Client
{
  public static final String HOST = "localhost";
  public static final int PORT = 2222;
  
  /**
   * Local cache of key/value pairs we've already looked up.
   */
  private ArrayList<Record> cache;
  
  /**
   * Scanner for console input.
   */
  private Scanner scanner;
  
  /**
   * Indicates whether the client should be shut down.
   */
  private boolean done = false;
  
  /**
   * Entry point.
   * @param args
   */
  public static void main(String[] args)
  {
    new Client().go();
  }
  
  public Client()
  {
    cache = new ArrayList<Record>();
    scanner = new Scanner(System.in);
  }
  
  /**
   * Main.java client loop.
   */
  public void go()
  {
    while (!done)
    {
      String response = getResponse();
      parseResponse(response);
    }
    
  }
  
  /**
   * Prints a menu and returns the text entered by user.
   * @return
   */
  private String getResponse()
  {
    System.out.println();
    System.out.println("Enter id number to look up, 'd' to display list, 'q' to quit");
    System.out.print("Your choice: ");
    return scanner.nextLine();
  }
  
  /**
   * Parses the string entered by user and takes appropriate action.
   * @param s
   */
  private void parseResponse(String s)
  {
    s = s.trim();
    if (isNumeric(s))
    {
      int key = Integer.parseInt(s);
      doLookup(key);
    }
    else
    {
      char ch = s.charAt(0);
      if (ch == 'd')
      {
        displayAll();
      }
      else if (ch == 'q')
      {
        done = true;
      }
      else
      {
        System.out.println("Please enter 'd', 'q', or an id number");
      }
    }
  }
  
  /**
   * Returns the value for the given key, retrieving it from the 
   * slow database if not present in the local list.
   * @param key
   * @return
   */
  private void doLookup(int key)
  {
    String value = getLocalValue(key);
    if (value != null)
    {
      display(key, value);
    }
    else
    {
      new Thread(() -> getValueFromDB(key)).start();
    }   
  }
  
  /**
   * Look up given key in the slow database and add it to the local list
   * after displaying the result.
   * @param key
   */
  private void getValueFromDB(int key)
  {
    Socket s = null;
    try {
      // open a connection to the server
      s = new Socket(HOST, PORT);

      // for line-oriented output we use a PrintWriter
      PrintWriter pw = new PrintWriter(s.getOutputStream());
      pw.println("" + key);
      pw.flush();  // don't forget to flush...    

      // read response, which we expect to be line-oriented text
      Scanner scanner = new Scanner(s.getInputStream());
      String value = scanner.nextLine();
      display(key, value);

      synchronized (cache) { //Locked on cache object
        // make sure it's in the local cache
        if (getLocalValue(key) == null) {
          cache.add(new Record(key, value));
          Collections.sort(cache);
        }
      }
    }
    catch (IOException e)
    {
      System.out.println(e);
    }
    finally
    {
      // be sure streams are closed
      try
      {
        s.close();
      }
      catch (IOException ignore){}
    }

  }

  /**
   * Returns true if the given string represents a positive integer.
   * @param s
   * @return
   */
  private boolean isNumeric(String s)
  {
    for (int i = 0; i < s.length(); ++i)
    {
      if (!Character.isDigit(s.charAt(i)))
      {
        return false;
      }
    }
    return true;
  }
  
  /**
   * Displays key/value pair.
   * @param key
   * @param value
   */
  private void display(int key, String value)
  {
    System.out.println("Value for id " + key + ": " + value);
  }
  
  /**
   * Displays all key/value pairs in local list.
   */
  private void displayAll()
  {
    synchronized(cache) { //Locked on cache object
      cache.forEach((record) -> System.out.println(record.key() + " " + record.value()));
    }
  }
  
  /**
   * Returns the value for given key, or null if not present in the list.
   * @param key
   * @return
   */
  private String getLocalValue(int key)
  {
    synchronized(cache){ //now synchronized on cache object
      // binary search, since the list is sorted
      int start = 0;
      int end = cache.size() - 1;
      while (start <= end) {
        int mid = (start + end) / 2;
        Record currentRecord = cache.get(mid);
        if (key == currentRecord.key()) {
          return currentRecord.value();
        } else if (key < currentRecord.key()) {
          end = mid - 1;
        } else {
          start = mid + 1;
        }
      }
    }
    
    // not found
    return null;      
  }
  
  /**
   * Key/value pair.
   */
  private static class Record implements Comparable<Record>
  {
    private final int key;
    private final String value;
    
    public Record(int key, String value)
    {
      this.key = key;
      this.value = value;
    }
    
    public int key()
    {
      return key;
    }
    
    public String value()
    {
      return value;
    }

    @Override
    public int compareTo(Record rhs)
    {
      return this.key - rhs.key;
    }
  }
}
