# **Proyecto Final - Fitness App**

## **Descripción del Proyecto**
**Fitness App** es una aplicación móvil diseñada para gestionar y explorar leyendas del fitness. Permite a los usuarios crear su propio perfil, compararse con leyendas precargadas y localizar gimnasios destacados. La app incluye funciones esenciales como navegación mediante un menú de pestañas, ranking dinámico y mapas interactivos.

---

## **Requisitos Previos**
- **Conexión a Internet**: La app requiere una conexión activa para funcionar correctamente.
- **Cuenta de Usuario**: Ingresa con un correo electrónico y una contraseña mayor a 6 caracteres.
  - **Correo de prueba**: `testuser@example.com`
  - **Contraseña de prueba**: `password123`

---

## **Instrucciones de Uso**
1. **Registro/Iniciar Sesión**:
   - Si eres nuevo, regístrate ingresando tu correo y creando una contraseña (mínimo 6 caracteres).
   - Si ya tienes cuenta, inicia sesión directamente.
   
2. **Navegación Principal**:
   - Al iniciar sesión, accedes al menú de pestañas con las siguientes vistas:
     - **Main**: Vista principal con las leyendas clasificadas en tres categorías:
       - Leyenda.
       - Top Global.
       - Profesional.
     - **Ranking**: Comparativa visual de los PRs (Benchpress, Squat, Deadlift) entre leyendas y el usuario.
     - **Mapa**: Muestra los gimnasios y sus líderes asignados.
     - **Perfil**: Donde puedes crear tu propia leyenda.

3. **Creación de tu Leyenda**:
   - Completa los siguientes datos:
     - Nombre.
     - Apodo.
     - Descripción.
     - Fecha de nacimiento.
     - Ocupación.
     - **PRs (Personal Records)**:
       - Benchpress.
       - Squat.
       - Deadlift.
   - Adjunta una imagen de tu galería.
   - Guarda tu perfil, el cual se asignará automáticamente a una categoría (Leyenda, Top Global, Profesional) en la vista principal.

4. **Explorar Leyendas**:
   - Haz clic en cualquier leyenda para visualizar sus detalles:
     - Nombre, PRs, descripción y video demostrativo.
     - Ubicación en el mapa (excepto para la leyenda del usuario).

5. **Ranking**:
   - Observa un gráfico comparativo con tus marcas y las leyendas precargadas.

6. **Mapa de Gimnasios**:
   - Encuentra gimnasios destacados y consulta el líder asignado al hacer clic en el gimnasio.

---

## **Especificaciones Técnicas**
1. **Icono y Launch Screen**:
   - Diseño personalizado basado en los principios de branding.
2. **Navegación UX**:
   - Implementación de controles nativos (botones, etiquetas, cuadros de texto).
3. **Conexión a Internet**:
   - Detección de conexión y manejo de errores para evitar fallos.
4. **Backend**:
   - Soporte constante para garantizar la disponibilidad de los endpoints.

---

## **Memoria Técnica**
1. **Objetivo del App**: Facilitar la comparación y registro de marcas personales en el mundo del fitness, así como explorar gimnasios y leyendas destacadas.
2. **Especificaciones del Logo**: Diseño minimalista y moderno para reflejar el dinamismo del fitness.
3. **Compatibilidad**: Diseñado para dispositivos Android con orientación vertical.
4. **Credenciales de Prueba**:
   - **Correo**: `testuser@example.com`
   - **Contraseña**: `password123`
5. **Dependencias del Proyecto**:
   - Retrofit: Para la conexión con el backend.
   - Glide: Para la carga de imágenes.
   - MPAndroidChart: Para gráficos interactivos.

---

## **Imágenes**
Incluye capturas de las siguientes vistas:
1. **Creación de Leyenda**.
2. **Vista Principal**.
3. **Ranking**.
4. **Mapa de Gimnasios**.
