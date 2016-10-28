package com.znvoid.demo.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;  
  
public class FileUtils {  
    private String SDPATH;  
      
    private int FILESIZE = 4 * 1024;   
    /*
     * �ļ������Ƿ��ҵ�
     */
	private boolean found = false;
	/*
	 * �ļ������ʽ
	 */
	private String encoding = null;
    public String getSDPATH(){  
        return SDPATH;  
    }  
      
    public FileUtils(){  
        //�õ���ǰ�ⲿ�洢�豸��Ŀ¼( /SDCARD )  
        SDPATH = Environment.getExternalStorageDirectory() + "/";  
    }  
      
    /**  
     * ��SD���ϴ����ļ�  
     * @param fileName  
     * @return  
     * @throws IOException  
     */  
    public File createSDFile(String fileName) throws IOException{  
        File file = new File(SDPATH + fileName);  
        file.createNewFile();  
        return file;  
    }  
      
    /**  
     * ��SD���ϴ���Ŀ¼  
     * @param dirName  
     * @return  
     */  
    public File createSDDir(String dirName){  
        File dir = new File(SDPATH + dirName);  
        dir.mkdir();  
        return dir;  
    }  
      
    /**  
     * �ж�SD���ϵ��ļ����Ƿ����  
     * @param fileName  
     * @return  
     */  
    public boolean isFileExist(String fileName){  
        File file = new File(SDPATH + fileName);  
        return file.exists();  
    }  
      
    /**  
     * ��һ��InputStream���������д�뵽SD����  
     * @param path  
     * @param fileName  
     * @param input  
     * @return  
     */  
    public File write2SDFromInput(String path,String fileName,InputStream input){  
        File file = null;  
        OutputStream output = null;  
        try {  
            createSDDir(path);  
            file = createSDFile(path + fileName);  
            output = new FileOutputStream(file);  
            byte[] buffer = new byte[FILESIZE];  
            while((input.read(buffer)) != -1){  
                output.write(buffer);  
            }  
            output.flush();  
        }   
        catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            try {  
                output.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return file;  
    }  
  
    /**  
     * ��һ���ַ���д�뵽SD����  
     * @param path  
     * @param fileName  
     * @param input  
     * @return  
     */  
    public File write2SDFromInput(String path,String fileName,String input){  
        File file = null;  
        OutputStream output = null;  
        try {  
            createSDDir(path);  
            file = createSDFile(path + fileName);  
            output = new FileOutputStream(file);
            output.write(input.getBytes());  
            output.flush();  
        }   
        catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            try {  
                output.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return file;  
    }
    
	/**
	 * ����һ���ļ�(File)���󣬼���ļ�����
	 * 
	 * @param file
	 *            File����ʵ��
	 * @return �ļ����룬���ޣ��򷵻�null
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String guessFileEncoding(File file) throws FileNotFoundException, IOException {
		return guessFileEncoding(file, new nsDetector());
	}

	/**
	 * <pre>
	 * ��ȡ�ļ��ı���
	 * &#64;param file
	 *            File����ʵ��
	 * &#64;param languageHint
	 *            ������ʾ������� @see #nsPSMDetector ,ȡֵ���£�
	 *             1 : Japanese
	 *             2 : Chinese
	 *             3 : Simplified Chinese
	 *             4 : Traditional Chinese
	 *             5 : Korean
	 *             6 : Dont know(default)
	 * </pre>
	 * 
	 * @return �ļ����룬eg��UTF-8,GBK,GB2312��ʽ(��ȷ����ʱ�򣬷��ؿ��ܵ��ַ���������)�����ޣ��򷵻�null
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String guessFileEncoding(File file, int languageHint) throws FileNotFoundException, IOException {
		return guessFileEncoding(file, new nsDetector(languageHint));
	}

	/**
	 * ��ȡ�ļ��ı���
	 * 
	 * @param file
	 * @param det
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String guessFileEncoding(File file, nsDetector det) throws FileNotFoundException, IOException {
		// Set an observer...
		// The Notify() will be called when a matching charset is found.
	
		
		det.Init(new nsICharsetDetectionObserver() {
			@Override
			public void Notify(String charset) {
				encoding = charset;
				 found = true;
			}
		});

		BufferedInputStream imp = new BufferedInputStream(new FileInputStream(file));
		byte[] buf = new byte[1024];
		int len;
		boolean done = false;
		boolean isAscii = false;

		while ((len = imp.read(buf, 0, buf.length)) != -1) {
			// Check if the stream is only ascii.
			isAscii = det.isAscii(buf, len);
			if (isAscii) {
				break;
			}
			// DoIt if non-ascii and not done yet.
			done = det.DoIt(buf, len, false);
			if (done) {
				break;
			}
		}
		imp.close();
		det.DataEnd();

		
		if (isAscii) {
			encoding = "ASCII";
			found = true;
		}

		if (!found) {
			String[] prob = det.getProbableCharsets();
			// ���ｫ���ܵ��ַ��������������
			for (int i = 0; i < prob.length; i++) {
				if (i == 0) {
					encoding = prob[i];
				} else {
					encoding += "," + prob[i];
				}
			}

			if (prob.length > 0) {
				// ��û�з��������,Ҳ����ֻȡ��һ�����ܵı���,���ﷵ�ص���һ�����ܵ�����
				return encoding;
			} else {
				return null;
			}
		}
		return encoding;
	} 
    
	/*
	 * �����ļ���δ�ҵ����ؿ�
	 * FileUtils.getSpecificTypeOfFile(this, new String[]{".doc",".apk"});  
	 */
	
	public static List<String> getSpecificTypeOfFile(Context context,String[] extension)  
    {  
		List<String> result=new ArrayList<String>();
        //������л�ȡ  
        Uri fileUri=Files.getContentUri("external");  
        //ɸѡ�У�����ֻɸѡ�ˣ��ļ�·���Ͳ�����׺���ļ���  
        String[] projection=new String[]{  
                FileColumns.DATA,FileColumns.TITLE  
        };  
        //����ɸѡ���  
        String selection="";  
        for(int i=0;i<extension.length;i++)  
        {  
            if(i!=0)  
            {  
                selection=selection+" OR ";  
            }  
            selection=selection+FileColumns.DATA+" LIKE '%"+extension[i]+"'";  
        }  
        //��ʱ�����˳��Խ����������;����Ӻ���ǰ�ƶ��α�Ϳ�ʵ��ʱ��ݼ�  
        String sortOrder=FileColumns.DATE_MODIFIED;  
        //��ȡ���ݽ���������  
        ContentResolver resolver=context.getContentResolver();  
        //��ȡ�α�  
        Cursor cursor=resolver.query(fileUri, projection, selection, null, sortOrder);  
        if(cursor==null)  
            return null;  
        //�α�����ʼ��ǰ�ݼ����Դ�ʵ��ʱ��ݼ�˳��������ʵ��ļ���������ʾ��  
        if(cursor.moveToLast())  
        {  
            do{  
                //����ļ�������·��  
                String data=cursor.getString(0);  
                result.add(data);
            }while(cursor.moveToPrevious());  
        }  
        cursor.close();  
          return result;
    }  
}  
