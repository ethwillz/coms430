package hw3.submitted_code.yahtzee;

public class Message extends AbstractMessage {

    private boolean isLeftBroadcast;

    public Message(Component sender, boolean isLeftBroadcast){
        super(sender);
        this.isLeftBroadcast = isLeftBroadcast;
    }

    public boolean isLeftBroadcast(){
        return isLeftBroadcast;
    }

    @Override
    public Component getSender() {
        return sender;
    }

    @Override
    public void dispatch(Component receiver) {
        receiver.handleMessage(this);
    }
}
