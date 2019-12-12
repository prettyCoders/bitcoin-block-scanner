import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pojo.HttpClient;
import pojo.Node;
import scanner.BlockScanner;
import scanner.TransactionQueue;

public class BlockScannerTest {

    @BeforeEach
    public void setup() {
        Node node = Node.getInstance();
        node.init(
                //47.105.38.255
                "http://47.105.38.255:9050",
                "bifu-btc",
                "LKJIUJNBNDSB4561324AUYG",
                "47.105.38.255",
                9050
        );

        HttpClient.getInstance().setCredentials(
                node.getCredentialsUserName(),
                node.getCredentialsPassword(),
                node.getAuthScopeHost(),
                node.getAuthScopePort());
    }

    @Test
    public void testScanner() {
//        BlockScanner scanner = new BlockScanner();
//        for (int blockHeight = 607447; blockHeight < 607592; blockHeight++) {
//            long startTime = System.currentTimeMillis();
//            TransactionQueue transactionQueue = scanner.scan(blockHeight);
//            while (!transactionQueue.productFinished()) {
//                JSONObject transaction = transactionQueue.take();
//            }
//            System.out.printf("Block height:" + blockHeight + " tx count:" + scanner.getBlock().getTx().size() + " 耗时：%d ms\n", System.currentTimeMillis() - startTime);
//            System.out.println("scan finished.");
//        }
//        scanner.shutdown();
    }
}
