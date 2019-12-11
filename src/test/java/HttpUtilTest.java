import com.alibaba.fastjson.JSONObject;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Test;
import pojo.Node;
import pojo.HttpClient;
import pojo.RequestForm;
import util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpUtilTest {

    @Test
    public void testPost() {
        Node node = Node.getInstance();
        node.init(
                "http://118.190.59.62:8092",
                "bifu-btc",
                "LKJIUJNBNDSB4561324AUYG",
                "118.190.59.62",
                8092
        );

        HttpClient.getInstance().setCredentials(
                node.getCredentialsUserName(),
                node.getCredentialsPassword(),
                node.getAuthScopeHost(),
                node.getAuthScopePort());
        List<Object> params = new ArrayList<>();
        params.add("6b2308165f9797f2b87c4d59930394289de2e03ad7b04cd5970d53eb8ea974e8");
        params.add(true);//解码
        RequestForm requestForm = new RequestForm("getrawtransaction", params);
        StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(requestForm), ContentType.APPLICATION_JSON);
        try {
            String response = HttpUtil.post(node.getNodeURI(), stringEntity);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
