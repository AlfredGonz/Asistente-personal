package com.example.alfredo.asistente

import ai.api.AIListener
import ai.api.android.AIConfiguration
import ai.api.android.AIService
import ai.api.model.AIError
import ai.api.model.AIResponse
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.Manifest
import android.speech.tts.TextToSpeech
import android.support.v4.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AIListener, TextToSpeech.OnInitListener {
    override fun onInit(p0: Int) {

    }

    override fun onResult(result: AIResponse?) {
        val respuesta = result?.result
        val escuchado = respuesta?.resolvedQuery
        val responder = respuesta?.fulfillment?.speech
        reemplazaTextos(escuchado, responder)
    }

    fun reemplazaTextos(escuchado: String?, respuesta: String?)
    {
        tv_escuchado.text = escuchado
        tv_respuesta.text = respuesta
        hablar(respuesta)
    }

    fun hablar(respuesta: String?)
    {
        leer?.speak(respuesta, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onListeningStarted() {
    }

    override fun onAudioLevel(level: Float) {
    }

    override fun onError(error: AIError?) {
        val err = "¡Hubo un error!"
        reemplazaTextos(err, err)
    }

    override fun onListeningCanceled() {
    }

    override fun onListeningFinished() {
    }

    val accesToken = "91ecd7db081b440aab6943ead5401b72"
    val REQUEST = 200
    var leer: TextToSpeech?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        leer = TextToSpeech(this, this)
        validar()
        configAsistente()
    }

    fun configAsistente()
    {
        val configuracion = AIConfiguration(accesToken, ai.api.AIConfiguration.SupportedLanguages.Spanish,
            AIConfiguration.RecognitionEngine.System)
        val service = AIService.getService(this, configuracion)
        service.setListener(this)
        micButton.setOnClickListener{service.startListening()}
    }

    fun validar()
    {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
        {

        }
    }

    fun solicitarPermiso()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST)
        }
    }
}
