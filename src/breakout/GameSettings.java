package breakout;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.util.Duration;
import javafx.collections.FXCollections;

public final class GameSettings {

    public static final Duration ANIMATION_TIME = Duration.millis(40);				// �������� �������
    public static final int MAX_LIVES = 8;											// ����������� ������� �����
    
    public static final int FIELD_BRICK_IN_ROW = 15;								// ������� ������ � �����

    public static final int WINDOW_BORDER = 3; 										// ����� ����
    public static final int SCREEN_WIDTH = 1010;									// ������ ���� �����
    public static final int SCREEN_HEIGHT = 725;									// ������ ���� �����

    public static final int INFO_TEXT_SPACE = 10;									// ������� �� ������� �� ���������, �� ����������� ������ (�����, �������)

    public static final int BRICK_WIDTH = 48;										// ������ ������
    public static final int BRICK_HEIGHT = 24;										// ������ ������
    public static final int SHADOW_WIDTH = 10;										// ������ ��
    public static final int SHADOW_HEIGHT = 16;										// ������ ��

    public static final double BALL_MIN_SPEED = 6;									// �������� �������� �'���
    public static final double BALL_MAX_SPEED = BRICK_HEIGHT;						// ����������� �������� �'���
    public static final double BALL_MIN_COORD_SPEED = 2;
    public static final double BALL_SPEED_INC = 0.5f;								// ���������� ��������� �������� �'���

    public static final int BOARD_Y = SCREEN_HEIGHT - 40;							// ������ ����� �� ���� ������
    public static final int BOARD_SPEED = 8;										// �������� ���� �����

    public static final int BONUS_SPEED = 3;										// ������� ������ ������

    public static final int FIELD_WIDTH = FIELD_BRICK_IN_ROW * BRICK_WIDTH; 		// ������ ����
    public static final int FIELD_HEIGHT = FIELD_WIDTH;								// ������ ����
    public static final int FIELD_Y = SCREEN_HEIGHT - FIELD_HEIGHT;			

    private static ObservableList<Image> ballsImages = FXCollections.<Image>observableArrayList();			// ������ ��������� �'����
    private static ObservableList<Image> boardsImages = FXCollections.<Image>observableArrayList(); 		// ������ ��������� ������ �����
    private static ObservableList<Image> bricksImages = FXCollections.<Image>observableArrayList(); 		// ������ ��������� ������
    private static ObservableList<Image> bonusesImages = FXCollections.<Image>observableArrayList(); 		// ������ ��������� ������
    private static ObservableList<Image> startWindowImages = FXCollections.<Image>observableArrayList(); 	// ������ ��������� ���������� ����
    private static ObservableList<Image> otherImages = FXCollections.<Image>observableArrayList(); 			// ������ ��������� ����� ���������
    
    public enum BallsImages {														// ������ ���� ������ �'����
    	BALL_SIZE0_IMAGE, BALL_SIZE1_IMAGE, BALL_SIZE2_IMAGE, BALL_SIZE3_IMAGE, BALL_SIZE4_IMAGE, BALL_SIZE5_IMAGE
    }
    
    public static final String[] BALLS_IMAGES = new String[] {						// ����� ��������� �'���� ����� ������
    	"ball/ball0.png",
        "ball/ball1.png",
        "ball/ball2.png",
        "ball/ball3.png",
        "ball/ball4.png",
        "ball/ball5.png"
    };
    
    public enum BoardsImages {														// ������ ����� ������ �����
    	BOARD_LEFT_IMAGE, BOARD_CENTER_IMAGE, BOARD_RIGHT_IMAGE,
    }
    
    public static final String[] BOARDS_IMAGES = new String[] {						// ����� ��������� ����� ������ �����
    	"board/left.png",
        "board/center.png",
        "board/right.png"
    };
    
	public enum BrickTypes {														// ������ ������ ����� ���� (�������)
		BLUE_BRICK, BROKEN1_BRICK, BROKEN2_BRICK, BROWN_BRICK, CYAN_BRICK, GREEN_BRICK, GREY_BRICK, MAGENTA_BRICK,
		ORANGE_BRICK, RED_BRICK, VIOLET_BRICK, WHITE_BRICK, YELLOW_BRICK
	}
	
	public static final String[] BRICKS_IMAGES = new String[] {						// ����� ��������� ������ ����� ���� (�������)
        "brick/blue.png",
        "brick/broken1.png",
        "brick/broken2.png",
        "brick/brown.png",
        "brick/cyan.png",
        "brick/green.png",
        "brick/grey.png",
        "brick/magenta.png",
        "brick/orange.png",
        "brick/red.png",
        "brick/violet.png",
        "brick/white.png",
        "brick/yellow.png"
    };
	
