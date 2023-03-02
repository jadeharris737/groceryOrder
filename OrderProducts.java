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

/*Display the product ordering Graphical User Interface (GUI), load and display product information,
 allow the user to order and unorder items, then save or discard an order.*/
class OrderProducts extends JFrame implements ActionListener, ListSelectionListener
{
	private OrderSystem orderSystem;
	private JLabel productLabel, orderLabel, messageLabel;
	private JButton okButton, cancelButton, orderButton, unOrderButton;
	private Container container;
	private GridBagLayout gridBagLayout;
	private GridBagConstraints constraints;
	private JList<String> productList, orderList;
	private DefaultListModel<String> productDefaultList, orderDefaultList;
	private JTextArea productDetailsTextArea, orderDetailsTextArea;
	private JTextField messageField;
	private ArrayList<Product> products;
	private ArrayList<OrderDetail> orderDetails;

	/*Default constructor to create the GUI and load the data into the GUI*/
	public OrderProducts(ArrayList<Product> products, OrderSystem orderSystem) 
	{
		/*Call the JFrame constructor and set the title of the container window*/
		super("Order products");
		
		/*Initialise an empty lists for all order details that the user selects in this interface*/
		orderDetails = new ArrayList<OrderDetail>();
		/*Store reference to the ordering system GUI*/
		this.orderSystem = orderSystem;
		/*Initialise the list of products for use in the class*/
		this.products = products;
		
		/*Specify that when the exit button is clicked, application will exit*/
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*Set the size of the window, a method inerhited from JFrame*/
		setSize(1000, 600);
		/*Show the GUI*/
		setVisible(true);
		
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
		
		
		/*Add the GUI components for products and ordering them*/
		addProductComponents();
		
		/*Add the button GUI components*/
		addButtons();
		
		/*Add the GUI component that will display messages*/
		addMessages();
    }
	
