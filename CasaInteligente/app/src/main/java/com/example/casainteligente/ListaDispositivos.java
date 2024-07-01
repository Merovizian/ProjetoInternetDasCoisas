package com.example.casainteligente;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.Set;

public class ListaDispositivos extends ListActivity {
    private BluetoothAdapter bluetoothAdapter;

    static String ENDERECO_MAC = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        ArrayAdapter<String> arrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        bluetoothAdapter = bluetoothAdapter.getDefaultAdapter();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Set<BluetoothDevice> dispositivosPareados = bluetoothAdapter.getBondedDevices();

        if(!dispositivosPareados.isEmpty()){
            for(BluetoothDevice dispositivo : dispositivosPareados){
                String nomeBT = dispositivo.getName();
                String macBT = dispositivo.getAddress();

                if (Objects.equals(nomeBT, "Casa Inteligente")){
                    arrayBluetooth.add(nomeBT + "\n" + macBT);
                }

            }
        }

        setListAdapter(arrayBluetooth);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String informacaoGeral = ((TextView) v).getText().toString();
        String enderecoMAC = informacaoGeral.substring(informacaoGeral.length()-17);
        Toast.makeText(this, enderecoMAC, Toast.LENGTH_SHORT).show();

        Intent retornarMAC = new Intent();
        retornarMAC.putExtra(ENDERECO_MAC, enderecoMAC);
        setResult(RESULT_OK,retornarMAC);
        finish();

    }
}
