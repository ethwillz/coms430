package hw3.submitted_code.yahtzee;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Component representing a Yahtzee flash cube.  Each cube will broadcast
 * a PingMessage to the left and right every POLL_INTERVAL ms.  If no reply
 * is received within TIMEOUT ms, the cube assumes it has no neighbor
 * in that direction.  If a reply is received, the PingReplyMessage contains
 * the sender's current display value.  This is ignored for a right ping,
 * but for a left ping this value is the number of cubes to the receiver's left.
 */
public class Cube extends ThreadedComponent
{
  public static final int POLL_INTERVAL = 50; // ms
  public static final int TIMEOUT = 250;

  private enum STATE {
    STARTING, GENERATING, SCORING, DONE
  }
  
  private TimerComponent timer;
  private Component left, right;
  private int leftId, rightId;
  private int data;
  ScheduledThreadPoolExecutor exec;
  STATE state;

  public Cube(TimerComponent timer)
  {
    this.timer = timer;
    exec = new ScheduledThreadPoolExecutor(10);
    state = STATE.STARTING;
  }
  
  @Override
  public void send(IMessage message)
  {
    message.dispatch(this);
  }

  public void handleTimeout(TimeoutMessage msg){
    if(state == STATE.STARTING) {
      if (msg.isLeftBroadcast() && msg.getCorrelationId() > leftId) {
        left = null;
        leftId = msg.getCorrelationId();
        if (right != null) {
          Universe.updateDisplay(this, "S");
          right.send(new PositionMessage(this, 2, null));
        } else {
          Universe.updateDisplay(this, "?");
        }
      } else if (!msg.isLeftBroadcast() && msg.getCorrelationId() > rightId) {
        rightId = msg.getCorrelationId();
        right = null;
      }
    }
    if(state == STATE.GENERATING){
      if (msg.isLeftBroadcast() && msg.getCorrelationId() > leftId && left != null) {
        left = null;
        leftId = msg.getCorrelationId();
        data = (int) (Math.random() * 6 + 1);
        if (right == null) {
          Universe.updateDisplay(this, "" + data);
          state = STATE.SCORING;
        }
      } else if (!msg.isLeftBroadcast() && msg.getCorrelationId() > rightId && right != null) {
        right = null;
        if (left == null) {
          Universe.updateDisplay(this, "" + data);
          state = STATE.SCORING;
        }
      }
    }
    if(state == STATE.SCORING) {
      if (msg.isLeftBroadcast() && msg.getCorrelationId() > leftId) {
        left = null;
        leftId = msg.getCorrelationId();
        if (right != null) {
          Universe.updateDisplay(this, "D");
          right.send(new PositionMessage(this, 2, data));
        } else {
          Universe.updateDisplay(this, "" + data);
        }
      } else if (!msg.isLeftBroadcast() && msg.getCorrelationId() > rightId) {
        rightId = msg.getCorrelationId();
        right = null;
      }
    }
    if(state == STATE.DONE){
      if (msg.isLeftBroadcast() && msg.getCorrelationId() > leftId && left != null) {
        left = null;
        leftId = msg.getCorrelationId();
        if (right == null) {
          Universe.updateDisplay(this, "?" );
          state = STATE.STARTING;
        }
      } else if (!msg.isLeftBroadcast() && msg.getCorrelationId() > rightId && right != null) {
        right = null;
        if (left == null) {
          Universe.updateDisplay(this, "?");
          state = STATE.STARTING;
        }
      }
    }
  }

  public void handleMessage(Message msg){
      if (msg.isLeftBroadcast() && msg.getId() > rightId) {
        right = msg.getSender();
        rightId = msg.getId();
      } else if (!msg.isLeftBroadcast() && msg.getId() > leftId) {
        left = msg.getSender();
        leftId = msg.getId();
      }
  }

  public void handlePositionMessage(PositionMessage msg){
    if(state == STATE.STARTING) {
      if (msg.getPos() == 2) {
        Universe.updateDisplay(this, "T");
        right.send(new PositionMessage(this, 3, null));
      }
      if (msg.getPos() == 3) {
        Universe.updateDisplay(this, "A");
        right.send(new PositionMessage(this, 4, null));
      }
      if (msg.getPos() == 4) {
        Universe.updateDisplay(this, "R");
        right.send(new PositionMessage(this, 5, null));
      }
      if (msg.getPos() == 5) {
        Universe.updateDisplay(this, "T");
        state = STATE.GENERATING;
        left.send(new PositionMessage(this, -1, null));
      }
      if (msg.getPos() == -1) {
        state = STATE.GENERATING;
        left.send(new PositionMessage(this, -1, null));
      }
    }
    if(state == STATE.SCORING){
      if (msg.getPos() == 2) {
        Universe.updateDisplay(this, "O");
        right.send(new PositionMessage(this, 3, msg.getData() + data));
      }
      if (msg.getPos() == 3) {
        Universe.updateDisplay(this, "N");
        right.send(new PositionMessage(this, 4, msg.getData() + data));
      }
      if (msg.getPos() == 4) {
        Universe.updateDisplay(this, "E");
        right.send(new PositionMessage(this, 5, msg.getData() + data));
      }
      if (msg.getPos() == 5) {
        Universe.updateDisplay(this, "" + (msg.getData() + data));
        state = STATE.DONE;
        left.send(new PositionMessage(this, -1, msg.getData() + data));
      }
      if (msg.getPos() == -1) {
        state = STATE.DONE;
        left.send(new PositionMessage(this, -1, null));
      }
    }
  }

  @Override
  public void start()
  {
    Runnable r = () -> {
      try {
        Message left = new Message(this, true);
        Message right = new Message(this, false);
        int leftId = left.getId(), rightId = right.getId();
        Universe.broadcastLeft(left);
        Universe.broadcastRight(right);
        timer.send(new SetTimeoutMessage(this, leftId, TIMEOUT, true, exec));
        timer.send(new SetTimeoutMessage(this, rightId, TIMEOUT, false, exec));
      } catch (Throwable e) {
        e.printStackTrace();
      }
    };
    new ScheduledThreadPoolExecutor(1).scheduleAtFixedRate(r, 0, POLL_INTERVAL, TimeUnit.MILLISECONDS);
  }

}