package breakout;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bonus extends Parent {

    public static final String[] BONUS_NAMES = new String[] {					// ����� ������
        "��������� �������Ҳ �'���", "���������� �������Ҳ �'���",
        "��������� ���̲�� �����", "���������� ���̲�� �����",
        "��������� ���̲�� �'���", "���������� ���̲�� �'���",
        "���������� �'��� �� �����",
        "\"��������\" �'��",
        "��������� �����",
    };

    private int bonusType;														// ��� ������
    private int bonusWidth;														// ������ ������
    private int bonusHeight;													// ������ ������
    private ImageView bonusImageView;											// ����������� ���������� ������
    
    public Bonus(int type) {													// ��������� ����������� ��������� ������
    	
    	bonusImageView = new ImageView();										// ����������� ���������� ������
        getChildren().add(bonusImageView);										// �� ���� ��������� �� ������
        
        
        this.bonusType = type;													// ������������ ���� ������
        Image image = GameSettings.getBonusesImages().get(type);				// ��������� ���������� ������
        
        bonusWidth = (int)image.getWidth() - GameSettings.SHADOW_WIDTH;		// ������ ������
        bonusHeight = (int)image.getHeight() - GameSettings.SHADOW_HEIGHT;		// ������ ������
        
        bonusImageView.setImage(image);											// ������������ ���������� ������
    }
    
    public int getBonusType() {													// ��������� ���� ������
    	
        return bonusType;
    }
    
    public int getBonusWidth() {												// ��������� ������ ������
    	
        return bonusWidth;
    }

    public int getBonusHeight() {												// ��������� ������ ������
    	
        return bonusHeight;
    }
}