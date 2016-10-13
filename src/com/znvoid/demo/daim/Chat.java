package com.znvoid.demo.daim;


public class Chat {
	public static final int MESSAGE_RECEIVE = 0;
	 public static final int MESSAGE_SEND = 1;
    
    private String author;
    private String message;
    private int direction;
    
    

   

	public Chat(String author, String message, int direction) {
		super();
		this.author = author;
		this.message = message;
		this.direction = direction;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public Chat(String author, String message) {
        this.message = message;
        this.author = author;
    }

    public void setAuthor(String author) {
		this.author = author;
	}

	public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
