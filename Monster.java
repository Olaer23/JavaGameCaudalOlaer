import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.JComponent;
import java.awt.Rectangle;

public class Monster{
	public int xPos = 150;
	public int yPos = 300;
	private int width = 0;
	private int height = 0;
	public int life = 20;
	public int atk = 1;
	private boolean idle = true;
	public boolean alive = true;
	public boolean contact = false;
	private int yBound = 355;
	private int xBound = 650;
	
	private int direction = 0;
	//0-right 1-left

	public BufferedImage image;
	public URL resource = getClass().getResource("slime/idle0.png");

	public Monster(final Draw comp){
		try{
			image = ImageIO.read(resource);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public Monster(int xPass, int yPass, Draw comp){
		xPos = xPass;
		yPos = yPass;

		try{
			image = ImageIO.read(resource);
		}
		catch(IOException e){
			e.printStackTrace();
		}

		height = image.getHeight();
		width = image.getWidth();

		animate(comp);
	}
	//Attack
	public void attack(final Draw compPass){
		if(alive){
			compPass.checkCollisionMon();
			compPass.checkDamageMon();
		}
	}
	//Movement
	public void moveTo(int toX, int toY){
		if(alive){
			if(xPos<toX){
				xPos++;
				direction = 0;
			}
			else if(xPos>toX){
				xPos--;
				direction =1;
			}

			if(yPos-40<toY){
				yPos++;
			}
			else if(yPos-40>toY){
				yPos--;
			}
		}
	}
	//Monster CC
	public Rectangle Monster(){
		Rectangle bounds = new Rectangle(xPos, yPos, image.getWidth(), image.getHeight());
		return bounds;
	}
}