import java.awt.Color;

import acm.program.*;
import acm.graphics.*;

public class chessBox extends GraphicsProgram {
   public GRect Box;  
   private boolean clicked;
   
   public void setclicked(boolean b){
	   clicked = b;
   }
   
   public boolean isClicked(){
	   if(clicked)
		   return true;
	   else
		   return false;
   }
   
   public void changeColor(){
	   Box.setFillColor(Color.LIGHT_GRAY);
	   Box.setFilled(true);
   }
}
