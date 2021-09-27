package sales_taxes_project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class MainApplication {

	private static String path = new File("").getAbsolutePath(); //relative path of the program obtained through the absolute path
	private static String configFile = "config.ini"; //file name of the config file with file extension
	private static double basicSalesTax = 0.1; //basic sales tax in decimals
	private static double importSalesTax = 0.05; //import sales tax in decimals
	private static DecimalFormat df = new DecimalFormat("#0.00"); //format that ensures that decimals only display two decimal places
	
	public static void main(String[] args) {
		String[] noTaxItems = null;
		Properties props = new Properties();
		// try to read the config file included with the program
		try (FileInputStream in = new FileInputStream(path + "/" + configFile)) {
		    /*
		     * If successful, load the config file's properties in order to obtain the list of items
		     * that should ignore the basic sales tax. Once obtained, split the items into a proper array.
		     */
			props.load(in);
		    noTaxItems = props.getProperty("noTaxItems").split("; ");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			noTaxItems = new String[1]; // if not successful, create empty string (all items have basic sales tax then)
			noTaxItems[0] = "";
		} catch (IOException e) {
			e.printStackTrace();
			noTaxItems = new String[1]; // if not successful, create empty string (all items have basic sales tax then)
			noTaxItems[0] = "";
		}
		
		Scanner sc = new Scanner(System.in); // prepare the scanner for user input
		ArrayList<String> receipt = new ArrayList<>(); // ArrayList for user inputs
		ArrayList<Product> products = new ArrayList<>(); // ArrayList for the actual Product objects
		System.out.println("Please enter a shopping item to be added to the shopping basket.");
		System.out.println("It must follow this pattern:");
		System.out.println("*amount of item* *name of item* at *raw price of item*");
		System.out.println("Please specify in the name of item if it's imported with 'imported'.");
		System.out.println("Items that don't follow the pattern will be ignored.");
		System.out.println("Please enter '#' to end your user inputs.");
		String input = "";
		/*
		 * Ask the user to input all of the shopping items, one line at a time, and insert it into
		 * an ArrayList containing all user inputs. End all user inputs once a "#" input is detected.
		 */
		while (!input.equals("#")) {
			input = sc.nextLine();
			if (!input.equals("#")) {
				receipt.add(input);
			}
		}
		sc.close();
		
		System.out.println("Validating your inputs, please wait...");
		for (String item : receipt) {
			// split each line into an array of words and start checking if the line is a valid item
			String[] splitItem = item.split(" ");
			boolean isValid = isValid(splitItem);
			/*
			 * If this is a valid shopping item, create a new Product object using the information
			 * obtained from the shopping item, then insert it into an ArrayList for later use.
			 */
			if (isValid) {
				int productCount = Integer.parseInt(splitItem[0]);
				String productName = rebuildProductName(splitItem);
				double rawPrice = Double.parseDouble(splitItem[splitItem.length-1]);
				Product newProduct = new Product(productCount,productName,rawPrice);
				newProduct.hasNoTax(noTaxItems); // separate function to determine if the product ignores basic sales tax
				products.add(newProduct);
			}
		}
		
		System.out.println("Validation completed. Items on the receipt: "+products.size());
		double totalPrice = 0.0; // total price of all products
		double salesTaxes = 0.0; // the total amounts of sales taxes paid
		for (Product product : products) {
			double tax = 0.0;
			// check if a product has a certain tax and add to the tax sum accordingly
			if (!product.getNoTaxVal()) {
				tax += product.getRawPrice() * basicSalesTax;
			}
			if (product.getImportedVal()) {
				tax += product.getRawPrice() * importSalesTax;
			}
			
			tax = roundUpToNextFive(df.format(tax).replace(",", ".")); // round up the tax sum to the nearest 0.05
			product.setTaxedPrice(product.getRawPrice() + tax); // set the taxed price of the product
			salesTaxes += tax; // add tax sum to the total amounts of sales taxes paid
			totalPrice += product.getTaxedPrice(); // add taxed price to total price
		}
		
		System.out.println("###############################################");
		// in a for-loop, display the taxed price of each product separately
		for (Product product : products) {
			String formattedPrice = df.format(product.getTaxedPrice()); // format the product's taxed price correctly
			System.out.println("> "+product.getCount()+" "+product.getName()+": "+formattedPrice);
		}
		String formattedSalesTaxes = df.format(salesTaxes); // format the total amounts of sales taxes paid correctly
		String formattedTotal = df.format(totalPrice); // format the total price correctly
		System.out.println("> Sales Taxes: "+formattedSalesTaxes); // display the total amounts of sales taxes paid
		System.out.println("> Total: "+formattedTotal); // display the total price
	}
	
	public static boolean isValid(String[] splitItem) {
		// check if first word is a numeric integer value (item count)
		try {
			Integer.parseInt(splitItem[0]);
		} catch (Exception intE) {
			return false;
		}
		// check if last word is a double value (item price without taxes)
		try {
			Double.parseDouble(splitItem[splitItem.length-1]);
		} catch (Exception doubleE) {
			return false;
		}
		// finally, check if the word before that is "at" (part of the shopping item's required sentence structure)
		boolean isValid = false;
		if (splitItem[splitItem.length-2].equals("at")) {
			isValid = true;
		}
		return isValid;
	}
	
	public static String rebuildProductName(String[] splitItem) {
		/*
		 * If the first word is the item count, the last word is the item price without taxes and
		 * the word before that is "at", then the remaining words in the array are the product name.
		 * This function rebuilds those words back into a single string.
		 */
		String name = splitItem[1];
		for (int i = 2; i < splitItem.length-2; i++) {
			name += " ";
			name += splitItem[i];
		}
		return name;
	}
	
	public static double roundUpToNextFive(String taxString) {
		String taxEndString = taxString.substring(taxString.length()-1);
		String newTaxString;
		int taxEnd = Integer.parseInt(taxEndString);
		if (taxEnd < 5 && taxEnd > 0) {
			// if the price ends somewhere between 0 and 5, then round up to 0.05
			newTaxString = taxString.substring(0,taxString.length()-1) + "5";
			return Double.parseDouble(newTaxString);
		} else if (taxEnd > 5) {
			// if the price ends somewhere above 5, then round up to the next decimal place (e.g. 0.06 -> 0.10)
			newTaxString = taxString.substring(0,taxString.length()-1);
			return Double.parseDouble(newTaxString) + 0.1;
		} else {
			// price ends at 0 or 5, do nothing
			return Double.parseDouble(taxString);
		}
	}

}