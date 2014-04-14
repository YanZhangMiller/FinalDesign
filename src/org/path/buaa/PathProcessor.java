package org.path.buaa;

import org.tools.buaa.MySql;

public class PathProcessor implements Runnable{

	MySql m = new MySql();
	volatile boolean isStop = false;
	public PathProcessor()
	{
		m.setCover(true);
	}
	/**
	 * stop calculation of News Path.
	 */
	public void stop()
	{
		isStop = true;
	}
	public void run()
	{
		while (!isStop)
		{
			
			
			try {
				Thread.sleep(300000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		
	}
	
	
	public static void main(String[] args)
	{
		
	}
}
