# Mediproyecto_PM
UNIVERSIDAD DEL VALLE DE GUATEMALA 

Colegio Universitario 
Programa para dispositivos móviles 
Arodi Josué Chávez – 241112 
David Sebastián Lemus – 241155 
Luis Alejandro Hernández – 241424 
Juan Carlos Durini 
Programación de Plataformas Móviles 
Guatemala, 2025 


#Introducción 

Hoy en día el mundo cada vez se digitaliza más, el acceso rápido y confiable a asistencia médica se vuelve esencial, especialmente en momentos de emergencia o en las comunidades donde los servicios de salud son limitados. Por ellos, Surgió nuestra idea llamada HusL(Help us lifes). Una plataforma móvil diseñada para conectar a los usuarios con ayuda médica básica, primeros auxilios y recordatorios de situaciones médicas comunes. Esta aplicación busca reducir los tiempos de reacción, HusL  no pretende sustituir la atención médica profesional, sino ser un puente entre la necesidad y atención oportuna, para que asi las personas puedan tomar decisiones rápidas. 

 
 
#Público Objetivo 

HusL está dirigida principalmente para el siguiente público: 

Personas adultas mayores que viven solas y necesitan acceso rápido a ayuda médica básica. 

Padres o tutores de niños pequeños que podrían requerir asistencia inmediata en casos como golpes, alergias repentinas, etc. 

Estudiantes universitarios que viven solos, y no cuentan con familiares cerca para emergencias menores. 

Comunidades rurales o alejadas donde el acceso a un centro de salud puede tardar horas. 

 

Este público se va a beneficiar directamente al contar con dicha herramienta intuitiva porque proporcionara pasos a seguir ante emergencias, un directorio de contactos médicos cercanos, y opciones de asistencia remota básica por profesionales de la salud. 

 

 

#Research de competencias 

En el campo de la salud existen aplicaciones similares en el mercado, las cuales cumplen con algunas funciones relacionadas con la atención médica que nosotros podríamos ofrecerles. 

Doctoralia 

Esta app tiene las funcionalidades de poder encontrar ya gendar a médicos en las áreas urbanas. 

Esta no ofrece asistencia médica inmediata ni primeros auxilios 

Competencia directa 

First Aid – cruz roja 

Esta brinda información de primeros auxilios sin necesidad de conexión. 

Tiene un enfoque más educativo que interactivo que un usuario. 

Se podría considerar como una competencia directa, esto principalmente a su contenido de emergencia 

Ada – Tu salud en tus manos 

Evaluación de síntomas mediante IA 

Carece de enfoque en primereos auxilios sin contacto directo a profesionales. 

Competencia indirecta. 

Salud Guatemala 

Información oficial del ministerio de salud pública 

Muy limitada en interactividad, más enfocada en comunicación institucional. 

Estas son lagunas de las aplicaciones existentes las cuales nos podrían hacer competencia directa respecto a nuestra aplicación móvil. Sin embargo, la idea del nuestro es poder ir avanzando y hacer nuevas implementaciones las cuales ayuden a nuestros usuarios. 

 

 

#Features del proyecto 

HusL tendrá las siguientes funcionalidades implementadas: 

Asistencia de primeros auxilios: Guías paso a paso según el tipo de emergencia (fracturas, desmayos, quemaduras, etc). 

Botón de emergencia: permite contactar automáticamente a un número médico o familiar asignado. 

Mapa de contactos: lista de contactos de emergencia al que se les enviara un mensaje cuando la persona este pasando por alguna situación de salud imprevista. 

Historial médico básico: registro local del usuario con datos esenciales (alergias, tipo de sangre, enfermedades crónicas). 

Notificaciones: mensajes diarios con notificaciones de sus medicinas o citas médicas. 

Multiplataforma y accesibilidad: interfaz sencilla para todo tipo de usuarios con compatibilidad para Android. 

 

 

#Servicios Externos 

Firebase Authentication 

Rol: Administración integral de la autenticación de usuarios. 

Funcionalidades: 

Acceso mediante correo electrónico/contraseña 

Registro de nuevos usuarios 

Restablecimiento de contraseña 

Sesiones duraderas 

Estado actual: No implementado (login sin validación real) 

Necesidad: Alta - Protección y administración de usuarios 

 
Firebase Firestore 

Rol: Almacenamiento en la nube para conservación de datos. 

Funcionalidades: 

Conservar el perfil médico de usuarios (alergías, grupo sanguíneo, etc.) 

Sincronizar alertas de salud entre dispositivos. 

Guardar contactos de emergencia. 

Respaldo automático de datos esenciales. 

Estado actual: No implementado (información únicamente en memoria) 

Necesidad: Alta – La información se pierde al cerrar la aplicación. 

 
Google Maps API/Mapbox 

Rol: Mostrar ubicaciones en el mapa de contactos. 

Funcionalidades: 

Indicar la posición actual de usuario. 

Sena;ar lugares de contactos de emergencia próximos. 

Determinar trayectorias hacia hospitales más próximos. 

Compartir la localización en tiempo real de situaciones de emergencia. 

Estado actual: Mapa simulado utilizando Canvas (sin funcionalidad) 

