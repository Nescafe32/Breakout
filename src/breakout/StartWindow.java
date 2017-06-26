package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;

public class StartWindow extends Parent {
    
    private enum States {												// стани завантаження зображень
    	SHOW_BREAK_OUT, SHOW_FIREBALL, SHOW_PREESANYKEY
    }
    
    private ImageView background, break_, out, fireBall, pressanykey;	// зображення фону та вогняного м'яча, надписів "BREAK", "OUT", "PRESS ANY KEY"
    
    private Timeline timeline;											// для відображення анімації по ключовим кадрам
    
    private int state, stateArg;										// номер стану та кількість пройдений ключових кадрів
    
    StartWindow() {
    	
        state = States.SHOW_BREAK_OUT.ordinal();						// початковий стан - анімаційний вивід BREAK OUT
        stateArg = 0;
        initTimeline();													// ініціалізація анімації стартового вікна
        
        background = new ImageView();									// відображення зображення фону
        background.setFocusTraversable(true);							// встановлення фокусу на основне поле (картинка фону)
        background.setImage(GameSettings.getOtherImages().get(GameSettings.OtherImages.BACKGROUND_IMAGE.ordinal()));	// встановлення зображення фону
        background.setFitWidth(GameSettings.SCREEN_WIDTH);				// встановлення кінцевої ширини зображення рівної ширині екрану
        background.setFitHeight(GameSettings.SCREEN_HEIGHT);			// встановлення кінцевої висотии зображення рівної висоті екрану
        
        background.setOnMousePressed(event-> {							// завантаження основного вікна гри після натиснення будь-якої клавіші миші
        	Main.getMainFrame().startGame();
        });
        
        background.setOnKeyPressed(event -> { 							// завантаження основного вікна гри після натиснення будь-якої клавіші на клавіатурі
        	Main.getMainFrame().startGame();
        });
        
        // ---------------------------------------------------------------------------------------------------------------
        
        break_ = new ImageView();										// для відображення зображення "BREAK"
        break_.setImage(GameSettings.getStartWindowImages().get(GameSettings.StartWindowImages.BRICK_IMAGE.ordinal()));		// встановлення картинки "BREAK"
        break_.setTranslateY(break_.getImage().getHeight() + 50);		// зміщення картинки по осі Y відносно її висоти
        
        out = new ImageView();											// для відображення зображення "OUT"
        out.setImage(GameSettings.getStartWindowImages().get(GameSettings.StartWindowImages.BREAKER_IMAGE.ordinal())); 	// встановлення картинки "OUT"
        out.setTranslateY(break_.getTranslateY() + break_.getImage().getHeight() * 5 / 4); // зміщення картинки по осі Y відносно її висоти та висоти картинки "BREAK"
        
        fireBall = new ImageView();										// для відображення зображення "FIREBALL"
        fireBall.setImage(GameSettings.getStartWindowImages().get(GameSettings.StartWindowImages.FIREBALL_IMAGE.ordinal())); // встановлення картинки "FIREBALL"
        fireBall.setTranslateY(out.getTranslateY() -
                (fireBall.getImage().getHeight() - out.getImage().getHeight()) / 2); // зміщення картинки по осі Y відносно її висоти та висоти картинки "BREAK"
        fireBall.setVisible(false);										// початково ця картинка невидима
        
        double y = out.getTranslateY() + out.getImage().getHeight(); 	// для визначення положення надпису "PRESS ANY KEY"
        pressanykey = new ImageView();									// для відображення зображення "PRESS ANY LEY"
        pressanykey.setImage(GameSettings.getStartWindowImages().get(GameSettings.StartWindowImages.PRESSANYKEY_IMAGE.ordinal())); // встановлення картинки "PRESS ANY KEY"
        pressanykey.setTranslateX((GameSettings.SCREEN_WIDTH - pressanykey.getImage().getWidth()) / 2); // зміщення картинки по осі X відносно її ширини та ширини екрану
        pressanykey.setTranslateY(y + (GameSettings.SCREEN_HEIGHT - y) / 2);  // зміщення картинки по осі Y відносно висоти картинки "OUT" та висоти екрану
        pressanykey.setOpacity(0); // відображення малюнку з запізненням: після закінчення анімації картинок "BREAK", "OUT" та "FIREBALL"

        // ---------------------------------------------------------------------------------------------------------------
        
        Group group = new Group();
        group.getChildren().add(background);				// задання фону стартового вікна
        group.getChildren().addAll(break_, out, fireBall, pressanykey); // задання зображень на надписів, що будуть анімаційно відображені у стартовому вікні
        getChildren().add(group);							// та додавання їх на панель
    }

    private void initTimeline() {							// ініціалізація анімації стартового вікна
    	
        timeline = new Timeline();							// представлення анімації по ключовим кадрам
        timeline.setCycleCount(Timeline.INDEFINITE); 		// повтор анімації нескінченну кількість разів
        
        KeyFrame kf = new KeyFrame(GameSettings.ANIMATION_TIME, event -> { // для представлення ключових кадрів
            
    		if (state == States.SHOW_BREAK_OUT.ordinal()) {					// якщо ми в першому стані - анімаційно відображається "BREAK OUT"
                stateArg++;
                int center = GameSettings.SCREEN_WIDTH / 2;
                int offset = (int)(Math.cos(stateArg / 4.0) * (40 - stateArg) / 40 * center);
                break_.setTranslateX(center - break_.getImage().getWidth() / 2 + offset);
                out.setTranslateX(center - out.getImage().getWidth() / 2 - offset);
                if (stateArg == 40) {
                    stateArg = 0;
                    state = States.SHOW_FIREBALL.ordinal();				// перехід в стан анімаційного відображення зображення "FIREBALL"
                }
                
                return;
            }
    		
            if (state == States.SHOW_FIREBALL.ordinal()) {				// якщо ми в другому стані - анімаційно відображаємо "FIREBALL"
            	
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
                    state = States.SHOW_PREESANYKEY.ordinal();		// перехід в стан анімаційного відображення зображення "PRESS ANY KEY"
                }
                
                return;
            }
            
            if (pressanykey.getOpacity() < 1) {						// якщо ми в третьому стані - анімаційно відображаємо "PRESS ANY KEY"
                pressanykey.setOpacity(pressanykey.getOpacity() + 0.05f);	// з такою швидкістю відображення (вспливання)
            }
            
            stateArg--;       
        });
        
        timeline.getKeyFrames().add(kf);					// додавання ключового кадру в список ключових кадрів анімації
    }
    
    public void start () {									// початок програвання анімації стартового вікна
    	
        background.requestFocus();							// наведення фокусу на основне поле (картинка фону)
        timeline.play();									// початок програвання анімації
    }

    public void stop() { 									// зупинка анімації і її повернення в початкову позицію
    	
        timeline.stop();
    }
}