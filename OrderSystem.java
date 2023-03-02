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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


/*The primary class used to display a Graphical User Interface (GUI), load and display customer information,
 allow the user to select a customer, interact with the system, and save an order into a text file.*/
public class OrderSystem extends JFrame implements ActionListener, ListSelectionListener
{
	private ArrayList<Customer> customers;
	private ArrayList<Product> products;
	private ArrayList<Order> orders;
	private JButton newOrderButton, saveOrderButton;
	private JLabel customerCodeLabel, customerDetailsLabel, messageLabel;
	private JTextField messageField;
	private Container container;
	private GridBagLayout gridBagLayout;
	private GridBagConstraints constraints;
	private JTextArea customerDetails;
	private JList<String> customerCodeList;
	private DefaultListModel<String> customerCodeDefaultList;
	private String selectedCustomerCode;
	
	public static void main(String[] args)
	{
		/*Instantiate a new system*/
		OrderSystem orderSystem = new OrderSystem();
		/*Specify that when the exit button is clicked, application will exit*/
        orderSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*Set the size of the window, a method inerhited from JFrame*/
		orderSystem.setSize(900, 600);
		/*Show the GUI*/
		orderSystem.setVisible(true);
		
	}
	
	/*Default constructor for the system to create the GUI and load the data into the system*/
	public OrderSystem()
	{
		/*Call the JFrame constructor and set the title of the container window*/
		super("Order System");
		
		/*Load customer data*/
		customers = new ArrayList<Customer>();		
		loadCustomers();
			
		/*Load product data*/
		products = new ArrayList<Product>();		
		loadProducts();
		
		/*Load order data if any exists*/
		orders = new ArrayList<Order>();
		loadOrders();
		
		/*Store the container 'layer' in the JFrame that is used to add and hold objects*/
		container = getContentPane();
		/*Set the GridBagLayout for the container*/
		gridBagLayout = new GridBagLayout();
		container.setLayout(gridBagLayout);
		/*Specify that the components in the container should be placed from left to right*/
		container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		/*Create a constraints object that will contain attributes to be applied to the container layout.
		Firstly, specify that elements should fill the entire horizontal length of a GridBag cell.*/
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		/*Add the GUI components for customers*/
		addCustomerComponents();
		 
		/*Add the button GUI components*/
		addButtons();
		
		/*Add the GUI component that will display messages*/
		addMessages();
	}

	/*Load the customers data into the container*/
	public void loadCustomers()
	{ 
		/*Each time the loadCustomers method is called, if an input type is incorrect with any order, display an error message
		only once. Reset each time customers are loaded.*/
		boolean inputMismatchThrown = false;
		
		Path path = Paths.get("customers.txt");
		try 
		{
			if(Files.exists(path)) 
			{
				if(!Files.isDirectory(path)) 
				{ 
					Scanner input = new Scanner(path);
					/*Set the deliminating pattern of the file, allowing the Scanner to break its input at the given
					symbols. This allows input.next() (for instance) to correctly correctly input the next segment of data*/
					input.useDelimiter(",|\r\n|\n");
					customers.clear();
					
					/*While there is another line in the file, initialise and add a new customer*/
					while(input.hasNext()) 
					{
						Customer customer = new Customer();
						customer.readData(input);
						customers.add(customer);
					}
					input.close();
				}
				else
				{
					JOptionPane.showMessageDialog(null, String.format("File %s is a directory. Failed to read the text file. Reload application.", path));
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, String.format("File %s does not exist. Failed to read the text file. Reload application.", path));
			}
		}
		catch (IOException err) 
		{
			/*Handle the exception if an issue occurred while accessing information using files*/
			JOptionPane.showMessageDialog(null, "IO exception error. Failed to read the customers.txt file. Reload application.");
		}
		catch (InputMismatchException err)
		{
			if(inputMismatchThrown == false)
			{
				/*Handle the exception if an issue occurred while reading field data from the text fields for any
				customer.*/
				JOptionPane.showMessageDialog(null, "Wrong input type. Failed to read the customers.txt file. Reload application.");
				inputMismatchThrown = true;
			}
		}
	}
	/*Load the products data into the container*/
	public void loadProducts()
	{
		/*Each time the loadProducts method is called, if an input type is incorrect with any order, display an error message
		only once. Reset each time products are loaded.*/
		boolean inputMismatchThrown = false;
		
		Path path = Paths.get("products.txt");
		try 
		{
			if(Files.exists(path)) 
			{
				if(!Files.isDirectory(path)) 
				{ 
					Scanner input = new Scanner(path);
					/*Set the deliminating pattern of the file, allowing the Scanner to break its input at the given
					symbols. This allows input.next() (for instance) to correctly correctly input the next segment of data*/
					input.useDelimiter(",|\r\n|\n");
					products.clear();
					
					/*While there is another line in the file, initialise and add a new Product*/
					while(input.hasNext()) 
					{
						Product product = new Product();
						product.readData(input);
						products.add(product);
					}
					input.close();
				}
				else
				{
					JOptionPane.showMessageDialog(null, String.format("File %s is a directory. Failed to read the products.txt file. Reload application.", path));
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, String.format("File %s does not exist. Failed to read the products.txt file. Reload application.", path));
			}
		}
		catch (IOException err) 
		{
			/*Handle the exception if an issue occurred while accessing information using files*/
			JOptionPane.showMessageDialog(null, "IO exception error. Failed to read the products.txt file. Reload application.");
		}
		catch (InputMismatchException err)
		{
			if(inputMismatchThrown == false)
			{
				/*Handle the exception if an issue occurred while reading field data from the text fields for any
				product.*/
				JOptionPane.showMessageDialog(null, "Wrong input type. Failed to read the products.txt file. Reload application.");
				inputMismatchThrown = true;
			}
		}
	}

