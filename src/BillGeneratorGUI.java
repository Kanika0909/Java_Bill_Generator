import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// Item.java
class Item {
    private String name;
    private int quantity;
    private double price;

    public Item(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public double getTotal() {
        return quantity * price;
    }

    public Object[] toRow() {
        return new Object[]{name, quantity, price, getTotal()};
    }
}

// Bill.java
class Bill {
    private ArrayList<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }

    public double getTotalBill() {
        return items.stream().mapToDouble(Item::getTotal).sum();
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}

// GUI Class
public class BillGeneratorGUI extends JFrame {
    private JTextField nameField, quantityField, priceField;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private Bill bill;

    public BillGeneratorGUI() {
        bill = new Bill();
        setTitle("Java Bill Generator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel - Input Fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Item"));

        nameField = new JTextField();
        quantityField = new JTextField();
        priceField = new JTextField();

        JButton addButton = new JButton("Add Item");
        addButton.addActionListener(e -> addItem());

        inputPanel.add(new JLabel("Item Name:"));
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(new JLabel(""));
        inputPanel.add(nameField);
        inputPanel.add(quantityField);
        inputPanel.add(priceField);
        inputPanel.add(addButton);

        // Center - Table
        String[] columns = {"Name", "Quantity", "Price", "Total"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Bottom - Total and Generate
        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total: ₹0.00");
        JButton generateButton = new JButton("Generate Bill");

        generateButton.addActionListener(e -> generateBill());
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(generateButton, BorderLayout.EAST);

        // Add panels to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addItem() {
        try {
            String name = nameField.getText();
            int qty = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            Item item = new Item(name, qty, price);
            bill.addItem(item);
            tableModel.addRow(item.toRow());
            totalLabel.setText("Total: ₹" + bill.getTotalBill());

            nameField.setText("");
            quantityField.setText("");
            priceField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter correct values.");
        }
    }

    private void generateBill() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n------ BILL ------\n");
        for (Item item : bill.getItems()) {
            sb.append(item.toRow()[0]).append(" x").append(item.toRow()[1])
                    .append(" = ₹").append(item.toRow()[3]).append("\n");
        }
        sb.append("------------------\n");
        sb.append("Total Amount: ₹").append(bill.getTotalBill()).append("\n");

        JOptionPane.showMessageDialog(this, sb.toString(), "Generated Bill", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BillGeneratorGUI().setVisible(true));
    }
}
