package breakout;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bonus extends Parent {

    public static final String[] BONUS_NAMES = new String[] {					// назви бонусів
        "ЗМЕНШЕННЯ ШВИДКОСТІ М'ЯЧА", "ЗБІЛЬШЕННЯ ШВИДКОСТІ М'ЯЧА",
        "ЗМЕНШЕННЯ РОЗМІРУ ДОШКИ", "ЗБІЛЬШЕННЯ РОЗМІРУ ДОШКИ",
        "ЗМЕНШЕННЯ РОЗМІРУ М'ЯЧА", "ЗБІЛЬШЕННЯ РОЗМІРУ М'ЯЧА",
        "ПРИЛИПАННЯ М'ЯЧА ДО ДОШКИ",
        "\"ВОГНЯНИЙ\" М'ЯЧ",
        "ДОДАТКОВЕ ЖИТТЯ",
    };

    private int bonusType;														// тип бонуса
    private int bonusWidth;														// ширина бонуса
    private int bonusHeight;													// висота бонуса
    private ImageView bonusImageView;											// відображення зображення бонусу
    
    public Bonus(int type) {													// початкова ініціалізація параметрів бонусу
    	
    	bonusImageView = new ImageView();										// відображення зображення бонусу
        getChildren().add(bonusImageView);										// та його додавання на панель
        
        
        this.bonusType = type;													// встановлення типу бонусу
        Image image = GameSettings.getBonusesImages().get(type);				// отримання зображення бонусу
        
        bonusWidth = (int)image.getWidth() - GameSettings.SHADOW_WIDTH;		// ширина бонусу
        bonusHeight = (int)image.getHeight() - GameSettings.SHADOW_HEIGHT;		// висота бонусу
        
        bonusImageView.setImage(image);											// встановлення зображення бонусу
    }
    
    public int getBonusType() {													// отримання типу бонусу
    	
        return bonusType;
    }
    
    public int getBonusWidth() {												// отримання ширини бонусу
    	
        return bonusWidth;
    }

    public int getBonusHeight() {												// отримання висоти бонусу
    	
        return bonusHeight;
    }
}