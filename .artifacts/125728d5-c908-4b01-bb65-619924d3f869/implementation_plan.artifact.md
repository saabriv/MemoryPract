# Plan de Reorganización de Paquetes y Estructura de Carpetas

Reorganizar el código fuente del proyecto para que siga la arquitectura de carpetas y paquetes establecida (`data` y sus subcarpetas, y `ui` y sus subcarpetas).

## User Review Required

> [!IMPORTANT]
> Se moverán los siguientes archivos a sus paquetes correspondientes:
> 1. `AnimalAdapter.java` y `ColorAdapter.java` $\rightarrow$ `com.example.memorypract.ui.adaptadores` (`app/src/main/java/com/example/memorypract/ui/adaptadores/`)
> 2. `AnimalItem.java` y `ColorItem.java` $\rightarrow$ `com.example.memorypract.data.modelos` (`app/src/main/java/com/example/memorypract/data/modelos/`)
> 3. Las actividades (`MainActivity`, `InicioActivity`, `ColoresActivity`, `AnimalesActivity`, `PantallaobjetosActivity`, `PantallaObservarActivity`, `login`) se mantendrán en `com.example.memorypract.ui.controladores`.

## Open Questions

- Ninguna. La estructura objetivo (`data/modelos`, `ui/adaptadores`, `ui/controladores`) ya está definida en el proyecto.

## Proposed Changes

### Paquete `com.example.memorypract.ui.adaptadores`
#### [MODIFY] [AnimalAdapter.java](file:///C:/Users/Ana/AndroidStudioProjects/MemoryPract/app/src/main/java/com/example/memorypract/ui/adaptadores/AnimalAdapter.java)
- Cambiar package a `com.example.memorypract.ui.adaptadores`.
- Actualizar imports (`import com.example.memorypract.data.modelos.AnimalItem;`).
- Mover físicamente el archivo a `app/src/main/java/com/example/memorypract/ui/adaptadores/AnimalAdapter.java`.

#### [MODIFY] [ColorAdapter.java](file:///C:/Users/Ana/AndroidStudioProjects/MemoryPract/app/src/main/java/com/example/memorypract/ui/adaptadores/ColorAdapter.java)
- Cambiar package a `com.example.memorypract.ui.adaptadores`.
- Actualizar imports (`import com.example.memorypract.data.modelos.ColorItem;`).
- Mover físicamente el archivo a `app/src/main/java/com/example/memorypract/ui/adaptadores/ColorAdapter.java`.

---

### Paquete `com.example.memorypract.data.modelos`
#### [MODIFY] [AnimalItem.java](file:///C:/Users/Ana/AndroidStudioProjects/MemoryPract/app/src/main/java/com/example/memorypract/data/modelos/AnimalItem.java)
- Cambiar package a `com.example.memorypract.data.modelos`.
- Mover físicamente el archivo a `app/src/main/java/com/example/memorypract/data/modelos/AnimalItem.java`.

#### [MODIFY] [ColorItem.java](file:///C:/Users/Ana/AndroidStudioProjects/MemoryPract/app/src/main/java/com/example/memorypract/data/modelos/ColorItem.java)
- Cambiar package a `com.example.memorypract.data.modelos`.
- Mover físicamente el archivo a `app/src/main/java/com/example/memorypract/data/modelos/ColorItem.java`.

---

### Actualización de Referencias en Activities
#### [MODIFY] [AnimalesActivity.java](file:///C:/Users/Ana/AndroidStudioProjects/MemoryPract/app/src/main/java/com/example/memorypract/ui/controladores/AnimalesActivity.java)
- Añadir import de `com.example.memorypract.ui.adaptadores.AnimalAdapter`.
- Añadir import de `com.example.memorypract.data.modelos.AnimalItem`.

#### [MODIFY] [ColoresActivity.java](file:///C:/Users/Ana/AndroidStudioProjects/MemoryPract/app/src/main/java/com/example/memorypract/ui/controladores/ColoresActivity.java)
- Añadir import de `com.example.memorypract.ui.adaptadores.ColorAdapter`.
- Añadir import de `com.example.memorypract.data.modelos.ColorItem`.

## Verification Plan

### Automated Tests
- Ejecutar compilación con Gradle (`gradle_build("app:assembleDebug")`) para verificar que no hay errores de sintaxis, paquetes faltantes o referencias rotas.

### Manual Verification
- Sincronizar proyecto y verificar compilación limpia.
