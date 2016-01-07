import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.*;


public class DATABASE {
	
	private Connection conn = null;
	private Statement stmt = null;
	
	public static DATABASE database = new DATABASE();
	public static void main(String[] args ){
		boolean iscrawler = false;
		try {
			if(iscrawler)
				database.crawler();
			else{
				//open database
				database.buildSQLite();
				database.connDB("ConceptNet_en");
				database.createStmt();
				
				List<myDATA> datalist = database.searchTable("snake%", "NotIsA", "%", 0, false);
				//List<myDATA> datalist2 = database.searchTable("go/v%", "%", "%", 1, false);
				
				System.out.println("datalist1");
				int i=0;
				while(i<datalist.size()){
					System.out.println( datalist.get(i).start + " " + datalist.get(i).rel + " " + datalist.get(i).end + " w" + datalist.get(i).weight);
					i++;
				}
				/*
				System.out.println("datalist2");
				int i=0;
				while(i<datalist2.size()){
					System.out.println( datalist2.get(i).start + " " + datalist2.get(i).rel + " " + datalist2.get(i).end + " w" + datalist2.get(i).weight);
					i++;
				}
				//System.out.println("datalist1 size="+datalist.size());
				System.out.println("datalist2 size="+datalist2.size());
				*/
				//close database
				database.closeStmt();
				database.disconnDB();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void crawler() throws Exception{
		buildSQLite();
		connDB("ConceptNet_en");
		createStmt();
		createTable(); 
		
		// read file
		FileInputStream fstream = new FileInputStream("doing.txt");
		// Get the object of DataInputStream
		DataInputStream datain = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(datain));
		String strLine, writeStr="";
		int time = 0;
		while ((strLine = br.readLine()) != null) {
			System.out.println("crawler: " + strLine);
			writeStr = writeStr + strLine + "\n";
			
			String URL = "http://conceptnet5.media.mit.edu/data/5.4/search?start="+ strLine +"&end=/c/en/&limit=1000";
			String JSON_str = getWeb(URL);
			JSONDecoding(JSON_str, URL);
			
			time++;
			if(time>200){//重新連線
				time = 0;
				closeStmt();
				disconnDB();
				System.out.println("");
				System.out.println("connect again");
				System.out.println("");
				buildSQLite();
				connDB("ConceptNet_en");
				createStmt();
			}
		}

		// write file
		// true表示append
		BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("done.txt",true),"utf8"));
        bufWriter.write(writeStr);
        bufWriter.close();
        //清空
        bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("doing.txt",false),"utf8"));
        bufWriter.write("");
        bufWriter.close();
		
		closeStmt();
		disconnDB(); 
		
		System.out.println("crawler over!!!!!!!!!!!!!!!!!!!!");
	}
	
	// search form table
	public List<myDATA> searchTable(String start_c, String rel_c, String end_c, double weight_c, boolean open) throws Exception{
		List<myDATA> dataList = new ArrayList<>();
		
		if(open){
			buildSQLite();
			connDB("ConceptNet_en");
			createStmt();
		}
		
		// input加上/c/en /r/
		start_c = "/c/en/" + start_c;
		rel_c = "/r/" + rel_c;
		end_c = "/c/en/"+end_c;
		
		// /c/en/xxx
		ResultSet rs = stmt.executeQuery( "SELECT * FROM EDGES WHERE start LIKE \""
						+start_c+ "\" AND rel LIKE \"" + rel_c+ "\" AND end LIKE \"" +end_c+ "\" AND weight>" + weight_c + ";");
		//System.out.println( "SELECT * FROM EDGES WHERE start LIKE \""
		//				+start_c+ "\" AND rel LIKE \"" + rel_c+ "\" AND end LIKE \"" +end_c+ "\" AND weight>" + weight_c + ";");
		while ( rs.next() ) {
			String id = rs.getString("id");
			String start = rs.getString("start");
			String rel = rs.getString("rel");
			String end = rs.getString("end");
			double weight = rs.getDouble("weight");
			String surface_text = rs.getString("surface_text");
			surface_text = surface_text.replace('$', '\"');
			
			myDATA data = new myDATA();
			data.id = id;
			data.start = start.substring(6, start.length());
			data.rel = rel.substring(3, rel.length());
			data.end = end.substring(6, end.length());
			data.weight = weight;
			data.surface_text = surface_text;
			dataList.add(data);
		} 
		
		
		rs.close();
		if(open){
			closeStmt();
			disconnDB();
		}
		
		//System.out.println("search over!!!!!!!!!!!!!!!!!!!!");
		return dataList;
	}
	// create table
	// handle table exist
	private void createTable() throws SQLException{
		//stmt.execute("DROP TABLE IF EXISTS test");
        stmt.execute("CREATE TABLE IF NOT EXISTS EDGES(id text primary key not null, start text, rel text, end text, weight real, surface_text text)");
        //System.out.println("Create table sucess!");
	}	
	// 放入一組資料
	private void insertData(String id, String start, String rel, String end, double weight, String surface_text) throws SQLException{
		stmt.execute("INSERT INTO EDGES(id, start, rel, end, weight, surface_text) VALUES( \"" +
        			id + "\", \"" + start+ "\", \"" + rel + "\", \"" + end +"\", " + weight + ", \"" + surface_text + "\")");
	}
	// 載入資料庫驅動
	public void buildSQLite() throws ClassNotFoundException{
        Class.forName("org.sqlite.JDBC");
        System.out.println("Load sqlite Driver sucess!");
	}
	// connection DB
	public void connDB(String DBname) throws SQLException{
		// connect
        conn = DriverManager.getConnection("jdbc:sqlite:" +DBname +".db");
		System.out.println("Connect sucessfully!");
	}
	// disconnection db
	public void disconnDB() throws SQLException {
		conn.close();
		System.out.println("disconnect connect!");
	}
	// create statement
	public void createStmt() throws SQLException{
		stmt = conn.createStatement();
	}
	// close statement
	public void closeStmt() throws SQLException{
	     stmt.close();
	}
	
	// 解析輸入JSON格式的String成 n組data含5個feature
	// 如果numFound=1000會印出
	private void JSONDecoding (String str_JSON, String web_URL) throws JSONException{
		JSONObject myJSON = new JSONObject(str_JSON);
		String id, start, rel, end, surface_text;
		double weight;

		int numFound = Integer.parseInt(myJSON.get("numFound").toString());
		if(numFound==1000) System.out.println("numFound=1000 : " + web_URL);
		
		JSONArray edges = myJSON.getJSONArray("edges");
		for (int i=0; i<numFound; i++){
			// 解析JSON
			JSONObject edge = (JSONObject)edges.get(i);
			id = edge.get("id").toString();
			start = edge.get("start").toString();
			rel = edge.get("rel").toString();
			end = edge.get("end").toString();
			weight = Double.parseDouble( edge.get("weight").toString() );
			surface_text = edge.get("surfaceText").toString();
			surface_text = surface_text.replace('\"','$');
			
			// insert
			if(start.contains("/c/en/") && end.contains("/c/en/")){
				try{
					insertData(id, start, rel, end, weight, surface_text);
				} catch(SQLException e){
					// handle重複資料問題
					if(e.getMessage().contains("[SQLITE_CONSTRAINT]")){
						System.out.println("("+ start + ", " + rel+ ", "+ end +")" + "  REPEAT");
					}
					else{
						System.out.println("("+ start + ", " + rel+ ", "+ end +")" + "  ERROR");
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	// 讀取網頁html並輸出成String
	private String getWeb(String strURL){
		URL url = null;
		String data = null;
		String output = "";
		URLConnection conn = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		
		try{
			url = new URL(strURL);
			conn = url.openConnection();
			inputStreamReader = new InputStreamReader(conn.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);
			
			while( (data=bufferedReader.readLine()) !=null ){
				output = output+data+"\n";
			}
			
		} catch(Exception ex){
			ex.printStackTrace();
		} finally{
			try{
				bufferedReader.close();
			} catch(Exception ex){
				ex.printStackTrace();
			}			
		}
		
		return output;
	}
	
}
