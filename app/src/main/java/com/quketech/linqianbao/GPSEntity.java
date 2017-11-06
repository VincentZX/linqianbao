package com.quketech.linqianbao;

/**
 * @author VincnetZX    17/10/9 上午11:55
 */

public class GPSEntity {
    private double latitude ;
    private double longitude;

    public GPSEntity(final double latitude, final double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }
}
