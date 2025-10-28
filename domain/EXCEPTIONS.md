# Domain Exception Architecture

Este documento describe la arquitectura de excepciones del módulo de dominio, basada en principios de Domain-Driven Design (DDD).

## Jerarquía de Excepciones

### Base Exceptions (`domain/common/exception/`)

#### `DomainException`
Clase raíz de todas las excepciones del dominio.

**Características**:
- Incluye un `errorCode` opcional para categorización
- Método `toString()` mejorado que incluye el error code
- Todas las excepciones de negocio heredan de esta clase

**Uso**:
```kotlin
abstract class DomainException(
    message: String,
    cause: Throwable? = null,
    val errorCode: String? = null
) : Exception(message, cause)
```

#### `ValidationException`
Base para excepciones de validación de entidades y value objects.

**Características**:
- Incluye `fieldName` - nombre del campo que falló
- Incluye `invalidValue` - valor que causó el fallo
- Mensajes específicos sobre por qué falló la validación

**Uso**:
```kotlin
abstract class ValidationException(
    val fieldName: String,
    val invalidValue: Any?,
    message: String,
    errorCode: String? = null
) : DomainException(...)
```

#### `NotFoundException`
Base para recursos no encontrados (típicamente mapea a HTTP 404).

**Características**:
- Incluye `resourceType` - tipo de recurso buscado
- Incluye `identifier` - identificador usado en la búsqueda

**Uso**:
```kotlin
abstract class NotFoundException(
    val resourceType: String,
    val identifier: Any,
    message: String,
    errorCode: String? = null
) : DomainException(...)
```

#### `RepositoryException`
Base para fallos de infraestructura/repositorio.

**Características**:
- Incluye `service` - nombre del servicio que falló
- Soporta `cause` para excepciones subyacentes
- Útil para errores de BD, API, filesystem, etc.

**Uso**:
```kotlin
abstract class RepositoryException(
    val service: String,
    message: String,
    cause: Throwable? = null,
    errorCode: String? = null
) : DomainException(...)
```

---

## Character Exceptions (`domain/character/exception/`)

Todas las excepciones de Character son `sealed class` que heredan de `DomainException`.

### Excepciones de Validación (Invariantes del Modelo)

#### `InvalidCharacterId`
- **Error Code**: `INVALID_CHARACTER_ID`
- **Regla**: ID debe ser >= 1
- **Uso**: Validación en construcción de `Character`

#### `InvalidCharacterName`
- **Error Code**: `INVALID_CHARACTER_NAME`
- **Regla**: Nombre no puede estar en blanco
- **Uso**: Validación en construcción de `Character`

#### `InvalidCharacterSpecies`
- **Error Code**: `INVALID_CHARACTER_SPECIES`
- **Regla**: Species no puede estar en blanco
- **Uso**: Validación en construcción de `Character`

#### `InvalidCharacterImageUrl`
- **Error Code**: `INVALID_CHARACTER_IMAGE_URL`
- **Regla**: URL debe ser HTTP/HTTPS válida
- **Uso**: Validación en construcción de `Character`

#### `InvalidCharacterUrl`
- **Error Code**: `INVALID_CHARACTER_URL`
- **Regla**: URL debe ser HTTP/HTTPS válida
- **Uso**: Validación en construcción de `Character`

#### `InvalidCharacterEpisodes`
- **Error Code**: `INVALID_CHARACTER_EPISODES`
- **Regla**: Debe tener al menos 1 episodio
- **Propiedades**: `episodeCount: Int`
- **Uso**: Validación en construcción de `Character`

#### `InvalidCharacterLocation`
- **Error Code**: `INVALID_CHARACTER_LOCATION`
- **Regla**: Location name y URL deben ser válidos
- **Propiedades**: `locationType: String`, `reason: String`
- **Uso**: Validación en construcción de `Character` y `CharacterLocation`

#### `InvalidCharacterCreatedDate`
- **Error Code**: `INVALID_CHARACTER_CREATED_DATE`
- **Regla**: Fecha debe ser ISO-8601 válida y no en el futuro
- **Propiedades**: `created: String`
- **Uso**: Validación en construcción de `Character`

### Excepciones de Casos de Uso

#### `InvalidCharacterPage`
- **Error Code**: `INVALID_CHARACTER_PAGE`
- **Regla**: Número de página debe ser >= 1
- **Propiedades**: `page: Int`
- **Uso**: Use cases de paginación

#### `InvalidCharacterSearchQuery`
- **Error Code**: `INVALID_CHARACTER_SEARCH_QUERY`
- **Regla**: Query de búsqueda no puede estar en blanco
- **Propiedades**: `query: String`
- **Uso**: Use case de búsqueda

### Excepciones de Repositorio/Infraestructura

#### `CharacterNotFound`
- **Error Code**: `CHARACTER_NOT_FOUND`
- **Propiedades**: `characterId: Int`
- **Uso**: Cuando un character específico no existe

#### `CharactersNotFoundByName`
- **Error Code**: `CHARACTERS_NOT_FOUND_BY_NAME`
- **Propiedades**: `searchName: String`
- **Uso**: Cuando una búsqueda por nombre no retorna resultados

