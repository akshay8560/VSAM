package cryptos.cryptocurrency.vsam.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import cryptos.cryptocurrency.vsam.R;
import cryptos.cryptocurrency.vsam.fragments.AddFragment;
import cryptos.cryptocurrency.vsam.fragments.ChallengeFragment;
import cryptos.cryptocurrency.vsam.fragments.HomeFragment;
import cryptos.cryptocurrency.vsam.fragments.ProfileFragment;
import cryptos.cryptocurrency.vsam.fragments.SearchFragment;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class HomeActivity extends AppCompatActivity {

    private MeowBottomNavigation bottomNavigation;
    private BlurView blurView;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        blurView = (BlurView) findViewById(R.id.blurView);
//
////        init();
//        float radius = 20f;
//
//        View decorView = getWindow().getDecorView();
//        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
//        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
//        //Set drawable to draw in the beginning of each blurred frame (Optional).
//        //Can be used in case your layout has a lot of transparent space and your content
//        //gets kinda lost after after blur is applied.
//        Drawable windowBackground = decorView.getBackground();
//
//        blurView.setupWith(rootView)
//                .setFrameClearDrawable(windowBackground)
//                .setBlurAlgorithm(new RenderScriptBlur(this))
//                .setBlurRadius(radius)
//                .setBlurAutoUpdate(true)
//                .setHasFixedTransformationMatrix(true);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home_black_24dp));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_search));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_add));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_icon));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_user));

        bottomNavigation.show(1 , true);
        replace(new HomeFragment());

        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                switch (model.getId())
                {
                    case 1:
                            replace(new HomeFragment());
                            break;

                    case 2:
                            replace(new SearchFragment());
                            break;

                    case 3:
                            replace(new AddFragment());
                            break;

                    case 4:
                            replace(new ChallengeFragment());
                            break;

                    case 5:
                            replace(new ProfileFragment());
                            break;
                }

                return null;
            }
        });

    }

    private void replace(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();

    }

//    private void init()
//    {
//        RecyclerView storiesBar = findViewById(R.id.recyclerView1);
//        RecyclerView postsBar = findViewById(R.id.recyclerView2);
//
//        List<Story > stories = new ArrayList<>();
//        stories.add(new Story(false));
//        stories.add(new Story(false));
//        stories.add(new Story(true));
//        stories.add(new Story(false));
//        stories.add(new Story(true));
//        stories.add(new Story(false));
//        stories.add(new Story(true));
//        stories.add(new Story(false));
//
//        StoriesAdapter adapter = new StoriesAdapter(stories , this);
//
//        storiesBar.setAdapter(adapter);
//        storiesBar.setLayoutManager(new LinearLayoutManager(this , RecyclerView.HORIZONTAL , false));
//
//        //Setting the space between stories
//        storiesBar.addItemDecoration(new StoriesDecoration(10));
//
//
//        //Posts Adapter
//        List<Data> posts = new ArrayList<>();
//        posts.add(new Data(false));
//        posts.add(new Data(false));
//        posts.add(new Data(false));
//        posts.add(new Data(false));
//        posts.add(new Data(false));
//        posts.add(new Data(false));
//
//        PostsAdapter adapter1 = new PostsAdapter(posts , this);
//
//        postsBar.setAdapter(adapter1);
//        postsBar.setLayoutManager(new LinearLayoutManager(this , RecyclerView.VERTICAL , false));
//
//
//    }
}
