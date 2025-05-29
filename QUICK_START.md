# 🚀 Быстрый старт AIUEducation

## Самый простой способ запуска

1. **Убедитесь, что у вас установлены Docker и Docker Compose**

2. **Запустите скрипт:**
   ```bash
   ./start-local.sh
   ```
   Выберите вариант `1` (Простой запуск)

3. **Откройте браузер:**
   ```
   http://localhost:8080
   ```

## Альтернативный способ

```bash
# Запуск одной командой
docker-compose -f docker-compose.simple.yml up --build
```

## Что запускается?

- ✅ **Spring Boot приложение** на порту 8080
- ✅ **PostgreSQL база данных** на порту 5432
- ✅ **Adminer** (админка БД) на порту 8090
- ✅ **Автоматическая инициализация БД**

## Доступные сервисы

- **Приложение**: http://localhost:8080
- **Adminer** (как Django Admin): http://localhost:8090
- **База данных**: localhost:5432

## Учетные данные

- **Приложение**: admin / admin
- **База данных**: aiueducation_user / aiueducation_pass

## Настройка Adminer

1. Откройте http://localhost:8090
2. Выберите **PostgreSQL**
3. Заполните данные:
   - **Сервер**: db
   - **Пользователь**: aiueducation_user
   - **Пароль**: aiueducation_pass
   - **База данных**: aiueducation_db
4. Нажмите **Войти**

## Функции профиля

После входа в приложение доступны:
- 📊 **Профиль пользователя** - /profile
- ❤️ **Лайкнутые курсы** - /profile/liked
- ⭐ **Избранные курсы** - /profile/favorites
- 📚 **Мои курсы** (для учителей) - /profile/my-courses

## Проверка работы

```bash
# Проверка health endpoint
curl http://localhost:8080/actuator/health

# Ожидаемый ответ:
# {"status":"UP","components":{"db":{"status":"UP"},...}}
```

## Остановка

```bash
# Остановка контейнеров
docker-compose -f docker-compose.simple.yml down

# Остановка с удалением данных БД
docker-compose -f docker-compose.simple.yml down -v
```

## Решение проблем

### Порты заняты
Если порты 8080, 5432 или 8090 заняты, измените их в файле `docker-compose.simple.yml`

### Очистка Docker
```bash
docker system prune -a
```

---

📖 **Подробная документация**: [README_LOCAL.md](README_LOCAL.md) 