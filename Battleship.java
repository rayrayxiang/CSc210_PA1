/*
 * File name: Battleship.java
 * Student: Ray Xiang
 * Course: CSC 210
 * Description: This class is a simplified version of Battleship played against 
 * the computer. Each side gets a 5 by 5 grid, and two ships of length 3. The 
 * player must enter coords like so: [capital letter A-E][number 0-4]. The 
 * first grid is the target, and the second grid is the ocean. A-E is top to 
 * bottom, and 0-4 is left to right.
 */

import java.util.Scanner;
import java.util.HashMap;

public class Battleship {

	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		// get initial inputs from the user
		String[] starting_inputs = get_starting_inputs(input);
		String player_name = starting_inputs[0];
		int[] player_ships = get_coords(starting_inputs);
		
		// make four grids, 2 for the player and 2 for the CPU
		String[][] player_ocean = make_ocean();
		place_ships(player_ocean, player_ships);
		String[][] player_target = make_ocean();
		String[][] CPU_ocean = make_ocean();
		place_ships(CPU_ocean, two_valid_ships());
		String[][] CPU_target = make_ocean();
		print_ocean(player_target);
		print_ocean(player_ocean);
		
		loop_turns(player_ocean, player_target, CPU_ocean, CPU_target, input, 
				player_name);
		System.out.println("GAME OVER");
		
