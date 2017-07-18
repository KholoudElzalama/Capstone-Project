package android.kholoudelzalama.i_cook.objects;

/**
 * Created by win on 10/07/2017.
 */

public class Params {
    private String[] to;

    private String[] sane;

    private String[] q;

    private String[] calories;

    private String[] app_key;

    private String[] app_id;

    private String[] from;

    private String[] health;

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getSane() {
        return sane;
    }

    public void setSane(String[] sane) {
        this.sane = sane;
    }

    public String[] getQ() {
        return q;
    }

    public void setQ(String[] q) {
        this.q = q;
    }

    public String[] getCalories() {
        return calories;
    }

    public void setCalories(String[] calories) {
        this.calories = calories;
    }

    public String[] getApp_key() {
        return app_key;
    }

    public void setApp_key(String[] app_key) {
        this.app_key = app_key;
    }

    public String[] getApp_id() {
        return app_id;
    }

    public void setApp_id(String[] app_id) {
        this.app_id = app_id;
    }

    public String[] getFrom() {
        return from;
    }

    public void setFrom(String[] from) {
        this.from = from;
    }

    public String[] getHealth() {
        return health;
    }

    public void setHealth(String[] health) {
        this.health = health;
    }

    @Override
    public String toString() {
        return "ClassPojo [to = " + to + ", sane = " + sane + ", q = " + q + ", calories = " + calories + ", app_key = " + app_key + ", app_id = " + app_id + ", from = " + from + ", health = " + health + "]";
    }

}
