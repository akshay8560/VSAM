package cryptos.cryptocurrency.vsam.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cryptos.cryptocurrency.vsam.R;
import cryptos.cryptocurrency.vsam.Adapters.PostsAdapter;
import cryptos.cryptocurrency.vsam.Adapters.StoriesAdapter;
import cryptos.cryptocurrency.vsam.models.Data;
import cryptos.cryptocurrency.vsam.models.StoriesDecoration;
import cryptos.cryptocurrency.vsam.models.Story;

public class HomeFragment extends Fragment {

    RecyclerView storiesBar;
    RecyclerView postsBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        storiesBar = view.findViewById(R.id.recyclerView1);
        postsBar = view.findViewById(R.id.recyclerView2);
        init();

        return view;
    }

    private void init()
    {
        List<Story> stories = new ArrayList<>();
        stories.add(new Story(false));
        stories.add(new Story(false));
        stories.add(new Story(true));
        stories.add(new Story(false));
        stories.add(new Story(true));
        stories.add(new Story(false));
        stories.add(new Story(true));
        stories.add(new Story(false));

        StoriesAdapter adapter = new StoriesAdapter(stories , getActivity());

        storiesBar.setAdapter(adapter);
        storiesBar.setLayoutManager(new LinearLayoutManager(getActivity() , RecyclerView.HORIZONTAL , false));

        //Setting the space between stories
        storiesBar.addItemDecoration(new StoriesDecoration(10));


        //Posts Adapter
        List<Data> posts = new ArrayList<>();
        posts.add(new Data(false));
        posts.add(new Data(false));
        posts.add(new Data(false));
        posts.add(new Data(false));
        posts.add(new Data(false));
        posts.add(new Data(false));

        PostsAdapter adapter1 = new PostsAdapter(posts , getActivity());

        postsBar.setAdapter(adapter1);
        postsBar.setLayoutManager(new LinearLayoutManager(getActivity() , RecyclerView.VERTICAL , false));


    }
}