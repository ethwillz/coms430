package hw3.submitted_code.yahtzee;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Timer component.  Sending a SetTimeoutMessage to this component
 * will cause a TimeoutMessage to be sent to the caller after
 * the timeout value given in the message.  The TimeoutMessage
 * will contain the "original ID" from the SetTimeoutMessage
 * as its correlation id.
 */
public class TimerComponent extends Component
{

  @Override
  public void send(IMessage message)
  {
    message.dispatch(this);
  }

  public void handleSetTimeout(SetTimeoutMessage msg){
    Runnable r = () -> msg.getSender().send(new TimeoutMessage(msg.getOriginalId(), msg.getSender(), msg.isLeftBroadcast()));
    msg.getExec().schedule(r, msg.getTimeout(), TimeUnit.MILLISECONDS);
  }

  @Override
  public void start()
  {
    // do nothing
  }
}
