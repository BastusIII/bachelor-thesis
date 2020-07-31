package edu.fhm.cs.ss.schafkopf.view;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.fhm.cs.ss.schafkopf.view.baseclasses.BasePlayerView;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IAutonomousPlayerView;
import edu.fhm.cs.ss.schafkopf.viewaccessible.ai.interfaces.IAI;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.ChooseGameAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.GetCardsAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.PlayCardAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.RaiseAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.StartNextGameAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.StrikeAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.StrikeBackAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.interfaces.IPrimitiveGameController;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.utilities.interfaces.IRestrictedPlayerUtils;

/**
 * This view is acting autonomously, if an action is expected on the current game data. It interacts with a {@link IPrimitiveGameController}.<br>
 * <br>
 * 
 * The actions are created an forwarded to the controller by a Thread, that checks for each incoming game data if it is expected to act. If an action has to be
 * done, the AI, this view was initialized with, is asked for the best move to make. According to the AI's answer, an action is created and send to the
 * controller.the An active player that Observes the game data and makes an action, if it is his turn. This view is not displaying the received game data in any
 * way.<br>
 * Only one move is forwarded to the controller for each received game data. If the AI calculates invalid moves, the game will end up in a deadlock because this
 * view will not act properly.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class AutonomousPlayerView extends BasePlayerView implements IAutonomousPlayerView {
	/**
	 * The thread that creates actions according to the best move that is calculated by the {@link #ai} if it is expected to act. This is checked with
	 * {@link IRestrictedPlayerUtils#isExpectedToAct()}. The Thread will terminate if {@link #isStopped()} returns true.
	 * 
	 * @author Sebastian Stumpf
	 * 
	 */
	private class ActionThread extends Thread {

		@Override
		public void run() {

			IRestrictedPlayerUtils currentActionPlayerUtils;
			final List<IAction> actions = new LinkedList<>();
			while (!isStopped()) {
				synchronized (updateUtilsMonitor) {
					try {
						while (ai == null || getPlayerId() == null || playerUtilsQueue.isEmpty()) {
							updateUtilsMonitor.wait();
						}
					} catch (final InterruptedException e) {
						if (isStopped()) {
							break;
						}
					}
					// working reference
					currentActionPlayerUtils = playerUtilsQueue.remove();
				}

				if (ai.acceptRestart(currentActionPlayerUtils)) {
					actions.add(new StartNextGameAction(getPlayerId()));
				}
				if (currentActionPlayerUtils.isExpectedToAct()) {
					switch (currentActionPlayerUtils.getRestrictedGameData().getGameState()) {
						case GET_RAISE:
							if (ai.raise(currentActionPlayerUtils)) {
								actions.add(new RaiseAction(getPlayerId()));
							}
							actions.add(new GetCardsAction(getPlayerId()));
							break;
						case CHOOSE:
							actions.add(new ChooseGameAction(getPlayerId(), ai.getBestGame(currentActionPlayerUtils)));
							break;
						case PLAY:
							actions.add(new PlayCardAction(getPlayerId(), ai.getBestCard(currentActionPlayerUtils)));
							break;
						case STRIKE:
							actions.add(new StrikeAction(getPlayerId(), ai.strike(currentActionPlayerUtils)));
							break;
						case STRIKEBACK:
							actions.add(new StrikeBackAction(getPlayerId(), ai.strikeBack(currentActionPlayerUtils)));
							break;
						case FINISHED:
							break;
						default:
							break;
					}
				}

				for (final IAction action : actions) {
					getGameController().handleGameAction(action);
				}

				actions.clear();
			}
		}
	}

	/** The player utilities received with an update are stored here and processed in order by the {@link ActionThread}. */
	private final Queue<IRestrictedPlayerUtils> playerUtilsQueue;
	/** This monitor is used to synchronize the access on the player utility queue. */
	private final Object updateUtilsMonitor;
	/** The players AI will be asked for the best moves to make. */
	private IAI ai;

	/**
	 * Default constructor initializes controller and AI with null.
	 */
	public AutonomousPlayerView() {

		this(null, null);
	}

	/**
	 * Initialize the view with the given utils. controller is set to null.
	 * 
	 * @param ai
	 *            the ai.
	 */
	public AutonomousPlayerView(final IAI ai) {

		this(null, ai);
	}

	/**
	 * Initialize the view with the given parameters. The view is subscribed by calling {@link BasePlayerView#BasePlayerView(IPrimitiveGameController)}.
	 * 
	 * @param controller
	 *            the controller.
	 * @param ai
	 *            the ai.
	 */
	public AutonomousPlayerView(final IPrimitiveGameController controller, final IAI ai) {

		super(controller);
		this.playerUtilsQueue = new LinkedList<IRestrictedPlayerUtils>();
		this.ai = ai;
		this.updateUtilsMonitor = new Object();
	}

	@Override
	public IAI getAi() {

		return ai;
	}

	@Override
	public void setAi(final IAI ai) {

		this.ai = ai;
	}

	@Override
	public void start() {

		registerThreads(new ActionThread());
		super.start();

	}

	@Override
	public void update() {

		// we get a updateGameData(...) call, if the gameData has changed and thus don't need to implement this method.
	}

	@Override
	public void updateGameData(final IRestrictedPlayerUtils restrictedplayerUtils) {

		synchronized (updateUtilsMonitor) {
			playerUtilsQueue.add(restrictedplayerUtils);
			updateUtilsMonitor.notifyAll();
		}
	}
}
