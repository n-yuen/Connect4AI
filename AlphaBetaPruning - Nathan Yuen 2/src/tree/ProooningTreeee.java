package tree;

import java.util.Iterator;
import java.util.List;

import com.briansea.cabinet.GameState;
import com.briansea.game.Move;
/**
 * An alpha-beta pruning tree to use with bsea's Cabinet.
 * 
 * @author nyuen
 *
 */
public class ProooningTreeee {
	private knowed root;
	private int infinity;
	private int neginfinity;

	private GaimRools game;

	public ProooningTreeee(GameState state, GaimRools game){
		infinity = Integer.MAX_VALUE;
		neginfinity = Integer.MIN_VALUE;
		root = new knowed(neginfinity, infinity, state);
		this.game = game;
	}

	/**
	 * Finds the best move using alpha-beta pruning 
	 * 
	 * @param depth the depth of the tree
	 * @return the best move
	 */
	public Move findBestMove(int depth){
		if (depth < 3){
			throw new IndexOutOfBoundsException();
		}
		Move bestMove = null;
		Move lastMove;
		int current;
		while (root.it.hasNext()){
			lastMove = root.it.next();
			current = subTreeBeta(root.addKnowed(lastMove), depth);
			if (current > root.alpha){
				root.alpha = current;
				bestMove = lastMove;
			}
		}
		System.err.println(root.alpha);
		return bestMove;
	}

	private int subTreeAlpha(knowed k, int depth){
		// Runs if game is over
		if (k.gs.isGameOver()){
			return neginfinity;
		}
		if (depth == 2){
			return k.heuristic(false);
		}
		return alphaBranches(k, depth);
	}


	private int alphaBranches(knowed k, int depth){
		int newA;
		while (k.it.hasNext()){
			newA = subTreeBeta(k.addKnowed(), depth-1);
			if (newA > k.alpha){
				k.alpha = newA;
				if (k.beta < k.alpha){
					return k.alpha;
				}
			}
		}
		return k.alpha;
	}

	private int subTreeBeta(knowed k, int depth){
		// Runs if game is over
		if (k.gs.isGameOver()){
			return infinity;
		}
		if (depth == 2){
			return k.heuristic(true);
		}
		return betaBranches(k, depth);
	}
	
	private int betaBranches(knowed k, int depth){
		int newB;
		while (k.it.hasNext()){
			newB = subTreeAlpha(k.addKnowed(), depth-1);
			if (newB < k.beta){
				k.beta = newB;
				if (k.beta < k.alpha){
					return k.beta;
				}
			}
		}
		return k.beta;
	}

	private class knowed {
		public int alpha;
		public int beta;
		public List<Move> validMoves;
		public GameState gs;
		public Iterator<Move> it;

		public knowed(int alpha, int beta, GameState startState){
			this.alpha = alpha;
			this.beta = beta;
			gs = startState;
			validMoves = gs.getValidMoves();
			it = validMoves.iterator();
		}

		public knowed addKnowed(){
			GameState newState = gs.copyInstance();
			newState.makeMove((Move) it.next());
			return new knowed(alpha, beta, newState);
		}

		public knowed addKnowed(Move m){
			GameState newState = gs.copyInstance();
			newState.makeMove(m);
			return new knowed(alpha, beta, newState);
		}

		public int heuristic(boolean P1Turn){
			GameState newState = gs.copyInstance();
			newState.makeMove((Move) it.next());
			return game.calcHeuristic(gs, P1Turn);
		}
	}
}
