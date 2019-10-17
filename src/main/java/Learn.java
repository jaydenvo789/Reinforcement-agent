import agents.*;
import loveletter.*;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscrete;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.util.DataManager;
import org.nd4j.linalg.learning.config.RmsProp;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.IDQN;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

import java.io.IOException;
import java.io.File;

public class Learn {


    public static QLearning.QLConfiguration Love_QL =
            new QLearning.QLConfiguration(
                    123,    //Random seed
                    16,    //Max step By epoch
                    10000, //Max step
                    2000, //Max size of experience replay
                    32,     //size of batches
                    500,    //target update (hard)
                    10,     //num step noop warmup
                    0.01,   //reward scaling
                    0.9,   //gamma
                    1.0,    //td-error clipping
                    0.1f,   //min epsilon
                    4000,   //num step for eps greedy anneal
                    true    //double DQN
            );

    public static void main(String[] args) throws IOException {
        train();
    }

    public static void train() throws IOException {
        QLearningDiscrete<LoveLetter> dql;
        DataManager dataManager = new DataManager(true);
        LoveLetterMDP mdp;
        boolean modelFileExist = new File("loveletter.model").exists();
        if (!modelFileExist) {
            DQNFactoryStdDense.Configuration LOVE_NET =
                    DQNFactoryStdDense.Configuration.builder()
                            .l2(0).updater(new
                            RmsProp(0.001))
                            .numHiddenNodes(256)
                            .numLayer(2)
                            .build();
            mdp = new LoveLetterMDP();
            dql = new QLearningDiscreteDense<LoveLetter>(mdp, LOVE_NET, Love_QL, dataManager);
            dql.getPolicy().save("loveletter.model");
            mdp.close();

            //First Train Model to avoid illegal moves
            IDQN dqn = DQN.load("loveletter.model");
            Agent[] agents= new Agent[]{new ReinforcementAgent(),new RandomAgent(),new RandomAgent(),new RandomAgent()};
            Random rand = new Random(System.currentTimeMillis());
            LoveLetter game = new LoveLetter(agents,0);
            mdp = new LoveLetterMDP(game,true);
            dql = new QLearningDiscreteDense<LoveLetter>(mdp, dqn, Love_QL, dataManager);
//            dql.train();
            dql.getPolicy().save("loveletter.model");
            System.out.println("Illegal Move Training Complete!");
        }

        while(true)
        {
            System.out.println("ROUND---------------------------");
            IDQN dqn = DQN.load("loveletter.model");
            Agent[] agents= new Agent[]{new ReinforcementAgent(),new RandomAgent(),new RandomAgent(),new RandomAgent()};
            Random rand = new Random(System.currentTimeMillis());
            LoveLetter game = new LoveLetter(agents, 0);
            mdp = new LoveLetterMDP(game,true);
            dql = new QLearningDiscreteDense<LoveLetter>(mdp, dqn, Love_QL, dataManager);
            dql.train();
            dql.getPolicy().save("loveletter.model");
            BufferedWriter writer = new BufferedWriter(new FileWriter("eliminated.txt", true));
            writer.append(Integer.toString(mdp.numIllegalMovesMade));
            writer.newLine();
            writer.close();
            mdp.close();
        }

//        for(int i = 0; i< 10; i++)
//        {
//            IDQN dqn = DQN.load("loveletter.model");
//            Agent[] agents= new Agent[]{new ReinforcementAgent(),new ReinforcementAgent(),new ReinforcementAgent(),new ReinforcementAgent()};
//            Random rand = new Random(System.currentTimeMillis());
//            LoveLetter game = new LoveLetter(agents,rand.nextInt(4));
//            mdp = new LoveLetterMDP(game,true);
//            dql = new QLearningDiscreteDense<LoveLetter>(mdp, dqn, Love_QL, dataManager);
//            dql.train();
//            dql.getPolicy().save("loveletter.model");
//            mdp.close();
//        }

    }
}
