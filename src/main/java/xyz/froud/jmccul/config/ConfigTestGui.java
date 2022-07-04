package xyz.froud.jmccul.config;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.jna.DaqDeviceDescriptor;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 */
public final class ConfigTestGui extends javax.swing.JFrame {

    private DaqDevice selectedDevice;

    public ConfigTestGui() {
        initComponents();
        setSize(800, 600);

        try {
            final DaqDeviceDescriptor[] descriptors = DeviceDiscovery.findDescriptors();

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

        final Class<?>[] configClasses = new Class[]{
                AnalogInputConfig.class, AnalogOutputConfig.class,
                BoardConfig.class, CounterConfig.class,
                DigitalInputConfig.class, DigitalOutputConfig.class,
                ExpansionConfig.class, NetworkConfig.class,
                TemperatureConfig.class, WirelessConfig.class};
        for (Class<?> theClass : configClasses) {
            createTabForConfigClass(theClass);
        }
    }

    private void openDescriptor(DaqDeviceDescriptor descriptor) throws JMCCULException {
        if (selectedDevice != null) {
            selectedDevice.close();
        }
        System.out.println("opening descriptor " + descriptor);
        selectedDevice = new DaqDevice(descriptor);
    }

    private static class GetterAndSetter {

        Method getter, setter;
    }

    private void createTabForConfigClass(Class<?> configClass) {
        final JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 20, 2, 20); //top, left, bottom, right
        contentPanel.add(new JLabel("<html><b>Item"), gbc);
        gbc.gridx = 1;
        contentPanel.add(new JLabel("<html><b>Get"), gbc);
        gbc.gridx = 2;
        contentPanel.add(new JLabel("<html><b>Set"), gbc);

        // The map key is the method name without "get" or "set" prefix.
        final Map<String, GetterAndSetter> methodMap = getMethodMap(configClass);

        final String[] methodNamesWithoutPrefixesSorted = methodMap.keySet().stream().sorted().toArray(String[]::new);

        for (int i = 0; i < methodNamesWithoutPrefixesSorted.length; i++) {
            final String methodNameWithoutPrefix = methodNamesWithoutPrefixesSorted[i];

            final GetterAndSetter gas = methodMap.get(methodNameWithoutPrefix);

            if (gas.getter == null && gas.setter == null) {
                System.out.println("getter and setter are both null?? " + methodNameWithoutPrefix);
                continue;
            }

            gbc.gridy = i + 1;//the header is at y=0
            gbc.gridx = 0;
            contentPanel.add(new JLabel(methodNameWithoutPrefix), gbc);

            gbc.gridx = 1;
            contentPanel.add(getComponentForGetter(configClass, methodNameWithoutPrefix, gas.getter), gbc);

            gbc.gridx = 2;
            contentPanel.add(getComponentForSetter(configClass, methodNameWithoutPrefix, gas.setter), gbc);

        }
        final JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(50);
        jTabbedPane1.add(scrollPane, configClass.getSimpleName());

    }

