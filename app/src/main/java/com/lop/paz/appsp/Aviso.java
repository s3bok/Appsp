package com.lop.paz.appsp;

/**
 * Created by pali on 12/06/16.
 */
public class Aviso {

    private int mId;
    private String mContet;
    private int mImportant;

    public Aviso(int mId, String mContet, int mImportant) {
        this.mId = mId;
        this.mContet = mContet;
        this.mImportant = mImportant;
    }


    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmContet() {
        return mContet;
    }

    public void setmContet(String mContet) {
        this.mContet = mContet;
    }

    public int getmImportant() {
        return mImportant;
    }

    public void setmImportant(int mImportant) {
        this.mImportant = mImportant;
    }
}
