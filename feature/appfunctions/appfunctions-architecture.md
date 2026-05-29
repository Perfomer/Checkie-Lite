# App Functions: архитектура и устройство

## Обзор

Модуль `:feature:appfunctions:impl` объявляет две Android App Functions поверх
`ReviewRepository`. Функции вызываются on-device ассистентом (Gemini) и позволяют
создавать и читать отзывы без открытия приложения.

Экспортируемые функции:
- `trackReview` — создаёт отзыв по переданным параметрам.
- `findReviews` — возвращает список отзывов, опционально фильтруя по тексту запроса.

## Аннотации

```kotlin
@AppFunction(isDescribedByKDoc = true)
suspend fun trackReview(...): TrackedReview

@AppFunctionSerializable(isDescribedByKDoc = true)
internal data class TrackedReview(...)
```

- `@AppFunction` (из пакета `androidx.appfunctions.service`) — помечает метод как
  вызываемую ассистентом функцию.
- `@AppFunctionSerializable` (из `androidx.appfunctions`) — делает data-класс
  сериализуемым для передачи ассистенту.
- `isDescribedByKDoc = true` — описание функции/полей берётся из KDoc-комментариев;
  ассистент использует их для понимания семантики.

Важно: в `1.0.0-alpha09` аннотация `@AppFunction` находится в артефакте
`appfunctions-service`, а не в основном `appfunctions`. Это видно в `build.gradle.kts`
модуля по комментарию к зависимости.

## Внедрение зависимостей

`App` реализует `AppFunctionConfiguration.Provider`:

```kotlin
class App : Application(), AppFunctionConfiguration.Provider {
    private val reviewRepository: ReviewRepository by inject()

    override val appFunctionConfiguration: AppFunctionConfiguration
        get() = checkieAppFunctionConfiguration(reviewRepository)
}
```

`checkieAppFunctionConfiguration` (файл `AppFunctionsConfiguration.kt`) регистрирует
фабрику для `CheckieAppFunctions`:

```kotlin
AppFunctionConfiguration.Builder()
    .addEnclosingClassFactory(CheckieAppFunctions::class.java) {
        CheckieAppFunctions(reviewRepository)
    }
    .build()
```

Библиотека создаёт экземпляр `CheckieAppFunctions` лениво через эту фабрику при каждом вызове.
Отдельный Koin-модуль для `CheckieAppFunctions` не нужен: единственная зависимость —
`ReviewRepository` — уже предоставляется существующим `localDataSourceModule`.

## Сборка и KSP-агрегация

Для корректной работы требуется KSP-компилятор в двух местах:

1. **`:feature:appfunctions:impl`** — обрабатывает `@AppFunction` и `@AppFunctionSerializable`,
   генерирует метаданные функций модуля.
2. **`:app`** — агрегирует функции всех модулей благодаря аргументу:
   ```kotlin
   ksp {
       arg("appfunctions:aggregateAppFunctions", "true")
   }
   ```
   Без этого аргумента в `:app` функции не будут зарегистрированы системно.

`compileSdk` и `targetSdk` подняты до 37 (требование `appfunctions 1.0.0-alpha09`).

## Гейтинг по версии Android

App Functions API доступен начиная с Android 16 (API 36). На устройствах с более
старой версией системы функции просто не вызываются — приложение продолжает работать
нормально. `minSdk = 24` безопасен: гейтинг происходит на уровне платформы, а не кода.
Специальный permission `EXECUTE_APP_FUNCTIONS` нужен только вызывающей стороне (ассистенту);
поставщику функций (Checkie) он не нужен.

## Чистая логика и тесты

Вся бизнес-логика вынесена в чистые функции, покрытые юнит-тестами:

| Файл | Что делает | Тест |
|---|---|---|
| `util/RatingCoercion.kt` | `coerceRating(Int)` → зажимает в 0..10 | `RatingCoercionTest` |
| `util/ReviewQueryFilter.kt` | `filterReviews(...)` → фильтр по бренду/названию | `ReviewQueryFilterTest` |
| `mapper/TrackedReviewMapper.kt` | `CheckieReview.toTrackedReview()` | `TrackedReviewMapperTest` |

## Известное ограничение: read-back после создания

`ReviewRepository.createReview()` возвращает `Unit` — id созданной записи не возвращается.
Чтобы вернуть ассистенту сохранённый отзыв, `trackReview` после создания читает из
репозитория свежайшую запись с совпадающими `productName`, `productBrand` и `rating`.
При коллизии в рамках одной миллисекунды (маловероятно на пути ассистента) будет возвращён
один из совпадающих отзывов. Если совпадающая запись не найдена — бросается ошибка.

