package com.example.printerbluetooth;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;

import java.util.ArrayList;

import static android.view.View.*;

public class MainActivity extends AppCompatActivity {
    private Printing printing;
    private Button btnPairUnpair, btnPrintText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Printooth.INSTANCE.init(MainActivity.this);

        if (Printooth.INSTANCE.hasPairedPrinter()) {
            printing = Printooth.INSTANCE.printer();
        }

        btnPairUnpair = findViewById(R.id.btnPairUnpair);
        btnPrintText = findViewById(R.id.btnPrintText);

        initViews();
        initListener();
    }

    private void initViews() {
        if (Printooth.INSTANCE.hasPairedPrinter()) {
            btnPairUnpair.setText(new StringBuilder("Unpair ").append(Printooth.INSTANCE.getPairedPrinter().getName()).toString());
        } else {
            btnPairUnpair.setText(new StringBuilder("Pair With Printer"));
        }
    }

    private void initListener() {
        btnPairUnpair.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Printooth.INSTANCE.hasPairedPrinter()) {
                        Printooth.INSTANCE.removeCurrentPrinter();
                    } else {
                        startActivityForResult(new Intent(MainActivity.this, ScanningActivity.class), ScanningActivity.SCANNING_FOR_PRINTER);
                        initViews();
                    }
                } catch (Exception e) {
                    Log.w("btnUnpair", e.toString());
                }
            }
        });

        btnPrintText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!Printooth.INSTANCE.hasPairedPrinter()) {
                        startActivityForResult(new Intent(MainActivity.this, ScanningActivity.class), ScanningActivity.SCANNING_FOR_PRINTER);
                    } else {
                        printSomeText();
                    }
                } catch (Exception e) {
                    Log.w("btnPrintText", e.toString());
                }

            }
        });

        if (printing != null) {
            printing.setPrintingCallback(new PrintingCallback() {
                @Override
                public void connectingWithPrinter() {
                    Toast.makeText(MainActivity.this, "1 Connecting with printer", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void printingOrderSentSuccessfully() {
                    Toast.makeText(MainActivity.this, "2Connecting with printer", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void connectionFailed(String s) {
                    Toast.makeText(MainActivity.this, "3Connecting with printer", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String s) {
                    Toast.makeText(MainActivity.this, "4Connecting with printer", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onMessage(String s) {
                    Toast.makeText(MainActivity.this, "5Connecting with printer", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void printSomeText() {
        try {
            ArrayList<Printable> printables = new ArrayList<>();
            printables.add(new TextPrintable.Builder()
                    .setText("Hello World")
                    .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                    .setNewLinesAfter(1)
                    .build());
            printing.print(printables);
        } catch (Exception e) {
            Log.w("PrintSomeText() ", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK) {
            printSomeText();
            initViews();
        }
    }
}