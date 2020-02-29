package craigslist;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;
import java.util.Comparator;

public class Configs
{
	private static final String CONFIG_DIR = "config/";
	private static final String QUERIES_CONFIG = "searches.cfg";
	private static final String GOOD_KEYWORDS_CONFIG = "good_keywords.cfg";
	private static final String BAD_KEYWORDS_CONFIG = "bad_keywords.cfg";
	private static final String YEARS_CONFIG = "years.cfg";
	private static final String MODELS_CONFIG = "models.cfg";
	private static final String UNWANTED_MODELS_CONFIG = "unwanted_models.cfg";
	private static final String MANUAL_CONFIG = "manual.cfg";
	private static final String AUTOMATIC_CONFIG = "automatic.cfg";
	private static final String TRANSMISSION_CONFIG = "transmission.cfg";
	private static final String PRICES_CONFIG = "prices.cfg";
	private static final String ODO_CONFIG = "odo.cfg";
	
	public static String[] queries;
	public static String[] goodKeywords;
	public static String[] badKeywords;
	public static String[] years;
	public static String[] models;
	public static String[] unwantedModels;
	public static String[] transmission;
	public static int[] prices;
	public static int[] odo;
	
	public static void load_config()
	{
		goodKeywords = read_string_config(GOOD_KEYWORDS_CONFIG);
		badKeywords = read_string_config(BAD_KEYWORDS_CONFIG);
		years = read_years();
		transmission = read_transmission();
		prices = read_prices();
		odo = read_odos();
	}

	private static String[] read_string_config(String configFile)
	{
		String[] configData = null;
		Scanner in;
		try
		{
			in = new Scanner(new FileReader(CONFIG_DIR+configFile));
			while(in.hasNext())
			{
				configData = in.next().split("\n");
			}
			in.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("Could not read " + configFile + ".");
		}
		return configData;
	}

	private static String[] read_years()
	{
		return null;
	}

	private static String[] read_transmission()
	{
		return null;
	}

	private static int[] read_prices()
	{
		int[] prices = null;

		Scanner in;
		try
		{
			in = new Scanner(new FileReader(CONFIG_DIR+PRICES_CONFIG));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("Could not read " + PRICES_CONFIG + ".");
		}
		return prices;
	}

	private static int[] read_odos()
	{
		int[] odos = null;

		Scanner in;
		try
		{
			in = new Scanner(new FileReader(CONFIG_DIR+ODO_CONFIG));

			in.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("Could not read " + ODO_CONFIG + ".");
		}
		return odos;
	}
};
