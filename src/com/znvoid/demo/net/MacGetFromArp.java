package com.znvoid.demo.net;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import android.util.Log;

public class MacGetFromArp {
	
	
	
	public  ArrayList<ClientScanResultSO> getClientList(boolean onlyReachables, int reachableTimeout) {
	    BufferedReader br = null;
	    ArrayList<ClientScanResultSO> result = null;

	   try {
	        result = new ArrayList<ClientScanResultSO>();
	        br = new BufferedReader(new FileReader("/proc/net/arp"));
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] splitted = line.split(" +");

	            if ((splitted != null) && (splitted.length >= 4)) {
	                // Basic sanity check
	                String mac = splitted[3];
	                
	                if (mac.matches("..:..:..:..:..:..")&& !"00:00:00:00:00:00".equals(mac)) {
	                   // boolean isReachable = InetAddress.getByName(splitted[0]).isReachable(reachableTimeout);
	                	 boolean  isReachable = true;
	                    //String name = InetAddress.getByName(splitted[0]).getHostName();
	                    String name = "11";
	                    if (!onlyReachables || isReachable) {
	                        result.add(new ClientScanResultSO(splitted[0], splitted[3], splitted[5], isReachable, name));
	                    }
	                }
	            }
	        }
	    } catch (Exception e) {
	        Log.e(this.getClass().toString(), e.getMessage());
	    } finally {
	        try {
	            br.close();
	        } catch (IOException e) {
	            Log.e(this.getClass().toString(), e.getMessage());
	        }
	    }

	    return result;
	}
}
