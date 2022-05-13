package jmccul.config;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import jmccul.DaqDevice;
import jmccul.DeviceDiscovery;
import jmccul.JMCCULException;
import jmccul.jna.DaqDeviceDescriptor;

/**
 *
 * @author Peter Froud
 */
public final class ConfigTestGui extends javax.swing.JFrame {

    private DaqDevice selectedDevice;

    public ConfigTestGui() {
        initComponents();
        setSize(800, 600);

        try {
            final DaqDeviceDescriptor[] descriptors = DeviceDiscovery.findDaqDeviceDescriptors();

            if (descriptors.length > 0) {
                System.out.println("Discovered device count: " + descriptors.length);
                jComboBoxDaqDevice.setModel(new DefaultComboBoxModel<>(descriptors));

                DaqDeviceDescriptor selected = jComboBoxDaqDevice.getModel().getElementAt(jComboBoxDaqDevice.getSelectedIndex());
                openDescriptor(selected);

            } else {
                System.out.println("No daq devices discovered");
                jComboBoxDaqDevice.setEnabled(false);
            }

        } catch (JMCCULException ex) {
            ex.printStackTrace();
            System.exit(1);

        }

        final Class[] configClasses = new Class[]{
            AnalogInputConfig.class, AnalogOutputConfig.class,
            BoardConfig.class, CounterConfig.class,
            DigitalInputConfig.class, DigitalOutputConfig.class,
            ExpansionConfig.class, NetworkConfig.class,
            TemperatureConfig.class, WirelessConfig.class};
        for (Class theClass : configClasses) {
            createTabForClass(theClass);
        }
    }

