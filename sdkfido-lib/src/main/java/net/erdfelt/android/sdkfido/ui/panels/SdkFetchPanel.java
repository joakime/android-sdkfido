package net.erdfelt.android.sdkfido.ui.panels;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import net.erdfelt.android.sdkfido.ui.actions.ActionMapper;
import net.erdfelt.android.sdkfido.ui.actions.ActionTarget;
import net.erdfelt.android.sdkfido.ui.layout.CommonStyles;
import net.erdfelt.android.sdkfido.ui.layout.GBC;
import net.erdfelt.android.sdkfido.ui.layout.GBCStyles;
import net.erdfelt.android.sdkfido.ui.utils.DirectoryPicker;
import net.erdfelt.android.sdkfido.ui.utils.TableUtils;

public class SdkFetchPanel extends JPanel {
    private static final long serialVersionUID = 7709281899499719804L;
    private static final Logger LOG = Logger.getLogger(SdkFetchPanel.class.getName());
    private ActionMapper actionMapper;

    public SdkFetchPanel() {
        super(false);
        actionMapper = new ActionMapper(this);
        initGui();
        Dimension minDim = new Dimension(500,200);
        setPreferredSize(minDim);
        setMinimumSize(minDim);
    }

    private void initGui() {
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

        TableUtils.setMinimumHeight(80, tableAvailableSdks, tableScroller);

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
        this.setLayout(new GridBagLayout());

        GBCStyles styles = CommonStyles.baseline();

        this.add(lblSdkHome, styles.use("label"));
        this.add(txtSdkHome, styles.use("dirtext"));
        this.add(pickerSdkHome, styles.use("dirpicker").endRow());

        this.add(lblProjectsHome, styles.use("label"));
        this.add(txtProjectsHome, styles.use("dirtext"));
        this.add(pickerProjectsHome, styles.use("dirpicker").endRow());

        this.add(lblAvailableSdks, styles.use("label").align(GBC.TOP_LEFT));
        this.add(tableScroller, styles.use("table").endRow());

        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new GridBagLayout());
        buttonBar.add(btnFetch, styles.use("button"));
        buttonBar.add(btnMavenize, styles.use("button"));
        buttonBar.add(progressBar, styles.use("progressbar").endBoth());

        this.add(buttonBar, styles.use("button_bar").x(1).endBoth());
    }

    @ActionTarget(name = "fetch")
    public void doFetch(ActionEvent event) {
        // TODO
        LOG.info("TODO: Implement Fetch Action");
    }

    @ActionTarget(name = "mavenize")
    public void doMavenize(ActionEvent event) {
        // TODO
        LOG.info("TODO: Implement Mavenize Action");
    }

    private File getMavenProjectsHomeDir() {
        // TODO Auto-generated method stub
        return new File("/home/joakim/code/android-maven/");
    }

    private File getSdkHomeDir() {
        // TODO Auto-generated method stub
        return new File("/home/joakim/java/android/current/");
    }
}
