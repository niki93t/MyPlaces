package rs.elfak.mosis.nikola.myplaces;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Nikola Adzic on 6/1/2016.
 */
public class MyPlacesServerList extends Dialog {
    ListView lv;
    private Handler guiThread;
    private ExecutorService transTread;
    Context context;
    private ProgressDialog progressDialog;

    public MyPlacesServerList(Context context) {
        super(context);
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.myplaces_serverlist);
        setContentView(R.layout.serverlist);
        lv = (ListView)findViewById(R.id.serverlist);
        registerForContextMenu(lv);
        lv.setOnCreateContextMenuListener(this);
        Button btnDone = (Button)findViewById(R.id.serverlist_done_button);
        btnDone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MyPlacesServerList.this.dismiss();
            }
        });
        guiThread = new Handler();
        transTread = Executors.newSingleThreadExecutor();
        transTread.submit(new Runnable() {
            @Override
            public void run() {
                guiShowProgressDialog("Contacting server", "Getting available places");
                try{
                    final List<String> names = MyPlacesHTTPHelper.getAllPlacesNames();
                    guiSetAdapter(names);
                }catch (Exception e){
                    e.printStackTrace();
                }
                guiDismissProgressDialog();
            }
        });
    }

    private void guiSetAdapter(final List<String> names){
        guiThread.post(new Runnable() {
            @Override
            public void run() {
                lv.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, names));
            }
        });
    }

    private void guiShowProgressDialog(final String title, final String message){
        guiThread.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.setTitle(title);
                progressDialog.setMessage(message);
                progressDialog.show();
            }
        });
    }

    private void guiDismissProgressDialog(){
        guiThread.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        String str = (String)lv.getItemAtPosition(info.position);
        menu.setHeaderTitle(str);
        menu.add(0, 1, 1, "Download " + str);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final String itemName = (String)lv.getItemAtPosition(info.position);
        transTread = Executors.newSingleThreadExecutor();
        transTread.submit(new Runnable() {
            @Override
            public void run() {
                guiShowProgressDialog("Downloading From Server", "Downloading " + itemName);
                try{
                    final MyPlace place = MyPlacesHTTPHelper.getMyPlace(itemName);
                    guiAddPlace(place);
                }catch (Exception e){
                    e.printStackTrace();
                }
                guiDismissProgressDialog();
            }
        });
        return super.onMenuItemSelected(featureId, item);
    }

    private void guiAddPlace(final MyPlace place) {
        guiThread.post(new Runnable() {
            @Override
            public void run() {
                if(place == null){
                    Toast.makeText(context, "Error during download", Toast.LENGTH_SHORT).show();
                }else{
                    MyPlacesData.getInstance().addNewPlace(place);
                    Toast.makeText(context, "Downloaded place with name: " + place.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
