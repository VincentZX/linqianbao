package com.quketech.linqianbao;

import java.util.List;
import java.util.Map;

/**
 * @author VincnetZX    17/10/9 上午11:28
 */

public class MobileInfo {
    private String imei;
    private String uuid;
    private String mac;
    private String ip;
    private List<Map<String, String>> contactlist;
    private GPSEntity gps;

    public void setGps(final GPSEntity gps) {
        this.gps = gps;
    }

    public void setIMEI(final String IMEI) {
        imei = IMEI;
    }

    public void setUUID(final String UUID) {
        uuid = UUID;
    }

    public void setMacAddres(final String macAddres) {
        mac = macAddres;
    }

    public void setIP(final String IP) {
        ip = IP;
    }

    public void setMp(final List<Map<String, String>> mp) {
        this.contactlist = mp;
    }
}
