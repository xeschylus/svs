
import java.awt.*;

 public class Ship {
//-4, -5, -5, -7, -7, -5, -4,
//-1, -1, -2, -2, -6, -6, -2
  private final double[] origXPts = {14,  -20, -6, 0, -6, -20, 14, 0};
  private final double[] origYPts = {-3,  -7, 0, 0, 0,  7, 3, 0 };
  private final double[] origFlameXPts = {-6, -23, -6};
  private final double[] origFlameYPts = {-3, 0, 3};
  private final int radius = 6;
  
  private double x, y, angle;
  private double xVelocity, yVelocity, acceleration;
  private double velocityDecay, rotationalSpeed;
  private boolean turningLeft, turningRight;
  private boolean accelerating, active;
  
  int[] xPts, yPts, flameXPts, flameYPts;
  
  int shotDelay, shotDelayLeft;
  
  public Ship(double x, double y, double angle, double acceleration,
              double velocityDecay, double rotationalSpeed, int shotDelay){
         // this.x refers to the Ship's x, x refers to the x parameter
   this.x = x;
  this.y = y;
  this.angle = angle;
  this.acceleration = acceleration;
  this.velocityDecay = velocityDecay;
  this.rotationalSpeed = rotationalSpeed;
  xVelocity = 0; // not moving
  yVelocity = 0;
  turningLeft = false; // not turning
  turningRight = false;
  accelerating = false; // not accelerating
  active = false; // start off paused
  xPts = new int[origXPts.length]; // allocate space for the arrays
  yPts  =new int[origYPts.length];
  flameXPts = new int[3];
  flameYPts = new int[3];
  this.shotDelay = shotDelay; // # of frames between shots
  shotDelayLeft = 0; // ready to shoot
 }
 
 public void draw(Graphics g){
 
 if(accelerating && active){
   for(int i = 0; i < 3; i++){
flameXPts[i]=(int)(origFlameXPts[i]*Math.cos(angle)-
origFlameYPts[i]*Math.sin(angle)+
x+.5);
flameYPts[i]=(int)(origFlameXPts[i]*Math.sin(angle)+
origFlameYPts[i]*Math.cos(angle)+
y+.5);
	}
	g.setColor(Color.RED);
	g.fillPolygon(flameXPts, flameYPts, 3);
	}
   for(int i = 0; i < origXPts.length; i++){
xPts[i]=(int)(origXPts[i]*Math.cos(angle)- //rotate
origYPts[i]*Math.sin(angle)+
x+.5); //translate and round
yPts[i]=(int)(origXPts[i]*Math.sin(angle)+ //rotate
origYPts[i]*Math.cos(angle)+
y+.5);
    }
	if(active)
	  g.setColor(Color.WHITE);
	else
	  g.setColor(Color.BLUE);
	g.fillPolygon(xPts, yPts, origXPts.length);
	}
	
	public void move(int scrnWidth, int scrnHeight){
	if(shotDelayLeft > 0)
	    shotDelayLeft--;
	if(turningLeft)
	    angle -= rotationalSpeed;
   if(turningRight)
	    angle += rotationalSpeed;
   if(angle > (2*Math.PI))
	    angle -= (2 * Math.PI);
	else if(angle < 0)
	    angle += (2 * Math.PI);
	if(accelerating) {
	  xVelocity += acceleration * Math.cos(angle);
	  yVelocity += acceleration * Math.sin(angle);
	  }
	 x += xVelocity;
	 y += yVelocity;
	 xVelocity *= velocityDecay;
	 yVelocity *= velocityDecay;
	 
	 if (x < 0)
	  x += scrnWidth;
	 else if(x > scrnWidth)
	  x -= scrnWidth;
	 if(y < 0)
	  y += scrnHeight;
	 else if (y > scrnHeight)
	  y -= scrnHeight;
	 }
	 
public void setAccelerating(boolean accelerating){
 this.accelerating = accelerating; //start or stop accelerating the ship
}

public void setTurningLeft(boolean turningLeft){
 this.turningLeft = turningLeft; //start or stop turning the ship
}

public void setTurningRight(boolean turningRight){
 this.turningRight = turningRight;
}

public double getX(){
 return x; // returns the ship’s x location
}

public double getY(){
 return y;
}

public double getRadius(){
 return radius; // returns radius of circle that approximates the ship
}

public void setActive(boolean active){
 this.active=active; //used when the game is paused or unpaused
}

public boolean isActive(){
 return active;
}

public boolean canShoot(){
if(shotDelayLeft>0) //checks to see if the ship is ready to
 return false; //shoot again yet or if it needs to wait longer
else
 return true;
}
 public Shot shoot(){
  shotDelayLeft = shotDelay;
  
  return new Shot(x, y, angle, xVelocity, yVelocity, 40);
  }

}