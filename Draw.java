import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;
import java.awt.Font;


public class Draw extends JComponent{

	public BufferedImage backgroundImage;
	int monY = 355;
	int monX = 650;
	
	// randomizer
	public Random randomizer;
	
	public boolean check = true;
	public boolean draw = false;
	public boolean collide = false;
	
	// hero
	public Hero hero1 = new Hero(this);
	
	// enemy
	private Random rand = new Random();
	private Random rand2 = new Random();
	
	public int enemyCount = 0;
	Monster[] monsters = new Monster[10];

	public Draw(){
		randomizer = new Random();
		spawnEnemy();
		
		try{
			hero1.image = ImageIO.read(hero1.resource);
			backgroundImage = ImageIO.read(getClass().getResource("background.jpg"));
		}
		catch(IOException e){
			e.printStackTrace();
		}
		hero1.height = hero1.image.getHeight();
		hero1.width = hero1.image.getWidth();
		
		startGame();
	}

	public void startGame(){
		Thread gameThread = new Thread(new Runnable(){
			public void run(){
				while(true){
					try{
						for(int x = 0; x < monsters.length; x++){
							if(monsters[x]!=null){
								monsters[x].moveTo(hero1.x ,hero1.y);
								repaint();
							}
						}
						
						
						Thread.sleep(100);
					} catch (InterruptedException e) {
							e.printStackTrace();
					}
				}
			}
			
		});
		gameThread.start();
	}
	
	/*ENEMY Attributes*/
	//Spawn
	public void spawnEnemy(){
		if(enemyCount < 10){
			monsters[enemyCount] = new Monster(randomizer.nextInt(monX), randomizer.nextInt(monY), this);
			System.out.println("[Slime](" + enemyCount + ") appeared");
			enemyCount++;
		}
	}
	//Reset
	public void resetEnemy(){
			enemyCount--;
	}
	//Attack for Monster
	public void checkCollisionMon(){
		for(int x = 0; x < monsters.length; x++){
			boolean collide = false;
			
			if(monsters[x]!=null && monsters[x].alive){
				if(monsters[x].Monster().intersects(hero1.Hero())){
					collide = true;
				}else{
					collide = false;
				}
			}
			if(collide){
				System.out.println("monster collision!");
				hero1.contact = true;
			}
		}
	}
	//Attack for Monster
	public void checkDamageMon(){
		for(int x=0; x<monsters.length; x++){
			if(monsters[x]!=null && monsters[x].alive){
				if(hero1.contact){
					hero1.hp = hero1.hp -monsters[x].atk;
					System.out.println("[" + hero1.name + "] HP: " + hero1.hp);
					if(hero1.hp<=0){
						hero1.hp = 0;
					}
				}
			}
		}
		hero1.contact = false;
	}
	/*HERO Attributes*/
	//Attack for Hero
	public void checkCollision(){
		for(int x = 0; x < monsters.length; x++){
			boolean collide = false;
			
			if(monsters[x]!=null && monsters[x].alive){
				if(hero1.Hero().intersects(monsters[x].Monster())){
					collide = true;
				}else{
					collide = false;
				}
			}
			if(collide){
				System.out.println("collision!");
				monsters[x].contact = true;
			}
		}
	}

	//Attack for Hero
	public void checkDamage(){
		for(int x=0; x<monsters.length; x++){
			if(monsters[x]!=null && monsters[x].alive){
				if(monsters[x].contact){
					monsters[x].life = monsters[x].life - hero1.atk;
				}
			}
		}
	}
	/*Draw Attributes*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.YELLOW);
		g.drawImage(backgroundImage, 0, 0, this);
		
		//HUD
		g.setColor(Color.BLACK);
		int heightBG = backgroundImage.getHeight();
		int widthBG = backgroundImage.getWidth();
		g.fillRect(0, heightBG, widthBG, 110);
		g.setColor(Color.YELLOW);
		g.setFont(new Font("default", Font.ITALIC, 10));
		g.drawString((hero1.required-hero1.exp) + " [exp] needed", 3, heightBG+105);
		g.setColor(Color.WHITE);
		g.setFont(new Font("default", Font.ITALIC, 10));
		g.drawString(hero1.hp + " / " + hero1.maxhp, 10, heightBG+62);
		g.drawString(hero1.mp + " / " + hero1.maxmp, 10, heightBG+92);	
		g.drawImage(hero1.image, hero1.x, hero1.y, this);
		
		for(int c = 0; c < monsters.length; c++){
			if(monsters[c]!=null){

				g.drawImage(monsters[c].image, monsters[c].xPos, monsters[c].yPos, this);
				g.setColor(Color.GREEN);
				g.fillRect(monsters[c].xPos+7, monsters[c].yPos, monsters[c].life, 2);
			}	
		}
	}
}