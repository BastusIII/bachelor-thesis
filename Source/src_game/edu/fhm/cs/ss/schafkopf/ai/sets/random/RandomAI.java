package edu.fhm.cs.ss.schafkopf.ai.sets.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import edu.fhm.cs.ss.schafkopf.ai.baseclasses.BaseAI;
import edu.fhm.cs.ss.schafkopf.model.BasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.utilities.interfaces.IRestrictedPlayerUtils;

/**
 * The Random AI answers the questions about the next move randomly, but valid.
 *
 * @author Sebastian Stumpf
 *
 */
public class RandomAI extends BaseAI {

	/**
	 * A field for forbidden gametypes if none are given in the constructor.
	 */
	private static final GameType[] DEFAULT_FORBIDDEN_GAMETYPES = { GameType.FARBWENZ_TOUT, GameType.FARBWENZ, GameType.SOLO, GameType.SOLO_TOUT, GameType.WENZ, GameType.WENZ_TOUT };

	/**
	 * These gametypes will not be chosen in {@link #getBestGame(IRestrictedPlayerUtils)}.
	 */
	private final Collection<GameType> forbiddenGameTypes;

	/**
	 * Creates a random AI with the accept state and the forbidden games defined in and {@link #DEFAULT_FORBIDDEN_GAMETYPES}.
	 *
	 * @param acceptGameState
	 *            the accept state.
	 */
	public RandomAI(final GameState acceptGameState) {

		this(acceptGameState, null);
	}

	/**
	 * Creates a random AI with given accept state and forbidden games.
	 *
	 * @param acceptGameState
	 *            the restart accept state.
	 * @param forbiddenGameTypes
	 *            the forbidden game types.
	 */
	public RandomAI(final GameState acceptGameState, final GameType[] forbiddenGameTypes) {

		super(acceptGameState, new RandomSpecializedAIFactory());
		if (forbiddenGameTypes == null || forbiddenGameTypes.length == 0) {
			this.forbiddenGameTypes = Arrays.asList(DEFAULT_FORBIDDEN_GAMETYPES);
		} else {
			this.forbiddenGameTypes = Arrays.asList(forbiddenGameTypes);
		}
	}

	@Override
	public IBasicGameData getBestGame(final IRestrictedPlayerUtils playerUtils) {

		// update Flag is set each new game, when the player calls this method.
		setUpdateFlag();
		GameType chosenGameType = null;
		CardColor chosenColor = null;
		final Collection<GameType> allowedGameTypes = playerUtils.getAvailableGameTypes();
		Collection<GameType> preferredGameTypes = new ArrayList<GameType>(allowedGameTypes);
		preferredGameTypes.removeAll(forbiddenGameTypes);
		if (preferredGameTypes.isEmpty()) {
			preferredGameTypes = allowedGameTypes;
		}
		int randomIndex = (int) (Math.random() * preferredGameTypes.size());
		int counter = 0;
		for (final GameType type : preferredGameTypes) {
			if (counter++ == randomIndex) {
				chosenGameType = type;
				break;
			}
		}
		final Collection<CardColor> allowedColors = playerUtils.getAvailableColors(chosenGameType);
		if (allowedColors != null && !allowedColors.isEmpty()) {
			randomIndex = (int) (Math.random() * allowedColors.size());
			counter = 0;
			for (final CardColor color : allowedColors) {
				if (counter++ == randomIndex) {
					chosenColor = color;
					break;
				}
			}
		}
		return new BasicGameData(chosenGameType, chosenColor);
	}

	@Override
	public boolean raise(final IRestrictedPlayerUtils playerUtils) {

		if (playerUtils.isAllowedToRaise()) {
			return (int) (Math.random() * 2) == 0;
		}
		return false;
	}
}
