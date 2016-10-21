/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */
package assignment4;

import java.lang.reflect.Constructor;
import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	
	private boolean hasMoved;
	
	private static boolean isFighting = false;;
	
	protected final void walk(int direction) {
		energy = energy - Params.walk_energy_cost;
		if(hasMoved == false){
			move(direction, 1);
		}
	}
	
	protected final void run(int direction) {
		energy = energy - Params.run_energy_cost;
		if(hasMoved == false){
			move(direction, 2);
		}
	}
	
	protected final void move(int direction, int distance){
		int tempX = this.x_coord;
		int tempY = this.y_coord;
		switch(direction){
			case 0:	tempX += distance; break;
					
			case 1: tempX += distance;
					tempY -= distance; break;
					
			case 2:	tempY -= distance; break;
					
					
			case 3: tempX -= distance;
					tempY -= distance; break;
					
			case 4:	tempX -= distance; break;
					
			case 5: tempX -= distance;
					tempY += distance; break;
					
			case 6:	tempY += distance; break;
					
			case 7: tempX += distance;
					tempY += distance; break;
		}
		hasMoved = true;
		if(isFighting){
			if(isOccupied(tempX, tempY)){
				return;
			}
		}
		
		this.x_coord = tempX;
		this.y_coord = tempY;
		this.x_coord %= Params.world_width;
		this.y_coord %= Params.world_height;
		if(this.x_coord < 0)
			this.x_coord+=Params.world_width;
		if(this.y_coord < 0)
			this.y_coord+=Params.world_height;
	}
	
	protected final boolean isOccupied(int x, int y){
		for(Critter crit:population){
			if(crit.x_coord == x && crit.y_coord == y)
				return true;
		}
		return false;
	}
	
	protected final void reproduce(Critter offspring, int direction) {
		if(this.energy < Params.min_reproduce_energy){
			return;
		}
		offspring.energy = (this.energy/2);
		this.energy = (this.energy/2)+ (this.energy%2);
		offspring.x_coord = this.x_coord;
		offspring.y_coord = this.y_coord;
		offspring.walk(direction);
		offspring.hasMoved = false;
		babies.add(offspring);
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try{
			Class<?> cls = Class.forName(critter_class_name);
			Constructor<?> construct = cls.getConstructor();
			Critter crit = (Critter)construct.newInstance();
			
			crit.x_coord = getRandomInt(Params.world_width);
			crit.y_coord = getRandomInt(Params.world_height);
			crit.energy = Params.start_energy;
			crit.hasMoved = false;
			
			population.add(crit);
			
		}catch(Exception e){
			throw new InvalidCritterException(critter_class_name);
		}
	
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		try{
			Class<?> cls = Class.forName(critter_class_name);
			for(Critter crit:population){
				if(cls.equals(crit.getClass())){
					result.add(crit);
				}
			}
		}catch(Exception e){
			throw new InvalidCritterException(critter_class_name);
		}
		
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population.clear();
		babies.clear();
	}
	
	public static void worldTimeStep() {
		for(Critter crit:population){
			crit.doTimeStep();
		}
		isFighting = true;
		for(Critter crit1:population){
			for(Critter crit2:population){
				if(!crit1.equals(crit2)){
					if(crit1.x_coord == crit2.x_coord && crit1.y_coord == crit2.y_coord){
						boolean fightA = crit1.fight(crit2.toString());
						boolean fightB = crit2.fight(crit1.toString());
						if(crit1.x_coord == crit2.x_coord && crit1.y_coord == crit2.y_coord && crit1.getEnergy() > 0 && crit2.getEnergy() > 0){
							int dice1, dice2;
							if(fightA){
								dice1 = getRandomInt(crit1.getEnergy());
							}else dice1 = 0;
							if(fightB){
								dice2 = getRandomInt(crit2.getEnergy());
							}else dice2 = 0;
							if(dice1 >= dice2){
								crit1.energy += crit2.energy*(0.5);
								crit2.energy = 0;
							}else{
								crit2.energy += crit1.energy*(0.5);
								crit1.energy = 0;
							}
						}
					}
				}
				crit1.hasMoved = false;
				crit2.hasMoved = false;
			}
		}
		isFighting = false;
		for(int i = population.size() - 1; i >= 0 ; i--){
			if(population.get(i).energy <= 0)
				population.remove(i);
		}
		for(int i = babies.size() - 1; i >= 0 ; i--){
			population.add(babies.get(i));
			babies.remove(i);
		}
		for(int i = Params.refresh_algae_count; i > 0; i--){
			Algae alg = new Algae();
			alg.setX_coord(getRandomInt(Params.world_width));
			alg.setY_coord(getRandomInt(Params.world_height));
			population.add(alg);
		}
	}
	
	public static void displayWorld() {
		String[][] dispMatrix = new String[Params.world_height+2][Params.world_width+2];
		for(int i = 0; i < dispMatrix.length; i++){
			for(int j = 0; j < dispMatrix[i].length; j++){
				if((i==0 && j==0)||(i==(dispMatrix.length-1)&&j==0)||(i==(dispMatrix.length-1)&&j==(dispMatrix[i].length-1))||(i==0&&j==(dispMatrix[i].length-1))){
					dispMatrix[i][j] = "+";
				}else if(i==0 || i==(dispMatrix.length-1)){
					dispMatrix[i][j] = "-";
				}else if(j==0||j==(dispMatrix[i].length-1)){
					dispMatrix[i][j] = "|";
				}else{
					dispMatrix[i][j] = " ";
				}
			}
		}
		
		for(Critter crit:population){
			dispMatrix[crit.y_coord+1][crit.x_coord+1] = crit.toString();
			
		}
		
		for(int i = 0; i < dispMatrix.length; i++){
			for(int j = 0; j < dispMatrix[i].length; j++){
				System.out.print(dispMatrix[i][j]);
			}
			System.out.println();
		}
	}
}
