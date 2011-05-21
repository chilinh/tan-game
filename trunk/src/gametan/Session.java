/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author linh
 */
public class Session implements Runnable {
    
    public Session(BlockingQueue<Integer> queue, Table table, ArrayList<JPanel> panels, jSessionPanel pnLeft, jSessionPanel pnRight) {
        this.queue = queue;
        this.table = table;
        this.panels = panels;
        this.pnLeft = pnLeft;
        this.pnRight = pnRight;
	this.cardsInSession = new ArrayList<Card>();
	this.pnLeft.setCards(this.cardsInSession);
	this.pnRight.setCards(this.cardsInSession);
        this.state = Session.SessionState.RUNNING;
    }

    // get set method
    public SessionState getState() {
	return state;
    }
    
    
    // addition method
    public void initSession(int idPlayer) {
        this.idPlayer = idPlayer;
        ArrayList<Player> players = this.table.getOrderInSession(this.idPlayer);
        this.attacker = players.get(0);
        this.defender = players.get(1);
        this.supporter = players.get(2);
        this.disable = players.get(3);
        
        this.attacker.setSessionType(Player.PlayerSessionType.ACTIVE);
        this.attacker.setSessionState(Player.PlayerSessionState.WAIT);
        this.attacker.setState(Player.PlayerState.NO_STAGE);
        this.defender.setSessionType(Player.PlayerSessionType.PASSIVE);
        this.defender.setSessionState(Player.PlayerSessionState.WAIT);
        this.defender.setState(Player.PlayerState.NO_STAGE);
        if (this.supporter != null) {
            this.supporter.setSessionType(Player.PlayerSessionType.SUPPORT);
            this.supporter.setSessionState(Player.PlayerSessionState.WAIT);
            this.supporter.setState(Player.PlayerState.NO_STAGE);
        }
        if (this.disable != null) {
            this.disable.setSessionType(Player.PlayerSessionType.SUPPORT);
            this.disable.setSessionState(Player.PlayerSessionState.WAIT);
            this.disable.setState(Player.PlayerState.NO_STAGE);
        }
        if (this.idPlayer == 1 || (this.idPlayer == 3 && this.table.getType() >= 3)) {
            this.pnLeft.setActive(true);
            this.pnRight.setActive(false);
        } else {
            this.pnRight.setActive(true);
            this.pnLeft.setActive(false);
        }
	this.cardsInSession.clear();
    }
    
    private void repaint() {
	try {
	    for (JPanel panel : this.panels) {
		panel.updateUI();
	    }
	    this.pnLeft.updateUI();
	    this.pnRight.updateUI();
	    Thread.sleep(50);
	} catch (InterruptedException ex) {
	    Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
    private Player checkWin() {
	if (this.attacker.GetNumCards() == 0) return this.attacker;
	if (this.defender.GetNumCards() == 0) return this.defender;
	if (this.supporter != null)
	    if (this.supporter.GetNumCards() == 0) return this.supporter;
	return null;
    }
    
    private void haveWinner(Player player) {
	JOptionPane.showMessageDialog(null, "Player "+player.getName()+" win", "Finish", JOptionPane.INFORMATION_MESSAGE);
        this.state = Session.SessionState.FINISH;
    }
    
    private final ArrayList<JPanel> panels;
    private final BlockingQueue<Integer> queue;
    private final Table table;
    private final jSessionPanel pnLeft, pnRight;
    private SessionState state;
    private ArrayList<Card> cardsInSession;
    private int idPlayer;
    private Player attacker;
    private Player defender;
    private Player supporter;
    private Player disable;

    public enum SessionState {
	RUNNING, WAITING, FINISH
    }
    
    @Override
    public void run() {
        try {
            Player player = null;
	    Player waiter = null;
            int result = 0;
            boolean skip = false;
            boolean change = false;
            while (true) {
                if (change) {
                    // wait to change
		    if (this.state != Session.SessionState.WAITING) {
			this.state = Session.SessionState.WAITING;
			this.queue.take();
		    }
		    this.state = Session.SessionState.RUNNING;
		    // take card
		    if (result == 1) {
			this.table.takeCardInDeck(this.attacker);
			this.table.takeCardInDeck(this.defender);
			this.table.takeCardInDeck(this.supporter);
			// TODO: update AI
			
		    } else {
			this.defender.ReceiveCards(this.cardsInSession);
			this.table.takeCardInDeck(this.attacker);
			this.table.takeCardInDeck(this.supporter);
			// TODO: update AI
			
		    }
		    // check win
		    player = checkWin();
		    if (player != null) {
			this.haveWinner(player);
			break;
		    }
		    // change session
		    this.initSession((this.idPlayer+result)%this.table.getType());
                }
                if (this.cardsInSession.isEmpty()) {
                    player = this.attacker;
		    waiter = this.defender;
                    skip = false;
                    change = false;
                    result = 0;
                    player.setSessionState(Player.PlayerSessionState.PLAY);
		    waiter.setSessionState(Player.PlayerSessionState.WAIT);
                }
                // check avail card
                player.CheckAvailable(this.cardsInSession, this.defender.GetNumCards(), this.table.isDeckEmpty());
                // show help
                this.repaint();
                switch(player.getState()) {
                    case CANT_ATTACK:
                        // cant attack
                        if (!this.cardsInSession.isEmpty() && this.supporter != null && !skip) {
			    // change attacker
			    if (player.getSessionType() == Player.PlayerSessionType.ACTIVE) player = this.supporter;
			    else player = this.attacker;
			    waiter = this.defender;
			    player.setSessionState(Player.PlayerSessionState.PLAY);
			    waiter.setSessionState(Player.PlayerSessionState.WAIT);
			    skip = true;
                        } else {
			    // no supporter, change session
			    change = true;
			    result = 1;
			}
                        break;
                    case CANT_DEFEND:
                        // change session
                        change = true;
                        result = 2;
                        break;
                    case MUST_TAKE_CARDS:
                        // change session
                        change = true;
                        result = 1;
                        break;   
                    case WIN:
			this.haveWinner(player);
                        break;
                    default:
                        // can play
                        if (player.getType() == Player.PlayerType.PERSON)
                            result = this.queue.take();
                        else result = player.ChooseCard();
                        if (result == -2) {
                            // player skip
			    if (!this.cardsInSession.isEmpty()) {
				if (this.supporter != null && !skip) {
				    // change attacker
				    if (player.getSessionType() == Player.PlayerSessionType.ACTIVE) player = this.supporter;
				    else player = this.attacker;
				    waiter = this.defender;
				    player.setSessionState(Player.PlayerSessionState.PLAY);
				    waiter.setSessionState(Player.PlayerSessionState.WAIT);
				    skip = true;
				} else {
				    // no supporter, change session
				    this.state = Session.SessionState.WAITING;
				    change = true;
				    result = 1;
				}
			    }
                        } else if (result == -1) {
                            // player stop defend and take cards
			    this.state = Session.SessionState.WAITING;
			    change = true;
			    result = 2;
			    break;
                        } else {
                            skip = false;
                            // play card
                            Card card = player.PlayCards(result);
                            this.cardsInSession.add(card);
                            this.repaint();
                            result = 0;
                            // @TODO: update ai
			    
			    // change active
			    Player tmp = player;
			    player = waiter;
			    waiter = tmp;
			    player.setSessionState(Player.PlayerSessionState.PLAY);
			    waiter.setSessionState(Player.PlayerSessionState.WAIT);
                        }
                }
                if (this.state == Session.SessionState.FINISH) break;
            }
        } catch (InterruptedException ex) {
        }
    }
    
}
