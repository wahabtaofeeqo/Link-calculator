package com.taocoder.linkcalculator;

import java.util.List;

public class ThreatLog {

    private String host;
    private boolean detected;
    private double scanTime;
    private int nullLinks;
    private int foreignLinks;
    private int noLinks;
    private int allLinks;
    private String title;
    private int type;

    private List<String> nulls;
    private List<String> foreigns;
    private List<String> inners;

    private static final ThreatLog log = new ThreatLog();

    public static ThreatLog getInstance() {
        return log;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isDetected() {
        return detected;
    }

    public void setDetected(boolean detected) {
        this.detected = detected;
    }

    public double getScanTime() {
        return scanTime;
    }

    public void setScanTime(double scanTime) {
        this.scanTime = scanTime;
    }

    public boolean getDetected() {
        return detected;
    }

    public int getNullLinks() {
        return nullLinks;
    }

    public void setNullLinks(int nullLinks) {
        this.nullLinks = nullLinks;
    }

    public int getForeignLinks() {
        return foreignLinks;
    }

    public void setForeignLinks(int foreignLinks) {
        this.foreignLinks = foreignLinks;
    }

    public int getNoLinks() {
        return noLinks;
    }

    public void setNoLinks(int noLinks) {
        this.noLinks = noLinks;
    }

    public List<String> getNulls() {
        return nulls;
    }

    public void setNulls(List<String> nulls) {
        this.nulls = nulls;
    }

    public int getAllLinks() {
        return allLinks;
    }

    public void setAllLinks(int allLinks) {
        this.allLinks = allLinks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getForeigns() {
        return foreigns;
    }

    public void setForeigns(List<String> foreigns) {
        this.foreigns = foreigns;
    }

    public List<String> getInners() {
        return inners;
    }

    public void setInners(List<String> inners) {
        this.inners = inners;
    }
}

