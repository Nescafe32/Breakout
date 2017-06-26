package breakout;

import java.util.Random;

public class Utils {

    public static int random (int max) {	// повернення випадкової цілої величини від 0 до max - 1
    	
    	Random random = new Random();
    	
        return (int) (random.nextDouble() * max);
    }

    public static int sign (double n) {		// визначення знаку значення
    	
        if (n == 0) {
            return 0;
        }
        
        if (n > 0) {
            return 1;   
        } 
        
        else {
            return -1;
        }
    }
}