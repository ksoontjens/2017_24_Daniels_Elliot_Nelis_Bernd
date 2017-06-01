/**
 * @author Bernd Nelis and Elliot Daniels
 */
package hellotvxlet;

//////////////////////////IMPORT SECTION////////////////////////////
import java.awt.Color;
import java.awt.Font;
import org.havi.ui.HComponent;
import org.havi.ui.HScene;
import org.havi.ui.HTextButton;
import org.havi.ui.HVisible;

//////////////////////////CLASS////////////////////////////////

public class ButtonComponent extends HComponent{
  
//////////////////////////VARIABLES////////////////////////////
    
    private HTextButton button;
    private Color color;
    private int grootte = 60;
    
///////////////POSSIBLE COLOR BUTTON GENERATOR/////////////// 
    

     public ButtonComponent(Color color, int x, int y, String name, Font font, HScene scene)
    {
        this.button=new HTextButton(name, x, y, grootte, grootte);
        this.button.setBackgroundMode(HVisible.BACKGROUND_FILL);
        this.button.setBackground(color);
        this.color = color;
        this.button.setFont(font);
        this.button.setBordersEnabled(false);
        
        scene.add(button);
    }
       
//////////////////////////SCORE BUTTON ////////////////////////////

    public ButtonComponent(Color color, int x, int y, String name, HScene scene, int sizeX, int sizeY)
    {
        this.button=new HTextButton(name, x, y, sizeX, sizeY);
        this.button.setBackgroundMode(HVisible.BACKGROUND_FILL);
        this.button.setBackground(color);
        this.button.setBordersEnabled(false);
        this.button.setForeground(Color.WHITE); 
       
        scene.add(button);
    }
    
//////////////////////////PLAYERS PICK BUTTON /////////////////////
    
    public ButtonComponent(int size, int x, int y, HScene scene) 
    {
        this.button=new HTextButton("", x, y, size, size);
        this.button.setBackgroundMode(HVisible.BACKGROUND_FILL);
        this.button.setBackground(Color.WHITE);
        this.color = Color.WHITE;
        this.button.setBordersEnabled(false);
        
        scene.add(button);
    }

//////////////////////////PLAYERS COLOR BUTTON/////////////////////
    
    public ButtonComponent(Color color, int x, int size, HScene scene)
    {
        this.button=new HTextButton("", x, 5, size, size);
        this.button.setBackgroundMode(HVisible.BACKGROUND_FILL);
        this.button.setBackground(color);
        this.button.setBordersEnabled(false);
        this.color = color;
        
        scene.add(button);
    }
    
//////////////////////////RETURN SELECTED BUTTON////////////////////////////    
    public HTextButton getButton()
    {
        return this.button;
    }
    
//////////////////////////RETURN SELECTED COLOR//////////////////////////// 
    
    public Color getColor()
    {
        return this.color;
    }
    
//////////////////////////CHANGE COLOR OF SQUARES////////////////////////////
    
    public void changeColor (Color color, HScene scene)
    {
        this.button.setBackground(color);
        scene.repaint();
        this.color = color;
    }

//////////////////////////CHANGE POSITON LEFT RIGHT////////////////////////////
    
    public void changeX(int x, HScene scene)
    {
        this.button.setLocation(x, 155);
        scene.repaint();
    }

//////////////////////////CHANGE POSITION LEFT RIGHT////////////////////////////    
    
    public void changePos(int x, int y, HScene scene) {
        this.button.setLocation(x, y);
        scene.repaint();
    }

//////////////////////////TEXT CHANGER//////////////////////////// 

    public void changeText(String newText, Color color, HScene scene)
    {
        this.button.setTextContent(newText, this.button.NORMAL_STATE);
        this.button.setForeground(color);     
        scene.repaint();
    }

//////////////////////////DUPLICATE CHECKER//////////////////////////// 

    public boolean isWhite()
    {
        return this.color == Color.WHITE;
    }
}