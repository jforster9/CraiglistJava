package craigslist;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.text.similarity.*;

public class ListingDB {
	private Connection c = null;
	LevenshteinDistance lev_dist = null;
	public static int LEV_DIST_THRESH_PER = 8;
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	Vector<Listing> db_listings = new Vector<Listing>();
	Vector<String> all_urls = new Vector<String>();
	public ListingDB()
	{	      
		lev_dist = new LevenshteinDistance(100);
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:sqlite/listing.db");
			create_listing_table();
			load_existing();
	  	} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
	  	}
		System.out.println("Opened database successfully");
	}
	public void create_listing_table() throws SQLException
	{
		Statement stmt = c.createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS 'Listings' (" +
						"'id'					INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
						"'date'					TEXT," +
						"'title'				TEXT," +
						"'content'				TEXT," +
						"'attr_make_model'		TEXT," +
						"'attr_transmission'	TEXT," +
						"'attr_odometer'		INTEGER," +
						"'attr_title_status'	TEXT," +
						"'price'				NUMERIC," +
						"'num_images'			INTEGER," +
						"'by_owner'				INTEGER," +
						"'url'					TEXT," +
						"'region'				TEXT," +
						"'value'				NUMERIC" + 
						");";
		stmt.executeUpdate(sql);
		stmt = c.createStatement();
		sql = "CREATE TABLE IF NOT EXISTS 'Requests' (" +
						"'id'					INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
						"'date'					TEXT," +
						"'requests'				INTEGER" +
						");";
		stmt.executeUpdate(sql);
		stmt = c.createStatement();
		sql = "CREATE TABLE IF NOT EXISTS 'ListingUrls' (" +
						"'id'					INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
						"'url'					TEXT," +
						"'date'					TEXT" +
						
						");";
		stmt.executeUpdate(sql);
		stmt.close();
	}
	public void load_existing() throws SQLException
	{
		String sql = "SELECT * FROM Listings";
		PreparedStatement stmt = c.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			Listing listing = new Listing();
			listing.title = rs.getString("title");
			listing.content = rs.getString("content");
			listing.date = LocalDateTime.parse(rs.getString("date"), formatter);
			listing.attr_make_model = rs.getString("attr_make_model");
			listing.attr_odometer = rs.getInt("attr_odometer");
			listing.num_images = rs.getInt("num_images");
			if(rs.getInt("by_owner") == 1) { listing.by_owner = true; } else {listing.by_owner = false; }
			listing.price = rs.getFloat("price");
			listing.region = rs.getString("region");
			listing.url = rs.getString("url");
			listing.value = rs.getFloat("value");
			db_listings.add(listing);
		}
		sql = "SELECT * FROM ListingUrls";
		stmt = c.prepareStatement(sql);
		rs = stmt.executeQuery();
		while(rs.next()){
			String d = rs.getString("date");
			LocalDateTime url_date = LocalDateTime.parse(rs.getString("date"), formatter);
			LocalDateTime month_ago = LocalDateTime.now().minusMonths(1);
			
			if(month_ago.isBefore(url_date))
			{
				String url = rs.getString("url");
				all_urls.add(url);
			}
			

			
		}
		return;
	}
	public int load_requests()
	{
		int total_requests = 0;
		try {
			LocalDateTime hr_ago = LocalDateTime.now().minusHours(1);
			String sql = "SELECT * FROM Requests";
			PreparedStatement stmt = c.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				LocalDateTime ldt = LocalDateTime.parse(rs.getString("date"), formatter);
				int requests = rs.getInt("requests");
				if(hr_ago.isBefore(ldt))
				{
					total_requests += requests;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return total_requests;
		
	}
	public synchronized boolean save_listing(Listing listing)
	{
		try
		{
			if(!is_unique(listing))
			{
				return false;
			}
			String sql = "INSERT INTO 'Listings'('date','title','content'," + 
					"'attr_make_model','attr_transmission','attr_odometer','attr_title_status'," + 
					"'price','num_images','by_owner','url','region','value')" + 
					"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
			
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, listing.date.format(formatter));
			stmt.setString(2, listing.title);
			stmt.setString(3, listing.content);
			stmt.setString(4, listing.attr_make_model);
			stmt.setString(5, listing.attr_transmission);
			stmt.setInt(6, listing.attr_odometer);
			stmt.setString(7, listing.attr_title_status);
			stmt.setFloat(8, listing.price);
			stmt.setInt(9, listing.num_images);
			if(listing.by_owner) { stmt.setInt(10, 1); } else { stmt.setInt(10, 0); };
			stmt.setString(11, listing.url);
			stmt.setString(12, listing.region);
			stmt.setFloat(13, listing.value);
			stmt.executeUpdate();
			stmt.close();
			db_listings.add(listing);
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error adding listing to db");
		}
		
		return false;
	}
	public void log_num_requests(int requests)
	{
		try {
			String sql = "INSERT INTO 'Requests'('date','requests')" + 
					"VALUES (?,?);";
			
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, LocalDateTime.now().withNano(0).format(formatter));
			stmt.setInt(2, requests);

			stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Couldn't record number of requests in db.");
		}
	}
	public void save_listing_urls(Vector<String> urls)
	{
		try {
			String sql = "INSERT INTO 'ListingUrls'('url','date')" + 
					"VALUES (?,?);";
			PreparedStatement stmt = c.prepareStatement(sql);
			String now = LocalDateTime.now().withNano(0).format(formatter);
			int max_per_batch = 100;
			int counter = 0;
			while(urls.size() > 0)
			{
				Iterator<String> iter = urls.iterator();
				while(iter.hasNext() && counter < max_per_batch)
				{
					String url = iter.next();
					
					stmt.setString(1, url);
					stmt.setString(2, now);
					stmt.addBatch();
					iter.remove();
					counter++;
				}
				stmt.executeBatch();
				counter = 0;
			}
			stmt.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Couldn't record number of requests in db.");
		}
		
		
	}
	public synchronized boolean is_unique(Listing listing) throws SQLException
	{
		// filters out listing that are slightly different reposts
		for(Listing db_listing : db_listings)
		{
			Integer ld = lev_dist.apply(db_listing.content, listing.content);
			Integer threshold =  (int) Math.max(LEV_DIST_THRESH_PER, LEV_DIST_THRESH_PER * Math.min(db_listing.content.length(), listing.content.length()) / 50f);
			
			if((ld < threshold && ld != -1) || (db_listing.title.equals(listing.title) && db_listing.title.length() > 30))
			{
				return false;
			}
		}
		return true;
	}
}
