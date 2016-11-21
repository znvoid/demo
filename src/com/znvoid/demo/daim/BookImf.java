package com.znvoid.demo.daim;

public class BookImf {

	/*
	 * �鼮·��
	 */
	private String path;
/*
 * �鼮ͼ��·��
 */
private String ic_path;

private int mark;


public BookImf(String path, String ic_path, int mark) {

	this.path = path;
	this.ic_path = ic_path;
	this.mark = mark;
}
public BookImf(String path, String ic_path) {
	this.path = path;
	this.ic_path = ic_path;
	this.mark = 0;
	
}

public void setMark(int mark) {
	this.mark = mark;
}
public int getMark() {
	return mark;
}

public BookImf(String path) {
	super();
	this.path = path;
	ic_path="deful";
	mark=0;
}
public String getPath() {
	return path;
}
public void setPath(String path) {
	this.path = path;
}
public String getIc_path() {
	return ic_path;
}
public void setIc_path(String ic_path) {
	this.ic_path = ic_path;
}

public String getName() {
	if ("deful".equals(path)) {
		return getPath();
	}
	
	int f=path.lastIndexOf("/");
	int t=path.lastIndexOf(".");
	
	return path.substring(f+1, t);
}

}
