package yction.com.vsicscomm.protocol.ips;

import android.annotation.SuppressLint;

import yction.com.vsicscomm.utils.Utils;

public class AttachmentFileName {

    @SuppressLint("DefaultLocale")
    public static String toFileName(AttachmentType at,
                                    int channel,
                                    byte alarmDeviceId,
                                    byte alarmSubType,
                                    int serialNo,
                                    String alarmId,
                                    String subffix) {
        return String.format("%s_%d_%s%s_%d_%s.%s",
                at.fileHeadCode,
                channel,
                Utils.int2HexString(alarmDeviceId, ""),
                Utils.int2HexString(alarmSubType, ""),
                serialNo,
                alarmId,
                subffix);
    }
}
