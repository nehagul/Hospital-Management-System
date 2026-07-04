package hospital.ui.dialogs;

import hospital.service.HospitalService;
import hospital.ui.UITheme;
import hospital.ui.VectorIcon;

import javax.swing.*;
import java.awt.*;

/**
 * ScheduleAppointmentDialog.java
 * ---------------------------------------------------------
 * GUI counterpart of console scheduleAppointment(): looks up the
 * patient by ID, then records appointment date/time/doctor, matching
 * the console's "appointment scheduled!" behavior exactly.
 * ---------------------------------------------------------
 */
public class ScheduleAppointmentDialog extends JDialog {

    public ScheduleAppointmentDialog(Window owner, HospitalService service, String presetId) {
        super(owner, "Schedule Appointment", ModalityType.APPLICATION_MODAL);
        setSize(440, 440);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(UITheme.CARD);
        root.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JPanel heading = UITheme.headingWithIcon("Schedule Appointment", VectorIcon.Type.CALENDAR, UITheme.PRIMARY);

        JTextField idField = new JTextField(presetId == null ? "" : presetId);
        JTextField dateField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField doctorField = new JTextField();
        UITheme.styleTextField(idField);
        UITheme.styleTextField(dateField);
        UITheme.styleTextField(timeField);
        UITheme.styleTextField(doctorField);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(UITheme.CARD);
        form.add(labeledField("Patient ID", idField));
        form.add(labeledField("Appointment Date (e.g. 2026-07-10)", dateField));
        form.add(labeledField("Appointment Time (e.g. 10:30 AM)", timeField));
        form.add(labeledField("Doctor", doctorField));

        JLabel status = new JLabel(" ");
        status.setFont(UITheme.FONT_LABEL);
        status.setForeground(UITheme.DANGER);

        JButton cancelBtn = new JButton("Cancel");
        UITheme.styleSecondaryButton(cancelBtn);
        JButton saveBtn = new JButton("Schedule");
        UITheme.stylePrimaryButton(saveBtn);
        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String date = dateField.getText().trim();
            String time = timeField.getText().trim();
            String doctor = doctorField.getText().trim();
            if (id.isEmpty() || date.isEmpty() || time.isEmpty() || doctor.isEmpty()) {
                status.setText("Please fill in all fields.");
                return;
            }
            boolean ok = service.scheduleAppointment(id, date, time, doctor);
            if (!ok) {
                status.setText("Patient not found.");
                return;
            }
            service.saveToFile();
            dispose();
            JOptionPane.showMessageDialog(owner, "Appointment scheduled successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(UITheme.CARD);
        south.add(status, BorderLayout.NORTH);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(UITheme.CARD);
        btnRow.add(cancelBtn);
        btnRow.add(saveBtn);
        south.add(btnRow, BorderLayout.SOUTH);

        root.add(heading, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(south, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JPanel labeledField(String label, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(UITheme.CARD);
        p.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        p.add(UITheme.sectionLabel(label), BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }
}
