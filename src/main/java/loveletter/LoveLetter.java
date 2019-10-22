package loveletter;
import agents.RandomAgent;
import org.deeplearning4j.rl4j.space.Encodable;

import java.util.Random;
public class LoveLetter implements Encodable
{
    private Agent rando;
    private Random random;
    private State gameState;
    private State[] playerStates;
    private Agent[] agents;
    private int currentPlayerIndex;
    private int numPlayers;
    private boolean hasPlayedIllegalMove;
    private Card currentPlayerTopCard;
    public LoveLetter(long seed,Agent[] agents, int currentPlayerIndex)
    {
        this.random = new Random(seed);
        this.currentPlayerIndex = currentPlayerIndex;
        this.agents = agents;
        rando = new RandomAgent();
        gameState = new State(random, agents);
        numPlayers = gameState.numPlayers();
        playerStates = new State[agents.length];
        hasPlayedIllegalMove = false;
        reset();
    }
    /**
     * Constructs a LoveLetter game.
     * Default construct with system random seed, and System.out as the PrintStream
     * **/
    public LoveLetter(Agent[] agents,int currentPlayerIndex)
    {
        this(System.currentTimeMillis(),agents, currentPlayerIndex);
    }

    public boolean isOver()
    {
        return gameState.roundOver() || gameState.eliminated(currentPlayerIndex);
    }

