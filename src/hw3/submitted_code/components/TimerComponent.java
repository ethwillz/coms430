package hw3.submitted_code.components;

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

  @Override
  public void start()
  {
    // TODO Auto-generated method stub
  }
}
