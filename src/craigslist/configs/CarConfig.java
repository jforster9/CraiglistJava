package craigslist.configs;

import java.io.File;
import java.io.IOException;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

public class CarConfig extends Configs 
{
	private static final String CAR_CONFIG = CONFIG_DIR + "car.ini";

	// Good results
	public static String[] models;
	public static String transmission;
	public static int odometer;
	public static String[] years;
	public static String[] goodKeywords;

	// Bad results
	public static String[] badKeywords;
	public static String[] unwantedModels;
	
	public void load_config() throws InvalidFileFormatException, IOException
	{
		super.load_config();
		
		clConfig = new Ini(new File(CAR_CONFIG));
		
		models = read_multiple_string_entries("models", "model");
		transmission = read_string_entry("dream car", "transmission");
		odometer = read_int_entry("dream car", "max odometer");
		years = read_multiple_string_entries("years", "year");
		goodKeywords = read_multiple_string_entries("good keywords", "key");
		
		badKeywords = read_multiple_string_entries("bad keywords", "key");
		unwantedModels = read_multiple_string_entries("unwanted models", "model");
	}
}
