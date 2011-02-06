package net.erdfelt.android.sdkfido.ui;

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
import net.erdfelt.android.sdkfido.ui.debug.DebugFrame;
import net.erdfelt.android.sdkfido.ui.layout.CommonStyles;
import net.erdfelt.android.sdkfido.ui.layout.GBCStyles;

public class WorkDirPanel extends JPanel {
    private static final long  serialVersionUID = 7744574839920516820L;
    public static final Logger LOG              = Logger.getLogger(WorkDirPanel.class.getName());
    private ActionMapper       actionMapper;

    public static void main(String[] args) {
        DebugFrame frame = new DebugFrame(new WorkDirPanel());
        frame.setVisible(true);
    }

    public WorkDirPanel() {
        super(false);
        actionMapper = new ActionMapper(this);
        initGui();
        Dimension minDim = new Dimension(500,200);
        setPreferredSize(minDim);
        setMinimumSize(minDim);
    }

    private void initGui() {
        // Create Widgets
        JLabel lblWorkDir = new JLabel("Work Dir:");
        JTextField txtWorkDir = new JTextField();
        DirectoryPicker pickerWorkDir = new DirectoryPicker();
        pickerWorkDir.setTextField(txtWorkDir);
        pickerWorkDir.setDefaultDirectory(getWorkDir());

        // TODO: switch to model
        String columns[] = { "Repo", "Branch", "Timestamp", "Size" };
        String data[][] = { { "android.kernel.org/base.git", "master", "2011-Feb-5", "320MB" },
                { "android.kernel.org/phone.git", "android-sdk-2.2.1", "2011-Jan-25", "110MB" } };
        JTable tableRepos = new JTable(data, columns);
        JScrollPane tableScroller = new JScrollPane(tableRepos);
        TableUtils.setMinimumHeight(50, tableRepos, tableScroller);

        JProgressBar progressBar = new JProgressBar();
        JButton btnGitPull = new JButton();
        btnGitPull.setAction(actionMapper);
        btnGitPull.setActionCommand("git-pull");
        btnGitPull.setText("Git Pull");

        JButton btnGitMaster = new JButton();
        btnGitMaster.setAction(actionMapper);
        btnGitMaster.setActionCommand("git-master");
        btnGitMaster.setText("Git Branch Master");

        JButton btnOpenOS = new JButton();
        btnOpenOS.setAction(actionMapper);
        btnOpenOS.setActionCommand("open-in-os");
        btnOpenOS.setText("Open in OS");

        // Add Widgets
        this.setLayout(new GridBagLayout());

        GBCStyles styles = CommonStyles.baseline();

        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new GridBagLayout());
        buttonBar.add(btnGitPull, styles.use("button"));
        buttonBar.add(btnGitMaster, styles.use("button"));
        buttonBar.add(btnOpenOS, styles.use("button"));
        buttonBar.add(progressBar, styles.use("progressbar").endBoth());
        
        this.add(lblWorkDir, styles.use("label"));
        this.add(txtWorkDir, styles.use("dirtext"));
        this.add(pickerWorkDir, styles.use("dirpicker").endRow());
        
        this.add(tableScroller, styles.use("table").endRow());
        this.add(buttonBar, styles.use("button_bar").endRow());
    }

    @ActionTarget(name = "git-pull")
    public void doGitPull(ActionEvent event) {
        LOG.info("TODO: Implement Git Pull");
    }

    @ActionTarget(name = "git-master")
    public void doGitBranchMaster(ActionEvent event) {
        LOG.info("TODO: Implement Git Branch Master");
    }

    @ActionTarget(name = "open-in-os")
    public void doOpenInOS(ActionEvent event) {
        LOG.info("TODO: Implement Open In OS");
    }

    private File getWorkDir() {
        // TODO Auto-generated method stub
        return new File("/home/joakim/.sdkfido");
    }
}
