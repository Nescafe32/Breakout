package breakout;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.util.Duration;
import javafx.collections.FXCollections;

public final class GameSettings {

    public static final Duration ANIMATION_TIME = Duration.millis(40);				// швидк≥сть ан≥мац≥њ
    public static final int MAX_LIVES = 8;											// максимальна к≥льк≥сть житт≥в
    
    public static final int FIELD_BRICK_IN_ROW = 15;								// к≥льк≥сть цеглин в р€дку

    public static final int WINDOW_BORDER = 3; 										// рамка в≥кна
    public static final int SCREEN_WIDTH = 1010;									// ширина в≥кна форми
    public static final int SCREEN_HEIGHT = 725;									// висота в≥кна форми

    public static final int INFO_TEXT_SPACE = 10;									// в≥дстань в≥д надпису до показника, що характеризуЇ надпис (р≥вень, рахунок)

    public static final int BRICK_WIDTH = 48;										// ширина цеглин
    public static final int BRICK_HEIGHT = 24;										// висота цеглин
    public static final int SHADOW_WIDTH = 10;										// ширина т≥н≥
    public static final int SHADOW_HEIGHT = 16;										// висота т≥н≥

    public static final double BALL_MIN_SPEED = 6;									// м≥н≥мальна швидк≥сть м'€ча
    public static final double BALL_MAX_SPEED = BRICK_HEIGHT;						// максимальна швидк≥сть м'€ча
    public static final double BALL_MIN_COORD_SPEED = 2;
    public static final double BALL_SPEED_INC = 0.5f;								// коеф≥ц≥Їнт зб≥льшенн€ швидкост≥ м'€ча

    public static final int BOARD_Y = SCREEN_HEIGHT - 40;							// висота дошки в≥д низу екрану
    public static final int BOARD_SPEED = 8;										// щвидк≥сть руху дошки

    public static final int BONUS_SPEED = 3;										// швидк≥ть пад≥нн€ бонусу

    public static final int FIELD_WIDTH = FIELD_BRICK_IN_ROW * BRICK_WIDTH; 		// ширина пол€
    public static final int FIELD_HEIGHT = FIELD_WIDTH;								// висота пол€
    public static final int FIELD_Y = SCREEN_HEIGHT - FIELD_HEIGHT;			

    private static ObservableList<Image> ballsImages = FXCollections.<Image>observableArrayList();			// список зображень м'€ч≥в
    private static ObservableList<Image> boardsImages = FXCollections.<Image>observableArrayList(); 		// список зображень чатсин дошки
    private static ObservableList<Image> bricksImages = FXCollections.<Image>observableArrayList(); 		// список зображень цеглин
    private static ObservableList<Image> bonusesImages = FXCollections.<Image>observableArrayList(); 		// список зображень бонус≥в
    private static ObservableList<Image> startWindowImages = FXCollections.<Image>observableArrayList(); 	// список зображень стартового в≥кна
    private static ObservableList<Image> otherImages = FXCollections.<Image>observableArrayList(); 			// список зображень ≥нших зображень
    
    public enum BallsImages {														// перел≥к тип≥в розм≥р≥в м'€ч≥в
    	BALL_SIZE0_IMAGE, BALL_SIZE1_IMAGE, BALL_SIZE2_IMAGE, BALL_SIZE3_IMAGE, BALL_SIZE4_IMAGE, BALL_SIZE5_IMAGE
    }
    
    public static final String[] BALLS_IMAGES = new String[] {						// назви зображень м'€ч≥в р≥зних розм≥р≥в
    	"ball/ball0.png",
        "ball/ball1.png",
        "ball/ball2.png",
        "ball/ball3.png",
        "ball/ball4.png",
        "ball/ball5.png"
    };
    
    public enum BoardsImages {														// перел≥к р≥зних частин дошки
    	BOARD_LEFT_IMAGE, BOARD_CENTER_IMAGE, BOARD_RIGHT_IMAGE,
    }
    
    public static final String[] BOARDS_IMAGES = new String[] {						// назви зображень р≥зних частин дошки
    	"board/left.png",
        "board/center.png",
        "board/right.png"
    };
    
	public enum BrickTypes {														// перел≥к цеглин р≥зних тип≥в (кольор≥в)
		BLUE_BRICK, BROKEN1_BRICK, BROKEN2_BRICK, BROWN_BRICK, CYAN_BRICK, GREEN_BRICK, GREY_BRICK, MAGENTA_BRICK,
		ORANGE_BRICK, RED_BRICK, VIOLET_BRICK, WHITE_BRICK, YELLOW_BRICK
	}
	
