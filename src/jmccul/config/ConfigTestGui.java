package jmccul.config;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import jmccul.DaqDevice;

/**
 *
 * @author Peter Froud
 */
public final class ConfigTestGui extends javax.swing.JFrame {

    private DaqDevice seletedDevice;

    /**
     * Creates new form ConfigTestGui
     */
    public ConfigTestGui() {
        initComponents();
        setSize(500, 850);

        // TODO look for daq devices and show them in a dropdown or something
        for (Class theClass : new Class[]{BoardConfig.class}) {
            createTabForClass(theClass);
        }
    }

    private class GetterAndSetter {

        Method getter, setter;
    }

    private void createTabForClass(Class theClass) {
        final JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 5, 2, 5);
        contentPanel.add(new JLabel("<html><b>Item"), gbc);
        gbc.gridx = 1;
        contentPanel.add(new JLabel("<html><b>Get"), gbc);
        gbc.gridx = 2;
        contentPanel.add(new JLabel("<html><b>Set"), gbc);

        final Method[] methods = theClass.getDeclaredMethods();

        final Map<String, GetterAndSetter> methodMap = new HashMap<>();

        for (Method method : methods) {
            final String methodName = method.getName();
            if (!(methodName.startsWith("get") || methodName.startsWith("set"))) {
                System.out.println("method name does not start with get or set: " + methodName);
                continue;
            }

            final String methodNameWithoutPrefix = methodName.substring(3);
            if (methodMap.containsKey(methodNameWithoutPrefix)) {
                final GetterAndSetter gas = methodMap.get(methodNameWithoutPrefix);
                if (methodName.startsWith("get")) {
                    if (gas.getter == null) {
                        gas.getter = method;
                    } else {
                        System.out.println("getter is already set for " + methodName);
                    }
                } else {
                    if (gas.setter == null) {
                        gas.setter = method;
                    } else {
                        System.out.println("setter is already set for " + methodName);
                    }
                }
            } else {
                final GetterAndSetter gas = new GetterAndSetter();
                if (methodName.startsWith("get")) {
                    gas.getter = method;
                } else {
                    gas.setter = method;
                }
                methodMap.put(methodNameWithoutPrefix, gas);
            }
        }

        final String[] methodNamesWithoutPrefixesSorted = methodMap.keySet().stream().sorted().toArray(String[]::new);

        for (int i = 0; i < methodNamesWithoutPrefixesSorted.length; i++) {
            final String methodNameWithoutPrefix = methodNamesWithoutPrefixesSorted[i];

            if (!methodMap.containsKey(methodNameWithoutPrefix)) {
                System.out.println("map does not contain key " + methodNameWithoutPrefix);
                continue;
            }
            final GetterAndSetter gas = methodMap.get(methodNameWithoutPrefix);

            if (gas.getter == null && gas.setter == null) {
                System.out.println("getter and setter are both null??" + methodNameWithoutPrefix);
                continue;
            }

            gbc.gridy = i + 1;//the header is at y=0
            gbc.gridx = 0;
            contentPanel.add(new JLabel(methodNameWithoutPrefix), gbc);

            gbc.gridx = 1;
            if (gas.getter == null) {
                contentPanel.add(new JLabel("Write-only(??)"), gbc);
            } else {

                final JLabel resultLabel = new JLabel("-");

                final JButton getButton = new JButton("Get");
                getButton.addActionListener((e) -> {
                    try {
                        Object rv = gas.getter.invoke(seletedDevice);
                        resultLabel.setText(rv.toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                JPanel flowLayoutPanel = new JPanel();
                flowLayoutPanel.add(getButton);
                flowLayoutPanel.add(resultLabel);
                contentPanel.add(flowLayoutPanel, gbc);
            }

            gbc.gridx = 2;
            if (gas.setter == null) {
                contentPanel.add(new JLabel("Read-only"), gbc);
            } else {

                final Parameter[] params = gas.setter.getParameters();
                switch (params.length) {
                    case 0:
                        final JLabel jLabel = new JLabel("no params!!!!!!!!!!!!!");
                        jLabel.setForeground(Color.red);
                        contentPanel.add(jLabel, gbc);
                        break;
                    case 1: {
                        // devNum is ignored
                        JPanel flowLayoutPanel = new JPanel();
                        final Parameter theParam = params[0];
                        final Class<?> paramType = theParam.getType();
                        if (paramType.isEnum()) {
                            try {
                                final Object invoke = paramType.getMethod("values").invoke(null);
                                final Object castToArrayType = paramType.arrayType().cast(invoke);
                                final Object[] objArray = (Object[]) castToArrayType;
                                final JComboBox comboBox = new JComboBox(objArray);
                                comboBox.setPreferredSize(new Dimension(100, 20));
                                flowLayoutPanel.add(comboBox);
                            } catch (ReflectiveOperationException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            // TODO check for boolean, int, string
                            JTextField textField = new JTextField(5);
                            flowLayoutPanel.add(textField);
                        }

                        JButton button = new JButton("Set");
                        flowLayoutPanel.add(button);
                        contentPanel.add(flowLayoutPanel, gbc);
                        break;
                    }

                    case 2: {
                        // devNum is used
                        JTextField textFieldDevNum = new JTextField(2);
                        JTextField textFieldValueToWrite = new JTextField(2);
                        JButton button = new JButton("Set");

                        JPanel flowLayoutPanel = new JPanel();
                        flowLayoutPanel.add(textFieldDevNum);
                        flowLayoutPanel.add(textFieldValueToWrite);
                        flowLayoutPanel.add(button);
                        contentPanel.add(flowLayoutPanel, gbc);
                        break;
                    }
                    default:
                        contentPanel.add(new JLabel("param count is " + params.length), gbc);
                        break;
                }

            }

        }
        final JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(50);
        jTabbedPane1.add(scrollPane, theClass.getSimpleName());

    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Config items tester");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));
        getContentPane().add(jTabbedPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConfigTestGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

}
