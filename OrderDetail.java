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



/*Model an OrderDetail product with its name, price, order quantity and subtotal. Implement common methods
 to read and write data to text files, and format data to the user from the MyIO interface*/
class OrderDetail implements MyFileIO
{
	private String pname;
	private double price;
	private int orderQuantity;
	private double subTotal;
	
	/*Default constructor to initialise an OrderDetail object with default values*/
	public OrderDetail()
	{
		pname = "";
		price = 0.0;
		orderQuantity = 0;
		subTotal = 0.0;
	}
	/*Parametrised constructor to initialise an OrderDetail object with given values*/
	public OrderDetail(String pname, double price, int orderQuantity)
	{
		this.pname = pname;
		this.price = price;
		this.orderQuantity = orderQuantity;
		this.subTotal = 0.0;
	}
	public void setOrderQuantity(int orderQuantity)
	{
		this.orderQuantity = orderQuantity;
	}
	public int getOrderQuantity()
	{
		return orderQuantity;
	}
	public String getPname()
	{
		return pname;
	}
	public double getSubTotal()
	{
		return subTotal;
	}
	/*Calculate the subtotal cost of the OrderDetail by multiplying the quantity of the product
	by its cost*/
	public void calculateSubTotal()
	{
		subTotal = orderQuantity * price;
	}
	/*Set the fields of the OrderDetail object to the input file data*/
	public void readData(Scanner input) throws InputMismatchException
	{
		pname = input.next();
		price = input.nextDouble();
		orderQuantity = input.nextInt();
	}
	/*Write a text-based representation of the OrderDetail object into the output file*/
	public void writeData(Formatter formatter)
	{
		formatter.format("%s,%.1f,%d\n", pname, price, orderQuantity);
	}
	/*Return the string representation of the OrderDetail's information in a format that can be interpreted
	by the user. @Override annotation indicates that the method should take precedence*/
	@Override
	public String toString()
	{
		return String.format("Product name: %s%nPrice: %.1f%nOrder quantity: %d%nSub total: %.1f", pname, price, orderQuantity, subTotal);
	}
}