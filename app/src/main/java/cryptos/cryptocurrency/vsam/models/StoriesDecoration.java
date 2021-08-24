package cryptos.cryptocurrency.vsam.models;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class StoriesDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public StoriesDecoration(int space)
    {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        outRect.right = space;
        outRect.left = space;
    }
}
