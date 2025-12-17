# 📚 Leído – Tu Diario Literario Personal

Aplicación Android para gestionar libros leídos y deseados de forma **privada**, sin redes sociales ni exposición pública.

---

## 📌 Datos del Proyecto

- **Estudiante:** Pereyra Maria
- **Comisión:** ACN4A – Turno Noche – Virtual
- **Materia:** Aplicaciones Móviles – Final
- **Año:** 2025

---

## 🧠 Sobre la Aplicación

**Leído** es una app personal para llevar registro de tus lecturas.  
Permite anotar libros leídos, libros deseados y escribir notas privadas.

No tiene likes, seguidores ni funciones sociales.  
Es un **diario literario personal**.

### ✨ Características principales

- Agregar libros leídos y deseados
- Gestionar listas de lectura
- Agregar notas personales privadas
- Marcar libros deseados como leídos
- Persistencia de datos con Firebase

---

## 🛠️ Informe Técnico

### 1. ¿Qué es Leído?
Leído es una aplicación Android que permite registrar los libros leídos y los que se desean leer.  
Funciona como un diario personal de lecturas en el celular.

La principal diferencia con otras apps es que es **totalmente privada**.

---

### 2. ¿Por qué hice esta app?
Porque muchas personas solo quieren llevar un registro simple de sus libros, sin redes sociales ni presión externa.  
Leído permite agregar un libro, escribir notas y listo.

---

### 3. Pantallas de la App

#### 3.1 Intro Animada
- Duración: 2.5 segundos
- Animación de círculos girando y texto con fade-in
- Implementada con `ObjectAnimator`
- Archivo: `AureliaIntroActivity.java`

#### 3.2 Splash Screen
- Duración: 2 segundos
- Muestra logo y subtítulo
- Implementado con `Handler`
- Archivo: `SplashActivity.java`

#### 3.3 Login
- Autenticación con Firebase Authentication
- Validaciones de email y contraseña
- Archivo: `LoginActivity.java`

#### 3.4 Registro
- Registro de usuario con Firebase
- Validación de campos y contraseñas
- Archivo: `RegistroActivity.java`

#### 3.5 Pantalla Principal
- Tabs: Leídos / Deseados
- `TabLayout`, `Fragments` y `FloatingActionButton`
- Archivo: `MainActivity.java`

#### 3.6 Fragment Leídos
- Lista con `RecyclerView` y `CardView`
- Eliminar libro
- Archivo: `FragmentLeidos.java`

#### 3.7 Fragment Deseados
- Permite marcar libro como leído o eliminar
- Archivo: `FragmentDeseados.java`

#### 3.8 Agregar Libro
- Diálogo para cargar datos del libro
- Portada por URL (Glide) o galería (Base64)
- Archivo: `DialogAgregarLibro.java`

#### 3.9 Detalle del Libro
- Muestra toda la información del libro
- Layout con `ConstraintLayout`
- Archivo: `DetalleLibroActivity.java`

---

## 💾 Almacenamiento de Datos

### Firebase Authentication
Usado para login y registro de usuarios de forma segura.

### Cloud Firestore
- Cada usuario tiene su propia colección de libros
- Operaciones: agregar, listar, eliminar y marcar como leído

---

## 🧩 Organización del Código

- **Repository Pattern:** `LibroRepository.java`
- **Adapter RecyclerView:** `LibroAdapter.java`
- **Modelo de datos:** `Libro.java`

---

## 🎨 Diseño y Colores

Paleta **Aurelia**:
- Violeta (#7B1FA2) – principal
- Rosa neón (#FF6090) – botones
- Dorado (#FFD740) – acentos

Recursos centralizados en:
- `colors.xml`
- `dimens.xml`
- `strings.xml`

---

## ⚠️ Dificultades y Soluciones

- Firebase asíncrono → uso de callbacks
- Imágenes Base64 pesadas → redimensionado previo
- RecyclerView no actualizaba → refresco manual del adapter

---

## 📚 Lo que Aprendí

- Manejo de Firebase y operaciones asíncronas
- Uso eficiente de RecyclerView
- Separación de lógica y UI
- Importancia del diseño y la organización

---

## ✅ Conclusión

Leído es una app funcional que cumple con todos los requisitos del final.  
Permitió aprender Android Studio, Firebase y buenas prácticas de desarrollo.

**Fecha:** Diciembre 2025  
**Estudiante:** Maria Pereyra

🔗 **Repositorio:**  
https://github.com/mariapereyradv/final-am-acn4a-pereyra
