package edu.utp.evchargemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import edu.utp.evchargemanager.ui.EvChargeManagerApp
import edu.utp.evchargemanager.ui.theme.EvChargeManagerTheme

/** Punto de entrada: solo aplica el tema y muestra la raíz de la aplicación. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EvChargeManagerTheme {
                EvChargeManagerApp()
            }
        }
    }
}
