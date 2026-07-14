# Zent v2 - Finanzas para Estudiantes

Zent es la evolución de FlowStudent, una aplicación de control financiero diseñada específicamente para estudiantes de bachillerato. Esta versión v2 introduce un sistema de diseño premium (negro/dorado), gestión completa de ingresos y gastos, gráficas avanzadas y control de presupuesto.

## 🚀 Nuevas Funcionalidades
- **Sistema de Diseño ZENT:** Interfaz minimalista en negro y dorado con soporte para modo claro/oscuro.
- **Gestión de Transacciones:** Registro detallado de ingresos y gastos.
- **Balance Real:** Visualización del balance mensual (Ingresos - Gastos).
- **Gráficas Avanzadas:** Visualización de actividad por semana, mes y año usando MPAndroidChart.
- **Control de Presupuesto:** Barra de progreso circular para monitorear el consumo del presupuesto mensual.
- **Perfil y Personalización:** Personalización de nombre de usuario y colores de categorías.
- **Persistencia Robusta:** Migración a Room v2 con soporte para tipos de transacción y timestamps.

## 🛠 Requisitos de Compilación
- **Android Studio Giraffe (2022.3.1)** o superior.
- **JDK 17**.
- **Gradle 8.1.0**.

## 📦 Instrucciones para Generar el APK
1.  Abre Android Studio y selecciona **Open**.
2.  Busca la carpeta `zent` y ábrela.
3.  Espera a que la sincronización de Gradle finalice (verás un mensaje de "Build Successful").
4.  Ve al menú superior: **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5.  Una vez finalizado, haz clic en **locate** en la notificación para encontrar el archivo `app-debug.apk`.

## 🛠 Notas Técnicas (Retroalimentación Aplicada)
- **Gradle Wrapper:** Incluido para asegurar la compatibilidad de versiones.
- **AndroidX & Jetifier:** Activados en `gradle.properties`.
- **Memoria:** Configurado `-Xmx2048m` para compilaciones rápidas con Jetpack Compose.
- **Recursos:** Todos los iconos y estilos están correctamente enlazados en el `AndroidManifest.xml`.
- **Dependencias:** MPAndroidChart añadido vía JitPack en `settings.gradle`.

---
Desarrollado con ❤️ para estudiantes.
