package breakout;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Board extends Parent {

    public static final int DEFAULT_SIZE = 2;									// розмір дошки за замовчуванням
    public static final int MAX_SIZE = 7;										// максимальний розмір дошки

    public static final Image LEFT = GameSettings.getBoardsImages().get(GameSettings.BoardsImages.BOARD_LEFT_IMAGE.ordinal());		// малюнок лівої частини дошки (заокруглення)
    public static final Image CENTER = GameSettings.getBoardsImages().get(GameSettings.BoardsImages.BOARD_CENTER_IMAGE.ordinal());	// малюнок основної частини дошки
    public static final Image RIGHT = GameSettings.getBoardsImages().get(GameSettings.BoardsImages.BOARD_RIGHT_IMAGE.ordinal());	// малюнок правої частини дошки (заокруглення)

    private int boardSize;														// розмір дошки
    private int boardWidth;														// ширина дошки
    private int boardHeight;													// висота дошки від низу екрану

    private ImageView leftBoardImageView;										// відображення малюнку лівої частини дошки
    private ImageView centerBoardImageView;										// відображення малюнку основної частини дошки
    private ImageView rightBoardImageView;										// відображення малюнку правої частини дошки
    
    
    
    public Board () {															// початкова ініціалізація параметрів дошки
    	
        boardHeight = (int)CENTER.getHeight() - GameSettings.SHADOW_HEIGHT;	// висота дошки від низу екрану
        
        Group group = new Group();
        
        leftBoardImageView = new ImageView();									// створення відображення малюнку для лівої частини дошки
        leftBoardImageView.setImage(LEFT);										// відображення малюнку лівої частини дошки
        
        centerBoardImageView = new ImageView();									// створення відображення малюнку для основної частини дошки
        centerBoardImageView.setImage(CENTER);									// відображення малюнку лівої частини дошки
        centerBoardImageView.setTranslateX(LEFT.getWidth());					// зміщення центральної частини дошки по осі Х відносно ширини малюнку лівої частини дошки 
        
        rightBoardImageView = new ImageView();									// створення відображення малюнку для правої частини дошки
        rightBoardImageView.setImage(RIGHT); 									// відображення малюнку правої частини дошки
        
        group.getChildren().addAll(leftBoardImageView, centerBoardImageView, rightBoardImageView);	// об'єднання всіх частин дошки в єдину
        getChildren().add(group);												// додавання дошки на панель
        
        changeBoardSize(DEFAULT_SIZE);											// встановлення розміру дошки за замовчуванням
    }

    public int getBoardSize () {												// отримання розміру дошки
    	
        return boardSize;
    }

    public int getBoardWidth () {												// отримання ширини дошки
    	
        return boardWidth;
    }

    public int getBoardHeight () {												// отримання висоти дошки від низу екрану
    	
        return boardHeight;
    }

    public void changeBoardSize (int newSize) {									// зміна розміру дошки
    	
        this.boardSize = newSize;												// визначення нового розміру дошки
        boardWidth = boardSize * 12 + 45;										// визначення нової ширини дошки
        
        double rightWidth = RIGHT.getWidth() - GameSettings.SHADOW_WIDTH;		// ширина правої частини дошки
        double centerWidth = boardWidth - LEFT.getWidth() - rightWidth;			// ширина центральної (основної) частини дошки
        
        centerBoardImageView.setViewport(new Rectangle2D(
            (CENTER.getWidth() - centerWidth) / 2, 0, centerWidth, CENTER.getHeight())); // встановлення маски зображення
        rightBoardImageView.setTranslateX(boardWidth - rightWidth);				// встановлення зміщення правої частини дошки (заокруглення)
    }
}