	/*Add the GUI components for to order products*/
	private void addProductComponents()
	{

		/*Add the GUI titles in Jlabels*/
		productLabel = new JLabel("Product Name");
		/*As the label component will not take up a whole cell in the GridBagLayout, create a panel with a FlowLayout of 
		'left' alignment so that the panel can have a left margin */
		FlowLayout leftFlowLayout = new FlowLayout(FlowLayout.LEFT);
		JPanel productLabelPanel = new JPanel(leftFlowLayout);
		/*Add a horizontal strut to the panel, which causes the element to be shifted 30 pixels from the left. This
		creates the effect of a left margin as shown on the interface.*/
		productLabelPanel.add(Box.createHorizontalStrut(30));
		/*After the horizontal strut has taken up 30 pixels of space, add the label.*/
		productLabelPanel.add(productLabel);
		/*Set the constraints for placing the label in the GridBagLayout. Weightx and weighty decimal percent set
		horizontal and vertical spacing respectively. Gridwidth specifies how many cells that the component should
		take up in the GridBagLayout. Gridx and gridy specify the coordiantes for the grid that the component should
		be positioned. Finally, the component is added to the container within the above constraints.*/
		constraints.weightx = 0.5;
		constraints.weighty = 0.5;
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		container.add(productLabelPanel, constraints);
		
		
		/*As mentioned above, weightx, weighty do not change for any components, therefore the GridBagConstraint object
		does not need to update these fields. The coordinate for the cell that this label should be positioned in however
		does change*/
		orderLabel = new JLabel("Order");
		JPanel orderLabelPanel = new JPanel(leftFlowLayout);
		orderLabelPanel.add(orderLabel);
		constraints.gridx = 2;
		constraints.gridy = 0;
		container.add(orderLabelPanel, constraints);
		
		
		/*Add a JList to store the available products. The DefaultListModel object offers a simplified method to manage
		the objects (in this case each product name String) in the list view.*/
		productDefaultList = new DefaultListModel<>();
		for(Product product: products)
		{
			productDefaultList.addElement(product.getPname());
		}
		/*Create a JList component that is managed by the contents in the DefaultListModel*/
		productList = new JList<>(productDefaultList);
		/*Specify only 10 product names can be viewed at once*/
		productList.setVisibleRowCount(10);
		/*Specify that the list is 350 pixels wide*/
		productList.setFixedCellWidth(350);
		/*Specify the selection mode of the list so that a user can only select one product at a time.
		This also allows the JList to generate a ListSelectionEvent.*/
		productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		/*Add the product list to a panel so that the component can have a 30 pixel left margin using a
		horizontal strut*/
		JPanel productListPanel = new JPanel(leftFlowLayout);
		productListPanel.add(Box.createHorizontalStrut(30));
		/*Then, create a JScrollPane object which the JList is placed into. Add the component with the scroll bar to the panel.*/
		productListPanel.add(new JScrollPane(productList), constraints);
		/*Listen and generate an event when a new item is selected from the list. A ‘this’ reference is given because the
		current ordering system object implements the ListSelectionListener Interface, which is required for the class that
		contains the instructions for the listener.*/
		productList.addListSelectionListener(this);
		
		/*Create another list for each item that the user adds to their order*/
		orderDefaultList = new DefaultListModel<>();
		orderList = new JList<>(orderDefaultList);
		orderList.setVisibleRowCount(10);
		orderList.setFixedCellWidth(350);
		orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		orderList.addListSelectionListener(this);
		JPanel orderListPanel = new JPanel(leftFlowLayout);
		orderListPanel.add(new JScrollPane(orderList));
		
		
	
		/*Add the text area to display the details of the selected product. Namely, the quantity of stock
		left for users to order*/
		productDetailsTextArea = new JTextArea(12, 33);
		/*Specify that the user cannot edit data in the text area to improve usability of the program.*/
		productDetailsTextArea.setEditable(false);
		/*Continue to the next line when the edge of the text area is reached*/
		productDetailsTextArea.setLineWrap(true);
		/*Move to the next line while preserving each full word*/
		productDetailsTextArea.setWrapStyleWord(true);
		JPanel productDetailsTextAreaPanel = new JPanel(leftFlowLayout);
		productDetailsTextAreaPanel.add(Box.createHorizontalStrut(30));
		productDetailsTextAreaPanel.add(productDetailsTextArea);
	
	
		/*Add another text area to display the details of the OrderDetail added to the product. Namely,
		quantity of how many items the user has ordered.*/	
		orderDetailsTextArea = new JTextArea(12, 33);
		orderDetailsTextArea.setEditable(false);
		orderDetailsTextArea.setLineWrap(true);
		orderDetailsTextArea.setWrapStyleWord(true);
		JPanel orderDetailsTextAreaPanel = new JPanel(leftFlowLayout);
		orderDetailsTextAreaPanel.add(orderDetailsTextArea);
		
		/*Use a panel with the BorderLayout to position the information relating to the selected
		product list item adjacently on the interface*/
		Panel allProductInformation = new Panel();
		allProductInformation.setLayout(new BorderLayout());
		allProductInformation.add(productListPanel, BorderLayout.NORTH);
		allProductInformation.add(productDetailsTextAreaPanel, BorderLayout.SOUTH);
		/*Specify a grid height of 2 to take up two cells vertically of the GridBagLayout*/
		constraints.gridheight = 2;
		constraints.gridx = 0;
		constraints.gridy = 1;
		container.add(allProductInformation, constraints);
		
		/*Use a panel with the BorderLayout to position the information relating to the selected
		order list item adjacently on the interface*/
		Panel allOrderInformation = new Panel();
		allOrderInformation.setLayout(new BorderLayout());
		allOrderInformation.add(orderListPanel, BorderLayout.NORTH);
		allOrderInformation.add(orderDetailsTextAreaPanel, BorderLayout.SOUTH);
		constraints.gridheight = 2;
		constraints.gridx = 2;
		constraints.gridy = 1;
		container.add(allOrderInformation, constraints);
			
	}
	
	/*Add the GUI button components and generate events when the button is activated.*/
	private void addButtons()
	{
		/*As the button component will not take up a whole cell in the GridBagLayout, create a panel with a FlowLayout of 
		'right' alignment so that the button is in the right hand side of the cell. This causes the okButton and cancelButton
		to appear beside each other in the centre of the GUI*/
		Panel okButtonPanel = new Panel();
		FlowLayout rightFlowLayout = new FlowLayout(FlowLayout.RIGHT);
		okButtonPanel.setLayout(rightFlowLayout);
		okButton = new JButton("Ok");
		okButtonPanel.add(okButton);
		/*Listen and generate an event when an action occurs to the button. A ‘this’ reference is given because the
		current product ordering class implements the ActionListener interface, which is required for the class that
		contains the instructions for the listener.*/
		okButton.addActionListener(this);
		
		Panel cancelButtonPanel = new Panel();
		FlowLayout leftFlowLayout = new FlowLayout(FlowLayout.LEFT);
		cancelButtonPanel.setLayout(leftFlowLayout);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);		
		cancelButtonPanel.add(cancelButton);
		
