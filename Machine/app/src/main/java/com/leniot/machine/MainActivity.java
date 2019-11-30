package com.leniot.machine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
//import android.serialport.SerialPortFinder;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;

import android.widget.Button;

import com.kongqw.serialportlibrary.Device;
import com.kongqw.serialportlibrary.SerialPortFinder;
import com.kongqw.serialportlibrary.SerialPortManager;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;
import com.leniot.machine.util.ByteUtil;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button button;
    SerialPortManager mSerialPortManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        trySerialTest();

        button = findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mes = "1B5901151E";
                byte[] bytes = ByteUtil.hexStr2bytes(mes);
                mSerialPortManager.sendBytes(bytes);
                Toast.makeText(MainActivity.this, "发送的数据是：" + mes,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void trySerialTest() {
        SerialPortFinder serialPortFinder = new SerialPortFinder();
        ArrayList<Device> devices = serialPortFinder.getDevices();
        System.out.println("获取到节点数量为：" + devices.size());
        mSerialPortManager = new SerialPortManager();

        mSerialPortManager.setOnSerialPortDataListener(new OnSerialPortDataListener() {
            @Override
            public void onDataReceived(byte[] bytes) {
                System.out.println("收到数据：" + Arrays.toString(bytes));
                messageHandling(bytes);
            }

            @Override
            public void onDataSent(byte[] bytes) {
            }
        });

        mSerialPortManager.setOnOpenSerialPortListener(new OnOpenSerialPortListener() {
            @Override
            public void onSuccess(File device) {
                System.out.print("串口链接成功，节点为：" + device.getName());
            }

            @Override
            public void onFail(File device, Status status) {
                System.out.print("串口链接失败，节点为：" + device.getName() + status);
            }
        });

        boolean openSerialPort = mSerialPortManager.openSerialPort(new File("dev/ttyS5"), 115200);
        if (openSerialPort) {
            hintMessage("初始化完成！");
//            byte[] bytes = ByteUtil.hexStr2bytes("1B5901151E");
//            mSerialPortManager.sendBytes(bytes);
//            try {
//                System.out.println("发送成功");
//            } catch (Exception e) {
//                System.out.println("发送失败");
//                e.printStackTrace();
//            }
        } else {
            hintMessage("初始化失败，请检查配置，重新启动！");
        }
    }

    private void messageHandling(byte[] bytes) {
        Looper.prepare();
        hintMessage("收到数据：" + Arrays.toString(bytes));
        Looper.loop();
    }

    private void hintMessage(String mes) {
        Toast.makeText(MainActivity.this, mes,
                Toast.LENGTH_SHORT).show();
    }
}


    /*public void arr(String[] devicesPath) {
        Spinner spinner = findViewById(R.id.spin);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, devicesPath);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
    }*/

  /*  @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String content = parent.getItemAtPosition(position).toString();
        switch (parent.getId()) {
            case R.id.spin:
                System.out.println("选择的串口是：" + content);
                break;

            case R.id.send:
                String mes = "1B5901151E";
                mSerialPortManager.sendBytes(mes.getBytes());
                Toast.makeText(MainActivity.this, "发送的数据是：" + mes,
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
