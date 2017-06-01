/**
 * @author Bernd Nelis and Elliot Daniels
 */
package hellotvxlet;

//////////////////////////IMPORT SECTION////////////////////////////
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.tv.xlet.*;
import java.awt.Font;

import org.davic.resources.ResourceClient;
import org.davic.resources.ResourceProxy;
import org.dvb.event.EventManager;
import org.dvb.event.UserEvent;
import org.dvb.event.UserEventListener;
import org.dvb.event.UserEventRepository;
import org.dvb.ui.DVBColor;
import org.havi.ui.*;

import org.havi.ui.event.HRcEvent;

//////////////////////////CLASS////////////////////////////////

public class HelloTVXlet implements Xlet, UserEventListener, ResourceClient
{
//////////////////////////VARIABLES////////////////////////////
    protected int amountOfColours = 8;
    
    protected HScene scene;
    protected ButtonComponent[] buttons;
    protected int cursor = 0;
    protected int row = 0;
    protected int guessCursor = 0;
    protected Font font = new Font("Arial", Font.ITALIC, 15);
    
    protected int filledIn=0;
    
    protected Control[] feedbacks;
    protected ButtonComponent[][] lives;
    protected HStaticText bgc;
    
    protected Color[] colors;
    protected Color[] randomColours;
    protected String[] colorNames;
    
    protected ButtonComponent highlightButton;
    
    protected HScreen screen;
    protected HBackgroundDevice bgDev;
    protected HBackgroundConfigTemplate bgTemplate;
    protected HStillImageBackgroundConfiguration bgConfig;
 
    protected int victoryCounter = 0;
    protected int defeatCounter = 0;
    
    protected ButtonComponent textVictory;
    protected ButtonComponent textDefeat;
    protected ButtonComponent textNewGame;
    
    protected boolean refuseInput = false;

    
     public HelloTVXlet() {
        
    }
    
//////////////////////////INITIALISING METHOD////////////////////////////
    
    public void initXlet(XletContext context) throws XletStateChangeException
    {
        scene = HSceneFactory.getInstance().getDefaultHScene();
        screen = HScreen.getDefaultHScreen();
        
        bgDev = screen.getDefaultHBackgroundDevice();
        bgTemplate = new HBackgroundConfigTemplate();
        bgTemplate.setPreference(HBackgroundConfigTemplate.STILL_IMAGE, HBackgroundConfigTemplate.REQUIRED);
        bgConfig = (HStillImageBackgroundConfiguration) bgDev.getBestConfiguration(bgTemplate);
        HSceneTemplate sceneTemplate = new HSceneTemplate();
        sceneTemplate.setPreference(HSceneTemplate.SCENE_SCREEN_DIMENSION,new HScreenDimension(1.0f, 1.0f), HSceneTemplate.REQUIRED);
        sceneTemplate.setPreference(HSceneTemplate.SCENE_SCREEN_LOCATION,new HScreenPoint(0.0f, 0.0f), HSceneTemplate.REQUIRED);
    }

//////////////////////////GRAPHIC SETUP & REACTION ON INPUT////////////////////////////
    