    public boolean hasPlayedIllegalMove()
    {
        return hasPlayedIllegalMove;
    }
    public int getCard()
    {
    	return gameState.getCard(currentPlayerIndex).value();
    }
    public boolean hasPlayerWon()
    {
        return gameState.roundWinner() == currentPlayerIndex;
    }
    /**
     * Plays move
     */
    public void makeMove(int action)
    {
        try {
            boolean playerHasPlayed = false;
            Card topCard = null;
            while(!isOver())
            {
                Action act = null;
                try {
                    if (gameState.nextPlayer() == currentPlayerIndex && !playerHasPlayed) {
                        act = getAction(action);
                        playerHasPlayed = true;
                        gameState.update(act, this.currentPlayerTopCard);
                    }
                    else if(gameState.nextPlayer() == currentPlayerIndex && playerHasPlayed)
                    {
                        this.currentPlayerTopCard = gameState.drawCard();
                        return;
                    }
                    else {
                        topCard = gameState.drawCard();
                        act = agents[gameState.nextPlayer()].playCard(topCard);
                        gameState.update(act, topCard);
                    }

                }
                catch(IllegalActionException e)
                {
                    if(gameState.nextPlayer() == currentPlayerIndex)
                    {
                        hasPlayedIllegalMove = true;
                        rando.newRound(gameState.playerState(gameState.nextPlayer()));
                        act = rando.playCard(this.currentPlayerTopCard);
                        gameState.update(act, this.currentPlayerTopCard);
                    }
                    else
                    {
                        rando.newRound(gameState.playerState(gameState.nextPlayer()));
                        act = rando.playCard(topCard);
                        gameState.update(act, topCard);
                    }

                }
                for(int p = 0; p<this.numPlayers; p++)
                    this.agents[p].see(act,this.playerStates[p]);
            }
        }
        catch (IllegalActionException e)
        {
            System.out.println("Something has gone wrong.");
            e.printStackTrace();
        }
    }
    /*
        All possible moves that can be played in the game are indexed by numbers from 1-40.
        Given a integer,return the corresponding
        action Object
     */
    public Action getAction(Integer move)
    {
        Action act = null;
        try {
            switch (move) {
                case 1:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 1) % numPlayers, Card.values()[1]);
                    break;
                case 2:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 1) % numPlayers, Card.values()[2]);
                    break;
                case 3:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 1) % numPlayers, Card.values()[3]);
                    break;
                case 4:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 1) % numPlayers, Card.values()[4]);
                    break;
                case 5:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 1) % numPlayers, Card.values()[5]);
                    break;
                case 6:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 1) % numPlayers, Card.values()[6]);
                    break;
                case 7:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 2) % numPlayers, Card.values()[7]);
                    break;
                case 8:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 2) % numPlayers, Card.values()[1]);
                    break;
                case 9:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 2) % numPlayers, Card.values()[2]);
                    break;
                case 10:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 2) % numPlayers, Card.values()[3]);
                    break;
                case 11:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 2) % numPlayers, Card.values()[4]);
                    break;
                case 12:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 2) % numPlayers, Card.values()[5]);
                    break;
                case 13:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 2) % numPlayers, Card.values()[6]);
                    break;
                case 14:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 2) % numPlayers, Card.values()[7]);
                    break;
                case 15:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 3) % numPlayers, Card.values()[1]);
                    break;
                case 16:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 3) % numPlayers, Card.values()[2]);
                    break;
                case 17:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 3) % numPlayers, Card.values()[3]);
                    break;
                case 18:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 3) % numPlayers, Card.values()[4]);
                    break;
                case 19:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 3) % numPlayers, Card.values()[5]);
                    break;
                case 20:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 3) % numPlayers, Card.values()[6]);
                    break;
                case 21:
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 3) % numPlayers, Card.values()[7]);
                    break;
                case 22:
                    act = Action.playPriest(currentPlayerIndex, (currentPlayerIndex + 1) % numPlayers);
                    break;
                case 23:
                    act = Action.playPriest(currentPlayerIndex, (currentPlayerIndex + 2) % numPlayers);
                    break;
                case 24:
                    act = Action.playPriest(currentPlayerIndex, (currentPlayerIndex + 3) % numPlayers);
                    break;
                case 25:
                    act = Action.playBaron(currentPlayerIndex, (currentPlayerIndex + 1) % numPlayers) ;
                    break;
                case 26:
                    act = Action.playBaron(currentPlayerIndex, (currentPlayerIndex + 2) % numPlayers) ;
                    break;
                case 27:
                    act = Action.playBaron(currentPlayerIndex, (currentPlayerIndex + 3) % numPlayers) ;
                    break;
                case 28:
                    act = Action.playHandmaid(currentPlayerIndex);
                    break;
                case 29:
                    act = Action.playPrince(currentPlayerIndex, currentPlayerIndex);
                    break;
                case 30:
                    act = Action.playPrince(currentPlayerIndex, (currentPlayerIndex + 1) % numPlayers);
                    break;
                case 31:
                    act = Action.playPrince(currentPlayerIndex, (currentPlayerIndex + 2) % numPlayers);
                    break;
                case 32:
                    act = Action.playPrince(currentPlayerIndex, (currentPlayerIndex + 3) % numPlayers);
                    break;
                case 33:
                    act = Action.playKing(currentPlayerIndex, (currentPlayerIndex + 1) % numPlayers);
                    break;
                case 34:
                    act = Action.playKing(currentPlayerIndex, (currentPlayerIndex + 2) % numPlayers);
                    break;
                case 35:
                    act = Action.playKing(currentPlayerIndex, (currentPlayerIndex + 3) % numPlayers);
                    break;
                case 36:
                    act = Action.playCountess(currentPlayerIndex);
                    break;
                default:
                    act = Action.playPrincess(currentPlayerIndex);
            }
        }
        catch(IllegalActionException e){/* Do Nothing*/}
        return act;
    }

    public void reset()
    {
        try {
            this.gameState.newRound();
            this.hasPlayedIllegalMove = false;
            for (int i = 0; i < this.numPlayers; i++) {
                this.playerStates[i] = gameState.playerState(i);
                this.agents[i].newRound(playerStates[i]);
            }
            while(gameState.nextPlayer() != currentPlayerIndex && !isOver())
            {
                Card topCard = gameState.drawCard();
                Action act;
                try {
                    act = agents[gameState.nextPlayer()].playCard(topCard);
                    gameState.update(act,topCard);
                }
                catch(IllegalActionException e)
                {
                    rando.newRound(gameState.playerState(gameState.nextPlayer()));
                    act = rando.playCard(topCard);
                    gameState.update(act, topCard);
                }
                for(int p = 0; p<this.numPlayers; p++)
                    this.agents[p].see(act,playerStates[p]);
            }
            this.currentPlayerTopCard = gameState.drawCard();
        }
        catch(IllegalActionException e)
        {
            System.out.println("Something has gone wrong.");
            e.printStackTrace();
        }
    }

    @Override
    public double[] toArray() {
        double[] newObservation = new double[22];
        //Set to 1 if opponent is eliminated
        newObservation[0] = (gameState.eliminated((currentPlayerIndex + 1) % numPlayers)) ? 1 : 0;
        newObservation[1] = (gameState.eliminated((currentPlayerIndex + 2) % numPlayers)) ? 1 : 0;
        newObservation[2] = (gameState.eliminated((currentPlayerIndex + 3) % numPlayers)) ? 1 : 0;

        //Store the value of the last discarded cards of each opponent
        Card lastDiscardedCardOpponentOne = gameState.getLastDiscardedCard((currentPlayerIndex + 1) % numPlayers);
        Card lastDiscardedCardOpponentTwo = gameState.getLastDiscardedCard((currentPlayerIndex + 2) % numPlayers);
        Card lastDiscardedCardOpponentThree = gameState.getLastDiscardedCard((currentPlayerIndex + 3) % numPlayers);
        newObservation[3] = (lastDiscardedCardOpponentOne != null) ? lastDiscardedCardOpponentOne.value() : 0;
        newObservation[4] = (lastDiscardedCardOpponentTwo != null) ? lastDiscardedCardOpponentTwo.value() : 0;
        newObservation[5] = (lastDiscardedCardOpponentThree != null) ? lastDiscardedCardOpponentThree.value() : 0;

        //Store the value of the current card held by the opponent if we know it
        Card opponentOneCurrentCard = gameState.getCard((currentPlayerIndex + 1) % numPlayers);
        Card opponentTwoCurrentCard = gameState.getCard((currentPlayerIndex + 2) % numPlayers);
        Card opponentThreeCurrentCard = gameState.getCard((currentPlayerIndex + 3) % numPlayers);
        newObservation[6] = (opponentOneCurrentCard != null) ? opponentOneCurrentCard.value() : 0;
        newObservation[7] = (opponentTwoCurrentCard != null) ? opponentTwoCurrentCard.value() : 0;
        newObservation[8] = (opponentThreeCurrentCard != null) ? opponentThreeCurrentCard.value() : 0;

        //Store the number of cards that havnt been played for each type
        Card[] remainingCards = gameState.unseenCards();
        for(int i = 0; i < remainingCards.length;i++)
        {
            newObservation[9] = remainingCards[i].value() == 1 ? ++ newObservation[9] : newObservation[9] ;
            newObservation[10] = remainingCards[i].value() == 2 ? ++ newObservation[10] : newObservation[10] ;
            newObservation[11] = remainingCards[i].value() == 3 ? ++ newObservation[11] : newObservation[11] ;
            newObservation[12] = remainingCards[i].value() == 4 ? ++ newObservation[12] : newObservation[12] ;
            newObservation[13] = remainingCards[i].value() == 5 ? ++ newObservation[13] : newObservation[13] ;
            newObservation[14] = remainingCards[i].value() == 6 ? ++ newObservation[14] : newObservation[14] ;
            newObservation[15] = remainingCards[i].value() == 7 ? ++ newObservation[15] : newObservation[15] ;
            newObservation[16] = remainingCards[i].value() == 8 ? ++ newObservation[16] : newObservation[16] ;
        }

        boolean opponentOneHandmaid = gameState.handmaid((currentPlayerIndex + 1) % numPlayers);
        boolean opponentTwoHandmaid = gameState.handmaid((currentPlayerIndex + 2) % numPlayers);
        boolean opponentThreeHandmaid = gameState.handmaid((currentPlayerIndex + 3) % numPlayers);
        newObservation[17] = opponentOneHandmaid ? 1 : 0;
        newObservation[18] = opponentTwoHandmaid ? 1 : 0;
        newObservation[19] = opponentThreeHandmaid ? 1 : 0;
        Card playerCurrentCard = gameState.getCard(currentPlayerIndex);
        newObservation[20] = (playerCurrentCard != null) ? playerCurrentCard.value() : 0;
        newObservation[21] = currentPlayerTopCard.value();
        return newObservation;
    }
}
