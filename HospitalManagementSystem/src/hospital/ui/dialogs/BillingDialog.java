package hospital.ui.dialogs;

import hospital.model.Patient;
import hospital.service.HospitalService;
import hospital.ui.UITheme;
import hospital.ui.VectorIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * BillingDialog.java
 * ---------------------------------------------------------
 * GUI counterpart of console billing(): looks up a patient, collects
 * medicine and room charges, and computes the total using the exact
 * same formula as the console version (roomCharge + fixed doctor fee of
 * 1500 + medicine).
 *
 * The result is rendered as a proper two-column "receipt" (label left,
 * amount right, each row a real component) instead of a single block of
 * space-padded text -- space-padding only lines up in a monospace font,
 * and this app's UI font is proportional, so that approach produced
 * misaligned numbers. A real layout guarantees clean alignment always.
 * ---------------------------------------------------------
 */
public class BillingDialog extends JDialog {

    private final JPanel resultCard;
    private final CardLayout resultLayout;
    private static final String EMPTY = "EMPTY";
    private static final String FILLED = "FILLED";

    public BillingDialog(Window owner, HospitalService service, String presetId) {
        super(owner, "Patient Billing", ModalityType.APPLICATION_MODAL);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(UITheme.CARD);
        root.setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));

        JPanel heading = UITheme.headingWithIcon("Generate Bill", VectorIcon.Type.WALLET, UITheme.PRIMARY);

        JTextField idField = new JTextField(presetId == null ? "" : presetId);
        JTextField medicineField = new JTextField();
        JTextField roomField = new JTextField();
        UITheme.styleTextField(idField);
        UITheme.styleTextField(medicineField);
        UITheme.styleTextField(roomField);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(UITheme.CARD);
        form.add(labeledField("Patient ID", idField));
        form.add(labeledField("Medicine Charges (Rs.)", medicineField));
        form.add(labeledField("Room Charges (Rs.)", roomField));

        // ---- Result area: a real "receipt" card built from components, not text ----
        resultLayout = new CardLayout();
        resultCard = new JPanel(resultLayout);
        resultCard.setOpaque(false);
        resultCard.add(new JPanel(), EMPTY);
        resultCard.setPreferredSize(new Dimension(380, 210));

        JLabel status = new JLabel(" ");
        status.setFont(UITheme.FONT_LABEL);
        status.setForeground(UITheme.DANGER);

        JButton closeBtn = new JButton("Close");
        UITheme.styleSecondaryButton(closeBtn);
        JButton calcBtn = new JButton("Calculate Bill");
        UITheme.stylePrimaryButton(calcBtn);
        closeBtn.addActionListener(e -> dispose());
        calcBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            Patient p = service.findById(id);
            if (p == null) {
                status.setText("Patient not found.");
                resultLayout.show(resultCard, EMPTY);
                return;
            }
            int medicine, roomCharge;
            try {
                medicine = Integer.parseInt(medicineField.getText().trim());
                roomCharge = Integer.parseInt(roomField.getText().trim());
                if (medicine < 0 || roomCharge < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                status.setText("Charges must be valid non-negative numbers.");
                resultLayout.show(resultCard, EMPTY);
                return;
            }
            status.setText(" ");
            HospitalService.BillResult bill = service.calculateBill(id, medicine, roomCharge);
            resultCard.add(buildReceipt(p, bill), FILLED);
            resultLayout.show(resultCard, FILLED);
            pack();
            setLocationRelativeTo(owner);
        });

        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(UITheme.CARD);
        south.add(status, BorderLayout.NORTH);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(UITheme.CARD);
        btnRow.add(closeBtn);
        btnRow.add(calcBtn);
        south.add(btnRow, BorderLayout.SOUTH);

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(UITheme.CARD);
        center.add(form, BorderLayout.NORTH);
        center.add(resultCard, BorderLayout.CENTER);

        root.add(heading, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(south, BorderLayout.SOUTH);
        setContentPane(root);

        setMinimumSize(new Dimension(460, 520));
        pack();
    }

    /** Builds a clean, properly aligned itemized receipt panel for the computed bill. */
    private JPanel buildReceipt(Patient p, HospitalService.BillResult bill) {
        JPanel receipt = new JPanel();
        receipt.setLayout(new BoxLayout(receipt, BoxLayout.Y_AXIS));
        receipt.setBackground(UITheme.BACKGROUND);
        receipt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(16, 18, 16, 18)));

        JLabel patientLine = new JLabel(p.getName() + "  \u00B7  ID " + p.getId());
        patientLine.setFont(UITheme.FONT_LABEL_BOLD);
        patientLine.setForeground(UITheme.TEXT_DARK);
        patientLine.setAlignmentX(Component.LEFT_ALIGNMENT);

        receipt.add(patientLine);
        receipt.add(Box.createVerticalStrut(12));
        receipt.add(receiptRow("Room Charges", bill.roomCharge, false));
        receipt.add(Box.createVerticalStrut(8));
        receipt.add(receiptRow("Doctor Fee", bill.doctorFee, false));
        receipt.add(Box.createVerticalStrut(8));
        receipt.add(receiptRow("Medicine Charges", bill.medicine, false));
        receipt.add(Box.createVerticalStrut(12));
        receipt.add(new JSeparator());
        receipt.add(Box.createVerticalStrut(12));
        receipt.add(receiptRow("TOTAL", bill.total, true));

        return receipt;
    }

    private JPanel receiptRow(String label, int amount, boolean emphasize) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = new JLabel(label);
        l.setFont(emphasize ? UITheme.FONT_HEADING : UITheme.FONT_LABEL);
        l.setForeground(emphasize ? UITheme.TEXT_DARK : UITheme.TEXT_MUTED);

        JLabel v = new JLabel(String.format("Rs. %,d", amount));
        v.setFont(emphasize ? UITheme.FONT_HEADING : UITheme.FONT_LABEL_BOLD);
        v.setForeground(emphasize ? UITheme.PRIMARY_DARK : UITheme.TEXT_DARK);
        v.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        return row;
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
