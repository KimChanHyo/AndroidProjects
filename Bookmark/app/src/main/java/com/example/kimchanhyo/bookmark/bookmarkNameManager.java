package com.example.kimchanhyo.bookmark;

import android.content.Context;

import java.io.*;
import java.util.*;

/**
 * Created by Kim Chan Hyo on 2017-11-10.
 */

// 북마크의 이름을 관리하는 클래스
// 북마크 이름들은 BookmarkName.txt 파일에 통합돼서 저장된다.
// 북마크 이름들은 '\n'으로 서로 구분된다.
public class bookmarkNameManager {
    private static final String FILE_NAME = "BookmarkName.txt";
    Context mContext = null;

    bookmarkNameManager(Context _context) { mContext = _context; }

    // BookmarkName.txt 파일이 없는 경우 파일 생성을 위한 메소드
    public void create() {
        try {
            FileOutputStream fos = null;
            fos = mContext.openFileOutput(FILE_NAME, Context.MODE_APPEND);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일에 북마크의 이름을 쓰는 메소드(한개씩 추가, MODE_APPEND)
    public void save(String name) {
        try {
            FileOutputStream fos = null;
            fos = mContext.openFileOutput(FILE_NAME, Context.MODE_APPEND);
            fos.write((name + "\n").getBytes());    // '\n' 은 구분자
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 파일에 북마크의 이름을 쓰는 메소드(전부 등록, MODE_PRIVATE)
    public void save(ArrayList<String> name) {
        try {
            FileOutputStream fos = null;
            fos = mContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);

            // name(ArrayList)에 있는 모든 북마크 이름을 새로 추가
            for(int i = 0 ; i < name.size() ; ++i)
                fos.write((name.get(i) + "\n").getBytes()); // '\n' 은 구분자
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 기존 북마크 이름들을 load
    public ArrayList<String> load() {
        try {
            FileInputStream fis = mContext.openFileInput(FILE_NAME);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            // 구분자('\n')를 이용하여 북마크 이름들을 서로 분리하여 저장
            String[] str = new String(data).split("\\n");
            ArrayList<String> bookmarkName = new ArrayList<String>(Arrays.asList(str));

            return bookmarkName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
