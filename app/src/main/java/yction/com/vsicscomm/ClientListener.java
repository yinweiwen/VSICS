package yction.com.vsicscomm;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import yction.com.vsicscomm.protocol.AcrCode;
import yction.com.vsicscomm.protocol.ips.cmd.AlarmAttachmentUploadReq;
import yction.com.vsicscomm.protocol.ips.cmd.BasicInfoReq;
import yction.com.vsicscomm.protocol.ips.cmd.ParamQueryReq;
import yction.com.vsicscomm.protocol.ips.cmd.ParamQuerySpecialReq;
import yction.com.vsicscomm.protocol.ips.cmd.ParamSetReq;
import yction.com.vsicscomm.protocol.p808.CmdResp;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.protocol.p808.Protocol;
import yction.com.vsicscomm.utils.Utils;


public class ClientListener implements TcpClientListener {

    private final static String LOG_TAG = "CLIENT_LISTENER";

    @SuppressLint("UseSparseArrays")
    private final Map<Integer, CmdResp> _cmds = new HashMap<>();

    public ClientListener(NetService service) {
        registry(new BasicInfoReq());
        registry(new ParamQueryReq());
        registry(new ParamQuerySpecialReq());
        registry(new ParamSetReq());
        registry(new AlarmAttachmentUploadReq(service));
    }

    public void registry(CmdResp cmd) {
        _cmds.put(cmd._mid.getCode(), cmd);
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
            Log.i(LOG_TAG, "处理消息:" + Utils.int2HexString(cmd._mid.getCode()) + " " + cmd._mid.getDesc());
            return cmd.onMsg(msg);
        }
        return null;
    }
}
