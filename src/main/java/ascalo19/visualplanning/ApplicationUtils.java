package ascalo19.visualplanning;

import org.apache.commons.exec.*;
import org.springframework.boot.ApplicationPid;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.FileCopyUtils;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ApplicationUtils implements ApplicationContextAware {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private static ApplicationContext applicationContext;

    public static boolean isPortAvailable(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                /* should not be thrown */
                }
            }
        }
        return false;
    }

    public static boolean isProcessRunning(int pid, int timeout, TimeUnit timeunit) throws java.io.IOException {
        String line;
        if (OS.isFamilyWindows()) {
            //tasklist exit code is always 0. Parse output
            //findstr exit code 0 if found pid, 1 if it doesn't
            line = "cmd /c \"tasklist /FI \"PID eq " + pid + "\" | findstr " + pid + "\"";
        } else {
            //ps exit code 0 if process exists, 1 if it doesn't
            line = "ps -p " + pid;
        }
        CommandLine cmdLine = CommandLine.parse(line);
        DefaultExecutor executor = new DefaultExecutor();
        // disable logging of stdout/strderr
        executor.setStreamHandler(new PumpStreamHandler(null, null, null));
        // disable exception for valid exit values
        executor.setExitValues(new int[]{0, 1});
        // set timer for zombie process
        ExecuteWatchdog timeoutWatchdog = new ExecuteWatchdog(timeunit.toMillis(timeout));
        executor.setWatchdog(timeoutWatchdog);
        int exitValue = executor.execute(cmdLine);
        // 0 is the default exit code which means the process exists
        return exitValue == 0;
    }

    public static boolean isRunning() {
        try {
            File pidFile = new File(Application.PID_FILE_NAME);
            if (pidFile.exists()) {
                String pid = FileCopyUtils.copyToString(new FileReader(pidFile));
                if (new ApplicationPid().toString().equals(pid)) {
                    return true;
                } else {
                    return isProcessRunning(Integer.parseInt(pid), 10, TimeUnit.SECONDS);
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return false;
    }

    public static void browseQuietly(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
            // Ignore
        }
    }

    public static void showUi() {
        String serverPort = "8080";
        if (applicationContext != null) {
            serverPort = applicationContext.getEnvironment().getProperty("local.server.port");
        }
        browseQuietly("http://localhost:" + serverPort);
    }

    public static Date timeOnly(Date date) {
        return parseTime(timeFormat.format(date));
    }

    public static Date parseDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            // Ignore
        }
        return null;
    }

    public static Date parseTime(String time) {
        try {
            return timeFormat.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
            // Ignore
        }
        return null;
    }

    public static Date parseDateTime(String dateTime) {
        try {
            return dateTimeFormat.parse(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
            // Ignore
        }
        return null;
    }

    public static Date setTime(Date date, Date time) {
        return parseDateTime(dateFormat.format(date) + " " + timeFormat.format(time));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
