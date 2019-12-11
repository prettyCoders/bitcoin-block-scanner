package util;

import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import pojo.HttpClient;

import java.io.IOException;

@Data
public class HttpUtil {


    public static String post(String url, StringEntity stringEntity) throws IOException {
        CloseableHttpClient client = HttpClient.getInstance().getHttpClient();
//        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse response = client.execute(httpPost);
            try {
                return EntityUtils.toString(response.getEntity());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
//        } finally {
//            if (client != null) {
//                client.close();
//            }
//        }

    }

}
