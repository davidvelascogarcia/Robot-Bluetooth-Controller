/*
*
*       Android: Arduino Bluetooth Controller
*       Clase: User Interface
*       Autor: @davidvelascogarcia
*
*
 */

// Interfaz de usuario

package com.daos.davidvelascogarcia.bluetoothcontroller;

// Librerias

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

// Clase principal

public class user_interface extends AppCompatActivity {

    // Objetos de los elementos gráficos

    Button id_desconectar;

    // Objetos de control de movimiento

    Button id_adelante, id_atras, id_derecha, id_izquierda, id_girar_derecha, id_girar_izquierda;

    // Objetos de control de vista

    Button id_mirar_derecha, id_mirar_izquierda, id_mirar_arriba, id_mirar_abajo;

    // Objetos de modos de control

    Button id_modo_vigia, id_modo_autonomo, id_modo_analisis, id_modo_defensa;

    // Objetos de elemento gráficos

    TextView id_buffer_in;
    Handler bluetooth_in;

    // Variables

    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    private ConnectedThread MyConexionBT;

    // Identificador del servicio - SPP UUID

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC del dispositivo

    private static String address = null;

    // Función al crar
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);

        // Enlazar objetos del XML con los objetos creados en el código

        id_desconectar = (Button) findViewById(R.id.id_desconectar);
        id_buffer_in = (TextView) findViewById(R.id.id_buffer_in);
        id_adelante = (Button) findViewById(R.id.id_adelante);
        id_atras = (Button) findViewById(R.id.id_atras);
        id_derecha = (Button) findViewById(R.id.id_derecha);
        id_izquierda = (Button) findViewById(R.id.id_izquierda);
        id_girar_derecha = (Button) findViewById(R.id.id_girar_derecha);
        id_girar_izquierda = (Button) findViewById(R.id.id_girar_izquierda);
        id_mirar_arriba = (Button) findViewById(R.id.id_mirar_arriba);
        id_mirar_abajo = (Button) findViewById(R.id.id_mirar_abajo);
        id_mirar_derecha = (Button) findViewById(R.id.id_mirar_derecha);
        id_mirar_izquierda = (Button) findViewById(R.id.id_mirar_izquierda);
        id_modo_analisis = (Button) findViewById(R.id.id_modo_analisis);
        id_modo_autonomo = (Button) findViewById(R.id.id_modo_autonomo);
        id_modo_defensa = (Button) findViewById(R.id.id_modo_defensa);
        id_modo_vigia = (Button) findViewById(R.id.id_modo_vigia);

        bluetooth_in = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;
                    DataStringIN.append(readMessage);

                    int endOfLineIndex = DataStringIN.indexOf("#");

                    if (endOfLineIndex > 0) {
                        String dataInPrint = DataStringIN.substring(0, endOfLineIndex);
                        id_buffer_in.setText("Dato: " + dataInPrint);
                        DataStringIN.delete(0, DataStringIN.length());
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        VerificarEstadoBT();

        // Configuramos al hacer click en el botón

        id_adelante.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("a");
            }
        });
        id_atras.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("b");
            }
        });
        id_derecha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("c");
            }
        });
        id_izquierda.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("d");
            }
        });
        id_girar_derecha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("e");
            }
        });
        id_girar_izquierda.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("f");
            }
        });
        id_mirar_arriba.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("g");
            }
        });
        id_mirar_abajo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("h");
            }
        });
        id_mirar_derecha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("i");
            }
        });
        id_mirar_izquierda.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("j");
            }
        });
        id_modo_vigia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("k");
            }
        });
        id_modo_defensa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("l");
            }
        });
        id_modo_autonomo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("m");
            }
        });

        id_modo_analisis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("n");
            }
        });
        id_desconectar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (btSocket!=null)
                {
                    try {btSocket.close();}
                    catch (IOException e)
                    { Toast.makeText(getBaseContext(), "Se ha producido un error", Toast.LENGTH_SHORT).show();;}
                }
                finish();
            }
        });
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException
    {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Intent intent = getIntent();
        address = intent.getStringExtra(dispositivos_bluetooth.EXTRA_DEVICE_ADDRESS);
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try
        {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Creación de Socket fallida...", Toast.LENGTH_LONG).show();
        }
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {}
        }
        MyConexionBT = new ConnectedThread(btSocket);
        MyConexionBT.start();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            btSocket.close();
        } catch (IOException e2) {}
    }

    private void VerificarEstadoBT() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Este dispositivo con soporta conexión bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
    private class ConnectedThread extends Thread
    {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket)
        {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try
            {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run()
        {
            byte[] buffer = new byte[256];
            int bytes;
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    bluetooth_in.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void write(String input)
        {
            try {
                mmOutStream.write(input.getBytes());
            }
            catch (IOException e)
            {
                Toast.makeText(getBaseContext(), "Cerrando, se ha producido un fallo en la conexión bluetooth", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}