# AIUEducation - Платформа онлайн-обучения

![AIUEducation Logo](https://img.shields.io/badge/AIUEducation-Online%20Learning-blue)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)

## 🎓 О проекте

AIUEducation - это современная платформа онлайн-обучения, разработанная для домена **freelance.com.kz**. Платформа предоставляет полнофункциональную систему управления курсами, аналогичную Udemy, с поддержкой видеоуроков, системы рейтингов, комментариев и отслеживания прогресса.

## ✨ Основные возможности

### 👨‍🎓 Для студентов
- 📚 Просмотр каталога курсов с фильтрацией по категориям
- 🎥 Просмотр видеоуроков с отслеживанием прогресса
- ⭐ Система рейтингов и отзывов
- 💬 Комментарии к курсам
- 📖 Добавление курсов в избранное
- 👁️ Отслеживание просмотров

### 👨‍🏫 Для преподавателей
- 📝 Создание и редактирование курсов
- 🎬 Загрузка видеоматериалов
- 📋 Управление уроками курса
- 📊 Просмотр статистики курсов
- 🎯 Полный контроль над своими курсами

### 🔧 Технические возможности
- 🔐 Система аутентификации и авторизации
- 📱 Адаптивный дизайн (Tailwind CSS)
- 🚀 Высокая производительность
- 📈 Мониторинг и метрики (Spring Actuator)
- 🐳 Готовность к контейнеризации (Docker)
- 🔒 Безопасность (HTTPS, CSRF защита)

## 🛠️ Технологический стек

### Backend
- **Java 17** - Основной язык программирования
- **Spring Boot 3.5.0** - Фреймворк для разработки
- **Spring Security** - Безопасность и аутентификация
- **Spring Data JPA** - Работа с базой данных
- **Hibernate** - ORM
- **Spring Actuator** - Мониторинг и метрики

### Frontend
- **Thymeleaf** - Шаблонизатор
- **Tailwind CSS** - CSS фреймворк
- **Font Awesome** - Иконки
- **JavaScript** - Интерактивность

### База данных
- **PostgreSQL 15** - Основная база данных
- **H2** - База данных для тестирования

### DevOps
- **Docker** - Контейнеризация
- **Docker Compose** - Оркестрация контейнеров
- **Nginx** - Веб-сервер и прокси
- **Maven** - Сборка проекта

## 🚀 Быстрый старт

### Предварительные требования
- Java 17+
- Maven 3.6+
- PostgreSQL 15+
- Docker & Docker Compose (для продакшена)

### Локальная разработка

1. **Клонирование репозитория**
```bash
git clone <repository-url>
cd aiueducation
```

2. **Настройка базы данных**
```bash
# Создание базы данных PostgreSQL
createdb demo
createuser demo --password
```

3. **Настройка конфигурации**
```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/demo
spring.datasource.username=demo
spring.datasource.password=demo
```

4. **Запуск приложения**
```bash
mvn spring-boot:run
```

5. **Открытие в браузере**
```
http://localhost:8080
```

### Продакшен деплой

Для деплоя на **freelance.com.kz** используйте автоматический скрипт:

```bash
# Сделать скрипт исполняемым
chmod +x deploy.sh

# Запустить деплой
./deploy.sh deploy
```

Подробная инструкция по деплою: [DEPLOYMENT.md](DEPLOYMENT.md)

## 📁 Структура проекта

```
aiueducation/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── config/          # Конфигурация Spring
│   │   │   ├── controller/      # REST контроллеры
│   │   │   ├── model/           # JPA сущности
│   │   │   ├── repository/      # Репозитории данных
│   │   │   └── service/         # Бизнес-логика
│   │   └── resources/
│   │       ├── static/          # Статические файлы
│   │       ├── templates/       # Thymeleaf шаблоны
│   │       ├── application.properties
│   │       └── application-prod.properties
│   └── test/                    # Тесты
├── docker-compose.prod.yml      # Docker Compose для продакшена
├── Dockerfile                   # Docker образ
├── nginx.conf                   # Конфигурация Nginx
├── deploy.sh                    # Скрипт деплоя
├── DEPLOYMENT.md               # Инструкция по деплою
└── README.md                   # Этот файл
```

## 🎯 Основные модели данных

### User (Пользователь)
- Аутентификация и авторизация
- Роли: STUDENT, TEACHER
- Профиль пользователя

### Course (Курс)
- Информация о курсе
- Категории и описания
- Связь с создателем

### Lesson (Урок)
- Уроки внутри курса
- Видеоматериалы
- Порядок уроков

### Rating (Рейтинг)
- Оценки курсов
- Комментарии к оценкам
- Средний рейтинг

### Comment (Комментарий)
- Комментарии к курсам
- Система обратной связи

### CourseView (Просмотры)
- Отслеживание уникальных просмотров
- Статистика по IP и пользователям

## 🔧 API Endpoints

### Публичные
- `GET /` - Главная страница
- `GET /courses` - Список курсов
- `GET /courses/{id}` - Страница курса
- `GET /login` - Страница входа
- `GET /register` - Страница регистрации

### Для аутентифицированных пользователей
- `POST /courses/{id}/like` - Лайк курса
- `POST /courses/{id}/favorite` - Добавить в избранное
- `POST /courses/{id}/comments` - Добавить комментарий
- `POST /courses/{id}/rate` - Оценить курс

### Для преподавателей
- `GET /courses/new` - Форма создания курса
- `POST /courses` - Создать курс
- `GET /courses/{id}/edit` - Форма редактирования
- `POST /courses/{id}/edit` - Обновить курс
- `POST /courses/{id}/lessons` - Добавить урок

### Мониторинг
- `GET /actuator/health` - Проверка здоровья
- `GET /actuator/metrics` - Метрики приложения
- `GET /actuator/info` - Информация о приложении

## 🔒 Безопасность

- **HTTPS** - Обязательное шифрование в продакшене
- **CSRF Protection** - Защита от межсайтовых запросов
- **Rate Limiting** - Ограничение частоты запросов
- **SQL Injection Protection** - Использование JPA/Hibernate
- **XSS Protection** - Экранирование в шаблонах
- **Security Headers** - Настройка заголовков безопасности

## 📊 Мониторинг и логирование

### Метрики
- Здоровье приложения
- Использование памяти
- Статистика базы данных
- Время отклика

### Логи
- Логи приложения
- Логи безопасности
- Логи базы данных
- Логи веб-сервера

## 🤝 Участие в разработке

1. Форкните репозиторий
2. Создайте ветку для новой функции (`git checkout -b feature/amazing-feature`)
3. Зафиксируйте изменения (`git commit -m 'Add amazing feature'`)
4. Отправьте в ветку (`git push origin feature/amazing-feature`)
5. Откройте Pull Request

## 📝 Лицензия

Этот проект лицензирован под MIT License - см. файл [LICENSE](LICENSE) для деталей.

## 📞 Контакты

- **Домен**: [freelance.com.kz](https://freelance.com.kz)
- **Email**: support@freelance.com.kz
- **Документация**: [DEPLOYMENT.md](DEPLOYMENT.md)

## 🙏 Благодарности

- Spring Boot команде за отличный фреймворк
- Tailwind CSS за современный CSS фреймворк
- PostgreSQL за надежную базу данных
- Docker за упрощение деплоя

---

**AIUEducation** - Образование будущего уже сегодня! 🚀 