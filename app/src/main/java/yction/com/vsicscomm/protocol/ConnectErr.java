package yction.com.vsicscomm.protocol;

public enum ConnectErr {

    None, // 成功
    TcpErr, // tcp连接错误
    ServerErr, // 服务端异常
    Refuse, // 拒绝登录
    Repeat; // 重复上线

    // 重新连接前的延时ms
    public int sleepPolicy() {
        switch (this.name()) {
            case "TcpErr":
                return 1000;
            case "Refuse":
            case "Repeat":
                return 10 * 1000;
            case "ServerErr":
                return 60 * 1000;
            default:
                return 1000;
        }
    }
}
