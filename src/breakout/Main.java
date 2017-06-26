package breakout;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static MainFrame mainFrame;								// вікно програми (стартове або ігрове)

    public static MainFrame getMainFrame () {						// повернення вікна програми
    	
        return mainFrame;
    }
    
    @Override 
    public void start (Stage stage) {								// ГОЛОВНИЙ МЕТОД, що відповідає за запуск гри 
    	
        GameSettings.initializeAllImagesLists();					// ініціалізація всіх зображень
        
        Group root = new Group();
        mainFrame = new MainFrame(root);
        
        stage.setTitle("BREAKOUT RR");								// заголовок вікна
        stage.setResizable(false);									// заборона зміни розміру вікна
        stage.setWidth(GameSettings.SCREEN_WIDTH + 2 * GameSettings.WINDOW_BORDER);	// ширина вікна
        stage.setHeight(GameSettings.SCREEN_HEIGHT+ 2 * GameSettings.WINDOW_BORDER);	// висота вікна
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        mainFrame.changeExecutionState(MainFrame.START_WINDOW);		// завантаження стартового вікна програми
        
        stage.getIcons().add(new Image("breakout/other/logo.png"));	// встановлення іконки сцени
        stage.show();												// відображення сцени
    }

    public class MainFrame {

        private Group root;

        private StartWindow startWindow;							// екземпляр стартового вікна

        private Level level;										// екзмемпляр рівня (ігрового вікна)

        private int lifeCount; 										// кількість життів
        private int score; 											// поточний рахунок

        private MainFrame (Group root) {
        	
            this.root = root;
        }

        public int getExecutionState() {							// отримання стану гри (стартове вікно -> перший рівень -> другий рівень -> ...)
        	
            return state;
        }

        public int getScore () {									// отримання рахунку
        	
            return score;
        }

        public void setScore (int score) {							// встановлення рахунку
        	
            this.score = score;
        }

        public int getLifeCount () {								// отримання кількості життів
        	
            return lifeCount;
        }

        public void increaseLivesCount () {							// збільшення кількості життів
        	
            lifeCount = Math.min(lifeCount + 1, GameSettings.MAX_LIVES);
        }
        
        public void decreaseLivesCount () {							// зменшення кількості життів
        	
            lifeCount--;
        }

        public static final int START_WINDOW = 0;
        private int state = START_WINDOW;							// стан завантаження стартового вікна

        public void changeExecutionState (int newState) {			// зміна стану виконання (рівнів)
        	
            this.state = newState;									// встановлення нового стану виконання
            
            if (startWindow != null) {								// якщо відбувся перехід з стартового вікна на ігрове
            	startWindow.stop();									// завершення відображення анімації стартового вікна
            }
            
            if (level != null) {									// якщо припиняється гри (закінчились рівні або користувач втратив усі життя)
                level.stop();										// завершення відтворення анімації ігрового вікна
            }
            
            if (state < 1 || state > LevelData.getLevelsCount()) {	// якщо ми в стані відображення стартового вікна або гру завершено
                root.getChildren().remove(level);					// видаляємо рівень з панелі
                level = null;										// створюємо об'єкт, що відповідатиме рівню
                startWindow = new StartWindow();					// створюємо стартове вікно
                root.getChildren().add(startWindow);				// додаємо його на панель
                startWindow.start();								// починаємо завантаження стартового вікна
            } 
            
            else {													// якщо ми в ігровому стані
                root.getChildren().remove(startWindow);				// видаляємо стартове вікно
                startWindow = null;									// створюємо об'єкт, що відповідатиме стартовому вікну
                level = new Level(state);							// завантажуємо новий рівень
                root.getChildren().add(level);						// додаємо його на панель
                level.start();										// починаємо завантаження рівня
            }
        }
        
        public void startGame () {									// початок гри
        	
            lifeCount = 4;											// початкова кількість життів рівна 4
            score = 0;												// початковий рахунок рівний 0
            changeExecutionState(1);								// перехід в стан відображення першого рівня
        }
    }  
    
    public static void main(String[] args) {
    	
        launch(args);
    }
}