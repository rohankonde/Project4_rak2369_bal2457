package assignment4;

public class Critter2 extends Critter {
	//EMPEROR CRITTER NEVER LOOKS BACK
	@Override
	public String toString(){return "2";}
	private int counter;
	private int direction;
	
	public Critter2(){
		counter = 1;
		direction = Critter.getRandomInt(8);
	}
	
	@Override
	public void doTimeStep() {
		walk(direction);
		if(counter%3 == 0){
			Critter3[] minions = new Critter3[7];
			Critter2 emperor = new Critter2();
			counter = 0;
			reproduce(emperor, 7);
			for(int i = 0; i < 7; i++){
				minions[i] = new Critter3();
				reproduce(minions[i], i);
			}
			
		}
		counter++;
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean fight(String oponent) {
		Critter2 survivor = new Critter2();
		reproduce(survivor, Critter.getRandomInt(8));
		return false;
	}

}
