package com.example.proyecto_1.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun PantallaRegistro() {
    val cs = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(cs.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Registro",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = cs.onSurface
            )
        }

        RowLabelSmallField(label = "Nombre")
        RowLabelSmallField(label = "Edad")
        RowLabelSmallField(label = "Género")

        // Campos de ancho completo
        SectionLabel("Tipo de sangre")
        DisabledFieldFull()

        SectionLabel("Alergías")
        DisabledFieldFull()

        SectionLabel("Contacto de emergencia")
        DisabledFieldFull()

        Spacer(Modifier.weight(1f))

        // Botón grande (usa colores del tema)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = { /* sin acción - mock */ },
                shape = RoundedCornerShape(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cs.secondaryContainer,
                    contentColor = cs.onSecondaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(64.dp)
            ) {
                Text("Guardar", fontSize = 20.sp, textAlign = TextAlign.Center)
            }
        }
    }
}


@Composable
private fun RowLabelSmallField(label: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        DisabledFieldSmall()
    }
}

@Composable
private fun DisabledFieldSmall() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        enabled = false,
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .width(140.dp)
            .heightIn(min = 44.dp)
    )
}

@Composable
private fun DisabledFieldFull() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        enabled = false,
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 44.dp)
    )
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
}

/* ---------- Previa ---------- */
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewRegistro() {
    MaterialTheme { PantallaRegistro() }
}
