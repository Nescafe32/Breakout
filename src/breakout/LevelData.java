package breakout;

import java.util.Arrays;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class LevelData {

    private static final String NEXT_LEVEL = "-->";

    private static final String[] LEVELS_DATA = new String[] {				// тестовий список рівнів

    	"",																	// перший рівень
    	"",
    	"",
    	" R   O   C   M ",
        " R  O    CV  M ",
        " RYY     C V M ",
        " R  O    C  VM ",
        " R   O   C   M ",
        "",
        "",
        "000000GGG000000",
        "YYYYYY222YYYYYY",
        "",
        "",
        " B   R   VOOOW ",
        " B   R   V   W ",
        " B   R   V   W ",
        " BLLLR   V   W ",
        "     R   V   W ",
        "     R   V   W ",
        "     R   VCCCW ",

        NEXT_LEVEL,
        
        "",																	// другий рівень
        "",
        "",
        "    Y     Y    ",
        "    Y     Y    ",
        "     Y   Y     ",
        "     Y   Y     ",
        "    2222222    ",
        "   222222222   ",
        "   22R222R22   ",
        "  222R222R222  ",
        " 2222222222222 ",
        " 2222222222222 ",
        " 2222222222222 ",
        " 2 222222222 2 ",
        " 2 2       2 2 ",
        " 2 2       2 2 ",
        "    222 222    ",
        "    222 222    ",

        NEXT_LEVEL,
    	
    	"",																	// третій рівень
        "",
        "",
        "",
        "",
        "      YY       ",
        "     CYYCC     ",
        "    CCCCCCC    ",
        "    CCCCCCC    ",
        "    YYYYYYY    ",
        "    YMMMMMY    ",
        "    MM0MM0M    ",
        "    MMMMMMM    ",
        "    MMMWWMM    ",
        "   RRMMMMMRR   ",
        "   YRRRRRRRY   ",
        "  GRRRRRRRRRG  ",
        " GGRRRRRRRRRGG ",
        "GGGBBBBBBBBBGGG",
        

        NEXT_LEVEL,

        "",																	// четвертий рівень
        "",
        "",
        "  R YYY G G RRR",
        "  R Y Y G G R R",
        "  R Y Y G G R R",
        "  R YYY G G RRR",
        "R R Y Y G G R R",
        "R R Y Y G G R R",
        " R  Y Y  G  R R",
        "",
        "",
        "",
        "    222 2 2    ",
        "    2   2 2    ",
        "    2   2 2    ",
        "    222  2     ",
        "    2   2 2    ",
        "    2   2 2    ",
        "    2   2 2    ",

         NEXT_LEVEL,

         "",																// п'ятий рівень
         "",
         "000000000000000",
         "",
         "",
         "0 222 222 222 0",
         "0 2R2 2R2 2R2 0",
         "0 222 222 222 0",
         "",
         "0 222 222 222 0",
         "0 2Y2 2Y2 2Y2 0",
         "0 222 222 222 0",
         "",
         "0 222 222 222 0",
         "0 2G2 2G2 2G2 0",
         "0 222 222 222 0",
         "",
         "",
         "BBBBBBBBBBBBBBB",

         NEXT_LEVEL,

    	"",																	// шостий рівень
        "222222222222222",
        "2WWWWWWWWWWWWW2",
        "2WRRRRRRRRRRRW2",
        "2WRWWWWWWWWWRW2",
        "2WRWRRRRRRRWRW2",
        "2WRWR22222RWRW2",
        "2WRWR22222RWRW2",
        "2WRWR22222RWRW2",
        "2WRWR22222RWRW2",
        "2WRWR22222RWRW2",
        "2WRWR22222RWRW2",
        "2WRWR22222RWRW2",
        "2WRWR22222RWRW2",
        "2WRWR22222RWRW2",
        "2WRWR22222RWRW2",
        "2WRWRRRRRRRWRW2",
        "2WRWWWWWWWWWRW2",
        "2WRRRRRRRRRRRW2",
        "2WWWWWWWWWWWWW2",
        "222222222222222",

         NEXT_LEVEL,

         "R O Y W G B C M",													// сьомий рівень
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "R O Y W G B C M",
         "2 2 2 2 2 2 2 2",

         NEXT_LEVEL,
         
         "",																// восьмий рівень
         "",
         "0000       0000",
         "0222       2220",
         "0222       2220",
         "0222GGGGGGG2220",
         "0222GRRRRRG2220",
         "0WWYGBBBBBGYWW0",
         "0WWYGBBBBBGYWW0",
         "0WWYGCCCCCGYWW0",
         "    GRRRRRG    ",
         "0WWYGCCCCCGYWW0",
         "0WWYGBBBBBGYWW0",
         "0WWYGBBBBBGYWW0",
         "0222GRRRRRG2220",
         "0222GGGGGGG2220",
         "0222       2220",
         "0222       2220",
         "0000       0000",
    };

    private static ObservableList<Integer> levelsBounds;					// список з межами рівнів
    
    private static void initializeLevelsBounds() {							// ініціалізація меж рівнів
    	
        if (levelsBounds == null) {											// якщо рівні ще не проініціалізовано
            levelsBounds = FXCollections.<Integer>observableArrayList(); 	// ініціюємо список для запису меж рівнів
            levelsBounds.add(0);											// позиція початку першого рівня
            
            for (int i = 0; i < LEVELS_DATA.length; i++) {					// визначаємо межі рівнів
                if (LEVELS_DATA[i].equals(NEXT_LEVEL)) {					// якщо досягнуто кінця рівня
                    levelsBounds.add(i);									// записуємо позицію завершення поточного рівня (NEXT_LEVEL) 
                }
            }
            
            levelsBounds.add(LEVELS_DATA.length + 1);						// позиція завершення останнього рівня
        }
    }	

    public static int getLevelsCount() {									// отримання кількості рівнів
    	
    	initializeLevelsBounds();											// ініціалізація меж рівнів
        
        return levelsBounds.size() - 1;										// повернення кількості рівнів
    }

    public static String[] getLevelData(int level) {						// отримання інформації про рівень
    	
    	initializeLevelsBounds();											// ініціалізація рівнів (якщо потрібно) (!!!) а нужно ли?
        
        if (level < 1 || level > getLevelsCount()) {						// якщо передано параметр менший за 1 або більший за наявну кількість рівнів (користувач пройшов гру) 
            return null;													// виконуємо відповідні дії
        } 
        
        else {
        	// інакше повертаємо масив з текстовим складом рівня: маємо вхідний масив з усіма рівнями, початок рівня для копіювання та його кінець
            return Arrays.copyOfRange(LEVELS_DATA, levelsBounds.get(level - 1) + 1, levelsBounds.get(level));
        }
    }
}