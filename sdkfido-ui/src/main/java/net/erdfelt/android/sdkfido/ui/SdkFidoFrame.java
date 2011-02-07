package net.erdfelt.android.sdkfido.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
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
import net.erdfelt.android.sdkfido.ui.layout.GBC;
import net.erdfelt.android.sdkfido.ui.panels.ConsoleView;
import net.erdfelt.android.sdkfido.ui.panels.SdkFetchPanel;
import net.erdfelt.android.sdkfido.ui.panels.WorkDirPanel;

import org.apache.commons.lang.SystemUtils;

public class SdkFidoFrame extends JFrame {
    private static final long   serialVersionUID = 1L;
    private static final Logger LOG              = Logger.getLogger(SdkFidoFrame.class.getName());
    private ActionMapper        actionMapper;
    private WindowHandler       winhandler;
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

        pack();
        
        Dimension minDim = getPreferredSize();
        setMinimumSize(minDim);
        
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
        WorkDirPanel workDirPanel = new WorkDirPanel();
        return workDirPanel;
    }

    private Component createSdkPanel() {
        SdkFetchPanel sdkPanel = new SdkFetchPanel();
        return sdkPanel;
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