	/*Load the orders data into the container*/
	public void loadOrders()
	{ 
		/*Each time the loadOrders method is called, if an input type is incorrect with any order, display an error message
		only once. Reset each time orders are loaded.*/
		boolean inputMismatchThrown = false;
		
		Path path = Paths.get("orders.txt");
		try 
		{
			if(Files.exists(path)) 
			{
				if(!Files.isDirectory(path)) 
				{ 
					Scanner input = new Scanner(path);
					/*Set the deliminating pattern of the file, allowing the Scanner to break its input at the given
					symbols. This allows input.next() (for instance) to correctly correctly input the next segment of data*/
					input.useDelimiter(",|\r\n|\n");
					orders.clear();
					
					/*While there is another line in the file, initialise and add a new Order*/
					while(input.hasNext()) 
					{
						Order order = new Order();
						order.readData(input);
						orders.add(order);
					}
					input.close();
				}
				else
				{
					JOptionPane.showMessageDialog(null, String.format("File %s is a directory. Failed to read the text file. Reload application.", path));
				}
			}
			else
			{
				//Error handling is no required here as orders.txt is not required for the system to read in intially.
			}
		}
		catch (IOException err) 
		{
			/*Handle the exception if an issue occurred while accessing information using files*/
			JOptionPane.showMessageDialog(null, "IO exception error. Failed to read the orders.txt file. Reload application.");
		}
		catch (InputMismatchException err)
		{
			if(inputMismatchThrown == false)
			{
				/*Handle the exception if an issue occurred while reading field data from the text fields for any
				order.*/
				JOptionPane.showMessageDialog(null, "Wrong input type. Failed to read the orders.txt file. Reload application.");
				inputMismatchThrown = true;
			}
		}
	}
	
