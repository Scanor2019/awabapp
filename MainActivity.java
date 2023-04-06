import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references to the start and stop buttons
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);

        // Set up click listeners for the buttons
        startButton.setOnClickListener(v -> {
            // Check if accessibility service is enabled
            if (!isAccessibilityServiceEnabled()) {
                // Ask the user to enable the service
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                return;
            }

            // Open the TikTok link
            String url = "https://vt.tiktok.com/ZS8sPuCqX/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);

            // Wait for 5 seconds
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Click on position (300, 990)
            clickOnPosition(300, 990);

            // Wait for 5 seconds
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Click on position (970, 380)
            clickOnPosition(970, 380);

            // Wait for 5 seconds
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Check the RGB value at position (270, 2113)
            int[] rgb = getRGBAtPosition(270, 2113);
            if (rgb[0] == 241 && rgb[1] == 241 && rgb[2] == 241) {
                // Click on position (270, 2113)
                clickOnPosition(270, 2113);
            } else {
                // Scroll the screen
                scrollScreen();
            }
        });

        stopButton.setOnClickListener(v -> {
            // Stop any ongoing actions
            // ...
        });
    }

    /**
     * Checks if the Accessibility Service is enabled for this app.
     *
     * @return true if enabled, false otherwise.
     */
    private boolean isAccessibilityServiceEnabled() {
        AccessibilityManager am = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo service : enabledServices) {
            if (service.getResolveInfo().serviceInfo.packageName.equals(getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Performs a click at the specified position on the screen.
     *
     * @param x The x-coordinate of the position to click.
     * @param y The y-coordinate of the position to click.
     */
    private void clickOnPosition(int x, int y) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            AccessibilityNodeInfo targetNode = findNodeAtPosition(nodeInfo, x, y);
            if (targetNode != null) {
                targetNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    /**
     * Finds the AccessibilityNodeInfo at the specified position.
     *
     * @param rootNode The root AccessibilityNodeInfo to start searching from.
     * @param x        The x-coordinate of the position to find.
     * @param y        The y-coordinate of the position to find.
     * @return The AccessibilityNodeInfo at the specified position, or null if not found.
     */
    private AccessibilityNodeInfo findNodeAtPosition(AccessibilityNodeInfo rootNode, int x, int y) {
        // Use DFS to find the node at the specified position
        if (rootNode.getBoundsInScreen().contains(x, y)) {
            for (int i = 0; i < rootNode.getChildCount(); i++) {
                AccessibilityNodeInfo child = rootNode.getChild(i);
                if (child != null) {
                    AccessibilityNodeInfo foundNode = findNodeAtPosition(child, x, y);
                    if (foundNode != null) {
                        return foundNode;
                    }
                }
            }
            return rootNode;
        }
        return null;
    }

    /**
     * Gets the RGB color value at the specified position on the screen.
     *
     * @param x The x-coordinate of the position to check.
     * @param y The y-coordinate of the position to check.
     * @return An int array containing the RGB values at the specified position.
     */
    private int[] getRGBAtPosition(int x, int y) {
        int pixel = 0;
        int[] rgb = new int[3];
        try {
            pixel = getWindow().getDecorView().getRootView().getDrawingCache().getPixel(x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rgb[0] = Color.red(pixel);
        rgb[1] = Color.green(pixel);
        rgb[2] = Color.blue(pixel);
        return rgb;
    }

    /**
     * Scrolls the screen down.
     */
    private void scrollScreen() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        }
    }
}
