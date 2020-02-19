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

	public static String[] read_good_keywords()
	{
		String[] goodKeywords;

		return goodKeyWords;
	}

	public static String[] read_bad_keywords()
	{
		String[] badKeyWords;

		return badKeyWords;
	}

	public static String[] read_years()
	{
		String[] years;

		return years;
	}

	public static String[] read_models()
	{
		String[] models;

		return models;
	}

	public static String[] read_unwanted_models()
	{
		String[] unwanted_models;

		return unwanted_models;
	}

	public static String[] read_transmission()
	{
		String transmission;
		String[] transmissionKeywords; 

		return transmissionKeywords;
	}

	public static Int[] read_price()
	{
		Int[] prices;

		return prices;
	}

	public static Int[] read_odos()
	{
		Int[] odos;

		return odos;
	}
};
