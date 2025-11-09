package com.example.proyecto_1.ui.feature.primerosauxilios

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaVisualizadorPDF(
    nombreArchivo: String,
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    var paginaActual by remember { mutableStateOf(0) }
    var totalPaginas by remember { mutableStateOf(0) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var pdfRenderer by remember { mutableStateOf<PdfRenderer?>(null) }

    // Cargar el PDF desde los recursos raw
    LaunchedEffect(nombreArchivo) {
        try {
            val resourceId = context.resources.getIdentifier(
                nombreArchivo.substringBeforeLast("."),
                "raw",
                context.packageName
            )

            if (resourceId != 0) {
                // Copiar el archivo a un archivo temporal
                val inputStream = context.resources.openRawResource(resourceId)
                val file = File(context.cacheDir, nombreArchivo)
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                // Abrir el PDF
                val fileDescriptor = ParcelFileDescriptor.open(
                    file,
                    ParcelFileDescriptor.MODE_READ_ONLY
                )

                pdfRenderer = PdfRenderer(fileDescriptor)
                totalPaginas = pdfRenderer?.pageCount ?: 0

                // Renderizar la primera página
                renderizarPagina(pdfRenderer, paginaActual) { nuevoBitmap ->
                    bitmap = nuevoBitmap
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            pdfRenderer?.close()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Guía de Primeros Auxilios",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        if (totalPaginas > 0) {
                            Text(
                                text = "Página ${paginaActual + 1} de $totalPaginas",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        },
        bottomBar = {
            if (totalPaginas > 1) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Botón Anterior
                        IconButton(
                            onClick = {
                                if (paginaActual > 0) {
                                    paginaActual--
                                    renderizarPagina(pdfRenderer, paginaActual) { nuevoBitmap ->
                                        bitmap = nuevoBitmap
                                    }
                                }
                            },
                            enabled = paginaActual > 0
                        ) {
                            Icon(
                                imageVector = Icons.Default.NavigateBefore,
                                contentDescription = "Página anterior",
                                tint = if (paginaActual > 0)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                        }

                        Text(
                            text = "${paginaActual + 1} / $totalPaginas",
                            fontWeight = FontWeight.Medium
                        )

                        // Botón Siguiente
                        IconButton(
                            onClick = {
                                if (paginaActual < totalPaginas - 1) {
                                    paginaActual++
                                    renderizarPagina(pdfRenderer, paginaActual) { nuevoBitmap ->
                                        bitmap = nuevoBitmap
                                    }
                                }
                            },
                            enabled = paginaActual < totalPaginas - 1
                        ) {
                            Icon(
                                imageVector = Icons.Default.NavigateNext,
                                contentDescription = "Página siguiente",
                                tint = if (paginaActual < totalPaginas - 1)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "Página del PDF",
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                )
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

private fun renderizarPagina(
    renderer: PdfRenderer?,
    paginaIndex: Int,
    onBitmapReady: (Bitmap) -> Unit
) {
    renderer?.let { pdf ->
        pdf.openPage(paginaIndex).use { pagina ->
            val bitmap = Bitmap.createBitmap(
                pagina.width * 2,
                pagina.height * 2,
                Bitmap.Config.ARGB_8888
            )
            pagina.render(
                bitmap,
                null,
                null,
                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
            )
            onBitmapReady(bitmap)
        }
    }
}