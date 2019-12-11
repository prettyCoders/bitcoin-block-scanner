package scanner;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.Block;
import pojo.Node;
import pojo.RequestForm;
import util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * 区块扫描器
 * 提供常用区块数据的查询方法
 * 线程池可定制
 * 查询出来的详细交易数据将 offer 进队列
 */
public class BlockScanner {


    private static final Logger LOGGER = LoggerFactory.getLogger(BlockScanner.class);

    /**
     * 待扫描的区块
     */
    private Block block;

    public Block getBlock(){
        return this.block;
    }

    /**
     * 存放交易数据的队列，必须使用线程安全的队列
     */
    private final Queue<JSONObject> queue;


    /**
     * 交易队列的提交次数，用于判断任务是否完成（queueSubmitCount==区块交易数量）
     */
    private volatile int queueSubmitCount = 0;

    private ForkJoinPool forkJoinPool;

    /**
     * 并发数
     */
     private final int threadCount;


    /**
     * 默认线程池线程数= JVM 可用处理器数量+1
     */
    private final static int DEFAULT_THREAD_COUNT = Runtime.getRuntime().availableProcessors() + 1;


    /**
     * 默认构造器
     */
    public BlockScanner() {
        this.threadCount = DEFAULT_THREAD_COUNT;
        queue = new ConcurrentLinkedQueue<>();
    }

    /**
     * 可指定线程池线程数
     *
     * @param threadCount 线程数
     */
    public BlockScanner(int threadCount) {
        this.threadCount = threadCount;
        queue = new ConcurrentLinkedQueue<>();
    }

    public boolean isFinished() {
        //所有交易查询的结果已经 push 进了队列，并且 queue.isEmpty()，也就是说已经全部被 take
        return block.getTx().size() == getQueueSubmitCount() && queue.isEmpty();
    }

    public boolean isEmptyQueue() {
        return queue.isEmpty();
    }

    /**
     * 扫描
     *
     * @param blockHeight 区块高度
     */
    public void scan(int blockHeight) {

        String blockHash = queryBlockHash(blockHeight);
        block = queryBlockDataByHash(blockHash);
        // 创建包含Runtime.getRuntime().availableProcessors()返回值作为个数的并行线程的ForkJoinPool
        forkJoinPool = new ForkJoinPool(threadCount);
        // 提交可分解的 TransactionQuerier 任务
        int transactionCount = block.getTx().size();
        //ForkJoin 会不断拆分任务，如果总任务数量为奇数，则会造成丢失
        if (transactionCount % 2 != 0) {
            transactionCount++;
        }
        forkJoinPool.execute(new TransactionQuerier(block.getTx(), 0, transactionCount));
    }

    /**
     * 关闭扫描器，释放资源
     */
    public void shutdown() {
        if (!forkJoinPool.isShutdown()) {
            System.out.println("Scanner shutdown.");
            forkJoinPool.shutdown();
        }
    }


    private void addQueueSubmitCount(){
        synchronized (this){
            queueSubmitCount++;
        }
    }

    private int getQueueSubmitCount(){
        synchronized (this){
            return queueSubmitCount;
        }
    }


    private String queryBlockHash(int height) {
        List<Object> params = new ArrayList<>();
        params.add(height);
        RequestForm requestForm = new RequestForm("getblockhash", params);
        StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(requestForm), ContentType.APPLICATION_JSON);
        String response = null;
        try {
            response = HttpUtil.post(Node.getInstance().getNodeURI(), stringEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONObject.parseObject(response).getString("result");
    }

    private Block queryBlockDataByHash(String hash) {
        List<Object> params = new ArrayList<>();
        params.add(hash);
        RequestForm requestForm = new RequestForm("getblock", params);
        StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(requestForm), ContentType.APPLICATION_JSON);
        String response = null;
        try {
            response = HttpUtil.post(Node.getInstance().getNodeURI(), stringEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(JSONObject.parseObject(response).getJSONObject("result").toString(), Block.class);
    }

    public JSONObject take() {
        return queue.poll();
    }

    public int queueSize() {
        return queue.size();
    }


    /**
     * 交易查询器 使用ForkJoin
     */
    private class TransactionQuerier extends RecursiveAction {
        private final List<String> txHashes;
        private int start;
        private int end;

        public TransactionQuerier(List<String> txHashes, int start, int end) {
            this.txHashes = txHashes;
            this.start = start;
            this.end = end;
        }

        // 每个"小任务"最多查询多少笔交易（交易数量/并发数）
        private final int THRESHOLD = block.getTx().size()/threadCount;

        @Override
        protected void compute() {
            // 当end-start的值小于MAX时候，开始打印
            if ((end - start) < THRESHOLD) {
                for (int i = start; i < end; i++) {
                    queue.offer(queryRawTransaction(txHashes.get(i)));
                    addQueueSubmitCount();
//                    System.out.println("队列提交次数：" + getQueueSubmitCount());
                }
            } else {
                // 将大任务分解成两个小任务
                int middle = (start + end) / 2;
                invokeAll(new TransactionQuerier(txHashes, start, middle), new TransactionQuerier(txHashes, middle, end));
            }
        }

        private JSONObject queryRawTransaction(String txHash) {
            List<Object> params = new ArrayList<>();
            params.add(txHash);
            params.add(true);//解码
            RequestForm requestForm = new RequestForm("getrawtransaction", params);
            StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(requestForm), ContentType.APPLICATION_JSON);
            String response = null;
            try {
                response = HttpUtil.post(Node.getInstance().getNodeURI(), stringEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return JSONObject.parseObject(response).getJSONObject("result");
        }
    }
}