		input.close();
	}
	
	
	/*
	 * The body of the game; loop asks the user to send hits until the game is 
	 * over.
	 */
	public static void loop_turns(String[][] player_ocean, 
			String[][] player_target, String[][] CPU_ocean, 
			String[][] CPU_target, Scanner input, String player_name) {
		while(true) {
			// player makes shot
			System.out.println("Call your shot " + player_name + "!");
			System.out.println("Enter the row and column (eg. B2):");
			String[] player_shot = input.nextLine().split("");
			int x1 = decode(player_shot[0]);
			int y1 = Integer.parseInt(player_shot[1]);
			mark_grid(player_target, CPU_ocean, x1, y1);
			print_ocean(player_target);
			print_ocean(player_ocean);
			if(six_hits(CPU_ocean)) {
				System.out.println("You win " + player_name + "!");
				break;
			}
			
			// CPU makes shot
			int[] random_shot = random_shot(CPU_target);
			int x2 = random_shot[0];
			int y2 = random_shot[1];
			mark_grid(CPU_target, player_ocean, x2, y2);
			System.out.println("Press the enter key to see the computer's "
					+ "shot.");
			input.nextLine();
			print_ocean(player_target);
			print_ocean(player_ocean);
			if(six_hits(player_ocean)) {
				System.out.println("You lost! Better luck next time!");
				break;
			}
		}
	}
	
	
	/*
	 * Marks the current target grid and opponent's ocean grid with H's or M's 
	 * depending on whether or not the shot hit. For example, if the CPU 
	 * missed, this method marks the CPU's target grid and the player's ocean 
	 * grid with an "M." 
	 */
	public static void mark_grid(String[][] current, String[][] opponent, 
			int x, int y) {
		if(opponent[x][y].equals("C")) {
			opponent[x][y] = "H";
			current[x][y] = "H";
		}
		else {
			opponent[x][y] = "M";
			current[x][y] = "M";
		}
	}
	
	
	/*
	 * Asks the user for the inputs needed to start the game, which are the 
	 * player's name and where to place their ships. Returns all inputs as an 
	 * array of strings.
	 */
	public static String[] get_starting_inputs(Scanner input) {
		String[] starting_inputs = new String[7];
		
		// slot 0 for player name
		System.out.println("What is your first name?");
		starting_inputs[0] = input.nextLine();
		String player_name = starting_inputs[0];
		
		System.out.println("Place your ships " + player_name + "!");
		System.out.println("You have two cruisers that are both of length 3.");
		// slots 1-3 for ship 1
		System.out.println("Place ship number 1:");
		for(int i = 1;i <= 3;i++) {
			System.out.println("Enter the row and column (eg. B2):");
			starting_inputs[i] = input.nextLine();
		}
		// slots 4-6 for ship 2
		System.out.println("Place ship number 2:");
		for(int i = 4;i <= 6;i++) {
			System.out.println("Enter the row and column (eg. B2):");
			starting_inputs[i] = input.nextLine();
		}
		
		return starting_inputs;
	}

	
	/*
	 * Extracts the letter-number coordinates from the array returned by 
	 * get_starting_inputs into an array of integers.
	 */
	public static int[] get_coords(String[] starting_inputs) {
		// 12 slots for 6 pairs
		int[] coords = new int[12];
		int k = 0;
		for (int i = 1; i <= 6; i++) {
			String[] coord = starting_inputs[i].split("");
			coords[k] = decode(coord[0]);
			coords[k + 1] = Integer.parseInt(coord[1]);
			k += 2;
		}
		return coords;
	}
	
	
	/*
	 * Returns a number 0-4 given a capital letter A-E.
	 */
	public static int decode(String letter) {
		HashMap<String, Integer> decoder = new HashMap<String, Integer>();
		decoder.put("A", 0);
		decoder.put("B", 1);
		decoder.put("C", 2);
		decoder.put("D", 3);
		decoder.put("E", 4);
		return decoder.get(letter);
	}

	
	/*
	 * Returns a 5 by 5 2D array filled with █'s, which will be the base for 
	 * the grids.
	 */
	public static String[][] make_ocean() {
		String[][] ocean = new String[5][5];
		for(int i = 0; i <= 4; i++) {
			for (int j = 0; j <= 4; j++) {
				ocean[i][j] = "█";
			}
		}
		return ocean;
	}
	
	
	/*
	 * Prints a grid given its 2D array.
	 */
	public static void print_ocean(String[][] ocean) {
		System.out.print("\n");
		for(int i = 0;i <= 4;i++) {
			for(int j = 0;j <= 4; j++) {
				System.out.print(ocean[i][j] + " ");
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}
	
	
	/*
	 * Places two ships onto the 2D array. coords has 12 elements representing 
	 * 6 ordered pairs. 
	 */
	public static void place_ships(String[][] ocean, int[] coords) {
		for (int i = 0; i <= 10; i += 2) {
			int x = coords[i];
			int y = coords[i + 1];
			ocean[x][y] = "C";
		}
	}
	
	/*
	 * Returns the coordinates of two random ships.
	 */
	public static int[] two_valid_ships() {
		int[] coords = new int[12];
		// generate two random ships
		int[] ship1 = random_ship();
		int[] ship2 = random_ship();
		// generate a new ship if they overlap
		while(overlap(ship1, ship2)) {
			ship2 = random_ship();
		}
		// paste coordinates into an array
		for(int i = 0; i <= 5; i++) {
			coords[i] = ship1[i];
		}
		for(int i = 6; i <= 11; i++) {
			coords[i] = ship2[i - 6];
		}
		return coords;
	}

	
	/*
	 * Returns the coordinates of a random length 3 ship.
	 */
	public static int[] random_ship() {
		// random starting coords
		int x = (int)(Math.random() * 5);
		int y = (int)(Math.random() * 5);
		// random direction to increment remaining coords
		int direction = (int)(Math.random() * 4);
		while(off_ocean(build(x, y, direction))) {
			direction = (int)(Math.random() * 4);
		}
		
		int[] ship = build(x, y, direction);
		return ship;
	}
	
	
	/*
	 * Checks if the coordinates of a length 3 ship fit inside a 5 by 5 ocean.
	 */
	public static boolean off_ocean(int[] coords) {
		for(int i = 0; i <= 5; i++) {
			if(coords[i] < 0 || coords[i] > 4) {
				return true;
			}
		}
		return false;
	}
	
	
	/*
	 * Returns the coords of a length 3 ship using an initial point and a 
	 * direction to increment the other 2 points.
	 */
	public static int[] build(int x, int y, int direction) {
		int[] ship = new int[6];
		ship[0] = x;
		ship[1] = y;
		// we build the ship down, up, right, or left
		if(direction == 0) {
			ship[2] = x + 1;
			ship[3] = y;
			ship[4] = x + 2;
			ship[5] = y;
		}
		if(direction == 1) {
			ship[2] = x - 1;
			ship[3] = y;
			ship[4] = x - 2;
			ship[5] = y;
		}
		if(direction == 2) {
			ship[2] = x;
			ship[3] = y + 1;
			ship[4] = x;
			ship[5] = y + 2;
		}
		if(direction == 3) {
			ship[2] = x;
			ship[3] = y - 1;
			ship[4] = x;
			ship[5] = y - 2;
		}
		return ship;
	}
	
	
	
	/*
	 * Checks if two ships overlap.
	 */
	public static boolean overlap(int[] ship1, int[] ship2) {
		for(int i = 0; i <= 4; i += 2) {
			int x1 = ship1[i];
			int y1 = ship1[i + 1];
			for(int j = 0; j <= 4; j += 2) {
				int x2 = ship2[j];
				int y2 = ship2[j + 1];
				if(x1 == x2 && y1 == y2) {
					return true;
				}
			}
		}
		return false;
	}

	
	/*
	 * Returns the coordinates of a random shot made by the computer.
	 */
	public static int[] random_shot(String[][] CPU_target) {
		// a random shot
		int x = (int)(Math.random() * 5);
		int y = (int)(Math.random() * 5);
		// generate a different random shot if the current shot is a repeat
		while(!CPU_target[x][y].equals("█")) {
			x = (int)(Math.random() * 5);
			y = (int)(Math.random() * 5);
		}
		int[] shot = {x, y};
		return shot;
	}
	
	
	/*
	 * Checks if the game is over i.e. 6 hits were made by a side.
	 */
	public static boolean six_hits(String[][] ocean) {
		int hit_count = 0;
		for(int i = 0; i <= 4; i++) {
			for(int j = 0; j <= 4; j++) {
				if(ocean[i][j].equals("H")) {
					hit_count += 1;
				}
			}
		}
		if(hit_count < 6) {
			return false;
		}
		return true;
	}
	

}
