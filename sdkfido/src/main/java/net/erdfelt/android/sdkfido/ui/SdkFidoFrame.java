package net.erdfelt.android.sdkfido.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import net.erdfelt.android.sdkfido.Build;
import net.erdfelt.android.sdkfido.Prefs;
import net.erdfelt.android.sdkfido.ui.actions.ActionMapper;
import net.erdfelt.android.sdkfido.ui.actions.ActionTarget;
import net.erdfelt.android.sdkfido.ui.actions.KeyAction;
import net.erdfelt.android.sdkfido.ui.layout.CommonStyles;
import net.erdfelt.android.sdkfido.ui.layout.GBC;
import net.erdfelt.android.sdkfido.ui.layout.GBCStyles;
import net.erdfelt.android.sdkfido.ui.layout.Placeholder;

import org.apache.commons.lang.SystemUtils;

public class SdkFidoFrame extends JFrame {
    private static final long   serialVersionUID = 1L;
    private static final Logger LOG              = Logger.getLogger(SdkFidoFrame.class.getName());
    private ActionMapper        actionMapper;
    private WindowHandler       winhandler;
    private JLabel              lblStatus;
    private ConsoleView         console;

    public SdkFidoFrame() {
        LOG.info("SdkFidoFrame Start");
        initgui();
    }

    private void initgui() {
        setName("SdkFidoFrame");

        this.winhandler = new WindowHandler(this, true);
        this.winhandler.setPersistLocation(true);
        this.winhandler.setPersistSize(true);

        this.actionMapper = new ActionMapper(this);

        String lnf = Prefs.getString("looknfeel", UIManager.getCrossPlatformLookAndFeelClassName());
        setLookAndFeel(lnf);

        setTitle("SDKFido - " + Build.getVersion());

        getContentPane().setLayout(new BorderLayout());
        enableExitKey();
        // Menu Bar
        setJMenuBar(createMainMenu());

        Container content = getContentPane();
        content.add(BorderLayout.CENTER, createBody());

        // Status Bar
        content.add(BorderLayout.SOUTH, createStatusBar());

        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.winhandler.setSizePreferred(new Dimension(400, 400));
        addWindowListener(this.winhandler);
    }

    private Component createBody() {
        Container body = new Container();

        body.setLayout(new GridBagLayout());
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("SDKs", createSdkPanel());
        tabs.addTab("Work Dir", createWorkDirPanel());

        body.add(tabs, new GBC().fillWide().margin(5, 5, 5, 5).endRow());
        body.add(createConsolePane(), new GBC().fillBoth().margin(0, 5, 5, 5).weightTall(1.0).endBoth());

        return body;
    }

    private ConsoleView createConsolePane() {
        console = new ConsoleView();
        return console;
    }

    private Component createWorkDirPanel() {
        // TODO Auto-generated method stub
        return new Placeholder("Work Dir Panel");
    }

    private Component createSdkPanel() {
        JPanel panel = new JPanel();

        // Create Widgets

        JLabel lblSdkHome = new JLabel("ANDROID_HOME:");
        JTextField txtSdkHome = new JTextField();
        DirectoryPicker pickerSdkHome = new DirectoryPicker();
        pickerSdkHome.setTextField(txtSdkHome);
        pickerSdkHome.setDefaultDirectory(getSdkHomeDir());

        JLabel lblProjectsHome = new JLabel("Maven Projects Home:");
        JTextField txtProjectsHome = new JTextField();
        DirectoryPicker pickerProjectsHome = new DirectoryPicker();
        pickerProjectsHome.setTextField(txtProjectsHome);
        pickerProjectsHome.setDefaultDirectory(getMavenProjectsHomeDir());

        JLabel lblAvailableSdks = new JLabel("Available Android SDKs:");

        // TODO: switch to model
        String columns[] = { "SDK Version", "Status" };
        String data[][] = { { "Android 2.2.1", "Available" }, { "Android 2.1.2", "Not present in SDK Home" },
                { "Android 2.0", "Not present in SDK Home" }, { "Android 1.6", "Not present in SDK Home" },
                { "Android 1.5", "Not present in SDK Home" } };
        JTable tableAvailableSdks = new JTable(data, columns);
        JScrollPane tableScroller = new JScrollPane(tableAvailableSdks);
        
        Dimension d = tableAvailableSdks.getPreferredScrollableViewportSize();
        d.height = 80;
        tableAvailableSdks.setPreferredScrollableViewportSize(d);
        tableAvailableSdks.setMinimumSize(d);
        tableAvailableSdks.setFillsViewportHeight(true);
        
        Dimension hd = tableAvailableSdks.getTableHeader().getPreferredSize();
        d.height += hd.height;
        tableScroller.setMinimumSize(d);

        JProgressBar progressBar = new JProgressBar();
        JButton btnFetch = new JButton();
        btnFetch.setAction(actionMapper);
        btnFetch.setActionCommand("fetch");
        btnFetch.setText("Fetch!");

        JButton btnMavenize = new JButton();
        btnMavenize.setAction(actionMapper);
        btnMavenize.setActionCommand("mavenize");
        btnMavenize.setText("Mavenize");

        // Add Widgets
        panel.setLayout(new GridBagLayout());

        GBCStyles styles = CommonStyles.baseline();

        panel.add(lblSdkHome, styles.use("label"));
        panel.add(txtSdkHome, styles.use("dirtext"));
        panel.add(pickerSdkHome, styles.use("dirpicker").endRow());

        panel.add(lblProjectsHome, styles.use("label"));
        panel.add(txtProjectsHome, styles.use("dirtext"));
        panel.add(pickerProjectsHome, styles.use("dirpicker").endRow());

        panel.add(lblAvailableSdks, styles.use("label").align(GBC.TOP_LEFT));
        panel.add(tableScroller, styles.use("table").endRow());

        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new GridBagLayout());
        buttonBar.add(btnFetch, styles.use("button"));
        buttonBar.add(btnMavenize, styles.use("button"));
        buttonBar.add(progressBar, styles.use("progressbar").fillWide().endBoth());

