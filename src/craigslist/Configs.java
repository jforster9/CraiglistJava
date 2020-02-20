package craigslist;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;
import java.util.Comparator;

public static class Configs implements Serializable
{
	private static final String CONFIG_DIR = "config/";
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

	private static String[] read_string_config(String configFile)
	{
		String[] configData;
		Scanner in;
		try
		{
			in = new Scanner(new FileReader(CONFIG_DIR+configFile));
			while(int.hasNext())
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

	public static String[] read_good_keywords()
	{
		return read_string_config(GOOD_KEYWORDS_CONFIG);
	}

	public static String[] read_bad_keywords()
	{
		return read_string_config(BAD_KEYWORDS_CONFIG);
	}

	public static String[] read_models()
	{
		return read_string_config(MODELS_CONFIG);
	}

	public static String[] read_unwanted_models()
	{
		return read_string_config(UNWANTED_MODELS_CONFIG);
	}

	public static String[] read_years()
	{
	}

	public static String[] read_transmission()
	{
		return null;
	}

	public static Int[] read_price()
	{
		Int[] prices;

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

	public static Int[] read_odos()
	{
		Int[] odos;

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
