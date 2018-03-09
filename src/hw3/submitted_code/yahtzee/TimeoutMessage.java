package hw3.submitted_code.yahtzee;

/**
 * Message used by timer component to indicate to a caller that
 * the requested timeout has expired.
 */
public class TimeoutMessage extends AbstractMessage
{
  private boolean isLeftBroadcast;

  public TimeoutMessage(int correlationId, Component sender, boolean isLeftBroadcast)
  {
    super(correlationId, sender);
    this.isLeftBroadcast = isLeftBroadcast;
  }
  
  @Override
  public void dispatch(Component receiver)
  {
    receiver.handleTimeout(this);
  }

  public boolean isLeftBroadcast() { return isLeftBroadcast; }

}
