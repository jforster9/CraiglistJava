package craigslist.configs;

import java.io.File;
import java.io.IOException;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

public class Configs
{
	protected static final String CONFIG_DIR = "config/";
	private static final String CRAIGSLIST_CONFIG = CONFIG_DIR + "craigslist.ini";
	protected static Ini clConfig;
	
	public static String category;
	public static String[] regions;
	public static String[] queries;
	public static int price;
	
	public void load_config() throws InvalidFileFormatException, IOException
	{
		clConfig = new Ini(new File(CRAIGSLIST_CONFIG));
		
		read_category();
		price = read_int_entry("for sale", "max price");
		regions = read_multiple_string_entries("regions", "loc");
		queries = read_multiple_string_entries("searches", "key");
	}
	
	protected static String read_string_entry(String sectionName, String option)
	{
		Ini.Section section = clConfig.get(sectionName);
		return section.get(option, String.class);
	}
	
	protected static String[] read_multiple_string_entries(String sectionName, String option)
	{
		Ini.Section section = clConfig.get(sectionName);
		return section.getAll(option, String[].class);
	}
	
	protected static int read_int_entry(String sectionName, String option)
	{
		Ini.Section section = clConfig.get(sectionName);
		return section.get(option, int.class);
	}
	
	protected static int[] read_multiple_int_entries(String sectionName, String option)
	{
		Ini.Section section = clConfig.get(sectionName);
		return section.getAll(option, int[].class);
	}
	
	private static final String CARS = "cars";
	private static final String BIKES = "bikes";
	private static final String TECH = "tech";
	
	private static void read_category()
	{
		Ini.Section section = clConfig.get("for sale");
		category = section.get("category", String.class);
		
		if (category.equals(CARS))
		{
			category = "cta";
		}
		else if (category.equals(BIKES))
		{
			category = "bia";
		}
		else if (category.equals(TECH))
		{
			category = "ela";
		}
		else
		{
			category = "sss";
		}
	}
};
