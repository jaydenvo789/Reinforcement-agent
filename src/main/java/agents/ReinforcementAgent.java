package agents;
import loveletter.*;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.api.ndarray.INDArray;
import java.io.IOException;

public class ReinforcementAgent implements Agent{

    DQNPolicy dqn;
    State state;
    Card drawedCard;
    public ReinforcementAgent() throws IOException
    {
        dqn = DQNPolicy.load("loveletter-unweighted.model");
    }
    @Override
    public void newRound(State start) {
        this.state = start;
    }

    public String toString(){return "Your Mum";}
    @Override
    public void see(Action act, State results) {
        this.state = results;
    }

    @Override
    public Action playCard(Card c) {
        this.drawedCard = c;
        INDArray observations = Nd4j.create(getObservation());
        Integer action = this.dqn.nextAction(observations.reshape(new long[] {1, observations.length()}));

        return getAction(action + 1);
    }

    public double[] getObservation()
    {
        double[] newObservation = new double[22];
        int currentPlayerIndex = state.getPlayerIndex();
        int numPlayers = state.numPlayers();
        //Store 1 if opponent is eliminated. Else 0
        newObservation[0] = (state.eliminated((currentPlayerIndex + 1) % numPlayers)) ? 1 : 0;
        newObservation[1] = (state.eliminated((currentPlayerIndex + 2) % numPlayers)) ? 1 : 0;
        newObservation[2] = (state.eliminated((currentPlayerIndex + 3) % numPlayers)) ? 1 : 0;

        //Store the value of the last discarded cards of each opponent
        Card lastDiscardedCardOpponentOne = state.getLastDiscardedCard((currentPlayerIndex + 1) % numPlayers);
        Card lastDiscardedCardOpponentTwo = state.getLastDiscardedCard((currentPlayerIndex + 2) % numPlayers);
        Card lastDiscardedCardOpponentThree = state.getLastDiscardedCard((currentPlayerIndex + 3) % numPlayers);
        newObservation[3] = (lastDiscardedCardOpponentOne != null) ? lastDiscardedCardOpponentOne.value() : 0;
        newObservation[4] = (lastDiscardedCardOpponentTwo != null) ? lastDiscardedCardOpponentTwo.value() : 0;
        newObservation[5] = (lastDiscardedCardOpponentThree != null) ? lastDiscardedCardOpponentThree.value() : 0;

        //Store the value of the current card held by the opponent if we know it
        Card opponentOneCurrentCard = state.getCard((currentPlayerIndex + 1) % numPlayers);
        Card opponentTwoCurrentCard = state.getCard((currentPlayerIndex + 2) % numPlayers);
        Card opponentThreeCurrentCard = state.getCard((currentPlayerIndex + 3) % numPlayers);
        newObservation[6] = (opponentOneCurrentCard != null) ? opponentOneCurrentCard.value() : 0;
        newObservation[7] = (opponentTwoCurrentCard != null) ? opponentTwoCurrentCard.value() : 0;
        newObservation[8] = (opponentThreeCurrentCard != null) ? opponentThreeCurrentCard.value() : 0;

        //Store the number of cards that havn't been played for each type
        Card[] remainingCards = state.unseenCards();
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

        boolean opponentOneHandmaid = state.handmaid((currentPlayerIndex + 1) % numPlayers);
        boolean opponentTwoHandmaid = state.handmaid((currentPlayerIndex + 2) % numPlayers);
        boolean opponentThreeHandmaid = state.handmaid((currentPlayerIndex + 3) % numPlayers);
        newObservation[17] = opponentOneHandmaid ? 1 : 0;
        newObservation[18] = opponentTwoHandmaid ? 1 : 0;
        newObservation[19] = opponentThreeHandmaid ? 1 : 0;
        newObservation[20]  = state.getCard(state.getPlayerIndex()).value();
        newObservation[21] = drawedCard.value();
        return newObservation;
    }

    /*
        All possible moves that can be played in the game are indexed by numbers from 1-40.
        Given a integer , return the corresponding
        action Object
     */
    public Action getAction(Integer move)
    {
        int currentPlayerIndex = this.state.getPlayerIndex();
        int numPlayers = this.state.numPlayers();
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
                    act = Action.playGuard(currentPlayerIndex, (currentPlayerIndex + 1) % numPlayers, Card.values()[7]);
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


}
