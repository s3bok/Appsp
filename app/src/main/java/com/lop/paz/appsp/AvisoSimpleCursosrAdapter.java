package com.lop.paz.appsp;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

/**
 * Created by pali on 12/06/16.
 */
public class AvisoSimpleCursosrAdapter extends SimpleCursorAdapter {

    public AvisoSimpleCursosrAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }
}
