package com.liquidbrowser;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private WebView webView;
    private EditText addressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildLiquidInterface();
    }

    private void buildLiquidInterface() {

        /*
         * ROOT
         */

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                Color.rgb(248, 248, 250)
        );

        /*
         * TOP GLASS AREA
         */

        LinearLayout topGlass =
                createGlassContainer();

        LinearLayout.LayoutParams topParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        topParams.setMargins(
                14,
                18,
                14,
                10
        );

        /*
         * ADDRESS BAR
         */

        addressBar =
                new EditText(this);

        addressBar.setSingleLine(true);

        addressBar.setHint(
                "Search or enter website"
        );

        addressBar.setTextSize(16);

        addressBar.setTextColor(
                Color.rgb(25, 25, 28)
        );

        addressBar.setHintTextColor(
                Color.rgb(120, 120, 125)
        );

        addressBar.setPadding(
                22,
                16,
                22,
                16
        );

        GradientDrawable addressBackground =
                new GradientDrawable();

        addressBackground.setColor(
                Color.argb(
                        175,
                        255,
                        255,
                        255
                )
        );

        addressBackground.setCornerRadius(
                60
        );

        addressBackground.setStroke(
                1,
                Color.argb(
                        90,
                        255,
                        255,
                        255
                )
        );

        addressBar.setBackground(
                addressBackground
        );

        topGlass.addView(
                addressBar,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

        root.addView(
                topGlass,
                topParams
        );

        /*
         * WEBVIEW
         */

        webView =
                new WebView(this);

        configureWebView();

        LinearLayout.LayoutParams webParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1
                );

        root.addView(
                webView,
                webParams
        );

        /*
         * BOTTOM GLASS TOOLBAR
         */

        LinearLayout bottomGlass =
                createGlassContainer();

        bottomGlass.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams bottomParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        bottomParams.setMargins(
                14,
                8,
                14,
                16
        );

        /*
         * BACK
         */

        ImageButton back =
                createGlassButton(
                        "‹"
                );

        /*
         * FORWARD
         */

        ImageButton forward =
                createGlassButton(
                        "›"
                );

        /*
         * TABS
         */

        ImageButton tabs =
                createGlassButton(
                        "▢"
                );

        /*
         * MENU
         */

        ImageButton menu =
                createGlassButton(
                        "•••"
                );

        bottomGlass.addView(back);
        bottomGlass.addView(forward);
        bottomGlass.addView(tabs);
        bottomGlass.addView(menu);

        root.addView(
                bottomGlass,
                bottomParams
        );

        /*
         * ACTIONS
         */

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

        tabs.setOnClickListener(
                v -> {

                    showMessage(
                            "Tabs coming next"
                    );
                }
        );

        menu.setOnClickListener(
                v -> {

                    showMessage(
                            "Browser menu"
                    );
                }
        );

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

        /*
         * START PAGE
         */

        webView.loadUrl(
                "https://www.google.com"
        );

        setContentView(root);
    }

    /*
     * ============================
     * WEBVIEW
     * ============================
     */

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

        settings.setUseWideViewPort(
                true
        );

        settings.setLoadWithOverviewMode(
                true
        );

        webView.setWebViewClient(
                new WebViewClient() {

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
                    }
                }
        );

        webView.setWebChromeClient(
                new WebChromeClient()
        );
    }

    /*
     * ============================
     * OPEN ADDRESS
     * ============================
     */

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
                        "https://"
                )
                ||
                input.startsWith(
                        "http://"
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
                    android.net.Uri.encode(
                            input
                    );
        }

        webView.loadUrl(
                url
        );
    }

    /*
     * ============================
     * GLASS CONTAINER
     * ============================
     */

    private LinearLayout createGlassContainer() {

        LinearLayout container =
                new LinearLayout(this);

        container.setOrientation(
                LinearLayout.HORIZONTAL
        );

        container.setGravity(
                Gravity.CENTER
        );

        container.setPadding(
                8,
                8,
                8,
                8
        );

        GradientDrawable glass =
                new GradientDrawable();

        glass.setColor(
                Color.argb(
                        190,
                        255,
                        255,
                        255
                )
        );

        glass.setCornerRadius(
                32
        );

        glass.setStroke(
                1,
                Color.argb(
                        100,
                        255,
                        255,
                        255
                )
        );

        container.setBackground(
                glass
        );

        container.setElevation(
                10
        );

        return container;
    }

    /*
     * ============================
     * GLASS BUTTON
     * ============================
     */

    private ImageButton createGlassButton(
            String symbol
    ) {

        ImageButton button =
                new ImageButton(this);

        button.setImageDrawable(
                new android.graphics.drawable.ColorDrawable(
                        Color.TRANSPARENT
                )
        );

        button.setContentDescription(
                symbol
        );

        button.setBackgroundColor(
                Color.TRANSPARENT
        );

        button.setPadding(
                20,
                12,
                20,
                12
        );

        TextView label =
                new TextView(this);

        label.setText(
                symbol
        );

        label.setTextSize(
                22
        );

        label.setTextColor(
                Color.BLACK
        );

        /*
         * Android ImageButton cannot directly
         * contain TextView, so use a drawable
         * generated from text.
         *
         * For now we use the accessibility
         * description and simple native button.
         */

        button.setContentDescription(
                symbol
        );

        return button;
    }

    /*
     * ============================
     * MESSAGE
     * ============================
     */

    private void showMessage(
            String message
    ) {

        android.widget.Toast.makeText(
                this,
                message,
                android.widget.Toast.LENGTH_SHORT
        ).show();
    }

    /*
     * ============================
     * ANDROID BACK
     * ============================
     */

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