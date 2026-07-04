package hospital.ui.dialogs;

import hospital.model.Patient;
import hospital.service.HospitalService;
import hospital.ui.UITheme;
import hospital.ui.VectorIcon;

import javax.swing.*;
import java.awt.*;

/**
 * AddPatientDialog.java
 * ---------------------------------------------------------
 * GUI counterpart of console addPatient(). Collects exactly the same
 * fields the console version asked for (ID, Name, Age, Gender, Disease,
 * Doctor, Admission Date, Contact, Room Number, Emergency Priority) and
 * adds the resulting Patient through HospitalService, then persists it
 * -- identical to the console's "addPatient(); saveToFile();" sequence.
 * ---------------------------------------------------------
 */
public class AddPatientDialog extends JDialog {

    private boolean saved = false;

    public AddPatientDialog(Window owner, HospitalService service) {
        super(owner, "Add Patient", ModalityType.APPLICATION_MODAL);
        setSize(480, 660);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.CARD);
        root.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JPanel heading = UITheme.headingWithIcon("New Patient Record", VectorIcon.Type.ADD, UITheme.PRIMARY);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(UITheme.CARD);

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField diseaseField = new JTextField();
        JTextField doctorField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField roomField = new JTextField();

        JRadioButton normalPriority = new JRadioButton("Normal", true);
        JRadioButton highPriority = new JRadioButton("High");
        ButtonGroup priorityGroup = new ButtonGroup();
        priorityGroup.add(normalPriority);
        priorityGroup.add(highPriority);
        normalPriority.setBackground(UITheme.CARD);
        highPriority.setBackground(UITheme.CARD);
        highPriority.setForeground(UITheme.DANGER);

        form.add(field("Patient ID", idField));
        form.add(field("Full Name", nameField));
        form.add(field("Age", ageField));
        form.add(fieldCombo("Gender", genderBox));
        form.add(field("Disease / Diagnosis", diseaseField));
        form.add(field("Attending Doctor", doctorField));
        form.add(field("Admission Date (e.g. 2026-07-03)", dateField));
        form.add(field("Contact Number", contactField));
        form.add(field("Room Number", roomField));

        JPanel priorityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        priorityPanel.setBackground(UITheme.CARD);
        priorityPanel.add(normalPriority);
        priorityPanel.add(highPriority);
        form.add(labelOnly("Emergency Priority"));
        form.add(priorityPanel);

        JLabel error = new JLabel(" ");
        error.setForeground(UITheme.DANGER);
        error.setFont(UITheme.FONT_LABEL);
        error.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton cancelBtn = new JButton("Cancel");
        UITheme.styleSecondaryButton(cancelBtn);
        JButton saveBtn = new JButton("Add Patient");
        UITheme.stylePrimaryButton(saveBtn);

        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            String disease = diseaseField.getText().trim();
            String doctor = doctorField.getText().trim();
            String date = dateField.getText().trim();
            String contact = contactField.getText().trim();
            String room = roomField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || ageText.isEmpty() || disease.isEmpty()
                    || doctor.isEmpty() || date.isEmpty() || contact.isEmpty() || room.isEmpty()) {
                error.setText("Please fill in all fields.");
                return;
            }
            if (service.findById(id) != null) {
                error.setText("A patient with this ID already exists.");
                return;
            }
            int age;
            try {
                age = Integer.parseInt(ageText);
                if (age <= 0 || age > 150) {
                    error.setText("Please enter a valid age.");
                    return;
                }
            } catch (NumberFormatException ex) {
                error.setText("Age must be a number.");
                return;
            }

            String priority = highPriority.isSelected() ? "High" : "Normal";
            Patient p = new Patient(id, name, age, (String) genderBox.getSelectedItem(),
                    disease, doctor, date, room, contact, priority);
            service.addPatient(p);
            service.saveToFile();
            saved = true;
            dispose();
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setBackground(UITheme.CARD);
        buttons.add(cancelBtn);
        buttons.add(saveBtn);

        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(UITheme.CARD);
        south.add(error, BorderLayout.NORTH);
        south.add(buttons, BorderLayout.SOUTH);
        south.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        root.add(heading, BorderLayout.NORTH);
        root.add(new JScrollPane(form), BorderLayout.CENTER);
        root.add(south, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel field(String label, JTextField tf) {
        UITheme.styleTextField(tf);
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UITheme.CARD);
        p.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        JLabel l = UITheme.sectionLabel(label);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        p.add(l, BorderLayout.NORTH);
        p.add(tf, BorderLayout.CENTER);
        return p;
    }

    private JPanel fieldCombo(String label, JComboBox<String> box) {
        UITheme.styleComboBox(box);
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UITheme.CARD);
        p.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        JLabel l = UITheme.sectionLabel(label);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        p.add(l, BorderLayout.NORTH);
        p.add(box, BorderLayout.CENTER);
        return p;
    }

    private JLabel labelOnly(String text) {
        JLabel l = UITheme.sectionLabel(text);
        l.setBorder(BorderFactory.createEmptyBorder(6, 0, 4, 0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    public boolean isSaved() {
        return saved;
    }
}
