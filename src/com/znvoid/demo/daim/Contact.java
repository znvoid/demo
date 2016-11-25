package com.znvoid.demo.daim;

public class Contact {

	private String id;
	
	private String name;
	
	private String head;
	
	private String  lastMsg;
	
	private String time;

	public Contact(String id, String name, String head, String lastMsg, String time) {
		super();
		this.id = id;
		this.name = name;
		this.head = head;
		this.lastMsg = lastMsg;
		this.time = time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getLastMsg() {
		return lastMsg;
	}

	public void setLastMsg(String lastMsg) {
		this.lastMsg = lastMsg;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
}
