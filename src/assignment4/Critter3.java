package assignment4;

public class Critter3 extends Critter{
	@Override
	public String toString(){return "3";}
	
	@Override
	public void doTimeStep() {
		if(Critter.getRandomInt(10) == 1){
			run(Critter.getRandomInt(8));
		}
		if(getEnergy() < 20){
			run(Critter.getRandomInt(8));
		}
		
	}

	@Override
	public boolean fight(String oponent) {
		return true;
	}

}
