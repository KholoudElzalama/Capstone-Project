package android.kholoudelzalama.i_cook.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.kholoudelzalama.i_cook.R;
import android.kholoudelzalama.i_cook.objects.Recipe;
import android.kholoudelzalama.i_cook.objects.User;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by win on 18/07/2017.
 */

public class FavouritesWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory, ValueEventListener {

    List<Recipe> recipes;
    Context context;
    Intent intent;
    Query mQuery;

    public FavouritesWidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        recipes = new ArrayList<>();

    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        recipes.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef = database.getReference(context.getString(R.string.fb_users));

        if (mAuth.getCurrentUser() == null) {
            Log.d("Widget", "no user to show data");
        } else {

            String uEmail = mAuth.getCurrentUser().getEmail();
            mQuery = myRef.orderByChild("email").equalTo(uEmail);

            mQuery.addValueEventListener(this);
            Log.d("Widget", "query done " + mQuery.toString());
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        recipes.clear();
    }

    @Override
    public int getCount() {
        int count = 0;
        if (recipes != null) {
            count = recipes.size();
        }
        Log.d("Widget", "value is " + String.valueOf(count));
        return count;
    }

    @Override
    public RemoteViews getViewAt(int i) {


        RemoteViews view = new RemoteViews(context.getPackageName(),
                R.layout.favourites_lv_item);

        view.setTextViewText(R.id.tv_label, recipes.get(i).getLabel());

        try {
            Bitmap myBitmap = Picasso.with(context).load(recipes.get(i).getImage()).get();
            view.setImageViewBitmap(R.id.iv_photo, myBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {

        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        User user = new User();
        Log.d("Widget", "value is " + dataSnapshot.toString());
        user = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
        if (user.getFavourites() != null) {
            Log.d("Widget", "yaaaaaaaaaaaay got data");
            recipes = user.getFavourites();
            Log.d("Widget", String.valueOf(recipes.size()));

        }

        synchronized (this) {
            this.notify();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
