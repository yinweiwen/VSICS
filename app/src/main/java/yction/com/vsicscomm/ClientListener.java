package yction.com.vsicscomm;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import yction.com.vsicscomm.protocol.AcrCode;
import yction.com.vsicscomm.protocol.p808.CmdResp;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.protocol.p808.Protocol;


public class ClientListener implements TcpClientListener {

    private final static String LOG_TAG = "CLIENT_LISTENER";

    @SuppressLint("UseSparseArrays")
    private final Map<Integer, CmdResp> _cmds = new HashMap<>();

    public ClientListener() {

    }

    public void registry(int msgId, CmdResp cmd) {
        _cmds.put(msgId, cmd);
    }

    @Override
    public void onConnection() {
        Log.i(LOG_TAG, "connected");
    }

    @Override
    public void onClose(String error) {
        Log.i(LOG_TAG, "closed");
    }

    @Override
    public void onError(String error) {
        Log.i(LOG_TAG, "error:" + error);
    }

    // 分包应答
    @Override
    public Msg onSeperatePackage(Msg msg) {
        CmdResp cmd = _cmds.get(msg.msgId());
        if (cmd != null) return cmd.onSeperatePackage(msg);
        return Protocol.commAck(msg, AcrCode.Success);
    }

    @Override
    public Msg onMsg(Msg msg) {
        CmdResp cmd = _cmds.get(msg.msgId());
        if (cmd != null) {
            return cmd.onMsg(msg);
        }
        return null;
    }
}
