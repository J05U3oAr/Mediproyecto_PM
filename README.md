# Mediproyecto_PM
# UNIVERSIDAD DEL VALLE DE GUATEMALA  

---

# HusL (Help us Lifes)  
**Programa para Dispositivos Móviles**

**Integrantes:**  
- Arodi Josué Chávez – 241112  
- David Sebastián Lemus – 241155  
- Luis Alejandro Hernández – 241424  

**Catedrático:** Juan Carlos Durini  
**Curso:** Programación de Plataformas Móviles  
**Guatemala, 2025**

---

## Introducción  
Hoy en día el mundo cada vez se digitaliza más; el acceso rápido y confiable a asistencia médica se vuelve esencial, especialmente en momentos de emergencia o en las comunidades donde los servicios de salud son limitados.  

Por ello, surgió nuestra idea llamada **HusL (Help us Lifes)**, una plataforma móvil diseñada para conectar a los usuarios con ayuda médica básica, primeros auxilios y recordatorios de situaciones médicas comunes.  

Esta aplicación busca reducir los tiempos de reacción; **HusL no pretende sustituir la atención médica profesional**, sino ser un puente entre la necesidad y la atención oportuna, para que así las personas puedan tomar decisiones rápidas.

---

## Público Objetivo  
HusL está dirigida principalmente para el siguiente público:

- Personas adultas mayores que viven solas y necesitan acceso rápido a ayuda médica básica.  
- Padres o tutores de niños pequeños que podrían requerir asistencia inmediata en casos como golpes, alergias repentinas, etc.  
- Estudiantes universitarios que viven solos y no cuentan con familiares cerca para emergencias menores.  
- Comunidades rurales o alejadas donde el acceso a un centro de salud puede tardar horas.  

Este público se beneficiará directamente al contar con una herramienta intuitiva que proporcionará pasos a seguir ante emergencias, un directorio de contactos médicos cercanos y opciones de asistencia remota básica por profesionales de la salud.

---

## Research de Competencias  
En el campo de la salud existen aplicaciones similares en el mercado que cumplen con algunas funciones relacionadas con la atención médica que nosotros podríamos ofrecer.

### Doctoralia  
- Permite encontrar y agendar médicos en áreas urbanas.  
- No ofrece asistencia médica inmediata ni primeros auxilios.  

### First Aid – Cruz Roja *(Competencia directa)*  
- Brinda información de primeros auxilios sin conexión.  
- Enfoque más educativo que interactivo.  
- Contenido útil en emergencias.  

### Ada – Tu salud en tus manos *(Competencia indirecta)*  
- Evaluación de síntomas mediante IA.  
- Carece de enfoque en primeros auxilios o contacto directo con profesionales.  

### Salud Guatemala *(Competencia indirecta)*  
- Información oficial del Ministerio de Salud Pública.  
- Limitada en interactividad, más enfocada en comunicación institucional.  

> Estas aplicaciones representan posibles competencias, pero **HusL** busca avanzar más allá con implementaciones nuevas que mejoren la experiencia del usuario.

---

## Features del Proyecto  
HusL contará con las siguientes funcionalidades implementadas:

- **Asistencia de primeros auxilios:** Guías paso a paso según el tipo de emergencia (fracturas, desmayos, quemaduras, etc).  
- **Botón de emergencia:** Contacta automáticamente a un número médico o familiar asignado.  
- **Mapa de contactos:** Envía alertas o mensajes automáticos a contactos de emergencia.  
- **Historial médico básico:** Registro local del usuario (alergias, tipo de sangre, enfermedades crónicas).  
- **Notificaciones:** Recordatorios diarios de medicinas o citas médicas.  
- **Multiplataforma y accesibilidad:** Interfaz sencilla, optimizada para Android.  

---

## Servicios Externos

### Firebase Authentication  
**Rol:** Administración integral de la autenticación de usuarios.  
**Funcionalidades:**  
- Inicio de sesión con correo y contraseña.  
- Registro de usuarios.  
- Restablecimiento de contraseñas.  
- Sesiones duraderas.  
**Estado actual:** No implementado (login sin validación real).  
**Necesidad:** Alta – Protección y administración de usuarios.  

---

### Firebase Firestore  
**Rol:** Almacenamiento en la nube.  
**Funcionalidades:**  
- Guardar perfil médico, alertas y contactos.  
- Sincronizar datos entre dispositivos.  
- Respaldo automático.  
**Estado actual:** No implementado.  
**Necesidad:** Alta – La información se pierde al cerrar la app.  

---

### Google Maps API / Mapbox  
**Rol:** Mostrar ubicaciones en el mapa de contactos.  
**Funcionalidades:**  
- Mostrar posición actual.  
- Indicar contactos y hospitales cercanos.  
- Compartir localización en tiempo real.  
**Estado actual:** Mapa simulado con Canvas.  
**Necesidad:** Media – Mejora la experiencia del usuario.  

---

### Twilio API / Firebase Cloud Messaging  
**Rol:** Envío de notificaciones SMS o Push.  
**Funcionalidades:**  
- Enviar alertas con ubicación.  
- Notificaciones a contactos asignados.  
**Estado actual:** No implementado.  
**Necesidad:** Alta – Funcionalidad esencial.  

---

### OpenFDA API  
**Rol:** Consultar datos sobre fármacos.  
**Funcionalidades:**  
- Comprobar nombres e interacciones de medicamentos.  
**Estado actual:** No implementado.  
**Necesidad:** Baja – Función complementaria.  

---

### WorkManager + AlarmManager  
**Rol:** Sistema de notificaciones.  
**Funcionalidades:**  
- Avisos de medicamentos a hora establecida.  
- Notificaciones incluso si la app está cerrada.  
**Estado actual:** No implementado.  
**Necesidad:** Alta – Core del sistema de recordatorios.  

---

## Librerías Externas  

### Carga de Imágenes (Coil Compose)  
Permite cargar y visualizar imágenes de forma asíncrona en la interfaz. Se usa para fotos de perfil, guías de primeros auxilios, y recursos locales o de red.

---

### Mapas (Mapbox)  
Mapa funcional que muestra ubicación del usuario y contactos de emergencia, con soporte offline y sin necesidad de API key. Ideal para prototipos universitarios.  

---

### Networking (Retrofit + Gson + Logging Interceptor)  
Manejo de APIs externas (OpenFDA, Twilio, backend).  
Transforma JSON a objetos Kotlin, gestiona errores de red y muestra logs en desarrollo.  

---

### Comunicaciones (Twilio SDK)  
Envía SMS automáticos con la ubicación del usuario al presionar “Enviar alerta”.  
Posibilidad de recordatorios por SMS como complemento a notificaciones push.  

---

### Utilidades (Timber)  
Optimiza el registro y depuración del código.  
Sustituye `Log.d()` / `Log.e()` nativos por una API más limpia.  
Desactiva registros en producción automáticamente.  

---

## Base de Datos Local – Room Database  

### Rol:  
Soluciona el problema de pérdida de datos (actualmente en memoria RAM).

### Funcionalidades:
- **Perfil médico permanente:** Nombre, edad, sexo, grupo sanguíneo, alergias, contacto de emergencia.  
- **Recordatorios médicos:** Fechas, horas y especificaciones de medicamentos o citas.  
- **Contactos de emergencia:** Lista persistente accesible sin conexión.  

Los datos se mantienen incluso si el usuario cierra la app o reinicia el dispositivo.


