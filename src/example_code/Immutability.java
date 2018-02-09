package example_code;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Immutability
{
}

// Thread-safe, mutable class still requires safe publication via
// a happens-before
class Configuration
{
  private String host;
  private int port;
  
  public Configuration(String h, int p)
  {
    host = h;
    port = p;
  }
  public synchronized String getHost()
  {
    return host;
  }
  public synchronized int getPort()
  {
    return port;
  }
  public synchronized void setHost(String h)
  {
    host = h;
  }
  public synchronized void setPort(int p)
  {
    port = p;
  }
}


// "effectively immutable" class requires safe publication via a happens-before
class Configuration1
{
  private String host;
  private int port;
  
  public Configuration1(String h, int p)
  {
    host = h;
    port = p;
  }
  public String getHost()
  {
    return host;
  }
  public int getPort()
  {
    return port;
  }
}


// immutable version can be freely shared between threads, even
// if not safely published
class Configuration2
{
  private final String host;
  private final int port;
  
  public Configuration2(String h, int p)
  {
    host = h;
    port = p;
  }
  public String getHost()
  {
    return host;
  }
  public int getPort()
  {
    return port;
  }
}


//Is this class immutable?
class Game
{
private final ArrayList<String> players;

public Game(ArrayList<String> arr)
{
 players = arr;
}

public boolean findPlayer(String s)
{
 return players.contains(s);  
}

public int count()
{
 return players.size();
}

public List<String> getPlayers()
{
 return players;
}
}

// is this class immutable?
//(from Paul Butcher, "Seven Concurrency Models in Seven Weeks", p. 50)
class MyDateParser
{
  private final DateFormat f;

  public MyDateParser()
  {
    f= new SimpleDateFormat("yyyy-MM-dd");
  }

  public Date create(String s) throws ParseException
  {
    return f.parse(s);
  }
}

