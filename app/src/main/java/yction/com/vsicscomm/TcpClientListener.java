package yction.com.vsicscomm;

import yction.com.vsicscomm.protocol.p808.Msg;

public interface TcpClientListener {
    void onConnection();

    void onClose(String error);

    void onError(String error);

    Msg onMsg(Msg msg);

    Msg onSeperatePackage(Msg msg);
}
