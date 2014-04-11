package tools;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    static final String[] TableItem = { "url","title","content","html","keywords","posurl","possite","fromurl","fromsite","pubtime","lastrefresh","processed"};
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
	
	public boolean insertData(String table,Dictionary<String,Object> Data)
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
			

			for (int i=0;i<TableItem.length;i++)
			{
				if (Data.get(TableItem[i])==null)
				{
					pst.setObject(i + 1, null);
				}
				else
				{
					pst.setObject(i + 1, Data.get(TableItem[i]));
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
	
	public boolean isExist(String table,String key)
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
	public List<String> getEmptyRows(String table,Date d)
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
	
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		MySql m = new MySql();
		Dictionary<String,Object> d = new Hashtable<String,Object>();
		d.put("url", "www.qq.com");
		d.put("title", "mainpage");
		d.put("lastrefresh",new Date());
		d.put("pubtime",new Date());
		m.insertData("siteww", d);
		
	}
	
	
	protected void finalize() throws Throwable {  
	    super.finalize();  
	    conn.close();
	}  

}
