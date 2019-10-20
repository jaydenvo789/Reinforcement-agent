import agents.*;
import loveletter.*;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscrete;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.util.DataManager;
import org.nd4j.linalg.learning.config.RmsProp;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.nd4j.jita.conf.CudaEnvironment;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

import java.io.IOException;
import java.io.File;

public class Learn {


    public static QLearning.QLConfiguration Love_QL_Illegal_moves =
            new QLearning.QLConfiguration(
                    123,    //Random seed
                    20,    //Max step By epoch
                    1000000, //Max step
                    10000, //Max size of experience replay
                    64,     //size of batches
                    500,    //target update (hard)
                    10,     //num step noop warmup
                    0.01,   //reward scaling
                    0.9,   //gamma
                    1.0,    //td-error clipping
                    0.2f,   //min epsilon
                    10000,   //num step for eps greedy anneal
                    true    //double DQN
            );
    public static QLearning.QLConfiguration Love_QL_Self_Play =
            new QLearning.QLConfiguration(
                    123,    //Random seed
                    20,    //Max step By epoch
                    500000, //Max step
                    10000, //Max size of experience replay
                    64,     //size of batches
                    500,    //target update (hard)
                    10,     //num step noop warmup
                    0.01,   //reward scaling
                    0.9,   //gamma
                    1.0,    //td-error clipping
                    0.05f,   //min epsilon
                    50000,   //num step for eps greedy anneal
                    true    //double DQN
            );

    //Configuration for the Double Q network
    public static   DQNFactoryStdDense.Configuration LOVE_NET =
            DQNFactoryStdDense.Configuration.builder()
                    .l2(0).updater(new
                    RmsProp(0.001))
                    .numHiddenNodes(512)
                    .numLayer(2)
                    .build();

    public static void main(String[] args) throws IOException {
        CudaEnvironment.getInstance().getConfiguration().allowMultiGPU(true);
//        trainIllegalMoves();
//        trainSelfPlay(1);
        trainRandom();
//        trainSelfPlay(100);
    }

    public static void trainIllegalMoves() throws IOException {
        DataManager dataManager = new DataManager(true);
        Agent[] agents= new Agent[]{new ReinforcementAgent("loveletter-unweighted.model"),new RandomAgent(),new RandomAgent(),new RandomAgent()};
        Random rand = new Random(System.currentTimeMillis());
        LoveLetter game = new LoveLetter(agents,0);
        LoveLetterMDP mdp = new LoveLetterMDP(game);
        QLearningDiscrete<LoveLetter> dql = new QLearningDiscreteDense<LoveLetter>(mdp, LOVE_NET, Love_QL_Illegal_moves , dataManager);
        dql.train();
        dql.getPolicy().save("loveletter.model");
        mdp.close();
        System.out.println("Illegal Move Training Complete!");
    }

    public static void trainSelfPlay(int numRounds) throws IOException
    {
        for(int i = 0 ; i <numRounds;i++) {
            DataManager dataManager = new DataManager(true);
            Agent[] agents = new Agent[]{
                    new ReinforcementAgent("loveletter.model"),
                    new ReinforcementAgent("loveletter.model"),
                    new ReinforcementAgent("loveletter.model"),
                    new ReinforcementAgent("loveletter.model")};
            Random rand = new Random(System.currentTimeMillis());
            LoveLetter game = new LoveLetter(agents, rand.nextInt(4));
            LoveLetterMDP mdp = new LoveLetterMDP(game);
            IDQN dqn = DQN.load("loveletter.model");
            QLearningDiscrete<LoveLetter> dql = new QLearningDiscreteDense<>(mdp, dqn, Love_QL_Self_Play, dataManager);
            dql.train();
            dql.getPolicy().save("loveletter.model");
            mdp.close();
            System.out.println("Training Complete");
        }
    }

    public static void trainRandom() throws IOException
    {
        DataManager dataManager = new DataManager(true);
        Agent[] agents = new Agent[]{
                new ReinforcementAgent("loveletter.model"),
                new RandomAgent(),
                new RandomAgent(),
                new RandomAgent()};
        Random rand = new Random(System.currentTimeMillis());
        LoveLetter game = new LoveLetter(agents, rand.nextInt(0));
        LoveLetterMDP mdp = new LoveLetterMDP(game);
        IDQN dqn = DQN.load("loveletter.model");
        QLearningDiscrete<LoveLetter> dql = new QLearningDiscreteDense<>(mdp, dqn, Love_QL_Self_Play, dataManager);
        dql.train();
        dql.getPolicy().save("loveletter.model");
        mdp.close();
        System.out.println("Training Complete");
    }

}
