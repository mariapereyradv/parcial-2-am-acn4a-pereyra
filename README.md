# Leído - Tu Diario Literario Personal

> Aplicación Android para gestionar libros leídos y deseados de forma privada

---

## 👤 Datos del Proyecto

- **Estudiante:** Pereyra
- **Comisión:** ACN4A - Turno Noche
- **Materia:** Aplicaciones Móviles - Parcial 2
- **Año:** 2024

---

## 📱 Sobre la Aplicación

**Leído** es una app personal para llevar registro de tus lecturas. Sin redes sociales, sin presión, totalmente privado.

### Características principales:
- ✅ Agregar libros leídos y deseados
- ✅ Gestionar lista de lecturas
- ✅ Agregar notas personales privadas
- ✅ Marcar libros deseados como leídos
- ✅ Persistencia local con SharedPreferences

---

## 🎨 Diseño

**Paleta Aurelia:**
- Violeta: `#7B1FA2` (principal)
- Rosa neón: `#FF6090` (acciones)
- Dorado: `#FFD740` (acentos)

---

## Pantallas

### 1. Splash Screen
- Logo animado de "Leído"
- Duración: 2 segundos
- Transición automática a Login

### 2. Login
**Funcionalidad:**
- Campos: email y contraseña
- Validación de campos vacíos
- Checkbox políticas de privacidad obligatorio
- Usuario demo: `demo` / `1234`

**Flujo:**
1. Usuario ingresa credenciales
2. Presiona "INICIAR SESIÓN"
3. Sistema valida → si OK navega a Main, si no muestra error

### 3. Registro
**Funcionalidad:**
- Campos: nombre, email, contraseña, confirmar contraseña
- Validaciones: email válido, contraseña mínimo 4 caracteres, contraseñas coinciden

**Flujo:**
1. Usuario completa formulario
2. Presiona "REGISTRARSE"
3. Sistema valida → si OK guarda y navega a Login

### 4. Pantalla Principal (Main)
**Elementos:**
- Header con logo y botón de perfil
- 2 Tabs: "📖 Leídos" y "⭐ Deseados"
- Lista de libros según tab activo
- FAB (+) para agregar libros

**Flujo:**
1. Usuario ve tab Leídos por defecto
2. Puede cambiar a tab Deseados
3. Presiona FAB → abre diálogo agregar
4. Presiona perfil → cierra sesión

### 5. Lista de Leídos (Fragment)
**Funcionalidad:**
- Muestra libros leídos en RecyclerView
- Click corto: ver detalle (TODO)
- Click largo o botón eliminar: diálogo de confirmación

**Flujo eliminación:**
1. Usuario mantiene presionado o toca 🗑️
2. Aparece diálogo "¿Eliminar [título]?"
3. Usuario confirma → libro eliminado, lista actualizada

### 6. Lista de Deseados (Fragment)
**Funcionalidad:**
- Muestra libros deseados en RecyclerView
- Click corto: ver detalle (TODO)
- Click largo o botón: menú de opciones

**Flujo opciones:**
1. Usuario mantiene presionado
2. Aparece menú: "Marcar como leído" / "Eliminar" / "Cancelar"
3. Si marca como leído → mueve a Leídos
4. Si elimina → borra el libro

### 7. Diálogo Agregar Libro
**Campos:**
- Título * (obligatorio)
- Autor
- Editorial
- ISBN
- Tus notas (multilínea)

**Flujo:**
1. Usuario completa al menos título
2. Presiona "GUARDAR" → valida y guarda
3. O presiona "CANCELAR" → cierra sin guardar
4. Libro se agrega según tab origen (Leído o Deseado)

---

## Tecnologías

**Android:**
- Lenguaje: Java
- Min SDK: 21
- Target SDK: 31
- IDE: Android Studio Chipmunk

**Componentes:**
- Activities: Splash, Login, Registro, Main
- Fragments: Leídos, Deseados
- DialogFragment para agregar libro
- RecyclerView + Adapter para listas
- TabLayout de Material Design
- FloatingActionButton

**Persistencia:**
- SharedPreferences + Gson
- Patrón Repository (singleton)

**Layouts:**
- LinearLayout
- ConstraintLayout
- RelativeLayout
- ScrollView
- CardView

---

## Estructura de Datos
```java
Libro {
    id: String (UUID)
    titulo: String
    autor: String
    editorial: String
    isbn: String
    comentario: String
    esLeido: boolean
}
```

**LibroRepository:**
- `obtenerLeidos()` → lista filtrada
- `obtenerDeseados()` → lista filtrada
- `agregarLibro(libro)`
- `eliminarLibro(libro)`
- `cambiarEstado(libro)`

---

## Cómo Ejecutar

1. Clonar repositorio
2. Abrir en Android Studio
3. Sync Gradle
4. Run en dispositivo/emulador
5. Login con: `demo` / `1234`

---

## Organización del Código

**Recursos organizados:**
- ✅ `strings.xml` - todos los textos
- ✅ `dimens.xml` - dimensiones reutilizables
- ✅ `colors.xml` - paleta Aurelia

---

## Próximas Funcionalidades (Entrega 2)

- Pantalla de detalle de libro
- Edición de libros
- Búsqueda y filtros
- Descarga de portadas desde URL

---

##  Repositorio

[github.com/TU_USUARIO/parcial-2-am-acn4a-pereyra](https://github.com/TU_USUARIO/parcial-2-am-acn4a-pereyra)
