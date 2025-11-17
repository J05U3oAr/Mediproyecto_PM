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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
//PantallaVisualizadorPDF - Pantalla para visualizar archivos PDF de guías de primeros auxilios
//Carga el PDF desde recursos raw, renderiza la página actual y permite navegar entre páginas
@Composable
fun PantallaVisualizadorPDF(
    nombreArchivo: String,                      // Nombre del archivo PDF (incluye extensión .pdf)
    onVolver: () -> Unit,                      // Acción para volver a la pantalla anterior
    viewModel: VisualizadorPDFViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    //Estado de la página actual, cantidad total de páginas y bitmap generado
    var paginaActual by remember { mutableStateOf(0) }
    var totalPaginas by remember { mutableStateOf(0) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var pdfRenderer by remember { mutableStateOf<PdfRenderer?>(null) }

    //LaunchedEffect - Carga y prepara el PDF cuando cambia el nombre de archivo
    //1. Obtiene el ID del recurso en /res/raw
    //2. Copia el archivo a memoria caché
    //3. Inicializa PdfRenderer y renderiza la primera página
    LaunchedEffect(nombreArchivo) {
        try {
            val resourceId = context.resources.getIdentifier(
                nombreArchivo.substringBeforeLast("."),
                "raw",
                context.packageName
            )

            if (resourceId != 0) {
                // Copiar el archivo a un archivo temporal en la caché de la app
                val inputStream = context.resources.openRawResource(resourceId)
                val file = File(context.cacheDir, nombreArchivo)
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                // Abrir el PDF con PdfRenderer
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

    //DisposableEffect - Se asegura de cerrar el PdfRenderer cuando se salga de la pantalla
    DisposableEffect(Unit) {
        onDispose {
            pdfRenderer?.close()
        }
    }

    Scaffold(
        //Barra superior con título de la guía y el indicador de página actual
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
        //Barra inferior con controles de navegación (anterior/siguiente) entre páginas del PDF
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
                        // Botón Anterior - Va a la página previa si no estamos en la primera
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

                        //Texto que muestra página actual / total de páginas
                        Text(
                            text = "${paginaActual + 1} / $totalPaginas",
                            fontWeight = FontWeight.Medium
                        )

                        // Botón Siguiente - Avanza a la siguiente página si no estamos en la última
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
            if (uiState.isLoading) {
                // Pantalla de carga mientras se prepara o procesa el PDF
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 6.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Cargando PDF...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }
            } else {
                // Contenido del PDF: se muestra la página actual como imagen
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
                    // Indicador de carga mientras todavía no se ha generado el bitmap
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

//renderizarPagina - Función auxiliar para renderizar una página del PDF a un Bitmap
//Recibe el PdfRenderer, el índice de página y un callback para devolver el Bitmap generado
private fun renderizarPagina(
    renderer: PdfRenderer?,
    paginaIndex: Int,
    onBitmapReady: (Bitmap) -> Unit
) {
    renderer?.let { pdf ->
        pdf.openPage(paginaIndex).use { pagina ->
            //Se crea un bitmap con el tamaño de la página (escalado x2 para mejor resolución)
            val bitmap = Bitmap.createBitmap(
                pagina.width * 2,
                pagina.height * 2,
                Bitmap.Config.ARGB_8888
            )
            //Se dibuja la página del PDF sobre el bitmap
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