package pojo;

import lombok.Data;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Http 客户端
 */
@Data
public class HttpClient {

    //httpclient 用于发送 Http 请求
    private CloseableHttpClient httpClient = HttpClients.createDefault();

    private HttpClient() {
    }

    //在静态内部类中持有Singleton的实例，并且可被直接初始化
    private static class Holder {
        private static HttpClient instance = new HttpClient();
    }

    //调用getInstance()，实际上是获取Holder的静态属性instance
    public static HttpClient getInstance() {
        return Holder.instance;
    }

    /**
     * 为 HttpClient 配置证书
     */
    public void setCredentials(String credentialsUserName, String credentialsPassword, String authScopeHost, Integer authScopePort) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        credentialsProvider.setCredentials(
                new AuthScope(authScopeHost, authScopePort),
                new UsernamePasswordCredentials(credentialsUserName, credentialsPassword));

        httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
    }

}