import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions

class MainActivity : AppCompatActivity() {

    private lateinit var inputText: EditText
    private lateinit var translateButton: Button
    private lateinit var translatedText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputText = findViewById(R.id.input_text)
        translateButton = findViewById(R.id.translate_button)
        translatedText = findViewById(R.id.translated_text)

        translateButton.setOnClickListener {
            val input = inputText.text.toString()
            if (input.isEmpty()) {
                return@setOnClickListener
            }

            val options = FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(FirebaseTranslateLanguage.EN)
                .setTargetLanguage(FirebaseTranslateLanguage.FR)
                .build()

            val translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)

            translator.downloadModelIfNeeded().addOnSuccessListener {
                translator.translate(input).addOnSuccessListener { translated ->
                    translatedText.text = translated
                }
            }
        }
    }
}