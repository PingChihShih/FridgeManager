package cool.project.fridgemanager;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Item {

    /* Field */
    Context context = null;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    int id;
    String name = "";
    Uri uri;
    String tag = "";
    Date putdate;
    Date duedate;

    /* Method */

    public Item(Context c) {
        context = c;
        uri = getUriToDrawable(context, R.drawable.img_not_found);
    }

    public void setId(String stringid){
        id = Integer.parseInt(stringid);
    }

    public void setValues(String n, Uri u, String t, Date pd, Date dd){
        name = n;
        uri = u;
        tag = t;
        putdate = pd;
        duedate = dd;
    }

    public void setValues(String n, String u, String t, String pd, String dd){
        name = n;

        // TODO: update here after
        if(u == null)
            uri = getUriToDrawable(context, R.drawable.img_not_found);
        else
            uri = Uri.parse(u);
        tag = t;
        try {
            if(pd == null)
                putdate = null;
            else
                putdate = format.parse(pd);
            if(dd == null)
                duedate = null;
            else
                duedate = format.parse(dd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setValues(String n, Integer rid, String t, Date pd, Date dd){
        name = n;
        uri = getUriToDrawable(context, rid);
        tag = t;
        putdate = pd;
        duedate = dd;
    }

    public String getDateStr() {
        Calendar cal = Calendar.getInstance();
        if(putdate == null)
            return "unknown";
        cal.setTime(putdate);
        String ret = Integer.toString(cal.get(Calendar.YEAR)) + "-"
                + Integer.toString(cal.get(Calendar.MONTH)) + "-"
                + Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        return ret;
    }

    public String getRemainedDays(){
        if(duedate == null)
            return "? days";
        Date now = new Date(System.currentTimeMillis());
        int days = (int) getDateDiff(duedate, now, TimeUnit.DAYS);

        String tmp = "";

        switch (days){
            case 1:
                return "1 day";
            case 0:
                return "today";
            case -1:
                return "-1 day";
            default:
                return days + " days";
        }
    }

    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public final Uri getUriToDrawable(@NonNull Context context, @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }
}
