package breakout;

import java.util.Arrays;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class LevelData {

    private static final String NEXT_LEVEL = "-->";

    private static final String[] LEVELS_DATA = new String[] {				// �������� ������ ����

    	"",																	// ������ �����
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
        
        "",																	// ������ �����
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
    	
    	"",																	// ����� �����
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

        "",																	// ��������� �����
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

         "",																// �'���� �����
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

    	"",																	// ������ �����
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

         "R O Y W G B C M",													// ������ �����
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
         
         "",																// ������� �����
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

    private static ObservableList<Integer> levelsBounds;					// ������ � ������ ����
    
    private static void initializeLevelsBounds() {							// ����������� ��� ����
    	
        if (levelsBounds == null) {											// ���� ��� �� �� ��������������
            levelsBounds = FXCollections.<Integer>observableArrayList(); 	// �������� ������ ��� ������ ��� ����
            levelsBounds.add(0);											// ������� ������� ������� ����
            
            for (int i = 0; i < LEVELS_DATA.length; i++) {					// ��������� ��� ����
                if (LEVELS_DATA[i].equals(NEXT_LEVEL)) {					// ���� ��������� ���� ����
                    levelsBounds.add(i);									// �������� ������� ���������� ��������� ���� (NEXT_LEVEL) 
                }
            }
            
            levelsBounds.add(LEVELS_DATA.length + 1);						// ������� ���������� ���������� ����
        }
    }	

    public static int getLevelsCount() {									// ��������� ������� ����
    	
    	initializeLevelsBounds();											// ����������� ��� ����
        
        return levelsBounds.size() - 1;										// ���������� ������� ����
    }

    public static String[] getLevelData(int level) {						// ��������� ���������� ��� �����
    	
    	initializeLevelsBounds();											// ����������� ���� (���� �������) (!!!) � ����� ��?
        
        if (level < 1 || level > getLevelsCount()) {						// ���� �������� �������� ������ �� 1 ��� ������ �� ������ ������� ���� (���������� ������� ���) 
            return null;													// �������� ������� 䳿
        } 
        
        else {
        	// ������ ��������� ����� � ��������� ������� ����: ���� ������� ����� � ���� ������, ������� ���� ��� ��������� �� ���� �����
            return Arrays.copyOfRange(LEVELS_DATA, levelsBounds.get(level - 1) + 1, levelsBounds.get(level));
        }
    }
}