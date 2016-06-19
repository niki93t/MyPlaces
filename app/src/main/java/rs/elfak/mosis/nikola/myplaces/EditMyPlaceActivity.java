package rs.elfak.mosis.nikola.myplaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditMyPlaceActivity extends AppCompatActivity implements View.OnClickListener{

    boolean editMode = true;
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_place);
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

        try
        {
            Intent listIntent = getIntent();
            Bundle positionBundle = listIntent.getExtras();
            if(positionBundle != null)
                position = positionBundle.getInt("position");
            else
                editMode = false;
        } catch (Exception e){
            editMode = false;
        }
        final Button finishedButton = (Button)findViewById(R.id.editmyplace_finished_button);
        finishedButton.setOnClickListener(this);
        Button cancelButton = (Button)findViewById(R.id.editmyplace_cancel_button);
        cancelButton.setOnClickListener(this);
        EditText nameEditText = (EditText)findViewById(R.id.editmyplace_name_edit);

        if(!editMode)
        {
            finishedButton.setEnabled(false);
            finishedButton.setText("Add");
        }
        else if (position >= 0)
        {
            finishedButton.setText("Save");
            MyPlace place = MyPlacesData.getInstance().getPlace(position);
            nameEditText.setText(place.getName());
            EditText descEditText = (EditText)findViewById(R.id.editmyplace_desc_edit);
            descEditText.setText(place.getDescription());
            EditText latEditText = (EditText)findViewById(R.id.editmyplace_lat_edit);
            latEditText.setText(place.getLatitude());
            EditText lonEditText = (EditText)findViewById(R.id.editmyplace_lon_edit);
            lonEditText.setText(place.getLongitude());
        }

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                finishedButton.setEnabled(s.length() > 0);
            }
        });

        Button locationButton = (Button)findViewById(R.id.editmyplace_location_button);
        locationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.editmyplace_finished_button:
                EditText etName = (EditText)findViewById(R.id.editmyplace_name_edit);
                String nme = etName.getText().toString();
                EditText etDesc = (EditText)findViewById(R.id.editmyplace_desc_edit);
                String desc = etDesc.getText().toString();
                EditText etLat = (EditText)findViewById(R.id.editmyplace_lat_edit);
                String lat = etLat.getText().toString();
                EditText etLon = (EditText)findViewById(R.id.editmyplace_lon_edit);
                String lon = etLon.getText().toString();

                if(!editMode)
                {
                    MyPlace place = new MyPlace(nme, desc);
                    place.setLatitude(lat);
                    place.setLongitude(lon);
                    MyPlacesData.getInstance().addNewPlace(place);
                }
                else
                {
                    MyPlace place = MyPlacesData.getInstance().getPlace(position);
                    place.setName(nme);
                    place.setDescription(desc);
                    place.setLatitude(lat);
                    place.setLongitude(lon);
                    MyPlacesData.getInstance().updatePlace(place);
                }

                setResult(Activity.RESULT_OK);
                finish();
                break;
            case R.id.editmyplace_cancel_button:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case R.id.editmyplace_location_button:
                Intent i = new Intent(this, MyPlacesMapActivity.class);
                i.putExtra("state", MyPlacesMapActivity.SELECT_COORDINATES);
                startActivityForResult(i,1);
                break;
        }
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
        getMenuInflater().inflate(R.menu.menu_edit_my_place,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String lon = data.getExtras().getString("lon");
                EditText lonText = (EditText)findViewById(R.id.editmyplace_lon_edit);
                lonText.setText(lon);
                String lat = data.getExtras().getString("lat");
                EditText latText = (EditText)findViewById(R.id.editmyplace_lat_edit);
                latText.setText(lat);
            }
        }
        catch (Exception e)
        {
            //TODO: handle exception
        }
    }
}
