package nope.notsdsmt.dgfae.pongbrubakernoah;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {
    private static final int NUM_ITEMS = 3;

    MenuPagerAdapter mAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MenuPagerAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(MenuFragment.GAME);
    }

    public static class MenuPagerAdapter extends FragmentPagerAdapter{
        public MenuPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount(){
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position){
            Fragment frag = new MenuFragment();
            Bundle args = new Bundle();
            args.putInt(MenuFragment.PAGE_IDX, position);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return "PAGE " + (position + 1);
        }
    }

    public static class MenuFragment extends Fragment{
        public static final String PAGE_IDX = "page_idx";
        public static final int
                STATS = 0,
                GAME = 1,
                SETTINGS = 2;
        public static final int[] PAGES = {
                R.layout.main_menu_stats,
                R.layout.main_menu_game,
                R.layout.main_menu_settings,
        };

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState){
            return inflater.inflate(PAGES[getArguments().getInt(PAGE_IDX, 1)],
                    container, false);
        }
    }

    public void gameGotoSettings(View view){
        mPager.setCurrentItem(MenuFragment.SETTINGS);
    }

    public void gameGotoStats(View view){
        mPager.setCurrentItem(MenuFragment.STATS);
    }

    public void startGame(View view){
        Intent intent = new Intent(this, Pong.class);
        startActivity(intent);
    }
}