    private Component getComponentForGetter(Class<?> configClass, String methodNameWithoutPrefix, Method getterMethod) {
        if (getterMethod == null) {
            return new JLabel("Write-only (getter is null)");
        }

        final Parameter[] getterParams = getterMethod.getParameters();

        /*
        There are two possibilities for getters:
        If devNum is used, the getter method has one parameter (the devNum).
        If devNum is ignored, the getter method has no parameters.
         */
        if (getterParams.length != 0 && getterParams.length != 1) {
            System.out.println(configClass.getSimpleName() + ": " + methodNameWithoutPrefix + ": param count of getter is " + getterParams.length);
            JLabel jlabel = new JLabel("param count is " + getterParams.length);
            jlabel.setForeground(Color.red);
            return jlabel;
        }

        final boolean isDevNumUsed = getterParams.length == 1;
        final JPanel flowLayoutPanel = new JPanel();

        Component inputComponentForDevNum;
        if (isDevNumUsed) {
            final Parameter paramForDevNum = getterParams[0];
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
                final Object invoked;
                final Object configInstance = getConfigInstanceFromSelectedDevice(configClass);

                if (isDevNumUsed && inputComponentForDevNum_ != null) {
                    final Parameter paramForDevNum = getterParams[0];
                    final Object devNumInputValue = getUserInputFromComponent(inputComponentForDevNum_, paramForDevNum.getType());
                    invoked = getterMethod.invoke(configInstance, devNumInputValue);
                } else {
                    invoked = getterMethod.invoke(configInstance);
                }

                resultLabel.setForeground(Color.black);
                if (invoked == null) {
                    resultLabel.setText("null");
                } else {
                    resultLabel.setText(invoked.toString());
                }
            } catch (Exception ex) {
                if (ex instanceof InvocationTargetException && ex.getCause() instanceof JMCCULException) {
                    // usually means the DAQ device does not support this config item
                    resultLabel.setForeground(Color.black);
                    resultLabel.setText(ex.getCause().getMessage());
                } else {
                    System.err.println("Exception getting " + methodNameWithoutPrefix + " ( " + configClass.getName() + "): " + ex);
                    resultLabel.setForeground(Color.red);
                    resultLabel.setText(ex.getClass().getName());
                }
            }
        });
        flowLayoutPanel.add(getButton);
        flowLayoutPanel.add(resultLabel);
        return flowLayoutPanel;

    }

    private Object getConfigInstanceFromSelectedDevice(Class<?> configClass) {
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

    private Component getComponentForSetter(Class<?> configClass, String methodNameWithoutPrefix, Method setterMethod) {
        if (setterMethod == null) {
            return new JLabel("Read-only (setter is null)");
        }
        final Parameter[] setterParams = setterMethod.getParameters();

        /*
        There are two possibilities for getters:
        If devNum is used, the setter method has two parameter (the devNum and the value to write).
        If devNum is ignored, the setter method has one parameter (the value to write).
         */
        if (setterParams.length != 1 && setterParams.length != 2) {
            System.out.println(configClass.getSimpleName() + ": " + methodNameWithoutPrefix + ": param count of setter is " + setterParams.length);
            JLabel jlabel = new JLabel("param count is " + setterParams.length);
            jlabel.setForeground(Color.red);
            return jlabel;
        }

        final boolean isDevNumUsed = setterParams.length == 2;
        final JPanel flowLayoutPanel = new JPanel();

        Component inputComponentForDevNum;
        final Parameter paramForInput;
        if (isDevNumUsed) {
            final Parameter paramForDevNum = setterParams[0];
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

            paramForInput = setterParams[1];
        } else {
            // devNum is ignored
            inputComponentForDevNum = null;
            paramForInput = setterParams[0];
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
            if (selectedDevice == null || inputComponentForNewValue_ == null) {
                return;
            }

            try {
                final Object configInstance = getConfigInstanceFromSelectedDevice(configClass);

                if (isDevNumUsed && inputComponentForDevNum_ != null) {
                    final Parameter paramForDevNum = setterParams[0];
                    final Parameter paramForNewValue = setterParams[1];
                    final Object devNumInputValue = getUserInputFromComponent(inputComponentForDevNum_, paramForDevNum.getType());
                    final Object newValueInputValue = getUserInputFromComponent(inputComponentForNewValue_, paramForNewValue.getType());
                    status.setText("OK - devNum " + devNumInputValue + ", new value " + newValueInputValue);
                    setterMethod.invoke(configInstance, devNumInputValue, newValueInputValue);
                } else {
                    final Parameter paramForNewValue = setterParams[0];
                    final Object devNumInputValue = getUserInputFromComponent(inputComponentForNewValue_, paramForNewValue.getType());
                    status.setText("Set to " + devNumInputValue);
                    setterMethod.invoke(configInstance, devNumInputValue);
                }
                status.setForeground(Color.black);
            } catch (Exception ex) {
                if (ex instanceof InvocationTargetException && ex.getCause() instanceof JMCCULException) {
                    // usually means the DAQ device does not support this config item
                    status.setForeground(Color.black);
                    status.setText(ex.getCause().getMessage());
                } else {
                    System.err.println("Exception setting " + methodNameWithoutPrefix + " ( " + configClass.getName() + "):");
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

    private Component getInputComponentForParameter(Parameter param) throws ReflectiveOperationException {
        final Class<?> paramType = param.getType();
        if (paramType.isEnum()) {
            // Get all the enum values using reflection, then filter out anything named NOT_USED or NOT_SET.
            final Object invoked = paramType.getMethod("values").invoke(null);
            final Object[] castToObjectArray = (Object[]) invoked;
            final Object[] filtered = Arrays.stream(castToObjectArray).filter(obj -> {
                final String enumName = ((Enum<?>) obj).name();
                return !enumName.equals("NOT_USED") && !enumName.equals("NOT_SET");
            }).toArray();
            return new JComboBox(filtered);

        } else if (paramType == boolean.class) {
            return new JCheckBox();

        } else if (paramType == String.class) {
            return new JTextField(10);

        } else {
            return new JTextField("0", 2);
        }
    }

    private Object getUserInputFromComponent(Component inputComponent, Class<?> desiredType) {
        if (inputComponent instanceof JComboBox) {
            if (desiredType.isEnum()) {
                final JComboBox castToComboBox = (JComboBox) inputComponent;
                return castToComboBox.getSelectedItem();
            } else {
                throw new IllegalArgumentException("the component is a JComboBox but the desired type is not an enum: " + desiredType);
            }
        } else if (inputComponent instanceof JCheckBox) {
            if (desiredType == boolean.class) {
                final JCheckBox castToCheckbox = (JCheckBox) inputComponent;
                return castToCheckbox.isSelected();
            } else {
                throw new IllegalArgumentException("the component is a JCheckBox but the desired type is not boolean: " + desiredType);
            }
        } else if (inputComponent instanceof JTextField) {
            final JTextField castToTextField = (JTextField) inputComponent;
            if (desiredType == String.class) {
                return castToTextField.getText();
            } else if (desiredType == int.class) {
                return Integer.parseInt(castToTextField.getText());
            } else {
                throw new IllegalArgumentException("the component is a JTextField but the desired type is neither String nor int: " + desiredType);
            }
        } else {
            throw new IllegalArgumentException("don't know what to do when the component is a " + inputComponent.getClass().getSimpleName());
        }
    }

    private Map<String, GetterAndSetter> getMethodMap(Class<?> configClass) throws SecurityException {
        // the map key is the name of the methods without the get/set prefix
        final Map<String, GetterAndSetter> rv = new HashMap<>();

        for (Method method : configClass.getDeclaredMethods()) {
            final String methodName = method.getName();
            if (!(methodName.startsWith("get") || methodName.startsWith("set"))) {
                System.out.println(configClass.getSimpleName() + ": method name does not start with get or set: " + methodName);
                continue;
            }

            final String methodNameWithoutPrefix = methodName.substring(3);
            if (rv.containsKey(methodNameWithoutPrefix)) {
                // add to the existing GetterAndSetter object
                final GetterAndSetter gas = rv.get(methodNameWithoutPrefix);
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
                rv.put(methodNameWithoutPrefix, gas);
            }
        }
        return rv;
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
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 javax.swing.UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new ConfigTestGui().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<xyz.froud.jmccul.jna.DaqDeviceDescriptor> jComboBoxDaqDevice;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

}
