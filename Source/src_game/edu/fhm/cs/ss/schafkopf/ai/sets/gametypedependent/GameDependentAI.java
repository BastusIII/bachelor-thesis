package edu.fhm.cs.ss.schafkopf.ai.sets.gametypedependent;

import edu.fhm.cs.ss.schafkopf.ai.baseclasses.BaseAI;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.utilities.interfaces.IRestrictedPlayerUtils;

/**
 * This AI implements the general questions by itself and uses the {@link BaseAI} to keep the specialized AI up to date and answer questions that need a running
 * game.
 * 
 * The specialized AI is generated from a {@link GameTypeDependantSpecializedAIFactory}.
 * 
 * This Class is part of a set to demonstrate the structure of an AI-Set, that contains specialized AI's for every GameType.
 * 
 * @deprecated NOT IMPLEMENTED.
 * 
 * @author Sebastian Stumpf
 * 
 */
@Deprecated
public class GameDependentAI extends BaseAI {

	/**
	 * Instantiates this AI.
	 * 
	 * @param acceptRestartGameStatus
	 *            the gameState to accept the next game.
	 */
	protected GameDependentAI(final GameState acceptRestartGameStatus) {

		super(acceptRestartGameStatus, new GameTypeDependantSpecializedAIFactory());
	}

	@Override
	public IBasicGameData getBestGame(final IRestrictedPlayerUtils playerUtils) {

		return null;
	}

	@Override
	public boolean raise(final IRestrictedPlayerUtils playerUtils) {

		return false;
	}

}
