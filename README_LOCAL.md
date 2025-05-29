# Локальный запуск AIUEducation

Этот документ описывает как запустить проект локально с помощью Docker Compose.

## Предварительные требования

- Docker
- Docker Compose
- Git

## Варианты запуска

### 1. Простой запуск (рекомендуется)

```bash
# Запуск только приложения и базы данных
docker-compose -f docker-compose.simple.yml up --build

# Или в фоновом режиме
docker-compose -f docker-compose.simple.yml up -d --build
```

**Доступные сервисы:**
- Приложение: http://localhost:8080
- PostgreSQL: localhost:5432

### 2. Быстрый запуск для разработки

```bash
# Запуск с hot reload и pgAdmin
docker-compose -f docker-compose.dev.yml up --build

# Или в фоновом режиме
docker-compose -f docker-compose.dev.yml up -d --build
```

**Доступные сервисы:**
- Приложение: http://localhost:8080
- pgAdmin: http://localhost:5050 (admin@example.com / admin)
- PostgreSQL: localhost:5432

### 3. Запуск с Nginx (полная конфигурация)

```bash
# Запуск с nginx прокси
docker-compose -f docker-compose.local.yml up --build

# Или в фоновом режиме
docker-compose -f docker-compose.local.yml up -d --build
```

**Доступные сервисы:**
- Nginx (фронт): http://localhost:8080
- Приложение (прямой доступ): http://localhost:8081
- PostgreSQL: localhost:5435

### 4. Продакшн конфигурация

```bash
# Запуск продакшн версии (требует .env файл)
docker-compose up --build
```

## Настройка базы данных

### Подключение к PostgreSQL

**Для простого запуска (docker-compose.simple.yml):**
- Host: localhost
- Port: 5432
- Database: aiueducation_db
- Username: aiueducation_user
- Password: aiueducation_pass

**Для разработки (docker-compose.dev.yml):**
- Host: localhost
- Port: 5432
- Database: aiueducation_db
- Username: aiueducation_user
- Password: aiueducation_pass

**Для локального тестирования (docker-compose.local.yml):**
- Host: localhost
- Port: 5435
- Database: aiueducation_db
- Username: aiueducation_user
- Password: aiueducation_pass

### Использование pgAdmin

1. Откройте http://localhost:5050
2. Войдите с учетными данными: admin@example.com / admin
3. Добавьте новый сервер:
   - Name: AIUEducation DB
   - Host: db
   - Port: 5432
   - Username: aiueducation_user
   - Password: aiueducation_pass

## Полезные команды

### Просмотр логов

```bash
# Все сервисы
docker-compose -f docker-compose.dev.yml logs -f

# Только приложение
docker-compose -f docker-compose.dev.yml logs -f app

# Только база данных
docker-compose -f docker-compose.dev.yml logs -f db
```

### Остановка сервисов

```bash
# Остановка
docker-compose -f docker-compose.dev.yml down

# Остановка с удалением volumes (ВНИМАНИЕ: удалит данные БД)
docker-compose -f docker-compose.dev.yml down -v
```

### Пересборка приложения

```bash
# Пересборка только приложения
docker-compose -f docker-compose.dev.yml up --build app

# Полная пересборка
docker-compose -f docker-compose.dev.yml build --no-cache
```

### Выполнение команд в контейнере

```bash
# Подключение к контейнеру приложения
docker exec -it aiueducation-app-dev sh

# Подключение к базе данных
docker exec -it aiueducation-db-dev psql -U aiueducation_user -d aiueducation_db
```

## Отладка

### Проверка состояния сервисов

```bash
docker-compose -f docker-compose.dev.yml ps
```

### Проверка health check базы данных

```bash
docker exec aiueducation-db-dev pg_isready -U aiueducation_user -d aiueducation_db
```

### Просмотр переменных окружения

```bash
docker exec aiueducation-app-dev env
```

## Структура файлов

- `docker-compose.dev.yml` - Конфигурация для разработки с hot reload
- `docker-compose.local.yml` - Конфигурация с nginx для локального тестирования
- `docker-compose.yml` - Продакшн конфигурация
- `Dockerfile.dev` - Dockerfile для разработки
- `Dockerfile` - Продакшн Dockerfile
- `init-db.sql` - Скрипт инициализации базы данных
- `src/main/resources/application-dev.properties` - Настройки для разработки

## Решение проблем

### Порты заняты

Если порты 8080, 5432 или 5050 заняты, измените их в соответствующем docker-compose файле.

### Проблемы с правами доступа

```bash
# Исправление прав на uploads директорию
sudo chown -R $USER:$USER uploads/
```

### Очистка Docker

```bash
# Удаление неиспользуемых образов и контейнеров
docker system prune -a

# Удаление volumes (ВНИМАНИЕ: удалит все данные)
docker volume prune
```

## Учетные данные по умолчанию

- **Приложение**: admin / admin
- **pgAdmin**: admin@example.com / admin
- **PostgreSQL**: aiueducation_user / aiueducation_pass 