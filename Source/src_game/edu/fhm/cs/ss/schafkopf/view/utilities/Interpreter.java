package edu.fhm.cs.ss.schafkopf.view.utilities;

import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.ChooseGameAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.GetCardsAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.PlayCardAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.RaiseAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.StartNextGameAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.StrikeAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.StrikeBackAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardValue;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPlayerId;

/**
 * The interpreter offers functionality to convert strings from the user interactions to actions that can be executed by the game controller.
 *
 * @author Sebastian Stumpf
 *
 */
public class Interpreter {
	/** The player ID is needed to create valid actions and has to be set by the view. **/
	private IPlayerId playerId;

	/**
	 * Constructor, player ID is set to null.
	 */
	public Interpreter() {

		this.playerId = null;
	}

	/**
	 * @return the player ID. It is needed to create valid actions and has to be set by the view.
	 */
	public IPlayerId getPlayerId() {

		return playerId;
	}

	/**
	 * Interprets String instruction in a special format as an action, manipulates that action according to the instruction and returns it.<br>
	 * Accepted format: [action type],[arguments].<br>
	 * Action types and their arguments:<br>
	 * play: [card color],[card value] -> play a card<br>
	 * choose: [game type],[card color] -> card color is not needed for wenz && pass, card color is interpreted as trump for solos and mate color for partner
	 * games<br>
	 * raise: [boolean] -> raise the pool multiplier if less than 4 cards<br>
	 * strike: [boolean] -> raise the pool multiplier by striking the lead player<br>
	 * strike back: [boolean] -> raise the pool multiplier by striking back the opponent team player<br>
	 * next game: [-] -> start a new game<br>
	 * get: [-] -> get 4 cards<br>
	 * Arguments and their values:<br>
	 * card color: schelln, herz, gras, eichel -> also valid: s,h,g,e<br>
	 * card value: 7-10, unter, ober, koenig, sau -> also valid: 7-9,u,o,k,s<br>
	 * game type: pass, wenz, wenztout, farbwenz, farbwenztout, solo, solotout, sauspiel, si<br>
	 * boolean: f for false, all other values including no value for true
	 *
	 * @param instruction
	 *            the string instruction.
	 * @return the action if the instruction could be interpreted or null.
	 */
	public IAction interpretString(final String instruction) {

		IAction matchedAction = null;
		final String normalizedInstruction = instruction.toLowerCase().replaceAll("ü", "ue").replaceAll("ä", "ae").replaceAll("ö", "oe").replaceAll("ß", "ss").replaceAll("[^a-z0-9,]", "")
				.replaceAll(",+", ",");
		final String[] instructions = normalizedInstruction.split(",");
		try {
			if (instructions[0].startsWith("p")) {
				matchedAction = interpretPlay(instructions[1].charAt(0), instructions[2].charAt(0));
			} else if (instructions[0].startsWith("c")) {
				matchedAction = interpretChoose(instructions[1], instructions.length >= 3 ? instructions[2].charAt(0) : '-');
			} else if (instructions[0].startsWith("r")) {
				matchedAction = new RaiseAction(playerId);
			} else if (instructions[0].startsWith("strikeb")) {
				matchedAction = new StrikeBackAction(playerId, (instructions.length >= 3 ? instructions[2].charAt(0) : '-') != 'f');
			} else if (instructions[0].startsWith("strike")) {
				matchedAction = new StrikeAction(playerId, (instructions.length >= 3 ? instructions[2].charAt(0) : '-') != 'f');
			} else if (instructions[0].startsWith("g")) {
				matchedAction = new GetCardsAction(playerId);
			} else if (instructions[0].startsWith("n")) {
				matchedAction = new StartNextGameAction(playerId);
			}
		} catch (final IndexOutOfBoundsException e) {
			matchedAction = null;
		}
		return matchedAction;
	}

	/**
	 * @param playerId
	 *            the player ID to set.
	 */
	public void setPlayerId(final IPlayerId playerId) {

		this.playerId = playerId;
	}

	/**
	 * Interprets a color char and a game type sting and set it to the chosen game of the players {@link ChooseGameAction} if both have valid matches.
	 *
	 * @param color
	 *            the color char (s,h,g,e).
	 * @param gameType
	 *            the game type string.
	 * @return the action if a card matched, else null.
	 */
	private IAction interpretChoose(final String gameType, final char color) {

		final GameType matchedGameType = interpretGameType(gameType);
		if (matchedGameType != null) {
			if (matchedGameType.needsColor) {
				final CardColor matchedColor = interpretColor(color);
				if (matchedColor != null) {
					return new ChooseGameAction(playerId, matchedGameType, matchedColor);
				}
			} else {
				return new ChooseGameAction(playerId, matchedGameType, null);
			}
		}
		return null;
	}

	/**
	 * Interprets a color char.
	 *
	 * @param color
	 *            the color char (s,h,g,e).
	 * @return the matched color or null.
	 */
	private CardColor interpretColor(final char color) {

		switch (color) {
			case 's':
				return CardColor.SCHELLN;
			case 'h':
				return CardColor.HERZ;
			case 'g':
				return CardColor.GRAS;
			case 'e':
				return CardColor.EICHEL;
			default:
				return null;
		}
	}

	/**
	 * Interprets a gameType String.
	 *
	 * @param gameType
	 *            the gameType string (pass, wenz, wenztout, farbwenz, farbwenztout, solo, solotout, sauspiel, si).
	 * @return the matched gameType or null;
	 */
	private GameType interpretGameType(final String gameType) {

		switch (gameType) {
			case "pass":
				return GameType.PASS;
			case "wenz":
				return GameType.WENZ;
			case "wenztout":
				return GameType.WENZ_TOUT;
			case "si":
				return GameType.SI;
			case "farbwenz":
				return GameType.FARBWENZ;
			case "farbwenztout":
				return GameType.FARBWENZ_TOUT;
			case "solo":
				return GameType.SOLO;
			case "solotout":
				return GameType.SOLO_TOUT;
			case "sauspiel":
				return GameType.SAUSPIEL;
			default:
				return null;
		}
	}

	/**
	 * Interprets a color char and a value char and sets the card of the player {@link PlayCardAction} if both have valid matches.
	 *
	 * @param color
	 *            the color char (s,h,g,e).
	 * @param value
	 *            the value char (7-9,u,o,k,s).
	 * @return the action if a card matched, else null.
	 */
	private IAction interpretPlay(final char color, final char value) {

		final CardColor matchedColor = interpretColor(color);
		final CardValue matchedValue = interpretValue(value);
		if (matchedColor != null && matchedValue != null) {
			return new PlayCardAction(playerId, matchedColor, matchedValue);
		} else {
			return null;
		}
	}

	/**
	 * Interprets a value char.
	 *
	 * @param value
	 *            the value char (7-9,u,o,k,s).
	 * @return the matched value or null;
	 */
	private CardValue interpretValue(final char value) {

		switch (value) {
			case '7':
				return CardValue.SIEBENER;
			case '8':
				return CardValue.ACHTER;
			case '9':
				return CardValue.NEUNER;
			case '1':
				return CardValue.ZEHNER;
			case 'u':
				return CardValue.UNTER;
			case 'o':
				return CardValue.OBER;
			case 'k':
				return CardValue.KÖNIG;
			case 's':
				return CardValue.SAU;
			default:
				return null;
		}
	}
}
