package breakout;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static MainFrame mainFrame;								// ���� �������� (�������� ��� ������)

    public static MainFrame getMainFrame () {						// ���������� ���� ��������
    	
        return mainFrame;
    }
    
    @Override 
    public void start (Stage stage) {								// �������� �����, �� ������� �� ������ ��� 
    	
        GameSettings.initializeAllImagesLists();					// ����������� ��� ���������
        
        Group root = new Group();
        mainFrame = new MainFrame(root);
        
        stage.setTitle("BREAKOUT RR");								// ��������� ����
        stage.setResizable(false);									// �������� ���� ������ ����
        stage.setWidth(GameSettings.SCREEN_WIDTH + 2 * GameSettings.WINDOW_BORDER);	// ������ ����
        stage.setHeight(GameSettings.SCREEN_HEIGHT+ 2 * GameSettings.WINDOW_BORDER);	// ������ ����
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        mainFrame.changeExecutionState(MainFrame.START_WINDOW);		// ������������ ���������� ���� ��������
        
        stage.getIcons().add(new Image("breakout/other/logo.png"));	// ������������ ������ �����
        stage.show();												// ����������� �����
    }

    public class MainFrame {

        private Group root;

        private StartWindow startWindow;							// ��������� ���������� ����

        private Level level;										// ���������� ���� (�������� ����)

        private int lifeCount; 										// ������� �����
        private int score; 											// �������� �������

        private MainFrame (Group root) {
        	
            this.root = root;
        }

        public int getExecutionState() {							// ��������� ����� ��� (�������� ���� -> ������ ����� -> ������ ����� -> ...)
        	
            return state;
        }

        public int getScore () {									// ��������� �������
        	
            return score;
        }

        public void setScore (int score) {							// ������������ �������
        	
            this.score = score;
        }

        public int getLifeCount () {								// ��������� ������� �����
        	
            return lifeCount;
        }

        public void increaseLivesCount () {							// ��������� ������� �����
        	
            lifeCount = Math.min(lifeCount + 1, GameSettings.MAX_LIVES);
        }
        
        public void decreaseLivesCount () {							// ��������� ������� �����
        	
            lifeCount--;
        }

        public static final int START_WINDOW = 0;
        private int state = START_WINDOW;							// ���� ������������ ���������� ����

        public void changeExecutionState (int newState) {			// ���� ����� ��������� (����)
        	
            this.state = newState;									// ������������ ������ ����� ���������
            
            if (startWindow != null) {								// ���� ������� ������� � ���������� ���� �� ������
            	startWindow.stop();									// ���������� ����������� ������� ���������� ����
            }
            
            if (level != null) {									// ���� ������������ ��� (���������� ��� ��� ���������� ������� �� �����)
                level.stop();										// ���������� ���������� ������� �������� ����
            }
            
            if (state < 1 || state > LevelData.getLevelsCount()) {	// ���� �� � ���� ����������� ���������� ���� ��� ��� ���������
                root.getChildren().remove(level);					// ��������� ����� � �����
                level = null;										// ��������� ��'���, �� ����������� ����
                startWindow = new StartWindow();					// ��������� �������� ����
                root.getChildren().add(startWindow);				// ������ ���� �� ������
                startWindow.start();								// �������� ������������ ���������� ����
            } 
            
            else {													// ���� �� � �������� ����
                root.getChildren().remove(startWindow);				// ��������� �������� ����
                startWindow = null;									// ��������� ��'���, �� ����������� ���������� ����
                level = new Level(state);							// ����������� ����� �����
                root.getChildren().add(level);						// ������ ���� �� ������
                level.start();										// �������� ������������ ����
            }
        }
        
        public void startGame () {									// ������� ���
        	
            lifeCount = 4;											// ��������� ������� ����� ���� 4
            score = 0;												// ���������� ������� ����� 0
            changeExecutionState(1);								// ������� � ���� ����������� ������� ����
        }
    }  
    
    public static void main(String[] args) {
    	
        launch(args);
    }
}