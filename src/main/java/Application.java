import com.alibaba.fastjson.JSONObject;
import pojo.HttpClient;
import pojo.Node;
import scanner.BlockScanner;
import scanner.TransactionQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        Node node = Node.getInstance();
        node.init(
                //47.105.38.255
                "http://127.0.0.1:9050",
                "bifu-btc",
                "LKJIUJNBNDSB4561324AUYG",
                "127.0.0.1",
                9050
        );

        HttpClient.getInstance().setCredentials(
                node.getCredentialsUserName(),
                node.getCredentialsPassword(),
                node.getAuthScopeHost(),
                node.getAuthScopePort());

        BlockScanner scanner = new BlockScanner();
//        ExecutorService threadPool=Executors.newFixedThreadPool(5);
        for (int blockHeight = 607447; blockHeight < 607592; blockHeight++) {
            int finalBlockHeight = blockHeight;
//            threadPool.submit(new Runnable() {
//                @Override
//                public void run() {
                    long startTime = System.currentTimeMillis();
                    TransactionQueue transactionQueue = scanner.scan(finalBlockHeight);
                    while (!transactionQueue.productFinished()) {
                        JSONObject transaction = transactionQueue.take();
                    }
                    System.out.printf("Block height:" + finalBlockHeight + " tx count:" + scanner.getBlock().getTx().size() + " 耗时：%d ms\n", System.currentTimeMillis() - startTime);
                    System.out.println("scan finished.");
                }
//            });
//        }
//        Thread.currentThread().join();
        scanner.shutdown();
    }
}
