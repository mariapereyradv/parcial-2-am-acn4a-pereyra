# Leído - Tu Diario Literario Personal

> Aplicación Android para gestionar libros leídos y deseados de forma privada

---

## Datos del Proyecto

- **Estudiante:** Pereyra Maria
- **Comisión:** ACN4A - Turno Noche - Virtual
- **Materia:** Aplicaciones Móviles - Final
- **Año:** 2025

---

## Sobre la Aplicación

**Leído** es una app personal para llevar registro de tus lecturas. Sin redes sociales, sin presión, totalmente privado.

### Características principales:
- ✅ Agregar libros leídos y deseados
- ✅ Gestionar lista de lecturas
- ✅ Agregar notas personales privadas
- ✅ Marcar libros deseados como leídos
- ✅ Persistencia local con SharedPreferences

---

## INFORME TÉCNICO - LEÍDO

**1. ¿QUÉ ES LEÍDO?**
   Leído es una app para Android donde puedo anotar los libros que leí y los que quiero leer. Es como un diario personal de lecturas, pero en el celular.
   La diferencia con otras apps es que es totalmente privada: no hay likes, no hay amigos, no hay nada público. Es solo para el que lo descargue y use.
**2. ¿POR QUÉ HICE ESTA APP?**
   Porque hay muchas personas de todas las edades que solo quieren llevar un registro de sus libros para no leer lo mismo dos veces y buscan un medio accesible y práctico sin redes sociales, sin like o comentarios de terceros
   Entonces Leído es una app simple: agregas un libro, escribís tus notas, y listo. Sin complicaciones.También anotas los libros que pensas leer más adelante.
**3. PANTALLAS DE LA APP**
   3.1. Intro Animada (2.5 segundos)
   ¿Qué hace?
   Muestra una animación de círculos girando con el nombre "AURELIA UNIVERSO". Es como una intro de película, para que la app se vea más profesional y porque está integrado a la materia de plataforma de desarrollo y al universo Aurelia, una web de una sommelier profesional que además de sus servicios tiene la particularidad de maridar libros con vinos que ella recomienda de acuerdo a los estilos y características de las distintas cepas y regiones de las vides.
   ¿Cómo la hice?
   Usé ObjectAnimator de Android. Los círculos rotan automáticamente y el texto aparece con efecto fade-in. Dura 2.5 segundos y después pasa automáticamente al Splash.
   Archivo: AureliaIntroActivity.java
   3.2. Splash Screen (2 segundos)
   ¿Qué hace?
   Muestra el logo grande de "Leído" con el subtítulo "Tu diario literario personal".
   ¿Cómo la hice?
   Es una pantalla simple con un LinearLayout. Espera 2 segundos usando un Handler y después te lleva al Login.
   Archivo: SplashActivity.java


###3.3. Login###
¿Qué hace?
Te deja entrar con tu email y contraseña. Si ya tenés cuenta, entrás directo. Si no, podés registrarte.
¿Cómo funciona?
Uso Firebase Authentication. Cuando apretás el botón "INICIAR SESIÓN", Firebase verifica si el email y contraseña existen. Si están bien, te deja entrar. Si no, muestra un error.
Validaciones:
Email no puede estar vacío
Contraseña no puede estar vacía
Email tiene que tener formato válido (algo@algo.com)
Archivo: LoginActivity.java


###3.4. Registro###
¿Qué hace?
Te deja crear una cuenta nueva con nombre, email y contraseña.
¿Cómo funciona?
También uso Firebase Authentication. Cuando apretás "REGISTRARSE":
Valida que todos los campos estén completos
Verifica que las contraseñas coincidan
Crea el usuario en Firebase
Te manda de vuelta al Login
Validaciones:
Nombre no puede estar vacío
Email tiene que ser válido
Contraseña mínimo 6 caracteres (Firebase lo pide)
Las dos contraseñas tienen que ser iguales
Archivo: RegistroActivity.java

###3.5. Pantalla Principal (Main)###
¿Qué hace?
Es la pantalla donde ves tus libros. Tiene dos pestañas (tabs):
Leídos: los libros que ya terminaste
Deseados: los que querés leer
¿Cómo está hecha?
Usé:
Un TabLayout de Material Design para las pestañas
Dos Fragments (uno para cada lista)
Un FloatingActionButton (el botón redondo que flota) para agregar libros
Cuando cambiás de pestaña, se cambia el fragmento:
Botón de perfil:
Arriba a la derecha hay un icono de usuario. Si lo tocás, te pregunta si querés cerrar sesión.
Archivo: MainActivity.java