Necesidad: Media – Optimiza la experiencia del mapa de contactos. 

 
Twivilio API o Firebase Cloud Messaging 

Rol: Envío de notificaciones SMS/Push a contactos de emergencia. 

Funcionalidades: 

Enviar un mensaje de texto automático con la ubicación al pulsar el botón de emergencia. 

Notificaciones emergentes a contactos asignados. 

Confirmación de recepción de notificiaciones. 

Estado actual: No implementado (botón "Enviar alerta” sin operar) 

Necesidad: Alta – Funcionalidad esencial de emergencias 

 
OpenFDA API 

Rol: Datos acerca de fármacos. 

Funcionalidades: 

Comprobar nombres de medicamentos en avisos. 

Mostrar datos sobre interacciones entre medicamentos 

Estado actual: No implementado 

Necesidad: Función extra. 


WorkManager + AlarmManager 

Rol: Sistema de notificaciones  

Funcionalidades: 

Avisos de medicamentos a la hora establecida. 

Notificaciones continuas, aunque la app esté cerrada. 

Repetición de alertas diarias/semanales. 

Estado actual: No implementado 

Necesidad: Alta – Core del calendario de recordatorios. 





#Librerías Externas 

Carga de Imágenes (io.coil-kt:coil-compose) 

Cargar y visualizar imágenes de manera asíncrona en la interfaz. Se emplea para exhibir imágenes de perfil de usuarios, ilustraciones de las guías de primeros auxilios, y cualquier imagen que provenga de URLs externas o recursos locales. Gestiona de manera automática la memoria caché de imágenes para optimizar el rendimiento y ofrece placeholders mientras se están descargando las imágenes. 

Mapas (Mapbox) 

Crear un mapa funcional totalmente gratuito en la pantalla "Mapa de contactos". Permite visualizar la ubicación GPS del usuario con un círculo azul, insertar marcadores personalizados para cada contacto de emergencia (incluyendo nombre y relación), realizar zoom y desplazamientos en el mapa, y operar sin conexión si se descargan previamente los tiles. No necesita claves de API ni configuraciones complicadas, perfecto para prototipos universitarios y MVPs con poco presupuesto. 

Networking (retrofit + converter-gson + logging-interceptor) 

Consumir APIs REST externas de manera segura. Se empleará para invocar la API de OpenFDA y conseguir información precisa sobre medicamentos (composición, advertencias, interacciones), enviar mensajes de texto de emergencia mediante la API de Twilio cuando el usuario presione el botón de alerta, y interactuar con cualquier backend propio que se desarrolle. Retrofit transforma automáticamente las respuestas JSON en objetos Kotlin, gestiona errores de red de manera efectiva, y el interceptor de logging permite observar todas las solicitudes/respuestas en Logcat durante el desarrollo para simplificar el proceso de depuración. 

Comunicaciones (Twilio SDK) 

Transmitir mensajes de texto automáticos a los contactos de emergencia cuando el usuario pulsa el botón "Enviar alerta" o el botón de emergencia en la interfaz principal. El mensaje de texto contendrá la ubicación GPS actual del usuario y un aviso de emergencia. Igualmente se puede emplear para mandar recordatorios de medicamentos por SMS como complemento a las notificaciones push. La opción más liviana es evitar el uso del SDK completo y realizar llamadas directamente a la API REST de Twilio a través de Retrofit. 

Utilidades (timber) 

Optimizar el sistema de registro durante el desarrollo para que el proceso de depuración sea más efectivo. Sustituye los Log.d() y Log.e() nativos de Android por una API más sencilla. Permite observar el flujo de ejecución del código, los valores de las variables, errores de red/BD y el estado de la app en Logcat. La principal ventaja es que desactiva de manera automática todos los registros en construcciones de producción, evitando así la exposición de información confidencial y optimizando el rendimiento. 

 

 

#Base de Datos Local 

Room DataBase 

Room se empleará como un sistema de almacenamiento local para solucionar el problema crítico presente: toda la información de la aplicación (perfil médico, recordatorios, contactos de emergencia) se almacena en la memoria RAM utilizando AppDataManager con mutableStateOf y mutableStateListOf, lo que implica que se pierde por completo cada vez que el usuario cierra la app. 

 
Funcionalidades 

Mantener el perfil médico del usuario: Almacenar de forma permanente: nombre, edad, sexo, grupo sanguíneo, alergias y información de contacto de emergencia. Los datos se mantienen almacenados, aunque el usuario cierre la aplicación, apague el teléfono o desinstale (hasta el próximo clear data) y hace una lectura inmediata al abrir la aplicación sin necesidad de esperar conexión a internet. 

Guardar recordatorios médicos de manera permanente: Almacenar todos los recordatorios de medicamentos y citas médicas con sus fechas, horas y especificaciones. Permite solicitudes ordenadas (primeros recordatorios próximos, por fecha, por categoría) y borra recordatorios finalizados o caducados 

Manejar contactos de emergencia constantes: Guardar lista exhaustiva de contactos con nombre, teléfono, relación y ubicación. Da acceso inmediato incluso sin conexión a la red (esencial en situaciones de emergencia).
