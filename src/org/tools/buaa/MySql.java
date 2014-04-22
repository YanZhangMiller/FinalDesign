package org.tools.buaa;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;







public class MySql {
	static final String url="jdbc:mysql://localhost:3306/news";
	static final String usr="root";
	static final String pswd="925418";
	static final String createTable = " (url varchar(100) key, title varchar(50), content text, html text, keywords varchar(50), posurl varchar(100), possite varchar(20), fromurl varchar(100), fromsite varchar(20), pubtime datetime, lastrefresh datetime, processed boolean )";
    static final String insertData = " (url,title,content,html,keywords,posurl,possite,fromurl,fromsite,pubtime,lastrefresh,processed) values( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
    static final String[] tableItem = { "url","title","content","html","keywords","posurl","possite","fromurl","fromsite","pubtime","lastrefresh","processed"};
	boolean isCover = false;
	/**
	 * 
	 * @param isCover overwrite the records in the database if is set to true. 
	 */
    public void setCover(boolean isCover) {
		this.isCover = isCover;
	}


	Connection conn;
	PreparedStatement pst;
	String InsertTable = "";
	public MySql()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url,usr,pswd);
		}
		catch (Exception e){}
	}
	/**
	 * 
	 * @param table 
	 * @param Data
	 * @return true if succeed.
	 */
	public synchronized boolean insertData(String table,Dictionary<String,Object> Data)
	{
		try 
		{
			if (conn == null || !conn.isValid(10))
			{
				conn.close();
				conn = DriverManager.getConnection(url,usr,pswd);
			}
			if (pst==null || pst.isClosed() || !InsertTable.equals(table))
			{
				InsertTable = table;
				pst = conn.prepareStatement("insert into " + table + insertData);
			}
			

			for (int i=0;i<tableItem.length;i++)
			{
				if (Data.get(tableItem[i])==null)
				{
					pst.setObject(i + 1, null);
				}
				else
				{
					pst.setObject(i + 1, Data.get(tableItem[i]));
				}
			}
			pst.executeUpdate();
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			try
			{
				switch (ex.getErrorCode())
				{
				
				case 1146:
					PreparedStatement p = conn.prepareStatement("create table " + table + createTable);
					p.executeUpdate();
					pst.executeUpdate();
					break;
				case 1062:
					if (!isCover)
						return false;
					PreparedStatement q = conn.prepareStatement("delete from " + table + " where url = ?");
					q.setObject(1, Data.get("url"));
					q.executeUpdate();
					pst.executeUpdate();
					break;
				default:
					throw ex;
				
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				InsertTable = "";
				return false;
			}
		}
		return true;
	}
	
	public synchronized boolean isExist(String table,String key)
	{
		try
		{
			if (conn == null || !conn.isValid(10))
			{
				conn.close();
				conn = DriverManager.getConnection(url,usr,pswd);
			}
			PreparedStatement p = conn.prepareStatement("select url from " + table + " where url = ?");
			p.setObject(1, key);
			ResultSet rs = p.executeQuery();
			if (rs.next())
				return true;
		}
		catch (Exception ex){}
		return false;
	}
	public synchronized List<String> getEmptyRows(String table,Date d)
	{
		List<String> ret = new ArrayList<String>();
		try
		{
			if (conn == null || !conn.isValid(10))
			{
				conn.close();
				conn = DriverManager.getConnection(url,usr,pswd);
			}
			PreparedStatement p = conn.prepareStatement("select url from " + table + " where content is null and TO_DAYS(pubtime) >= TO_DATS(?)");
			p.setObject(1, d);
			ResultSet rs = p.executeQuery();
			while (rs.next())
				ret.add(rs.getString("url"));
		} catch (Exception ex) {}
		
		return ret;
	}
	/**
	 * get next count unprocessed records.
	 * 
	 * @param table data table to get unprocessed data. 
	 * @param count number of unprocessed records to get.
	 * @return return Array of Dictionary contains records in table. null if errors occur.
	 */
	public synchronized List<Dictionary<String,Object>> getUnprocessed(String table,int count)
	{
		if (count<=0)
			return new ArrayList<Dictionary<String,Object>>();
		try
		{
			if (conn == null || !conn.isValid(10))
			{
				conn.close();
				conn = DriverManager.getConnection(url,usr,pswd);
			}
			PreparedStatement p = conn.prepareStatement("select * from " + table + " where processed = 0 or processed is null order by pubtime desc limit ?");
			p.setObject(1, count);
			List<Dictionary<String,Object>>ret = new ArrayList<Dictionary<String,Object>>();
			ResultSet rs = p.executeQuery();
			while (rs.next())
			{
				Dictionary<String,Object> dic = new Hashtable<String,Object>();
				for (String x : tableItem)
				{
					if(rs.getObject(x)!=null)
						dic.put(x, rs.getObject(x));
				}
				ret.add(dic);
			}
			
			return ret;			
		} catch (Exception ex) {}
		return null;
	}

	/**
	 * Delete url in table.
	 * @param table
	 * @param url primary key
	 */
	public synchronized void delete(String table,String url)
	{
		try
		{
			if (conn == null || !conn.isValid(10))
			{
				conn.close();
				conn = DriverManager.getConnection(url,usr,pswd);
			}
			PreparedStatement p = conn.prepareStatement("delete from " + table + " where url = ?");
			p.setObject(1, url);
			p.executeUpdate();
		} catch (Exception ex) {}
	}
	
	
	/**
	 * Move the record form tableFrom to tableTo.
	 * 
	 * @param tableFrom	Source table.
	 * @param tableTo Destination table.
	 * @param url
	 */
	public synchronized void moveTo(String tableFrom,String tableTo,String url)
	{
		try
		{
			if (conn == null || !conn.isValid(10))
			{
				conn.close();
				conn = DriverManager.getConnection(url,usr,pswd);
			}
			PreparedStatement p = conn.prepareStatement("select * from " + tableFrom + " where url = ?");
			
			p.setObject(1, url);
			ResultSet rs = p.executeQuery();
			if (rs.next())
			{
				Dictionary<String,Object> dic = new Hashtable<String,Object>();
				for(String x: tableItem)
				{
					
					if (rs.getObject(x)!=null)
						dic.put(x, rs.getObject(x));
				}
				this.insertData(tableTo, dic);
				this.delete(tableFrom, url);
			}
						
		} catch (Exception ex) {ex.printStackTrace();}
	}
	
	public synchronized List<String> getTables()
	{
		List<String> ret = new ArrayList<String>();
		try
		{
			if (conn == null || !conn.isValid(10))
			{
				conn.close();
				conn = DriverManager.getConnection(url,usr,pswd);
			}
			PreparedStatement p = conn.prepareStatement("show tables");
			
			ResultSet rs = p.executeQuery();
			
			while (rs.next())
			{
				ret.add(rs.getString(1));
			}
		}
		catch (Exception e){e.printStackTrace();}
		return ret;
	}
	public static void main(String[] args) throws SQLException  {
		// TODO Auto-generated method stub
		MySql m = new MySql();
		Dictionary<String,Object> d = new Hashtable<String,Object>();
		d.put("url", "www.qq.com1");
		d.put("title", "mainpage");
		d.put("lastrefresh",new Date());
		d.put("pubtime",new Date());
		m.insertData("siteqq", d);
		
		for(Dictionary<String,Object>x : m.getUnprocessed("siteww", -5))
		{
			System.out.println(x.get("url"));
		}
		//m.moveTo("siteww", "error", "www.qq.com");
		System.out.println(m.getTables());
		
	}
	
	
	protected void finalize() throws Throwable {  
	    super.finalize();  
	    conn.close();
	}
	

}
