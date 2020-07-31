package edu.fhm.cs.ss.schafkopf.ai.baseclasses;

import edu.fhm.cs.ss.schafkopf.ai.interfaces.ISpecializedAI;
import edu.fhm.cs.ss.schafkopf.ai.interfaces.ISpecializedAIFactory;
import edu.fhm.cs.ss.schafkopf.viewaccessible.ai.interfaces.IAI;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.utilities.interfaces.IRestrictedPlayerUtils;

/**
 * This abstract base class implements the basic methods of {@link IAI} and handles the forwarding and updating of the specialized AI based on on the provided
 * factory. Implementing classes have to care of setting the update flag if an update is needed and implement {@link #getBestGame(IRestrictedPlayerUtils)},
 * {@link #raise(IRestrictedPlayerUtils)}.
 * 
 * @author Sebastian Stumpf
 * 
 */
public abstract class BaseAI implements IAI {

	/**
	 * The game dependent AI the requests that depend on the game data state are forwarded to.
	 */
	private ISpecializedAI specializedAI;
	/**
	 * The game state at which this AI will accept the next game.
	 */
	private final GameState acceptRestartGameState;
	/**
	 * The factory, specialized AIs are created with.
	 */
	private final ISpecializedAIFactory specializedAiFactory;
	/**
	 * If this flag is set, the specialized AI will be updated before forwarding a method to it.
	 */
	private boolean updateFlag;

	/**
	 * Create an instance with given acceptRestartState and given factory.
	 * 
	 * @param acceptRestartGameStatus
	 *            the accept restart state.
	 * @param specializedAiFactory
	 *            the factory.
	 * @throws IllegalArgumentException
	 *             if the factory is null.
	 */
	protected BaseAI(final GameState acceptRestartGameStatus, final ISpecializedAIFactory specializedAiFactory) {

		this.updateFlag = true;
		this.specializedAI = null;
		this.acceptRestartGameState = acceptRestartGameStatus;
		if (specializedAiFactory == null) {
			throw new IllegalArgumentException();
		}
		this.specializedAiFactory = specializedAiFactory;
	}

	/**
	 * Create an instance with acceptRestartState = null and the given factory.
	 * 
	 * When the accept state is null the AI will always accept the next game.
	 * 
	 * @param specializedAiFactory
	 *            the factory.
	 * @throws IllegalArgumentException
	 *             if the factory is null.
	 */
	protected BaseAI(final ISpecializedAIFactory specializedAiFactory) {

		this(null, specializedAiFactory);
	}

	@Override
	public boolean acceptRestart(final IRestrictedPlayerUtils playerUtils) {

		// AI accepts the start of a new game when in the
		// predefined game status, if this is null, it always accepts a restart
		if (acceptRestartGameState == null) {
			return true;
		}
		// player already accepting restart, we don't have to accept twice
		if (playerUtils.getPovPlayerData().isAcceptingNextGameStart()) {
			return false;
		}
		// if the game is finished, we always accept a restart
		if (playerUtils.getRestrictedGameData().getGameState().equals(GameState.FINISHED)) {
			return true;
		}
		// if game state equal to defined accept state, we always accept a
		// restart
		if (!acceptRestartGameState.equals(playerUtils.getRestrictedGameData().getGameState())) {
			return false;
		}
		return true;
	}

	@Override
	public ICard getBestCard(final IRestrictedPlayerUtils playerUtils) {

		updateSpecializedAI(playerUtils);
		if (specializedAI == null) {
			return null;
		}
		return specializedAI.getBestCard(playerUtils);
	}

	@Override
	public boolean strike(final IRestrictedPlayerUtils playerUtils) {

		updateSpecializedAI(playerUtils);
		if (specializedAI == null) {
			return false;
		}
		return specializedAI.strike(playerUtils);
	}

	@Override
	public boolean strikeBack(final IRestrictedPlayerUtils playerUtils) {

		updateSpecializedAI(playerUtils);
		if (specializedAI == null) {
			return false;
		}
		return specializedAI.strikeBack(playerUtils);
	}

	/**
	 * Private method that updates the specialized AI, if {@link #updateFlag} is set.
	 * 
	 * @param playerUtils
	 *            the playerUtils used by the {@link #specializedAiFactory}.
	 */
	private void updateSpecializedAI(final IRestrictedPlayerUtils playerUtils) {

		// update specialized ai if needed
		if (updateFlag) {
			specializedAI = specializedAiFactory.getAI(playerUtils.getRestrictedGameData().getGameType());
			updateFlag = false;
		}
	}

	/**
	 * Can be called by extending classes to set the {@link #updateFlag}.
	 * 
	 */
	protected void setUpdateFlag() {

		this.updateFlag = true;
	}
}