#### `CharacterRepositoryUnavailable`
- **Error Code**: `CHARACTER_REPOSITORY_UNAVAILABLE`
- **Propiedades**: `message: String`, `cause: Throwable?`
- **Uso**: Cuando el repositorio está temporalmente no disponible

#### `InvalidCharacterData`
- **Error Code**: `INVALID_CHARACTER_DATA`
- **Propiedades**: `message: String`, `cause: Throwable?`
- **Uso**: Cuando los datos están corruptos o en formato inválido (típicamente en capa de datos)

---

## Patrón de Uso

### En Modelos de Dominio

Los modelos se validan a sí mismos en construcción:

```kotlin
data class Character(...) {
    init {
        validate()
    }

    private fun validate() {
        validateId()
        validateName()
        validateSpecies()
        // ... más validaciones
    }

    private fun validateId() {
        if (id < 1) {
            throw CharacterException.InvalidCharacterId(characterId = id)
        }
    }

    private fun validateName() {
        if (name.isBlank()) {
            throw CharacterException.InvalidCharacterName(name = name)
        }
    }
}
```

### En Casos de Uso

```kotlin
class GetCharactersUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(page: Int): List<Character> {
        if (page < 1) {
            throw CharacterException.InvalidCharacterPage(page = page)
        }

        return repository.getCharacters(page)
    }
}
```

### En Repositorios

```kotlin
class CharacterRepositoryImpl : CharacterRepository {
    override suspend fun getCharacterById(id: Int): Character {
        try {
            val response = api.getCharacter(id)
            if (response == null) {
                throw CharacterException.CharacterNotFound(characterId = id)
            }
            return mapper.toDomain(response)
        } catch (e: NetworkException) {
            throw CharacterException.CharacterRepositoryUnavailable(
                message = "Network error",
                cause = e
            )
        }
    }
}
```

---

## Exhaustive When

Las sealed classes permiten exhaustive when statements:

```kotlin
fun handleCharacterException(exception: CharacterException): String {
    return when (exception) {
        is CharacterException.CharacterNotFound -> "Character not found"
        is CharacterException.InvalidCharacterId -> "Invalid ID"
        is CharacterException.InvalidCharacterName -> "Invalid name"
        // ... todos los casos deben ser manejados
    }
    // El compilador garantiza que todos los casos están cubiertos
}
```

---

## Error Codes

Todos los error codes siguen el patrón `SCREAMING_SNAKE_CASE`:

| Exception | Error Code |
|-----------|-----------|
| `InvalidCharacterId` | `INVALID_CHARACTER_ID` |
| `InvalidCharacterName` | `INVALID_CHARACTER_NAME` |
| `InvalidCharacterSpecies` | `INVALID_CHARACTER_SPECIES` |
| `InvalidCharacterImageUrl` | `INVALID_CHARACTER_IMAGE_URL` |
| `InvalidCharacterUrl` | `INVALID_CHARACTER_URL` |
| `InvalidCharacterEpisodes` | `INVALID_CHARACTER_EPISODES` |
| `InvalidCharacterLocation` | `INVALID_CHARACTER_LOCATION` |
| `InvalidCharacterCreatedDate` | `INVALID_CHARACTER_CREATED_DATE` |
| `InvalidCharacterPage` | `INVALID_CHARACTER_PAGE` |
| `InvalidCharacterSearchQuery` | `INVALID_CHARACTER_SEARCH_QUERY` |
| `CharacterNotFound` | `CHARACTER_NOT_FOUND` |
| `CharactersNotFoundByName` | `CHARACTERS_NOT_FOUND_BY_NAME` |
| `CharacterRepositoryUnavailable` | `CHARACTER_REPOSITORY_UNAVAILABLE` |
| `InvalidCharacterData` | `INVALID_CHARACTER_DATA` |

**Uso de Error Codes**:
- Logging estructurado
- Tracking de errores
- Dashboards de monitoreo
- Agrupación de errores similares
- Internacionalización de mensajes

---

## Testing

Todas las excepciones tienen tests exhaustivos:

```kotlin
@Test
fun `given invalid ID when creating character then throws InvalidCharacterId`() {
    val invalidId = 0

    try {
        CharacterBuilder.rickSanchez().withId(invalidId).build()
        fail("Should have thrown InvalidCharacterId exception")
    } catch (e: CharacterException.InvalidCharacterId) {
        assertEquals(invalidId, e.characterId)
        assertEquals("INVALID_CHARACTER_ID", e.errorCode)
    }
}
```

Ver `domain/src/test/` para todos los tests (80+ tests).

---

## Extensión

Para agregar nuevas excepciones:

1. **Determinar el tipo base** (Validation, NotFound, Repository)
2. **Agregar a sealed class** en `CharacterException`
3. **Incluir error code único**
4. **Documentar la regla de negocio**
5. **Escribir tests exhaustivos**

Ejemplo:

```kotlin
sealed class CharacterException(...) : DomainException(...) {
    // ... excepciones existentes

    data class InvalidCharacterType(
        val type: String
    ) : CharacterException(
        message = "Invalid character type: '$type'",
        errorCode = "INVALID_CHARACTER_TYPE"
    )
}
```

---

**Última actualización**: Octubre 2025