    public void startXlet() 
    {
        UserEventRepository myEventRepository = new UserEventRepository("User event");
        myEventRepository.addAllArrowKeys(); // Adding all the necessary keys.
        myEventRepository.addKey(HRcEvent.VK_SPACE);
        myEventRepository.addKey(HRcEvent.VK_BACK_SPACE);
        
        EventManager myEventManager = EventManager.getInstance();
        myEventManager.addUserEventListener(this, myEventRepository);
    
        buttons = new ButtonComponent[amountOfColours];
        lives = new ButtonComponent[4][amountOfColours];
        colors = new Color[amountOfColours]; 
        feedbacks = new Control[amountOfColours];
        colorNames = new String[8];
      
        colors[0] = Color.BLUE; // Adding all the colors in the array colors
        colors[1] = Color.YELLOW;
        colors[2] = Color.PINK;
        colors[3] = Color.GREEN;
        colors[4] = Color.CYAN;
        colors[5] = Color.RED;
        colors[6] = Color.BLACK; 
        colors[7] = Color.ORANGE;
        colors[7] = Color.ORANGE;
        
        colorNames[0] = "Blue";
        colorNames[1] = "Yellow";
        colorNames[2] = "Pink";
        colorNames[3] = "Green";
        colorNames[4] = "Cyan";
        colorNames[5] = "Red";
        colorNames[6] = "Black";
        colorNames[7] = "Orange";
         
        makeRandomColors(); // Make an array of 4 random colors
        
//////////////////////////COLOR PICKER////////////////////////////
        
        for(int i = 0; i < amountOfColours; i++) //makes for each available color a button with the color and the name of that color
        {
            buttons[i] = new ButtonComponent(colors[i], 40, 50+(60*i), colorNames[i],font, scene);
        }
           
//////////////////////////COLOR INPUT////////////////////////////
        
        for(int y = 0; y < 4; y++)
        {
            // Guess button
            for(int i = 0; i < amountOfColours; i++) // makes for 4 buttons so you can fill in a possible combination
            {
               lives[y][i] = new ButtonComponent(60, 150+(y*65), 10+(i*70), scene);
            }
        } 

//////////////////////////COLOR INPUT HIGHLIGHTER////////////////////////////
//Makes a highlighting button to show the player wich color he is selected and where he'll place it. 
        
        highlightButton = new ButtonComponent(Color.GREEN, 145, 70, scene); 
        
        for(int i = 0; i < buttons.length; i++)
        {
            int prevButton = i - 1;
            int nextButton = i + 1;
            if(i == 0)
            {
                prevButton = 3;
            }
            if(nextButton == buttons.length)
            {   
                nextButton = buttons.length - 8;
            }
           buttons[i].getButton().setFocusTraversal(buttons[prevButton].getButton(), buttons[nextButton].getButton(), null, null);
            
        }
             
//////////////////////////FEEDBACK COMPONENT////////////////////////////
// Creates graphic buttons to show the player if he has choosen the right color and if he placed it right.
        
        for(int i = 0; i < amountOfColours; i++)
        {
            feedbacks[i] = new Control(425, 10+(70*i), scene);
        }

//////////////////////////SCOREBOARD////////////////////////////
// Shows the score of the player and the instruction for starting a new game.
        
        textVictory = new ButtonComponent(new DVBColor(255,255,255,179), 500, 200, "Victory: "+victoryCounter, scene, 200, 30);
        textDefeat = new ButtonComponent(new DVBColor(255,255,255,179), 500, 230, "Defeat: "+defeatCounter, scene, 200, 30);
        textNewGame = new ButtonComponent(new DVBColor(255,255,255,179), 500, 260, "Press Backspace for\r\n new game", scene, 200, 80);

//////////////////////////BACKGROUND COLOR////////////////////////////
// Adds a background of a single color.
        
        bgc = new HStaticText ("", 0,0,720,576);
        bgc.setBackgroundMode(HVisible.BACKGROUND_FILL);
        bgc.setBackground(new DVBColor(125,100,125,255));
        scene.add(bgc);

//////////////////////////SCENE VALIDATOR////////////////////////////
        
        scene.validate();
        scene.setVisible(true);
        buttons[0].getButton().requestFocus();
    }

//////////////////////////RANDOM COLOUR GENERATOR////////////////////////////
// Makes a computer generated randomized correct color code.
    
    public void makeRandomColors()
    { 
        
        Color[] selection = (Color[]) colors;
        String[] selectorNames = (String[]) colorNames.clone();
        randomColours = new Color[4];
        for(int i = 0; i < 4; i++)
        {
            int newIndex = generateUniqueRandom(selection);
            
            randomColours[i] = selection[newIndex];
            
            Color[] tempArray = new Color[selection.length-1];
            String[] tempArrayNames = new String[selection.length-1];
            int indexTempArray = 0;
            for(int index = 0; index < selection.length; index++)
            {
                if(index != newIndex)
                {
                    tempArray[indexTempArray] = selection[index];
                    tempArrayNames[indexTempArray] = selectorNames[index];
                    indexTempArray++;
                }
            }
            selection = null;
            selection = (Color[]) tempArray.clone();
            
            selectorNames = null;
            selectorNames = (String[]) tempArrayNames.clone();
        }
    }
    
////////////////////////NEVER THE SAME COLOURS//////////////////////////////
    
    public int generateUniqueRandom(Color[] selector)
    {
        Random rand = new Random();
        int randomColor = rand.nextInt(selector.length);
        
        return randomColor;
    }
 
//////////////////////////EVENTLISTENER////////////////////////////
// Listens for an input of the player and makes sure that duplicates cant be chosen.
    