    private void openDescriptor(DaqDeviceDescriptor descriptor) throws JMCCULException {
        if (selectedDevice != null) {
            selectedDevice.close();
        }
        System.out.println("opening descriptor " + descriptor);
        selectedDevice = new DaqDevice(descriptor);
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
        gbc.insets = new Insets(2, 20, 2, 20); //top, left, bottom, right
        contentPanel.add(new JLabel("<html><b>Item"), gbc);
        gbc.gridx = 1;
        contentPanel.add(new JLabel("<html><b>Get"), gbc);
        gbc.gridx = 2;
        contentPanel.add(new JLabel("<html><b>Set"), gbc);

        final Map<String, GetterAndSetter> methodMap = getMethodMap(theClass);

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

            contentPanel.add(getComponentForGetter(theClass, methodNameWithoutPrefix, gas), gbc);

            gbc.gridx = 2;
            contentPanel.add(getComponentForSetter(theClass, methodNameWithoutPrefix, gas), gbc);

        }
        final JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(50);
        jTabbedPane1.add(scrollPane, theClass.getSimpleName());

    }

    private Component getComponentForGetter(Class theClass, String methodNameWithoutPrefix, GetterAndSetter gas) {
        if (gas.getter == null) {
            return new JLabel("Write-only (getter is null)");
        } else {
            final Parameter[] params = gas.getter.getParameters();

            if (params.length != 0 && params.length != 1) {
                System.out.println(theClass.getSimpleName() + ": " + methodNameWithoutPrefix + ": param count of getter is " + params.length);
                JLabel jlabel = new JLabel("param count is " + params.length);
                jlabel.setForeground(Color.red);
                return jlabel;
            }

            final boolean isDevNumUsed = params.length == 1;
            final JPanel flowLayoutPanel = new JPanel();

            Component inputComponentForDevNum;
            if (isDevNumUsed) {
                final Parameter paramForDevNum = params[0];
                flowLayoutPanel.add(new JLabel(paramForDevNum.getName() + " (" + paramForDevNum.getType().getSimpleName() + "): "));
                try {
                    inputComponentForDevNum = getInputComponentForParameter(paramForDevNum);
                    flowLayoutPanel.add(inputComponentForDevNum);
                } catch (ReflectiveOperationException ex) {
                    final JLabel jLabel = new JLabel(ex.getClass().getSimpleName());
                    jLabel.setForeground(Color.red);
                    flowLayoutPanel.add(jLabel);
                    inputComponentForDevNum = null;
                }
            } else {
                inputComponentForDevNum = null;
            }

            final JLabel resultLabel = new JLabel();

            final JButton getButton = new JButton("Get");
            final Component inputComponentForDevNum_ = inputComponentForDevNum;//local variable referenced from a lambda expression must be final
            getButton.addActionListener(e -> {
                if (selectedDevice == null) {
                    return;
                }
                try {
                    Object invoke;
                    Object configInstance = getConfigInstance(theClass);

                    if (isDevNumUsed && inputComponentForDevNum_ != null) {
                        final Object devNumInputValue = getInput(inputComponentForDevNum_, params[0].getType());
                        invoke = gas.getter.invoke(configInstance, devNumInputValue);
                    } else {
                        invoke = gas.getter.invoke(configInstance);
                    }
                    if (invoke == null) {
                        resultLabel.setText("null");
                    } else {
                        resultLabel.setText(invoke.toString());
                    }
                    resultLabel.setForeground(Color.black);
                } catch (Exception ex) {
                    if (ex instanceof InvocationTargetException && ex.getCause() instanceof JMCCULException) {
                        // usually means the board does not support it
                        resultLabel.setForeground(Color.black);
                        resultLabel.setText(ex.getCause().getMessage());
                    } else {
                        System.err.println("Exception getting " + methodNameWithoutPrefix + " ( " + theClass.getName() + "): " + ex.toString());
                        resultLabel.setForeground(Color.red);
                        resultLabel.setText(ex.getClass().getName());
                    }
                }
            });
            flowLayoutPanel.add(getButton);

            flowLayoutPanel.add(resultLabel);

            return flowLayoutPanel;
        }
    }

    private Object getConfigInstance(Class configClass) {
        if (configClass == AnalogInputConfig.class) {
            return selectedDevice.analogInputConfig;
        } else if (configClass == AnalogOutputConfig.class) {
            return selectedDevice.analogOutputConfig;
        } else if (configClass == BoardConfig.class) {
            return selectedDevice.boardConfig;
        } else if (configClass == CounterConfig.class) {
            return selectedDevice.counterConfig;
        } else if (configClass == DigitalInputConfig.class) {
            return selectedDevice.digitalInputConfig;
        } else if (configClass == DigitalOutputConfig.class) {
            return selectedDevice.digitalOutputConfig;
        } else if (configClass == ExpansionConfig.class) {
            return selectedDevice.expansionConfig;
        } else if (configClass == NetworkConfig.class) {
            return selectedDevice.networkConfig;
        } else if (configClass == TemperatureConfig.class) {
            return selectedDevice.temperatureConfig;
        } else if (configClass == WirelessConfig.class) {
            return selectedDevice.wirelessConfig;
        } else {
            throw new IllegalArgumentException("don't know what to do when configClass is " + configClass.getName());
        }

    }

    private Component getComponentForSetter(Class theClass, String methodNameWithoutPrefix, GetterAndSetter gas) {
        if (gas.setter == null) {
            return new JLabel("Read-only (setter is null)");
        } else {
            final Parameter[] params = gas.setter.getParameters();

            if (params.length != 1 && params.length != 2) {
                System.out.println(theClass.getSimpleName() + ": " + methodNameWithoutPrefix + ": param count of setter is " + params.length);
                JLabel jlabel = new JLabel("param count is " + params.length);
                jlabel.setForeground(Color.red);
                return jlabel;
            }

            final boolean isDevNumUsed = params.length == 2;
            final JPanel flowLayoutPanel = new JPanel();

            Component inputComponentForDevNum;
            final Parameter paramForInput;
            if (isDevNumUsed) {
                final Parameter paramForDevNum = params[0];
                flowLayoutPanel.add(new JLabel(paramForDevNum.getName() + " (" + paramForDevNum.getType().getSimpleName() + "): "));

                try {
                    inputComponentForDevNum = getInputComponentForParameter(paramForDevNum);
                    flowLayoutPanel.add(inputComponentForDevNum);
                } catch (ReflectiveOperationException ex) {
                    final JLabel jLabel = new JLabel(ex.getClass().getSimpleName());
                    jLabel.setForeground(Color.red);
                    flowLayoutPanel.add(jLabel);
                    inputComponentForDevNum = null;
                }

                paramForInput = params[1];
            } else {
                // devNum is ignored
                inputComponentForDevNum = null;
                paramForInput = params[0];
            }

            Component inputComponentForNewValue;
            flowLayoutPanel.add(new JLabel(paramForInput.getName() + " (" + paramForInput.getType().getSimpleName() + "): "));
            try {
                inputComponentForNewValue = getInputComponentForParameter(paramForInput);
                flowLayoutPanel.add(inputComponentForNewValue);
            } catch (ReflectiveOperationException ex) {
                final JLabel jLabel = new JLabel(ex.getClass().getSimpleName());
                jLabel.setForeground(Color.red);
                flowLayoutPanel.add(jLabel);
                inputComponentForNewValue = null;
            }
            final JButton button = new JButton("Set");
            final JLabel status = new JLabel();
            final Component inputComponentForDevNum_ = inputComponentForDevNum;//local variable referenced from a lambda expression must be final
            final Component inputComponentForNewValue_ = inputComponentForNewValue;//local variable referenced from a lambda expression must be final
            button.addActionListener(e -> {

                if (selectedDevice == null) {
                    return;
                }
                if (inputComponentForNewValue_ == null) {
                    return;
                }
                try {
                    Object configInstance = getConfigInstance(theClass);

                    if (isDevNumUsed && inputComponentForDevNum_ != null) {
                        final Object devNumInputValue = getInput(inputComponentForDevNum_, params[0].getType());
                        final Object newValueInputValue = getInput(inputComponentForNewValue_, params[1].getType());
                        status.setText("OK - devNum " + devNumInputValue + ", new value " + newValueInputValue);
                        gas.setter.invoke(configInstance, devNumInputValue, newValueInputValue);
                    } else {
                        final Object devNumInputValue = getInput(inputComponentForNewValue_, params[0].getType());
                        status.setText("Set to " + devNumInputValue);
                        gas.setter.invoke(configInstance, devNumInputValue);
                    }
                    status.setForeground(Color.black);
                } catch (Exception ex) {
                    if (ex instanceof InvocationTargetException && ex.getCause() instanceof JMCCULException) {
                        // usually means the board does not support it
                        status.setForeground(Color.black);
                        status.setText(ex.getCause().getMessage());
                    } else {
                        System.err.println("Exception setting " + methodNameWithoutPrefix + " ( " + theClass.getName() + "):");
                        ex.printStackTrace();
                        System.err.println();
                        status.setForeground(Color.red);
                        status.setText(ex.getClass().getName());
                    }
                }

            });
            flowLayoutPanel.add(button);
            flowLayoutPanel.add(status);
            return flowLayoutPanel;
        }
    }

    private Component getInputComponentForParameter(Parameter param) throws ReflectiveOperationException {
        final Class<?> paramType = param.getType();
        if (paramType.isEnum()) {
            final Object invoke = paramType.getMethod("values").invoke(null);
            final Object[] objArray = (Object[]) invoke;

            Object[] filtered = Arrays.stream(objArray).filter(obj -> {
                final String enumName = ((Enum) obj).name();
                return !enumName.equals("NOT_USED") && !enumName.equals("NOT_SET");
            }).toArray();

            final JComboBox comboBox = new JComboBox(filtered);
            comboBox.setPreferredSize(new Dimension(100, 20));
            return comboBox;
        } else if (paramType == boolean.class) {
            return new JCheckBox();
        } else {
            return new JTextField("0", 5);
        }
    }

    private Object getInput(Component comp, Class desiredType) {
        if (comp instanceof JComboBox) {
            if (desiredType.isEnum()) {
                final JComboBox cast = (JComboBox) comp;
                return cast.getSelectedItem();
            } else {
                throw new IllegalArgumentException("the component is a JComboBox but the type is not an enum: " + desiredType.getClass());
            }
        } else if (comp instanceof JCheckBox) {
            if (desiredType == boolean.class) {
                final JCheckBox cast = (JCheckBox) comp;
                return cast.isSelected();
            } else {
                throw new IllegalArgumentException("the component is a JCheckBox but the type is not boolean: " + desiredType.getClass());
            }
        } else if (comp instanceof JTextField) {
            final JTextField cast = (JTextField) comp;
            if (desiredType == String.class) {
                return cast.getText();
            } else if (desiredType == int.class) {
                return Integer.parseInt(cast.getText());
            } else {
                throw new IllegalArgumentException("the component is a JTextField but the type is neither String nor int: " + desiredType.getClass());
            }
        } else {
            throw new IllegalArgumentException("don't know what to do when the component is a " + comp.getClass().getSimpleName());
        }
    }

    private Map<String, GetterAndSetter> getMethodMap(Class theClass) throws SecurityException {
        final Method[] methods = theClass.getDeclaredMethods();

        // the map key is the name of the methods without the get/set prefix
        final Map<String, GetterAndSetter> methodMap = new HashMap<>();
        for (Method method : methods) {
            final String methodName = method.getName();
            if (!(methodName.startsWith("get") || methodName.startsWith("set"))) {
                System.out.println(theClass.getSimpleName() + ": method name does not start with get or set: " + methodName);
                continue;
            }

            final String methodNameWithoutPrefix = methodName.substring(3);
            if (methodMap.containsKey(methodNameWithoutPrefix)) {
                // add to the existing GetterAndSetter object
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
                // create a new GetterAndSetter object
                final GetterAndSetter gas = new GetterAndSetter();
                if (methodName.startsWith("get")) {
                    gas.getter = method;
                } else {
                    gas.setter = method;
                }
                methodMap.put(methodNameWithoutPrefix, gas);
            }
        }
        return methodMap;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxDaqDevice = new javax.swing.JComboBox<>();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Config items tester");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.BorderLayout(10, 10));

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("DAQ device:");
        jPanel1.add(jLabel1);

        jPanel1.add(jComboBoxDaqDevice);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (selectedDevice != null) {
            try {
                selectedDevice.close();
            } catch (JMCCULException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_formWindowClosing

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
    private javax.swing.JComboBox<jmccul.jna.DaqDeviceDescriptor> jComboBoxDaqDevice;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

}
