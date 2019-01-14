package tree;

import java.util.List;

import com.briansea.cabinet.GameState;
import com.briansea.cabinet.Plugin;
import com.briansea.cabinet.PluginInfo;
import com.briansea.game.Location;
import com.briansea.game.Move;
import com.briansea.game.Player;

public class bestai extends Player {
	private boolean detP1;
	private GaimRools game;
	
	private int numTurns;
	
	/**
	 * Every plugin must provide this static method
	 * @return a filled object describing the plugin
	 */
	public static PluginInfo getInfo() {
		PluginInfo pi = new PluginInfo() {

			@Override
			public String name() {
				return "best ai (nathan)";
			}

			@Override
			public String description() {
				return "by nathan";
			}

			@Override
			public Class<? extends Plugin> type() {
				return Player.class;
			}

			@Override
			public List<Class<? extends GameState>> supportedGames() {
				
				return null;
			}
			
		};
		
		return pi;
	}
	
	
	public bestai(){
		super("best ai");
		detP1 = false;
		game = new FoggyHeuristicCalc();
		numTurns = 0;
	}
	
	/**
	 * This method is called when your AI needs to make a move.  It should fill in 'm'
	 * with the appropriate move.
	 * 
	 * <b>Note:</b>This method may be ended prematurely.
	 * 
	 * @param gs the current state of the game
	 * @param m the object to fill in to represent the AIs move
	 * @return the AI's move; This should be m in most cases
	 */
	public Move makeMove( GameState gs, Move m ){
		if (!detP1){
			Location loc = new Location();
			if (gs.numTurns().min() == 1){
				game.P1 = false;
			}
			loc.x = 3;
			m.to = loc;
			detP1 = true;
			return m;
		}
		
		
		ProooningTreeee calc = new ProooningTreeee(gs, game);
		Move rtn;
		if (numTurns<2){
			rtn = calc.findBestMove(5);
			numTurns++;
		}else{
			rtn = calc.findBestMove(8);
			if (rtn == null){
				rtn = calc.findBestMove(6);
				if (rtn == null){
					rtn = calc.findBestMove(3);
				}
			}
		}
		m.copy(rtn);
		return m;
	}

}
