package demo.binea.com.recyclerviewandtoolbar;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import demo.binea.com.recyclerviewandtoolbar.adapter.RecyclerAdapter;
import demo.binea.com.recyclerviewandtoolbar.util.Utils;

/**
 * Created by xubinggui on 15/3/2.
 */
public class GooglePlayUIActivity extends ActionBarActivity {

	private LinearLayout mToolbarContainer;
	private int mToolbarHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppThemeGreen);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_play_main);

		mToolbarContainer = (LinearLayout) findViewById(R.id.toolbarContainer);
		initToolbar();
		initRecyclerView();
	}

	private void initToolbar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		setTitle(getString(R.string.app_name));
		mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
		mToolbarHeight = Utils.getToolbarHeight(this);
	}

	private void initRecyclerView() {
		final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

		int paddingTop = Utils.getToolbarHeight(this) + Utils.getTabsHeight(this);
		recyclerView.setPadding(recyclerView.getPaddingLeft(), paddingTop, recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());

		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		RecyclerAdapter recyclerAdapter = new RecyclerAdapter(createItemList());
		recyclerView.setAdapter(recyclerAdapter);

		recyclerView.setOnScrollListener(new HidingScrollListener(this) {

			@Override
			public void onMoved(int distance) {
				mToolbarContainer.setTranslationY(-distance);
			}

			@Override
			public void onShow() {
				mToolbarContainer.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
			}

			@Override
			public void onHide() {
				mToolbarContainer.animate().translationY(-mToolbarHeight).setInterpolator(new AccelerateInterpolator(2)).start();
			}

		});
	}

	private List<String> createItemList() {
		List<String> itemList = new ArrayList<>();
		for(int i=0;i<20;i++) {
			itemList.add("Item "+i);
		}
		return itemList;
	}
}