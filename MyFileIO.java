/*------------------------------------------------
My name: Jade Harris
My student number: 7519084
My course code: CSIT121
My email address: jh144@uowmail.edu.au
Assignment number: 3
------------------------------------------------*/

import java.util.*;


/*Define and standardise the common methods that unrelated classes in the program can perform to
read and write data to text files, and format data to the user.*/
interface MyFileIO
{
	public void readData(Scanner input);
	public void writeData(Formatter output);
	public String toString();
}