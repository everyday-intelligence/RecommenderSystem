package com.recsys.cache;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;


public class RecSysCache {
    private static final String cacheRegionName = "testCache1";
	private static JCS jcs =  null;

	public static JCS getJcs() {
		if(jcs == null){
			try
	        {
	            jcs = JCS.getInstance(cacheRegionName);
	        }
	        catch ( CacheException e )
	        {
	            System.out.println( "Problem initializing cache for region name ["
	              + cacheRegionName+ "].");
	        }

		}
		return jcs;
	}

	public static void setJcs(JCS jcs) {
		RecSysCache.jcs = jcs;
	}
	
	
	
}
