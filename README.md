# sales_taxes_project
This is the repository for the Sales Taxes coding assignment for Itemis. 

It contains all the workfiles as well as this README file that explains how the application works and how the Java classes have been made.

## How does the program work?
The main application file is called "ItemisSalesTaxes.jar". Before you run it, you first need to make sure that the "config.ini" file is in the same directory as the application itself. 

"config.ini" is a config file that contains a list of key words for products that should ignore the basic sales tax. The config file can be easily modified in a text editor, allowing you to add additional key words for products that should ignore the basic sales tax when necessary (the key words should be seperated with a "; ").

Once both the application file and the config file are in the same folder, run the main application in a command line window like so:

> java -jar ItemisSalesTaxes.jar

Once the application is running, add each of the shopping items you want to add, one line at a time, using the following sentence structure: 

> \*item count in numeric\* \*product name\* at \*price without taxes in decimals\*

If a product is imported, make sure to include that in the product name. So here are a few examples of valid shopping items:

> 1 perfume bottle at 7.99
> 
> 2 books at 9.99
> 
> 3 imported chocolate bars at 4.50

Once you're finished adding shopping items, write a singular "#" to end your user input. 

After that, the application creates Product objects out of the valid shopping items in order to then calculate the taxed price for each shopping item as well as the total price of all items and the total amounts of sales taxes paid. The results then get printed out on the command line window before the application closes.

## How was the application made?

The application makes use of two Java classes. The first one is the main class "MainApplication.java" while the second one is a class "Product.java" that is used to create the Product objects.

As the name suggests, "MainApplication.java" handles all the functionalities of the application. It reads the config file in order to create an Array containing all the key words for products that ignore basic sales tax, handles the user input for the shopping items, validates the user inputs to create Product objects out of the valid shopping items, handles the tax calculation and proper price formatting and rounding, and prints out the results on the console.

"Product.java" is the class that defines the Product object and all it's attributes. An object of the Product class has an item count, a product name, a price without taxes applied, a price with taxes applied and two booleans, each one determining whenever an object ignores basic sales tax or is imported respectively.

For the creation of those two Java classes, no external libraries were used. The libraries pre-included with Eclipse IDE that the two classes have imported are the following: 

* java.io.File
* java.io.FileInputStream
* java.io.FileNotFoundException
* java.io.IOException
* java.text.DecimalFormat
* java.util.ArrayList
* java.util.Properties
* java.util.Scanner
