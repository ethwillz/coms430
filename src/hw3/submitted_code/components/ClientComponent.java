package hw3.submitted_code.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Client component receives text messages from an input
 * component and sends requests to a database proxy.
 */
public class ClientComponent extends ThreadedComponent
{
  
  /**
   * Reference to database proxy. 
   */
  private Component db;
  private TimerComponent timer;
  private ScheduledThreadPoolExecutor exec;
  
  /**
   * Local cache of key/value pairs we've already looked up.
   */
  private ArrayList<Record> cache;

  /**
   * Map of message id to key of requests for which we're waiting for a result.
   */
  private Map<Integer, Integer> pending;
  
  public ClientComponent(Component db, TimerComponent timer)
  {
    this.db = db;
    this.timer = timer;
    cache = new ArrayList<>();
    pending = new HashMap<>();
    exec = new ScheduledThreadPoolExecutor(1);
  }

  public void handleTimeout(TimeoutMessage msg) {
    synchronized(pending){ //in case both containsKey checks pass and 2 threads attempt to remove entry
      int id = msg.getCorrelationId();
      if(pending.containsKey(id)) {
        System.out.println("Request for ID " + pending.remove(id) + " timed out");
        System.out.println("Enter id number to look up, 'd' to display list, 'q' to quit");
      }
    }
  }
  
  public void handleText(TextMessage msg)
  {
    parseResponse(msg.getText());
  }
  
  public void handleResult(ResultMessage msg)
  {
    synchronized(pending){
      int id = msg.getCorrelationId();
      if(pending.containsKey(id)) {
        Integer key = pending.remove(id);
        if (key != null) {
          String result = getLocalValue(key);
          if (result == null) {
            result = msg.getResult();
            cache.add(new Record(key, result));
          }
          System.out.println("Value for id " + key + ": " + result);
          System.out.println("Enter id number to look up, 'd' to display list, 'q' to quit");
        }
      }
    }
  }

  /**
   * Parses the string entered by user and takes appropriate action.
   * @param s
   */
  private void parseResponse(String s)
  {
    s = s.trim();
    if (s.length() == 0)
    {
      System.out.println("Enter id number to look up, 'd' to display list, 'q' to quit");
    }
    else if (isNumeric(s))
    {
      int key = Integer.parseInt(s);
      doLookup(key);
    }
    else
    {
      char ch = s.charAt(0);
      if (ch == 'd')
      {
        display();
      }
      else
      {
        System.out.println("Please enter 'd' or an id number");
      }
    }
  }
  
  /**
   * Looks up the value for the given key, retrieving it from the 
   * slow database if not present in the local list.
   * @param key
   */
  private void doLookup(int key)
  {
    String value = getLocalValue(key);
    if (value == null)
    {
      IMessage msg = new RequestMessage(this, key);
      int id = msg.getId();
      pending.put(id, key);
      db.send(msg);
      timer.send(new SetTimeoutMessage(this, id, 100, exec));
    }
    else
    {
      System.out.println("Value for id " + key + ": " + value);
      System.out.println("Enter id number to look up, 'd' to display list, 'q' to quit");
    }
  }
 
  /**
   * Returns the value for given key, or null if not present in the list.
   * @param key
   * @return
   */
  private String getLocalValue(int key)
  {
    for (Record r : cache)
    {
      if (r.key() == key)
      {
        return r.value();
      }
    }
    return null;
  }

  
  /**
   * Displays all key/value pairs in local list.
   */
  private void display()
  {
    for (int i =  0; i < cache.size(); ++i)
    {
      Record r = cache.get(i);     
      System.out.println(r.key() + " " + r.value());
    }
    System.out.println("Enter id number to look up, 'd' to display list, 'q' to quit");
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
