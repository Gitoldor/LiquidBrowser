package com.liquidbrowser;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity {

    private WebView webView;
    private EditText addressBar;

    private boolean incognitoMode = false;
    private boolean desktopMode = false;

    private final List<String> history =
            new ArrayList<>();

    private final Set<String> bookmarks =
            new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createBrowser();
    }

    private void createBrowser() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                Color.WHITE
        );

        // =========================
        // ADDRESS BAR
        // =========================

        addressBar =
                new EditText(this);

        addressBar.setSingleLine(true);

        addressBar.setHint(
                "Search or enter website"
        );

        addressBar.setTextSize(16);

        addressBar.setPadding(
                30,
                20,
                30,
                20
        );

        LinearLayout.LayoutParams
                addressParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        addressParams.setMargins(
                20,
                20,
                20,
                10
        );

        root.addView(
                addressBar,
                addressParams
        );

        // =========================
        // WEBVIEW
        // =========================

        webView =
                new WebView(this);

        configureWebView();

        LinearLayout.LayoutParams
                webParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1
                );

        root.addView(
                webView,
                webParams
        );

        // =========================
        // NAVIGATION
        // =========================

        LinearLayout navigation =
                new LinearLayout(this);

        navigation.setOrientation(
                LinearLayout.HORIZONTAL
        );

        navigation.setGravity(
                Gravity.CENTER
        );

        navigation.setPadding(
                5,
                5,
                5,
                10
        );

        Button back =
                createButton("‹");

        Button forward =
                createButton("›");

        Button refresh =
                createButton("↻");

        Button menu =
                createButton("•••");

        navigation.addView(back);
        navigation.addView(forward);
        navigation.addView(refresh);
        navigation.addView(menu);

        root.addView(navigation);

        // =========================
        // BUTTONS
        // =========================

        back.setOnClickListener(
                v -> {

                    if (webView.canGoBack()) {
                        webView.goBack();
                    }
                }
        );

        forward.setOnClickListener(
                v -> {

                    if (webView.canGoForward()) {
                        webView.goForward();
                    }
                }
        );

        refresh.setOnClickListener(
                v -> webView.reload()
        );

        menu.setOnClickListener(
                v -> showBrowserMenu()
        );

        // =========================
        // ADDRESS BAR
        // =========================

        addressBar.setOnEditorActionListener(
                (v, actionId, event) -> {

                    openAddress(
                            addressBar
                                    .getText()
                                    .toString()
                    );

                    return true;
                }
        );

        // =========================
        // START PAGE
        // =========================

        webView.loadUrl(
                "https://www.google.com"
        );

        setContentView(root);
    }

    private void configureWebView() {

        WebSettings settings =
                webView.getSettings();

        settings.setJavaScriptEnabled(
                true
        );

        settings.setDomStorageEnabled(
                true
        );

        settings.setDatabaseEnabled(
                true
        );

        settings.setLoadWithOverviewMode(
                true
        );

        settings.setUseWideViewPort(
                true
        );

        settings.setBuiltInZoomControls(
                false
        );

        settings.setDisplayZoomControls(
                false
        );

        webView.setWebViewClient(
                new WebViewClient() {

                    @Override
                    public boolean shouldOverrideUrlLoading(
                            WebView view,
                            WebResourceRequest request
                    ) {

                        return false;
                    }

                    @Override
                    public void onPageFinished(
                            WebView view,
                            String url
                    ) {

                        super.onPageFinished(
                                view,
                                url
                        );

                        addressBar.setText(
                                url
                        );

                        if (!incognitoMode
                                && url != null
                                && !url.isEmpty()) {

                            history.add(url);
                        }
                    }
                }
        );

        webView.setWebChromeClient(
                new WebChromeClient()
        );

        // =========================
        // DOWNLOADS
        // =========================

        webView.setDownloadListener(
                new DownloadListener() {

                    @Override
                    public void onDownloadStart(
                            String url,
                            String userAgent,
                            String contentDisposition,
                            String mimeType,
                            long contentLength
                    ) {

                        startDownload(
                                url,
                                userAgent,
                                contentDisposition,
                                mimeType
                        );
                    }
                }
        );
    }

    private void startDownload(
            String url,
            String userAgent,
            String contentDisposition,
            String mimeType
    ) {

        try {

            DownloadManager.Request request =
                    new DownloadManager.Request(
                            Uri.parse(url)
                    );

            request.setMimeType(
                    mimeType
            );

            request.addRequestHeader(
                    "User-Agent",
                    userAgent
            );

            request.setNotificationVisibility(
                    DownloadManager
                            .Request
                            .VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            );

            request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "LiquidBrowserDownload"
            );

            DownloadManager manager =
                    (DownloadManager)
                            getSystemService(
                                    DOWNLOAD_SERVICE
                            );

            manager.enqueue(request);

            Toast.makeText(
                    this,
                    "Download started",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Download failed",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void openAddress(
            String input
    ) {

        input =
                input.trim();

        if (input.isEmpty()) {
            return;
        }

        String url;

        if (
                input.startsWith(
                        "http://"
                )
                ||
                input.startsWith(
                        "https://"
                )
        ) {

            url = input;

        } else if (
                input.contains(".")
                &&
                !input.contains(" ")
        ) {

            url =
                    "https://"
                    + input;

        } else {

            url =
                    "https://www.google.com/search?q="
                    +
                    Uri.encode(
                            input
                    );
        }

        webView.loadUrl(url);
    }

    // =========================
    // BROWSER MENU
    // =========================

    private void showBrowserMenu() {

        LinearLayout menu =
                new LinearLayout(this);

        menu.setOrientation(
                LinearLayout.VERTICAL
        );

        menu.setPadding(
                40,
                30,
                40,
                30
        );

        TextView title =
                new TextView(this);

        title.setText(
                "Liquid Browser"
        );

        title.setTextSize(24);

        menu.addView(title);

        Button bookmark =
                createMenuButton(
                        "🔖 Bookmark"
                );

        Button historyButton =
                createMenuButton(
                        "🕘 History"
                );

        Button incognito =
                createMenuButton(
                        "🕵️ Incognito"
                );

        Button desktop =
                createMenuButton(
                        "🖥 Desktop Site"
                );

        Button find =
                createMenuButton(
                        "🔍 Find in Page"
                );

        Button share =
                createMenuButton(
                        "↗ Share"
                );

        menu.addView(bookmark);
        menu.addView(historyButton);
        menu.addView(incognito);
        menu.addView(desktop);
        menu.addView(find);
        menu.addView(share);

        setContentView(menu);

        bookmark.setOnClickListener(
                v -> {

                    addBookmark();

                    createBrowser();
                }
        );

        historyButton.setOnClickListener(
                v -> showHistory()
        );

        incognito.setOnClickListener(
                v -> {

                    incognitoMode =
                            !incognitoMode;

                    Toast.makeText(
                            this,
                            incognitoMode
                                    ? "Incognito ON"
                                    : "Incognito OFF",
                            Toast.LENGTH_SHORT
                    ).show();

                    createBrowser();
                }
        );

        desktop.setOnClickListener(
                v -> {

                    desktopMode =
                            !desktopMode;

                    applyDesktopMode();

                    createBrowser();
                }
        );

        find.setOnClickListener(
                v -> showFindDialog()
        );

        share.setOnClickListener(
                v -> shareCurrentPage()
        );
    }

    private Button createMenuButton(
            String text
    ) {

        Button button =
                new Button(this);

        button.setText(text);

        button.setTextSize(16);

        return button;
    }

    // =========================
    // BOOKMARKS
    // =========================

    private void addBookmark() {

        String url =
                webView.getUrl();

        if (url == null) {
            return;
        }

        bookmarks.add(url);

        Toast.makeText(
                this,
                "Bookmarked",
                Toast.LENGTH_SHORT
        ).show();
    }

    // =========================
    // HISTORY
    // =========================

    private void showHistory() {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(
                30,
                40,
                30,
                30
        );

        TextView title =
                new TextView(this);

        title.setText(
                "History"
        );

        title.setTextSize(26);

        layout.addView(title);

        for (String url : history) {

            TextView item =
                    new TextView(this);

            item.setText(url);

            item.setTextSize(15);

            item.setPadding(
                    0,
                    20,
                    0,
                    20
            );

            layout.addView(item);
        }

        Button back =
                createMenuButton(
                        "Back to Browser"
                );

        layout.addView(back);

        back.setOnClickListener(
                v -> createBrowser()
        );

        setContentView(layout);
    }

    // =========================
    // DESKTOP MODE
    // =========================

    private void applyDesktopMode() {

        WebSettings settings =
                webView.getSettings();

        if (desktopMode) {

            settings.setUserAgentString(
                    "Mozilla/5.0 (X11; Linux x86_64) "
                    +
                    "AppleWebKit/537.36 "
                    +
                    "(KHTML, like Gecko) "
                    +
                    "Chrome/120 Safari/537.36"
            );

        } else {

            settings.setUserAgentString(
                    null
            );
        }
    }

    // =========================
    // FIND IN PAGE
    // =========================

    private void showFindDialog() {

        EditText input =
                new EditText(this);

        input.setHint(
                "Find text"
        );

        setContentView(input);

        input.requestFocus();

        input.setOnEditorActionListener(
                (v, actionId, event) -> {

                    webView.findAllAsync(
                            input.getText()
                                    .toString()
                    );

                    return true;
                }
        );
    }

    // =========================
    // SHARE
    // =========================

    private void shareCurrentPage() {

        String url =
                webView.getUrl();

        if (url == null) {
            return;
        }

        Intent share =
                new Intent(
                        Intent.ACTION_SEND
                );

        share.setType(
                "text/plain"
        );

        share.putExtra(
                Intent.EXTRA_TEXT,
                url
        );

        startActivity(
                Intent.createChooser(
                        share,
                        "Share page"
                )
        );
    }

    // =========================
    // NAVIGATION BUTTON
    // =========================

    private Button createButton(
            String text
    ) {

        Button button =
                new Button(this);

        button.setText(text);

        button.setTextSize(20);

        button.setTextColor(
                Color.BLACK
        );

        button.setBackgroundColor(
                Color.TRANSPARENT
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                );

        button.setLayoutParams(
                params
        );

        return button;
    }

    @Override
    public void onBackPressed() {

        if (
                webView != null
                &&
                webView.canGoBack()
        ) {

            webView.goBack();

        } else {

            super.onBackPressed();
        }
    }
}