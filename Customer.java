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

/*Model a customer with their code, name, address and phone number. Implement common methods
 to read and write data to text files, and format data to the user from the MyIO interface*/
class Customer implements MyFileIO
{
	private String code;
	private String name;
	private String address;
	private String phone;
	
	/*Default constructor to initialise a Customer with default values*/
	public Customer()
	{
		code = "";
		name = "";
		address = "";
		phone = "";
	}
	public String getCode()
	{
		return code;
	}
	/*Set the fields of the Customer object to the input file data*/
	public void readData(Scanner input) throws InputMismatchException
	{
		code = input.next();
		name = input.next();
		address = input.next();
		phone = input.next();
	}
	/*Write a text-based representation of the Customer object into the output file*/
	public void writeData(Formatter output)
	{
		output.format("%s,%s,%s,%s\n", code, name, address, phone);
	}
	/*Return the string representation of the Customer's information in a format that can be interpreted
	by the user. @Override annotation indicates that the method should take precedence*/
	@Override
	public String toString()
	{
		return String.format("Customer code: %s%nName: %s%nAddress: %s%nPhone: %s", code, name, address, phone);
	}
}