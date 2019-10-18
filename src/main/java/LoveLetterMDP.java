import loveletter.LoveLetter;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
public class LoveLetterMDP implements MDP<LoveLetter, Integer, DiscreteSpace> {

    private LoveLetter game;
    final protected DiscreteSpace discreteSpace;
    final protected ObservationSpace<LoveLetter> observationSpace;


    public LoveLetterMDP(LoveLetter game)
    {
        this.game = game;
        discreteSpace = new DiscreteSpace(37); // 40 Possible actions
        observationSpace = new ArrayObservationSpace<>(new int[] {22} );
    }

    @Override
    public ObservationSpace<LoveLetter> getObservationSpace()
    {
        return observationSpace;
    }

    @Override
    public DiscreteSpace getActionSpace() {

        return discreteSpace;
    }

    @Override
    public LoveLetter reset()
    {
        game.reset();
        return game;
    }

    @Override
    public void close()
    {
        System.out.println("Finished Training!");
    }

    @Override
    public StepReply<LoveLetter> step(Integer integer)
    {
        int reward = 0;
        //Somehow our client got eliminated before they could even make a move
        if(isDone())
        {
            return new StepReply<LoveLetter>(this.game, 0, isDone(), null);
        }
        else
        {
            this.game.makeMove(integer+1);
            if(isDone())
            {
                if(game.hasPlayedIllegalMove() || (game.getScore() == 0 ))
                {
                    reward = -1;
                }
                else
                {
                    reward = 1;
                }
            }
            return new StepReply<LoveLetter>(this.game, reward, isDone(), null);
        }

    }

    @Override
    public boolean isDone()
    {
        return game.isOver() || game.hasPlayedIllegalMove();
    }

    @Override
    public MDP<LoveLetter, Integer, DiscreteSpace> newInstance() {

        return null;
    }
}
