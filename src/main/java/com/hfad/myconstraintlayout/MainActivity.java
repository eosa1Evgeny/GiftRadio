package com.hfad.myconstraintlayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.myconstraintlayout.data.Radio;
import com.hfad.myconstraintlayout.data.RadioLab;

import java.util.List;

import co.mobiwise.library.RadioListener;
import co.mobiwise.library.RadioManager;

public class MainActivity extends AppCompatActivity implements RadioListener {


    public String mRadioUrl;
    public String TAG = "mMainActivity";
    private WifiManager.WifiLock mWifilock;

    private FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    private RadioAdapter mAdapter;

    private RadioManager mRadioManager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);

        //радиоменеджер
        mRadioManager = RadioManager.with(this);
        mRadioManager.registerListener(this);

        //Блокировка выключения WiFi, без этого приложение завершает проигрывание в фоне
        mWifilock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        mWifilock.acquire();

        mRecyclerView = findViewById(R.id.radio_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRadio();
            }
        });

        updateUI();
    }

    /**
     * Запуск радио
     */

    public void startRadio() {


        try {

            mRadioManager.startRadio(mRadioUrl);


        } catch (Exception e) {
            Log.e(TAG, getLocalClassName() + e.getMessage() + " ***startRadio***");
        }
    }

    /**
     * Остановка радио
     */

    public void stopRadio() {

        if (mRadioManager.isPlaying()) {
            mRadioManager.stopRadio();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mRadioManager != null) {
            mRadioManager.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void updateUI() {

        RadioLab radioLab = RadioLab.get(this);
        List<Radio> radios = radioLab.getRadios();

        if (mAdapter == null) {
            mAdapter = new RadioAdapter(radios);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setRadios(radios);
            mAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onRadioConnected() {

    }

    @Override
    public void onRadioStarted() {

    }

    @Override
    public void onRadioStopped() {

    }

    @Override
    public void onMetaDataReceived(String s, String s1) {

    }


    private class RadioHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {


        public Radio mRadio;
        private TextView mTitleTextView;
        private TextView mTextView;
        public ImageView txtOptionDigit;


        public RadioHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_radio_title_text_view);
            mTextView = (TextView) itemView.findViewById(R.id.list_item_radio_data_text_view);
            txtOptionDigit = (ImageView) itemView.findViewById(R.id.txtOptionDigit);
        }

        public void bindRadio(Radio radio) {

            mRadio = radio;
            mTitleTextView.setText(mRadio.getName());
            mTextView.setText(mRadio.getDate().toString());
        }


        @Override
        public void onClick(View v) {

            mRadioUrl = mRadio.getAddres();

            startRadio();
            Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Играет " + mRadio.getName(), Snackbar.LENGTH_SHORT).show();
        }
    }


    private class RadioAdapter extends RecyclerView.Adapter<RadioHolder> {

        private List<Radio> mRadios;


        public RadioAdapter(List<Radio> radios) {
            mRadios = radios;

        }

        @Override
        public RadioHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View view = layoutInflater.inflate(R.layout.list_item_radio, parent, false);
            return new RadioHolder(view);
        }

        @Override
        public void onBindViewHolder(final RadioHolder holder, final int position) {

            Radio radio = mRadios.get(position);

            holder.txtOptionDigit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(MainActivity.this, holder.txtOptionDigit);
                    popupMenu.inflate(R.menu.option_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.mnu_edit:
                                    editRadio(mRadios.get(position));
                                    break;
                                case R.id.mnu_delete:
                                    alDialog(position);
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

            holder.bindRadio(radio);

        }


        private void alDialog(final int position) {


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    deleteRadio(mRadios.get(position));
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Отмена удаления", Snackbar.LENGTH_SHORT).show();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }


        /**
         * Удаляем запись радио из базы
         *
         * @param radio
         */

        private void deleteRadio(Radio radio) {

            RadioLab.get((MainActivity.this)).deleteRadio(radio);
            updateUI();
        }

        /**
         * Редактируем запись в базе
         *
         * @param radio
         */

        private void editRadio(Radio radio) {

            Intent i;
            i = new Intent(MainActivity.this, AddRadioActivity.class);
            i.putExtra("mainActivityEditId", radio.getId().toString());
            startActivity(i);


            updateUI();
        }


        @Override
        public int getItemCount() {
            return mRadios.size();
        }


        public void setRadios(List<Radio> radios) {
            mRadios = radios;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    Snackbar mySnackbar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(this, "Настройки тост", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.actiton_add:

                Intent i = new Intent(MainActivity.this, AddRadioActivity.class);
                startActivity(i);

                return true;

            case R.id.action_info:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Toast.makeText(this, "О программе", Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mRadioManager.isPlaying()) {

            mRadioManager.stopRadio();

        }

        try {

            mRadioManager.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        //не трогать, будет отключаться при воспроизведении через wifi (проверено на андроид 6)
        mWifilock.acquire();
    }

}