	/*Add the GUI components for customers*/
	private void addCustomerComponents()
	{
		/*Add the GUI titles in Jlabels*/
		customerCodeLabel = new JLabel("Customer Code");
		/*Set the constraints for placing the label in the GridBagLayout. Weightx and weighty decimal percent set
		horizontal and vertical spacing respectively. Gridwidth specifies how many cells that the component should
		take up in the GridBagLayout. Gridx and gridy specify the coordiantes for the grid that the component should
		be positioned. Finally, the component is added to the container within the above constraints.*/
		constraints.weightx = 0.5;
		constraints.weighty = 0.5;
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		container.add(customerCodeLabel, constraints);
		
		/*As mentioned above, weightx, weighty do not change for any components, therefore the GridBagConstraint object
		does not need to update these fields. The coordinate for the cell that this label should be positioned in however
		does change*/
		customerDetailsLabel = new JLabel("Customer details");
		constraints.gridx = 1;
		constraints.gridy = 0;
		container.add(customerDetailsLabel, constraints);
		
		
		/*Add a JList to store customer codes. The DefaultListModel object offers a simplified method to manage
		the objects (in this case each customer code String) in the list view.*/
		customerCodeDefaultList = new DefaultListModel<>();
		for(Customer customer:customers)
		{
			customerCodeDefaultList.addElement(customer.getCode());
		}
		/*Create a JList component that is managed by the contents in the DefaultListModel*/
		customerCodeList = new JList<>(customerCodeDefaultList);
		/*Specify only 10 customer codes can be viewed at once*/
		customerCodeList.setVisibleRowCount(10); 
		/*Specify that the list is 400 pixels wide*/
		customerCodeList.setFixedCellWidth(400);
		/*Specify the selection mode of the list so that a user can only select one customer at a time.
		This also allows the JList to generate a ListSelectionEvent.*/
		customerCodeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		constraints.gridx = 0;
		constraints.gridy = 1;
		/*Create a JScrollPane object which the JList is placed into. Add the component with the scroll bar to the container.*/
		container.add(new JScrollPane(customerCodeList), constraints);
		/*Listen and generate an event when a new item is selected from the list. A ‘this’ reference is given because the
		current ordering system object implements the ListSelectionListener Interface, which is required for the class that
		contains the instructions for the listener.*/
		customerCodeList.addListSelectionListener(this);
		
		
		/*Add the text area to display the selected customers details*/
		customerDetails = new JTextArea(12, 20);
		/*Specify that the user cannot edit data in the text area to improve usability of the program.*/
		customerDetails.setEditable(false);
		/*Continue to the next line when the edge of the text area is reached*/
		customerDetails.setLineWrap(true);
		/*Move to the next line while preserving each full word*/
		customerDetails.setWrapStyleWord(true);
		constraints.gridx = 1;
		constraints.gridy = 1;
		container.add(customerDetails, constraints);
	}

