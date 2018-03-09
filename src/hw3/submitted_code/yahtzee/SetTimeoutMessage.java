package hw3.submitted_code.yahtzee;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * CubeMessage type used to request a message from the
 * timer component.  The 'originalId' will be used
 * as the correlation id in the TimeoutMessage, so that the
 * sender can use this message to refer to a different
 * message.
 */
public class SetTimeoutMessage extends AbstractMessage
{
  private int originalId;
  private int timeout; // ms
  private boolean isLeftBroadcast;
  private ScheduledThreadPoolExecutor exec;
  
  public SetTimeoutMessage(Component sender, int originalId, int timeout, boolean isLeftBroadcast,
                           ScheduledThreadPoolExecutor exec) {
    super(sender);
    this.originalId = originalId;
    this.timeout = timeout;
    this.isLeftBroadcast = isLeftBroadcast;
    this.exec = exec;
  }
  
  @Override
  public void dispatch(Component receiver)
  {
    receiver.handleSetTimeout(this);
  }

  public int getOriginalId()
  {
    return originalId;
  }
  
  public int getTimeout()
  {
    return timeout;
  }

  public boolean isLeftBroadcast() { return isLeftBroadcast; }

  public ScheduledThreadPoolExecutor getExec() {
    return exec;
  }
}
