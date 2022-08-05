package com.music.mp3player.audio.mediaplayer.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.role.RoleManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.music.mp3player.audio.mediaplayer.BuildConfig;
import com.music.mp3player.audio.mediaplayer.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import static android.content.Context.ROLE_SERVICE;
import static android.content.Context.TELECOM_SERVICE;

public class AppUtils {

    public static boolean SHOW_OPEN_ADS = true;

    public static boolean isNetworkAvailable(Context c) {
        try {


            if (c != null) {
                ConnectivityManager manager = (ConnectivityManager)
                        c.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                boolean isAvailable = false;
                if (networkInfo != null && networkInfo.isConnected()) {
                    isAvailable = true;
                }
                return isAvailable;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackbar(Activity c, @NonNull String message) {//, View v
        View v = c.findViewById(android.R.id.content);
        if (v != null) {
            Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show();
        } else {
            Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPassword(String string, boolean allowSpecialChars) {
        if (string.matches(".*[a-z].*")) {
            // Do something
            return true;
        } else
            return false;
    }

    // @TargetApi(Build.VERSION_CODES.M)
    public static boolean isDefaultDialer(Context context) {
        try {


            if (!context.getPackageName().startsWith(BuildConfig.APPLICATION_ID)) {
                return true;
            } else if (context.getPackageName().startsWith(BuildConfig.APPLICATION_ID) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                RoleManager roleManager = (RoleManager) context.getSystemService(ROLE_SERVICE);
                return roleManager.isRoleAvailable(RoleManager.ROLE_DIALER) && roleManager.isRoleHeld(RoleManager.ROLE_DIALER);
            } else {
                try {
                    TelecomManager telecomManager = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        telecomManager = (TelecomManager) context.getSystemService(TELECOM_SERVICE);
                    }
                    return isMarshmallowPlus() && telecomManager.getDefaultDialerPackage().equals(context.getPackageName());
                } catch (Exception e) {
                    return false;
                }


            }
        } catch (Exception e) {

        }
        return false;
    }

    public static boolean isReadContact(Context context) {
        boolean permission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                permission = false;
            } else {
                permission = true;
            }
        }
        return permission;
    }

    public static boolean isWriteContact(Context context) {
        boolean permission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                permission = false;
            } else {
                permission = true;
            }
        }
        return permission;
    }

    public static boolean isReadCallLog(Context context) {
        boolean permission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                permission = false;
            } else {
                permission = true;
            }
        }
        return permission;
    }

    public static boolean isWriteCallLog(Context context) {
        boolean permission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                permission = false;
            } else {
                permission = true;
            }
        }
        return permission;
    }

    public static boolean isReadPhoneState(Context context) {
        boolean permission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permission = false;
            } else {
                permission = true;
            }
        }
        return permission;
    }

    public static boolean isReadExternalStorage(Context context) {
        boolean permission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permission = false;
            } else {
                permission = true;
            }
        }
        return permission;
    }

    public static boolean isWriteExternalStorage(Context context) {
        boolean permission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permission = false;
            } else {
                permission = true;
            }
        }
        return permission;
    }

    public static boolean isRecordAudio(Context context) {
        boolean permission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permission = false;
            } else {
                permission = true;
            }
        }
        return permission;
    }

    public static boolean isPhoneCall(Context context) {
        boolean permission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                permission = false;
            } else {
                permission = true;
            }
        }
        return permission;
    }

    public static boolean isMarshmallowPlus() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isNougatPlus() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean isNougatMR1Plus() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1;
    }

    public static boolean isOreoPlus() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static boolean isOreoMr1Plus() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1;
    }

    public static boolean isPiePlus() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    public static boolean isQPlus() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

   /* public static boolean isRPlus() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }*/


    public static int getCharKeyCode(Character character) {
        switch (character) {
            case '0':
                return KeyEvent.KEYCODE_0;
            case '1':
                return KeyEvent.KEYCODE_1;
            case '2':
                return KeyEvent.KEYCODE_2;
            case '3':
                return KeyEvent.KEYCODE_3;
            case '4':
                return KeyEvent.KEYCODE_4;
            case '5':
                return KeyEvent.KEYCODE_5;
            case '6':
                return KeyEvent.KEYCODE_6;
            case '7':
                return KeyEvent.KEYCODE_7;
            case '8':
                return KeyEvent.KEYCODE_8;
            case '9':
                return KeyEvent.KEYCODE_9;
            case '*':
                return KeyEvent.KEYCODE_STAR;
            case '+':
                return KeyEvent.KEYCODE_PLUS;
            default:
                return KeyEvent.KEYCODE_POUND;

        }
    }

    public static void copyClipBoard(Context context, String number) {
        ClipData clip = ClipData.newPlainText(context.getResources().getString(R.string.app_name), number);
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(clip);
        showToast(context, "Copied.");
    }


    public static boolean isPackageInstalled(Context mContext, String packageName) {
        PackageManager pm = mContext.getPackageManager();

        try {
            pm.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void shareDetails(Context mContext, String txt, String imagepath) {
//        Uri uri = Uri.parse("file://"+imagepath);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_SUBJECT, "Share Contact Details");
        share.putExtra(Intent.EXTRA_TEXT, txt);
        if (!imagepath.equalsIgnoreCase("null")) {
            Uri uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", new File(imagepath));
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.setType("image/*");
        } else {
            share.setType("text/*");
        }
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.startActivity(Intent.createChooser(share, "Share Detail via..."));
    }

    public static void displayErrorDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error");
        builder.setMessage("Error Dialog");
        builder.setPositiveButton("Ok Dialog",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.create().show();
    }

    public static void sendEmail(Context mContext, String email, String text, String imagepath) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse("mailto:" + Uri.encode(email)));

//        Uri uri = Uri.parse("file://"+imagepath);

        if (!imagepath.equalsIgnoreCase("null")) {
            Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", new File(imagepath));
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            emailIntent.setType("image/*");
        } else {
            emailIntent.setType("text/*");
        }
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "" + text);


        try {
            mContext.startActivity(Intent.createChooser(emailIntent, "Send email via..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext,
                    "There are no email clients installed.", Toast.LENGTH_SHORT)
                    .show();
        }
    }

}
