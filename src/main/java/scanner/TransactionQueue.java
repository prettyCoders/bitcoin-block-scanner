package scanner;

import com.alibaba.fastjson.JSONObject;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 用于存放某个区块的交易，也就是数据缓冲队列，
 * 该队列是定制队列，可以指定数据总量，到达这个数量就说明所有数据已生产完成，等待消费
 */
public class TransactionQueue {

    /**
     * 用于存放生产的交易数据
     */
    private Queue<JSONObject> transactionsQueue;

    /**
     * 数据总量
     */
    private final int transactionCount;

    /**
     * 当前入队数量
     */
    private int count;

    /**
     * 可指定数据总量构造方法
     *
     * @param transactionCount 数据总量
     */
    public TransactionQueue(int transactionCount) {
        this.transactionCount = transactionCount;
        transactionsQueue = new ConcurrentLinkedQueue<>();
    }

    /**
     * 虽然 ConcurrentLinkedQueue 是线程安全的队列，但是需要统计入队数据的数量，
     * 还要唤醒由于队列为空进而被阻塞的 take 线程，所以，加上同步控制
     *
     * @param transactionData 交易
     */
    public void offer(JSONObject transactionData) throws Exception {
        synchronized (this) {
            if (count + 1 > transactionCount) {
                throw new Exception("超过数据总量");
            }
            transactionsQueue.offer(transactionData);
            count++;
            notifyAll();
        }
    }

    /**
     * 拿数据，如果队列空，则阻塞
     *
     * @return 交易数据
     */
    public JSONObject take() {
        synchronized (this) {
            while (transactionsQueue.isEmpty()) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return transactionsQueue.poll();
        }
    }

    /**
     * 查询是否所有数据都已经生产完成
     */
    public boolean productFinished() {
        return count == transactionCount;
    }

}
