package rs.elfak.mosis.nikola.myplaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewMyPlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int position = -1;
        try
        {
            Intent listIntent = getIntent();
            Bundle positionBundle = listIntent.getExtras();
            position = positionBundle.getInt("position");
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
        if(position >= 0)
        {
            MyPlace place = MyPlacesData.getInstance().getPlace(position);
            TextView tvName = (TextView)findViewById(R.id.viewmyplace_name_text);
            tvName.setText(place.getName());
            TextView tvDesc = (TextView)findViewById(R.id.viewmyplace_desc_text);
            tvDesc.setText(place.getDescription());
            TextView tvLat = (TextView)findViewById(R.id.viewmyplace_lat_text);
            tvLat.setText(place.getLatitude());
            TextView tvLon = (TextView)findViewById(R.id.viewmyplace_lon_text);
            tvLon.setText(place.getLongitude());
        }
        final Button finishedButton = (Button)findViewById(R.id.viewmyplace_finished_button);
        finishedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.show_map_item){
            //Toast.makeText(this, "Show Map!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MyPlacesMapActivity.class);
            i.putExtra("state", MyPlacesMapActivity.SHOW_MAP);
            startActivity(i);
        } else if(id == R.id.my_places_list_item){
            //Toast.makeText(this, "New Place!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MyPlacesList.class);
            startActivity(i);
        } else if(id == R.id.about_item){
            //Toast.makeText(this, "About!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, About.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_my_place, menu);
        return true;
    }
}
