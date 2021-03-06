package example_code;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Escape
{
}



class Foo5x
{
  private int[] a = new int[1];

  public synchronized void increment()
  {
    a[0] += 1;
  }

  public synchronized int get()
  {
    return a[0];
  }

  // *** Reference to 'a' has escaped, since caller may
  // still have an alias
  public synchronized void reset(int[] arr)
  {
    a = arr;
  }
}

// Example of an "improperly constructed" object, where
// 'this' escapes from the constructor. (All Swing applications are
// improperly constructed, additional idioms are used to ensure
// thread safety; see the complete FirstSwingExample's main method.)
class FirstSwingExample extends JPanel
{
  private JButton button;
  private JLabel label;
  private int count;

  public FirstSwingExample()
  {
    label = new JLabel("Push this button! ");
    this.add(label);
    button = new JButton("Push me");
    this.add(button);
    ActionListener listener = new MyButtonClickHandler();

    // *** 'this' is escaping from the constructor here,
    // since MyButtonClickHandler is an inner class.
    // Without additional precautions, it would be possible
    // for a click event to occur and attempt to invoke
    // operations on 'this' FirstSwingExample object before
    // it is fully constructed.
    button.addActionListener(listener);
  }

  private class MyButtonClickHandler implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent event)
    {
      ++count;
      label.setText("Pushed " + count + " times: ");
    }

  }
  // ... remaining details not shown ...
}


//(from Paul Butcher, "Seven Concurrency Models in Seven Weeks", p. 51)
class Tournament
{
  private List<String> players = new ArrayList<String>();

  public synchronized void addPlayer(String p)
  {
    players.add(p);
  }

  // *** Reference to 'this' escapes here, iterator can modify data w/o sync
  public synchronized Iterator<String> getPlayerIterator()
  {
    return players.iterator();
  }
}