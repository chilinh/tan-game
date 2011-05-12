/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

/**
 *
 * @author linh
 */

public class Player {

    public Player(String name, PlayerType type) {
        this.name = name;
        this.type = type;
        this.state = Player.PlayerState.NO_STAGE;
        this.type = Player.PlayerType.PERSON;
        this.sessionState = PlayerSessionState.WAIT;
        this.sessionType = Player.PlayerSessionType.DEACTIVE;
    }

    public PlayerSessionState getSessionState() {
        return sessionState;
    }

    public void setSessionState(PlayerSessionState sessionState) {
        this.sessionState = sessionState;
    }

    public PlayerSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(PlayerSessionType sessionType) {
        this.state = Player.PlayerState.NO_STAGE;
        this.sessionType = sessionType;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public PlayerType getType() {
        return type;
    }
    
    public enum PlayerState {
        NO_STAGE, CAN_DEFEND, CANT_DEFEND, CAN_ATTACK, CANT_ATTACK, WIN, MUST_TAKE_CARDS
    }

    public enum PlayerType {
        PERSON, BOT
    }

    public enum PlayerSessionType {
        ACTIVE, PASSIVE, SUPPORT, DEACTIVE
    }

    public enum PlayerSessionState {
        PLAY, WAIT
    }

    private String name;
    private PlayerType type;
    private PlayerState state;
    private PlayerSessionType sessionType;
    private PlayerSessionState sessionState;
    private ArrayCards cards;
    private ArrayCards cardsCanPlay;
}
