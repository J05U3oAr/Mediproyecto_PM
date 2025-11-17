//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.primerosauxilios

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaVisualizadorPDF(
    nombreArchivo: String,
    onVolver: () -> Unit,
    viewModel: VisualizadorPDFViewModel = viewModel()
) {
    val cs = MaterialTheme.colorScheme
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var paginaActual by remember { mutableStateOf(0) }
    var totalPaginas by remember { mutableStateOf(0) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var pdfRenderer by remember { mutableStateOf<PdfRenderer?>(null) }

    LaunchedEffect(nombreArchivo) {
        try {
            val resourceId = context.resources.getIdentifier(
                nombreArchivo.substringBeforeLast("."),
                "raw",
                context.packageName
            )

            if (resourceId != 0) {
                val inputStream = context.resources.openRawResource(resourceId)
                val file = File(context.cacheDir, nombreArchivo)
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                val fileDescriptor = ParcelFileDescriptor.open(
                    file,
                    ParcelFileDescriptor.MODE_READ_ONLY
                )

                pdfRenderer = PdfRenderer(fileDescriptor)
                totalPaginas = pdfRenderer?.pageCount ?: 0

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
                                color = cs.onSecondaryContainer.copy(alpha = 0.7f)
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
                    containerColor = cs.secondaryContainer,
                    titleContentColor = cs.onSecondaryContainer,
                    navigationIconContentColor = cs.onSecondaryContainer
                )
            )
        },
        bottomBar = {
            if (totalPaginas > 1) {
                Surface(
                    color = cs.surface,
                    tonalElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                                    cs.primary
                                else
                                    cs.onSurface.copy(alpha = 0.3f)
                            )
                        }

                        Text(
                            text = "${paginaActual + 1} / $totalPaginas",
                            fontWeight = FontWeight.Medium,
                            color = cs.onSurface
                        )

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
                                    cs.primary
                                else
                                    cs.onSurface.copy(alpha = 0.3f)
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
                .background(cs.background)
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(cs.background),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp),
                            color = cs.primary,
                            strokeWidth = 6.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Cargando PDF...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = cs.onBackground
                        )
                    }
                }
            } else {
                if (bitmap != null) {
                    // Contenedor con scroll vertical
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Card blanco que contiene el PDF para máxima legibilidad
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White // Siempre blanco para el PDF
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    bitmap = bitmap!!.asImageBitmap(),
                                    contentDescription = "Página del PDF",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(4.dp))
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = cs.primary)
                    }
                }
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