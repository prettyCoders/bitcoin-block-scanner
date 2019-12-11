package pojo;

import lombok.Data;

@Data
public class Node {
    //全节点 RPC URI
    private String nodeURI;
    //证书用户名
    private String credentialsUserName;
    //证书密码
    private String credentialsPassword;
    //证书验证范围 host
    private String authScopeHost;
    //证书验证范围 port
    private Integer authScopePort;

    private Node() {
    }

    private static class Holder {
        private static Node instance = new Node();
    }

    //调用getInstance()，实际上是获取Holder的静态属性instance
    public static Node getInstance() {
        return Node.Holder.instance;
    }


    public void init(String nodeURI) {
        this.nodeURI = nodeURI;
    }

    public void init(String nodeURI, String credentialsUserName, String credentialsPassword, String authScopeHost, Integer authScopePort) {
        this.nodeURI = nodeURI;
        this.credentialsUserName = credentialsUserName;
        this.credentialsPassword = credentialsPassword;
        this.authScopeHost = authScopeHost;
        this.authScopePort = authScopePort;
    }

}
