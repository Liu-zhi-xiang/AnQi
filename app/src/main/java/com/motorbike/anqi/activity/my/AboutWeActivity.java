package com.motorbike.anqi.activity.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.motorbike.anqi.R;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;

/**
 * 关于我们
 */
public class AboutWeActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private WebView web;
    private WebSettings webSettings;
    private  JavaScriptInterface javascriptInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_we);
        initView();
    }

    private void initView() {
        llBack= findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        tvTitle= findViewById(R.id.tvTitle);
        tvTitle.setText("关于我们");
        web = findViewById(R.id.web);
        initWebViewData(web, BaseRequesUrl.Aboutwe);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llBack:
                finish();
                break;
        }
    }

    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    private void initWebViewData(WebView webView,String data) {
        javascriptInterface = new JavaScriptInterface(this);
        webSettings = webView.getSettings();
        //是否支持js
        webSettings.setJavaScriptEnabled(true);
        // 设置支持缩放
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBlockNetworkImage(false);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new MyWebViewClient());
        //设置进度条,处理提示框
        //添加监听
        webView.addJavascriptInterface(javascriptInterface, "imagelistner");
        //加载数据
//        webView.loadData(data, "text/html; charset=UTF-8", null);
//        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
//        webView.loadUrl("https://docs.google.com/viewer?url=weixintest.ihk.cn/ihkwx_upload/1.pdf");
        webView.loadUrl(data);

    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner(view);
            imgReset(view);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    /**
     * 循环遍历标签中的图片
     * js 语法
     */
    private void imgReset(WebView webView) {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%';   " +
                "}" +
                "})()");
    }

    // js通信接口
    public static class JavaScriptInterface {
        private Context context;
        public JavaScriptInterface(Context context) {
            this.context = context;
        }

        //点击图片回调方法
        //必须添加注解,否则无法响应
        @JavascriptInterface
        public void openImage(String img) {
            System.out.println(img);

        }
    }

    // 注入js函数监听
    private void addImageClickListner(WebView webView) {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }
}
