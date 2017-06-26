package breakout;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import breakout.Main.MainFrame;

public class Level extends Parent {

    private static final double MOB_SCALING = 1.5f;
    private static final MainFrame mainFrame = Main.getMainFrame();	// ������� (�������) ����

    private int brickCount;											// ������� ������
    private int catchedBonus;										// ��� ���������� ������
    
    private ArrayList<Brick> bricks;								// ������ ������
    private ArrayList<Brick> fadeBricks;							// ������ �������� ������
    private ArrayList<Bonus> bonuses;								// ������ ������
    private ArrayList<Bonus> lives;									// ������ �����

    private Group group;

    private enum States {											// ������ �������� ����� ���
    	STARTING_LEVEL, BALL_CATCHED, PLAYING, GAME_OVER
    }

    private int state;												// ���� ���
    private int boardDirection;										// ������ ���� �����
    
    private double ballDirX, ballDirY;								// ������ ���� �'��� �� ���� X �� Y
    
    private int levelNumber;										// ����� ����
    
    private Board board;											// ��'��� �����
    private Ball ball;												// ��'��� �'���
    
    private Text roundText;											// ������ "в����:"
    private Text round;												// ������, �� �������� ����� ����
    private Text scoreText;											// ������ "�������:"
    private Text score;												// ������, �� �������� ������� ������
    private Text livesText;											// ������ "�����:"
    
    private ImageView message;										// ����������� ���������� ����������� (READY, GAME OVER)
    
    private Timeline startingTimeline;
    private Timeline timeline;
    
    private Group informationPanel;									// ������������ ������

    public Level(int levelNumber) {
    	
        group = new Group();
        getChildren().add(group);
        initializeContent(levelNumber);								// ����������� ��������
    }
    
    private void initializeContent(int level) {						// ����������� ��������
    	
        state = States.STARTING_LEVEL.ordinal();					// ������� � ���� ������� ����
        levelNumber = level;										// ������������ ������ ����
        
        lives = new ArrayList <Bonus>();							// ������ �����
        bricks = new ArrayList <Brick>();							// ������ ������
        fadeBricks = new ArrayList <Brick>();						// ������ �������� ������
        bonuses = new ArrayList <Bonus>();							// ������ ������
        
        ball = new Ball(); 											// ��������� ��'���� �'���
        ball.setVisible(false);										// ��������� �'�� ���������
        
        board = new Board();										// ��������� ��'���� �����
        board.setTranslateY(GameSettings.BOARD_Y);
        board.setVisible(false);									// ��������� ����� ��������
        
        message = new ImageView();									// ��� ����������� ���������� "READY"
        message.setImage(GameSettings.getOtherImages().get(GameSettings.OtherImages.READY_IMAGE.ordinal()));	// ������������ ���������� "READY" ��� ���� �����������
        message.setTranslateX((GameSettings.FIELD_WIDTH - message.getImage().getWidth()) / 2);	// ���� ����������� �� �� X ������� ������ ���� �� ������ ����������
        message.setTranslateY(GameSettings.FIELD_Y +
            (GameSettings.FIELD_HEIGHT - message.getImage().getHeight()) / 2);					// ���� ����������� �� �� Y ������� ������ ���� �� ������ ����������
        message.setVisible(false);									// ��������� �� ����������� ��������
        
        initializeLevelBricks();									// ����������� ������ �� ���
        initializeStartingTimeline();								// ����������� �������� ������� �������� ����
        initializeTimeline();
        initializeInformationPanel();								// ����������� ������������ �����
        
        ImageView backgroundImage = new ImageView();				// ����������� ���������� ������ �����
        backgroundImage.setFocusTraversable(true);					// ��������� ������ �� �� ����������
        backgroundImage.setImage(GameSettings.getOtherImages().get(GameSettings.OtherImages.BACKGROUND_IMAGE.ordinal()));	// ������������ ���������� ������ �����
        backgroundImage.setFitWidth(GameSettings.SCREEN_WIDTH);	// ������������ ������ ������ ������ ����� ������� ������ ������ ������
        backgroundImage.setFitHeight(GameSettings.SCREEN_HEIGHT);	// ������������ ������ ������ ������ ����� ������� ������ ������ ������
        
        backgroundImage.setOnMouseMoved(event -> {					// �������� ��䳿 ��� ��� (���������) �����
            moveBoard(event.getX() - board.getBoardWidth() / 2);
        });
        
        backgroundImage.setOnMouseDragged(event -> {				// �������� ��䳿 ��� ��������� ���� ����� ��� ��������� ��������� �� ������ ����
            moveBoard(event.getX() - board.getBoardWidth() / 2);
        });
        
        backgroundImage.setOnMousePressed(event -> {				// �������� ���� ��� ��������� �� ������ �����
            
            if (state == States.BALL_CATCHED.ordinal()) {			// ���� �'�� ����������� �� �����
                state = States.PLAYING.ordinal();					// ���� ������� �� ������ ���� �� ����� ���
            }
            
            if (state == States.GAME_OVER.ordinal()) {				// ���� ���������� ������� �� ����� ��� ������� �� ���
                mainFrame.changeExecutionState(MainFrame.START_WINDOW);	// ����������� �� �������� ����
            }
        });
        
        backgroundImage.setOnKeyPressed(event -> {					// �������� ���� ��� ������� �� ���� ������ ���������
                
        	if (event.getCode() == KeyCode.ESCAPE) { 				// ���� ���������� �������� ������ ESCAPE (Esc) 
                Platform.exit();									// ������ ���� �����������
            }
        	
        	// ���� �'�� ����������� �� ����� � ��������� ������ SPACE (�����) ��� ENTER
            if (state == States.BALL_CATCHED.ordinal() && (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.ENTER)) {
                state = States.PLAYING.ordinal();					// ��� ������������ (�'�� ������ �� ����� � ������ ���)
            }
            
            if (state == States.GAME_OVER.ordinal()) {				// ���� ���������� ������� �� ����� ��� ������� �� ���
                mainFrame.changeExecutionState(MainFrame.START_WINDOW);	// ����������� �� �������� ����
            }
            
            // ���� ��� ��� (�'�� �� ����� ������ ��������) � ��������� ������ Q
            if (state == States.PLAYING.ordinal() && event.getCode() == KeyCode.Q) {
            	
            	// �� �������� �����, � ����� �� �'�� ������������ �� ����� �� ������������� ����
                lostLife();
                return;
            }
            
            if ((event.getCode() == KeyCode.LEFT)) {				// ���� ��������� ������ ����	
            	boardDirection = - GameSettings.BOARD_SPEED;		// ��������� ����� ���� � ������� �������� ���� �����
            }
            
            if ((event.getCode() == KeyCode.RIGHT)) {				// ���� ��������� ������ ������
            	boardDirection = GameSettings.BOARD_SPEED;			// ��������� ����� ������ � ������� �������� ���� �����
            }

        });
        
        backgroundImage.setOnKeyReleased(event -> {					// �������� ���� ��� ��������� ������ ����� ��������� (������ ���� �� ������)
            	
        	// ���� ����� ��� ���� ��������� ������ ���� ��� ������ � �� �� ���������
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
            	boardDirection = 0;									// ��� ����� ������������
            }
        });
        
