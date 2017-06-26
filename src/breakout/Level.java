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
    private static final MainFrame mainFrame = Main.getMainFrame();	// головне (вступне) вікно

    private int brickCount;											// кількість цеглин
    private int catchedBonus;										// тип зловленого бонусу
    
    private ArrayList<Brick> bricks;								// список цеглин
    private ArrayList<Brick> fadeBricks;							// список знищених цеглин
    private ArrayList<Bonus> bonuses;								// список бонусів
    private ArrayList<Bonus> lives;									// список життів

    private Group group;

    private enum States {											// перелік можливих станів гри
    	STARTING_LEVEL, BALL_CATCHED, PLAYING, GAME_OVER
    }

    private int state;												// стан гри
    private int boardDirection;										// напрям руху дошки
    
    private double ballDirX, ballDirY;								// напрям руху м'яча по осям X та Y
    
    private int levelNumber;										// номер рівня
    
    private Board board;											// об'єкт дошки
    private Ball ball;												// об'єкт м'яча
    
    private Text roundText;											// надпис "РІВЕНЬ:"
    private Text round;												// надпис, що відображає номер рівня
    private Text scoreText;											// надпис "РАХУНОК:"
    private Text score;												// надпис, що відображає рахунок гравця
    private Text livesText;											// надпис "ЖИТТЯ:"
    
    private ImageView message;										// відображення зображення повідомлення (READY, GAME OVER)
    
    private Timeline startingTimeline;
    private Timeline timeline;
    
    private Group informationPanel;									// інформаційна панель

    public Level(int levelNumber) {
    	
        group = new Group();
        getChildren().add(group);
        initializeContent(levelNumber);								// ініціалізація контенту
    }
    
    private void initializeContent(int level) {						// ініціалізація контенту
    	
        state = States.STARTING_LEVEL.ordinal();					// перехід в стан початку рівня
        levelNumber = level;										// встановлення номеру рівня
        
        lives = new ArrayList <Bonus>();							// список життів
        bricks = new ArrayList <Brick>();							// список цеглин
        fadeBricks = new ArrayList <Brick>();						// список знищених цеглин
        bonuses = new ArrayList <Bonus>();							// список бонусів
        
        ball = new Ball(); 											// створення об'єкту м'яча
        ball.setVisible(false);										// початково м'яч невидимий
        
        board = new Board();										// створення об'єкту дошки
        board.setTranslateY(GameSettings.BOARD_Y);
        board.setVisible(false);									// початково дошка невидима
        
        message = new ImageView();									// для відображення зображення "READY"
        message.setImage(GameSettings.getOtherImages().get(GameSettings.OtherImages.READY_IMAGE.ordinal()));	// встановлення зображення "READY" для його відображення
        message.setTranslateX((GameSettings.FIELD_WIDTH - message.getImage().getWidth()) / 2);	// зсув повідомлення по осі X відносно ширини поля та ширини зображення
        message.setTranslateY(GameSettings.FIELD_Y +
            (GameSettings.FIELD_HEIGHT - message.getImage().getHeight()) / 2);					// зсув повідомлення по осі Y відносно висоти поля та висоти зображення
        message.setVisible(false);									// початково це повідомлення невидиме
        
        initializeLevelBricks();									// ініціалізація цеглин на рівні
        initializeStartingTimeline();								// відображення стартової анімації ігрового вікна
        initializeTimeline();
        initializeInformationPanel();								// ініціалізація інформаційної панелі
        
        ImageView backgroundImage = new ImageView();				// відображення зображення ігрової панелі
        backgroundImage.setFocusTraversable(true);					// наведення фокусу на це зображення
        backgroundImage.setImage(GameSettings.getOtherImages().get(GameSettings.OtherImages.BACKGROUND_IMAGE.ordinal()));	// встановлення зображення ігрової панелі
        backgroundImage.setFitWidth(GameSettings.SCREEN_WIDTH);	// встановлення кінцевої ширини ігрової панелі розміром заданої ширини екрану
        backgroundImage.setFitHeight(GameSettings.SCREEN_HEIGHT);	// встановлення кінцевої висоти ігрової панелі розміром заданої висоти екрану
        
        backgroundImage.setOnMouseMoved(event -> {					// обробник події при русі (переміщенні) мишки
            moveBoard(event.getX() - board.getBoardWidth() / 2);
        });
        
        backgroundImage.setOnMouseDragged(event -> {				// обробник події для можливості руху мишки при тривалому натисненні на клавішу миші
            moveBoard(event.getX() - board.getBoardWidth() / 2);
        });
        
        backgroundImage.setOnMousePressed(event -> {				// обробник подій при натисненні на клавішу мишки
            
            if (state == States.BALL_CATCHED.ordinal()) {			// якщо м'яч прилипнутий до дошки
                state = States.PLAYING.ordinal();					// після натиску на клавішу миші він почне рух
            }
            
            if (state == States.GAME_OVER.ordinal()) {				// якщо користувач втратив всі життя або пройшов усі рівні
                mainFrame.changeExecutionState(MainFrame.START_WINDOW);	// повертаємось на стартове вікно
            }
        });
        
        backgroundImage.setOnKeyPressed(event -> {					// обробник подій при натиску на деякі клавіші клавіатури
                
        	if (event.getCode() == KeyCode.ESCAPE) { 				// якщо користувач натиснув клавішу ESCAPE (Esc) 
                Platform.exit();									// ігрове вікно закривається
            }
        	
        	// якщо м'яч прилипнутий до дошки і натиснуто клавішу SPACE (пробіл) або ENTER
            if (state == States.BALL_CATCHED.ordinal() && (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.ENTER)) {
                state = States.PLAYING.ordinal();					// гра продовжується (м'яч відлипає від дошки і починає рух)
            }
            
            if (state == States.GAME_OVER.ordinal()) {				// якщо користувач втратив всі життя або пройшов усі рівні
                mainFrame.changeExecutionState(MainFrame.START_WINDOW);	// повертаємось на стартове вікно
            }
            
            // якщо йде гра (м'яч на даний момент рухається) і натиснута клавіша Q
            if (state == States.PLAYING.ordinal() && event.getCode() == KeyCode.Q) {
            	
            	// ми втрачаємо життя, а дошка та м'яч повертаються на задані за замовчуванням місця
                lostLife();
                return;
            }
            
            if ((event.getCode() == KeyCode.LEFT)) {				// якщо натиснута стрілка вліво	
            	boardDirection = - GameSettings.BOARD_SPEED;		// переміщуємо дошку вліво з заданою швидкістю руху дошки
            }
            
            if ((event.getCode() == KeyCode.RIGHT)) {				// якщо натиснута стрілка вправо
            	boardDirection = GameSettings.BOARD_SPEED;			// переміщуємо дошку вправо з заданою швидкістю руху дошки
            }

        });
        
        backgroundImage.setOnKeyReleased(event -> {					// обробник подій при відпусканні деяких клавіш клавіатури (стрілок вліво та вправо)
            	
        	// якщо перед тим була натиснена стрілка вліво або вправо і ми її відпустили
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
            	boardDirection = 0;									// рух дошки припиняється
            }
        });
        
        group.getChildren().add(backgroundImage);					// додаємо картинку на панель
        
        for (int row = 0; row < bricks.size() / GameSettings.FIELD_BRICK_IN_ROW; row++) { // розставлення цеглин на ігровій панелі
            for (int col = 0; col < GameSettings.FIELD_BRICK_IN_ROW; col++) {
                Brick b = getBrick(row, col);						// створення нової цеглини
                
                if (b != null) {									// якщо цеглина є (тобто, в класі LevelData вона має якийсь тип, а не пропущена)
                    group.getChildren().add(b);						// додаємо її на ігрову панель
                }
            }
        }

        group.getChildren().addAll(message, ball, board, informationPanel);	// додаємо на панель повідомлення "READY", м'яч, дошку та інформаційну панель
    }

    private void initializeLevelBricks() {								// ініціалізація цеглин на рівні
    	
        String[] level = LevelData.getLevelData(levelNumber);			// отримання інформації про склад рівня по його номеру
        
        for (int row = 0; row < level.length; row++) {					// проходимось по рядках рівня
            for (int col = 0; col < GameSettings.FIELD_BRICK_IN_ROW; col++) {	// та по довжині кожного рядка (його стовпцях) 
                String rowString = level[row];							// виділяємо по ряду кожен ряд рівня
                Brick brick = null;										// створюємо змінну, що може згодом відповідати цеглині
                
                if (rowString != null && col < rowString.length()) {	// якщо рядок присутній та не досігнуто його кінця
                    String type = rowString.substring(col, col + 1);	// визначаємо символ, що може відповідати типу (кольору) цеглини
                    
                    if (!type.equals(" ")) {							// якщо це не пробіл (цеглина не відсутня)
                        brick = new Brick(Brick.getBrickType(type));	// визначаємо її тип
                        brick.setTranslateX(col * GameSettings.BRICK_WIDTH);							// зміщуємо її по осі X
                        brick.setTranslateY(GameSettings.FIELD_Y + row * GameSettings.BRICK_HEIGHT); 	// та по осі Y
                        
                        if (brick.getType() != GameSettings.BrickTypes.GREY_BRICK.ordinal()) {			// якщо цеглина не кам'яна
                            brickCount++;								// збільшуємо кількість "нормальних" цеглин
                        }
                    }
                }
                
                bricks.add(brick);										// додаємо цеглину у список цеглин
            }
        }
    }
    
    private void initializeStartingTimeline() {							// відображення стартової анімації ігрового вікна
    	
        startingTimeline = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.millis(500), event -> {	// час очікування початку гри (надпису "READY")
        	
            message.setVisible(true);									// встановлення повідомлення видимим
            state = States.STARTING_LEVEL.ordinal();					// встановлення стану гри рівним початку (завантаженні) рівня
            board.setVisible(false);									// початково дошка невидима
            ball.setVisible(false);										// початково м'яч невидимий
        }, new KeyValue(message.opacityProperty(), 0));
        
        KeyFrame kf2 = new KeyFrame(Duration.millis(1500), new KeyValue(message.opacityProperty(), 1)); // час відображення надпису "READY"
        KeyFrame kf3 = new KeyFrame(Duration.millis(3000), new KeyValue(message.opacityProperty(), 1));	// час зникання надпису "READY"
        
        KeyFrame kf4 = new KeyFrame(Duration.millis(2000), event -> {	// час, після якого надпис пропадає та починається гра
            
            message.setVisible(false);									// встановлення повідомлення невидимим

            board.setTranslateX((GameSettings.FIELD_WIDTH - board.getBoardWidth()) / 2.0);	// розміщення дошки на панелі по осі X
            
            ball.setTranslateX((GameSettings.FIELD_WIDTH - ball.getBallDiameter()) / 2.0);	// розміщення м'яча на панелі по осі X
            ball.setTranslateY(GameSettings.BOARD_Y - ball.getBallDiameter());				// розміщення м'яча на панелі по осі Y
            
            ballDirX = (Utils.random(2) * 2 - 1) * GameSettings.BALL_MIN_COORD_SPEED;
            ballDirY = -GameSettings.BALL_MIN_SPEED;

            board.setVisible(true);										// встановлення дошки видимою
            ball.setVisible(true);										// встановлення м'яча видимим
            state = States.BALL_CATCHED.ordinal();						// м'яч прилипнутий до дошки
        }, new KeyValue(message.opacityProperty(), 0));

        startingTimeline.getKeyFrames().addAll(kf1, kf2, kf3, kf4); 	// визначаємо ключові кадри для відображення стартової анімації
    }

    private void initializeTimeline() {									// інаціалізація анімаціїї гри
    	
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);					// нескінченна кількість повторів анімації
        
        KeyFrame kf = new KeyFrame(GameSettings.ANIMATION_TIME, event -> {
        	
        	// опрацювання зникання цеглин
            Iterator<Brick> brickIterator = fadeBricks.iterator();
            
            while (brickIterator.hasNext()) {
                Brick brick = brickIterator.next();
                brick.setOpacity(brick.getOpacity() - 0.1);
                
                if (brick.getOpacity() <= 0) {
                    brick.setVisible(false);							// цеглина зникає
                    brickIterator.remove();								// та видаляється зі списку цеглин
                }
            }
            
            // переміщення дошки (якщо необхідно)
            if (boardDirection != 0 && state != States.STARTING_LEVEL.ordinal()) {
                moveBoard(board.getTranslateX() + boardDirection);
            }
            
            // опрацювання бонусів
            Iterator<Bonus> bonusIterator = bonuses.iterator();
            
            while (bonusIterator.hasNext()) {
                Bonus bonus = bonusIterator.next();
                
                if (bonus.getTranslateY() > GameSettings.SCREEN_HEIGHT) {	// якщо не встигли зловити бонус
                    bonus.setVisible(false);								// він стає невидимим
                    bonusIterator.remove();									// та видаляється зі списку бонусів
                    group.getChildren().remove(bonus);						// та з панелі
                } 
                
                else {
                    bonus.setTranslateY(bonus.getTranslateY() + GameSettings.BONUS_SPEED); // швидкість падіння бонусу
                    
                    if (bonus.getTranslateX() + bonus.getBonusWidth() > board.getTranslateX() &&
                            bonus.getTranslateX() < board.getTranslateX() + board.getBoardWidth() &&
                            bonus.getTranslateY() + bonus.getBonusHeight() > board.getTranslateY() &&
                            bonus.getTranslateY() < board.getTranslateY() + board.getBoardHeight()) {	// якщо зловили бонус
                    	
                        updateScore(100);							// додаємо 100 очок до рахунку
                        catchedBonus = bonus.getBonusType();		// визначаємо тип бонусу
                        bonus.setVisible(false);					// робимо його невидимим
                        bonusIterator.remove();						// видаляємо його зі списку бонусів
                        group.getChildren().remove(bonus);			// та з панелі
                        
                        if (bonus.getBonusType() == GameSettings.BonusTypes.REDUCE_BALL_SPEED.ordinal()) { 	// якщо бонус - це сповільнення руху м'яча
                            ballDirX /= 1.5;						// зменшуємо швидкість руху м'яча по осі X в 1.5 рази
                            ballDirY /= 1.5;						// зменшуємо швидкість руху м'яча по осі Y в 1.5 рази
                            correctBallSpeed();						// коректуємо (зменшуємо) швидкість м'яча
                        } 
                        
                        else if (bonus.getBonusType() == GameSettings.BonusTypes.GROW_BALL_SPEED.ordinal()) { 	// якщо бонус - це пришвидшення руху м'яча
                            ballDirX *= 1.5;						// збільшуємо швидкість руху м'яча по осі X в 1.5 рази
                            ballDirY *= 1.5;						// збільшуємо швидкість руху м'яча по осі X в 1.5 рази
                            correctBallSpeed();						// коректуємо (збільшуємо швидкість м'яча)
                        } 
                        
                        else if (bonus.getBonusType() == GameSettings.BonusTypes.REDUCE_BOARD_SIZE.ordinal()) {// якщо бонус - це зменшення розміру дошки
                        	
                            if (board.getBoardSize() > 0) {							// якщо дошку ще можна зменшити
                                int oldWidth = board.getBoardWidth();				// визначаємо її попередню ширину
                                board.changeBoardSize(board.getBoardSize() - 1);	// зменшуємо її розмір
                                board.setTranslateX(board.getTranslateX() + ((oldWidth - board.getBoardWidth()) / 2)); // та змінюємо її положення по осі X
                            }
                        } 
                        
                        else if (bonus.getBonusType() == GameSettings.BonusTypes.GROW_BOARD_SIZE.ordinal()) { 	// якщо бонус - це збільшення розміру дошки
                        	
                            if (board.getBoardSize() < Board.MAX_SIZE) {			// якщо не досягнуто максимального розміру дошки
                            	board.changeBoardSize(board.getBoardSize() + 1);	// збільшуємо її розмір
                                
                                if (board.getTranslateX() + board.getBoardWidth() > GameSettings.FIELD_WIDTH) {// якщо після збільшення дошки вона вилазить за межі екрану
                                	board.setTranslateX(GameSettings.FIELD_WIDTH - board.getBoardWidth());		// коректуємо це
                                }
                            }
                        } 
                        
                        else if (bonus.getBonusType() == GameSettings.BonusTypes.REDUCE_BALL_SIZE.ordinal()) { // якщо бонус - це зменшення розміру м'яча
                        	
                            if (ball.getBallSize() > 0) {						// якщо м'яч ще можна зменшити
                                ball.changeBallSize(ball.getBallSize() - 1);	// зменшуємо його розмір
                                
                                if (state == States.BALL_CATCHED.ordinal()) {
                                    ball.setTranslateY(GameSettings.BOARD_Y - ball.getBallDiameter());
                                }
                            }
                        } 
                         
                        else if (bonus.getBonusType() == GameSettings.BonusTypes.GROW_BALL_SIZE.ordinal()) { 	// якщо бонус - це збільшення розміру м'яча
                        	
                            if (ball.getBallSize() < Ball.MAX_BALL_SIZE) {		// якщо не досягнуто максимального розміру м'яча
                                ball.changeBallSize(ball.getBallSize() + 1);	// збільшуємо його розмір
                                
                                if (state == States.BALL_CATCHED.ordinal()) {
                                    ball.setTranslateY(GameSettings.BOARD_Y - ball.getBallDiameter());
                                }
                            }
                        } 
                        
                        else if (bonus.getBonusType() == GameSettings.BonusTypes.EXTRA_LIFE.ordinal()) { 		// якщо бонус - це додаткове життя
                            mainFrame.increaseLivesCount();				// оновлюємо кількість подій у вікні
                            updateLives();								// та на ігровій панелі
                        }
                    }
                }
            }
            
            if (state != States.PLAYING.ordinal()) {
                return;
            }
            
            double newX = ball.getTranslateX() + ballDirX;	// визначаємо рух м'яча по осі X
            double newY = ball.getTranslateY() + ballDirY;	// визначаємо рух м'яча по осі Y
            
            boolean inverseX = false;						// інверсія руху м'яча по осі X
            boolean inverseY = false;						// інверсія руху м'яча по осі Y
            
            if (newX < 0) {									// якщо вдарились у стіну
                newX = -newX;								// інверсуємо рух м'яча по осі X
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
            
            // визначаємо попадання м'яча по дошці
            if (ballDirY > 0 &&
                    ball.getTranslateY() + ball.getBallDiameter() < GameSettings.BOARD_Y &&
                    newY + ball.getBallDiameter() >= GameSettings.BOARD_Y &&
                    newX >= board.getTranslateX() - ball.getBallDiameter() &&
                    newX < board.getTranslateX() + board.getBoardWidth() + ball.getBallDiameter()) {
                inverseY = true;

                // збільшуємо швидкість м'яча
                double speed = Math.sqrt(ballDirX * ballDirX + ballDirY * ballDirY);
                ballDirX *= (speed + GameSettings.BALL_SPEED_INC) / speed;
                ballDirY *= (speed + GameSettings.BALL_SPEED_INC) / speed;

                // коректуємо рух м'яча по осям X та Y
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
                
                correctBallSpeed();		// коректуємо швидкість м'яча
                
                if (catchedBonus == GameSettings.BonusTypes.CATCH_BALL.ordinal()) { // якщо бонус - це прилипання м'яча до дошки
                    newY = GameSettings.BOARD_Y - ball.getBallDiameter();
                    state = States.BALL_CATCHED.ordinal();	// м'яч прилипає до дошки і не рухається
                }
            }

            // визначаємо попадання м'яча в цеглину
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
                            catchedBonus != GameSettings.BonusTypes.FIRE_BALL.ordinal()) { // ПЕРЕНАЗВАТЬ
                        inverseX = true;
                        inverseY = true;
                    }
                }
            }
            
            ball.setTranslateX(newX);
            ball.setTranslateY(newY);
            
            if (inverseX) {			// інверсуємо рух м'яча по осі X
                ballDirX = - ballDirX;
            }
            
            if (inverseY) {			// інверсуємо рух м'яча по осі Y
                ballDirY = - ballDirY;
            }
            
            if (ball.getTranslateY() > GameSettings.SCREEN_HEIGHT) { // якщо не встигли зловити м'яча і він торкнувся підлоги
                lostLife(); 		// відбувається втрата життя
            }
        });
        
        timeline.getKeyFrames().add(kf);	// додавання ключових кадрів в ігрову анімацію
    }
    
    private void initializeInformationPanel() {						// ініціалізація панелі з інформацією (рівень, поточний рахунок, кількість життів, підказки до бонусів)
    	
    	informationPanel = new Group();								// створення інформаційної панелі
        Font f = new Font("Impact", 18);							// шрифт для надписів
        
        roundText = new Text();										// надпис "РІВЕНЬ:"
        roundText.setTranslateX(30);								// зміщення надпису по осі X
        roundText.setTranslateY(128);								// зміщення надпису по осі Y
        roundText.setText("РІВЕНЬ:");								// встановлення тексту надпису
        roundText.setFill(Color.AZURE);								// колір тексту надпису
        roundText.setTextOrigin(VPos.TOP);							// вирівнювання тексту по вертикалі
        roundText.setFont(f);										// встановлення шрифту надпису		
        
        round = new Text();											// надпис, що відповідає рівню користувача
        round.setTranslateX(roundText.getTranslateX() + roundText.getBoundsInLocal().getWidth() + GameSettings.INFO_TEXT_SPACE + 12); // визначаємо його розміщення по осі X
        round.setTranslateY(roundText.getTranslateY());				// та по осі Y
        round.setFill(Color.rgb(0, 204, 102));						// встановлюємо колір тексту
        round.setTextOrigin(VPos.TOP);								// вирівнювання по вертикалі
        round.setFont(f);											// встановлюємо шрифт
        round.setText(levelNumber + "");							// та текст
        
        scoreText = new Text();										// надпис "РАХУНОК:"
        scoreText.setTranslateX(30);								// зміщення надпису по осі X
        scoreText.setTranslateY(164);								// зміщення надпису по осі Y
        scoreText.setText("РАХУНОК:");								// встановлення тексту надпису
        scoreText.setFill(Color.AZURE);								// колір тексту надпису
        scoreText.setTextOrigin(VPos.TOP);							// вирівнювання тексту по вертикалі
        scoreText.setFont(f);										// встановлення шрифту надпису	
        
        score = new Text();											// надпис, що відповідає рахунку користувача
        score.setTranslateX(scoreText.getTranslateX() + scoreText.getBoundsInLocal().getWidth() + GameSettings.INFO_TEXT_SPACE); // визначаємо його розміщення по осі X
        score.setTranslateY(scoreText.getTranslateY());				// та по осі Y
        score.setFill(Color.rgb(0, 204, 102));						// встановлюємо колір тексту
        score.setTextOrigin(VPos.TOP);								// вирівнювання по вертикалі
        score.setFont(f);											// встановлюємо шрифт
        score.setText("");											// та текст
        
        livesText = new Text();										// надпис "ЖИТТЯ:"
        livesText.setTranslateX(30);								// зміщення надпису по осі X
        livesText.setTranslateY(200);								// зміщення надпису по осі Y
        livesText.setText("ЖИТТЯ:");								// встановлення тексту надпису
        livesText.setFill(Color.AZURE);								// колір тексту надпису
        livesText.setTextOrigin(VPos.TOP);							// вирівнювання тексту по вертикалі
        livesText.setFont(f);										// встановлення шрифту надпису
        
        Rectangle black = new Rectangle();							// прямокутник - для кольору панелі з інформацієї
        black.setWidth(GameSettings.SCREEN_WIDTH - GameSettings.FIELD_WIDTH);	// ширина інформаційної панелі (задана ширина екрану - ширина ігрової панелі)
        black.setHeight(GameSettings.SCREEN_HEIGHT);				// висота інформаційної панелі
        black.setFill(Color.BLACK);									// колір інформаційної панелі
        
        ImageView vline = new ImageView();							// "розділювач" між ігровою та інформаційною панелями
        vline.setImage(GameSettings.getOtherImages().get(GameSettings.OtherImages.VLINE_IMAGE.ordinal()));// зображення "розділювача" між ігровою та інформаційною панелями
        
        ImageView logo = new ImageView();							// зображення гри
        logo.setImage(GameSettings.getOtherImages().get(GameSettings.OtherImages.LOGO_IMAGE.ordinal()));	// встановлення зображення гри
        logo.setTranslateX(90);										// зміщення зображення по осі X
        logo.setTranslateY(35);										// зміщення зображення по осі Y
        
        Text bonuses = new Text();									// надпис "БОНУСИ"
        bonuses.setTranslateX(80);									// зміщення надпису по осі X
        bonuses.setTranslateY(280);									// зміщення надпису по осі Y
        bonuses.setText("БОНУСИ");									// встановлення тексту надпису
        bonuses.setFill(Color.RED);									// колір тексту надпису
        bonuses.setTextOrigin(VPos.TOP);							// вирівнювання тексту по вертикалі
        bonuses.setFont(new Font("Impact", 40));					// встановлення шрифту надпису
        
        informationPanel.getChildren().addAll(black, vline, logo, roundText, round, scoreText, score, livesText, bonuses); // додаємо ці елементи на інформаційну панель
        
        for (int i = 0; i < Bonus.BONUS_NAMES.length; i++) {		// додавання бонусів та підказок щодо них на інформаційну панель
            Bonus bonus = new Bonus(i);
            
            Text text = new Text();
            text.setTranslateX(80);									// зміщення назви бонусу по осі X
            text.setTranslateY(350 + i * 40);						// зміщення назви бонусу по осі Y
            text.setText(Bonus.BONUS_NAMES[i]);						// встановлення тексту назви бонусу
            text.setFill(Color.DARKORANGE);							// колір тексту назви бонусу
            text.setTextOrigin(VPos.TOP);							// вирівнювання тексту по вертикалі
            text.setFont(new Font("Arial", 12));					// встановлення шрифту тексту назви бонусу
            
            bonus.setTranslateX(10 + (820 - 750 - bonus.getBonusWidth()) / 2);
            bonus.setTranslateY(text.getTranslateY() - (bonus.getBonusHeight() - text.getBoundsInLocal().getHeight()) / 2);

            informationPanel.getChildren().addAll(bonus, text);		// додавання зображення та назви бонусу на інформаційну панель
        }
        
        informationPanel.setTranslateX(GameSettings.FIELD_WIDTH);	// розміщення інформаційної панелі за ігровою панеллю (в правій частині вікна)
    }

    public void start() {	// початок гри
    	
        startingTimeline.play();
        timeline.play();
        group.getChildren().get(0).requestFocus();
        updateScore(0);		// рахунок рівний 0
        updateLives();		// встановлення кількості життів по замовчуванню (4)
    }

    public void stop() {	// завершення гри
    	
        startingTimeline.stop();
        timeline.stop();
    }

    private Brick getBrick(int row, int col) {					// отримання цеглини по текстовим даним
    	
        int i = row * GameSettings.FIELD_BRICK_IN_ROW + col;	// визначаємо позицію для перевірки
        
        // якщо на визначеній позиції цеглину не знайдено 
        if (col < 0 || col >= GameSettings.FIELD_BRICK_IN_ROW || row < 0 || i >= bricks.size()) {
        	
            return null;	// нова цеглина не створюватиметься
        } 
        
        else {				// інакше визначаємо на повертаємо символ, що відповідає типу цеглини
        	
            return bricks.get(i);
        }
    }

    private void kickBrick(int row, int col) {									// визначення дій при попаданні в цеглину
    	
        Brick brick = getBrick(row, col);										// визначаємо позицію та тип цеглини, в яку потрапили
        
        // якщо цеглина вже відсутня або в нас зараз немає "вогляного" м'яча (бонус) і ми потрапили у кам'яну цеглину чи цеглину, що "надбивається"
        if (brick == null || (catchedBonus != GameSettings.BonusTypes.FIRE_BALL.ordinal() && !brick.ifBrickCanBeKicked())) {
            return;		// нічого не відбувається
        }
        
        updateScore(10);														// інакше додаємо 10 очок за попадання в цеглину
        
        if (brick.getType() != GameSettings.BrickTypes.GREY_BRICK.ordinal()) {	// якщо потрапили не в сіру (кам'яну) цеглину
            brickCount--;														// зменшуємо кількість цеглин на рівні
            
            if (brickCount == 0) {												// якщо збито всі цеглини
                mainFrame.changeExecutionState(mainFrame.getExecutionState() + 1);	// відбувається перехід на новий рівень (якщо він ще є)
            }
        }
        
        bricks.set(row * GameSettings.FIELD_BRICK_IN_ROW + col, null);			// стираємо посилання на цеглину у списку цеглин рівня
        fadeBricks.add(brick);													// та додаємо збиту цеглину у список збитих цеглин
        
        // генеруємо випадкове значення
        if (Utils.random(8) == 0 && bonuses.size() < 5) {						// якщо випадково згенероване значення рівне 0 та ми зловили менше, ніж 5 бонусів				
            Bonus bonus = new Bonus(Utils.random(Bonus.BONUS_NAMES.length));	// створюємо об'єкт бонусу (генеруємо випадковий тип бонусу від 0 до 8)
            bonus.setTranslateY(brick.getTranslateY());							// зміщуємо його по осі Y на позицію збитої цеглини
            bonus.setTranslateX(brick.getTranslateX() + (GameSettings.BRICK_WIDTH - bonus.getBonusWidth()) / 2); // та по осі X
            bonus.setVisible(true);												// робимо його видимим
            group.getChildren().add(bonus);										// додаємо його на панель
            bonuses.add(bonus);													// та в масив бонусів
        }
    }
    
    private void correctBallSpeed() {											// коректування швидкості м'яча
    	
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
    
    private void updateScore(int inc) {			// оновлення рахунку
    	
        mainFrame.setScore(mainFrame.getScore() + inc);
        score.setText(mainFrame.getScore() + "");
    }

    private void moveBoard(double newX) {		// переміщення дошки
    	
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

    private void updateLives() {	// оновлення кількості життів
    	
        while (lives.size() > mainFrame.getLifeCount()) {
            Bonus lifeBat = lives.get(lives.size() - 1);
            lives.remove(lifeBat);
            informationPanel.getChildren().remove(lifeBat);
        }
        
        // додавання життя (але якщо не більше 8)
        int maxVisibleLifes = 8;																	// максимальна кількість життів
        double scale = 0.8;																			// зміщення

        for (int life = lives.size(); life < Math.min(mainFrame.getLifeCount(), maxVisibleLifes); life++) {
            Bonus lifeBonus = new Bonus(GameSettings.BonusTypes.EXTRA_LIFE.ordinal()); 			// додаткове життя
            lifeBonus.setScaleX(scale);																// встановлюємо його масштаб по осі X
            lifeBonus.setScaleY(scale);																// та по осі Y	
            lifeBonus.setTranslateX(livesText.getTranslateX() +
                livesText.getBoundsInLocal().getWidth() + (life % 4) * lifeBonus.getBonusWidth()); 	// максимум 4 зображення життів відображається в рядку
            lifeBonus.setTranslateY(livesText.getTranslateY() +
                (life / 4) * lifeBonus.getBonusHeight() * MOB_SCALING);								// максимум 2 зображення життів відображається в рядку
            
            lives.add(lifeBonus);																	// додаємо нове життя в список життів користувача
            informationPanel.getChildren().add(lifeBonus);											// та відображаємо нове життя на інформаційній панелі
        }
    }

    private void lostLife() {								// якщо відбувалась втрата життя
    	
        mainFrame.decreaseLivesCount();						// зменшуємо кількість життів на головній панелі
        
        if (mainFrame.getLifeCount() < 0) { 				// якщо життів більше не лишилось
            state = States.GAME_OVER.ordinal();				// стан гри рівний завершенню гри
            ball.setVisible(false);							// робимо м'яч невидимим
            board.setVisible(false);						// робимо дошку невидимою
            
            message.setImage(GameSettings.getOtherImages().get(GameSettings.OtherImages.GAMEOVER_IMAGE.ordinal()));	// відображаємо картинку "GAME OVER"
            message.setTranslateX((GameSettings.FIELD_WIDTH - message.getImage().getWidth()) / 2);			// зміщуємо його по осі X
            message.setTranslateY(GameSettings.FIELD_Y +
                (GameSettings.FIELD_HEIGHT - message.getImage().getHeight()) / 2);							// та по осі Y	
            message.setVisible(true);						// робимо повідомлення (картинку) видимим
            message.setOpacity(1);							// робимо невелику затримку перед відображенням повідомлення
        } 
        
        else {												// якщо життя ще лишились
            updateLives();
            board.changeBoardSize(Board.DEFAULT_SIZE);		// повертаємо розмір дошки за замовчуванням 
            ball.changeBallSize(Ball.DEFAULT_BALL_SIZE);	// повертаємо розмір м'яча за замовчуванням
            board.setTranslateX((GameSettings.FIELD_WIDTH - board.getBoardWidth()) / 2);	// встановлюємо дошку по центру ігрової панелі
            ball.setTranslateX(GameSettings.FIELD_WIDTH / 2 - ball.getBallDiameter() / 2);
            ball.setTranslateY(GameSettings.BOARD_Y - ball.getBallDiameter());			// встановлюємо м'яч по центру дошки
            state = States.BALL_CATCHED.ordinal();			// м'яч прилипнутий до дошки
            catchedBonus = 0;
            ballDirX = (Utils.random(2) * 2 - 1) * GameSettings.BALL_MIN_COORD_SPEED;
            ballDirY = - GameSettings.BALL_MIN_SPEED;
        }
    }
}