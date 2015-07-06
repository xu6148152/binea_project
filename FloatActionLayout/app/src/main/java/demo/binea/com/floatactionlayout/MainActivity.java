package demo.binea.com.floatactionlayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity implements SkittleBuilder.SkittleClickListener {

    SkittleLayout skittleLayout;
    SkittleBuilder skittleBuilder;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView tvBackground, tvHistory;
    ImageView backdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        skittleLayout = (SkittleLayout) findViewById(R.id.skittleLayout);
        skittleBuilder = new SkittleBuilder(this, skittleLayout, false,
                null, Color.RED);
        skittleBuilder.setSkittleListener(this);
        skittleBuilder.setMainSkittleColor(Color.GREEN);

        tvBackground = (TextView) findViewById(R.id.tvBackground);
        tvHistory = (TextView) findViewById(R.id.tvHistory);
        backdrop = (ImageView) findViewById(R.id.backdrop);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Game of Thrones");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addSkittles();
    }

    private void addSkittles() {

        //For adding normal Skittles
        //        skittleBuilder.addSkittle(R.drawable.lannister_icon, R.color.lannister);
        //        skittleBuilder.addSkittle(R.drawable.barratheon_icon, R.color.barratheon);
        //        skittleBuilder.addSkittle(R.drawable.stark_icon, R.color.stark);

        //Adding Text Skittles
        TextSkittle textSkittle = skittleBuilder.makeTextSkittle
                (R.drawable.lannister_icon, getResources().getString(R.string.house_lannister), R.color.lannister);
        textSkittle.setTextBackgroundColor(R.color.textBackground);
        textSkittle.setTextColor(android.R.color.black);
        skittleBuilder.addTextSkittle(textSkittle);

        textSkittle = skittleBuilder.makeTextSkittle
                (R.drawable.barratheon_icon, getResources().getString(R.string.house_barratheon), R.color.barratheon);
        textSkittle.setTextBackgroundColor(R.color.textBackground);
        textSkittle.setTextColor(android.R.color.black);
        skittleBuilder.addTextSkittle(textSkittle);

        textSkittle = skittleBuilder.makeTextSkittle
                (R.drawable.stark_icon, getResources().getString(R.string.house_stark), R.color.stark);
        textSkittle.setTextBackgroundColor(R.color.textBackground);
        textSkittle.setTextColor(android.R.color.black);
        skittleBuilder.addTextSkittle(textSkittle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSkittleClick(Skittle skittle) {

        switch (skittle.getPosition()) {
            case 1:

                setUpHouse(R.string.lannister_background, R.string.lannister_history,
                        R.string.house_lannister, R.drawable.house_lannister);
                break;
            case 2:

                setUpHouse(R.string.barratheon_background, R.string.barratheon_history,
                        R.string.house_barratheon, R.drawable.house_barratheon);
                break;
            case 3:

                setUpHouse(R.string.stark_background, R.string.stark_history,
                        R.string.house_stark, R.drawable.house_stark);
        }

    }

    @Override
    public void onTextSkittleClick(TextSkittle textSkittle) {

        Snackbar.make(skittleLayout.getSkittleContainer(), "Skittle Pressed", Snackbar.LENGTH_LONG)
                .show();

        switch (textSkittle.getPosition()) {
            case 1:

                setUpHouse(R.string.lannister_background, R.string.lannister_history,
                        R.string.house_lannister, R.drawable.house_lannister);
                break;
            case 2:

                setUpHouse(R.string.barratheon_background, R.string.barratheon_history,
                        R.string.house_barratheon, R.drawable.house_barratheon);
                break;
            case 3:

                setUpHouse(R.string.stark_background, R.string.stark_history,
                        R.string.house_stark, R.drawable.house_stark);
        }

    }

    private void setUpHouse(int background, int history, int title, int coatOfArms) {

        tvBackground.setText(getResources().getString(background));
        tvHistory.setText(getResources().getString(history));
        //        toolbar.setTitle("Changed");
        backdrop.setImageDrawable(getResources().getDrawable(coatOfArms));

    }
}
