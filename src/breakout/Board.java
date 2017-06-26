package breakout;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Board extends Parent {

    public static final int DEFAULT_SIZE = 2;									// ����� ����� �� �������������
    public static final int MAX_SIZE = 7;										// ������������ ����� �����

    public static final Image LEFT = GameSettings.getBoardsImages().get(GameSettings.BoardsImages.BOARD_LEFT_IMAGE.ordinal());		// ������� ��� ������� ����� (������������)
    public static final Image CENTER = GameSettings.getBoardsImages().get(GameSettings.BoardsImages.BOARD_CENTER_IMAGE.ordinal());	// ������� ������� ������� �����
    public static final Image RIGHT = GameSettings.getBoardsImages().get(GameSettings.BoardsImages.BOARD_RIGHT_IMAGE.ordinal());	// ������� ����� ������� ����� (������������)

    private int boardSize;														// ����� �����
    private int boardWidth;														// ������ �����
    private int boardHeight;													// ������ ����� �� ���� ������

    private ImageView leftBoardImageView;										// ����������� ������� ��� ������� �����
    private ImageView centerBoardImageView;										// ����������� ������� ������� ������� �����
    private ImageView rightBoardImageView;										// ����������� ������� ����� ������� �����
    
    
    
    public Board () {															// ��������� ����������� ��������� �����
    	
        boardHeight = (int)CENTER.getHeight() - GameSettings.SHADOW_HEIGHT;	// ������ ����� �� ���� ������
        
        Group group = new Group();
        
        leftBoardImageView = new ImageView();									// ��������� ����������� ������� ��� ��� ������� �����
        leftBoardImageView.setImage(LEFT);										// ����������� ������� ��� ������� �����
        
        centerBoardImageView = new ImageView();									// ��������� ����������� ������� ��� ������� ������� �����
        centerBoardImageView.setImage(CENTER);									// ����������� ������� ��� ������� �����
        centerBoardImageView.setTranslateX(LEFT.getWidth());					// ������� ���������� ������� ����� �� �� � ������� ������ ������� ��� ������� ����� 
        
        rightBoardImageView = new ImageView();									// ��������� ����������� ������� ��� ����� ������� �����
        rightBoardImageView.setImage(RIGHT); 									// ����������� ������� ����� ������� �����
        
        group.getChildren().addAll(leftBoardImageView, centerBoardImageView, rightBoardImageView);	// ��'������� ��� ������ ����� � �����
        getChildren().add(group);												// ��������� ����� �� ������
        
        changeBoardSize(DEFAULT_SIZE);											// ������������ ������ ����� �� �������������
    }

    public int getBoardSize () {												// ��������� ������ �����
    	
        return boardSize;
    }

    public int getBoardWidth () {												// ��������� ������ �����
    	
        return boardWidth;
    }

    public int getBoardHeight () {												// ��������� ������ ����� �� ���� ������
    	
        return boardHeight;
    }

    public void changeBoardSize (int newSize) {									// ���� ������ �����
    	
        this.boardSize = newSize;												// ���������� ������ ������ �����
        boardWidth = boardSize * 12 + 45;										// ���������� ���� ������ �����
        
        double rightWidth = RIGHT.getWidth() - GameSettings.SHADOW_WIDTH;		// ������ ����� ������� �����
        double centerWidth = boardWidth - LEFT.getWidth() - rightWidth;			// ������ ���������� (�������) ������� �����
        
        centerBoardImageView.setViewport(new Rectangle2D(
            (CENTER.getWidth() - centerWidth) / 2, 0, centerWidth, CENTER.getHeight())); // ������������ ����� ����������
        rightBoardImageView.setTranslateX(boardWidth - rightWidth);				// ������������ ������� ����� ������� ����� (������������)
    }
}