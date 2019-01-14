package tree;

import com.briansea.cabinet.GameState;

/**
 * The framework for a heuristic calculator for any given Game/
 * 
 * @author nyuen
 *
 */
public abstract class GaimRools {
	/**
	 * <tt>true</tt> if the AI is the first player, <tt>false</tt> otherwise
	 */
	public boolean P1;
	
	public GaimRools(){
		P1 = true;
	}
	
	/**
	 * Returns an integer heuristic that represents how good the game state is for the AI, with a positive value being good and a 
	 * negative value being bad.
	 * 
	 * @param state the GameState to be analyzed
	 * 
	 * @param P1Turn <tt>true</tt> if player 1 has the next move, <tt>false</tt> otherwise
	 * 
	 * @return an integer heuristic that represents how good the game state is for the AI
	 */
	public abstract int calcHeuristic(GameState state, boolean P1Turn);

}
