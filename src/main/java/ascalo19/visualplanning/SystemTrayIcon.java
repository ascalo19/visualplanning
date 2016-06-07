package ascalo19.visualplanning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Component
public class SystemTrayIcon {

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        new SystemTrayIcon().initSystemTray();
    }

    @PostConstruct
    private void initSystemTray() {
        try {
            if (SystemTray.isSupported()) {

                PopupMenu popup = new PopupMenu();
                ImageIcon icon = new ImageIcon(getClass().getResource("/static/images/tray-white.png"));
                TrayIcon trayIcon = new TrayIcon(icon.getImage());
                SystemTray tray = SystemTray.getSystemTray();

                MenuItem open = new MenuItem("Ouvrir");
                open.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ApplicationUtils.showUi();
                    }
                });

                MenuItem exit = new MenuItem("Quitter");
                exit.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SpringApplication.exit(applicationContext);
                        tray.remove(trayIcon);
                    }
                });

                popup.add(open);
                popup.add(exit);
                trayIcon.setPopupMenu(popup);
                trayIcon.setToolTip("Visual Planning");
                tray.add(trayIcon);

            }
        } catch (Exception e) {
            // Ignore
        }
    }
}
