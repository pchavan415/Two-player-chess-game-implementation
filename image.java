import acm.program.*;
import acm.graphics.*;

public class image {
    GImage pieceImage;
    boolean present;
    int value;
    
    public void moveImage(int x2,int y2){
    	pieceImage.setLocation(x2, y2);
    }
}
