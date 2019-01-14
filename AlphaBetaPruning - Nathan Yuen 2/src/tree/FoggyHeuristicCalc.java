package tree;

import java.util.List;

import com.briansea.cabinet.GameState;
import com.briansea.game.Location;
import com.briansea.game.Team;



public class FoggyHeuristicCalc extends GaimRools {


	@Override
	public int calcHeuristic(GameState state, boolean P1Turn) {

		int player;
		int opponent;

		if (P1){
			player = 1;
			opponent = 2;
		}else{
			player = 2;
			opponent = 1;
		}

		// Scans the board and puts it into an 2D array of ints. First player is represented by 1, second player by 2, empty by 0.
		boolean p1OddThreat = false;
		boolean p2EvenThreat = false;
		int h = 5;
		int heur = 0;
		int[][] board = new int[7][6];
		int[] heights = new int[7];
		int[] column;
		Location loc = new Location();
		for (int w=0; w<7; w++){
			column = board[w];
			loc.y = 5;
			loc.x = w;
			List<Team> p = state.getOwner(loc);
			while (!p.isEmpty() && h >= 0){
				if (p.get(0).getName().equals("Red")){
					column[h] = 2;
				}else{
					column[h] = 1;
				}
				h--;
				loc.y = h;
				p = state.getOwner(loc);
			}
			heights[w] = h;
			h = 5;

		}
		
		int hBound;
		int vBound;
		int current;
		int i;
		int inARow;
		int previous;
		int inARowP1;
		int inARowP2;
		boolean colHasThreat;
		int[] evenThreats = new int[6];

		/*
		 * Analyzes every square of the board that is empty to determine the number of odd and even threats.
		 * Odd threats are very good for the first player, while even threats are very good for the second player.
		 * At each square, each adjacent square is analyzed and then if that square is owned by a player, the square in the same
		 * direction will be analyzed. For example, if the square to the top left of an empty square is owned by Red, the method 
		 * will check if the square to the top left of that one is also red. By doing this, it will determine if a player has a
		 * "threat" on that square; i.e. the player will win if the player ends up owning that empty square.
		 */
		for (int w=0; w<7; w++){
			// Goes up columns to find threats
			int hgt = heights[w];
			colHasThreat = false;

			/*
			 * First, the method checks to see if the other player has 3 in a row and will win on the next move.
			 */
			
			if (hgt != -1){
				inARow = 0;
				// If opponent then has 3 in a row vertically, returns lowest/highest possible value
				if (hgt < 3){
					for (int v=1; v<4; v++){
						if (board[w][hgt+v] == opponent){
							inARow++;
						}
					}
					if (inARow == 3){
						if (P1Turn){
							return Integer.MIN_VALUE;
						}
						return Integer.MAX_VALUE;
					}
				}

				// If opp has 3 in a row diagonally down (\), returns lowest
				// Top left corner
				inARow = 0;
				hBound = 0;
				vBound = 0;
				current = 1;
				i = 1;
				// Top left
				while (w > hBound && hgt > vBound && current == opponent && inARow < 3 && i<4){
					current = board[w-i][hgt-i];
					inARow++;
					hBound++;
					vBound++;
					i++;
				}
				hBound = 6;
				vBound = 5;
				current = 1;
				i = 1;
				// Bottom right
				while (w > hBound && hgt > vBound && current == opponent && inARow < 3 && i<4){
					current = board[w+i][hgt+i];
					inARow++;
					hBound--;
					vBound--;
					i++;
				}
				if (inARow >= 3){
					if (P1Turn){
						return Integer.MIN_VALUE;
					}
					return Integer.MAX_VALUE;
				}

				// Diagonally up (/)
				inARow = 0;
				hBound = 0;
				vBound = 5;
				current = 1;
				i = 1;
				// Bottom left
				while (w > hBound && hgt > vBound && current == opponent && inARow < 3 && i<4){
					current = board[w-i][hgt+i];
					inARow++;
					hBound++;
					vBound--;
					i++;
				}
				hBound = 6;
				vBound = 0;
				current = 1;
				i = 1;
				// Top right
				while (w > hBound && hgt > vBound && current == opponent && inARow < 3 && i<4){
					current = board[w+i][hgt-i];
					inARow++;
					hBound--;
					vBound++;
					i++;
				}

				if (inARow >= 3){
					if (P1Turn){
						return Integer.MIN_VALUE;
					}
					return Integer.MAX_VALUE;
				}

				inARow = 0;

				// Checks for opponent horizontal 3 in a row
				hBound = 0;
				current = 1;
				i = 1;
				// Left
				while (w > hBound && current != 0 && inARow < 3 && inARow < 3 && i<4){
					current = board[w-i][hgt];
					if (current == opponent){
						inARow++;
					}
					hBound++;
					i++;
				}
				hBound = 6;
				current = 1;
				i = 1;
				// Right
				while (w > hBound && current != 0 && inARow < 3 && inARow < 3 && i<4){
					current = board[w+i][hgt];
					if (current == opponent){
						inARow++;
					}
					hBound--;
					i++;
				}
				if (inARow >= 3){
					inARow = 0;
				}
			}
			
			h = heights[w]-1;
			while (h >= 0){

				// Diagonal down (\)
				inARowP1 = inARowP2 = 0;
				hBound = 0;
				vBound = 0;
				current = 1;
				previous = 0;
				i = 1;
				// Top left
				while (w > hBound && h > vBound && current != 0 && inARowP1 < 3 && inARowP2 < 3 && i<4){
					current = board[w-i][h-i];
					if (current == 1 && previous != 2){
						inARowP1++;
						previous = current;
					}else if (current == 2 && previous != 1){
						inARowP2++;
						previous = current;
					}
					hBound++;
					vBound++;
					i++;
				}
				hBound = 6;
				vBound = 5;
				previous = 0;
				current = 1;
				i = 1;
				// Bottom right
				while (w < hBound && h < vBound && current != 0 && inARowP1 < 3 && inARowP2 < 3 && i<4){
					current = board[w+i][h+i];
					if (current == 1 && previous != 2){
						inARowP1++;
						previous = current;
					}else if (current == 2 && previous != 1){
						inARowP2++;
						previous = current;
					}
					hBound--;
					vBound--;
					i++;
				}
				
				// If either player has a threat, changes heuristic dramatically
				if (inARowP1 >= 3){
					if (h%2 == 0){
						heur += 1000;
					}else{
						if (p1OddThreat){
							heur += 2000;
						}else{
							// Good threat - first good threat is very good
							if (colHasThreat == false){
								heur += 200000;
								p1OddThreat = true;
							}
						}
						colHasThreat = true;
					}
				}
				if (inARowP2 >= 3){
					if (h%2 == 0){
						if (p2EvenThreat){
							heur -= 2000;
						}else{
							// Good threat - first good threat is very good
							if (colHasThreat == false){
								heur -= 150000;
								p2EvenThreat = true;
							}
							evenThreats[h]++;
						}
						colHasThreat = true;
					}else{
						heur -= 1000;
					}
				}
				inARowP1 = inARowP2 = 0;

				// DIAGONAL UP ///////////////
				hBound = 0;
				vBound = 5;
				current = 1;
				previous = 0;
				i = 1;
				
				// Bottom left
				while (w > hBound && h < vBound && current != 0 && inARowP1 < 3 && inARowP2 < 3 && i<4){
					current = board[w-i][h+i];
					if (current == 1 && previous != 2){
						inARowP1++;
						previous = current;
					}else if (current == 2 && previous != 1){
						inARowP2++;
						previous = current;
					}
					hBound++;
					vBound--;
					i++;
				}
				hBound = 6;
				vBound = 0;
				current = 1;
				previous = 0;
				i = 1;
				
				// Bottom right
				while (w < hBound && h > vBound && current != 0 && inARowP1 < 3 && inARowP2 < 3 && i<4){
					current = board[w+i][h-i];
					if (current == 1 && previous != 2){
						inARowP1++;
						previous = current;
					}else if (current == 2 && previous != 1){
						inARowP2++;
						previous = current;
					}
					hBound--;
					vBound++;
					i++;
				}



				if (inARowP1 >= 3){
					if (h%2 == 0){
						heur += 1000;
					}else{
						if (p1OddThreat){
							heur += 2000;
						}else{
							if (colHasThreat == false){
								heur += 200000;
								p1OddThreat = true;
							}
						}
						colHasThreat = true;
					}
				}
				if (inARowP2 >= 3){
					if (h%2 == 0){
						if (p2EvenThreat){
							heur -= 2000;
						}else{
							if (colHasThreat == false){
								heur -= 150000;
								p2EvenThreat = true;
							}
							evenThreats[h]++;
						}
						colHasThreat = true;
					}else{
						heur -= 1000;
					}
				}
				inARowP1 = inARowP2 = 0;
				previous = 0;

				// HORIZ -----------------
				hBound = 0;
				current = 1;
				i = 1;
				
				// Left
				while (w > hBound && current != 0 && inARowP1 < 3 && inARowP2 < 3 && i<4){
					current = board[w-i][h];
					if (current == 1 && previous != 2){
						inARowP1++;
						previous = current;
					}else if (current == 2 && previous != 1){
						inARowP2++;
						previous = current;
					}
					hBound++;
					i++;
				}
				hBound = 6;
				previous = 0;
				current = 1;
				i = 1;
				
				// Right
				while (w < hBound && current != 0 && inARowP1 < 3 && inARowP2 < 3 && i<4){
					current = board[w+i][h];
					if (current == 1 && previous != 2){
						inARowP1++;
						previous = current;
					}else if (current == 2 && previous != 1){
						inARowP2++;
						previous = current;
					}
					hBound--;
					i++;
				}
				if (inARowP1 >= 3){
					if (h%2 == 0){
						heur += 1000;
					}else{
						if (p1OddThreat){
							heur += 2000;
						}else{
							if (colHasThreat == false){
								heur += 200000;
								p1OddThreat = true;
							}
						}
						colHasThreat = true;
					}
				}
				if (inARowP2 >= 3){
					if (h%2 == 0){
						if (p2EvenThreat){
							heur -= 2000;
						}else{
							if (colHasThreat == false){
								heur -= 150000;
								p2EvenThreat = true;
							}
							evenThreats[h]++;
						}
						colHasThreat = true;
					}else{
						heur -= 1000;
					}
				}
				h--;
			}
		}

		// If p2 has 2 even threats in the same row, p2 wins.
		for (int z : evenThreats){
			if (z >= 2){
				heur = -150000;
			}
		}
		
		if (!P1){
			heur = -heur;
		}
		int value = 0;
		
		// Weights the center column highly, so the AI will take control in the early game
		for (int p : board[3]){
			value += 10;
			if (p == player){
				heur += value;
			}else if (p != 0){
				heur -= value;
			}
		}
		
		// Weights the columns adjacent to the center column more highly as a last resort
		for (int p : board[2]){
			if (p == player){
				heur += 1;
			}
		}
		for (int p : board[4]){
			if (p == player){
				heur += 1;
			}
		}
		return heur;

	}

}
