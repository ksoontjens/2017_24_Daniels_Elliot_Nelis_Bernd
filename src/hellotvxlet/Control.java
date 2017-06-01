/**
 * @author Bernd Nelis and Elliot Daniels
 */
package hellotvxlet;

//////////////////////////IMPORT SECTION////////////////////////////

import java.awt.Color;
import org.havi.ui.HScene;

//////////////////////////CLASS////////////////////////////////

public class Control
{
    
//////////////////////////VARIABLES//////////////////////////// 
    
    protected ButtonComponent[] buttons;
    protected Color neutralColor = Color.RED;
    protected Color colorCorrectColor = Color.ORANGE;
    protected Color fullCorrectColor = Color.GREEN;

//////////////////////////GRAPHIC CONTROL BUTTON////////////////////////////
// Makes the buttons for the control object wich can be wrong, almost correct or correct.
    
    public Control(int x, int y, HScene scene)
    {
        this.buttons = new ButtonComponent[4];
        this.buttons[0] = new ButtonComponent(28, x, y, scene);
        this.buttons[1] = new ButtonComponent(28, x+32, y, scene);
        this.buttons[2] = new ButtonComponent(28, x, y+32, scene);
        this.buttons[3] = new ButtonComponent(28, x+32, y+32, scene);
        
        this.makeNeutral(scene);
    }

//////////////////////////SHOW CONTROL////////////////////////////
// This methods takes data and fills in the graphic button with the correct data.
    
    public void setFeedback(int colorCorrect, int fullCorrect, HScene scene)
    {
        for(int button = 0; button < colorCorrect; button++)
        {
            this.buttons[button].changeColor(colorCorrectColor ,scene);
        }
        
        for(int button = colorCorrect; button < (fullCorrect+colorCorrect); button++)
        {
            this.buttons[button].changeColor(fullCorrectColor ,scene);
        }
    }
    
//////////////////////////GRAPHIC CONTROL BUTTON////////////////////////////
// This method resets the graphics of the controlbutton.
   
    public void makeNeutral(HScene scene)
    {
        for(int button = 0; button < 4; button++)
        {
            this.buttons[button].changeColor(neutralColor ,scene);
        }
    }
}