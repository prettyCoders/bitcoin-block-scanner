package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 调用infura接口需要的表单
 */
@Data
@AllArgsConstructor
public class RequestForm {

    String jsonrpc="1.0";

    String method;

    List<Object> params;

    Integer id=1;

    public RequestForm(String method, List<Object> params) {
        this.method = method;
        this.params = params;
    }
}
