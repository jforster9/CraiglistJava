package craigslist;

import java.io.File;
import java.io.IOException;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

public class CarConfig extends Configs 
{
	private static final String CAR_CONFIG = CONFIG_DIR + "car.ini";

	// Good results
	public static String[] models;
	public static String[] transmission;
	public static int odometer;
	public static String[] years;

	// Bad results
	public static String[] unwantedModels;
	public static String[] badTransmission;

	public static boolean bSalvagedOk;
	
	public static void load_car_config() throws InvalidFileFormatException, IOException
	{
		load_config();

		clConfig = new Ini(new File(CAR_CONFIG));

		models = read_multiple_string_entries("models", "model");
		read_transmission_config();
		odometer = read_int_entry("dream car", "max odometer");
		years = read_multiple_string_entries("years", "year");
		unwantedModels = read_multiple_string_entries("unwanted models", "model");

		bSalvagedOk = read_bool_entry("dream car", "salvaged");
	}

	private static final String[] MANUAL = { "manual transmission", "6 speed manual", "manual", "6 speed", "6-speed",
			"6mt", "six speed", "stick shift", "manual trans", "transmission manual", "transmission: manual" };
	private static final String[] AUTO = { "6a", "transmission: automatic", "automatic 6-speed", "6 speed auto",
			"auto trans", "a/t", "6-speed a/t", "auto 6-spd", "transmission : automatic" };

	private static void read_transmission_config() {
		Ini.Section section = clConfig.get("dream car");
		String trans = section.get("transmission");

		if(trans.equals("manual"))
		{
			transmission = MANUAL;
			badTransmission = AUTO;
		}
		else if(trans.equals("automatic"))
		{

			transmission = AUTO;
			badTransmission = MANUAL;
		}
		else
		{
			transmission = badTransmission = null;
		}
	}
}
