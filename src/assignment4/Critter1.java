package assignment4;

public class Critter1 extends Critter{
	@Override
	public String toString(){return "1";}
	
	private int direction;
	
	public Critter1(){
		direction = Critter.getRandomInt(8);
	}
	
	public boolean fight(String not_used){
		return true;
	}
	
	@Override
	public void doTimeStep(){
		run(direction);
		if(Critter.getRandomInt(8) == direction){
			for(int i=0; i<10; i++){
				run(direction);
			}
		}
		if(getEnergy() >= Params.min_reproduce_energy){
			Critter1 child1 = new Critter1();
			Critter1 child2 = new Critter1();
			reproduce(child1, Critter.getRandomInt(8));
			reproduce(child2, Critter.getRandomInt(8));
		}
		direction = Critter.getRandomInt(8);
	}
}
