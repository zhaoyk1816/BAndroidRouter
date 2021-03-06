package com.github.beansoftapp.android.router.interceptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class JumpInvoker implements Parcelable, Invoker {
    public static final Creator<JumpInvoker> CREATOR = new Creator<JumpInvoker>() {

        public JumpInvoker createFromParcel(Parcel parcel) {
            return new JumpInvoker(parcel);
        }

        public JumpInvoker[] newArray(int i) {
            return new JumpInvoker[i];
        }
    };

    private Bundle bundle;
    private String targetClassName;

    private int requestCode = -1;

    public JumpInvoker(String str, Bundle bundle) {
        this.targetClassName = str;
        if (bundle == null) {
            this.bundle = new Bundle();
        } else {
            this.bundle = bundle;
        }
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    protected JumpInvoker(Parcel parcel) {
        this.targetClassName = parcel.readString();
        this.bundle = parcel.readBundle();
        this.requestCode = parcel.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.targetClassName);
        dest.writeBundle(this.bundle);
        dest.writeInt(this.requestCode);
    }

    public void invoke(Context context) {
        try {
            Intent intent = new Intent(context, Class.forName(this.targetClassName));
            intent.putExtras(this.bundle);

            if (requestCode >= 0) {
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(intent, requestCode);
                } else {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
