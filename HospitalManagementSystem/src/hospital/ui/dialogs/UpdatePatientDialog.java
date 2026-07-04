package hospital.ui.dialogs;

import hospital.model.Patient;
import hospital.service.HospitalService;
import hospital.ui.UITheme;
import hospital.ui.VectorIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UpdatePatientDialog extends JDialog {

    private boolean saved = false;

    public UpdatePatientDialog(Window owner, HospitalService service, String presetId) {
        super(owner, "Update Patient", ModalityType.APPLICATION_MODAL);
        setSize(480, 280);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(UITheme.CARD);
        root.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel heading = UITheme.headingWithIcon("Find Patient to Update", VectorIcon.Type.EDIT, UITheme.PRIMARY);

        // FIXED: Properly sized Patient ID field
        JTextField idField = new JTextField(presetId == null ? "" : presetId);
        idField.setPreferredSize(new Dimension(350, 45));
        idField.setMinimumSize(new Dimension(300, 40));
        idField.setMaximumSize(new Dimension(400, 48));
        idField.setEditable(true);
        idField.setEnabled(true);
        idField.setFocusable(true);
        idField.requestFocusInWindow();
        
        // Clean, consistent styling
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        idField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xDDE4E9), 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));
        idField.setBackground(Color.WHITE);
        idField.setForeground(Color.BLACK);

        JLabel error = new JLabel(" ");
        error.setForeground(UITheme.DANGER);
        error.setFont(UITheme.FONT_LABEL);

        JButton findBtn = new JButton("Find Patient");
        UITheme.stylePrimaryButton(findBtn);
        findBtn.setPreferredSize(new Dimension(140, 45));

        findBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                error.setText("Please enter a Patient ID.");
                return;
            }
            Patient p = service.findById(id);
            if (p == null) {
                error.setText("Patient not found!");
                return;
            }
            // Close this dialog and open edit form
            dispose();
            boolean result = showEditForm(owner, service, p);
            saved = result;
        });

        // ID panel with proper spacing
        JPanel idPanel = new JPanel(new BorderLayout(0, 8));
        idPanel.setBackground(UITheme.CARD);
        idPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        idPanel.add(UITheme.sectionLabel("Patient ID"), BorderLayout.NORTH);
        
        // Panel to hold ID field and button side by side
        JPanel idRow = new JPanel(new BorderLayout(12, 0));
        idRow.setBackground(UITheme.CARD);
        idRow.add(idField, BorderLayout.CENTER);
        idRow.add(findBtn, BorderLayout.EAST);
        idPanel.add(idRow, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(UITheme.CARD);
        south.add(error, BorderLayout.NORTH);
        
        root.add(heading, BorderLayout.NORTH);
        root.add(idPanel, BorderLayout.CENTER);
        root.add(south, BorderLayout.SOUTH);

        setContentPane(root);
    }

    /** Shows the field-editing form for an already-located patient. Returns true if saved. */
    private boolean showEditForm(Window owner, HospitalService service, Patient p) {
        JDialog dialog = new JDialog(owner, "Update Patient - " + p.getId(), ModalityType.APPLICATION_MODAL);
        dialog.setSize(480, 460);
        dialog.setLocationRelativeTo(owner);
        dialog.setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(UITheme.CARD);
        root.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JPanel heading = UITheme.headingWithIcon("Editing " + p.getName() + " (" + p.getId() + ")",
                VectorIcon.Type.EDIT, UITheme.PRIMARY);

        String[] options = {
                "Name", "Age", "Gender", "Disease", "Doctor", "Room Number", "Contact", "Emergency Priority"
        };
        JComboBox<String> fieldBox = new JComboBox<>(options);
        UITheme.styleComboBox(fieldBox);
        fieldBox.setPreferredSize(new Dimension(200, 35));

        JPanel valueArea = new JPanel(new CardLayout());
        valueArea.setBackground(UITheme.CARD);

        JTextField textValue = new JTextField();
        textValue.setPreferredSize(new Dimension(300, 40));
        textValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textValue.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xDDE4E9), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));

        JComboBox<String> genderValue = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        UITheme.styleComboBox(genderValue);
        genderValue.setPreferredSize(new Dimension(300, 40));

        JComboBox<String> priorityValue = new JComboBox<>(new String[]{"Normal", "High"});
        UITheme.styleComboBox(priorityValue);
        priorityValue.setPreferredSize(new Dimension(300, 40));

        CardLayout valueLayout = (CardLayout) valueArea.getLayout();
        valueArea.add(wrap(textValue), "TEXT");
        valueArea.add(wrap(genderValue), "GENDER");
        valueArea.add(wrap(priorityValue), "PRIORITY");

        Runnable syncSelection = () -> {
            String sel = (String) fieldBox.getSelectedItem();
            if ("Gender".equals(sel)) {
                valueLayout.show(valueArea, "GENDER");
                genderValue.setSelectedItem(p.getGender());
            } else if ("Emergency Priority".equals(sel)) {
                valueLayout.show(valueArea, "PRIORITY");
                priorityValue.setSelectedItem(p.getPriority());
            } else {
                valueLayout.show(valueArea, "TEXT");
                switch (sel) {
                    case "Name": textValue.setText(p.getName()); break;
                    case "Age": textValue.setText(String.valueOf(p.getAge())); break;
                    case "Disease": textValue.setText(p.getDisease()); break;
                    case "Doctor": textValue.setText(p.getDoctor()); break;
                    case "Room Number": textValue.setText(p.getRoom()); break;
                    case "Contact": textValue.setText(p.getContact()); break;
                    default: textValue.setText("");
                }
            }
        };
        fieldBox.addActionListener(e -> syncSelection.run());
        syncSelection.run();

        JLabel error = new JLabel(" ");
        error.setForeground(UITheme.DANGER);
        error.setFont(UITheme.FONT_LABEL);

        JButton cancelBtn = new JButton("Cancel");
        UITheme.styleSecondaryButton(cancelBtn);
        cancelBtn.setPreferredSize(new Dimension(100, 40));
        
        JButton saveBtn = new JButton("Save Changes");
        UITheme.stylePrimaryButton(saveBtn);
        saveBtn.setPreferredSize(new Dimension(130, 40));

        final boolean[] result = {false};
        cancelBtn.addActionListener(e -> {
            result[0] = false;
            dialog.dispose();
        });
        
        saveBtn.addActionListener(e -> {
            String sel = (String) fieldBox.getSelectedItem();
            try {
                switch (sel) {
                    case "Name":
                        if (textValue.getText().trim().isEmpty()) { 
                            error.setText("Value cannot be empty."); 
                            return; 
                        }
                        p.setName(textValue.getText().trim());
                        break;
                    case "Age":
                        String ageText = textValue.getText().trim();
                        if (ageText.isEmpty()) { 
                            error.setText("Please enter age."); 
                            return; 
                        }
                        int age = Integer.parseInt(ageText);
                        if (age <= 0 || age > 150) { 
                            error.setText("Please enter a valid age (1-150)."); 
                            return; 
                        }
                        p.setAge(age);
                        break;
                    case "Gender":
                        p.setGender((String) genderValue.getSelectedItem());
                        break;
                    case "Disease":
                        if (textValue.getText().trim().isEmpty()) { 
                            error.setText("Value cannot be empty."); 
                            return; 
                        }
                        p.setDisease(textValue.getText().trim());
                        break;
                    case "Doctor":
                        if (textValue.getText().trim().isEmpty()) { 
                            error.setText("Value cannot be empty."); 
                            return; 
                        }
                        p.setDoctor(textValue.getText().trim());
                        break;
                    case "Room Number":
                        if (textValue.getText().trim().isEmpty()) { 
                            error.setText("Value cannot be empty."); 
                            return; 
                        }
                        p.setRoom(textValue.getText().trim());
                        break;
                    case "Contact":
                        if (textValue.getText().trim().isEmpty()) { 
                            error.setText("Value cannot be empty."); 
                            return; 
                        }
                        p.setContact(textValue.getText().trim());
                        break;
                    case "Emergency Priority":
                        p.setPriority((String) priorityValue.getSelectedItem());
                        break;
                }
            } catch (NumberFormatException ex) {
                error.setText("Age must be a number.");
                return;
            }
            service.saveToFile();
            result[0] = true;
            dialog.dispose();
        });

        JPanel fieldPanel = new JPanel(new BorderLayout(0, 6));
        fieldPanel.setBackground(UITheme.CARD);
        fieldPanel.add(UITheme.sectionLabel("Select Field to Update"), BorderLayout.NORTH);
        fieldPanel.add(fieldBox, BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(UITheme.CARD);
        center.add(fieldPanel, BorderLayout.NORTH);
        center.add(valueArea, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(UITheme.CARD);
        south.add(error, BorderLayout.NORTH);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(UITheme.CARD);
        btnRow.add(cancelBtn);
        btnRow.add(saveBtn);
        south.add(btnRow, BorderLayout.SOUTH);

        root.add(heading, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(south, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
        return result[0];
    }

    private JPanel wrap(JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UITheme.CARD);
        p.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        p.add(c, BorderLayout.NORTH);
        return p;
    }

    public boolean isSaved() {
        return saved;
    }
}