        panel.add(buttonBar, styles.use("button_bar").x(1).fillWide().endBoth());

        return panel;
    }

    private File getMavenProjectsHomeDir() {
        // TODO Auto-generated method stub
        return new File("/home/joakim/code/android-maven/");
    }

    private File getSdkHomeDir() {
        // TODO Auto-generated method stub
        return new File("/home/joakim/java/android/current/");
    }

    private JMenuBar createMainMenu() {
        JMenuBar mainMenu = new JMenuBar();
        mainMenu.add(createFileMenu());
        mainMenu.add(createViewMenu());
        return mainMenu;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('f');

        JMenuItem fileExit = new JMenuItem("Exit");
        fileExit.setMnemonic('x');
        fileExit.setActionCommand("exit");
        fileExit.addActionListener(actionMapper);
        fileMenu.add(fileExit);

        return fileMenu;
    }

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('v');

        JMenu lnfMenu = new JMenu("Look and Feel");
        lnfMenu.setMnemonic('f');

        ButtonGroup lnfGroup = new ButtonGroup();
        LookAndFeelInfo lnfs[] = UIManager.getInstalledLookAndFeels();
        String lnfCurrentName = null;
        LookAndFeel lnfCurrent = UIManager.getLookAndFeel();
        if (lnfCurrent != null) {
            lnfCurrentName = lnfCurrent.getClass().getName();
        }
        UISwitcher switcher = new UISwitcher();
        for (int i = 0; i < lnfs.length; i++) {
            JRadioButtonMenuItem lnfItem = new JRadioButtonMenuItem(lnfs[i].getName());
            lnfItem.addActionListener(switcher);
            lnfItem.setActionCommand(lnfs[i].getClassName());
            lnfGroup.add(lnfItem);
            lnfMenu.add(lnfItem);

            if (lnfs[i].getClassName().equals(lnfCurrentName)) {
                lnfGroup.setSelected(lnfItem.getModel(), true);
            }
        }
        viewMenu.add(lnfMenu);

        return viewMenu;
    }

    private JPanel createStatusBar() {
        JPanel statusbar = new JPanel();

        statusbar.setLayout(new BorderLayout());

        lblStatus = new JLabel(
                "Welcome to SDKFido.  Setup your SDK Home, pick an SDK to download, and hit Download ...");
        lblStatus.setFont(new Font("dialog", Font.PLAIN, 10));
        statusbar.add(BorderLayout.CENTER, lblStatus);

        return statusbar;
    }

    private void enableExitKey() {
        InputMap rootInput = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap rootAction = getRootPane().getActionMap();

        if (SystemUtils.IS_OS_UNIX || SystemUtils.IS_OS_WINDOWS) {
            rootInput.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_DOWN_MASK), "exit");
            rootInput.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK), "exit");
        }

        if (SystemUtils.IS_OS_MAC) {
            rootInput.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.META_DOWN_MASK), "exit");
        }

        rootAction.put("exit", new KeyAction(actionMapper, "exit"));
    }

    @ActionTarget(name = "exit")
    public void doExit(ActionEvent event) {
        winhandler.close();
    }

    @ActionTarget(name = "fetch")
    public void doFetch(ActionEvent event) {
        // TODO
        TODO("Implement Fetch Action");
    }

    @ActionTarget(name = "mavenize")
    public void doMavenize(ActionEvent event) {
        // TODO
        TODO("Implement Mavenize Action");
    }

    @ActionTarget(name = "gitupdate")
    public void doGitUpdate(ActionEvent event) {
        // TODO
        TODO("Implement Git Update Action");
    }

    private void TODO(String msg) {
        LOG.warning("!TODO! - " + msg);
    }
    
    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        console.attachLogger();
    }

    public void setLookAndFeel(String uiclassname) {
        try {
            UIManager.setLookAndFeel(uiclassname);
            SwingUtilities.updateComponentTreeUI(this);
            Prefs.setString("looknfeel", uiclassname);
            Prefs.save();
        } catch (ClassNotFoundException e1) {
            LOG.warning("Unable to set Look and Feel (it is missing).");
        } catch (InstantiationException e1) {
            LOG.warning("Unable to set Look and Feel (cannot be instantiated by JRE).");
        } catch (IllegalAccessException e1) {
            LOG.warning("Unable to set Look and Feel (cannot be used by JRE).");
        } catch (UnsupportedLookAndFeelException e1) {
            LOG.warning("Unable to set Look and Feel (not supported by JRE).");
        }
    }

    public class UISwitcher implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JRadioButtonMenuItem) {
                setLookAndFeel(e.getActionCommand());
            }
        }
    }
}