		/*Use a panel with the FlowLayout to position the ok and cancel button centered together on the interface*/
		Panel okCancelButtonPanel = new Panel();
		okCancelButtonPanel.setLayout(new FlowLayout());
		okCancelButtonPanel.add(okButtonPanel);
		okCancelButtonPanel.add(cancelButtonPanel);
		/*Specify a grid width of 2 to take up three cells horizontally of the GridBagLayout*/
		constraints.gridwidth = 3;
		constraints.gridx = 0;
		constraints.gridy = 3;
		container.add(okCancelButtonPanel, constraints);
	
		Panel orderButtonPanel = new Panel();
		orderButtonPanel.setLayout(leftFlowLayout);
		orderButton = new JButton("  Order>>  ");
		orderButtonPanel.add(orderButton);
		orderButton.addActionListener(this);
		
		Panel unOrderButtonPanel = new Panel();
		unOrderButtonPanel.setLayout(leftFlowLayout);
		unOrderButton = new JButton("Unorder<<");
		unOrderButtonPanel.add(unOrderButton);
		unOrderButton.addActionListener(this);	
		
		/*Use a panel with the BorderLayout to position the information relating to the ordering and unordering products
		vertically grouped on the interface*/
		Panel orderUnOrderButtonPanel = new Panel();
		BorderLayout borderLayout = new BorderLayout();
		orderUnOrderButtonPanel.setLayout(borderLayout);
		orderUnOrderButtonPanel.add(orderButtonPanel, BorderLayout.NORTH);
		orderUnOrderButtonPanel.add(unOrderButtonPanel, BorderLayout.SOUTH);
		constraints.gridwidth = 1;
		constraints.gridx = 1;
		constraints.gridy = 1;
		container.add(orderUnOrderButtonPanel, constraints);
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
		
