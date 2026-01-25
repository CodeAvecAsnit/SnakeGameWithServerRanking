package AIAgent.component;

import java.io.*;

/**
 * @author : Asnit Bakhati
 *
 */

public class FilePersister {

    private File file;

    public FilePersister(File file) {
        this.file=file;
    }

//    public boolean fileExists(){
//        System.out.println("File exists");
//        return file.exists();
//    }

    public void loadQTable(double[][] Q) throws IOException {
        InputStream is = getClass().getResourceAsStream("/model/snakeTableVast.bin");
        if (is == null) {
            throw new IOException("Resource not found: /model/xx.bin");
        }
        try (DataInputStream dis = new DataInputStream(is)) {
            for (int i = 0; i < Q.length; ++i) {
                for (int j = 0; j < Q[0].length; ++j) {
                    Q[i][j] = dis.readDouble();
                }
            }
            System.out.println("Q-Table loaded successfully from Resources.");
        }
    }
//
//    public void persistQTable(double[][] Q) throws IOException {
//        FileOutputStream fis = new FileOutputStream(file);
//        DataOutputStream dis = new DataOutputStream(fis);
//        for (int i = 0; i < Q.length; ++i) {
//            for (int j = 0; j < Q[0].length; ++j) {
//                dis.writeDouble(Q[i][j]);
//            }
//        }
//        System.out.println("File saved");
//    }
//
//    public void displayQTable(double[][] qTable){
//        System.out.println("Q-TABLE");
//        for(int i = 0 ; i < qTable.length;++i){
//            for(int j = 0 ; j < qTable[0].length;++j){
//                System.out.print(qTable[i][j]+", ");
//            }
//            System.out.println();
//        }
//    }
}