    public void userEventReceived(UserEvent a)
    {
        if(a.getType()==KeyEvent.KEY_PRESSED)
        {
            if(!refuseInput)
            {
                if(a.getCode()==HRcEvent.VK_SPACE)
                {
                    boolean sameColor = false;
                
                    for(int i = 0; i < 4; i++)
                    {
                        if(buttons[cursor].getColor() == lives[i][row].getColor())
                        {
                            sameColor = true;
                        }
                    }
                
                    if(!sameColor)
                    {
                        if(lives[guessCursor][row].isWhite())
                        {
                            filledIn++;
                        }
                        lives[guessCursor][row].changeColor(buttons[cursor].getColor(), scene);
                        guessCursor++;
                        if(guessCursor > 4-1)
                        {
                            guessCursor = 0;
                        }
                    }
                
                    if(filledIn > 3)
                    {
                        guessCursor = 0;
                        row++;
                        filledIn = 0;
                        checkCombination();
                    }
                    changeSquareSelection();
                }
            
                if(a.getCode()== HRcEvent.VK_LEFT)
                {
                    guessCursor--;
                    if(guessCursor < 0)
                    {
                        guessCursor = 3;
                    }
                    changeSquareSelection();
                }
                if(a.getCode()== HRcEvent.VK_RIGHT)
                {
                    guessCursor++;
                    if(guessCursor > 4-1)
                    {
                        guessCursor = 0;
                    }
                    changeSquareSelection();
                }
            
                if(a.getCode()== HRcEvent.VK_DOWN)
                {
                    cursor++;
                    if(cursor > amountOfColours-1)
                    {
                        cursor = 0;
                    }
                }
                if(a.getCode()==HRcEvent.VK_UP)
                {
                    cursor--;
                    if(cursor < 0)
                    {
                        cursor = amountOfColours-1;
                    }
                }
            }
            if(a.getCode()==HRcEvent.VK_BACK_SPACE)
            {
                cursor = 0;
                row = 0;
                guessCursor = 0;
                filledIn=0;
                changeSquareSelection();
                for (int i = 0; i < feedbacks.length; i++)
                {
                    feedbacks[i].makeNeutral(scene);
                }
                
                // Guess buttons
                for(int y = 0; y < 4; y++)
               {
                    for(int i = 0; i < amountOfColours; i++)
                    {
                        lives[y][i].changeColor(Color.WHITE, scene);
                    }
               }
                
                //Make new random code
                makeRandomColors();
                
                buttons[0].getButton().requestFocus();
                refuseInput = false;
            }
        }
    }
    
//////////////////////////LEFT RIGHT SELECTOR////////////////////////////
    
    public void changeSquareSelection(){
        highlightButton.changePos(145+(65*guessCursor), 5+(70*row), scene);
    }

//////////////////////////COMBINATION CHECKER////////////////////
    
    public void checkCombination()
    {
        int correctColors = 0;
        int correctPlaces = 0;
        
        for(int i = 0; i < 4; i++)
        {
            if(randomColours[i] == lives[i][row-1].getColor())
            {
                correctPlaces++;
            }
            else
            {
                boolean correctColor = false;
                for(int y = 0; y < 4; y++)
                {
                    if(randomColours[y] == lives[i][row-1].getColor())
                    {
                        correctColor = true;
                    }
                }
                if(correctColor)
                {
                    correctColors++;
                }
            }
        }
        feedbacks[row-1].setFeedback(correctColors, correctPlaces, scene);
        if(correctPlaces == 4)
        {
            victoryCounter++;
            textVictory.changeText("Victory: "+victoryCounter,Color.BLACK, scene);
            refuseInput = true;
        }
        else if(row == amountOfColours)
        {
            row = 0;
            changeSquareSelection();
            defeatCounter++;
            textDefeat.changeText("Defeat: "+defeatCounter,Color.BLACK, scene);
            refuseInput = true;
            // Set first rule the correct code
            for(int y = 0; y < 4; y++)
            {
                lives[y][0].changeColor(randomColours[y],scene);
                feedbacks[0].setFeedback(0, 4, scene);
            }
        }
    }
   
    public void pauseXlet() {}
    public void destroyXlet(boolean unconditional) {}
    public void notifyRelease (ResourceProxy proxy) {}
    public void release (ResourceProxy proxy) {}
    public boolean requestRelease (ResourceProxy proxy, Object requestData) { return false;} 
}