        group.getChildren().add(backgroundImage);					// ������ �������� �� ������
        
        for (int row = 0; row < bricks.size() / GameSettings.FIELD_BRICK_IN_ROW; row++) { // ������������ ������ �� ������ �����
            for (int col = 0; col < GameSettings.FIELD_BRICK_IN_ROW; col++) {
                Brick b = getBrick(row, col);						// ��������� ���� �������
                
                if (b != null) {									// ���� ������� � (�����, � ���� LevelData ���� �� ������ ���, � �� ���������)
                    group.getChildren().add(b);						// ������ �� �� ������ ������
                }
            }
        }

        group.getChildren().addAll(message, ball, board, informationPanel);	// ������ �� ������ ����������� "READY", �'��, ����� �� ������������ ������
    }

    private void initializeLevelBricks() {								// ����������� ������ �� ���
    	
        String[] level = LevelData.getLevelData(levelNumber);			// ��������� ���������� ��� ����� ���� �� ���� ������
        
        for (int row = 0; row < level.length; row++) {					// ����������� �� ������ ����
            for (int col = 0; col < GameSettings.FIELD_BRICK_IN_ROW; col++) {	// �� �� ������ ������� ����� (���� ��������) 
                String rowString = level[row];							// �������� �� ���� ����� ��� ����
                Brick brick = null;										// ��������� �����, �� ���� ������ ��������� ������
                
                if (rowString != null && col < rowString.length()) {	// ���� ����� �������� �� �� �������� ���� ����
                    String type = rowString.substring(col, col + 1);	// ��������� ������, �� ���� ��������� ���� (�������) �������
                    
                    if (!type.equals(" ")) {							// ���� �� �� ����� (������� �� �������)
                        brick = new Brick(Brick.getBrickType(type));	// ��������� �� ���
                        brick.setTranslateX(col * GameSettings.BRICK_WIDTH);							// ������ �� �� �� X
                        brick.setTranslateY(GameSettings.FIELD_Y + row * GameSettings.BRICK_HEIGHT); 	// �� �� �� Y
                        
                        if (brick.getType() != GameSettings.BrickTypes.GREY_BRICK.ordinal()) {			// ���� ������� �� ���'���
                            brickCount++;								// �������� ������� "����������" ������
                        }
                    }
                }
                
                bricks.add(brick);										// ������ ������� � ������ ������
            }
        }
    }
    
    private void initializeStartingTimeline() {							// ����������� �������� ������� �������� ����
    	
        startingTimeline = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.millis(500), event -> {	// ��� ���������� ������� ��� (������� "READY")
        	
            message.setVisible(true);									// ������������ ����������� �������
            state = States.STARTING_LEVEL.ordinal();					// ������������ ����� ��� ����� ������� (�����������) ����
            board.setVisible(false);									// ��������� ����� ��������
            ball.setVisible(false);										// ��������� �'�� ���������
        }, new KeyValue(message.opacityProperty(), 0));
        
        KeyFrame kf2 = new KeyFrame(Duration.millis(1500), new KeyValue(message.opacityProperty(), 1)); // ��� ����������� ������� "READY"
        KeyFrame kf3 = new KeyFrame(Duration.millis(3000), new KeyValue(message.opacityProperty(), 1));	// ��� �������� ������� "READY"
        
        KeyFrame kf4 = new KeyFrame(Duration.millis(2000), event -> {	// ���, ���� ����� ������ ������� �� ���������� ���
            
            message.setVisible(false);									// ������������ ����������� ���������

            board.setTranslateX((GameSettings.FIELD_WIDTH - board.getBoardWidth()) / 2.0);	// ��������� ����� �� ����� �� �� X
            
            ball.setTranslateX((GameSettings.FIELD_WIDTH - ball.getBallDiameter()) / 2.0);	// ��������� �'��� �� ����� �� �� X
            ball.setTranslateY(GameSettings.BOARD_Y - ball.getBallDiameter());				// ��������� �'��� �� ����� �� �� Y
            
            ballDirX = (Utils.random(2) * 2 - 1) * GameSettings.BALL_MIN_COORD_SPEED;
            ballDirY = -GameSettings.BALL_MIN_SPEED;

            board.setVisible(true);										// ������������ ����� �������
            ball.setVisible(true);										// ������������ �'��� �������
            state = States.BALL_CATCHED.ordinal();						// �'�� ����������� �� �����
        }, new KeyValue(message.opacityProperty(), 0));

        startingTimeline.getKeyFrames().addAll(kf1, kf2, kf3, kf4); 	// ��������� ������ ����� ��� ����������� �������� �������
    }

    private void initializeTimeline() {									// ������������ �������� ���
    	
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);					// ���������� ������� ������� �������
        
        KeyFrame kf = new KeyFrame(GameSettings.ANIMATION_TIME, event -> {
        	
        	// ����������� �������� ������
            Iterator<Brick> brickIterator = fadeBricks.iterator();
            
            while (brickIterator.hasNext()) {
                Brick brick = brickIterator.next();
                brick.setOpacity(brick.getOpacity() - 0.1);
                
                if (brick.getOpacity() <= 0) {
                    brick.setVisible(false);							// ������� �����
                    brickIterator.remove();								// �� ����������� � ������ ������
                }
            }
            
            // ���������� ����� (���� ���������)
            if (boardDirection != 0 && state != States.STARTING_LEVEL.ordinal()) {
                moveBoard(board.getTranslateX() + boardDirection);
            }
            
            // ����������� ������
            Iterator<Bonus> bonusIterator = bonuses.iterator();
            
            while (bonusIterator.hasNext()) {
                Bonus bonus = bonusIterator.next();
                
                if (bonus.getTranslateY() > GameSettings.SCREEN_HEIGHT) {	// ���� �� ������� ������� �����
                    bonus.setVisible(false);								// �� ��� ���������
                    bonusIterator.remove();									// �� ����������� � ������ ������
                    group.getChildren().remove(bonus);						// �� � �����
                } 
                
                else {
                    bonus.setTranslateY(bonus.getTranslateY() + GameSettings.BONUS_SPEED); // �������� ������ ������
                    
                    if (bonus.getTranslateX() + bonus.getBonusWidth() > board.getTranslateX() &&
                            bonus.getTranslateX() < board.getTranslateX() + board.getBoardWidth() &&
                            bonus.getTranslateY() + bonus.getBonusHeight() > board.getTranslateY() &&
                            bonus.getTranslateY() < board.getTranslateY() + board.getBoardHeight()) {	// ���� ������� �����
                    	
                        updateScore(100);							// ������ 100 ���� �� �������
                        catchedBonus = bonus.getBonusType();		// ��������� ��� ������
                        bonus.setVisible(false);					// ������ ���� ���������
                        bonusIterator.remove();						// ��������� ���� � ������ ������
                        group.getChildren().remove(bonus);			// �� � �����
                        
                        if (bonus.getBonusType() == GameSettings.BonusTypes.REDUCE_BALL_SPEED.ordinal()) { 	// ���� ����� - �� ����������� ���� �'���
                            ballDirX /= 1.5;						// �������� �������� ���� �'��� �� �� X � 1.5 ����
                            ballDirY /= 1.5;						// �������� �������� ���� �'��� �� �� Y � 1.5 ����
                            correctBallSpeed();						// ��������� (��������) �������� �'���
                        } 
                        
                        else if (bonus.getBonusType() == GameSettings.BonusTypes.GROW_BALL_SPEED.ordinal()) { 	// ���� ����� - �� ������������ ���� �'���
                            ballDirX *= 1.5;						// �������� �������� ���� �'��� �� �� X � 1.5 ����
                            ballDirY *= 1.5;						// �������� �������� ���� �'��� �� �� X � 1.5 ����
                            correctBallSpeed();						// ��������� (�������� �������� �'���)
                        } 
                        
                        else if (bonus.getBonusType() == GameSettings.BonusTypes.REDUCE_BOARD_SIZE.ordinal()) {// ���� ����� - �� ��������� ������ �����
                        	
                            if (board.getBoardSize() > 0) {							// ���� ����� �� ����� ��������
                                int oldWidth = board.getBoardWidth();				// ��������� �� ��������� ������
                                board.changeBoardSize(board.getBoardSize() - 1);	// �������� �� �����
                                board.setTranslateX(board.getTranslateX() + ((oldWidth - board.getBoardWidth()) / 2)); // �� ������� �� ��������� �� �� X
                            }
                        } 
                        
                        else if (bonus.getBonusType() == GameSettings.BonusTypes.GROW_BOARD_SIZE.ordinal()) { 	// ���� ����� - �� ��������� ������ �����
                        	
                            if (board.getBoardSize() < Board.MAX_SIZE) {			// ���� �� ��������� ������������� ������ �����
                            	board.changeBoardSize(board.getBoardSize() + 1);	// �������� �� �����
                                
                                if (board.getTranslateX() + board.getBoardWidth() > GameSettings.FIELD_WIDTH) {// ���� ���� ��������� ����� ���� �������� �� ��� ������
                                	board.setTranslateX(GameSettings.FIELD_WIDTH - board.getBoardWidth());		// ��������� ��
                                }
                            }
                        } 
                        
                        else if (bonus.getBonusType() == GameSettings.BonusTypes.REDUCE_BALL_SIZE.ordinal()) { // ���� ����� - �� ��������� ������ �'���
                        	
                            if (ball.getBallSize() > 0) {						// ���� �'�� �� ����� ��������
                                ball.changeBallSize(ball.getBallSize() - 1);	// �������� ���� �����
                                
                                if (state == States.BALL_CATCHED.ordinal()) {
                                    ball.setTranslateY(GameSettings.BOARD_Y - ball.getBallDiameter());
                                }
                            }
                        } 
                         
                        else if (bonus.getBonusType() == GameSettings.BonusTypes.GROW_BALL_SIZE.ordinal()) { 	// ���� ����� - �� ��������� ������ �'���
                        	
                            if (ball.getBallSize() < Ball.MAX_BALL_SIZE) {		// ���� �� ��������� ������������� ������ �'���
                                ball.changeBallSize(ball.getBallSize() + 1);	// �������� ���� �����
                                
                                if (state == States.BALL_CATCHED.ordinal()) {
                                    ball.setTranslateY(GameSettings.BOARD_Y - ball.getBallDiameter());
                                }
                            }
                        } 
                        
                        else if (bonus.getBonusType() == GameSettings.BonusTypes.EXTRA_LIFE.ordinal()) { 		// ���� ����� - �� ��������� �����
                            mainFrame.increaseLivesCount();				// ��������� ������� ���� � ���
                            updateLives();								// �� �� ������ �����
                        }
                    }
                }
            }
            
            if (state != States.PLAYING.ordinal()) {
                return;
            }
            
            double newX = ball.getTranslateX() + ballDirX;	// ��������� ��� �'��� �� �� X
            double newY = ball.getTranslateY() + ballDirY;	// ��������� ��� �'��� �� �� Y
            
            boolean inverseX = false;						// ������� ���� �'��� �� �� X
            boolean inverseY = false;						// ������� ���� �'��� �� �� Y
            
            if (newX < 0) {									// ���� ��������� � ����
                newX = -newX;								// ��������� ��� �'��� �� �� X
                inverseX = true;
            }
            
            int BALL_MAX_X = GameSettings.FIELD_WIDTH - ball.getBallDiameter();
            
            if (newX > BALL_MAX_X) {
                newX = BALL_MAX_X - (newX - BALL_MAX_X);
                inverseX = true;
            }
            
            if (newY < GameSettings.FIELD_Y) {
                newY = 2 * GameSettings.FIELD_Y - newY;
                inverseY = true;
            }
            
            // ��������� ��������� �'��� �� �����
            if (ballDirY > 0 &&
                    ball.getTranslateY() + ball.getBallDiameter() < GameSettings.BOARD_Y &&
                    newY + ball.getBallDiameter() >= GameSettings.BOARD_Y &&
                    newX >= board.getTranslateX() - ball.getBallDiameter() &&
                    newX < board.getTranslateX() + board.getBoardWidth() + ball.getBallDiameter()) {
                inverseY = true;

                // �������� �������� �'���
                double speed = Math.sqrt(ballDirX * ballDirX + ballDirY * ballDirY);
                ballDirX *= (speed + GameSettings.BALL_SPEED_INC) / speed;
                ballDirY *= (speed + GameSettings.BALL_SPEED_INC) / speed;

                // ��������� ��� �'��� �� ���� X �� Y
                double offsetX = newX + ball.getBallDiameter() / 2 - board.getTranslateX() - board.getBoardWidth() / 2;
                
                if (Math.abs(offsetX) > board.getBoardWidth() / 4) {
                    ballDirX += offsetX / 5;
                    double MAX_COORD_SPEED = Math.sqrt(speed * speed -
                        GameSettings.BALL_MIN_COORD_SPEED * GameSettings.BALL_MIN_COORD_SPEED);
                    
                    if (Math.abs(ballDirX) > MAX_COORD_SPEED) {
                        ballDirX = Utils.sign(ballDirX) * MAX_COORD_SPEED;
                    }
                    
                    ballDirY = Utils.sign(ballDirY) *
                        Math.sqrt(speed * speed - ballDirX * ballDirX);
                }
                
                correctBallSpeed();		// ��������� �������� �'���
                
                if (catchedBonus == GameSettings.BonusTypes.CATCH_BALL.ordinal()) { // ���� ����� - �� ���������� �'��� �� �����
                    newY = GameSettings.BOARD_Y - ball.getBallDiameter();
                    state = States.BALL_CATCHED.ordinal();	// �'�� ������� �� ����� � �� ��������
                }
            }

            // ��������� ��������� �'��� � �������
            int firstCol = (int)(newX / GameSettings.BRICK_WIDTH);
            int secondCol = (int)((newX + ball.getBallDiameter()) / GameSettings.BRICK_WIDTH);
            int firstRow = (int)((newY - GameSettings.FIELD_Y) / GameSettings.BRICK_HEIGHT);
            int secondRow = (int)((newY - GameSettings.FIELD_Y + ball.getBallDiameter()) / GameSettings.BRICK_HEIGHT);
            
            if (ballDirX > 0) {
                int temp = secondCol;
                secondCol = firstCol;
                firstCol = temp;
            }
            
            if (ballDirY > 0) {
                int temp = secondRow;
                secondRow = firstRow;
                firstRow = temp;
            }
            
            Brick vertBrick = getBrick(firstRow, secondCol);
            Brick horBrick = getBrick(secondRow, firstCol);
            
            if (vertBrick != null) {
                kickBrick(firstRow, secondCol);
                
                if (catchedBonus != GameSettings.BonusTypes.FIRE_BALL.ordinal()) {
                    inverseY = true;
                }
                
            }
            
            if (horBrick != null && (firstCol != secondCol || firstRow != secondRow)) {
                kickBrick(secondRow, firstCol);
                
                if (catchedBonus != GameSettings.BonusTypes.FIRE_BALL.ordinal()) {
                    inverseX = true;
                } 
            }
            
            if (firstCol != secondCol || firstRow != secondRow) {
                Brick diagBrick = getBrick(firstRow, firstCol);
                
                if (diagBrick != null && diagBrick != vertBrick && diagBrick != horBrick) {
                    kickBrick(firstRow, firstCol);
                    
                    if (vertBrick == null && horBrick == null &&
                            catchedBonus != GameSettings.BonusTypes.FIRE_BALL.ordinal()) { // �����������
                        inverseX = true;
                        inverseY = true;
                    }
                }
            }
            
            ball.setTranslateX(newX);
            ball.setTranslateY(newY);
            
            if (inverseX) {			// ��������� ��� �'��� �� �� X
                ballDirX = - ballDirX;
            }
            
            if (inverseY) {			// ��������� ��� �'��� �� �� Y
                ballDirY = - ballDirY;
            }
            
            if (ball.getTranslateY() > GameSettings.SCREEN_HEIGHT) { // ���� �� ������� ������� �'��� � �� ��������� ������
                lostLife(); 		// ���������� ������ �����
            }
        });
        
        timeline.getKeyFrames().add(kf);	// ��������� �������� ����� � ������ �������
    }
    
    private void initializeInformationPanel() {						// ����������� ����� � ����������� (�����, �������� �������, ������� �����, ������� �� ������)
    	
    	informationPanel = new Group();								// ��������� ������������ �����
        Font f = new Font("Impact", 18);							// ����� ��� �������
        
        roundText = new Text();										// ������ "в����:"
        roundText.setTranslateX(30);								// ������� ������� �� �� X
        roundText.setTranslateY(128);								// ������� ������� �� �� Y
        roundText.setText("в����:");								// ������������ ������ �������
        roundText.setFill(Color.AZURE);								// ���� ������ �������
        roundText.setTextOrigin(VPos.TOP);							// ����������� ������ �� ��������
        roundText.setFont(f);										// ������������ ������ �������		
        
        round = new Text();											// ������, �� ������� ���� �����������
        round.setTranslateX(roundText.getTranslateX() + roundText.getBoundsInLocal().getWidth() + GameSettings.INFO_TEXT_SPACE + 12); // ��������� ���� ��������� �� �� X
        round.setTranslateY(roundText.getTranslateY());				// �� �� �� Y
        round.setFill(Color.rgb(0, 204, 102));						// ������������ ���� ������
        round.setTextOrigin(VPos.TOP);								// ����������� �� ��������
        round.setFont(f);											// ������������ �����
        round.setText(levelNumber + "");							// �� �����
        
        scoreText = new Text();										// ������ "�������:"
        scoreText.setTranslateX(30);								// ������� ������� �� �� X
        scoreText.setTranslateY(164);								// ������� ������� �� �� Y
        scoreText.setText("�������:");								// ������������ ������ �������
        scoreText.setFill(Color.AZURE);								// ���� ������ �������
        scoreText.setTextOrigin(VPos.TOP);							// ����������� ������ �� ��������
        scoreText.setFont(f);										// ������������ ������ �������	
        
        score = new Text();											// ������, �� ������� ������� �����������
        score.setTranslateX(scoreText.getTranslateX() + scoreText.getBoundsInLocal().getWidth() + GameSettings.INFO_TEXT_SPACE); // ��������� ���� ��������� �� �� X
        score.setTranslateY(scoreText.getTranslateY());				// �� �� �� Y
        score.setFill(Color.rgb(0, 204, 102));						// ������������ ���� ������
        score.setTextOrigin(VPos.TOP);								// ����������� �� ��������
        score.setFont(f);											// ������������ �����
        score.setText("");											// �� �����
        
        livesText = new Text();										// ������ "�����:"
        livesText.setTranslateX(30);								// ������� ������� �� �� X
        livesText.setTranslateY(200);								// ������� ������� �� �� Y
        livesText.setText("�����:");								// ������������ ������ �������
        livesText.setFill(Color.AZURE);								// ���� ������ �������
        livesText.setTextOrigin(VPos.TOP);							// ����������� ������ �� ��������
        livesText.setFont(f);										// ������������ ������ �������
        
        Rectangle black = new Rectangle();							// ����������� - ��� ������� ����� � �����������
        black.setWidth(GameSettings.SCREEN_WIDTH - GameSettings.FIELD_WIDTH);	// ������ ������������ ����� (������ ������ ������ - ������ ������ �����)
        black.setHeight(GameSettings.SCREEN_HEIGHT);				// ������ ������������ �����
        black.setFill(Color.BLACK);									// ���� ������������ �����
        
        ImageView vline = new ImageView();							// "���������" �� ������� �� ������������� ��������
        vline.setImage(GameSettings.getOtherImages().get(GameSettings.OtherImages.VLINE_IMAGE.ordinal()));// ���������� "����������" �� ������� �� ������������� ��������
        
        ImageView logo = new ImageView();							// ���������� ���
        logo.setImage(GameSettings.getOtherImages().get(GameSettings.OtherImages.LOGO_IMAGE.ordinal()));	// ������������ ���������� ���
        logo.setTranslateX(90);										// ������� ���������� �� �� X
        logo.setTranslateY(35);										// ������� ���������� �� �� Y
        
        Text bonuses = new Text();									// ������ "������"
        bonuses.setTranslateX(80);									// ������� ������� �� �� X
        bonuses.setTranslateY(280);									// ������� ������� �� �� Y
        bonuses.setText("������");									// ������������ ������ �������
        bonuses.setFill(Color.RED);									// ���� ������ �������
        bonuses.setTextOrigin(VPos.TOP);							// ����������� ������ �� ��������
        bonuses.setFont(new Font("Impact", 40));					// ������������ ������ �������
        
        informationPanel.getChildren().addAll(black, vline, logo, roundText, round, scoreText, score, livesText, bonuses); // ������ �� �������� �� ������������ ������
        
        for (int i = 0; i < Bonus.BONUS_NAMES.length; i++) {		// ��������� ������ �� ������� ���� ��� �� ������������ ������
            Bonus bonus = new Bonus(i);
            
            Text text = new Text();
            text.setTranslateX(80);									// ������� ����� ������ �� �� X
            text.setTranslateY(350 + i * 40);						// ������� ����� ������ �� �� Y
            text.setText(Bonus.BONUS_NAMES[i]);						// ������������ ������ ����� ������
            text.setFill(Color.DARKORANGE);							// ���� ������ ����� ������
            text.setTextOrigin(VPos.TOP);							// ����������� ������ �� ��������
            text.setFont(new Font("Arial", 12));					// ������������ ������ ������ ����� ������
            
            bonus.setTranslateX(10 + (820 - 750 - bonus.getBonusWidth()) / 2);
            bonus.setTranslateY(text.getTranslateY() - (bonus.getBonusHeight() - text.getBoundsInLocal().getHeight()) / 2);

            informationPanel.getChildren().addAll(bonus, text);		// ��������� ���������� �� ����� ������ �� ������������ ������
        }
        
        informationPanel.setTranslateX(GameSettings.FIELD_WIDTH);	// ��������� ������������ ����� �� ������� ������� (� ����� ������ ����)
    }

    public void start() {	// ������� ���
    	
        startingTimeline.play();
        timeline.play();
        group.getChildren().get(0).requestFocus();
        updateScore(0);		// ������� ����� 0
        updateLives();		// ������������ ������� ����� �� ������������ (4)
    }

    public void stop() {	// ���������� ���
    	
        startingTimeline.stop();
        timeline.stop();
    }

    private Brick getBrick(int row, int col) {					// ��������� ������� �� ��������� �����
    	
        int i = row * GameSettings.FIELD_BRICK_IN_ROW + col;	// ��������� ������� ��� ��������
        
        // ���� �� ��������� ������� ������� �� �������� 
        if (col < 0 || col >= GameSettings.FIELD_BRICK_IN_ROW || row < 0 || i >= bricks.size()) {
        	
            return null;	// ���� ������� �� ����������������
        } 
        
        else {				// ������ ��������� �� ��������� ������, �� ������� ���� �������
        	
            return bricks.get(i);
        }
    }

    private void kickBrick(int row, int col) {									// ���������� �� ��� �������� � �������
    	
        Brick brick = getBrick(row, col);										// ��������� ������� �� ��� �������, � ��� ���������
        
        // ���� ������� ��� ������� ��� � ��� ����� ���� "���������" �'��� (�����) � �� ��������� � ���'��� ������� �� �������, �� "�����������"
        if (brick == null || (catchedBonus != GameSettings.BonusTypes.FIRE_BALL.ordinal() && !brick.ifBrickCanBeKicked())) {
            return;		// ����� �� ����������
        }
        
        updateScore(10);														// ������ ������ 10 ���� �� ��������� � �������
        
        if (brick.getType() != GameSettings.BrickTypes.GREY_BRICK.ordinal()) {	// ���� ��������� �� � ��� (���'���) �������
            brickCount--;														// �������� ������� ������ �� ���
            
            if (brickCount == 0) {												// ���� ����� �� �������
                mainFrame.changeExecutionState(mainFrame.getExecutionState() + 1);	// ���������� ������� �� ����� ����� (���� �� �� �)
            }
        }
        
        bricks.set(row * GameSettings.FIELD_BRICK_IN_ROW + col, null);			// ������� ��������� �� ������� � ������ ������ ����
        fadeBricks.add(brick);													// �� ������ ����� ������� � ������ ������ ������
        
        // �������� ��������� ��������
        if (Utils.random(8) == 0 && bonuses.size() < 5) {						// ���� ��������� ����������� �������� ���� 0 �� �� ������� �����, �� 5 ������				
            Bonus bonus = new Bonus(Utils.random(Bonus.BONUS_NAMES.length));	// ��������� ��'��� ������ (�������� ���������� ��� ������ �� 0 �� 8)
            bonus.setTranslateY(brick.getTranslateY());							// ������ ���� �� �� Y �� ������� ����� �������
            bonus.setTranslateX(brick.getTranslateX() + (GameSettings.BRICK_WIDTH - bonus.getBonusWidth()) / 2); // �� �� �� X
            bonus.setVisible(true);												// ������ ���� �������
            group.getChildren().add(bonus);										// ������ ���� �� ������
            bonuses.add(bonus);													// �� � ����� ������
        }
    }
    
    private void correctBallSpeed() {											// ������������ �������� �'���
    	
        double speed = Math.sqrt(ballDirX * ballDirX + ballDirY * ballDirY);
        
        if (speed > GameSettings.BALL_MAX_SPEED) {
            ballDirX *= GameSettings.BALL_MAX_SPEED / speed;
            ballDirY *= GameSettings.BALL_MAX_SPEED / speed;
            speed = GameSettings.BALL_MAX_SPEED;
        }
        
        if (speed < GameSettings.BALL_MIN_SPEED) {
            ballDirX *= GameSettings.BALL_MIN_SPEED / speed;
            ballDirY *= GameSettings.BALL_MIN_SPEED / speed;
            speed = GameSettings.BALL_MIN_SPEED;
        }
        
        if (Math.abs(ballDirX) < GameSettings.BALL_MIN_COORD_SPEED) {
            ballDirX = Utils.sign(ballDirX) * GameSettings.BALL_MIN_COORD_SPEED;
            ballDirY = Utils.sign(ballDirY) * Math.sqrt(speed * speed - ballDirX * ballDirX);
        } 
        
        else if (Math.abs(ballDirY) < GameSettings.BALL_MIN_COORD_SPEED) {
            ballDirY = Utils.sign(ballDirY) * GameSettings.BALL_MIN_COORD_SPEED;
            ballDirX = Utils.sign(ballDirX) * Math.sqrt(speed * speed - ballDirY * ballDirY);
        }
    }
    
    private void updateScore(int inc) {			// ��������� �������
    	
        mainFrame.setScore(mainFrame.getScore() + inc);
        score.setText(mainFrame.getScore() + "");
    }

    private void moveBoard(double newX) {		// ���������� �����
    	
        double x = newX;
        
        if (x < 0) {
            x = 0;
        }
        
        if (x + board.getBoardWidth() > GameSettings.FIELD_WIDTH) {
            x = GameSettings.FIELD_WIDTH - board.getBoardWidth();
        }
        
        if (state == States.BALL_CATCHED.ordinal()) {
            double ballX = ball.getTranslateX() + x - board.getTranslateX();
            
            if (ballX < 0) {
                ballX = 0;
            }
            
            double BALL_MAX_X = GameSettings.FIELD_WIDTH - ball.getBallDiameter();
            
            if (ballX > BALL_MAX_X) {
                ballX = BALL_MAX_X;
            }
            
            ball.setTranslateX(ballX);
        }
        
        board.setTranslateX(x);
    }

    private void updateLives() {	// ��������� ������� �����
    	
        while (lives.size() > mainFrame.getLifeCount()) {
            Bonus lifeBat = lives.get(lives.size() - 1);
            lives.remove(lifeBat);
            informationPanel.getChildren().remove(lifeBat);
        }
        
        // ��������� ����� (��� ���� �� ����� 8)
        int maxVisibleLifes = 8;																	// ����������� ������� �����
        double scale = 0.8;																			// �������

        for (int life = lives.size(); life < Math.min(mainFrame.getLifeCount(), maxVisibleLifes); life++) {
            Bonus lifeBonus = new Bonus(GameSettings.BonusTypes.EXTRA_LIFE.ordinal()); 			// ��������� �����
            lifeBonus.setScaleX(scale);																// ������������ ���� ������� �� �� X
            lifeBonus.setScaleY(scale);																// �� �� �� Y	
            lifeBonus.setTranslateX(livesText.getTranslateX() +
                livesText.getBoundsInLocal().getWidth() + (life % 4) * lifeBonus.getBonusWidth()); 	// �������� 4 ���������� ����� ������������ � �����
            lifeBonus.setTranslateY(livesText.getTranslateY() +
                (life / 4) * lifeBonus.getBonusHeight() * MOB_SCALING);								// �������� 2 ���������� ����� ������������ � �����
            
            lives.add(lifeBonus);																	// ������ ���� ����� � ������ ����� �����������
            informationPanel.getChildren().add(lifeBonus);											// �� ���������� ���� ����� �� ������������ �����
        }
    }

    private void lostLife() {								// ���� ���������� ������ �����
    	
        mainFrame.decreaseLivesCount();						// �������� ������� ����� �� ������� �����
        
        if (mainFrame.getLifeCount() < 0) { 				// ���� ����� ����� �� ��������
            state = States.GAME_OVER.ordinal();				// ���� ��� ����� ���������� ���
            ball.setVisible(false);							// ������ �'�� ���������
            board.setVisible(false);						// ������ ����� ���������
            
            message.setImage(GameSettings.getOtherImages().get(GameSettings.OtherImages.GAMEOVER_IMAGE.ordinal()));	// ���������� �������� "GAME OVER"
            message.setTranslateX((GameSettings.FIELD_WIDTH - message.getImage().getWidth()) / 2);			// ������ ���� �� �� X
            message.setTranslateY(GameSettings.FIELD_Y +
                (GameSettings.FIELD_HEIGHT - message.getImage().getHeight()) / 2);							// �� �� �� Y	
            message.setVisible(true);						// ������ ����������� (��������) �������
            message.setOpacity(1);							// ������ �������� �������� ����� ������������ �����������
        } 
        
        else {												// ���� ����� �� ��������
            updateLives();
            board.changeBoardSize(Board.DEFAULT_SIZE);		// ��������� ����� ����� �� ������������� 
            ball.changeBallSize(Ball.DEFAULT_BALL_SIZE);	// ��������� ����� �'��� �� �������������
            board.setTranslateX((GameSettings.FIELD_WIDTH - board.getBoardWidth()) / 2);	// ������������ ����� �� ������ ������ �����
            ball.setTranslateX(GameSettings.FIELD_WIDTH / 2 - ball.getBallDiameter() / 2);
            ball.setTranslateY(GameSettings.BOARD_Y - ball.getBallDiameter());			// ������������ �'�� �� ������ �����
            state = States.BALL_CATCHED.ordinal();			// �'�� ����������� �� �����
            catchedBonus = 0;
            ballDirX = (Utils.random(2) * 2 - 1) * GameSettings.BALL_MIN_COORD_SPEED;
            ballDirY = - GameSettings.BALL_MIN_SPEED;
        }
    }
}