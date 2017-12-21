package com.example.ahmed.mybakingapp.Widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.ahmed.mybakingapp.Provider.RecipeContract;
import com.example.ahmed.mybakingapp.R;

public class WidgetListService extends RemoteViewsService {
    public static final String TAG = WidgetListService.class.getSimpleName();
    public static final String EXTRA_WIDGET_RECIPE_ID = "extra_widget_recipe_id";
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "onGetViewFactory: ");
        return new WidgetListAdapter(getApplicationContext(), intent.getIntExtra(EXTRA_WIDGET_RECIPE_ID, 0));
    }

    public class WidgetListAdapter implements RemoteViewsFactory {
        private Context context;
        private int recipeId;
        private Cursor cursor;

        public WidgetListAdapter(Context context, int recipeId) {
            this.context = context;
            this.recipeId = recipeId;
        }

        @Override
        public void onCreate() {
            Log.d(TAG, "onCreate: " + recipeId);
            if (recipeId != 0) {
                cursor = getContentResolver().query(RecipeContract.IngredientEntry.CONTENT_URI,
                        null,
                        RecipeContract.IngredientEntry.COLUMN_RECIPE_ID + "=?",
                        new String[]{Integer.toString(recipeId)},
                        RecipeContract.IngredientEntry._ID);
            }

            Log.d(TAG, "onCreate: " + cursor.getCount());

        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {
            cursor.close();
        }

        @Override
        public int getCount() {
            return (cursor != null) ? cursor.getCount() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {

            if (cursor.moveToPosition(position) ) {
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_ingred_list);
                views.setTextViewText(R.id.ingred, cursor.getString(cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_INGREDIENT)));
//                views.setTextViewText(R.id.ingred_measure, cursor.getString(cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_MEASURE)));
//                views.setTextViewText(R.id.ingred_Quantity, cursor.getInt(cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_QUALITY)) + "");

                return views;
            }
            if (cursor == null || cursor.getCount() == 0) return null;
            return null;
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
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
