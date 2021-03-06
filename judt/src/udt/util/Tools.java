/**    
 * 文件名：Tools.java    
 *    
 * 版本信息：    
 * 日期：2018年8月28日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package udt.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.Enumeration;

/**    
 *     
 * 项目名称：judt    
 * 类名称：Tools    
 * 类描述：    一般工具
 * 创建人：jinyu    
 * 创建时间：2018年8月28日 上午10:47:31    
 * 修改人：jinyu    
 * 修改时间：2018年8月28日 上午10:47:31    
 * 修改备注：    
 * @version     
 *     
 */
public class Tools {
	public static InetAddress getLocalHostLANAddress() throws Exception {
	    try {
	        InetAddress candidateAddress = null;
	        // 遍历所有的网络接口
	        for (Enumeration<?> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
	            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
	            // 在所有的接口下再遍历IP
	            for (Enumeration<?> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
	                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
	                if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
	                    if (inetAddr.isSiteLocalAddress()) {
	                        // 如果是site-local地址，就是它了
	                        return inetAddr;
	                    } else if (candidateAddress == null) {
	                        // site-local类型的地址未被发现，先记录候选地址
	                        candidateAddress = inetAddr;
	                    }
	                }
	            }
	        }
	        if (candidateAddress != null) {
	            return candidateAddress;
	        }
	        // 如果没有发现 non-loopback地址.只能用最次选的方案
	        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
	        return jdkSuppliedAddress;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	/**
	 * 
	* @Title: getPeerIP
	* @Description: 或者字符串
	* @param @return    参数
	* @return String    返回类型
	 */
	public static String  getPeerIP()
	{
		String address="127.0.0.1";
		InetAddress addr = null;
		try {
			addr = getLocalHostLANAddress();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(addr!=null)
		{
			address=addr.toString();
			address=addr.getHostAddress();
		}
		return address;
	}
	
	
	/*
	 * IP转换
	 */
	public static long ipToLong(String strIp) {
		String[]ip = strIp.split("\\.");
		return (Long.parseLong(ip[0]) << 24) + (Long.parseLong(ip[1]) << 16) + (Long.parseLong(ip[2]) << 8) + Long.parseLong(ip[3]);
	}
	
	
	public static String longToIP(long longIp) {
		StringBuffer sb = new StringBuffer("");
		// 直接右移24位
		sb.append(String.valueOf((longIp >>> 24)));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
		sb.append(".");
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(".");
		// 将高24位置0
		sb.append(String.valueOf((longIp & 0x000000FF)));
		return sb.toString();
	}
	
	
	public static long[] ip6ToLong(String strIp) {
		long[]ips=new long[4];
		String[]ip = strIp.split(":");
		//128位，
		ips[3] =(Long.parseLong(ip[15]) << 24) + (Long.parseLong(ip[14]) << 16) + (Long.parseLong(ip[13]) << 8) + Long.parseLong(ip[12]);
		ips[2] =(Long.parseLong(ip[11]) << 24) + (Long.parseLong(ip[10]) << 16) + (Long.parseLong(ip[9]) << 8) + Long.parseLong(ip[8]);
		ips[1] =(Long.parseLong(ip[7]) << 24) + (Long.parseLong(ip[6]) << 16) + (Long.parseLong(ip[5]) << 8) + Long.parseLong(ip[4]);
		ips[0] =(Long.parseLong(ip[3]) << 24) + (Long.parseLong(ip[2]) << 16) + (Long.parseLong(ip[1]) << 8) + Long.parseLong(ip[0]);
		return ips;
	}
	
	public static String longToIP6(long[] longIps) {
		StringBuffer sb = new StringBuffer("");
		for(int i=0;i<4;i++)
		{
			long longIp=longIps[i];
		// 直接右移24位
		sb.append(String.valueOf((longIp >>> 24)));
		sb.append(":");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
		sb.append(":");
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(":");
		// 将高24位置0
		sb.append(String.valueOf((longIp & 0x000000FF)));
		}
		return sb.toString();
	}
	
	/**
	 * 
	* @Title: iptopeer
	* @Description: 转换IP
	* @param @param addr
	* @param @return    参数
	* @return long[]    返回类型
	 */
	public static long[] iptopeer(String addr)
	{
		long[] ips=new long[4];
		if(addr.contains("."))
		{
			ips[0]=ipToLong(addr);
		}
		else
		{
			ips=ip6ToLong(addr);
		}
		return ips;
	}
	
	
	public static String string2MD5(String inStr){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){  
            System.out.println(e.toString());  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
  
    }  
	 public static String convertMD5(String inStr){  
		  
	        char[] a = inStr.toCharArray();  
	        for (int i = 0; i < a.length; i++){  
	            a[i] = (char) (a[i] ^ 't');  
	        }  
	        String s = new String(a);  
	        return s;  
	  
	    }  
}
