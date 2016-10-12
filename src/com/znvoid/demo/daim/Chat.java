package com.znvoid.demo.daim;


public class Chat {

    private String message;
    private String author;

    
    

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
