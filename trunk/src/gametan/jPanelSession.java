/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author linh
 */
public class jPanelSession extends JPanel implements Runnable {

    private static final int PNWIDTH = 800;
    private static final int PNHEIGHT = 600;
    private static int DEFAULT_FPS = 50;
    private static final int NO_DELAYS_PER_YIELD = 16;
    private static final int MAX_FRAME_SKIPS = 5;
    private static final Color LIGHT_BLUE = new Color(0.17f, 0.87f, 1.0f);
    private static final Font MESSAGE_FONT = new Font("SansSerif", Font.PLAIN, 24);
    private static final Color MESSAGE_COLOR = Color.RED;

    public jPanelSession() {
        period = ((long) 1000.0/DEFAULT_FPS) * 1000000L;
        help = new HelpView();
        help.setDrawing(PNWIDTH, PNHEIGHT, 0, 0);
        setDoubleBuffered(false);
        setPreferredSize(new Dimension(PNWIDTH, PNHEIGHT));
        setFocusable(true);
        requestFocus();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouse(e);
            }
        });
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKey(e);
            }        
        });
    }

    public boolean checkWin(Player player) {
	if (player.getNumCards() == 0 && table.isDeckEmpty()) {
	    gameOver = true;
	    winner = player;
	    return true;
	}
	return false;
    }
    
    private void handleMouse(MouseEvent e) {
	if (table == null || isPaused) return;
        Player p = table.getPerson();
        int x = e.getX();
        int y = e.getY();
        if (p.getState() == Player.PlayerState.CHOOSE) {
            if (p.checkCor(x, y)) p.setState(Player.PlayerState.PLAYED);
            if (p.getSessionType() == Player.SessionType.ACTIVE || p.getSessionType() == Player.SessionType.SUPPORT) {
                if (table.checkCor(x, y)) p.setState(Player.PlayerState.SKIP);
                if (cardsInSession.checkCor(x, y)) p.setState(Player.PlayerState.SKIP);
            }
            if (p.getSessionType() == Player.SessionType.PASSIVE)
                if (cardsInSession.checkCor(x, y)) p.setState(Player.PlayerState.TAKE);
        }
    }
    
    private void handleKey(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_N:
                int n = JOptionPane.showOptionDialog(this.getParent(), "Choose number of players in game", "New Game",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
                startGame(n*2, Player.Level.HARD);
            case KeyEvent.VK_H:
                showSupport();
                break;
            case KeyEvent.VK_P:
                showHelp();
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
        }
    }
    
    private void init() {
        serving = false;
        isPaused = false;
        gameOver = false;
        dbImage = null;
        table = null;
        players = null;
        cardsInSession = null;
        winner = null;
    }
    
    private void initSession(boolean skip) {
        players = table.getOrderInSession(skip);
        cardsInSession = table.getActiveSession();
        
        players.get(0).setSessionType(Player.SessionType.ACTIVE);
        players.get(0).setState(Player.PlayerState.PLAY);
        players.get(1).setSessionType(Player.SessionType.PASSIVE);
        players.get(1).setState(Player.PlayerState.WAIT);
        if (type == Game.FOUR_PLAYER) {
            players.get(2).setSessionType(Player.SessionType.SUPPORT);
            players.get(2).setState(Player.PlayerState.WAIT);
            players.get(3).setSessionType(Player.SessionType.DEACTIVE);
            players.get(3).setState(Player.PlayerState.NO_STATE);
        }
        player = players.get(0);
        waiter = players.get(1);
    }

    public void startGame(int type, Player.Level level) {
        init();
        this.type = type;
        table = new Game(type, level);
        table.setDrawing(PNWIDTH, PNHEIGHT, 0, 0);
        initSession(false);
        serving = true;
    }

    public void gameOver() {
    }

    public void stopGame() {
        running = false;
    }
    
    public void showSupport() {
        Player p = table.getPerson();
        p.setShowHelp(!p.isShowHelp());
    }
    
    public void showHelp() {
	isPaused = !isPaused;
    }
    
    private void message(String msg) {
        FontMetrics metrics = dbg.getFontMetrics(MESSAGE_FONT);
        int x = (PNWIDTH - metrics.stringWidth(msg)) / 2;
        int y = (PNHEIGHT - metrics.getHeight()) / 2;
        Color c = dbg.getColor();
        dbg.setColor(MESSAGE_COLOR);
        dbg.setFont(MESSAGE_FONT);
        dbg.drawString(msg, x, y);
        dbg.setColor(c);
    }
    
    private void gameUpdate() {
        if (serving) serving = table.serveCards();
        else if (!isPaused && !gameOver && table != null) {
	    //@TODO: code here
            if (table.isMoving()) return;
            switch (player.getState()) {
                case PLAY:
                    player.CheckAvailable(cardsInSession, players.get(1).getNumCards(), table.isDeckEmpty());
                    break;
                case CHOOSE:
                    if (player.getType() == Player.PlayerType.BOT) {
                        player.ChooseCard();
                        player.setState(Player.PlayerState.PLAYED);
                    }
                    break;
                case PLAYED:
                    if (table.playCardToSession(player)) return;
                    if (checkWin(player)) return;
                    if (players.get(0).getState() == Player.PlayerState.SKIP) players.get(0).setState(Player.PlayerState.WAIT);
                    if (type == Game.FOUR_PLAYER)
                        if (players.get(2).getState() == Player.PlayerState.SKIP) players.get(2).setState(Player.PlayerState.WAIT);
                    player.setState(Player.PlayerState.WAIT);
                    Player temp = player;
                    player = waiter;
                    waiter = temp;
                    player.setState(Player.PlayerState.PLAY);
                    break;
                case SKIP:
                    switch (player.getSessionType()) {
                        case ACTIVE:
                            if (type == Game.FOUR_PLAYER) {
                                if (players.get(2).getState() != Player.PlayerState.SKIP) {
                                    player = players.get(2);
                                    player.setState(Player.PlayerState.PLAY);
                                    return;
                                }
                            }
                            break;
                        case SUPPORT:
                            if (players.get(0).getState() != Player.PlayerState.SKIP) {
                                player = players.get(0);
                                player.setState(Player.PlayerState.PLAY);
                                return;
                            }
                            break;
                        case PASSIVE:
                            break;
                    }
                    if (table.takeCardInDeck(players.get(0))) return;
		    if (checkWin(players.get(0))) return;
                    if (table.takeCardInDeck(players.get(1))) return;
		    if (checkWin(players.get(1))) return;
                    if (type == Game.FOUR_PLAYER) {
                        if (table.takeCardInDeck(players.get(2))) return;
			if (checkWin(players.get(2))) return;
		    }
                    table.addCardDone(cardsInSession);
                    initSession(false);
                    break;
                case TAKE:
                    if (table.takeCardInDeck(players.get(0))) return;
		    if (checkWin(players.get(0))) return;
                    if (type == Game.FOUR_PLAYER) {
                        if (table.takeCardInDeck(players.get(2))) return;
			if (checkWin(players.get(2))) return;
		    }
                    if (table.takeCardInSession(players.get(1))) return;
		    if (checkWin(players.get(1))) return;
                    initSession(true);
                    break;
            }
        }
    }

    private void gameRender() {
        if (dbImage == null) {
            dbImage = createImage(PNWIDTH, PNHEIGHT);
            if (dbImage == null) {
                System.out.println("dbImage is null");
                return;
            } else {
                dbg = dbImage.getGraphics();
            }
        }
        // background
        dbg.setColor(LIGHT_BLUE);
        dbg.fillRect(0, 0, PNWIDTH, PNHEIGHT);

        // draw all item
        if (table != null) table.draw(dbg);
        
        if (serving) message("Serving cards, player " + players.get(0).getName() + " plays first");
        if (isPaused) help.draw(dbg);
        if (gameOver) message("Game over, player " + winner.getName() + " wins");
    }

    private void paintScreen() {
        Graphics g;
        try {
            g = this.getGraphics();
            if ((g != null) && (dbImage != null)) {
                g.drawImage(dbImage, 0, 0, null);
            }
            g.dispose();
        } catch (Exception e) {
            System.out.println("Graphics context error: " + e);
        }
    }
    
    private int type;
    private Game table;
    private Player winner;
    private Player player;
    private Player waiter;
    private ArrayList<Player> players;
    private ArrayCardsSession cardsInSession;
    
    private Thread animator;
    private HelpView help;
    private long period;
    private boolean serving;
    private boolean running;
    private boolean isPaused;
    private boolean gameOver;
    
    private Graphics dbg;
    private Image dbImage = null;
    private Object[] options = {"Cancel", "Two Players", "Four Players"};

    @Override
    public void addNotify() {
        super.addNotify();
        isPaused = true;
        animator = new Thread(this);
        animator.start();
    }

    @Override
    public void run() {
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        long excess = 0L;

        running = true;
        beforeTime = System.nanoTime();

        while (running) {
            gameUpdate();
            gameRender();
            paintScreen();

            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            sleepTime = (period - timeDiff) - overSleepTime;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1000000L);
                } catch (InterruptedException ex) {
                }
                overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
            } else {
                excess -= sleepTime;
                overSleepTime = 0L;

                if (++noDelays >= NO_DELAYS_PER_YIELD) {
                    Thread.yield();
                    noDelays = 0;
                }
            }
            beforeTime = System.nanoTime();

            int skips = 0;
            while ((excess > period) && (skips < MAX_FRAME_SKIPS)) {
                excess -= period;
                gameUpdate();
                skips++;
            }
        }
    }
    
}
