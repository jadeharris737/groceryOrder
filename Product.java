/*------------------------------------------------
My name: Jade Harris
My student number: 7519084
My course code: CSIT121
My email address: jh144@uowmail.edu.au
Assignment number: 3
------------------------------------------------*/

import java.util.*;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.*;


/*Model a product with its name, supplier, category, stock quantity and price. Implement common methods
 to read and write data to text files, and format data to the user from the MyIO interface*/
class Product implements MyFileIO
{
	private String pname;
	private String supplier;
	private String category;
	private int stockQuantity;
	private double price;
	
	/*Default constructor to initialise a Product with default values*/
	public Product()
	{
		pname = "";
		supplier = "";
		category = "";
		stockQuantity = 0;
		price = 0.0;
	}
	public String getPname()
	{
		return pname;
	}
	public double getPrice()
	{
		return price;
	}
	public int getStockQuantity()
	{
		return stockQuantity;
	}
	public void setStockQuantity(int stockQuantity)
	{
		this.stockQuantity = stockQuantity;
	}
	/*Set the fields of the Product object to the input file data*/
	public void readData(Scanner input) throws InputMismatchException
	{
		pname = input.next();
		supplier = input.next();
		category = input.next();
		stockQuantity = input.nextInt();
		price = input.nextDouble();
	}
	/*Write a text-based representation of the Product object into the output file*/
	public void writeData(Formatter output)
	{
		output.format("%s,%s,%s,%d,%.2f\n", pname, supplier, category, stockQuantity, price);
	}
	/*Return the string representation of the Product's information in a format that can be interpreted
	by the user. @Override annotation indicates that the method should take precedence*/
	@Override
	public String toString()
	{
		return String.format("Product name: %s%nSupplier: %s%nCategory: %s%nStock quantity: %d%nPrice: %.1f", pname, supplier, category, stockQuantity, price);
	}
}