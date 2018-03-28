package com.example.user.ouvir;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button iniciar;
    private TextView mensagem;

    TextToSpeech falar;
    Context context;

    private final int ID_TEXTO_PARA_VOZ = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        iniciar = (Button)findViewById(R.id.button);
        mensagem = (TextView)findViewById(R.id.textView);

        falar = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    falar.setLanguage(Locale.getDefault());
                }
            }
        });

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iVoz = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                iVoz.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                iVoz.putExtra(RecognizerIntent.EXTRA_LANGUAGE , Locale.getDefault());
                iVoz.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale algo...");

                try{
                    startActivityForResult(iVoz, ID_TEXTO_PARA_VOZ);
                }catch (ActivityNotFoundException a){
                    Toast.makeText(getApplicationContext(), "Seu celular não suporta comando de voz!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    protected void onActivityResult(int id, int resultCodeId, Intent dados){
        super.onActivityResult(id, resultCodeId, dados);

        switch (id){
            case ID_TEXTO_PARA_VOZ:
                if(resultCodeId == RESULT_OK && null != dados){
                    ArrayList<String> result = dados.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String ditado = result.get(0);
                    Toast.makeText(getApplicationContext(),ditado, Toast.LENGTH_LONG).show();
                    mensagem.setText(ditado);
                    setFalar();

                }break;
        }
    }
    private void setFalar(){

        String texto = "Você disse: "+mensagem.getText().toString();

        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT);

        falar.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
    }
}
