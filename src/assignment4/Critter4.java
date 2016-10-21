package assignment4;

public class Critter4 extends Critter{

	@Override
	public String toString(){return "4";}
	private int dir;
	public Critter4(){
		dir = Critter.getRandomInt(8);
	}
	
	@Override
	public void doTimeStep() {
		if(dir<0)
			dir += 8;
		if(dir%2 == 1)
			walk(dir);
		dir++;
		dir %= 8;
		if(getEnergy() < 50)
			dir -= 2;
		Critter4 child = new Critter4();
		if(dir%3 == 1)
			reproduce(child, Critter.getRandomInt(8));
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean fight(String oponent) {
		run((dir+4)%8);
		return true;
	}

}
