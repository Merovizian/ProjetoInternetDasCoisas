package com.example.casainteligente;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    public static int ATIVA_BLUETOOTH = 1;
    private static final int SOLICITA_ATIVACAO = 1;
    private static final int SOLICITA_CONEXAO = 2;


    public Button LedAzul, LedVermelho, LedVerde, LED;
    boolean azul = false, vermelho = false, verde = false, amarelo = false;
    public TextView valorBrilho, valorLDR, texMac;
    public ImageView bluetoothIcon;

    BluetoothAdapter meuBluetoothAdapter;
    BluetoothDevice meuDeviceBT;
    BluetoothSocket meusocketBT;
    UUID uuidBT = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    boolean conexao = false;

    private static String MAC = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        LedAzul = findViewById(R.id.ledBlue);
        LedAzul.setAlpha(0.3f);
        LedVerde = findViewById(R.id.ledGreen);
        LedVerde.setAlpha(0.3f);
        LedVermelho = findViewById(R.id.ledRed);
        LedVermelho.setAlpha(0.3f);

        LED = findViewById(R.id.ledFlash);
        LED.setAlpha(0.3f);


        valorBrilho = findViewById(R.id.text_ValorBrilhoLED);
        valorBrilho.setVisibility(View.INVISIBLE);

        texMac = findViewById(R.id.textView_MAC);
        texMac.setVisibility(View.INVISIBLE);

        valorLDR = findViewById(R.id.text_ValorLDR);
        valorLDR.setVisibility(View.INVISIBLE);


        bluetoothIcon = findViewById(R.id.imageView_BT);
        bluetoothIcon.setImageResource(R.drawable.ic_bluetooth_of_foreground);


        meuBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (meuBluetoothAdapter == null) {
            Toast.makeText(this, "Verifique seu Bluetooth", Toast.LENGTH_SHORT).show();
        } else if (!meuBluetoothAdapter.isEnabled()) {
            Intent ativaBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

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
            startActivityForResult(ativaBluetooth, ATIVA_BLUETOOTH);

        }

        bluetoothIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conexao) {
                    try {
                        meusocketBT.close();
                        conexao = false;
                        Toast.makeText(MainActivity.this, "Bluetooth Desconectado", Toast.LENGTH_SHORT).show();
                        bluetoothIcon.setImageResource(R.drawable.ic_bluetooth_of_foreground);
                    }catch (IOException error){
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Intent abrelista = new Intent(MainActivity.this, ListaDispositivos.class);
                    startActivityForResult(abrelista, SOLICITA_CONEXAO);

                }
            }
        });


        LedAzul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!azul){
                    LedAzul.setAlpha(1f);
                    azul = !azul;
                } else {
                    LedAzul.setAlpha(0.3f);
                    azul = !azul;
                }
            }
        });
        LedVerde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!verde){
                    LedVerde.setAlpha(1f);
                    verde = !verde;
                } else {
                    LedVerde.setAlpha(0.3f);
                    verde = !verde;
                }
            }
        });
        LedVermelho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!vermelho){
                    LedVermelho.setAlpha(1f);
                    vermelho = !vermelho;
                } else {
                    LedVermelho.setAlpha(0.3f);
                    vermelho = !vermelho;
                }
            }
        });
        LED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!amarelo){
                    LED.setAlpha(1f);
                    amarelo = !amarelo;
                } else {
                    LED.setAlpha(0.3f);
                    amarelo = !amarelo;
                }
            }
        });







    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {

            case SOLICITA_ATIVACAO:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth foi ativado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Bluetooth não ativado", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case SOLICITA_CONEXAO:
                if (resultCode == Activity.RESULT_OK) {
                    assert data != null;
                    MAC = Objects.requireNonNull(data.getExtras()).getString(ListaDispositivos.ENDERECO_MAC);
                    texMac.setText(MAC);
                    texMac.setVisibility(View.VISIBLE);


                    meuDeviceBT = meuBluetoothAdapter.getRemoteDevice(MAC);

                    try {
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
                        meusocketBT = meuDeviceBT.createRfcommSocketToServiceRecord(uuidBT);

                        meusocketBT.connect();

                        conexao = true;

                        Toast.makeText(this, "CONECTADO", Toast.LENGTH_SHORT).show();
                        bluetoothIcon.setImageResource(R.drawable.ic_bluetooth_foreground);


                    } catch (IOException erro){
                        Toast.makeText(this, erro.getMessage(), Toast.LENGTH_SHORT).show();
                    }






                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
















}