# Índice de Documentación

Bienvenido a la documentación del proyecto Rick and Morty Android. Esta guía te ayudará a navegar por todos los recursos disponibles.

## 📚 Documentación Principal

### [README.md](../README.md)
**Punto de entrada principal del proyecto**
- Descripción general del proyecto
- Características principales
- Arquitectura y tecnologías
- Instrucciones de instalación
- Guía de contribución
- Comandos útiles

**Audiencia**: Desarrolladores nuevos, contribuidores, usuarios

---

### [CLAUDE.md](../CLAUDE.md)
**Guía de referencia para Claude Code**
- Configuración del proyecto
- Arquitectura multi-módulo
- Comandos de build y testing
- Estructura del proyecto detallada
- Convenciones de desarrollo
- Domain-Driven Design

**Audiencia**: Claude Code, desarrolladores del equipo

---

## 📁 Documentación por Módulo

### Domain Module

#### [domain/EXCEPTIONS.md](../domain/EXCEPTIONS.md)
**Guía completa de la arquitectura de excepciones**
- Jerarquía de excepciones base
- Character exceptions (14 tipos)
- Patrones de uso
- Error codes
- Ejemplos de implementación
- Guía de extensión

**Temas cubiertos**:
- `DomainException`
- `ValidationException`
- `NotFoundException`
- `RepositoryException`
- `CharacterException` (sealed class)

**Audiencia**: Desarrolladores implementando lógica de negocio

---

#### [domain/TESTING.md](../domain/TESTING.md)
**Filosofía y convenciones de testing**
- Convención Given-When-Then
- Estructura de tests
- Test Data Builders
- Tipos de tests
- Principios y buenas prácticas
- Cobertura de tests (80+)

**Temas cubiertos**:
- Nomenclatura de tests
- Tests de modelo
- Tests de validación
- Tests de excepciones
- Tests de enums

**Audiencia**: Desarrolladores escribiendo tests

---

## 🗂️ Organización de la Documentación

```
RickandMortyAndroid/
├── README.md                    # 📖 Documentación principal
├── CLAUDE.md                    # 🤖 Guía para Claude Code
├── docs/
│   └── INDEX.md                # 📚 Este archivo
└── domain/
    ├── EXCEPTIONS.md           # ⚠️  Arquitectura de excepciones
    └── TESTING.md              # ✅ Guía de testing
```

---

## 🎯 Guías Rápidas por Tarea

### Empezar con el Proyecto
1. Lee [README.md](../README.md) - Visión general
2. Revisa [CLAUDE.md](../CLAUDE.md) - Configuración técnica
3. Explora `domain/` - Lógica de negocio

### Implementar Nueva Funcionalidad
1. Consulta [CLAUDE.md](../CLAUDE.md) - Arquitectura
2. Revisa [domain/EXCEPTIONS.md](../domain/EXCEPTIONS.md) - Excepciones
3. Lee [domain/TESTING.md](../domain/TESTING.md) - Tests

### Escribir Tests
1. Lee [domain/TESTING.md](../domain/TESTING.md) - Convenciones
2. Revisa tests existentes en `domain/src/test/`
3. Sigue el patrón Given-When-Then

### Crear Nuevas Excepciones
1. Consulta [domain/EXCEPTIONS.md](../domain/EXCEPTIONS.md) - Jerarquía
2. Determina el tipo base apropiado
3. Agrega a la sealed class correspondiente
4. Escribe tests exhaustivos

---

## 📊 Métricas del Proyecto

| Métrica | Valor |
|---------|-------|
| **Líneas de Documentación** | 1,300+ |
| **Archivos de Documentación** | 5 |
| **Tests Unitarios** | 80+ |
| **Módulos** | 3 (domain, data, presentation) |
| **Excepciones de Dominio** | 14 tipos |
| **Cobertura de Tests** | ~100% en dominio |

---

## 🔍 Búsqueda Rápida

### Buscar por Concepto

- **Clean Architecture** → [README.md](../README.md#arquitectura), [CLAUDE.md](../CLAUDE.md#architecture)
- **DDD** → [README.md](../README.md#domain-driven-design), [CLAUDE.md](../CLAUDE.md#domain-driven-design-ddd)
- **Excepciones** → [domain/EXCEPTIONS.md](../domain/EXCEPTIONS.md)
- **Tests** → [domain/TESTING.md](../domain/TESTING.md)
- **Validaciones** → [domain/EXCEPTIONS.md](../domain/EXCEPTIONS.md#excepciones-de-validación-invariantes-del-modelo)
- **Error Codes** → [domain/EXCEPTIONS.md](../domain/EXCEPTIONS.md#error-codes)
- **Given-When-Then** → [domain/TESTING.md](../domain/TESTING.md#given-when-then-pattern)
- **Builders** → [domain/TESTING.md](../domain/TESTING.md#builders-para-tests)

### Buscar por Archivo

| Necesito... | Ir a... |
|------------|---------|
| Instalar el proyecto | [README.md](../README.md#instalación) |
| Ejecutar tests | [README.md](../README.md#tests) |
| Comandos de build | [CLAUDE.md](../CLAUDE.md#common-commands) |
| Estructura del proyecto | [CLAUDE.md](../CLAUDE.md#project-structure) |
| Crear excepción | [domain/EXCEPTIONS.md](../domain/EXCEPTIONS.md#extensión) |
| Escribir test | [domain/TESTING.md](../domain/TESTING.md#agregar-nuevos-tests) |
| Ver error codes | [domain/EXCEPTIONS.md](../domain/EXCEPTIONS.md#error-codes) |

---

## 🚀 Próximos Pasos

### Para Desarrolladores Nuevos
1. ✅ Lee el [README.md](../README.md)
2. ✅ Configura el entorno según [CLAUDE.md](../CLAUDE.md)
3. ✅ Explora el código en `domain/`
4. ✅ Ejecuta los tests: `./gradlew :domain:test`
5. ✅ Lee [TESTING.md](../domain/TESTING.md) antes de escribir código

### Para Contribuidores
1. ✅ Revisa las [reglas de contribución](../README.md#contribuir)
2. ✅ Familiarízate con [EXCEPTIONS.md](../domain/EXCEPTIONS.md)
3. ✅ Sigue las convenciones de [TESTING.md](../domain/TESTING.md)
4. ✅ Escribe tests para toda nueva funcionalidad

---

## 📝 Actualizaciones

| Fecha | Documento | Cambio |
|-------|-----------|--------|
| Oct 2025 | Todos | Creación inicial de documentación |
| Oct 2025 | domain/EXCEPTIONS.md | Arquitectura de excepciones completa |
| Oct 2025 | domain/TESTING.md | Guía de testing con Given-When-Then |
| Oct 2025 | README.md | README principal del proyecto |
| Oct 2025 | CLAUDE.md | Actualizado con DDD y excepciones |

---

## 🤝 Contribuir a la Documentación

Si encuentras algo que falta o que podría mejorarse:

1. Abre un issue describiendo la mejora
2. Crea un PR con los cambios
3. Sigue el mismo estilo y formato
4. Actualiza este índice si es necesario

---

**Construido con ❤️ usando Clean Architecture y DDD**

**Última actualización**: Octubre 2025