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
import org.nd4j.jita.conf.CudaEnvironment;
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
                    1500000, //Max step
                    4000, //Max size of experience replay
                    128,     //size of batches
                    500,    //target update (hard)
                    10,     //num step noop warmup
                    0.01,   //reward scaling
                    0.9,   //gamma
                    1.0,    //td-error clipping
                    0.05f,   //min epsilon
                    40000,   //num step for eps greedy anneal
                    true    //double DQN
            );

    public static void main(String[] args) throws IOException {
        CudaEnvironment.getInstance().getConfiguration().allowMultiGPU(true);
        train();
    }

    public static void train() throws IOException {
        DataManager dataManager = new DataManager(true);
        DQNFactoryStdDense.Configuration LOVE_NET =
                DQNFactoryStdDense.Configuration.builder()
                        .l2(0).updater(new
                        RmsProp(0.001))
                        .numHiddenNodes(512)
                        .numLayer(2)
                        .build();
        Agent[] agents= new Agent[]{new ReinforcementAgent(),new ReinforcementAgent(),new ReinforcementAgent(),new ReinforcementAgent()};
        Random rand = new Random(System.currentTimeMillis());
        LoveLetter game = new LoveLetter(agents,rand.nextInt(4));
        LoveLetterMDP mdp = new LoveLetterMDP(game);
        QLearningDiscrete<LoveLetter> dql = new QLearningDiscreteDense<LoveLetter>(mdp, LOVE_NET, Love_QL, dataManager);
        dql.train();
        dql.getPolicy().save("loveletter.model");
        mdp.close();
        System.out.println("Illegal Move Training Complete!");
    }
}
