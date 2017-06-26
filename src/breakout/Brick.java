package breakout;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Brick extends Parent {

    private int brickType;															// тип цеглини
    private ImageView brickImageView;												// відображення зображення цеглини

    public Brick (int type) {														// початкова ініціалізація параметрів цеглини
    	
    	brickImageView = new ImageView();											// для відображення зображення цеглини
        getChildren().add(brickImageView);											// додавання зображення цеглини на панель
        
        choiceBrickType(type);														// встановлення типу (зображення) цеглини
    }

    public int getType () {															// отримання типу цеглини
    	
        return brickType;
    }

    public boolean ifBrickCanBeKicked () {											// перевірка чи цеглина може бути знижена
    	
        if (brickType == GameSettings.BrickTypes.GREY_BRICK.ordinal()) {			// якщо потрапили в кам'яну цеглину
            return false;															// з цеглиною нічого не відбувається (вона не знижується)
        }
        
        if (brickType == GameSettings.BrickTypes.BROKEN1_BRICK.ordinal()) {		// якщо потрапили в цеглину, що "надбивається"				
            choiceBrickType(GameSettings.BrickTypes.BROKEN2_BRICK.ordinal());		// цеглина стає "надбитою"
            return false;															// але не знищується
        }
        
        return true;																// інакше цеглина знищується
    }

    private void choiceBrickType (int brickType) {									// вибір та встановлення типу (зображення) цеглини
    	
        this.brickType = brickType;													// встановлення типу цеглини
        Image image = GameSettings.getBricksImages().get(brickType);				// вибір зображення цеглини по її типу
        brickImageView.setImage(image);												// встановлення зображення на цеглину
        brickImageView.setFitWidth(GameSettings.FIELD_WIDTH / 15);					// встановлення кінцевої ширини цеглини (ділимо на 15, оскільки в ряду знаходитиметься до 15 цеглин)
    }

    public static int getBrickType (String s) { 									// визначення типу (кольору) цеглини
    	
    	switch (s) {
	    	case "L": return GameSettings.BrickTypes.BLUE_BRICK.ordinal();			// синя цеглина
	    	case "2": return GameSettings.BrickTypes.BROKEN1_BRICK.ordinal(); 		// цеглина, що "надбивається"
	    	case "B": return GameSettings.BrickTypes.BROWN_BRICK.ordinal();		// коричнева цеглина
	    	case "C": return GameSettings.BrickTypes.CYAN_BRICK.ordinal();			// блакитна цеглина
	    	case "G": return GameSettings.BrickTypes.GREEN_BRICK.ordinal();		// зелена цеглина
	    	case "0": return GameSettings.BrickTypes.GREY_BRICK.ordinal();			// сіра (кам'яна == незнищувана) цеглина
	    	case "M": return GameSettings.BrickTypes.MAGENTA_BRICK.ordinal();		// пурпурна цеглина
	    	case "O": return GameSettings.BrickTypes.ORANGE_BRICK.ordinal();		// оранжева цеглина
	    	case "R": return GameSettings.BrickTypes.RED_BRICK.ordinal();			// червона цеглина
	    	case "V": return GameSettings.BrickTypes.VIOLET_BRICK.ordinal();		// фіолетова цеглина
	    	case "W": return GameSettings.BrickTypes.WHITE_BRICK.ordinal();		// біла цеглина
	    	case "Y": return GameSettings.BrickTypes.YELLOW_BRICK.ordinal();		// жовта цеглина
	    	default: System.out.println("Невідомий тип цеглини '{s}'");
	    			 return GameSettings.BrickTypes.WHITE_BRICK.ordinal();			// якщо при складанні рівня помилились з позначенням типу (кольору) цеглини, вона буде білою
    	}
    }
}