###3.6. Lista de Leídos (Fragment)###
¿Qué hace?
Muestra todos los libros que marcaste como "leídos".
¿Cómo funciona?
Uso un RecyclerView que es como un ListView pero más eficiente. Cada libro se muestra en una tarjeta (CardView) con:
Portada del libro (si tiene)
Título
Autor
Botón de eliminar
Eventos:
Click corto: abre la pantalla de detalle del libro
Click largo o botón 🗑️: pregunta si querés eliminarlo
Archivo: FragmentLeidos.java
###3.7. Lista de Deseados (Fragment)###
¿Qué hace?
Muestra los libros que querés leer en el futuro.
¿Cómo funciona?
Es igual al Fragment de Leídos, pero tiene una diferencia: cuando hacés click largo, te da dos opciones:
Marcar como leído → lo mueve a la lista de Leídos
Eliminar → lo borra
Archivo: FragmentDeseados.java

###3.8. Agregar Libro (Diálogo)###
¿Qué hace?
Es una ventana que aparece cuando apretás el botón + (FAB). Te deja agregar un libro nuevo.
Campos:
Título → obligatorio
Autor, Editorial, ISBN → opcionales
Portada → podés elegir:
Poner una URL de internet
Elegir una foto de la galería
Tus notas → campo grande para escribir lo que quieras
¿Cómo funciona la portada desde URL?
Uso Glide, una librería que descarga y muestra imágenes de internet:
¿Cómo funciona la portada desde galería?
Abro la galería del celular con un Intent
El usuario elige una foto
Convierto la foto a Base64 (un texto que representa la imagen)
Guardo ese texto en Firebase
¿Por qué Base64? Porque Firebase Firestore no puede guardar fotos directamente, solo texto. Entonces convierto la imagen en texto.
Archivo: DialogAgregarLibro.java


###3.9. Detalle del Libro###
¿Qué hace?
Muestra toda la información de un libro cuando hacés click en él.
¿Cómo está hecha?
Usé ConstraintLayout porque necesitaba poner muchas cosas en la pantalla y que se vean bien en todos los tamaños de celular.
Los datos llegan con Intent:


Archivo: DetalleLibroActivity.java
**4. ¿CÓMO GUARDÉ LOS DATOS?**
   ###4.1. Firebase Authentication###
   Para el login y registro usé Firebase Authentication. Es un servicio de Google que maneja usuarios automáticamente.
   ¿Por qué lo usé?
   Porque manejar contraseñas es complicado y peligroso. Firebase lo hace por mí de forma segura.

###4.2. Cloud Firestore (Base de Datos)###
Para guardar los libros usé Cloud Firestore. Es una base de datos en la nube.
Estructura:
Cada usuario tiene su propia carpeta de libros. Nadie más puede ver tus libros.
Operaciones que hice:
1. Agregar libro:
2. Ver libros:
3. Eliminar libro:
4. Marcar como leído:


**5. ¿CÓMO ORGANICÉ EL CÓDIGO?**
   ###5.1. Repository Pattern###
   Hice una clase llamada LibroRepository que maneja TODO lo relacionado con Firebase. Es como un intermediario:
   Activity → Repository → Firebase

