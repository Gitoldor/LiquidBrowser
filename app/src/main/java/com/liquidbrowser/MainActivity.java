package com.liquidbrowser;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

public class MainActivity extends Activity {

    private WebView webView;
    private EditText addressBar;

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

        addressBar.setText(
                "https://www.google.com"
        );

        addressBar.setTextSize(16);

        addressBar.setPadding(
                30,
                20,
                30,
                20
        );

        GradientDrawable addressBackground =
                new GradientDrawable();

        addressBackground.setColor(
                Color.rgb(245, 245, 247)
        );

        addressBackground.setCornerRadius(
                60
        );

        addressBar.setBackground(
                addressBackground
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

        webView.setWebViewClient(
                new WebViewClient()
        );

        webView.setWebChromeClient(
                new WebChromeClient()
        );

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
        // NAVIGATION BAR
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
                10,
                10,
                10,
                15
        );

        Button back =
                createButton("‹");

        Button forward =
                createButton("›");

        Button refresh =
                createButton("↻");

        navigation.addView(back);
        navigation.addView(forward);
        navigation.addView(refresh);

        root.addView(
                navigation
        );

        // =========================
        // BUTTON ACTIONS
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

        // =========================
        // ADDRESS BAR ACTION
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
        // LOAD FIRST PAGE
        // =========================

        webView.loadUrl(
                "https://www.google.com"
        );

        setContentView(root);
    }

    private Button createButton(
            String text
    ) {

        Button button =
                new Button(this);

        button.setText(text);

        button.setTextSize(24);

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
                input.startsWith("http://")
                ||
                input.startsWith("https://")
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
                    + input.replace(
                            " ",
                            "+"
                    );
        }

        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {

        if (webView != null
                && webView.canGoBack()) {

            webView.goBack();

        } else {

            super.onBackPressed();
        }
    }
}