package edu.fhm.cs.ss.schafkopf.ai.sets.simpledeterministic;

import java.util.Collection;

import edu.fhm.cs.ss.schafkopf.ai.baseclasses.BaseAI;
import edu.fhm.cs.ss.schafkopf.model.BasicGameData;
import edu.fhm.cs.ss.schafkopf.model.Card;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardValue;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.utilities.interfaces.IRestrictedPlayerUtils;

/**
 * The Random AI answers the questions about the next move deterministic, but without higher intelligence.
 *
 * @author Sebastian Stumpf
 *
 */
public class SimpleDeterministicAI extends BaseAI {
	/**
	 * Creates an instance AI with the accept state and the forbidden games defined in and {@link #DEFAULT_FORBIDDEN_GAMETYPES}.
	 *
	 * @param acceptRestartGameStatus
	 *            the accept state.
	 */
	public SimpleDeterministicAI(final GameState acceptRestartGameStatus) {

		super(acceptRestartGameStatus, new SimpleDeterministicSpecializedAIFactory());
	}

	@Override
	public IBasicGameData getBestGame(final IRestrictedPlayerUtils playerUtils) {

		setUpdateFlag();
		final Collection<GameType> gametypes = playerUtils.getAvailableGameTypes();
		if (gametypes.contains(GameType.SAUSPIEL)) {
			final Collection<CardColor> colors = playerUtils.getAvailableColors(GameType.SAUSPIEL);
			if (colors.contains(CardColor.EICHEL)) {
				return new BasicGameData(GameType.SAUSPIEL, CardColor.EICHEL);
			}
			if (colors.contains(CardColor.GRAS)) {
				return new BasicGameData(GameType.SAUSPIEL, CardColor.GRAS);
			}
			if (colors.contains(CardColor.EICHEL)) {
				return new BasicGameData(GameType.SAUSPIEL, CardColor.SCHELLN);
			}
			// this should not happen
			return new BasicGameData(GameType.PASS, null);
		} else {
			return new BasicGameData(GameType.PASS, null);
		}
	}

	@Override
	public boolean raise(final IRestrictedPlayerUtils playerUtils) {

		if (playerUtils.getPovPlayerData().getInitialHand().contains(new Card(CardColor.EICHEL, CardValue.OBER))) {
			return true;
		}
		return false;
	}

}
