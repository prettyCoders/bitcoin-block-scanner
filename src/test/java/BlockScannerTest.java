import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pojo.HttpClient;
import pojo.Node;
import scanner.BlockScanner;

public class BlockScannerTest {

    @BeforeEach
    public void setup() {
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
    }

    @Test
    public void testScanner(){
        for (int blockHeight = 607447; blockHeight < 607592; blockHeight++) {
            long startTime = System.currentTimeMillis();
            BlockScanner scanner = new BlockScanner();
            scanner.scan(blockHeight);

            while (!scanner.isFinished()) {
                if (!scanner.isEmptyQueue()) {
                    JSONObject transaction = scanner.take();
                }
            }
            System.out.printf("Block height:" + blockHeight + " tx count:" + scanner.getBlock().getTx().size() + " 耗时：%d ms\n", System.currentTimeMillis() - startTime);
            System.out.println("scan finished.");
            scanner.shutdown();
        }
    }
}
