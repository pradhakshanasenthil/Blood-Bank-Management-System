import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class BloodBankManagementSystemRefactored extends JFrame {

    JTextField txtUsername = new JTextField();
    JPasswordField txtPassword = new JPasswordField();

    private static final String URL = "jdbc:mysql://localhost:3306/bloodbank";
    private static final String USER = "root";
    private static final String PASS = "abcd";
    private static final Color BG_COLOR = new Color(236, 240, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(10, 83, 173);
    private static final Color SECONDARY_COLOR = new Color(46, 53, 64);
    private static final Color DANGER_COLOR = new Color(176, 32, 37);
    private static final Color TEXT_DARK = new Color(20, 24, 28);
    private static final Color MUTED_TEXT = new Color(45, 54, 65);
    private static final Color FIELD_BG = new Color(255, 255, 255);
    private static final Color FIELD_BORDER = new Color(140, 150, 160);
    private static final Color HEADER_BG = new Color(210, 219, 232);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final String[] BLOOD_GROUPS = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    public BloodBankManagementSystemRefactored() {
        loginScreen();
    }

    // ================= COMMON CHECK METHODS =================
    
    // Check if blood fields are valid
    private boolean checkBloodFields(String group, String units) {
        if(group == null || group.isEmpty() || units.isEmpty()){
            JOptionPane.showMessageDialog(null, "Fill all fields");
            return false;
        }
        try {
            Integer.parseInt(units);
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Units must be a valid number");
            return false;
        }
    }

    // Check if donor fields are valid
    private boolean checkDonorFields(String name, String group, String phone) {
        if(name.isEmpty() || group == null || group.isEmpty() || phone.isEmpty()){
            JOptionPane.showMessageDialog(null, "Fill all fields");
            return false;
        }
        if(!phone.matches("\\d{10}")){
            JOptionPane.showMessageDialog(null, "Phone must be 10 digits");
            return false;
        }
        return true;
    }

    // ================= COMMON ADD METHODS =================
    
    // Add blood to database
    private void addBlood(String group, String units, DefaultTableModel model, JComboBox<String> groupCombo, JTextField unitsField) {
        try {
            if(!checkBloodFields(group, units)) return;

            Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO blood_units(blood_group, units_available) VALUES (?,?)"
            );
            ps.setString(1, group);
            ps.setInt(2, Integer.parseInt(units));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Blood Added Successfully");
            loadBlood(model);
            groupCombo.setSelectedIndex(0);
            unitsField.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    // Add donor to database
    private void addDonor(String name, String group, String phone, DefaultTableModel model, 
                         JTextField nameField, JComboBox<String> groupCombo, JTextField phoneField) {
        try {
            if(!checkDonorFields(name, group, phone)) return;

            Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO donors(name, blood_group, phone) VALUES (?,?,?)"
            );
            ps.setString(1, name);
            ps.setString(2, group);
            ps.setString(3, phone);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Donor Added Successfully");
            loadDonors(model);
            nameField.setText("");
            groupCombo.setSelectedIndex(0);
            phoneField.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    // ================= UI STYLING METHODS =================
    
    private JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_COLOR);
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));
        return panel;
    }

    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(TITLE_FONT);
        label.setForeground(PRIMARY_COLOR);
        return label;
    }

    private JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(SUBTITLE_FONT);
        label.setForeground(MUTED_TEXT);
        return label;
    }

    private JButton createButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bg);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setBorder(new EmptyBorder(8, 14, 8, 14));
        return button;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_DARK);
        return label;
    }

    private void styleField(JTextField field) {
        field.setFont(BODY_FONT);
        field.setForeground(TEXT_DARK);
        field.setBackground(FIELD_BG);
        field.setCaretColor(TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(FIELD_BORDER),
            new EmptyBorder(5, 7, 5, 7)
        ));
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(BODY_FONT);
        combo.setForeground(TEXT_DARK);
        combo.setBackground(FIELD_BG);
        combo.setBorder(BorderFactory.createLineBorder(FIELD_BORDER));
    }

    private void styleTable(JTable table) {
        table.setFont(BODY_FONT);
        table.setForeground(TEXT_DARK);
        table.setBackground(Color.WHITE);
        table.setRowHeight(24);
        table.setGridColor(new Color(205, 212, 220));
        table.setSelectionBackground(new Color(10, 83, 173));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(HEADER_BG);
        table.getTableHeader().setForeground(TEXT_DARK);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    // ================= DATABASE CONNECTION =================
    
    private Connection connect() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // ================= LOGIN MODULE =================
    
    private void loginScreen() {
        setTitle("Login");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BG_COLOR);
        setLayout(new BorderLayout(12, 12));

        JPanel topPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(12, 12, 0, 12));
        topPanel.add(createTitleLabel("Blood Bank Management"));
        topPanel.add(createSubtitleLabel("Secure access for administrators"));
        add(topPanel, BorderLayout.NORTH);

        JPanel formPanel = createCardPanel();
        formPanel.setLayout(new GridLayout(3, 2, 12, 12));
        styleField(txtUsername);
        styleField(txtPassword);
        formPanel.add(createFieldLabel("Username"));
        formPanel.add(txtUsername);
        formPanel.add(createFieldLabel("Password"));
        formPanel.add(txtPassword);

        JButton btnLogin = createButton("Login", PRIMARY_COLOR);
        btnLogin.setPreferredSize(new Dimension(110, 32));
        formPanel.add(new JLabel(""));
        formPanel.add(btnLogin);

        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.setOpaque(false);
        centerWrap.setBorder(new EmptyBorder(0, 24, 24, 24));
        centerWrap.add(formPanel, BorderLayout.CENTER);
        add(centerWrap, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> login(this));
        setVisible(true);
    }

    private void login(JFrame frame) {
        try {
            Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM admin WHERE username=? AND password=?"
            );
            ps.setString(1, txtUsername.getText());
            ps.setString(2, new String(txtPassword.getPassword()));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                frame.dispose();
                dashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void dashboard() {
        JFrame frame = new JFrame("Dashboard");
        frame.setSize(500, 330);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(BG_COLOR);

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(16, 16, 0, 16));
        headerPanel.add(createTitleLabel("Dashboard"));
        headerPanel.add(createSubtitleLabel("Choose a module to continue"));

        JPanel actionPanel = createCardPanel();
        actionPanel.setLayout(new GridLayout(3, 1, 12, 12));

        JButton btnBlood = createButton("Manage Blood Inventory", PRIMARY_COLOR);
        JButton btnDonor = createButton("Manage Donors", SECONDARY_COLOR);
        JButton btnLogout = createButton("Logout", SECONDARY_COLOR);

        btnBlood.addActionListener(e -> manageBlood());
        btnDonor.addActionListener(e -> manageDonors());
        btnLogout.addActionListener(e -> System.exit(0));

        actionPanel.add(btnBlood);
        actionPanel.add(btnDonor);
        actionPanel.add(btnLogout);
        frame.add(headerPanel, BorderLayout.NORTH);

        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.setOpaque(false);
        centerWrap.setBorder(new EmptyBorder(4, 24, 24, 24));
        centerWrap.add(actionPanel, BorderLayout.CENTER);
        frame.add(centerWrap, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // ================= BLOOD INVENTORY MANAGEMENT =================
    
    private void manageBlood() {
        JFrame frame = new JFrame("Manage Blood");
        frame.setSize(760, 440);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(BG_COLOR);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Group","Units"},0);
        JTable table = new JTable(model);
        styleTable(table);
        loadBlood(model);

        JComboBox<String> group = new JComboBox<>(BLOOD_GROUPS);
        JTextField units = new JTextField();
        styleCombo(group);
        styleField(units);

        JButton addBtn = createButton("Add", PRIMARY_COLOR);
        JButton updateBtn = createButton("Update", new Color(100, 150, 200));
        JButton deleteBtn = createButton("Delete", DANGER_COLOR);
        JButton refreshBtn = createButton("Refresh", SECONDARY_COLOR);

        // Add button - uses common add method
        addBtn.addActionListener(e -> {
            String selectedGroup = (String) group.getSelectedItem();
            addBlood(selectedGroup, units.getText(), model, group, units);
        });

        // Update button
        updateBtn.addActionListener(e -> {
            try {
                int row = table.getSelectedRow();
                if(row < 0){
                    JOptionPane.showMessageDialog(frame, "Select a row to update");
                    return;
                }
                int id = (int) model.getValueAt(row, 0);
                String selectedGroup = (String) group.getSelectedItem();
                if(!checkBloodFields(selectedGroup, units.getText())) return;

                Connection conn = connect();
                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE blood_units SET blood_group=?, units_available=? WHERE id=?"
                );
                ps.setString(1, selectedGroup);
                ps.setInt(2, Integer.parseInt(units.getText()));
                ps.setInt(3, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Updated Successfully");
                loadBlood(model);
                group.setSelectedIndex(0);
                units.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        // Delete button
        deleteBtn.addActionListener(e -> {
            try {
                int row = table.getSelectedRow();
                if(row>=0){
                    int confirm = JOptionPane.showConfirmDialog(
                        frame,
                        "Delete selected blood entry?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm != JOptionPane.YES_OPTION) return;

                    int id = (int) model.getValueAt(row,0);
                    Connection conn = connect();
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM blood_units WHERE id=?");
                    ps.setInt(1,id);
                    ps.executeUpdate();
                    loadBlood(model);
                } else {
                    JOptionPane.showMessageDialog(frame, "Select a row to delete");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,ex.getMessage());
            }
        });

        // Refresh button
        refreshBtn.addActionListener(e -> loadBlood(model));

        JPanel panel = createCardPanel();
        panel.setLayout(new GridLayout(5,2,8,8));
        panel.add(createFieldLabel("Blood Group"));
        panel.add(group);
        panel.add(createFieldLabel("Units"));
        panel.add(units);
        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(refreshBtn);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        JPanel topWrap = new JPanel(new BorderLayout(6, 6));
        topWrap.setOpaque(false);
        topWrap.setBorder(new EmptyBorder(10, 12, 0, 12));
        topWrap.add(createSubtitleLabel("Blood Inventory"), BorderLayout.NORTH);
        topWrap.add(panel, BorderLayout.CENTER);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        JPanel tableWrap = createCardPanel();
        tableWrap.setLayout(new BorderLayout());
        tableWrap.add(tableScroll, BorderLayout.CENTER);
        tableWrap.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.setOpaque(false);
        centerWrap.setBorder(new EmptyBorder(0, 12, 12, 12));
        centerWrap.add(tableWrap, BorderLayout.CENTER);

        frame.add(topWrap,BorderLayout.NORTH);
        frame.add(centerWrap,BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void loadBlood(DefaultTableModel model) {
        try {
            model.setRowCount(0);
            Connection conn = connect();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM blood_units");
            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("blood_group"),
                    rs.getInt("units_available")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    // ================= DONOR MANAGEMENT =================
    
    private void manageDonors() {
        JFrame frame = new JFrame("Manage Donors");
        frame.setSize(820, 460);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(BG_COLOR);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Name","Group","Phone"},0);
        JTable table = new JTable(model);
        styleTable(table);
        loadDonors(model);

        JTextField name = new JTextField();
        JComboBox<String> group = new JComboBox<>(BLOOD_GROUPS);
        JTextField phone = new JTextField();
        styleField(name);
        styleCombo(group);
        styleField(phone);

        JButton addBtn = createButton("Add", PRIMARY_COLOR);
        JButton updateBtn = createButton("Update", new Color(100, 150, 200));
        JButton deleteBtn = createButton("Delete", DANGER_COLOR);
        JButton refreshBtn = createButton("Refresh", SECONDARY_COLOR);

        // Add button - uses common add method
        addBtn.addActionListener(e -> {
            String selectedGroup = (String) group.getSelectedItem();
            addDonor(name.getText(), selectedGroup, phone.getText(), model, name, group, phone);
        });

        // Update button
        updateBtn.addActionListener(e -> {
            try {
                int row = table.getSelectedRow();
                if(row < 0){
                    JOptionPane.showMessageDialog(frame, "Select a row to update");
                    return;
                }
                int id = (int) model.getValueAt(row, 0);
                String selectedGroup = (String) group.getSelectedItem();
                if(!checkDonorFields(name.getText(), selectedGroup, phone.getText())) return;

                Connection conn = connect();
                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE donors SET name=?, blood_group=?, phone=? WHERE id=?"
                );
                ps.setString(1, name.getText());
                ps.setString(2, selectedGroup);
                ps.setString(3, phone.getText());
                ps.setInt(4, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Updated Successfully");
                loadDonors(model);
                name.setText("");
                group.setSelectedIndex(0);
                phone.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        // Delete button
        deleteBtn.addActionListener(e -> {
            try {
                int row = table.getSelectedRow();
                if(row>=0){
                    int confirm = JOptionPane.showConfirmDialog(
                        frame,
                        "Delete selected donor?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm != JOptionPane.YES_OPTION) return;

                    int id = (int) model.getValueAt(row,0);
                    Connection conn = connect();
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM donors WHERE id=?");
                    ps.setInt(1,id);
                    ps.executeUpdate();
                    loadDonors(model);
                } else {
                    JOptionPane.showMessageDialog(frame, "Select a row to delete");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,ex.getMessage());
            }
        });

        // Refresh button
        refreshBtn.addActionListener(e -> loadDonors(model));

        JPanel panel = createCardPanel();
        panel.setLayout(new GridLayout(6,2,8,8));
        panel.add(createFieldLabel("Name"));
        panel.add(name);
        panel.add(createFieldLabel("Blood Group"));
        panel.add(group);
        panel.add(createFieldLabel("Phone"));
        panel.add(phone);
        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(refreshBtn);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        JPanel topWrap = new JPanel(new BorderLayout(6, 6));
        topWrap.setOpaque(false);
        topWrap.setBorder(new EmptyBorder(10, 12, 0, 12));
        topWrap.add(createSubtitleLabel("Donor Directory"), BorderLayout.NORTH);
        topWrap.add(panel, BorderLayout.CENTER);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        JPanel tableWrap = createCardPanel();
        tableWrap.setLayout(new BorderLayout());
        tableWrap.add(tableScroll, BorderLayout.CENTER);
        tableWrap.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.setOpaque(false);
        centerWrap.setBorder(new EmptyBorder(0, 12, 12, 12));
        centerWrap.add(tableWrap, BorderLayout.CENTER);

        frame.add(topWrap,BorderLayout.NORTH);
        frame.add(centerWrap,BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void loadDonors(DefaultTableModel model) {
        try {
            model.setRowCount(0);
            Connection conn = connect();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM donors");
            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("blood_group"),
                    rs.getString("phone")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Defaults to Swing look and feel if system look and feel is unavailable.
            }
            new BloodBankManagementSystemRefactored();
        });
    }
}
