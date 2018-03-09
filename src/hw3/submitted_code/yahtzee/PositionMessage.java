package hw3.submitted_code.yahtzee;

public class PositionMessage extends AbstractMessage {

    private int pos;
    private Integer data;

    public PositionMessage(Component sender, int pos, Integer data){
        super(sender);
        this.pos = pos;
        this.data = data;
    }

    public int getPos(){
        return pos;
    }

    public Integer getData(){ return data; }

    @Override
    public void dispatch(Component receiver) {
        receiver.handlePositionMessage(this);
    }
}
