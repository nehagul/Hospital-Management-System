package hospital.ui.dialogs;

import hospital.model.Patient;
import hospital.service.HospitalService;
import hospital.ui.UITheme;
import hospital.ui.VectorIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * SearchDialog.java
 * ---------------------------------------------------------
 * GUI counterpart of console searchPatient(): lets the operator search
 * by ID, Name, or Disease -- the exact same three options the console
 * menu offered -- and lists every matching record in a table.
 * ---------------------------------------------------------
 */
public class SearchDialog extends JDialog {

    public SearchDialog(Window owner, HospitalService service) {
        super(owner, "Search Patient", ModalityType.APPLICATION_MODAL);
        setSize(760, 500);
        setLocationRelativeTo(owner);

        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(UITheme.CARD);
        root.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JPanel heading = UITheme.headingWithIcon("Search Patient Records", VectorIcon.Type.SEARCH, UITheme.PRIMARY);

        JComboBox<String> modeBox = new JComboBox<>(new String[]{"By ID", "By Name", "By Disease"});
        UITheme.styleComboBox(modeBox);
        JTextField queryField = new JTextField();
        UITheme.styleTextField(queryField);
        JButton searchBtn = new JButton("Search");
        UITheme.stylePrimaryButton(searchBtn);

        JPanel controls = new JPanel(new BorderLayout(10, 0));
        controls.setBackground(UITheme.CARD);
        controls.add(modeBox, BorderLayout.WEST);
        controls.add(queryField, BorderLayout.CENTER);
        controls.add(searchBtn, BorderLayout.EAST);

        String[] columns = {"ID", "Name", "Age", "Gender", "Disease", "Doctor", "Room", "Priority"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        UITheme.styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);

        JLabel status = new JLabel(" ");
        status.setFont(UITheme.FONT_LABEL);
        status.setForeground(UITheme.TEXT_MUTED);

        searchBtn.addActionListener(e -> {
            String query = queryField.getText().trim();
            model.setRowCount(0);
            if (query.isEmpty()) {
                status.setText("Please enter a search value.");
                return;
            }
            List<Patient> results;
            String mode = (String) modeBox.getSelectedItem();
            if ("By ID".equals(mode)) {
                results = service.searchById(query);
            } else if ("By Name".equals(mode)) {
                results = service.searchByName(query);
            } else {
                results = service.searchByDisease(query);
            }
            for (Patient p : results) {
                model.addRow(new Object[]{p.getId(), p.getName(), p.getAge(), p.getGender(),
                        p.getDisease(), p.getDoctor(), p.getRoom(), p.getPriority()});
            }
            status.setText(results.isEmpty() ? "No patient found." : results.size() + " record(s) found.");
        });

        JButton closeBtn = new JButton("Close");
        UITheme.styleSecondaryButton(closeBtn);
        closeBtn.addActionListener(e -> dispose());
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.setBackground(UITheme.CARD);
        btnRow.add(closeBtn);

        JPanel north = new JPanel(new BorderLayout(0, 10));
        north.setBackground(UITheme.CARD);
        north.add(heading, BorderLayout.NORTH);
        north.add(controls, BorderLayout.SOUTH);

        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(UITheme.CARD);
        south.add(status, BorderLayout.WEST);
        south.add(btnRow, BorderLayout.EAST);

        root.add(north, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(south, BorderLayout.SOUTH);
        setContentPane(root);
    }
}
