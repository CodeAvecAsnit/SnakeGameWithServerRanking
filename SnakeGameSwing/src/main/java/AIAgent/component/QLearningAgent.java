package AIAgent.component;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @author : Asnit Bakhati
 *
 */
public abstract class QLearningAgent {

    protected double[][] qTable;
    protected double alpha;
    protected final double gamma;
    public double epsilon;
    protected final Random random;
    private FilePersister filePersister;

    protected QLearningAgent(double alpha, double gamma, double epsilon,int len,int bre,String fileName) {
        this.qTable = new double[len][bre];
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilon = epsilon;
        this.random = new Random();
        this.filePersister = new FilePersister(new File(fileName));
    }

    public void loadQTable() throws IOException{
        filePersister.loadQTable(qTable);
    }

    public void saveQTable() throws IOException{
        filePersister.persistQTable(qTable);
    }

    public void seeQTable() {
        filePersister.displayQTable(qTable);
    }
}