	/*Add the GUI button components and generate events when the button is activated.*/
	private void addButtons()
	{
		/*As the button component will not take up a whole cell in the GridBagLayout, create a panel with a FlowLayout of 
		'center' alignment so that the button is in the centre of the cell*/
		Panel newOrderButtonPanel = new Panel();
		FlowLayout centerFlowLayout = new FlowLayout(FlowLayout.CENTER);
		newOrderButtonPanel.setLayout(centerFlowLayout);
		newOrderButton = new JButton("New Order");
		newOrderButtonPanel.add(newOrderButton);
		constraints.gridx = 0;
		constraints.gridy = 2;
		container.add(newOrderButtonPanel, constraints);
		/*Listen and generate an event when an action occurs to the button. A ‘this’ reference is given because the
		current ordering system class implements the ActionListener interface, which is required for the class that
		contains the instructions for the listener.*/
		newOrderButton.addActionListener(this);
		
		
		Panel saveOrderButtonPanel = new Panel();
		saveOrderButtonPanel.setLayout(centerFlowLayout);
		saveOrderButton = new JButton("Save Order");
		saveOrderButtonPanel.add(saveOrderButton);
		saveOrderButton.addActionListener(this);
		constraints.gridx = 1;
		constraints.gridy = 2;
		container.add(saveOrderButtonPanel, constraints);
	}
	/*Add the GUI component that will display messages to the user*/
	private void addMessages()
	{
		/*As the message label and message text field components will not take up a whole cell in the GridBagLayout, 
		create a panel with a BorderLayout by default so that the related components can be grouped and displayed
		together*/
		Panel messagePanel = new Panel();
		messageLabel = new JLabel("Messages    ");
		/*Create a text field with up to 50 characters.*/
		messageField = new JTextField(50);
		
		messagePanel.add(messageLabel);
		messagePanel.add(messageField);
		/*Specify a grid width of 2 to take up two cells horizontally of the GridBagLayout*/
		constraints.gridwidth = 2;
		constraints.gridx = 0;
		constraints.gridy = 3;
		container.add(messagePanel, constraints);
	}
	/*Implement the method from the ActionHandler interface that specifies the action that the system should
	perform in response to an event, which is generated from a component with an ActionListener.
	In this system, this generally occurs from buttons. @Override annotation indicates that the method should take precedence*/
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		/*If the user has activated the newOrderButton*/
		if(event.getSource() == newOrderButton) 
		{ 
			/*If a customer is selected in the list (and thus the index is not -1), open a new order for the customer.
			Otherwise, display an error message*/
			if (customerCodeList.getSelectedIndex() > -1)
			{
				messageField.setText("");
				/*Open the Order Products GUI*/
				OrderProducts orderProducts = new OrderProducts(products, this);
				/*Store the customer code that made the order.*/
				selectedCustomerCode = (customers.get(customerCodeList.getSelectedIndex())).getCode();
			}
			else
			{
				messageField.setText("Select a customer");
			}
		}
		/*If the user has activated the saveOrderButton*/
		else if(event.getSource() == saveOrderButton)
		{
			saveOrders();
			saveProducts();
		}
	}
	
	
	/*Implement the method from the ListSelectionListener interface that specifies the action that the system should
	perform in response to an event when the list value changes, which is generated from a component with an ListSelectionListener.
	In this system, this occurs from JList components. @Override annotation indicates that the method should take precedence*/
	@Override
	public void valueChanged(ListSelectionEvent event) 
	{
		int selectedCustomerIndex = customerCodeList.getSelectedIndex();
		customerDetails.setText("");
		/*If a customer has been selected (index is not -1)*/
		if(selectedCustomerIndex >= 0) 
		{	
			/*Display the details for the selected customer*/
			Customer currentCustomer = customers.get(selectedCustomerIndex);
			
			customerDetails.setText(currentCustomer.toString());
		}
	}
	
	/*Create and add a new order to the system's list of orders. Generate the system time and the order number.*/
	public void addOrder(ArrayList<OrderDetail> orderDetails)
	{
		/*Create a date time formatter Object to output the date in the given pattern.
		Obtain the local time on the user's system clock, then format the string into
		the formatter's pattern dd/MM/yyyy*/
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		String date = dtf.format(now);
		
		/*The order number is the size of the container plus one*/
		int orderNumber = orders.size() + 1;
		
		Order newOrder = new Order(orderNumber, selectedCustomerCode, date, orderDetails);
		
		newOrder.calculateTotal();
		orders.add(newOrder);
	}
	
	/*Given the name of a product (generally from the list of OrderDetails, where the index of a product will be different
	to the position it is stored in the list of all products), return the index where the product exists in the product list.
	This function is also reusablein a system where products can be removed, as it can return if the product exists at all. 
	In this ordering system though this is only a useful addition.*/
	private int findProductIndex(String pname)
	{
		int position = 0;
		int found = -1;
		if(products.size() > 0)
		{
			for(Product product:products)
			{
				if(product.getPname() == pname)
				{
					found = position;
				}
				position++;
			}
		}
		return found;
	}
	
	/*Write and store each of the order objects into the text file.*/
	private void saveOrders()
	{
		try 
		{
			/*Create a new file or update the existing contents.*/
			Formatter output = new Formatter("orders.txt");
			
			/*Write and store all of the orders into the text file, including any new orders*/
			for(Order order: orders) 
			{
				order.writeData(output);
			}
			
			output.close();
			messageField.setText("Orders are saved.");
		}
		catch (IOException err) 
		{
			/*Handle the exception if an issue occurred while writing information using files*/
			messageField.setText("IO exception error. Failed to write to the text file.");
		}	
	}
	
	/*Write and store each of the product objects into the text file with updated stock quantity.*/
	private void saveProducts()
	{
		try 
		{
			/*Update the existing contents.*/
			Formatter output = new Formatter("products.txt");
			
			/*Write and store all of the updated products into the text file*/
			for(Product product: products) 
			{
				product.writeData(output);
			}
			
			output.close();
		}
		catch (IOException err) 
		{
			/*Handle the exception if an issue occurred while writing information using files*/
			messageField.setText("IO exception error. Failed to write to the text file.");
		}	
	}
}