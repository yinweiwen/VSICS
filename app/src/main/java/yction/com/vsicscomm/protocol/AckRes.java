package yction.com.vsicscomm.protocol;

import yction.com.vsicscomm.protocol.p808.Msg;

public class AckRes {
    public Errors error;
    public String msg;
    public int retryCnt;
    public Msg ack;

    public AckRes(Errors e) {
        error = e;
    }

    public AckRes(Errors e, String m) {
        error = e;
        msg = m;
    }

    public AckRes(Errors e, Msg m) {
        error = e;
        ack = m;
    }
}
