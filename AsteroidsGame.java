import java.applet.*; //needed to create an applet
import java.awt.*; //needed for graphics
import java.awt.event.*; //needed for event handling

 public class AsteroidsGame extends Applet implements Runnable, KeyListener{
   private Thread thread;
   private long startTime;
	private long endTime;
	private long framePeriod;
	private Dimension dim;
	private Image img;
	private Graphics g;
   Ship ship;
	boolean paused;
   Shot[] shots;
	int numShots;
	boolean shooting;
	Asteroid[] asteroids;
	int numAsteroids;
	double astRadius, minAstVel, maxAstVel;
	int astNumHits, astNumSplit;
	
	int level;
	
	
 public void init(){
  this.resize(750,750); // make sure the applet is the right size

  startTime = 0;
  endTime = 0;
  framePeriod = 25;
  addKeyListener(this);
  
  numAsteroids = 0;
  level = 0;
  astRadius = 60;
  minAstVel = 5;
  maxAstVel = 5;
  astNumHits = 3;
  astNumSplit = 2;
  
  shots = new Shot[41];
  
  dim = getSize();
  img = createImage(dim.width, dim.height);
  g = img.getGraphics();
  
  thread = new Thread(this);
  thread.start();
  
 }
 
 public void setUpNextLevel(){
 
  level++;
  ship = new Ship(250,250,0,.35,.98,.1,12);
  numShots = 0;
  paused = false;
  shooting = false;
  
  asteroids = new Asteroid[level * (int)Math.pow(astNumSplit,astNumHits-1)+1];
  numAsteroids = level;
  
  for(int i = 0; i < numAsteroids; i++)
    asteroids[i] = new Asteroid(Math.random() * dim.width, Math.random() * dim.height,
	                      astRadius, minAstVel, maxAstVel, astNumHits, astNumSplit);
	}
  
 
 public void paint(Graphics gfx){
  g.setColor(Color.black); //Notice these first four lines all
  g.fillRect(0,0,getWidth(),getHeight()); //use g instead of gfx now. g draws

   for (int i = 0; i < numShots; i++)
	  shots[i].draw(g);
  for(int i=0;i<numAsteroids;i++)
     asteroids[i].draw(g);
  ship.draw(g);
  g.drawString("Level " + level, 20, 20);
  gfx.drawImage(img, 0, 0, this);
 }
 
public void update(Graphics gfx){
paint(gfx); // call paint without clearing the screen
}

public void run(){
 
 for(;;){
  startTime=System.currentTimeMillis();
  
  //start next level when all asteroids are destroyed
   if(numAsteroids<=0)
   
	setUpNextLevel();
   
	if(!paused){
    ship.move(dim.width,dim.height); // move the ship
   //move shots and remove dead shots
  
   for(int i=0;i<numShots;i++){
    shots[i].move(dim.width,dim.height);
   //removes shot if it has gone for too long
   //without hitting anything
    if(shots[i].getLifeLeft()<=0){
   //shifts all the next shots up one
   //space in the array
    deleteShot(i);
    i--; // move the outer loop back one so
   // the shot shifted up is not skipped
 }
}
 //move asteroids and check for collisions
  updateAsteroids();//SEE NEW METHOD BELOW
 if(shooting && ship.canShoot()){
  //add a shot on to the array
    shots[numShots]=ship.shoot();
    numShots++;
 }
}
  repaint();
  try{

   endTime=System.currentTimeMillis();
  if(framePeriod-(endTime-startTime)>0)
   Thread.sleep(framePeriod-(endTime-startTime));
 }catch(InterruptedException e){
 }
}
}

public void deleteShot(int index){
 numShots--;
 for(int i = index; i < numShots; i++)
  shots[i] = shots[i + 1];
  shots[numShots] = null;
  }

private void deleteAsteroid(int index){
  //delete asteroid and shift ones after it up in the array
   numAsteroids--;
   
	for(int i=index;i<numAsteroids;i++)
    asteroids[i]=asteroids[i+1];
    asteroids[numAsteroids]=null;
}

private void addAsteroid(Asteroid ast){
  //adds the asteroid passed in to the end of the array
  asteroids[numAsteroids]=ast;
  numAsteroids++;
}

private void updateAsteroids(){
  for(int i=0;i<numAsteroids;i++){
  // move each asteroid
  asteroids[i].move(dim.width,dim.height);
  //check for collisions with the ship, restart the
  //level if the ship gets hit
  if(asteroids[i].shipCollision(ship)){
   level--; //restart this level
   numAsteroids=0;
  return;
 }
 //check for collisions with any of the shots
  for(int j=0;j<numShots;j++){
   if(asteroids[i].shotCollision(shots[j])){
  //if the shot hit an asteroid, delete the shot
   deleteShot(j);
  //split the asteroid up if needed
   if(asteroids[i].getHitsLeft()>1){
    for(int k=0;k<asteroids[i].getNumSplit();k++)
      addAsteroid(
      asteroids[i].createSplitAsteroid(
      minAstVel,maxAstVel));
 }
 //delete the original asteroid
  deleteAsteroid(i);
  j = numShots; //break out of inner loop - it has
  //already been hit, so don’t need to check

   //for collision with other shots
   i--; //don’t skip asteroid shifted back into
  //the deleted asteroid's position
   }
  }
 }
}

public void keyPressed(KeyEvent k){
 switch (k.getKeyCode()) {
  case KeyEvent.VK_ENTER:
       if(!ship.isActive() && !paused)
		   ship.setActive(true);
		 else {
		  paused = !paused;
		  if(paused) 
		    ship.setActive(false);
		  else
		    ship.setActive(true);
			}
        break;
  case KeyEvent.VK_UP:
          ship.setAccelerating(true);
          break;
  case KeyEvent.VK_CONTROL:
         shooting = true;
          break;
  case KeyEvent.VK_LEFT:
          ship.setTurningLeft(true);
          break;
  case KeyEvent.VK_RIGHT:
       ship.setTurningRight(true);           
		 break;
 }
}

public void keyReleased(KeyEvent k){
 switch (k.getKeyCode()) {
  case KeyEvent.VK_UP:
          ship.setAccelerating(false);
          break;
  case KeyEvent.VK_CONTROL:
          shooting = false;
          break;
  case KeyEvent.VK_LEFT:
         ship.setTurningLeft(false);
          break;
  case KeyEvent.VK_RIGHT:
         ship.setTurningRight(false);
          break;
 }
}

public void keyTyped(KeyEvent k){}
}