		/*Specify a grid width of 2 to take up three cells horizontally of the GridBagLayout and span the window*/
		constraints.gridwidth = 3;
		constraints.gridx = 0;
		constraints.gridy = 5;
		container.add(messagePanel, constraints);
	}
	
	/*Implement the method from the ActionHandler interface that specifies the action that the system should
	perform in response to an event, which is generated from a component with an ActionListener.
	In this system, this generally occurs from buttons. @Override annotation indicates that the method should take precedence*/
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		/*If the user has activated the okButton, add the order in the orderSystem class by sending over the current
		list of order details. Hide the product ordering window.*/
		if(event.getSource() == okButton) 
		{
			setVisible(false);
			orderSystem.addOrder(orderDetails);			
		}
		/*If the user has activated the okButton, do not save the order and increase the stock quantity by the order
		quantities. Hide the product ordering window.*/
		else if(event.getSource() == cancelButton)
		{
			resetStockQuantity();
			setVisible(false);
		}
		/*If the user has activated the orderButton, move the selected product from the product list into the order list
		and adjust quantities accordingly. Ensure that a product to move is selected in the product list*/
		else if(event.getSource() == orderButton)
		{
			/*Check that a product is selected (index is not -1)*/
			if(productList.getSelectedIndex()> -1)
			{
					int productIndex = productList.getSelectedIndex();
					/*Store the users currently selected product*/
					Product currentProduct = products.get(productIndex);
					
					/*Check if the product has any stock that the user can order. If so, reduce the stock and update the
					product's information displayed in the text area. Otherwise, display an error message.*/
					if(currentProduct.getStockQuantity() > 0)
					{					
						currentProduct.setStockQuantity(currentProduct.getStockQuantity() - 1);
						/*Refresh infromation*/
						productDetailsTextArea.setText(currentProduct.toString());

						/*Check if the product already exists on the users order list. If so, increase the ordered quantity and update the
						order detail's information displayed in the text area. Otherwise, add a new item to the order list.*/
						OrderDetail newDetail;
						/*Find the index of product in the orderDetails list. If the product hasn't yet been ordered,
						-1 will be returned. This is because an item may have the index of '5' in the list of products,
						but have been added second to the user's order list thus only have the index of '1'*/
						int orderItemIndex = findOrderItemIndex(currentProduct.getPname());
						if(orderItemIndex  >= 0)
						{
							/*Retrieve the existing orderDetail, increase the orderDetail by 1 and update the subtotal.*/
							newDetail = orderDetails.get(orderItemIndex);
							newDetail.setOrderQuantity(newDetail.getOrderQuantity() + 1);
							newDetail.calculateSubTotal();
						}
						else
						{
							/*Instantiate a new orderDetail, add the product to the ordered list, and select the new
							orderDetail in the orders list.*/
							newDetail = new OrderDetail(currentProduct.getPname(), currentProduct.getPrice(), 1);
							newDetail.calculateSubTotal();
							orderDetails.add(newDetail);
							orderDefaultList.addElement(currentProduct.getPname());
							orderList.setSelectedIndex(orderDetails.size() - 1);
						}
						
						/*Update the order detail's information with data such as the new quantity*/
						orderDetailsTextArea.setText(newDetail.toString());
						messageField.setText("The product is ordered.");
					}
					else
					{
						messageField.setText("The product is not in stock.");
					}
			}
		}
		/*If the user has activated the unOrderButton, reduce the quantity of the orderDetail in the order list and increase
		the quantity of product available in the product list. If a order quantity of 0 is reached, remove the item from the
		order list view*/
		else if(event.getSource() == unOrderButton)
		{
			/*Check that an orderDetail is selected (index is not -1)*/
			if(orderList.getSelectedIndex()> -1)
			{
				/*Store the current OrderDetail stored by the user*/
				int orderListIndex = orderList.getSelectedIndex();
				OrderDetail currentDetail = orderDetails.get(orderListIndex);
				if(currentDetail.getOrderQuantity() - 1 == 0)
				{
					/*If decreasing the current order quantity one means that there the user has no longer
					ordered the product, remove the product from the order list.*/
					orderDefaultList.remove(orderListIndex);
					orderDetails.remove(orderListIndex);
					orderDetailsTextArea.setText("");
				}
				else
				{
					/*Otherwise, decrease the quantity of the ordered item and update the subtotal cost related to that
					order item.*/
					currentDetail.setOrderQuantity(currentDetail.getOrderQuantity() - 1);
					currentDetail.calculateSubTotal();
					orderDetailsTextArea.setText(currentDetail.toString());
				}
				
				/*Increase and update the stock quantity for the product that is being unordered*/
				int productIndex = findProductIndex(currentDetail.getPname());
				Product currentProduct = products.get(productIndex);
				currentProduct.setStockQuantity(currentProduct.getStockQuantity() + 1);
				productDetailsTextArea.setText(currentProduct.toString());
				
				messageField.setText("The product is unordered.");
			}
			
		}
	}
	
	/*As the user has cancelled their order, iterate through each ordered item and increase the stock quantity
	of the product by the order quantity that the user had made*/
	private void resetStockQuantity()
	{
		if(orderDetails.size() > 0)
		{
			for(OrderDetail orderDetail:orderDetails)
			{
				int i = findProductIndex(orderDetail.getPname());
				Product currentProduct = products.get(i);
				currentProduct.setStockQuantity(currentProduct.getStockQuantity() + orderDetail.getOrderQuantity());
			}
		}
	}
	
	/*Given the name of a product (generally from the list of OrderDetails, where the index of a product will be different
	to the position it is stored in the list of all products), return the index where the product exists in the product list.
	Return -1 if the user hasn't yet ordered the product.*/
	private int findOrderItemIndex(String pname)
	{
		int position = 0;
		int found = -1;
		if(orderDetails.size() > 0)
		{
			for(OrderDetail detail:orderDetails)
			{
				if(detail.getPname() == pname)
				{
					found = position;
				}
				position++;
			}
		}
		return found;
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
	
	/*Implement the method from the ListSelectionListener interface that specifies the action that the system should
	perform in response to an event when the list value changes, which is generated from a component with an ListSelectionListener.
	In this system, this occurs from JList components. @Override annotation indicates that the method should take precedence*/
	@Override
	public void valueChanged(ListSelectionEvent event) 
	{
		if(event.getSource()== productList)
		{
			int selectedProductIndex = productList.getSelectedIndex();
			/*Clear the product details. This will be updated in the following selection statement if another
			product is the new value selected in the list*/
			productDetailsTextArea.setText("");
			messageField.setText("");
			/*If a product has been selected (index is not -1)*/
			if(selectedProductIndex >= 0) 
			{	
				/*Display the details for the selected product*/
				Product currentProduct = products.get(selectedProductIndex);
				productDetailsTextArea.setText(currentProduct.toString());
			}
		}
		else if (event.getSource() == orderList)
		{
			int selectedOrderDetailIndex = orderList.getSelectedIndex();
			/*Clear the order details. This will be updated in the following selection statement if another
			OrderDetail is the new value selected in the list*/
			orderDetailsTextArea.setText("");
			messageField.setText("");
			/*If a OrderDetail has been selected (index is not -1)*/
			if(selectedOrderDetailIndex >= 0) 
			{	 
				/*Display the details for the selected OrderDetail*/
				OrderDetail currentOrderDetail = orderDetails.get(selectedOrderDetailIndex);
				orderDetailsTextArea.setText(currentOrderDetail.toString());
			}
		}
	}
}