package kittentrate.view.custom;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by Manuel Lorenzo on 19/03/2017.
 */

public class AutofitGridLayoutManager extends GridLayoutManager {
    public AutofitGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AutofitGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public AutofitGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }
}
