import java.applet.*; //needed to create an applet
import java.awt.*; //needed for graphics
import java.awt.event.*; //needed for event handling

public class AsteroidsGame extends Applet implements Runnable, KeyListener{
Thread thread; // used to run the game
Dimension dim; // these are used for double buffering
Image img; // the back buffer
Graphics g; // used to draw on the back buffer
// used to regulate the speed of the game
long endTime, startTime, framePeriod;
/**
* This method is called when the applet is first created.
*/
public void init(){
resize(500,500); //can be set to any dimension desired
// INITIALIZE ANY OF YOUR OWN VARIABLES HERE
endTime=0;
startTime=0;
framePeriod=25; //may be adjusted to change the speed that
//the game runs.
addKeyListener(this); //tell the class to listen for KeyEvents
dim=getSize();
img=createImage(dim.width, dim.height);//create back buffer
g=img.getGraphics(); //create Graphics obj to draw on back buffer
thread=new Thread(this); //create the thread that runs game
thread.start(); //start the thread
}
/**
* This method paints the graphics of the applet.
* @param gfx - the applet's graphics object
*/
public void paint(Graphics gfx){
g.setColor(Color.black); //set color to clear the screen with
g.fillRect(0,0,500,500); //clear the screen


// CODE TO DRAW GRAPHICS HERE
gfx.drawImage(img,0,0,this); //copys back buffer onto the screen
}
/**
* This is the method called by repaint() and makes a call to
* paint() without clearing the screen.
* The original method, Applet.update(), clears the screen
* with a white rectangle and then calls paint.
* Clearing the screen causes flickering in games, so we have
* overriden update() to call paint without clearing.
* We can do this because we will make paint() redraw the entire
* screen, so clearing it first would be pointless.
* @param gfx - the applet's graphics object
*/
public void update(Graphics gfx){
paint(gfx);
}
/**
* This method contains the code to run the game.
* It is executed by a thread.
*/
public void run(){
for(;;){
startTime=System.currentTimeMillis();
// CODE TO EXECUTE A FRAME OF THE GAME HERE
repaint();
try{ //regulate the speed of the game
endTime=System.currentTimeMillis();
if(framePeriod-(endTime-startTime)>0)
Thread.sleep(framePeriod-(endTime-startTime));
}catch(InterruptedException e){
}
}
}

/**
* Responds to any keys being pressed on the keyboard
* @param e - contains information on the event (what key was
* pressed, etc)
*/
public void keyPressed(KeyEvent e){
if(e.getKeyCode()==KeyEvent.VK_UP){
// CODE TO RESPOND TO UP KEY BEING PRESSED
}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
// CODE TO RESPOND TO DOWN KEY BEING PRESSED
}else if(true);
 // DITTO FOR ALL CONTROLS IN THE GAME
}
/**
* Responds to any keys being released on the keyboard
* @param e - contains information on the event (what key was
* released, etc)
*/
public void keyReleased(KeyEvent e){

if(e.getKeyCode()==KeyEvent.VK_UP){
// CODE TO RESPOND TO UP KEY BEING RELEASED
}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
// CODE TO RESPOND TO DOWN KEY BEING RELEASED
}else if( )
 // DITTO FOR ALL CONTROLS IN THE GAME
}
/**
* This method doesn't usually need to do anything for simple games.
* It might be used if the game involved the user typing in text.
*/
public void keyTyped(KeyEvent e){
}
}
