package breakout;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Brick extends Parent {

    private int brickType;															// ��� �������
    private ImageView brickImageView;												// ����������� ���������� �������

    public Brick (int type) {														// ��������� ����������� ��������� �������
    	
    	brickImageView = new ImageView();											// ��� ����������� ���������� �������
        getChildren().add(brickImageView);											// ��������� ���������� ������� �� ������
        
        choiceBrickType(type);														// ������������ ���� (����������) �������
    }

    public int getType () {															// ��������� ���� �������
    	
        return brickType;
    }

    public boolean ifBrickCanBeKicked () {											// �������� �� ������� ���� ���� �������
    	
        if (brickType == GameSettings.BrickTypes.GREY_BRICK.ordinal()) {			// ���� ��������� � ���'��� �������
            return false;															// � �������� ����� �� ���������� (���� �� ���������)
        }
        
        if (brickType == GameSettings.BrickTypes.BROKEN1_BRICK.ordinal()) {		// ���� ��������� � �������, �� "�����������"				
            choiceBrickType(GameSettings.BrickTypes.BROKEN2_BRICK.ordinal());		// ������� ��� "��������"
            return false;															// ��� �� ���������
        }
        
        return true;																// ������ ������� ���������
    }

    private void choiceBrickType (int brickType) {									// ���� �� ������������ ���� (����������) �������
    	
        this.brickType = brickType;													// ������������ ���� �������
        Image image = GameSettings.getBricksImages().get(brickType);				// ���� ���������� ������� �� �� ����
        brickImageView.setImage(image);												// ������������ ���������� �� �������
        brickImageView.setFitWidth(GameSettings.FIELD_WIDTH / 15);					// ������������ ������ ������ ������� (����� �� 15, ������� � ���� ��������������� �� 15 ������)
    }

    public static int getBrickType (String s) { 									// ���������� ���� (�������) �������
    	
    	switch (s) {
	    	case "L": return GameSettings.BrickTypes.BLUE_BRICK.ordinal();			// ���� �������
	    	case "2": return GameSettings.BrickTypes.BROKEN1_BRICK.ordinal(); 		// �������, �� "�����������"
	    	case "B": return GameSettings.BrickTypes.BROWN_BRICK.ordinal();		// ��������� �������
	    	case "C": return GameSettings.BrickTypes.CYAN_BRICK.ordinal();			// �������� �������
	    	case "G": return GameSettings.BrickTypes.GREEN_BRICK.ordinal();		// ������ �������
	    	case "0": return GameSettings.BrickTypes.GREY_BRICK.ordinal();			// ��� (���'��� == �����������) �������
	    	case "M": return GameSettings.BrickTypes.MAGENTA_BRICK.ordinal();		// �������� �������
	    	case "O": return GameSettings.BrickTypes.ORANGE_BRICK.ordinal();		// �������� �������
	    	case "R": return GameSettings.BrickTypes.RED_BRICK.ordinal();			// ������� �������
	    	case "V": return GameSettings.BrickTypes.VIOLET_BRICK.ordinal();		// ��������� �������
	    	case "W": return GameSettings.BrickTypes.WHITE_BRICK.ordinal();		// ��� �������
	    	case "Y": return GameSettings.BrickTypes.YELLOW_BRICK.ordinal();		// ����� �������
	    	default: System.out.println("�������� ��� ������� '{s}'");
	    			 return GameSettings.BrickTypes.WHITE_BRICK.ordinal();			// ���� ��� �������� ���� ���������� � ����������� ���� (�������) �������, ���� ���� ����
    	}
    }
}