package breakout;

import javafx.scene.Parent;
import javafx.scene.image.ImageView;

public class Ball extends Parent {
    
    public static final int DEFAULT_BALL_SIZE = 2; 					// ����� �'��� �� ������������� 
    public static final int MAX_BALL_SIZE = 5;						// ����������� �������� ����� �'���
    
    private int ballSize;											// ����� �'���
    private int ballDiameter;										// ������ �'���
    
    private ImageView ballImageView;								// ����������, ��� ���� ����������� �� ��'��� �'���

    public Ball() {													// ��������� ����������� ��������� �'���
    	
    	ballImageView = new ImageView();							// ��������� ������ ����������� ����������
        getChildren().add(ballImageView);							// ��������� ����������� ���������� �'��� �� ������
        
        changeBallSize(DEFAULT_BALL_SIZE);							// ������������ �'��� ������ �� �������������
    }

    public int getBallSize() {										// ��������� ������ �'���
    	
        return ballSize;
    }

    public int getBallDiameter() {									// ��������� ������� �'���
    	
        return ballDiameter;
    }

    public void changeBallSize(int newSize) {						// ���� ������ �'���
    	
        this.ballSize = newSize;									// ���������� ����� ������ �'���
        ballImageView.setImage(GameSettings.getBallsImages().get(GameSettings.BallsImages.BALL_SIZE0_IMAGE.ordinal() + ballSize));	// ���� ������ �'��� ������ ������������ ������ ����������
        ballDiameter = (int) ballImageView.getImage().getWidth() - GameSettings.SHADOW_WIDTH;
    }
}