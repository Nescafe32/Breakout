package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;

public class StartWindow extends Parent {
    
    private enum States {												// ����� ������������ ���������
    	SHOW_BREAK_OUT, SHOW_FIREBALL, SHOW_PREESANYKEY
    }
    
    private ImageView background, break_, out, fireBall, pressanykey;	// ���������� ���� �� ��������� �'���, ������� "BREAK", "OUT", "PRESS ANY KEY"
    
    private Timeline timeline;											// ��� ����������� ������� �� �������� ������
    
    private int state, stateArg;										// ����� ����� �� ������� ��������� �������� �����
    
    StartWindow() {
    	
        state = States.SHOW_BREAK_OUT.ordinal();						// ���������� ���� - ���������� ���� BREAK OUT
        stateArg = 0;
        initTimeline();													// ����������� ������� ���������� ����
        
        background = new ImageView();									// ����������� ���������� ����
        background.setFocusTraversable(true);							// ������������ ������ �� ������� ���� (�������� ����)
        background.setImage(GameSettings.getOtherImages().get(GameSettings.OtherImages.BACKGROUND_IMAGE.ordinal()));	// ������������ ���������� ����
        background.setFitWidth(GameSettings.SCREEN_WIDTH);				// ������������ ������ ������ ���������� ���� ����� ������
        background.setFitHeight(GameSettings.SCREEN_HEIGHT);			// ������������ ������ ������� ���������� ���� ����� ������
        
        background.setOnMousePressed(event-> {							// ������������ ��������� ���� ��� ���� ���������� ����-��� ������ ����
        	Main.getMainFrame().startGame();
        });
        
        background.setOnKeyPressed(event -> { 							// ������������ ��������� ���� ��� ���� ���������� ����-��� ������ �� ��������
        	Main.getMainFrame().startGame();
        });
        
        // ---------------------------------------------------------------------------------------------------------------
        
        break_ = new ImageView();										// ��� ����������� ���������� "BREAK"
        break_.setImage(GameSettings.getStartWindowImages().get(GameSettings.StartWindowImages.BRICK_IMAGE.ordinal()));		// ������������ �������� "BREAK"
        break_.setTranslateY(break_.getImage().getHeight() + 50);		// ������� �������� �� �� Y ������� �� ������
        
        out = new ImageView();											// ��� ����������� ���������� "OUT"
        out.setImage(GameSettings.getStartWindowImages().get(GameSettings.StartWindowImages.BREAKER_IMAGE.ordinal())); 	// ������������ �������� "OUT"
        out.setTranslateY(break_.getTranslateY() + break_.getImage().getHeight() * 5 / 4); // ������� �������� �� �� Y ������� �� ������ �� ������ �������� "BREAK"
        
        fireBall = new ImageView();										// ��� ����������� ���������� "FIREBALL"
        fireBall.setImage(GameSettings.getStartWindowImages().get(GameSettings.StartWindowImages.FIREBALL_IMAGE.ordinal())); // ������������ �������� "FIREBALL"
        fireBall.setTranslateY(out.getTranslateY() -
                (fireBall.getImage().getHeight() - out.getImage().getHeight()) / 2); // ������� �������� �� �� Y ������� �� ������ �� ������ �������� "BREAK"
        fireBall.setVisible(false);										// ��������� �� �������� ��������
        
        double y = out.getTranslateY() + out.getImage().getHeight(); 	// ��� ���������� ��������� ������� "PRESS ANY KEY"
        pressanykey = new ImageView();									// ��� ����������� ���������� "PRESS ANY LEY"
        pressanykey.setImage(GameSettings.getStartWindowImages().get(GameSettings.StartWindowImages.PRESSANYKEY_IMAGE.ordinal())); // ������������ �������� "PRESS ANY KEY"
        pressanykey.setTranslateX((GameSettings.SCREEN_WIDTH - pressanykey.getImage().getWidth()) / 2); // ������� �������� �� �� X ������� �� ������ �� ������ ������
        pressanykey.setTranslateY(y + (GameSettings.SCREEN_HEIGHT - y) / 2);  // ������� �������� �� �� Y ������� ������ �������� "OUT" �� ������ ������
        pressanykey.setOpacity(0); // ����������� ������� � ����������: ���� ��������� ������� �������� "BREAK", "OUT" �� "FIREBALL"

        // ---------------------------------------------------------------------------------------------------------------
        
        Group group = new Group();
        group.getChildren().add(background);				// ������� ���� ���������� ����
        group.getChildren().addAll(break_, out, fireBall, pressanykey); // ������� ��������� �� �������, �� ������ ��������� ��������� � ���������� ���
        getChildren().add(group);							// �� ��������� �� �� ������
    }

    private void initTimeline() {							// ����������� ������� ���������� ����
    	
        timeline = new Timeline();							// ������������� ������� �� �������� ������
        timeline.setCycleCount(Timeline.INDEFINITE); 		// ������ ������� ���������� ������� ����
        
        KeyFrame kf = new KeyFrame(GameSettings.ANIMATION_TIME, event -> { // ��� ������������� �������� �����
            
    		if (state == States.SHOW_BREAK_OUT.ordinal()) {					// ���� �� � ������� ���� - ��������� ������������ "BREAK OUT"
                stateArg++;
                int center = GameSettings.SCREEN_WIDTH / 2;
                int offset = (int)(Math.cos(stateArg / 4.0) * (40 - stateArg) / 40 * center);
                break_.setTranslateX(center - break_.getImage().getWidth() / 2 + offset);
                out.setTranslateX(center - out.getImage().getWidth() / 2 - offset);
                if (stateArg == 40) {
                    stateArg = 0;
                    state = States.SHOW_FIREBALL.ordinal();				// ������� � ���� ����������� ����������� ���������� "FIREBALL"
                }
                
                return;
            }
    		
            if (state == States.SHOW_FIREBALL.ordinal()) {				// ���� �� � ������� ���� - ��������� ���������� "FIREBALL"
            	
                if (stateArg == 0) {
                	fireBall.setTranslateX(break_.getTranslateX() + out.getImage().getWidth());
                	fireBall.setScaleX(0);
                	fireBall.setScaleY(0);
                	fireBall.setVisible(true);
                }
                
                stateArg++;
                double coef = stateArg / 30f;
                out.setTranslateX(break_.getTranslateX() +
                        (break_.getImage().getWidth() - out.getImage().getWidth()) / 2f * (1 - coef));
                fireBall.setScaleX(coef);
                fireBall.setScaleY(coef);
                fireBall.setRotate((30 - stateArg) * 2);
                
                if (stateArg == 30) {
                    stateArg = 0;
                    state = States.SHOW_PREESANYKEY.ordinal();		// ������� � ���� ����������� ����������� ���������� "PRESS ANY KEY"
                }
                
                return;
            }
            
            if (pressanykey.getOpacity() < 1) {						// ���� �� � �������� ���� - ��������� ���������� "PRESS ANY KEY"
                pressanykey.setOpacity(pressanykey.getOpacity() + 0.05f);	// � ����� �������� ����������� (����������)
            }
            
            stateArg--;       
        });
        
        timeline.getKeyFrames().add(kf);					// ��������� ��������� ����� � ������ �������� ����� �������
    }
    
    public void start () {									// ������� ����������� ������� ���������� ����
    	
        background.requestFocus();							// ��������� ������ �� ������� ���� (�������� ����)
        timeline.play();									// ������� ����������� �������
    }

    public void stop() { 									// ������� ������� � �� ���������� � ��������� �������
    	
        timeline.stop();
    }
}