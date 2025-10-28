# Testing Guide

Este documento describe la filosofía y convenciones de testing del proyecto.

## Filosofía de Testing

Los tests en este proyecto sirven como:
- **Documentación ejecutable** del comportamiento del sistema
- **Especificación** de reglas de negocio
- **Red de seguridad** para refactorings
- **Garantía** de calidad del código

## Convención de Nomenclatura

### Given-When-Then Pattern

Todos los tests siguen el patrón **Given-When-Then** sin comentarios:

```kotlin
@Test
fun `given [contexto] when [acción] then [resultado esperado]`() {
    // Setup
    val input = setupData()

    // Ejecución
    val result = systemUnderTest.execute(input)

    // Verificación
    assertEquals(expected, result)
}
```

### Ejemplos

#### Test de Validación
```kotlin
@Test
fun `given blank name when creating character then throws InvalidCharacterName`() {
    try {
        CharacterBuilder.rickSanchez().withName("   ").build()
        fail("Should have thrown InvalidCharacterName exception")
    } catch (e: CharacterException.InvalidCharacterName) {
        assertEquals("   ", e.name)
    }
}
```

#### Test de Caso de Éxito
```kotlin
@Test
fun `given valid ID when creating character then returns character successfully`() {
    val validId = 1

    val character = CharacterBuilder.rickSanchez().withId(validId).build()

    assertEquals(validId, character.id)
}
```

#### Test de Excepción de Caso de Uso
```kotlin
@Test
fun `given zero page when getting characters then throws InvalidCharacterPage`() {
    val invalidPage = 0

    try {
        Character.validatePage(invalidPage)
        fail("Should have thrown InvalidCharacterPage exception")
    } catch (e: CharacterException.InvalidCharacterPage) {
        assertEquals(invalidPage, e.page)
    }
}
```

## Estructura de Tests

### Organización

```
domain/src/test/java/com/infinitum/labs/domain/
├── character/
│   ├── exception/
│   │   └── CharacterExceptionTest.kt       # Tests de excepciones
│   └── model/
│       ├── CharacterTest.kt                # Tests del modelo
│       ├── CharacterValidationTest.kt      # Tests de validaciones
│       ├── CharacterLocationTest.kt        # Tests de location
│       ├── CharacterStatusTest.kt          # Tests de status enum
│       └── CharacterGenderTest.kt          # Tests de gender enum
```

### Builders para Tests

El proyecto usa el patrón **Test Data Builder** para crear objetos de test:

```kotlin
CharacterBuilder.rickSanchez()
    .withId(123)
    .withName("Custom Name")
    .withStatus(CharacterStatus.ALIVE)
    .build()

CharacterLocationBuilder.aCharacterLocation()
    .withName("Earth (C-137)")
    .withUrl("https://rickandmortyapi.com/api/location/1")
    .build()
```

**Ventajas**:
- Datos de test expresivos y legibles
- Fácil modificación de valores específicos
- Reducción de código boilerplate
- Valores por defecto razonables

## Tipos de Tests

### 1. Tests de Modelo

Verifican que los modelos se comporten correctamente:

```kotlin
class CharacterTest {
    @Test
    fun `given all valid properties when creating character then returns character with correct values`() {
        val origin = CharacterLocationBuilder.aCharacterLocation().build()
        val location = CharacterLocationBuilder.aCharacterLocation().build()
        val episodes = listOf("https://rickandmortyapi.com/api/episode/1")

        val character = CharacterBuilder.rickSanchez()
            .withOrigin(origin)
            .withLocation(location)
            .withEpisode(episodes)
            .build()

        assertEquals(1, character.id)
        assertEquals("Rick Sanchez", character.name)
        assertEquals(CharacterStatus.ALIVE, character.status)
    }
}
```

### 2. Tests de Validación

Verifican que las reglas de negocio se apliquen:

```kotlin
class CharacterValidationTest {
    @Test
    fun `given zero ID when creating character then throws InvalidCharacterId`() {
        val invalidId = 0

        try {
            CharacterBuilder.rickSanchez().withId(invalidId).build()
            fail("Should have thrown InvalidCharacterId exception")
        } catch (e: CharacterException.InvalidCharacterId) {
            assertEquals(invalidId, e.characterId)
        }
    }
}
```

### 3. Tests de Excepciones

Verifican que las excepciones tengan la información correcta:

```kotlin
class CharacterExceptionTest {
    @Test
    fun `given character ID when creating CharacterNotFound exception then includes ID in message`() {
        val characterId = 123

        val exception = CharacterException.CharacterNotFound(characterId = characterId)

        assertEquals("Character with ID $characterId does not exist", exception.message)
        assertEquals(characterId, exception.characterId)
    }
}
```

### 4. Tests de Enums

Verifican que los enums tengan los valores correctos:

```kotlin
class CharacterStatusTest {
    @Test
    fun `given ALIVE enum when accessing name property then returns ALIVE string`() {
        val status = CharacterStatus.ALIVE

        assertEquals("ALIVE", status.name)
    }

    @Test
    fun `given CharacterStatus enum when getting all values then returns exactly 3 values`() {
        val values = CharacterStatus.values()

        assertEquals(3, values.size)
    }
}
```

## Principios de Testing

### 1. Sin Comentarios Innecesarios

❌ **Evitar**:
```kotlin
@Test
fun `testCharacterValidation`() {
    // Given
    val id = 1

    // When
    val character = Character(...)

    // Then
    assertEquals(1, character.id)
}
```

✅ **Preferir**:
```kotlin
@Test
fun `given valid ID when creating character then returns character successfully`() {
    val validId = 1

    val character = Character(...)

    assertEquals(validId, character.id)
}
```

### 2. Test Name = Documentación

El nombre del test debe ser suficientemente descriptivo para entender:
- **Given**: Estado inicial / contexto
- **When**: Acción que se ejecuta
- **Then**: Resultado esperado

### 3. Un Concepto por Test

Cada test debe verificar **un solo concepto**:

❌ **Evitar** (test que verifica múltiples conceptos):
```kotlin
@Test
fun `test character creation and validation`() {
    // Verifica creación
    val character = Character(...)
    assertNotNull(character)

    // Verifica validación
    try {
        Character(id = 0, ...)
        fail()
    } catch (e: Exception) { }
}
```

✅ **Preferir** (tests separados):
```kotlin
@Test
fun `given valid properties when creating character then returns character successfully`() { }

@Test
fun `given invalid ID when creating character then throws InvalidCharacterId`() { }
```

### 4. Tests Independientes

Cada test debe ser **independiente** y poder ejecutarse en cualquier orden.

### 5. Fast Tests

Los tests deben ser **rápidos**:
- Sin dependencias de red
- Sin acceso a base de datos
- Sin delays artificiales
- Puro Kotlin (no Android dependencies en domain)

## Cobertura de Tests

### Objetivos

- **Dominio**: 100% de cobertura en lógica de negocio
- **Validaciones**: Todos los casos válidos e inválidos
- **Excepciones**: Todas las excepciones testeadas
- **Edge Cases**: Casos límite cubiertos

### Estado Actual

```
domain/
├── Character.kt              ✅ 100%
├── CharacterLocation.kt      ✅ 100%
├── CharacterStatus.kt        ✅ 100%
├── CharacterGender.kt        ✅ 100%
└── CharacterException.kt     ✅ 100%

Total: 80+ tests
```

## Ejecutar Tests

### Todos los Tests
```bash
./gradlew test
```

### Tests de Dominio
```bash
./gradlew :domain:test
```

### Tests Específicos
```bash
./gradlew test --tests "*.CharacterTest"
./gradlew test --tests "*.CharacterValidationTest"
./gradlew test --tests "*.CharacterExceptionTest"
```

### Con Reporte de Cobertura
```bash
./gradlew :domain:test
# Ver reporte en: domain/build/reports/tests/test/index.html
```

## Agregar Nuevos Tests

### Checklist

Al agregar nueva funcionalidad, asegurar:

1. ✅ **Happy path** - El caso de éxito funciona
2. ✅ **Validaciones** - Todos los casos inválidos lanzan excepciones
3. ✅ **Edge cases** - Casos límite cubiertos
4. ✅ **Nomenclatura** - Sigue patrón Given-When-Then
5. ✅ **Sin comentarios** - El test es auto-documentado
6. ✅ **Independiente** - No depende de otros tests
7. ✅ **Rápido** - Ejecuta en milisegundos

### Template

```kotlin
@Test
fun `given [contexto inicial] when [acción] then [resultado esperado]`() {
    // Setup: Preparar datos de entrada
    val input = createTestData()

    // Execute: Ejecutar la acción
    val result = systemUnderTest.execute(input)

    // Verify: Verificar el resultado
    assertEquals(expected, result)
}
```

## Buenas Prácticas

### ✅ DO

- Usar nombres descriptivos en Given-When-Then
- Un assert por concepto
- Tests independientes y rápidos
- Usar builders para crear objetos de test
- Verificar valores específicos (no solo `assertNotNull`)

### ❌ DON'T

- Comentarios Given/When/Then redundantes
- Múltiples conceptos en un test
- Tests que dependen del orden de ejecución
- Logic compleja en los tests
- Sleeps o delays

## Recursos

- [JUnit 4 Documentation](https://junit.org/junit4/)
- [Test Builders Pattern](https://www.javacodegeeks.com/2013/01/test-data-builders.html)
- [Given-When-Then](https://martinfowler.com/bliki/GivenWhenThen.html)

---

**Última actualización**: Octubre 2025