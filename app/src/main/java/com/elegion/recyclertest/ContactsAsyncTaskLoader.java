package com.elegion.recyclertest;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class ContactsAsyncTaskLoader extends AsyncTaskLoader<String> {
    private String mId;

    public ContactsAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public String loadInBackground() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            Log.d(ContactsAsyncTaskLoader.class.getSimpleName(), e.getMessage());
        }


        if (mId == null) {
            return null;
        }

        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND "
                        + ContactsContract.CommonDataKinds.Phone.TYPE + " = ?",
                new String[]{mId, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)},
                null);

        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }

        String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

        return number;
    }

    public void setId(String id) {
        mId = id;
    }

    public void forceLoad(String id) {
        setId(id);
        super.forceLoad();
    }
}
