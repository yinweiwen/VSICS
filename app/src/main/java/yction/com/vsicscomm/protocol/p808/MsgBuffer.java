package yction.com.vsicscomm.protocol.p808;

import android.annotation.SuppressLint;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MsgBuffer {
    // 流水号 - 对应消息缓存
    private Map<Integer, Msg> _cache;

    // 检查缓存超时间隔ms
    private static final int refreshPeriodMillis = 5 * 60 * 1000;
    // 分包缓存超时时间ms
    private static final int deadtime = 10 * 60 * 1000;

    @SuppressLint("UseSparseArrays")
    public MsgBuffer() {
        _cache = new HashMap<>();
        Timer _timer = new Timer();
        _timer.schedule(new TimerTask() {
            @Override
            public void run() {
                RefreshCaches();
            }
        }, refreshPeriodMillis, refreshPeriodMillis);
    }

    public synchronized Msg AddAndGet(MsgFrame frame) {
        if (!frame.getHead().property.SP)
            return new Msg(frame, false);
        int mn = frame.getHead().msgNo;
        if (_cache.containsKey(mn)) {
            Msg msg = _cache.get(mn);
            msg.add(frame);
            if (msg.isReady()) {
                _cache.remove(mn);
            }
            return msg;
        } else {
            Msg msg = new Msg(frame, false);
            _cache.put(mn, msg);
            return msg;
        }
    }

    // 删除超时的包缓存
    private synchronized void RefreshCaches() {
        long now = new Date().getTime();
        for (Iterator<Map.Entry<Integer, Msg>> it = _cache.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Integer, Msg> item = it.next();
            if (now - item.getValue().getTime() > deadtime)
                it.remove();
        }
    }
}
