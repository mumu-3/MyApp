package com.example.jony.myapp.reader_APP.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;

import com.example.jony.myapp.main.BaseApplication;
import com.example.jony.myapp.main.DebugUtils;
import com.example.jony.myapp.R;
import com.example.jony.myapp.reader_APP.db.database.DatabaseHelper;
import com.example.jony.myapp.reader_APP.db.database.table.DailyTable;
import com.example.jony.myapp.reader_APP.db.database.table.NewsTable;
import com.example.jony.myapp.reader_APP.db.database.table.ReadingTable;
import com.example.jony.myapp.reader_APP.db.database.table.ScienceTable;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Jony on 2016/6/15.
 */
public class Utils {

    private static Context mContext = BaseApplication.AppContext;


    public static InputStream readFileFromRaw(int fileID) {
        return mContext.getResources()
                .openRawResource(fileID);
    }

    public static Document getDocmentByIS(InputStream is) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            doc = builder.parse(is);
            is.close();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static boolean hasString(String str) {
        if (str == null || str.equals("")) return false;
        return true;
    }


    public static String RegexFind(String regex, String string) {
        return RegexFind(regex, string, 1, 1);
    }

    public static String RegexFind(String regex, String string, int start, int end) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        String res = string;
        while (matcher.find()) {
            res = matcher.group();
        }
        return res.substring(start, res.length() - end);
    }


    public static String RegexReplace(String regex, String string, String replace) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.replaceAll(replace);
    }

    // 判断当前的语言环境
    public static int getCurrentLanguage() {
        int lang = Settings.getInstance().getInt(Settings.LANGUAGE, -1);
        if (lang == -1) {
            String language = Locale.getDefault().getLanguage();
            String country = Locale.getDefault().getCountry();

            if (language.equalsIgnoreCase("zh")) {
                if (country.equalsIgnoreCase("CN")) {
                    lang = 1;
                } else {
                    lang = 2;
                }
            } else {
                lang = 0;
            }
        }
        return lang;
    }

    // Must be called before setContentView()
    public static void changeLanguage(Context context, int lang) {
        String language = null;
        String country = null;

        switch (lang) {
            case 1:
                language = "zh";
                country = "CN";
                break;
            default:
                language = "en";
                country = "US";
                break;
        }

        Locale locale = new Locale(language, country);
        Configuration conf = context.getResources().getConfiguration();
        conf.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());
    }


    /**
     * 清除 WebView 和 数据库 中缓存的内容
     */
    public static void clearCache() {

        WebView wb = new WebView(mContext);
        wb.clearCache(true);

        DatabaseHelper mHelper = DatabaseHelper.instance(mContext);
        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.execSQL(mHelper.DELETE_TABLE_DATA + DailyTable.NAME);
        //  db.execSQL(DailyTable.CREATE_TABLE);

        db.execSQL(mHelper.DELETE_TABLE_DATA + NewsTable.NAME);
        //  db.execSQL(NewsTable.CREATE_TABLE);

        db.execSQL(mHelper.DELETE_TABLE_DATA + ReadingTable.NAME);
        //  db.execSQL(ReadingTable.CREATE_TABLE);

        db.execSQL(mHelper.DELETE_TABLE_DATA + ScienceTable.NAME);
        //  db.execSQL(ScienceTable.CREATE_TABLE);

    }

    /**
     * 获得当前系统的亮度值： 0~255
     */
    /**
     * 可调节的最大亮度值
     */
    public static final int MAX_BRIGHTNESS = 255;

    public static int getSysScreenBrightness() {
        int screenBrightness = MAX_BRIGHTNESS;
        try {
            screenBrightness = android.provider.Settings.System.getInt(mContext.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            DebugUtils.DLog("获得当前系统的亮度值失败：");
        }
        return screenBrightness;
    }

    /**
     * 设置当前系统的亮度值:0~255
     */
    public static void setSysScreenBrightness(int brightness) {
        try {
            ContentResolver resolver = mContext.getContentResolver();
            Uri uri = android.provider.Settings.System.getUriFor(android.provider.Settings.System.SCREEN_BRIGHTNESS);
            android.provider.Settings.System.putInt(resolver, android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
            resolver.notifyChange(uri, null); // 实时通知改变
        } catch (Exception e) {
            DebugUtils.DLog("设置当前系统的亮度值失败：" + e);
        }
    }

    /**
     * 剪切板
     */
    public static void copyToClipboard(View view, String info) {
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(mContext.CLIPBOARD_SERVICE);
        ClipData cd = ClipData.newPlainText("msg", info);
        cm.setPrimaryClip(cd);
        Snackbar.make(view, R.string.reader_notif_info_copied, Snackbar.LENGTH_SHORT).show();
    }

    public static void showKeyboard(Context context) {
        InputMethodManager inputManager = (InputMethodManager)context.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
        }

    }

    public static void closeKeyBoard(Context context){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //得到InputMethodManager的实例
        if (imm.isActive()) {
            //如果开启
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

}