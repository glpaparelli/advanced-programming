import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

public class TTTController 
    extends 
        javax.swing.JLabel 
    implements  
        VetoableChangeListener
{
    
    public TTTController(){
        this.lastMove = null;
        this.defaultBackground = Color.LIGHT_GRAY;
    }
    
    //a move by a player is attempted
    @Override
    public void vetoableChange(PropertyChangeEvent evt) 
        throws 
            PropertyVetoException 
    {   
        if(this.lastMove == null){
            this.lastMove = (String) evt.getNewValue();
            this.updateLabel(this.lastMove);
        }
        //if X attempt to click while he was also the last one to click we raise an exception
        else if(evt.getNewValue().equals(lastMove)){
            throw new PropertyVetoException("move not allowd", evt);
        }else{
            this.lastMove = (String) evt.getNewValue();
            this.updateLabel(this.lastMove);
        }
    }
    
    //update the label to show who is the next one to move
    private void updateLabel(String lastMove){
        this.setBackground(Color.ORANGE);
        if(lastMove.equals("x"))
            this.setText("NEXT MOVE: O");
        else
            this.setText("NEXT MOVE: X");
    }
    
    public void matchEnded(String result){
        if(result.equals("draw")){
            this.setText(result.toUpperCase());
            this.setBackground(Color.WHITE);
        }else{
            this.setText("THE WINNER: " + result.toUpperCase());
            this.setBackground(Color.GREEN);
        }
    }
    
    public void resetController(){
        this.lastMove = null;
        this.setText("START GAME");
        this.setBackground(this.defaultBackground);
    }
               
    private String lastMove;
    private final Color defaultBackground;
    
}
