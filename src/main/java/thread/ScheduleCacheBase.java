/**
 *
 * 通联数据机密
 * --------------------------------------------------------------------
 * 通联数据股份公司版权所有 © 2013-2016
 *
 * 注意：本文所载所有信息均属于通联数据股份公司资产。本文所包含的知识和技术概念均属于
 * 通联数据产权，并可能由中国、美国和其他国家专利或申请中的专利所覆盖，并受商业秘密或
 * 版权法保护。
 * 除非事先获得通联数据股份公司书面许可，严禁传播文中信息或复制本材料。
 *
 * DataYes CONFIDENTIAL
 * --------------------------------------------------------------------
 * Copyright © 2013-2016 DataYes, All Rights Reserved.
 *
 * NOTICE: All information contained herein is the property of DataYes
 * Incorporated. The intellectual and technical concepts contained herein are
 * proprietary to DataYes Incorporated, and may be covered by China, U.S. and
 * Other Countries Patents, patents in process, and are protected by trade
 * secret or copyright law.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from DataYes.
 *
 *
 */

package thread;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ScheduleCacheBase {
	static Logger logger = LoggerFactory.getLogger(ScheduleCacheBase.class);

	long updateInterval;
	long offset;
	MyTask timer = null;
	
	public ScheduleCacheBase(long updateInterval, long offset) {
		this.updateInterval = updateInterval;
		this.offset = offset;
		quickInit();
		startBatchJob();
	}

	public abstract void quickInit();
	
    public abstract void updCache();
    
    public abstract Class getRealClass();

	private void startBatchJob() {
		timer = new MyTask();
		try {
			Date tstart = new Date();
			long delay = (tstart.getTime()/updateInterval + 1) * updateInterval + offset - tstart.getTime();
			while(delay<0)
				delay += updateInterval;
			ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
			scheduledThreadPool.scheduleAtFixedRate(timer, delay, updateInterval, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Error: " + e);
		}
	}

	class MyTask extends java.util.TimerTask{
		@Override
		public void run() {
			try {
				logger.info("start to refresh cache: " + getRealClass());
				updCache();
				logger.info("cache refreshed: " + getRealClass());
			}
			catch(Exception e) {
				logger.error("Error in timer", e);
			}
		}
	}
}
