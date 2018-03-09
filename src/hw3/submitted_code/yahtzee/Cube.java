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

    private enum STATE { STARTING, GENERATING, SCORING, DONE }

    private TimerComponent timer;
    private Component left, right;
    private int leftId, rightId;
    private int data;
    private ScheduledThreadPoolExecutor exec;
    private STATE state;

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
        if(state == STATE.STARTING || state == STATE.SCORING) {
            if (msg.isLeftBroadcast() && msg.getCorrelationId() > leftId) {
                left = null;
                leftId = msg.getCorrelationId();
                if (right != null) {
                  if (state == STATE.STARTING) {
                      Universe.updateDisplay(this, "S");
                      right.send(new PositionMessage(this, 2, null));
                  } else { // state == scoring
                      Universe.updateDisplay(this, "D");
                      right.send(new PositionMessage(this, 2, data));
                  }
                } else {
                  if (state == STATE.STARTING) Universe.updateDisplay(this, "?");
                  else Universe.updateDisplay(this, "" + data);
                }
            } else if (!msg.isLeftBroadcast() && msg.getCorrelationId() > rightId) {
                rightId = msg.getCorrelationId();
                right = null;
            }
        }

        if(state == STATE.GENERATING || state == STATE.DONE) {
            if (msg.isLeftBroadcast() && msg.getCorrelationId() > leftId && left != null) {
                left = null;
                leftId = msg.getCorrelationId();
                data = (int) (Math.random() * 6 + 1);
                if (right == null) {
                    if (state == STATE.GENERATING) {
                        Universe.updateDisplay(this, "" + data);
                        state = STATE.SCORING;
                    } else { //state == done
                        Universe.updateDisplay(this, "?");
                        state = STATE.STARTING;
                    }
                }
            } else if (!msg.isLeftBroadcast() && msg.getCorrelationId() > rightId && right != null) {
                right = null;
                if (left == null) {
                    if (state == STATE.GENERATING) {
                        Universe.updateDisplay(this, "" + data);
                        state = STATE.SCORING;
                    } else {
                        Universe.updateDisplay(this, "?");
                        state = STATE.STARTING;
                    }
                }
            }
        }
    }

    public void handleCubeMessage(CubeMessage msg){
        if (msg.isLeftBroadcast() && msg.getId() > rightId) {
            right = msg.getSender();
            rightId = msg.getId();
        } else if (!msg.isLeftBroadcast() && msg.getId() > leftId) {
            left = msg.getSender();
            leftId = msg.getId();
        }
    }

    public void handlePositionMessage(PositionMessage msg){
        if (msg.getPos() == 2) {
            if(state == STATE.STARTING) {
                Universe.updateDisplay(this, "T");
                right.send(new PositionMessage(this, 3, null));
            } else { //state == scoring
                Universe.updateDisplay(this, "O");
                right.send(new PositionMessage(this, 3, msg.getData() + data));
            }
        }
        if (msg.getPos() == 3) {
            if(state == STATE.STARTING) {
                Universe.updateDisplay(this, "A");
                right.send(new PositionMessage(this, 4, null));
            } else {
                Universe.updateDisplay(this, "N");
                right.send(new PositionMessage(this, 4, msg.getData() + data));
            }
        }
        if (msg.getPos() == 4) {
            if(state == STATE.STARTING) {
                Universe.updateDisplay(this, "R");
                right.send(new PositionMessage(this, 5, null));
            } else {
                Universe.updateDisplay(this, "E");
                right.send(new PositionMessage(this, 5, msg.getData() + data));
            }
        }
        if (msg.getPos() == 5) {
            if(state == STATE.STARTING) {
                Universe.updateDisplay(this, "T");
                state = STATE.GENERATING;
                left.send(new PositionMessage(this, -1, null));
            } else {
                Universe.updateDisplay(this, "" + (msg.getData() + data));
                state = STATE.DONE;
                left.send(new PositionMessage(this, -1, msg.getData() + data));
            }
        }
        if (msg.getPos() == -1) {
            if(state == STATE.STARTING) state = STATE.GENERATING;
            else state = STATE.DONE;
            left.send(new PositionMessage(this, -1, null));
        }
    }

    @Override
    public void start()
    {
        Runnable r = () -> {
            try {
                CubeMessage left = new CubeMessage(this, true);
                CubeMessage right = new CubeMessage(this, false);
                Universe.broadcastLeft(left);
                Universe.broadcastRight(right);
                timer.send(new SetTimeoutMessage(this, left.getId(), TIMEOUT, true, exec));
                timer.send(new SetTimeoutMessage(this, right.getId(), TIMEOUT, false, exec));
            } catch (Throwable e) {
                e.printStackTrace(); // scheduled executor doesn't show exceptions unless caught
            }
        };
        new ScheduledThreadPoolExecutor(1).scheduleAtFixedRate(r, 0, POLL_INTERVAL, TimeUnit.MILLISECONDS);
    }

}