	public static final String[] BRICKS_IMAGES = new String[] {						// назви зображень цеглин р≥зних тип≥в (кольор≥в)
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
	
	public enum BonusTypes { 														// перел≥к тип≥в бонус≥в
		REDUCE_BALL_SPEED, GROW_BALL_SPEED,
		REDUCE_BOARD_SIZE, GROW_BOARD_SIZE,
		REDUCE_BALL_SIZE, GROW_BALL_SIZE,
		CATCH_BALL,
		FIRE_BALL,
		EXTRA_LIFE
	}
	
    public static final String[] BONUSES_IMAGES = new String[] {					// назви зображень бонус≥в
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
    
    public enum StartWindowImages {													// перел≥к зображень дл€ стартового в≥кна
    	BRICK_IMAGE, BREAKER_IMAGE, FIREBALL_IMAGE, PRESSANYKEY_IMAGE
    }
    
    public static final String[] START_WINDOW_IMAGES = new String [] {				// назви зображень дл€ стартового в≥кна
    	"startwindow/break.png",
        "startwindow/out.png",
        "startwindow/RR.png",
        "startwindow/pressanykey.png"
    };
	
	public enum OtherImages {														// перел≥к ≥нших зображень
    	BACKGROUND_IMAGE, LOGO_IMAGE, READY_IMAGE, GAMEOVER_IMAGE, VLINE_IMAGE
    }
    
    public static final String[] OTHER_IMAGES = new String[] {						// назви ≥нших зображень
        "other/background.jpg",
        "other/logo.png",
        "other/ready.png",
        "other/gameover.png",
        "other/vline.png"
    };

    public static void initializeAllImagesLists() {									// ≥н≥ц≥ал≥зац≥€ списк≥в вс≥х зображень
    	
    	for (String imageName : BALLS_IMAGES) { 									// дл€ встановленн€ зображень на м'€ч≥
            final String path = "breakout/" + imageName;							// отримуЇмо зображенн€ м'€ча, розм≥щене по вказаному шл€ху
            Image image = new Image(path);											// створюЇмо його
            
            ballsImages.add(image);													// та додаЇмо зображенн€ м'€ча в список м'€ч≥в
        }
    	
    	for (String imageName : BOARDS_IMAGES) { 									// дл€ встановленн€ зображень на дошку
            final String path = "breakout/" + imageName;							// отримуЇмо зображенн€ дошки, розм≥щене по вказаному шл€ху
            Image image = new Image(path);											// створюЇмо його
            
            boardsImages.add(image);												// та додаЇмо зображенн€ дошки в список зображень дошки
        }
    	
        for (String imageName : BRICKS_IMAGES) { 									// дл€ встановленн€ зображень на цеглини
            final String path = "breakout/" + imageName;							// отримуЇмо зображенн€ цеглини, розм≥щене по вказаному шл€ху
            Image image = new Image(path);											// створюЇмо його
            
            bricksImages.add(image);												// та додаЇмо зображенн€ цеглини в список цеглин
        }
        
        for (String imageName : BONUSES_IMAGES) {									// дл€ встановленн€ зображень на бонуси
            final String path = "breakout/" + imageName;							// отримуЇмо зображенн€ бонусу, розм≥щене по вказаному шл€ху
            Image image = new Image(path);											// створюЇмо його
            
            bonusesImages.add(image);												// та додаЇмо зображенн€ бонусу в список бонус≥в
        }
        
        for (String imageName : START_WINDOW_IMAGES) {								// дл€ встановленн€ зображень стартового в≥кна
            final String path = "breakout/" + imageName;							// отримуЇмо зображенн€ дл€ стартового в≥кна, розм≥щене по вказаному шл€ху
            Image image = new Image(path);											// створюЇмо його
            
            startWindowImages.add(image);											// та додаЇмо це зображенн€ в список зображень дл€ стартового в≥кна
        }
        
        for (String imageName : OTHER_IMAGES) {										// дл€ встановленн€ ≥нших зображень
            final String path = "breakout/" + imageName;							// отримуЇмо зображенн€, розм≥щене по вказаному шл€ху
            Image image = new Image(path);											// створюЇмо його
            
            otherImages.add(image);													// та додаЇмо зображенн€ в список ≥нших зображень
        }
    }
    
    public static ObservableList<Image> getBallsImages() {							// поверненн€ списку зображень м'€ч≥в
    	
        return ballsImages;
    }

    public static ObservableList<Image> getBoardsImages() {							// поверненн€ списку зображень частин дошки
    	
        return boardsImages;
    }

    public static ObservableList<Image> getBricksImages() {							// поверненн€ списку зображень цеглин
    	
        return bricksImages;
    }

    public static ObservableList<Image> getBonusesImages() {						// поверненн€ списку зображень бонус≥в
    	
        return bonusesImages;
    }
    
    public static ObservableList<Image> getStartWindowImages() {					// поверненн€ списку зображень дл€ стартового в≥кна
    	
        return startWindowImages;
    }
    
    public static ObservableList<Image> getOtherImages() {							// поверненн€ списку ≥нших зображень
    	
        return otherImages;
    }
}