¿Por qué?
Porque si el día de mañana quiero cambiar Firebase por otra cosa, solo cambio el Repository. Las Activities no se enteran
Archivo: LibroRepository.java
###5.2. Adapter para RecyclerView###
Para mostrar la lista de libros hice un LibroAdapter. Es la clase que le dice al RecyclerView cómo mostrar cada libro.
ViewHolder Pattern:
El RecyclerView reutiliza las vistas en lugar de crearlas cada vez. Esto hace que la lista sea súper rápida.
Archivo: LibroAdapter.java
###5.3. Modelo de Datos###
Creé una clase Libro que representa un libro:
Firestore convierte automáticamente esta clase en documentos.
Archivo: Libro.java
**6. LAYOUTS QUE USÉ**
   ###6.1. ConstraintLayout###
   Dónde: Pantalla de detalle del libro
   ¿Por qué?
   Porque necesitaba poner muchos elementos (imagen, título, autor, editorial, ISBN, comentarios) y que se adapten a todos los tamaños de pantalla.
   Con ConstraintLayout puedo decir "este texto va debajo de la imagen" o "este botón va al fondo".
   ###6.2. LinearLayout###
   Dónde: Login, Registro, Splash, Diálogo de agregar
   ¿Por qué?
   Cuando los elementos van uno debajo del otro (vertical) o uno al lado del otro (horizontal), LinearLayout es lo más simple.
   ###6.3. RelativeLayout###
   Dónde: Intro animada, MainActivity
   ¿Por qué?
   Porque necesitaba poner elementos relativos a otros. Por ejemplo, el botón de perfil "a la derecha del header" o las estrellas "en las esquinas".
   ###6.4. FrameLayout###
   Dónde: Contenedor de Fragments
   ¿Por qué?
   Es el contenedor estándar para poner Fragments. Android reemplaza el contenido del FrameLayout cuando cambiás de Fragment.

**7. DISEÑO Y COLORES**
   Usé una paleta que llamé "Aurelia":
   Violeta (#7B1FA2): color principal
   Rosa neón (#FF6090): para botones importantes
   Dorado (#FFD740): para acentos especiales
   ¿Por qué estos colores?
   Quería algo que se vea moderno pero cálido. El violeta da seriedad, el rosa da energía, y el dorado da un toque premium.
   Los botones principales tienen un gradiente de tres colores (rosa → coral → dorado) que les da un look más profesional.
   Recursos organizados:
   colors.xml → todos los colores
   dimens.xml → todos los tamaños (margins, paddings, textos)
   strings.xml → todos los textos
   ¿Por qué organizar así?
   Porque si mañana quiero cambiar un color o un texto, lo cambio en UN solo lugar y se actualiza en toda la app.
**8. DIFICULTADES QUE TUVE**
   ###8.1. Firebase es asíncrono###
   Problema:
   Cuando pedís datos a Firebase, no los tenés al instante. Tenés que esperar. Si intentás usarlos antes, la app crashea.
   Solución:
   Usé callbacks (interfaces con métodos que se llaman cuando los datos están listos):
   ###8.2. Base64 ocupa mucho###
   Problema:
   Las imágenes en Base64 ocupan mucho espacio. Si guardaba fotos grandes, Firebase tardaba en subir/bajar.
   Solución:
   Redimensioné las imágenes antes de convertirlas a Base64:
   Bitmap resized = Bitmap.createScaledBitmap(bitmap, 400, 600, true);
   String base64 = bitmapToBase64(resized);

###8.3. RecyclerView no se actualizaba###
Problema:
Cuando agregaba un libro, la lista no se actualizaba sola.
Solución:
Después de cada operación, llamo a refrescarLista() que vuelve a cargar los datos y le dice al adapter que actualice:
**9. LO QUE APRENDÍ**
   Técnico:
   Firebase es muy útil pero hay que entender que todo es asíncrono
   RecyclerView es mucho más eficiente que ListView
   Glide hace super fácil cargar imágenes de internet
   Organizar recursos hace que el código sea más claro
   Diseño:
   Las animaciones tienen que ser sutiles, si no molestan
   Los colores tienen que tener sentido (no poner 10 colores random)
   El espaciado consistente hace que se vea más profesional
   Arquitectura:
   Separar la lógica de datos de la UI hace todo más ordenado
   Los callbacks son la forma correcta de manejar operaciones asíncronas
   Un buen Repository te ahorra mucho código repetido
**10. LO QUE FALTA (y por qué)**
    Verificación de email
    Firebase permite enviar un mail para verificar la cuenta. No lo implementé porque:
    No era obligatorio según la consigna
    Para probarlo necesitás un mail real
    La app funciona perfecto sin esto

**11. CONCLUSIÓN**
    Leído es una app funcional que cumple con todos los requisitos del final:
    Utilizar, mejor dicho comenzar a utilizar Android Studio y adaptarme a una versión antigua, Chipmunk 2021.2.1
    Fecha: Diciembre 2025
    Estudiante: Maria Pereyra

    ###Repositorio: https://github.com/mariapereyradv/final-am-acn4a-pereyra###