## Как протестировать

Доступ к Gemini App Functions закрыт EAP, но фичу можно проверить end-to-end без
ассистента — функции исполняются напрямую через `adb`. Стратегия от дешёвого к полному.

### Уровень 0 — юнит-тесты чистой логики (без устройства)

`coerceRating`, `filterReviews`, `toTrackedReview` покрыты JUnit5:

```
./gradlew :feature:appfunctions:impl:testDebugUnitTest
```

### Уровень 1 — эмулятор Android 16 + adb (основной способ)

1. Поднять эмулятор с system image **API 36.1+**. На ранних 36.x команда
   `cmd app_function` может вернуть `No shell command implementation`.

2. Убедиться, что KSP-метаданные попали в APK:

   ```
   unzip -l app/build/outputs/apk/debug/app-debug.apk | grep app_functions
   ```

   Должны быть `app_functions.xml` и `app_functions_v2.xml`. Если их нет — функции не
   зарегистрируются (проблема со сборкой/агрегацией).

3. Установить debug-сборку и посмотреть зарегистрированные функции (пакет debug — с
   суффиксом `.dev`):

   ```
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   adb shell cmd app_function list-app-functions | grep -i checkie
   ```

   В выводе — стабильные `functionId` (полностью квалифицированное имя класса):
   `com.perfomer.checkielite.feature.appfunctions.CheckieAppFunctions#trackReview` и
   `…CheckieAppFunctions#findReviews`. Индексация через AppSearch может занять секунды
   после установки; если пусто — подождать/перезапустить приложение.

4. Исполнить функцию напрямую (эмулирует вызов ассистентом). Параметры в AppSearch-стиле:
   каждый аргумент оборачивается в массив из одного элемента. `appFunctionContext`
   передавать не нужно — его подставляет система:

   ```
   adb shell cmd app_function execute-app-function \
     --package com.perfomer.checkielite.dev \
     --function "com.perfomer.checkielite.feature.appfunctions.CheckieAppFunctions#trackReview" \
     --parameters '{"productName":["Cola"],"productBrand":["Darkside"],"rating":[10]}'
   ```

   ```
   adb shell cmd app_function execute-app-function \
     --package com.perfomer.checkielite.dev \
     --function "com.perfomer.checkielite.feature.appfunctions.CheckieAppFunctions#findReviews" \
     --parameters '{"query":["Darkside"]}'
   ```

   Работает без EAP, потому что `adb`/shell обладает привилегией `EXECUTE_APP_FUNCTIONS`
   — отдельный caller (Gemini) не требуется.

5. Открыть приложение и убедиться, что отзыв «Darkside / Cola / 10» появился в списке
   (это обычный отзыв, созданный через `createReview`).

### Уровень 2 — локальный JVM-тест @AppFunction-методов

Покрывает сами `trackReview`/`findReviews` (а не только чистые хелперы), без устройства.

`CheckieAppFunctionsTest` (`src/test/.../CheckieAppFunctionsTest.kt`) создаёт
`CheckieAppFunctions` с фейковым in-memory `ReviewRepository` и фейковым
`AppFunctionContext` (методы его не используют) и проверяет: создание отзыва и возврат
сгенерированного `id`, коэрсинг `rating 15 → 10`, исключение на пустом `productName`,
фильтрацию `findReviews` по query и возврат всех без query. Запуск:

```
./gradlew :feature:appfunctions:impl:testDebugUnitTest
```

Важно: `AppFunctionTestRule` (упоминается в части материалов про App Functions) в
`1.0.0-alpha09` **отсутствует** — он был в alpha03 и удалён. Поэтому тестируем класс
функций напрямую через фейки, без Robolectric и без тест-артефакта. `AppFunctionContext`
в alpha09 — это интерфейс с единственным `getContext(): Context`.

### Уровень 3 (опционально) — тестовый agent-app

Официальный сэмпл [github.com/android/appfunctions](https://github.com/android/appfunctions)
содержит приложение-агент, обнаруживающее и вызывающее функции через `AppFunctionManager`
— ближе всего к реальному сценарию Gemini.

## Статус и стабильность API

- Библиотека: `androidx.appfunctions:1.0.0-alpha09` — preview-статус, сигнатуры могут меняться.
- Gemini App Functions: закрытый EAP, доступен не на всех устройствах.
- Рекомендуется следить за релизами библиотеки перед обновлением targetSdk.
