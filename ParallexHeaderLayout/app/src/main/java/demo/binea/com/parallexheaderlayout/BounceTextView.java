package demo.binea.com.parallexheaderlayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import demo.binea.com.parallexheaderlayout.BounceScroller.State;

public class BounceTextView extends Activity {

	private BounceScroller scroller;
	private TextView tvInfo;
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
		}
	};

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.bounce_textview);

		tvInfo = (TextView) findViewById(R.id.tv_info);
		tvInfo.setOnClickListener(onClickListener);

		scroller = new BounceScroller(this);
		scroller.setListener(bl).enableHeader(true).enableFooter(true);
		setHeaderView();
		// setFooterView();
		scroller.attach(tvInfo);
	}

	public void setHeaderView() {
		ImageView header = new ImageView(BounceTextView.this);
		header.setImageResource(R.drawable.image4);
		header.setScaleType(ScaleType.FIT_XY);
		scroller.setHeaderView(header);
	}

	public void setFooterView() {
		TextView footer = new TextView(BounceTextView.this);
		footer.setPadding(10, 20, 10, 20);
		footer.setText("Pullable Footer View");
		footer.setBackgroundColor(getResources().getColor(R.color.grey_3));
		footer.setTextColor(getResources().getColor(R.color.white));
		footer.setGravity(Gravity.CENTER);
		scroller.setFooterView(footer);
	}

	private BounceListener bl = new BounceListener() {
		@Override
		public void onState(boolean header, State state) {
			if (state == State.STATE_FIT_EXTRAS) {
				scroller.postDelayed(new Runnable() {
					@Override
					public void run() {
						scroller.resetState();
					}
				}, 1200);
			}
		}

		@Override
		public void onOffset(boolean header, int offset) {
		}
	};
}
