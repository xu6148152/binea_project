package com.zepp.www.sample.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zepp.www.sample.R;
import com.zepp.www.stickyheaderrecyclerview.SectionAdapter;
import com.zepp.www.stickyheaderrecyclerview.StickyHeaderLayoutManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    TabLayout tabs;
    ViewPager viewPager;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MainActivityViewsPager(getSupportFragmentManager()));
        tabs.setupWithViewPager(viewPager);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class MainActivityViewsPager extends FragmentPagerAdapter {

        public MainActivityViewsPager(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainPageFragment();
                case 1:
                    return new AboutPageFragment();
                default:
                    return null;
            }
        }

        @Override public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.activity_main_pager_main);
                case 1:
                    return getString(R.string.activity_main_pager_about);
                default:
                    return null;
            }
        }

        @Override public int getCount() {
            return 2;
        }
    }

    public static class MainPageFragment extends Fragment {

        RecyclerView recyclerView;

        @Nullable @Override public View onCreateView(
                LayoutInflater inflater, @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_main, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            setupDemoRecyclerView();
            return view;
        }

        void setupDemoRecyclerView() {
            DemoModel[] demos = {
                    new DemoModel(getString(R.string.demo_list_item_addressbook_title),
                                  getString(R.string.demo_list_item_addressbook_description),
                                  AddressBookDemoActivity.class),

                    new DemoModel(getString(R.string.demo_list_item_callbacks_title),
                                  getString(R.string.demo_list_item_callbacks_description),
                                  HeaderCallbacksDemoActivity.class),

                    new DemoModel(getString(R.string.demo_list_item_sections_title),
                                  getString(R.string.demo_list_item_sections_description),
                                  SectioningAdapterDemoActivity.class)
            };

            recyclerView.setAdapter(new DemoAdapter(getContext(), demos, new ItemClickListener() {
                @Override public void onItemClick(DemoModel demoModel) {
                    startActivity(new Intent(getActivity(), demoModel.activityClass));
                }
            }));
            recyclerView.setLayoutManager(new StickyHeaderLayoutManager());
        }

        private static class DemoModel {
            String title;
            String description;
            Class activityClass;

            public DemoModel(String title, String description, Class activityClass) {
                this.title = title;
                this.description = description;
                this.activityClass = activityClass;
            }
        }

        private interface ItemClickListener {
            void onItemClick(DemoModel demoModel);
        }

        private static class DemoAdapter extends SectionAdapter {

            public class HeaderViewHolder extends SectionAdapter.HeaderViewHolder {
                TextView titleTextView;

                public HeaderViewHolder(View itemView) {
                    super(itemView);
                    titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
                }
            }

            public class ItemViewHolder extends SectionAdapter.ItemViewHolder {
                TextView titleTextView;
                TextView descriptionTextView;

                public ItemViewHolder(View itemView) {
                    super(itemView);
                    titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
                    descriptionTextView =
                            (TextView) itemView.findViewById(R.id.descriptionTextView);
                }
            }

            Context context;
            DemoModel[] demos;
            ItemClickListener itemClickListener;

            public DemoAdapter(
                    Context context, DemoModel[] demos, ItemClickListener itemClickListener) {
                this.context = context;
                this.demos = demos;
                this.itemClickListener = itemClickListener;
            }

            @Override public int getNumberOfSections() {
                return 1;
            }

            @Override public int getNumberOfItemsInSection(int sectionIndex) {
                return demos.length;
            }

            @Override public boolean doesSectionHaveHeader(int sectionIndex) {
                return true;
            }

            @Override
            public SectionAdapter.HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View v = inflater.inflate(R.layout.list_item_demo_header, parent, false);
                return new HeaderViewHolder(v);
            }

            @Override
            public SectionAdapter.ItemViewHolder onCreateItemViewHolder(ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View v = inflater.inflate(R.layout.list_item_demo_item, parent, false);
                return new ItemViewHolder(v);
            }

            @Override public void onBindHeaderViewHolder(
                    SectionAdapter.HeaderViewHolder viewHolder, int sectionIndex) {
                HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
                hvh.titleTextView.setText(context.getString(R.string.main_demo_list_title));
            }

            @Override public void onBindItemViewHolder(
                    SectionAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex) {
                ItemViewHolder ivh = (ItemViewHolder) viewHolder;

                final DemoModel dm = demos[itemIndex];
                ivh.titleTextView.setText(dm.title);
                ivh.descriptionTextView.setText(dm.description);

                ivh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        itemClickListener.onItemClick(dm);
                    }
                });
            }
        }
    }

    public static class AboutPageFragment extends Fragment {
        @Nullable @Override public View onCreateView(
                LayoutInflater inflater, @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_about, container, false);
        }
    }
}
