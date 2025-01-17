package demo.binea.com.fitchart;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import demo.binea.com.fitchart.widget.FitChart;
import demo.binea.com.fitchart.widget.FitChartValue;
import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FitChart fitChart = (FitChart)findViewById(R.id.fitChart);
        fitChart.setMinValue(0f);
        fitChart.setMaxValue(100f);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Resources resources = getResources();
                Collection<FitChartValue> values = new ArrayList<>();
                values.add(new FitChartValue(30f, resources.getColor(R.color.chart_value_1)));
                values.add(new FitChartValue(20f, resources.getColor(R.color.chart_value_2)));
                values.add(new FitChartValue(15f, resources.getColor(R.color.chart_value_3)));
                values.add(new FitChartValue(10f, resources.getColor(R.color.chart_value_4)));
                fitChart.setValues(values);
            }
        });

        findViewById(R.id.add_value).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                fitChart.setValue(30f);
            }
        });
    }
}
