package breakout;

import javafx.scene.Parent;
import javafx.scene.image.ImageView;

public class Ball extends Parent {
    
    public static final int DEFAULT_BALL_SIZE = 2; 					// розм≥р м'€ча за замовчуванн€м 
    public static final int MAX_BALL_SIZE = 5;						// максимально можливий розм≥р м'€ча
    
    private int ballSize;											// розм≥р м'€ча
    private int ballDiameter;										// д≥аметр м'€ча
    
    private ImageView ballImageView;								// зображенн€, €ке буде встановлено на об'Їкт м'€ча

    public Ball() {													// початкова ≥н≥ц≥ал≥зац≥€ параметр≥в м'€ча
    	
    	ballImageView = new ImageView();							// створенн€ нового в≥дображенн€ зображенн€
        getChildren().add(ballImageView);							// додаванн€ в≥дображенн€ зображенн€ м'€ча на панель
        
        changeBallSize(DEFAULT_BALL_SIZE);							// встановленн€ м'€чу розм≥ру за замовчуванн€м
    }

    public int getBallSize() {										// отриманн€ розм≥ру м'€ча
    	
        return ballSize;
    }

    public int getBallDiameter() {									// отриманн€ д≥аметру м'€ча
    	
        return ballDiameter;
    }

    public void changeBallSize(int newSize) {						// зм≥на розм≥ру м'€ча
    	
        this.ballSize = newSize;									// визначенн€ новго розм≥ру м'€ча
        ballImageView.setImage(GameSettings.getBallsImages().get(GameSettings.BallsImages.BALL_SIZE0_IMAGE.ordinal() + ballSize));	// зм≥на розм≥ру м'€ча шл€хом встановленн€ нового зображенн€
        ballDiameter = (int) ballImageView.getImage().getWidth() - GameSettings.SHADOW_WIDTH;
    }
}