package com.example.kimchanhyo.bookmark;

import android.content.Context;

import java.io.*;

/**
 * Created by Kim Chan Hyo on 2017-11-10.
 */

public class URL_Manager {
    private String fileName;
    Context mContext = null;

    URL_Manager(String _fileName, Context _context) { fileName = _fileName; mContext = _context;}

    // 북마크의 url을 fileName.txt 파일에 저장
    // 파일 이름은 북마크 이름으로 설정
    public void save(String url) {
        if(url == null || url.isEmpty())
            return;

        try {
            FileOutputStream fos = null;
            fos = mContext.openFileOutput(fileName + ".txt", Context.MODE_PRIVATE);
            fos.write(url.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 북마크 url load
    public String load() {
        try {
            FileInputStream fis = mContext.openFileInput(fileName + ".txt");
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            String str = new String(data);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete() { mContext.deleteFile(fileName); }
}