	public enum BonusTypes { 														// ������ ���� ������
		REDUCE_BALL_SPEED, GROW_BALL_SPEED,
		REDUCE_BOARD_SIZE, GROW_BOARD_SIZE,
		REDUCE_BALL_SIZE, GROW_BALL_SIZE,
		CATCH_BALL,
		FIRE_BALL,
		EXTRA_LIFE
	}
	
    public static final String[] BONUSES_IMAGES = new String[] {					// ����� ��������� ������
        "bonus/ballslow.png",
        "bonus/ballfast.png",
        "bonus/batreduce.png",
        "bonus/batgrow.png",
        "bonus/ballreduce.png",
        "bonus/ballgrow.png",
        "bonus/catch.png",
        "bonus/strike.png",
        "bonus/extralife.png"
    };
    
    public enum StartWindowImages {													// ������ ��������� ��� ���������� ����
    	BRICK_IMAGE, BREAKER_IMAGE, FIREBALL_IMAGE, PRESSANYKEY_IMAGE
    }
    
    public static final String[] START_WINDOW_IMAGES = new String [] {				// ����� ��������� ��� ���������� ����
    	"startwindow/break.png",
        "startwindow/out.png",
        "startwindow/RR.png",
        "startwindow/pressanykey.png"
    };
	
	public enum OtherImages {														// ������ ����� ���������
    	BACKGROUND_IMAGE, LOGO_IMAGE, READY_IMAGE, GAMEOVER_IMAGE, VLINE_IMAGE
    }
    
    public static final String[] OTHER_IMAGES = new String[] {						// ����� ����� ���������
        "other/background.jpg",
        "other/logo.png",
        "other/ready.png",
        "other/gameover.png",
        "other/vline.png"
    };

    public static void initializeAllImagesLists() {									// ����������� ������ ��� ���������
    	
    	for (String imageName : BALLS_IMAGES) { 									// ��� ������������ ��������� �� �'���
            final String path = "breakout/" + imageName;							// �������� ���������� �'���, �������� �� ��������� �����
            Image image = new Image(path);											// ��������� ����
            
            ballsImages.add(image);													// �� ������ ���������� �'��� � ������ �'����
        }
    	
    	for (String imageName : BOARDS_IMAGES) { 									// ��� ������������ ��������� �� �����
            final String path = "breakout/" + imageName;							// �������� ���������� �����, �������� �� ��������� �����
            Image image = new Image(path);											// ��������� ����
            
            boardsImages.add(image);												// �� ������ ���������� ����� � ������ ��������� �����
        }
    	
        for (String imageName : BRICKS_IMAGES) { 									// ��� ������������ ��������� �� �������
            final String path = "breakout/" + imageName;							// �������� ���������� �������, �������� �� ��������� �����
            Image image = new Image(path);											// ��������� ����
            
            bricksImages.add(image);												// �� ������ ���������� ������� � ������ ������
        }
        
        for (String imageName : BONUSES_IMAGES) {									// ��� ������������ ��������� �� ������
            final String path = "breakout/" + imageName;							// �������� ���������� ������, �������� �� ��������� �����
            Image image = new Image(path);											// ��������� ����
            
            bonusesImages.add(image);												// �� ������ ���������� ������ � ������ ������
        }
        
        for (String imageName : START_WINDOW_IMAGES) {								// ��� ������������ ��������� ���������� ����
            final String path = "breakout/" + imageName;							// �������� ���������� ��� ���������� ����, �������� �� ��������� �����
            Image image = new Image(path);											// ��������� ����
            
            startWindowImages.add(image);											// �� ������ �� ���������� � ������ ��������� ��� ���������� ����
        }
        
        for (String imageName : OTHER_IMAGES) {										// ��� ������������ ����� ���������
            final String path = "breakout/" + imageName;							// �������� ����������, �������� �� ��������� �����
            Image image = new Image(path);											// ��������� ����
            
            otherImages.add(image);													// �� ������ ���������� � ������ ����� ���������
        }
    }
    
    public static ObservableList<Image> getBallsImages() {							// ���������� ������ ��������� �'����
    	
        return ballsImages;
    }

    public static ObservableList<Image> getBoardsImages() {							// ���������� ������ ��������� ������ �����
    	
        return boardsImages;
    }

    public static ObservableList<Image> getBricksImages() {							// ���������� ������ ��������� ������
    	
        return bricksImages;
    }

    public static ObservableList<Image> getBonusesImages() {						// ���������� ������ ��������� ������
    	
        return bonusesImages;
    }
    
    public static ObservableList<Image> getStartWindowImages() {					// ���������� ������ ��������� ��� ���������� ����
    	
        return startWindowImages;
    }
    
    public static ObservableList<Image> getOtherImages() {							// ���������� ������ ����� ���������
    	
        return otherImages;
    }
}