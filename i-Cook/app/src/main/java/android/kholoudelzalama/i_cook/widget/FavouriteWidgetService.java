package android.kholoudelzalama.i_cook.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by win on 18/07/2017.
 */

public class FavouriteWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FavouritesWidgetDataProvider(this, intent);
    }
}
