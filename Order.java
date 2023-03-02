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


/*Model an Order with its number, customer code, the system date the order was created, a list of the OrderDetail
products made in the Order, and the total cost. Implement common methods
to read and write data to text files, and format data to the user from the MyIO interface*/
class Order implements MyFileIO
{
	private int orderNumber;
	private String code;
	private String orderDate;
	private ArrayList<OrderDetail> orderDetails;
	private double total;
	
	/*Default constructor to initialise an Order object with default values*/
	public Order()
	{
		orderNumber = 0;
		code = "";
		orderDate = "";
		orderDetails = new ArrayList<OrderDetail>();
		total = 0.0;
	}
	/*Parametrised constructor to initialise an Order object with given values*/
	public Order(int orderNumber, String code, String orderDate, ArrayList<OrderDetail> orderDetails)
	{
		this.orderNumber = orderNumber;
		this.code = code;
		this.orderDate = orderDate;
		this.orderDetails = orderDetails;
		total = 0.0;
	}
	/*Calculate the total cost of the Order by summing the subtotal of each OrderDetail in the Order*/
	public void calculateTotal()
	{
		for(OrderDetail detail:orderDetails)
		{
			total += detail.getSubTotal();
		}
	}
	/*Set the fields of the Order object to the input file data.*/
	public void readData(Scanner input) throws InputMismatchException
	{
			orderNumber = input.nextInt();
			code = input.next();
			orderDate = input.next();
			
			/*Based on the number of OrderDetails, a field stored in the text file, set the data
			for each OrderDetail in a new object then add the OrderDetail object to the Order*/
			int numberOfDetails = input.nextInt();
			for(int i = 0; i < numberOfDetails; i ++)
			{
				OrderDetail orderDetail = new OrderDetail();
				orderDetail.readData(input);
				orderDetails.add(orderDetail);
			}	
	}
	/*Write a text-based representation of the Order object into the output file*/
	public void writeData(Formatter output)
	{
		output.format("%d,%s,%s\n%d\n", orderNumber, code, orderDate, orderDetails.size());
		
		/*Write a text-based representation of each OrderDetail object contained in the Order
		into the output file*/
		for(OrderDetail orderDetail: orderDetails)
		{
			orderDetail.writeData(output);
		}
	}
	/*Return the string representation of the Order's information in a format that can be interpreted
	by the user. @Override annotation indicates that the method should take precedence*/
	@Override
	public String toString()
	{
		return String.format("Order number: %d%nCode: %s%nOrder date: %s%nSize: %d%n", orderNumber, code, orderDate, orderDetails.size());
	}
}