package com.znvoid.demo.net;

public class ClientScanResultSO {
	
	private String ip;
	
	private String hwAddress;
	private String device;
	
	private boolean isReachable;
	private String hostname;
	public ClientScanResultSO(String ip, String hwAddress, String device, boolean isReachable, String hostname) {
		super();
		this.ip = ip;
		this.hwAddress = hwAddress;
		this.device = device;
		this.isReachable = isReachable;
		this.hostname = hostname;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getHwAddress() {
		return hwAddress;
	}
	public void setHwAddress(String hwAddress) {
		this.hwAddress = hwAddress;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public boolean isReachable() {
		return isReachable;
	}
	public void setReachable(boolean isReachable) {
		this.isReachable = isReachable;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	
	
	

	
	
}
