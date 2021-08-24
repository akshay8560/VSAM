package cryptos.cryptocurrency.vsam.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import cryptos.cryptocurrency.vsam.R;
import cryptos.cryptocurrency.vsam.models.Data;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder>{

    private List<Data> dataList;
    Context context;
    Window window ;

    public PostsAdapter(List<Data> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new PostsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostsAdapter.PostsViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    //ViewHolder
    public static class PostsViewHolder extends RecyclerView.ViewHolder {

        public PostsViewHolder(@NonNull View itemView)
        {
            super(itemView);
        }
    }
}
;